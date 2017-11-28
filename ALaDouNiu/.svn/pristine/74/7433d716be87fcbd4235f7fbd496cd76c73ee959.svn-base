package com.ems358.tfaudiomanager;

/**
 * Created by jenuce on 2016/11/25.
 */
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class TFAndroidRecord {
    private boolean isRecording_=false;
    private AudioRecord producer_ = null;
    private TFAndroidRecordThread producerThread_ = null;
    private int threadBufferSize_ = 0;
    private static final int rate_ = 8000;
    private static final int audioFormate_ = AudioFormat.ENCODING_PCM_16BIT;
    private static final int ptime_ = 20;
    private TFEncapsulator encapsulator_=null;

    public TFAndroidRecord(){
        initTFRecordManager();
    }

    public boolean StartRecord(TFEncapsulator encapsulator){
        if (producer_!=null && producer_.getState() == AudioRecord.RECORDSTATE_RECORDING||isRecording_){
            return true;
        }
        isRecording_ = true;
        producerThread_ = new TFAndroidRecordThread();
        producerThread_.bufferSize = threadBufferSize_;
        producerThread_.start();
        encapsulator_ = encapsulator;
        return true;
    }
    public void StopRecord(boolean b_cancel){
        if (producerThread_ == null) return;
        isRecording_ = false;
        try {
            producerThread_.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (encapsulator_ != null){
            encapsulator_.stopEncapsulating(b_cancel);
        }

        encapsulator_ = null;
        producerThread_ = null;
    }
    public boolean IsRunning(){
        return isRecording_;
    }
    private int initTFRecordManager() {
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioRate = rate_;

        threadBufferSize_ = AudioRecord.getMinBufferSize(audioRate,
                channelConfig,
                audioFormate_);
        producer_ = new AudioRecord(MediaRecorder.AudioSource.MIC,
                audioRate,
                channelConfig,
                audioFormate_,
                threadBufferSize_);

        threadBufferSize_ = 2* rate_ * ptime_ / 1000;
        return 0;
    }

    class TFAndroidRecordThread extends Thread {
        private int bufferSize = 160;

        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            try {
                producer_.startRecording();
                if (encapsulator_ != null && isRecording_){
                    encapsulator_.prepareForEncapsulating();
                }
            }catch (Exception e){
                e.printStackTrace();
                return;
            }

            short[] buffer = new short[bufferSize/2];
            java.util.Arrays.fill(buffer, (byte) 0);
            Log.d("", "AudioProducer buffer size = " + bufferSize/2);
            int len =0;
            while (isRecording_) {
                len = producer_.read(buffer, 0, bufferSize/2);
                if (encapsulator_ != null && isRecording_ && len>0) {
                    encapsulator_.inputPCMDataFromBuffer(buffer,len);
                }
                java.util.Arrays.fill(buffer, (byte) 0);
            }
            Log.d("", "AudioProducerThread end *****************************");
            isRecording_ = false;
            producer_.stop();
        }
    }
}
