package com.ems358.tfaudiomanager;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jenuce on 2016/11/26.
 */

public class TFRecordManager implements TFEncapsulatingInterface {
    private TFAndroidRecord recorder_;
    private TFEncapsulator encapsulator_ = new TFEncapsulator();
    private Timer timer_timeout_;
    private Date record_date_;
    private TFRecordManagerInterface delegate_;

    public TFRecordManager() {
        encapsulator_.setDelegate_(this);
    }

    public void setDelegate(TFRecordManagerInterface delegate) {
        this.delegate_ = delegate;
    }

    public void start(String filename) {
        record_date_ = new Date();
        timer_timeout_ = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (delegate_ != null) {
                    delegate_.onRecordTimeout();
                }
            }
        };
        timer_timeout_.schedule(task, 30 * 1000);
        encapsulator_.setFileName(filename);
        recorder_ = new TFAndroidRecord();
        recorder_.StartRecord(encapsulator_);
        if (!recorder_.IsRunning()) {
            if (delegate_ != null) {
                delegate_.onRecordFailed("程序错误，无法继续录音，请重启程序试试");
            }
        }
        if (delegate_ != null) {
            delegate_.onRecordStart();
        }

    }

    public void stop() {
        if (recorder_!=null && recorder_.IsRunning()) {
            recorder_.StopRecord(false);
        }
        if (timer_timeout_!=null){
            timer_timeout_.cancel();
            timer_timeout_ = null;
        }
        recorder_ = null;
    }

    public void cancel() {
        if (recorder_ != null) {
            recorder_.StopRecord(true);
            recorder_ = null;
        }
        if (timer_timeout_!=null){
            timer_timeout_.cancel();
            timer_timeout_ = null;
        }
    }

    @Override
    public void encapsulatingOver(String filename, Double interval, boolean bCancel) {
        if (delegate_!=null){
            delegate_.onRecordStop(filename,record_date_,interval,bCancel);
        }
    }
}
