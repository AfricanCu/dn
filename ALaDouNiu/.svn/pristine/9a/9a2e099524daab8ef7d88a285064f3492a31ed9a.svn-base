using System;
using UnityEngine;
using System.Collections;
using Asset.Script.ABSystem;
using Object = UnityEngine.Object;
using Asset.Script.Core;

/// <summary>
/// 资源加载器
/// </summary>
public class ResLoader : Singleton<ResLoader>
{
    public delegate void loadFromResCallBack(Object obj);
    public delegate void loadFromABCallBack(AssetBundleAgant obj);

    /// <summary>
    /// 从AB加载资源
    /// </summary>
    /// <param name="pathOrName"></param>
    /// <param name="owner"></param>
    /// <param name="callback"></param>
    /// <param name="mode"></param>
    public void LoadFormAB(string pathOrName, loadFromABCallBack callback, Object owner = null, LoadMode mode = LoadMode.Syn)
    {
        ABManager.Instance.Load(pathOrName, (a) =>
        {
            callback(a);
        }, owner ,mode);
    }

    /// <summary>
    /// 从Resources同步加载资源
    /// </summary>
    /// <param name="pathOrName"></param>
    /// <param name="callback"></param>
    /// <param name="mode"></param>
    public Object LordFormResourcesBySyn(string pathOrName)
    {
        return Resources.Load(pathOrName);
    }

    public IEnumerator Asyncload(string pathOrName, loadFromResCallBack callback)
    {
        ResourceRequest request = Resources.LoadAsync(pathOrName);
        yield return request;
        if (request.isDone)
        {
            callback(request.asset);
        }
        else
        {
            throw new Exception("读取资源失败 " + pathOrName);
        }
    }
}


