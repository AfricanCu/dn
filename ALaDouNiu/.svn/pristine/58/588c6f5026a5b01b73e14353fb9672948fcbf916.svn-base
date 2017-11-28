using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.Networking;
using System.IO;
using Asset.Script.Tools;
using System;

namespace Asset.Script.Update
{
    [System.Serializable]
    public class ResAddress
    {
        public string[] res;
        public string[] login;
        public string androidversion = "1.0.2";
        public string iosversion = "1.0.2";
    }

    public delegate void CallBack(object obj);

    public class UpdateMgr : MonoBehaviour
    {
        private UILabel _msgLabel;

        public static UpdateMgr Instance
        {
            get
            {
                if (null == _instance)
                {
                    GameObject go = new GameObject();
                    go.AddComponent<UpdateMgr>();
                    go.name = typeof(UpdateMgr).Name;
                }
                return _instance;
            }
        }
        private static UpdateMgr _instance;
        private void Awake()
        {
            if (null != _instance)
            {
                Destroy(this);
            }
            else
            {
                _instance = this;
            }
        }

        private void Start()
        {
            DoResRelease();
        }

        private void Update()
        {
            if (ReleaseStreamingAssets.Instance.ReleaseProgress >= 0)
            {
                TryShowMsg("正在释放文件" + (int)ReleaseStreamingAssets.Instance.ReleaseProgress + "%");
            }
            if (ReleaseStreamingAssets.Instance.DecompressionProgress >= 0)
            {
                TryShowMsg("正在解压文件" + (int)ReleaseStreamingAssets.Instance.DecompressionProgress + "%");
            }
        }

        private void DoResRelease()
        {
            TryShowMsg("正在加载配置");
            if (Application.isEditor)
            {
                GetServerAddress(() => {
                    EndUpdate();
                });
            }
            else
            {
                if (ReleaseStreamingAssets.Instance.IsFirstSetup)
                {
                    Debug.Log("首次安装，释放资源");
                    ReleaseStreamingAssets.Instance.ReleaseAssets(this, () =>
                    {
                        GetServerAddress(() => {
                            BeginUpdate();
                        });
                    });
                }
                else
                {
                    GetServerAddress(() => {
                        BeginUpdate();
                    });
                }
            }
        }


        private void GetServerAddress(Action callback)
        {
            if (Application.internetReachability == NetworkReachability.NotReachable)
            {
                TryShowMsg("[FF0707]网络连接故障，请检查您的网络[-]");
                //Application.Quit();
                return;
            }

            /*StartCoroutine_Auto(_GetServerAddress((address) =>
            {
                if (String.IsNullOrEmpty(address))
                {
                    TryShowMsg("[FF0707]网络连接故障，请检查您的网络[-]");
                    return;
                }
                else
                {
                    ResAddress obj = JsonUtility.FromJson<ResAddress>(address);
                    UpdateConfig.Instance.ResServerUrl = obj.res[0];
                    UpdateConfig.Instance.LoginServerUrl = obj.login[0];
                    Debug.Log("resServer:" + UpdateConfig.Instance.ResServerUrl);
                    Debug.Log("loginServer:" + UpdateConfig.Instance.LoginServerUrl);
                    UpdateConfig.Instance.UpdateUrl();
                    callback();
                }
            }));*/
            List<string> urlList = new List<string>();
            urlList.Add("http://douniu2.oss-cn-shenzhen.aliyuncs.com/douniu6/serverConfig.json");
            urlList.Add("http://douniu1.oss-cn-shenzhen.aliyuncs.com/douniu6/serverConfig.json");
            StartCoroutine_Auto(FixFile.Instance.DownloadConfigLua(urlList,(jsonString) =>
            {
                ResAddress obj = JsonUtility.FromJson<ResAddress>(jsonString);
                UpdateConfig.Instance.ResServerUrl = obj.res[0];
                UpdateConfig.Instance.androidVersion = obj.androidversion;
                UpdateConfig.Instance.iosVersion = obj.iosversion;
                UpdateConfig.Instance.LoginServerUrl = jsonString;
                Debug.Log("resServer:" + UpdateConfig.Instance.ResServerUrl);
                Debug.Log("loginServer:" + UpdateConfig.Instance.LoginServerUrl);
                UpdateConfig.Instance.UpdateUrl();
                callback();
            }, () =>
            {
                TryShowMsg("[FF0707]网络连接故障，请检查您的网络[-]");
                return;
            }));
        }

