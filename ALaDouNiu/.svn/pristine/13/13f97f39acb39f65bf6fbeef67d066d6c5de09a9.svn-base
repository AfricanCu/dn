package com.ems358.sdk.map;

import android.content.Context;
import android.location.Location;

/**
 * author：hcqi .
 * des:
 * email:hechuanqi.top@gmail.com
 * date: 2017/8/29
 */

public interface LocationManager<T extends Location> {
    void init(Context context);

    void startLocation(LocationListener<T> locationListener);

    void startLocation();

    void setListener(LocationListener<T> listener);

    void stopLocation();

    void onDestroy();
}
