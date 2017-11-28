using UnityEngine;
using System.Collections;
using LuaInterface;

public class LocationTool : MonoBehaviour {

    public static LocationTool Instance
    {
        get
        {
            if (null == _instance)
            {
                GameObject go = new GameObject();
                go.name = typeof(LocationTool).Name;
                go.AddComponent<LocationTool>();
            }
            return _instance;
        }
    }

    private static LocationTool _instance;

    void Awake()
    {
        if (null != _instance)
        {
            Destroy(this);
        }
        else
        {
            _instance = this;
            DontDestroyOnLoad(this.gameObject);
        }
    }

#if UNITY_IPHONE
	[System.Runtime.InteropServices.DllImport("__Internal")]
	private static extern void requestingLocation();
#endif

    private LuaFunction reqLocCallBack = null;
    /// <summary>
    /// 拉取登陆授权
    /// </summary>
    /// <param name="authRequest">登陆名</param>
    /// <param name="appID"></param>
    /// <param name="sMark">标识，随便填</param>
    public void ReqLoc(LuaFunction callback)
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call("requestingLocation");
#elif UNITY_IPHONE
		requestingLocation();
#endif
        if (reqLocCallBack != null)
        {
            reqLocCallBack.Dispose();
        }
        reqLocCallBack = callback;
    }

    public void ReqLocRtn(string srtn)
    {
        Debug.Log("ReqLocRtn:" + srtn.ToString());
        if (reqLocCallBack != null)
        {
            reqLocCallBack.BeginPCall();
            reqLocCallBack.Push(srtn);
            reqLocCallBack.PCall();
            reqLocCallBack.EndPCall();
            reqLocCallBack.Dispose();
        }
    }

#if UNITY_IPHONE
	[System.Runtime.InteropServices.DllImport("__Internal")]
	private static extern void battery_Unity();
#endif

    private LuaFunction batteryCallBack = null;
    /// <summary>
    /// 拉取登陆授权
    /// </summary>
    /// <param name="authRequest">登陆名</param>
    /// <param name="appID"></param>
    /// <param name="sMark">标识，随便填</param>
    public void Battery_Unity(LuaFunction callback)
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call("battery_Unity");
#elif UNITY_IPHONE
		battery_Unity();
#endif
        if (batteryCallBack != null)
        {
            batteryCallBack.Dispose();
        }
        batteryCallBack = callback;
    }

    public void BatteryRtn(string srtn)
    {
        Debug.Log("BatteryRtn:" + srtn.ToString());
        if (batteryCallBack != null)
        {
            batteryCallBack.BeginPCall();
            batteryCallBack.Push(srtn);
            batteryCallBack.PCall();
            batteryCallBack.EndPCall();
            batteryCallBack.Dispose();
        }
    }


#if UNITY_IPHONE
	[System.Runtime.InteropServices.DllImport("__Internal")]
	private static extern void taiJiDun_Unity(string config);
#endif

    private LuaFunction taiJiDunCallBack = null;
    /// <summary>
    /// 拉取登陆授权
    /// </summary>
    /// <param name="authRequest">登陆名</param>
    /// <param name="appID"></param>
    /// <param name="sMark">标识，随便填</param>
    public void TaiJiDun_Unity(string config, LuaFunction callback)
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call("taiJiDun_Unity", config);
#elif UNITY_IPHONE
		taiJiDun_Unity(config);
#endif
        if (taiJiDunCallBack != null)
        {
            taiJiDunCallBack.Dispose();
        }
        taiJiDunCallBack = callback;
    }

    public void TaiJiDunRtn(string srtn)
    {
        Debug.Log("TaiJiDunRtn:" + srtn.ToString());
        if (taiJiDunCallBack != null)
        {
            taiJiDunCallBack.BeginPCall();
            taiJiDunCallBack.Push(srtn);
            taiJiDunCallBack.PCall();
            taiJiDunCallBack.EndPCall();
            taiJiDunCallBack.Dispose();
        }
    }

}
