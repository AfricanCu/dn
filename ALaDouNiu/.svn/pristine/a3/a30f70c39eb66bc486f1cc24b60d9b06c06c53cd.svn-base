using UnityEngine;
using System.Collections;
using Asset.Script.Core;
using System;
using System.IO;

namespace Asset.Script.Update
{
    
    public class CheckVersion : Singleton<CheckVersion>
    {
        public const string Version = "1.1.2";

        public delegate void CallBack(bool isSucess);
        public delegate void GetBack(string code);
        public delegate void CheckCallBack(CheckState state);

        public enum CheckState
        {
            /// <summary>
            /// 检查更新失败
            /// </summary>
            Fail,
            /// <summary>
            /// 不需要更新
            /// </summary>
            NoUpdate,
            /// <summary>
            /// 需要更新
            /// </summary>
            NeedUpdate,
            /// <summary>
            /// 需要换包
            /// </summary>
            NeedResetup,
        }

        public void Check(MonoBehaviour mono, CheckCallBack callback)
        {
            GetVersionCode(UpdateConfig.Instance.GetSmallVersion(), (code) =>
            {
                if (string.IsNullOrEmpty(code))//获取版本号为空
                {
                    callback(CheckState.Fail);
                    return;
                }
                string localCode = LocalVersion();
                if(string.IsNullOrEmpty(localCode)) //本地没有版本号文件，提示需要更新
                {
                    callback(CheckState.NeedUpdate);
                    return;
                }
                if (code != localCode)
                {
                    string[] subCodes = code.Split('.');
                    string[] StaticSubCodes = Version.Split('.');
                    if (StaticSubCodes[0] != subCodes[0])
                    {
                        callback(CheckState.NeedResetup);
                    }
                    else
                    {
                        callback(CheckState.NeedUpdate);
                    }
                }
                else
                {
                    callback(CheckState.NoUpdate);
                }
            });
        }


        public void GetVersionCode(string version, GetBack callBack)
        {
            /*WWW request = new WWW(url);
            yield return request;
            if (!string.IsNullOrEmpty(request.error))
            {
                Debug.LogError("获取版本号异常:" + request.error);
                Debug.LogError(url);
                callBack(null);
                yield break;
            }
            string code = request.text;*/
            callBack(version);
        }

        public string LocalVersion()
        {
            string code = null;
            if(File.Exists(UpdateConfig.Instance.LocalVersionCodePath))
            {
                using (StreamReader reader = File.OpenText(UpdateConfig.Instance.LocalVersionCodePath))
                {
                    code = reader.ReadToEnd();
                }
            }
            return code;
        }


        public IEnumerator GetMD5List(string url, GetBack callBack)
        {
            WWW request = new WWW(url);
            yield return request;
            if (!string.IsNullOrEmpty(request.error))
            {
                Debug.LogError("获取MD5列表异常:" + request.error);
                callBack(null);
                yield break;
            }
            string md5list = request.text;
            callBack(md5list);
        }
    }
}
