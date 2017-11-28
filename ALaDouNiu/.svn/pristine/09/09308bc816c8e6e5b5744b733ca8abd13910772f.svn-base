using UnityEngine;
using System.Collections;
using LuaInterface;
using System;
using System.IO;

//展示searchpath 使用，require 与 dofile 区别
public class ScriptsFromFile : MonoBehaviour 
{
    LuaState lua = null;
    private string strLog = "";    

	void Start () 
    {
#if UNITY_5		
        Application.logMessageReceived += Log;
#else
        Application.RegisterLogCallback(Log);
#endif         
        lua = new LuaState();                
        lua.Start();        
        //如果移动了ToLua目录，自己手动修复吧，只是例子就不做配置了
        string fullPath = Application.dataPath + "\\Lua";
        string dataPath = Application.dataPath + "\\Lua/DataTable";
        lua.AddSearchPath(fullPath);
        lua.AddSearchPath(dataPath);
    }

    void Log(string msg, string stackTrace, LogType type)
    {
        strLog += msg;
        strLog += "\r\n";
    }

    void OnGUI()
    {
        GUI.Label(new Rect(100, Screen.height / 2 - 100, 600, 400), strLog);

        if (GUI.Button(new Rect(50, 50, 120, 45), "DoFile"))
        {
            strLog = "";
            lua.DoFile("DataTableMgr.lua");
            lua.DoFile("test.lua");
            lua.DoFile("DataTableTest.lua");

            LuaFunction fun =  lua.GetFunction("test");
            fun.BeginPCall();
            fun.Call();
            fun.EndPCall();
            fun.Dispose();                  
        }
//         else if (GUI.Button(new Rect(50, 150, 120, 45), "Require"))
//         {
//             strLog = "";            
//             lua.Require("ScriptsFromFile");            
//         }

        lua.Collect();
        lua.CheckTop();
    }

    void OnApplicationQuit()
    {
        lua.Dispose();
        lua = null;

#if UNITY_5		
        Application.logMessageReceived -= Log;
#else
        Application.RegisterLogCallback(null);
#endif 
    }
}
