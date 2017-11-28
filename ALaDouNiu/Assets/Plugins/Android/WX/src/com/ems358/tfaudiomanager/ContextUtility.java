package com.ems358.tfaudiomanager;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by jenuce on 2016/11/29.
 */

public class ContextUtility {
    private static final String TAG = "ContextUtility";


    //    private static Cocos2dxActivity activity_ = null;
    private static Application application = null;
    private static AudioCallBack sAudioCallBack;
    private static Handler sHandler ;

    public static void setApplication(Application app) {
        application = app;
        sHandler = new Handler(application.getMainLooper());
    }

    public static Application getApplication() {
        /*Application application = null;
        Class<?> activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            final Method method2 = activityThreadClass.getMethod(
                    "currentActivityThread", new Class[0]);
            // 得到当前的ActivityThread对象
            Object localObject = method2.invoke(null, (Object[]) null);

            final Method method = activityThreadClass
                    .getMethod("getApplication");
            application = (Application) method.invoke(localObject, (Object[]) null);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if (application == null){

                try {
                    Field appField = activityThreadClass.getDeclaredField("mInitialApplication");
                    final Method method;
                    method = activityThreadClass.getMethod(
                            "currentActivityThread", new Class[0]);
                    // 得到当前的ActivityThread对象
                    Object localObject = method.invoke(null, (Object[]) null);
                    appField.setAccessible(true);
                    application = (Application) appField.get(localObject);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }*/
        return application;
    }

    //    public static void SetMainActivity(Cocos2dxActivity activity) {
//        activity_ = activity;
//    }
//
    public static void runOnUiThread(Runnable runnable) {
//        if (activity_ != null) {
//            activity_.runOnGLThread(runnable);
//        }

        sHandler.post(runnable);

    }

    public static AudioCallBack getAudioCallBack() {
        return sAudioCallBack;
    }

//    public static void call(int state, String data) {
//        if (getAudioCallBack() != null) {
//            getAudioCallBack().call(state, data);
//        } else {
//            Log.e(TAG, "call is null" );
//        }
//    }

    public static void setAudioCallBack(AudioCallBack audioCallBack) {
        sAudioCallBack = audioCallBack;
    }
}
