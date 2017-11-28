using UnityEngine;
using System.Collections;
using LuaInterface;
using Asset.Script.ABSystem;
using System.IO;
using UnityEngine.SceneManagement;
using System;
using Object = UnityEngine.Object;



/// <summary>
/// 资源管理模块对Lua接口
/// </summary>
public static class ResHelper
{
    /// <summary>
    /// 同步加载资源或预制
    /// </summary>
    /// <param name="pathOrName"></param>
    /// <returns></returns>
    public static Object LoadResBySyn(string pathOrName, Object owner)
    {
        try
        {
            Object res = null;
            bool isScene = false;

            if (!Application.isEditor)//在编辑器下直接加载Resources下的资源
            {
                ResLoader.Instance.LoadFormAB(pathOrName, (obj) =>
                {
                    if (null != obj)
                    {
                        if (!obj.m_AB.isStreamedSceneAssetBundle)
                        {
                            res = obj.m_AB.LoadAsset(Path.GetFileName(pathOrName));
                        }
                        else
                        {
                            isScene = true;
                            Debug.Log("场景AB加载完成: " + pathOrName);
                        }
                    }
                }, owner);
            }

            if (null == res)
            {
                res = Resources.Load(pathOrName);
            }

            if (null == res && !isScene)
            {
                Debug.LogError("资源或预制不存在：" + pathOrName);
            }

            return res;
        }
        catch (System.Exception ex)
        {

            Debug.LogError(ex);
            return null;
        }
    }

    /// <summary>
    /// 同步加载资源或预制
    /// </summary>
    /// <param name="pathOrName"></param>
    /// <returns></returns>
    public static Object LoadResBySyn(string pathOrName)
    {
        Object res = LoadResBySyn(pathOrName,null);
        return res;
    }


    /// <summary>
    /// 异步加载资源或预制
    /// </summary>
    /// <param name="pathOrName"></param>
    /// <param name="luafun"></param>
    /// <param name="owner"></param>
    public static void LoadResByAsync(string pathOrName, LuaFunction luafun, Object owner)
    {
        Object res = null;
        Action FormResources = () =>
        {
            //从Resources异步加载
            LoadCoroutineObj.Instance.StartCoroutine(ResLoader.Instance.Asyncload(pathOrName, (resObj) =>
            {
                if (null != resObj)
                {
                    res = resObj;

                    luafun.BeginPCall();
                    luafun.Push(res);
                    luafun.PCall();
                    luafun.EndPCall();
                }
                luafun.Dispose();
            }));
        };

        if(Application.isEditor)
        {
            FormResources();
        }
        else
        {
            ResLoader.Instance.LoadFormAB(pathOrName, (obj) =>
            {
                bool isScene = false;
                if (null != obj)
                {
                    if (!obj.m_AB.isStreamedSceneAssetBundle)
                    {
                        res = obj.m_AB.LoadAsset(Path.GetFileName(pathOrName));
                    }
                    else
                    {
                        isScene = true;
                    }
                }
                if (null == res && !isScene)
                {
                    FormResources();
                }
                else
                {
                    luafun.BeginPCall();
                    luafun.Push(res);
                    luafun.PCall();
                    luafun.EndPCall();
                    luafun.Dispose();
                }
            }, owner, LoadMode.Async);
        }
    }

    /// <summary>
    /// 异步加载资源或预制
    /// </summary>
    /// <param name="pathOrName"></param>
    /// <param name="luafun"></param>
    public static void LoadResByAsync(string pathOrName, LuaFunction luafun)
    {
        LoadResByAsync(pathOrName, luafun, null);
    }
    /// <summary>
    /// 为AB指定引用计数
    /// </summary>
    /// <param name="abName"></param>
    /// <param name="refObj"></param>
    public static void Retain(string abName, Object refObj)
    {
        ABManager.Instance.Retain(abName, refObj);
    }

    /// <summary>
    /// 卸载失效的资源
    /// </summary>
    public static void UnLoadUnUse()
    {
        ABManager.Instance.UnLoadUnUse();
    }
}
