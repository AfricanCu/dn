using UnityEngine;
using System.Collections.Generic;
using System.IO;
using Asset.Script.Core;
using Asset.Script.Update;

public class LuaHelper : Singleton<LuaHelper>
{
    /// <summary>
    /// 加载脚本
    /// </summary>
    /// <param name="fileName"></param>
    public void DoFile(string fileName)
    {
        LuaMgr.Instance.MainState.DoFile(fileName);
    }

    /// <summary>
    /// 加载路径下所有Lua脚本，路径是相对 LuaScriptPath 下的相对路径
    /// </summary>
    /// <param name="path"></param>
    /// <returns></returns>
    public void LoadAllLuaScriptByPath(string path)
    {
        string LuaScriptPath = ResPathHelper.Instance.LocalFilePath() + "/Lua";
        if (Application.isEditor)
        {
            LuaScriptPath = Application.dataPath + "/Lua";
        }
        string  fullpath = LuaScriptPath + path;
        LuaMgr.Instance.MainState.AddSearchPath(fullpath);
        List<string> fileNames = new List<string>();
        DirectoryInfo folder = new DirectoryInfo(fullpath);
        foreach (FileInfo file in folder.GetFiles("*.lua", SearchOption.AllDirectories))
        {
            LuaMgr.Instance.MainState.AddSearchPath(Path.GetDirectoryName(file.FullName));
            fileNames.Add(file.Name);
        }
        for (int i = 0; i < fileNames.Count; ++i)
        {
            DoFile(fileNames[i]);
        }
    }

    /// <summary>
    /// 获取本地版本号
    /// </summary>
    /// <returns></returns>
    public string GetLocalVersionCode()
    {
        return CheckVersion.Instance.LocalVersion();
    }
}

