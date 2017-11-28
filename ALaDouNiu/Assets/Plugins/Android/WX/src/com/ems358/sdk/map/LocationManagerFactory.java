package com.ems358.sdk.map;

/**
 * author：hcqi .
 * des:
 * email:hechuanqi.top@gmail.com
 * date: 2017/8/29
 */

public class LocationManagerFactory {

    public static LocationManager create() {
        return AMapLocationManager.getInstance();


    }
}
