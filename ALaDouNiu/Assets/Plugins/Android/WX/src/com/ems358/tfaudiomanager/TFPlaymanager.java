package com.ems358.tfaudiomanager;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jenuce on 2016/11/26.
 */

public class TFPlaymanager implements TFAndroidPlayerInterface {
    private ExecutorService queue_ = Executors.newSingleThreadExecutor();
    private Speex coder_;

    private String current_file_;
    private List<String> play_files_=new ArrayList();

    private TFAndroidPlayer player_ = new TFAndroidPlayer();

    private TFPlaymanagerInterface delegate_;

    public TFPlaymanager(){
        player_.setDelegate(this);
    }

    public void setDelegate(TFPlaymanagerInterface delegate) {
        this.delegate_ = delegate;
    }
    public boolean isPlaying(){
        return !player_.bstop();
    }
    public void addToFirst(final String file) {
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                play_files_.add(0,file);
                if (player_.bstop()){
                    player_.pass();
                    player_.start();
                }else{
                    player_.pass();
                }

                Log.e("TFPlaymanager","addToFirst");
//                if (play_files_.size()==1){
//                    playerOver();
//                }
            }
        });

    }

    public void addPlayFiles(final List<String> array) {
        queue_.execute(new Runnable() {
            @Override
            public void run() {
//                for (int i = 0;i<array.size();i++){
//                    if (play_files_.contains(array.get(i))){
//
//                        continue;
//                    }else{
//                        play_files_.add(array.get(i));
//                    }
//                }
                play_files_.addAll(array);
                if (player_.bstop()){
                    player_.start();
                }
                Log.e("TFPlaymanager","addPlayFiles");
//                if (play_files_.size()==array.size()){
//                    playerOver();
//                }
            }
        });
    }

    public void addPlayFile(final String file) {
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                if (play_files_.contains(file)){
                    return;
                }
                play_files_.add(file);
                if (player_.bstop()){
                    player_.start();
                }
                Log.e("TFPlaymanager","addPlayFile");
//                if (play_files_.size()==1){
//                    playerOver();
//                }
            }
        });
    }

    public void pass() {
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                player_.pass();
            }
        });
    }

    public void stop() {
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                play_files_.clear();
                player_.stop();
            }
        });
    }

    private void convertOggToPCMWithData() {
        int put_count = 0;

        byte[] header = new byte[2048];
        byte[] payload = new byte[65536];
        final int OGG_HEADERSIZE = 27;
        final int OGG_SEGOFFSET = 26;
        final String OGGID = "OggS";
        int segments = 0;
        int curseg = 0;
        int bodybytes = 0;
        int decsize = 0;
        int packetNo = 0;
        coder_ = new Speex();
        coder_.init();

        int origchksum;
        int chksum;
        RandomAccessFile dis = null;

        try {
            dis = new RandomAccessFile(current_file_, "r");
            while (true) {
                dis.readFully(header, 0, OGG_HEADERSIZE);
                origchksum = readInt(header, 22);
                readLong(header, 6);
                header[22] = 0;
                header[23] = 0;
                header[24] = 0;
                header[25] = 0;
                chksum = OggCrc.checksum(0, header, 0, OGG_HEADERSIZE);

                // make sure its a OGG header
                if (!OGGID.equals(new String(header, 0, 4))) {
                    System.err.println("missing ogg id!");
                    return;
                }

				/* how many segments are there? */
                segments = header[OGG_SEGOFFSET] & 0xFF;
                dis.readFully(header, OGG_HEADERSIZE, segments);
                chksum = OggCrc.checksum(chksum, header, OGG_HEADERSIZE, segments);

                /* decode each segment, writing output to wav */
                for (curseg = 0; curseg < segments; curseg++) {

					/* get the number of bytes in the segment */
                    bodybytes = header[OGG_HEADERSIZE + curseg] & 0xFF;
                    if (bodybytes == 255) {
                        System.err.println("sorry, don't handle 255 sizes!");
                        return;
                    }
                    dis.readFully(payload, 0, bodybytes);
                    chksum = OggCrc.checksum(chksum, payload, 0, bodybytes);

					/* decode the segment */
                    /* if first packet, read the Speex header */
                    if (packetNo == 0 || packetNo == 1) {
                        packetNo++;
                        continue;
                    } else {

						/* get the amount of decoded data */
                        short[] decoded = new short[160];
                        if ((decsize = coder_.decode(payload, decoded, 160)) > 0) {
                            player_.putMediaData(decoded, decsize);
                            put_count++;
                        }
                        packetNo++;
                    }
                }
                if (chksum != origchksum)
                    throw new IOException("Ogg CheckSums do not match");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (put_count==0){
                Log.e("TFPlaymanager","put_cout==0 playover");
                playerOver();
            }
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*private boolean readSpeexHeader(final byte[] packet, final int offset, final int bytes, boolean init) throws Exception {
        if (bytes != 80) {
            System.out.println("Oooops");
            return false;
        }
        if (!"Speex   ".equals(new String(packet, offset, 8))) {
            return false;
        }
        int mode = packet[40 + offset] & 0xFF;
        int sampleRate = readInt(packet, offset + 36);
        int channels = readInt(packet, offset + 48);
        int nframes = readInt(packet, offset + 64);
        int frameSize = readInt(packet, offset + 56);
        System.out.println("mode=" + mode + " sampleRate==" + sampleRate + " channels=" + channels + "nframes=" + nframes + "framesize="
                + frameSize);

        if (init) {
            // return speexDecoder.init(mode, sampleRate, channels, enhanced);
            return true;
        } else {
            return true;
        }
    }*/

    private static int readInt(final byte[] data, final int offset) {
		/*
		 * no 0xff on the last one to keep the sign
		 */
        return (data[offset] & 0xff) | ((data[offset + 1] & 0xff) << 8) | ((data[offset + 2] & 0xff) << 16) | (data[offset + 3] << 24);
    }

    private static long readLong(final byte[] data, final int offset) {
		/*
		 * no 0xff on the last one to keep the sign
		 */
        return (data[offset] & 0xff) | ((data[offset + 1] & 0xff) << 8) | ((data[offset + 2] & 0xff) << 16)
                | ((data[offset + 3] & 0xff) << 24) | ((data[offset + 4] & 0xff) << 32) | ((data[offset + 5] & 0xff) << 40)
                | ((data[offset + 6] & 0xff) << 48) | (data[offset + 7] << 56);
    }

    private static int readShort(final byte[] data, final int offset) {
		/*
		 * no 0xff on the last one to keep the sign
		 */
        return (data[offset] & 0xff) | (data[offset + 1] << 8);
    }

    @Override
    public void playerError(String error) {

    }

    @Override
    public void playerOver() {
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                if (player_.bstop()) {
                    if (delegate_ != null && current_file_!=null) {
                        delegate_.OnPlayOver(current_file_);
                    }
                    if (delegate_ != null) {
                        delegate_.OnWillStop();
                    }
                    current_file_ = null;
                    play_files_.clear();
                    return;
                }
                if (delegate_ != null && current_file_!=null) {
                    delegate_.OnPlayOver(current_file_);
                }
                if (play_files_.size()==0){
                    current_file_ = null;
                    player_.stop();
                    return;
                }
                current_file_ = play_files_.remove(0);
                if (delegate_ != null) {
                    delegate_.OnWillPlay(current_file_);
                }
                convertOggToPCMWithData();

            }
        });
    }
}
