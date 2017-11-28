package com.ems358.tfaudiomanager;

import java.util.Date;

/**
 * Created by jenuce on 2016/11/26.
 */

public interface TFRecordManagerInterface {
    void onRecordStart();
    void onRecordStop(String filepath,Date startDate,Double soundTime,boolean bCancel);
    void onRecordTimeout();
    void onRecordFailed(String failureInfoString);
}
