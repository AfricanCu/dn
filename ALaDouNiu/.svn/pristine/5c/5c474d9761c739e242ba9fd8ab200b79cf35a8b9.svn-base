using System;
using UnityEngine;
using System.Collections.Generic;
using Asset.Script.Core;
using System.IO;

namespace Asset.Script.Update
{
    /// <summary>
    /// 重装APP
    /// </summary>
    public class ReSetupAPP : Singleton<ReSetupAPP>
    {
        /// <summary>
        /// 检查是否需要进行APP重装
        /// </summary>
        /// <param name="mono"></param>
        /// <param name="callback"></param>
        public void CheckIsNeedResetup(MonoBehaviour mono,CallBack callback)
        {
            string versionCode = CheckVersion.Instance.LocalVersion();

            if(string.IsNullOrEmpty(versionCode))
            {
                callback(false);
                return;
            }

            string[] localCodes = versionCode.Split('.');

            CheckVersion.Instance.GetVersionCode(UpdateConfig.Instance.GetSmallVersion(), (code) =>
            {
                string[] versionCodes = code.Split('.');

                if(versionCodes[0] != localCodes[0]
                || versionCodes[1] != localCodes[1])
                {
                    callback(true);
                }
                else
                {
                    callback(false);
                }
            });
        }

        /// <summary>
        /// 进行APP重装
        /// </summary>
        public void ReSetup(CallBack callback)
        {


#if UNITY_ANDROID
            string savePath = ResPathHelper.Instance.GetAppSetupFilePath();
            savePath = FilePathTool.Instance.Normalization(savePath, Application.platform);
            if (!Directory.Exists(savePath))
            {
                Directory.CreateDirectory(savePath);
            }

            string appDownloadPath = "";

            Action setupAction = () =>
            {
                //todo:调用系统接口安装APK
                callback(true);
            };

            DownLoad download = DownLoad.CreateDownLoad();
            download.DownloadFile(new KeyValuePair<string, string>[]
            {
                new KeyValuePair<string, string>(appDownloadPath,savePath),
            });
            download.m_OnAllSuccess += (a, b) =>
            {
                setupAction();
            };
            download.m_OnFailed += (a, b) =>
            {
                callback(false);
            };

#elif UNITY_IPHONE
            Debug.Log("自己去更新");
#else
#endif

        }
    }



}