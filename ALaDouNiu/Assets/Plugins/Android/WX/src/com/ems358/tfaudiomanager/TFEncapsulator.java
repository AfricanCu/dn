package com.ems358.tfaudiomanager;

import android.util.Log;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jenuce on 2016/11/25.
 */

public class TFEncapsulator {
    private String out_file_name_;
    private Speex code;
    private OggSpeexWriter ogg_write = null;
    private ExecutorService queue_ = Executors.newSingleThreadExecutor();
    private List<ReadData> ring_buffer_ = new ArrayList<>();

    private byte[] temp_spx = new byte[320];

    private TFEncapsulatingInterface delegate_ = null;
    private double pcm_total_ = 0;


    public void setDelegate_(TFEncapsulatingInterface delegate_) {
        this.delegate_ = delegate_;
    }

    public void setFileName(String name) {
        out_file_name_ = name;
    }

    public void inputPCMDataFromBuffer(short[] data,int len) {
        ReadData rd = new ReadData();
        synchronized (ring_buffer_) {
            rd.size = len;
            System.arraycopy(data, 0, rd.ready, 0, len);
            ring_buffer_.add(rd);
        }
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                if (ogg_write != null) {
                    ReadData frd = null;
                    synchronized (ring_buffer_) {
                        frd = ring_buffer_.remove(0);
                    }
                    pcm_total_ += frd.size;
                    java.util.Arrays.fill(temp_spx, (byte) 0);
                    int psx_len = code.encode(frd.ready, 0, temp_spx, frd.size);
                    try {
                        ogg_write.writePacket(temp_spx, 0, psx_len);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void stopEncapsulating(final boolean bCancel) {
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                if (ogg_write != null) {
                    try {
                        ogg_write.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        ogg_write = null;
                    }

                }
                if (delegate_ != null) {
                    delegate_.encapsulatingOver(out_file_name_, pcm_total_ / 8 / 1000, bCancel);
                    Log.e("TFEncapsulator",String.format("%f",pcm_total_ / 8 / 1000));
                    pcm_total_ = 0;
                }
            }
        });
    }

    public void prepareForEncapsulating() {
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                code = new Speex();
                code.init();
                pcm_total_ = 0;
                ogg_write = new OggSpeexWriter(0, 8000, 1, 1, true);
                try {
                    ogg_write.open(out_file_name_);
                    ogg_write.writeHeader("tf");
                } catch (IOException e) {
                    ogg_write = null;
                    e.printStackTrace();
                }
            }
        });
    }

    class ReadData {
        private int size;
        private short[] ready = new short[320 / 2];
    }
}