        private IEnumerator _GetServerAddress(Action<string> callBack)
        {
            int count = 0;
            //地址一
            bool isGetText = true;
            WWW request = new WWW("http://url1.99thj.com/thj/thj3dmj/serverConfig.json");
            while (!request.isDone)
            {
                yield return new WaitForSeconds(0.1f);
                count++;
                if(count >= 20)
                {
                    isGetText = false;
                    break;
                }
            }

            if(isGetText)
            {
                if (!string.IsNullOrEmpty(request.error))
                {
                    Debug.Log("request.error:" + request.error.ToString());
                    isGetText = false;
                }
                else
                {
                    Debug.Log("request:" + request.text.ToString());
                }
            }
            
            if (isGetText)
            {
                callBack(request.text.ToString());
                request.Dispose();
                yield break;
            }
            else
            {
                request.Dispose();
            }

            //地址二
            count = 0;
            isGetText = true;
            request = new WWW("http://url2.99thj.com/thj/thj3dmj/serverConfig.json");
            while (!request.isDone)
            {
                yield return new WaitForSeconds(0.1f);
                count++;
                if (count >= 20)
                {
                    isGetText = false;
                    break;
                }
            }

            if (isGetText)
            {
                if (!string.IsNullOrEmpty(request.error))
                {
                    Debug.Log("request.error:" + request.error.ToString());
                    isGetText = false;
                }
                else
                {
                    Debug.Log("request:" + request.text.ToString());
                }
            }

            if (isGetText)
            {
                callBack(request.text.ToString());
                request.Dispose();
                yield break;
            }
            else
            {
                request.Dispose();
            }

            //地址三
            count = 0;
            isGetText = true;
            request = new WWW("http://url3.xzgame.net/thj/thj3dmj/serverConfig.json");
            while (!request.isDone)
            {
                yield return new WaitForSeconds(0.1f);
                count++;
                if (count >= 20)
                {
                    isGetText = false;
                    break;
                }
            }

            if (isGetText)
            {
                if (!string.IsNullOrEmpty(request.error))
                {
                    Debug.Log("request.error:" + request.error.ToString());
                    isGetText = false;
                }
                else
                {
                    Debug.Log("request:" + request.text.ToString());
                }
            }

            if (isGetText)
            {
                callBack(request.text.ToString());
                request.Dispose();
                yield break;
            }
            else
            {
                request.Dispose();
            }

            //地址四
            count = 0;
            isGetText = true;
            request = new WWW("http://thjurl1.oss-cn-shenzhen.aliyuncs.com/thj/thj3dmj/serverConfig.json");
            while (!request.isDone)
            {
                yield return new WaitForSeconds(0.1f);
                count++;
                if (count >= 20)
                {
                    isGetText = false;
                    break;
                }
            }

            if (isGetText)
            {
                if (!string.IsNullOrEmpty(request.error))
                {
                    Debug.Log("request.error:" + request.error.ToString());
                    isGetText = false;
                }
                else
                {
                    Debug.Log("request:" + request.text.ToString());
                }
            }

            if (isGetText)
            {
                callBack(request.text.ToString());
                request.Dispose();
                yield break;
            }
            else
            {
                request.Dispose();
            }

            //地址五
            count = 0;
            isGetText = true;
            request = new WWW("http://thjurl2.oss-cn-shanghai.aliyuncs.comthj/thj3dmj/serverConfig.json");
            while (!request.isDone)
            {
                yield return new WaitForSeconds(0.1f);
                count++;
                if (count >= 20)
                {
                    isGetText = false;
                    break;
                }
            }

            if (isGetText)
            {
                if (!string.IsNullOrEmpty(request.error))
                {
                    Debug.Log("request.error:" + request.error.ToString());
                    isGetText = false;
                }
                else
                {
                    Debug.Log("request:" + request.text.ToString());
                }
            }

            if (isGetText)
            {
                callBack(request.text.ToString());
                request.Dispose();
                yield break;
            }
            else
            {
                request.Dispose();
            }

            callBack(String.Empty);
        }

       

