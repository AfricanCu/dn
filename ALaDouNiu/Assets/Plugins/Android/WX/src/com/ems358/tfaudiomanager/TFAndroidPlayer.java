package com.ems358.tfaudiomanager;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
//import static android.media.AudioTrack.WRITE_BLOCKING;

/**
 * Created by jenuce on 2016/11/26.
 */

public class TFAndroidPlayer {
    private AudioTrack player_;
    private int threadBufferSize_ = 0;
    private int audioRate_=8000;
    private int ptime_ = 20;
    private boolean bpass = false;
    private boolean bstop = true;
    private AtomicInteger task_count = new AtomicInteger(0);
    //private List<Short> pcm_data_ = new ArrayList<>();
    private ExecutorService queue_ =null;

    private TFAndroidPlayerInterface delegate_;

    public TFAndroidPlayer(){
        initTFAndroidPlayer();
    }
    private void initTFAndroidPlayer(){
        int audioFormate = AudioFormat.ENCODING_PCM_16BIT;
        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        player_ = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                audioRate_,
                channelConfig,
                audioFormate,
//                AudioTrack.getMinBufferSize(
//                        audioRate_,
//                        channelConfig, audioFormate
//                ),
                320,
                AudioTrack.MODE_STREAM);
        threadBufferSize_ = 2*audioRate_*ptime_/1000;
        player_.setNotificationMarkerPosition(160);
        player_.setPositionNotificationPeriod(160);

        AudioTrack.OnPlaybackPositionUpdateListener mPositionUpdate = new AudioTrack.OnPlaybackPositionUpdateListener() {

            @Override
            public void onMarkerReached(AudioTrack track) {
            }

            @Override
            public void onPeriodicNotification(AudioTrack track) {
            }
        };
        player_.setPlaybackPositionUpdateListener(mPositionUpdate);
    }
    public void setDelegate(TFAndroidPlayerInterface delegate) {
        this.delegate_ = delegate;
    }
    public void start(){
        if (!bstop())return;
        if (player_==null){
        	initTFAndroidPlayer();
        }
        if (queue_==null){
            queue_ = Executors.newSingleThreadExecutor();
        }
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                task_count.set(0);
                bstop = false;
                bpass = true;
                player_.play();
                putMediaData(null,0);
                if (!MediaManager.getInstance(ContextUtility.getApplication()).isHeadsetOn() &&
                        !MediaManager.getInstance(ContextUtility.getApplication()).isBluetoothA2dpOn()){
                    MediaManager.getInstance(ContextUtility.getApplication()).setSpeakerphoneOn();
                }
            }
        });
    }
    public void stop(){
        if (bstop()) return;
        bstop = true;
        player_.pause();
        player_.flush();
        putMediaData(null,0);
    }
    public void pass(){
        if (bstop()){
            return;
        }
        bpass = true;
        player_.pause();
        player_.flush();
        player_.play();
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                bpass = false;
            }
        });
    }
    public boolean bstop(){
        //return player_==null || player_.getPlayState()==AudioTrack.PLAYSTATE_STOPPED || bstop;
        return bstop;
    }
    public void putMediaData(final short[] data, final int length){
        //pcm_data_.addAll(Arrays.asList(ArrayUtils.toObject(ArrayUtils.subarray(data,0,length))));
        task_count.addAndGet(1);
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                if (bstop){
                    task_count.set(0);
                    if (player_!=null &&player_.getPlayState()==AudioTrack.PLAYSTATE_PLAYING|| player_.getPlayState()==AudioTrack.PLAYSTATE_PAUSED){
                        player_.stop();
                        if (delegate_!=null){
                            delegate_.playerOver();
                        }
                    }else if (player_!=null){
                        if (delegate_!=null){
                            delegate_.playerOver();
                        }
                    }
                    return;
                }
                if ((!bpass) && !bstop && length>0){
                    Log.e("TFAndroidPlayter","player write");
                    int len  = player_.write(data,0,length);
                    while (len>0&& len<length){
                            len += player_.write(data,len,length-len);
                    }
                    try {
                        Thread.sleep(17);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (task_count.decrementAndGet()==0){
                    if (delegate_!=null){
                        delegate_.playerOver();
                    }
                    bpass = false;
                }
            }
        });

    }
}
