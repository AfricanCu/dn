using UnityEngine;
using System.Collections;
using LuaInterface;
using UnityEngine.SceneManagement;

public static class SceneHelper
{
    public static void LoadSceneSys(string sceneName)
    {
        SceneManager.LoadScene(sceneName);
    }

    public static void LoadSceneASys(string sceneName, LuaFunction luafun)
    {
        LuaMgr.Instance.StartCoroutine_Auto(ProcessASynLoadScene(sceneName, luafun));
    }

    public static void LoadSceneASys(string sceneName, LuaFunction luafun, LuaFunction progressCallBack)
    {
        LuaMgr.Instance.StartCoroutine_Auto(ProcessASynLoadScene(sceneName, luafun, progressCallBack));
    }

    private static IEnumerator ProcessASynLoadScene(string sceneName, LuaFunction luafun,LuaFunction asyncCallBack = null)
    {
        AsyncOperation async = SceneManager.LoadSceneAsync(sceneName);
        if(null != asyncCallBack)
        {
            asyncCallBack.BeginPCall();
            asyncCallBack.Push(async);
            asyncCallBack.PCall();
            asyncCallBack.EndPCall();
            asyncCallBack.Dispose();
        }
        yield return async;

        luafun.BeginPCall();
        luafun.Push(sceneName);
        luafun.PCall();
        luafun.EndPCall();
        luafun.Dispose();
    }
}