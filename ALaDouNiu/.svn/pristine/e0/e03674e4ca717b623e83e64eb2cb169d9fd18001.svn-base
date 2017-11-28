using UnityEngine;
using System.Collections;
using LuaInterface;

public class PlatformTool : MonoBehaviour
{

    public static PlatformTool Instance
    {
        get
        {
            if (null == _instance)
            {
                GameObject go = new GameObject();
                go.name = typeof(PlatformTool).Name;
                go.AddComponent<PlatformTool>();
            }
            return _instance;
        }
    }

    private static PlatformTool _instance;

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

    private string version = "1.0";

    public string GetVersion()
    {
        return version;
    }

    public int GetPlatformID()
    {
#if UNITY_EDITOR
        return 3;
#elif UNITY_ANDROID
        return 1;
#elif UNITY_IPHONE
        return 2;
#else
        return 0;
#endif
    }

#if UNITY_IPHONE
	[System.Runtime.InteropServices.DllImport("__Internal")]
	private static extern void sendLoginRequest(string wxid);
#endif

    private LuaFunction authCallBack = null;
    /// <summary>
    /// 拉取登陆授权
    /// </summary>
    /// <param name="authRequest">登陆名</param>
    /// <param name="appID"></param>
    /// <param name="sMark">标识，随便填</param>
    public void DoAuth(string authRequest, string appID, string sMark, LuaFunction callback)
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call(authRequest, appID, sMark);
#elif UNITY_IPHONE
		sendLoginRequest(appID);
#endif
        if (authCallBack != null)
        {
            authCallBack.Dispose();
        }
        authCallBack = callback;
    }

    public void AuthRtn(string rtn)
    {
        Debug.Log("AuthRtn:" + rtn.ToString());
        if (authCallBack != null)
        {
            authCallBack.BeginPCall();
            authCallBack.Push(rtn);
            authCallBack.PCall();
            authCallBack.EndPCall();
            authCallBack.Dispose();
        }
    }

#if UNITY_IPHONE
    [System.Runtime.InteropServices.DllImport("__Internal")]
    private static extern void share_Unity(string shareUrl, string wxId);
#endif
    private LuaFunction mWShareCallBack = null;
    public void MWShare(string mWShareQuest, string shareJson, string appID, LuaFunction callBack)
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call(mWShareQuest, shareJson,appID);
#elif UNITY_IPHONE
        share_Unity(shareJson, appID);
#endif
        if (mWShareCallBack != null)
        {
            mWShareCallBack.Dispose();
        }
        this.mWShareCallBack = callBack;
    }

    public void ShareRtn(string rtn)
    {
        Debug.Log("ShareRtn:" + rtn.ToString());
        if (mWShareCallBack != null)
        {
            mWShareCallBack.BeginPCall();
            mWShareCallBack.Push(rtn);
            mWShareCallBack.PCall();
            mWShareCallBack.EndPCall();
            mWShareCallBack.Dispose();
        }
    }

    private LuaFunction mWRoomCallBack = null;
    public void GetMWRoom(string mWRoomQuest, LuaFunction callBack)
    {
        if (mWRoomCallBack != null)
        {
            mWRoomCallBack.Dispose();
        }
        this.mWRoomCallBack = callBack;

#if UNITY_EDITOR
        MWRoomRtn(string.Empty);
#elif UNITY_ANDROID
        MWRoomRtn(string.Empty);
#elif UNITY_IPHONE
        MWRoomRtn(string.Empty);
#endif
    }

    public void ClearMWRoom()
    {

#if UNITY_EDITOR
        return;
#elif UNITY_ANDROID
        return;
#elif UNITY_IPHONE
        return;
#endif

    }

    public void MWRoomRtn(string roomID)
    {
        Debug.Log("MWRoomRtn:" + roomID.ToString());
        if (mWRoomCallBack != null)
        {
            mWRoomCallBack.BeginPCall();
            mWRoomCallBack.Push(roomID);
            mWRoomCallBack.PCall();
            mWRoomCallBack.EndPCall();
            mWRoomCallBack.Dispose();
        }
    }


    private LuaFunction payCallBack = null;

    public void PayRtn(string code)
    {
        Debug.Log("PayRtn:" + code);
        if (payCallBack != null)
        {
            payCallBack.BeginPCall();
            payCallBack.Push(code);
            payCallBack.PCall();
            payCallBack.EndPCall();
            payCallBack.Dispose();
        }
    }

