using UnityEngine;
using System.Collections;
using LuaInterface;

/// <summary>
/// 交互体对lua接口
/// </summary>
public static class InObjHelper
{
    /// <summary>
    /// 根据名字获取场景中的某个交互体
    /// </summary>
    /// <param name="name"></param>
    /// <returns></returns>
    public static GameObject FindInObjGameObject(string name)
    {
        return InteractiveObjMgr.Instance.GetByName(name).gameObject;
    }
    /// <summary>
    /// 根据名字让场景中某个交互体失效
    /// </summary>
    /// <param name="name"></param>
    public static void CloseObj(string name)
    {
        InteractiveObj Obj = InteractiveObjMgr.Instance.GetByName(name);
        if(null != Obj)
        {
            Obj.enabled = false;
        }
    }
    /// <summary>
    /// 根据名字激活场景中某个交互体
    /// </summary>
    /// <param name="name"></param>
    public static void ActiveObj(string name)
    {
        InteractiveObj Obj = InteractiveObjMgr.Instance.GetByName(name);
        if (null != Obj)
        {
            Obj.enabled = true;
        }
    }
    /// <summary>
    /// 注册交互体lua回调
    /// </summary>
    /// <param name="name"></param>
    /// <param name="luafun"></param>
    public static void RegLuaCallBack(string name, LuaFunction luafun)
    {
        InteractiveObj Obj = InteractiveObjMgr.Instance.GetByName(name);
        if (null != Obj)
        {
            Obj.LuaCallBack = luafun;
        }
        else
        {
            luafun.Dispose();
        }
    }

    public static void RegLuaCallBack(InteractiveObj Obj, LuaFunction luafun)
    {
        if (null != Obj)
        {
            Obj.LuaCallBack = luafun;
        }
        else
        {
            luafun.Dispose();
        }
    }
}
