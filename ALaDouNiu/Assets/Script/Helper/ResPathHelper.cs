using UnityEngine;
using System.Collections;
using Asset.Script.Core;
using System.IO;
using System.Security.Cryptography;

public class ResPathHelper : Singleton<ResPathHelper>
{
    /// <summary>
    /// StreamingAssets文件夹路径
    /// </summary>
    public string StreamingAssetsPath
    {
        get
        {
            if(Application.platform == RuntimePlatform.Android)
            {
                return Application.streamingAssetsPath;
            }
            else if (Application.platform == RuntimePlatform.IPhonePlayer)
            {
                return "file://" + Application.streamingAssetsPath;
            }
            else if (Application.platform == RuntimePlatform.WindowsEditor)
            {
                return "file://" + Application.streamingAssetsPath;
            }
            else
            {
                return Application.streamingAssetsPath;
            }
        }
    }

    /// <summary>
    /// 获取远端文件根目录
    /// </summary>
    /// <param name="url"></param>
    /// <returns></returns>
    public string SeverFilePath(string url)
    {
        return url + GetPlatformResSubPath(Application.platform);
    }

    /// <summary>
    /// 获取本地文件根目录
    /// </summary>
    /// <param name="url"></param>
    /// <returns></returns>
    public string LocalFilePath()
    {
        return Application.persistentDataPath + GetPlatformResSubPath(Application.platform);
    }

    /// <summary>
    /// 获取对应平台的文件储存路径
    /// </summary>
    /// <param name="platform"></param>
    /// <returns></returns>
    public string GetPlatformResFileFolder(RuntimePlatform platform)
    {
        return Application.persistentDataPath + GetPlatformResSubPath(platform);
    }

    /// <summary>
    /// 获取资源储存平台区分路径
    /// </summary>
    /// <param name="platform"></param>
    /// <returns></returns>
    public string GetPlatformResSubPath(RuntimePlatform platform)
    {
        return "/" + GetPlatformResFolderName(platform);
    }

    /// <summary>
    /// 获取不同平台资源文件夹名字
    /// </summary>
    /// <param name="platform"></param>
    /// <returns></returns>
    public string GetPlatformResFolderName(RuntimePlatform platform)
    {
        switch (platform)
        {
            case RuntimePlatform.WindowsPlayer:
                {
                    return "Win";
                }
            case RuntimePlatform.WindowsEditor:
                {
                    return "Win";
                }
            case RuntimePlatform.Android:
                {
                    return "Android";
                }
            case RuntimePlatform.IPhonePlayer:
                {
                    return "iOS";
                }
            case RuntimePlatform.OSXEditor:
                {
                    return "iOS";
                }
            default:
                {
                    return "NotDefined";
                }
        }
    }

    /// <summary>
    /// APP安装程序储存路径
    /// </summary>
    /// <returns></returns>
    public string GetAppSetupFilePath()
    {
        return Application.persistentDataPath + "/AppUpdate";
    }
}
