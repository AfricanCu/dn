using UnityEngine;
using System.Collections;
using Asset.Script.Core;

namespace Asset.Script.Update
{
    public class UpdateConfig : Singleton<UpdateConfig>
    {
        public string ResServerRoot;
        public string VersionCodeUrl;
        public string MD5Url;
        public string LocalVersionCodePath;
        public string VersionFileName = "Version.pig";
        public string iosVersion = "";
        public string androidVersion = "";

        public string ResServerUrl = "";
        //这里实际放了很多内容，所有的config配置都在这里面，格式为json
        public string LoginServerUrl = "";
        public UpdateConfig()
        {
            
        }

        public void UpdateUrl()
        {
            Debug.Log("ResServerUrl:" + ResServerUrl);
            ResServerRoot = ResPathHelper.Instance.SeverFilePath(ResServerUrl + "/1_0");
            VersionCodeUrl = ResServerRoot + "/" + VersionFileName;
            MD5Url = ResServerRoot + "/md5.txt";
            LocalVersionCodePath = ResPathHelper.Instance.LocalFilePath() + "/" + VersionFileName;
            Debug.Log("iosVersion:" + iosVersion);
        }

        public string GetSmallVersion()
        {
            if (PlatformTool.Instance.GetPlatformID() == 2)
            {
                return iosVersion;
            }
            else
            {
                return androidVersion;
            }
        }
    }
}