using UnityEngine;
using System.Collections;
using LuaInterface;
using System.Collections.Generic;
using System.IO;

public class LuaMgr : LuaClient
{
    public new static LuaMgr Instance
    {
        get
        {
            return _instance;
        }
    }
    private static LuaMgr _instance;

    public static void Create()
    {
        if (null == _instance)
        {
            GameObject go = new GameObject();
            go.name = typeof(LuaMgr).Name;
            go.AddComponent<LuaMgr>();
        }
    }

    public static void Dispose()
    {
        if(null != _instance)
            DestroyImmediate(_instance.gameObject);
    }

    public LuaState MainState
    {
        get
        {
            return luaState;
        }
    }

    protected override void StartMain()
    {
        string LocalLuaPath = ResPathHelper.Instance.LocalFilePath();
        if (Application.isEditor)
        {
            LocalLuaPath = Application.dataPath + "/Lua";
        }

        OpenCJson();

        float start = System.Environment.TickCount;
        MainState.AddSearchPath(LocalLuaPath);//所有lua脚本都放在这个文件夹下
        MainState.DoFile("Debug.lua");
        MainState.DoFile("Boot.lua");
        Debug.Log("引导耗时 " + (System.Environment.TickCount - start) + " 毫秒");
    }

    private new void Awake()
    {
        if (null != _instance)
        {
            Destroy(this);
        }
        else
        {
            _instance = this;
            base.Awake();
            DontDestroyOnLoad(this.gameObject);
        }
    }

    private new void OnDestroy()
    {
        base.Destroy();
        if(this == _instance)
            _instance = null;
    }



}
