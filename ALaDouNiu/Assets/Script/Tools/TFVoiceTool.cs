using UnityEngine;
using System.Collections;
using LuaInterface;

public class TFVoiceTool : MonoBehaviour {

    public static TFVoiceTool Instance
    {
        get
        {
            if (null == _instance)
            {
                GameObject go = new GameObject();
                go.name = typeof(TFVoiceTool).Name;
                go.AddComponent<TFVoiceTool>();
            }
            return _instance;
        }
    }

    private static TFVoiceTool _instance;

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
	private static extern void startRecord(string config);
#endif

    public void StartRecord(string config)
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call("startRecord", config);
#elif UNITY_IPHONE
		startRecord(config);
#endif
    }

#if UNITY_IPHONE
	[System.Runtime.InteropServices.DllImport("__Internal")]
	private static extern void stopRecord();
#endif
    public void StopRecord()
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call("stopRecord");
        //AndroidJavaClass jc2 = new AndroidJavaClass("com.ems358.tfaudiomanager.TFAudioManager");
        //  AndroidJavaObject jo2 = jc2.GetStatic<AndroidJavaObject>("s_instance_");
        //jo2.CallStatic("stopRecord");
#elif UNITY_IPHONE
		stopRecord();
#endif
    }

#if UNITY_IPHONE
	[System.Runtime.InteropServices.DllImport("__Internal")]
	private static extern void cancelRecord();
#endif
    public void CancelRecord()
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call("cancelRecord");
#elif UNITY_IPHONE
		cancelRecord();
#endif
    }

#if UNITY_IPHONE
	[System.Runtime.InteropServices.DllImport("__Internal")]
	private static extern void addPlay(string fileUrl);
#endif
    public void AddPlay(string fileUrl)
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call("addPlay", fileUrl);
#elif UNITY_IPHONE
		addPlay(fileUrl);
#endif
    }

#if UNITY_IPHONE
	[System.Runtime.InteropServices.DllImport("__Internal")]
	private static extern void passPlay();
#endif
    public void PassPlay()
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call("passPlay");
#elif UNITY_IPHONE
		passPlay();
#endif
    }

#if UNITY_IPHONE
	[System.Runtime.InteropServices.DllImport("__Internal")]
	private static extern void stopPlay();
#endif
    public void StopPlay()
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call("stopPlay");

#elif UNITY_IPHONE
		stopPlay();
#endif
    }

    private LuaFunction playFunc;

    public void SetMsgCallBack(LuaFunction callback)
    {
        if (playFunc != null)
        {
            playFunc.Dispose();
        }
        playFunc = callback;

    }
    public void PlayFuncMsg(string srtn)
    {
        if(playFunc != null)
        {
            playFunc.BeginPCall();
            playFunc.Push(srtn);
            playFunc.PCall();
            playFunc.EndPCall();
        }
    }

}
