package com.ems358.tfaudiomanager;

/**
 * author：hcqi .
 * des:
 * email:hechuanqi.top@gmail.com
 * date: 2017/9/13
 */

public interface AudioCallBack {



    void onWillPlay(int state, String data);

    void onWillStop(int state, String data);

    void onRecordStart(int state, String data);

    void onRecordTimeout(int state, String data);

    void onRecordFailed(int state, String data);

    void onRecordStop(int state, String data);

    int WILL_PLAY = 1;
    int WILL_STOP = 2;
    int RECORD_START = 11;
    int RECORD_TIMEOUT = 12;
    int RECORD_FAILED = 13;
    int RECORD_STOP = 14;
}