        /// <summary>
        /// 开始更新流程
        /// </summary>
        private void BeginUpdate()
        {
            TryShowMsg("正在加载配置");
            if (Application.isEditor)
            {
                EndUpdate();
            }
            else
            {
                System.Action checkVersionAction = () =>
                {
                    if (Application.internetReachability == NetworkReachability.NotReachable)
                    {
                        TryShowMsg("[FF0707]网络连接故障，请检查您的网络[-]");
                        //Application.Quit();
                        return;
                    }

                    CheckVersion.Instance.Check(this, (state) =>
                    {
                        if (state == CheckVersion.CheckState.Fail)
                        {
                            //EndUpdate();//检查更新失败，直接进游戏，管他娘的
                            TryShowMsg("[FF0707]获取配置失败，请检查您的网络[-]");
                        }
                        else if (state == CheckVersion.CheckState.NoUpdate)
                        {
                            Debug.Log("无需更新,当前版本：" + CheckVersion.Instance.LocalVersion());
                            EndUpdate();
                        }
                        else if (state == CheckVersion.CheckState.NeedUpdate)
                        {
                            //开始更新
                            StartCoroutine(CheckVersion.Instance.GetMD5List(UpdateConfig.Instance.MD5Url, (md5list) =>
                             {
                                 FixFile.State Fixstate = FixFile.Instance.Fix(UpdateConfig.Instance.ResServerRoot, md5list);
                                 if (Fixstate == FixFile.State.NeedFix)
                                 {
                                     FixFile.Instance.Download.m_OnAllSuccess += (a, b) =>
                                     {
                                         Debug.Log("更新配置完成,当前版本：" + CheckVersion.Instance.LocalVersion());
                                         EndUpdate();
                                     };
                                     FixFile.Instance.Download.m_OnFailed += (a, b) =>
                                     {
                                         Debug.Log("更新文件出现异常");
                                         TryShowMsg("更新配置出现异常，请检查您的网络");
                                     };
                                     FixFile.Instance.Download.m_OnSuccess += (a, b) =>
                                     {
                                         float progress = (float)FixFile.Instance.Download.CurProgress / (float)FixFile.Instance.Download.Total;
                                         progress = progress * 100f;
                                         TryShowMsg("正在加载配置" + (int)progress + "%");
                                     };
                                 }
                                 else if (Fixstate == FixFile.State.NotNeed)
                                 {
                                     EndUpdate();
                                 }
                                 else if (Fixstate == FixFile.State.Fixing)
                                 {
                                     Debug.Log("正在修复中");
                                 }
                             }));
                        }
                        else if (state == CheckVersion.CheckState.NeedResetup)
                        {
                            string url = null;
                            if(Application.platform == RuntimePlatform.Android)
                            {
                                url = "";
                            }
                            else if (Application.platform == RuntimePlatform.IPhonePlayer)
                            {
                                url = "";
                            }
                            else
                            {
                                url = "";
                            }
                            TryShowMsg("[url="+ url + "][u]版本已过期，点击更新最新版本[/u][/url]");
                        }
                    });
                };

                /*if (ReleaseStreamingAssets.Instance.IsFirstSetup)
                {
                    Debug.Log("首次安装，释放资源");
                    ReleaseStreamingAssets.Instance.ReleaseAssets(this, () =>
                    {
                        checkVersionAction();
                    });
                }
                else
                {
                    checkVersionAction();
                }*/

                checkVersionAction();
            }
        }


        private void EndUpdate()
        {
            GameObject go = new GameObject();
            go.AddComponent<Initialization>();
            TryShowMsg("");
            float end = System.Environment.TickCount;
        }
        private void Reset()
        {
            this.gameObject.name = this.GetType().Name;
        }
        private void TryShowMsg(string str)
        {
            if (null == _msgLabel)
            {
                _msgLabel = GameObject.FindObjectOfType<UILabel>();
            }
            if (null != _msgLabel)
                _msgLabel.text = str;
        }
    }
}


