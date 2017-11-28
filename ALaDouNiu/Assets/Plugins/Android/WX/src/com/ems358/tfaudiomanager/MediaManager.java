package com.ems358.tfaudiomanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by Administrator on 2015/12/21.
 */
public class MediaManager implements SensorEventListener {
    private static MediaManager ourInstance;
    private Context context;
    private AudioManager mAudioManager;
    private  boolean bRing =true;
    private  boolean isLocalmediaPlaying = false;
    private Sensor sensor;

    private MediaManager(Context context) {
        this.context = context.getApplicationContext();
        mAudioManager = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE));
        SensorManager sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

    public static MediaManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new MediaManager(context.getApplicationContext());
        }
        return ourInstance;
    }


    public void setBluetoothOn(){
        mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        mAudioManager.startBluetoothSco();
        mAudioManager.setBluetoothScoOn(true);
    }

    public void setHeadsetOn(){
        if(isLocalmediaPlaying){
            mAudioManager.setMode(AudioManager.MODE_NORMAL);
        }else{
            mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        }
        mAudioManager.stopBluetoothSco();
        mAudioManager.setBluetoothScoOn(false);
        mAudioManager.setSpeakerphoneOn(false);
    }

    /**
     * 扬声器与听筒切换
     */
    public void setSpeakerphoneOn(){
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        mAudioManager.stopBluetoothSco();
        mAudioManager.setBluetoothScoOn(false);
        mAudioManager.setSpeakerphoneOn(true);
    }



    public boolean isSpeakerOn() {
        return mAudioManager.isSpeakerphoneOn();
    }

    public boolean isHeadsetOn(){
//        IntentFilter iFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
//        Intent iStatus = context.registerReceiver(null, iFilter);
//        return iStatus.getIntExtra("state",0) ==1;
        return mAudioManager.isWiredHeadsetOn();
    }

    public boolean isBluetoothA2dpOn(){
        return mAudioManager.isBluetoothA2dpOn();
    }


    public void adjustSoftwareVolume(boolean isAdd) {
        int audioType = bRing ? AudioManager.STREAM_MUSIC: AudioManager.STREAM_VOICE_CALL;
        if(isAdd){
            mAudioManager.adjustStreamVolume(
                    audioType,
                    AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);

        }else {
            mAudioManager.adjustStreamVolume(
                    audioType,
                    AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
        }
    }



    class HeadsetReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                //插入和拔出耳机会触发此广播
                case Intent.ACTION_HEADSET_PLUG:
                    int state = intent.getIntExtra("state", 0);
                    if (state == 1){
                        setHeadsetOn();
                    } else if (state == 0){
                        setSpeakerphoneOn();
                    }
                    break;
                default:
                    break;
            }
        }
    }
//    class MyPhoneStateListener extends PhoneStateListener{
//        public void onCallStateChanged(int state,String incomingNumber){
//            switch (state){
//                case TelephonyManager.CALL_STATE_IDLE:
//                    break;
//                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    break;
//                case TelephonyManager.CALL_STATE_RINGING:
//                    break;
//            }
//        }
//    }
//    class PhoneServiceReceiver extends BroadcastReceiver{
//        TelephonyManager telephonyManager;
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            MyPhoneStateListener phoneStateListener = new MyPhoneStateListener();
//            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
//        }
//    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float value = event.values[0];
        if (value == sensor.getMaximumRange()) {
            setSpeakerphoneOn();
        } else {
            setHeadsetOn();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
