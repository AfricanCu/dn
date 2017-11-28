package com.ems358.sdk.map;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * author：hcqi .
 * des:定位管理类
 * email:hechuanqi.top@gmail.com
 * date: 2017/8/29
 */

public class AMapLocationManager implements LocationManager<AMapLocation> {

    public static AMapLocationManager sInstance;
    private AMapLocationClient mClient;

    private LocationListener<AMapLocation> mListener;

    public static AMapLocationManager getInstance() {
        if (sInstance == null) {
            synchronized (AMapLocationManager.class) {
                if (sInstance == null) {
                    sInstance = new AMapLocationManager();
                }
            }
        }
        return sInstance;
    }

    private AMapLocationManager() {
    }

    @Override
    public void init(Context context) {
        //构建对象
        mClient = new AMapLocationClient(context);
        //构建配置信息
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);// 可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        option.setGpsFirst(true);// 可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        option.setInterval(1000);// 可选，设置定位间隔。默认为2秒
        option.setNeedAddress(true);// 可选，设置是否返回逆地理地址信息。默认是true
        option.setOnceLocation(false);// 可选，设置是否单次定位。默认是false
        option.setOnceLocationLatest(false);// 可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        // 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        option.setSensorEnable(false);// 可选，设置是否使用传感器。默认是false
        option.setWifiScan(true); // 可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        option.setLocationCacheEnable(true); // 可选，设置是否使用缓存定位，默认为true

        mClient.setLocationOption(option);

    }

    @Override
    public void startLocation(final LocationListener<AMapLocation> listener) {
        if (mClient != null) {
            mClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    listener.onLocationChanged(aMapLocation);
                }
            });
            mClient.startLocation();
        }
    }

    @Override
    public void startLocation() {
        if (mListener == null) {
            throw new NullPointerException("listener null is please call method setListener");
        }
        mClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                mListener.onLocationChanged(aMapLocation);
            }
        });
        mClient.startLocation();
    }

    @Override
    public void setListener(LocationListener<AMapLocation> listener) {
        mListener = listener;
    }

    @Override
    public void stopLocation() {
        if (mClient != null) {
            mClient.stopLocation();
        }
    }

    @Override
    public void onDestroy() {
        if (mClient != null) {
            mClient.onDestroy();

        }
    }

}