#if UNITY_IPHONE
	[System.Runtime.InteropServices.DllImport("__Internal")]
	private static extern void startPayAppStore(string uid, string payid, string goodName, string goodPrice, string appID, string payUrl, string payNoticeUrl);
#endif

    public void PayAppStore(string uid, string payid, string goodName, string goodPrice, string appID, string payUrl, string payNoticeUrl, LuaFunction callBack)
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
       
#elif UNITY_IPHONE
        startPayAppStore(uid, payid, goodName, goodPrice, appID, payUrl, payNoticeUrl);
#endif
        if (payCallBack != null)
        {
            payCallBack.Dispose();
        }
        this.payCallBack = callBack;
    }

#if UNITY_IPHONE
	[System.Runtime.InteropServices.DllImport("__Internal")]
	private static extern void AppStoreReqProducts();
#endif

    public void ReqProducts()
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
       
#elif UNITY_IPHONE
        AppStoreReqProducts();
#endif
    }


    public byte[] Utf8GetBytes(string data)
    {
        return System.Text.UTF8Encoding.UTF8.GetBytes(data);
    }

    public string Utf8BytesToString(byte[] data)
    {
        return System.Text.UTF8Encoding.UTF8.GetString(data);
    }

    public int GetStringHashCode(string s)
    {
        return s.GetHashCode();
    }

    private IEnumerator _GetCapture()
    {
        yield return new WaitForEndOfFrame();
        int width = Screen.width;
        int height = Screen.height;
        Texture2D tex = new Texture2D(width, height, TextureFormat.RGB24, false);
        tex.ReadPixels(new Rect(0, 0, width, height), 0, 0, false);
        tex.Apply(false, false);
        byte[] imagebytes = tex.EncodeToJPG();
        Destroy(tex);
        tex.Compress(false);
        System.IO.File.WriteAllBytes(UnityEngine.Application.persistentDataPath + "/screenCaputer.jpg", imagebytes);
    }

    public void GetCapture()
    {
        StartCoroutine(_GetCapture());
    }

#if UNITY_IPHONE
    [System.Runtime.InteropServices.DllImport("__Internal")]
    private static extern void _copyTextToClipboard(string textList);
    [System.Runtime.InteropServices.DllImport("__Internal")]
    private static extern void _copyClipboardString();
#endif
    private LuaFunction copyClipBoardCallBack = null;
    public void CopyTextToClipBoard(string txt)
    {
#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call("CopyTextToClipBoard", txt);
#elif UNITY_IPHONE
        _copyTextToClipboard(txt);
#endif
    }

    public void CopyClipboardString(LuaFunction callBack)
    {
        if (copyClipBoardCallBack != null)
        {
            copyClipBoardCallBack.Dispose();
        }
        this.copyClipBoardCallBack = callBack;

#if UNITY_EDITOR

#elif UNITY_ANDROID
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call("CopyClipboardString");
#elif UNITY_IPHONE
        _copyClipboardString();
#endif
    }

    public void CopyClipboardRtn(string rtn)
    {
        Debug.Log("CopyClipboardRtn:" + rtn.ToString());
        if (copyClipBoardCallBack != null)
        {
            copyClipBoardCallBack.BeginPCall();
            copyClipBoardCallBack.Push(rtn);
            copyClipBoardCallBack.PCall();
            copyClipBoardCallBack.EndPCall();
            copyClipBoardCallBack.Dispose();
        }
    }

    void OnApplicationPause(bool pause)
    {
        if (!pause)
        {
            try
            {
                if (LuaMgr.GetMainState() != null)
                {
                    LuaFunction func = LuaMgr.GetMainState().GetFunction("MWTool.MWTool.AppResume");
                    if (func != null)
                    {
                        func.BeginPCall();
                        func.PCall();
                        func.EndPCall();
                        func.Dispose();
                    }
                }
            }
            catch (System.Exception e)
            {
                Debug.LogError(e.ToString());
            }
        }
    }
}
