using UnityEngine;
using System.Collections;
using LuaInterface;

//魔窗插件的抽象
public static class MagicWin
{
    private static LuaFunction _LuaCallBack;

    public static void OnMagicWinMsg(string msg)
    {
        if(null != _LuaCallBack)
        {
            _LuaCallBack.BeginPCall();
            _LuaCallBack.Push(msg);
            _LuaCallBack.PCall();
            _LuaCallBack.EndPCall();
        }
    }

    public static void RegLuaCallBack(LuaFunction luafun)
    {
        if(null != _LuaCallBack)
        {
            _LuaCallBack.Dispose();
        }
        _LuaCallBack = luafun;
    }
}
