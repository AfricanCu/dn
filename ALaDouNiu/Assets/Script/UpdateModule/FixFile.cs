using UnityEngine;
using System.Collections;
using Asset.Script.Core;
using System.IO;
using System.Collections.Generic;
using Asset.Script.Tools;

namespace Asset.Script.Update
{
    /// <summary>
    /// 修复本地文件
    /// </summary>
    public class FixFile : Singleton<FixFile>
    {
        public enum State
        {
            NotNeed,
            Fixing,
            NeedFix,
        }
    
        private bool _runing = false;
        private readonly string LocalRootPath = Path.GetFullPath(ResPathHelper.Instance.LocalFilePath());
        public DownLoad Download { private set; get; }

        /// <summary>
        /// 开始修复
        /// </summary>
        /// <param name="serverRootPath"></param>
        /// <param name="md5list"></param>
        /// <returns></returns>
        public State Fix(string serverRootPath, string md5list)
        {
            if (_runing)
            {
                return State.Fixing;
            }
            _runing = true;
            List<string> ReadyUpdate = BeginFix(serverRootPath, md5list);
            if(ReadyUpdate.Count <= 0)
            {
                return State.NotNeed;
            }
            DownloadFile(serverRootPath, ReadyUpdate);
            return State.NeedFix;
        }
        public void ResetFix()
        {
            _runing = false;
        }
        private List<string> BeginFix(string serverRootPath, string md5list)
        {
            try
            {
                //检查本地储存路径
                string LocalRootPath = Path.GetFullPath(ResPathHelper.Instance.LocalFilePath());
                LocalRootPath = FilePathTool.Instance.Normalization(LocalRootPath, Application.platform);
                if (!Directory.Exists(LocalRootPath))//如果不存在本地储存路径，则创建
                {
                    Directory.CreateDirectory(LocalRootPath);
                }
                //解析MD5表
                Dictionary<string, string> _md5Dic = new Dictionary<string, string>();
                using (StringReader reader = new StringReader(md5list))
                {
                    string line = reader.ReadLine();
                    while (!string.IsNullOrEmpty(line))
                    {
                        string[] data = line.Split('\t');

                        string filePath = LocalRootPath + data[0];

                        filePath = FilePathTool.Instance.Normalization(filePath, Application.platform);

                        string md5 = data[1];
                        _md5Dic.Add(filePath, md5);

                        line = reader.ReadLine();
                    }
                }

                //获取本地文件
                List<string> localFiles = new List<string>();
                DirectoryInfo dirInfo = new DirectoryInfo(LocalRootPath);
                foreach (FileInfo file in dirInfo.GetFiles("*.*", SearchOption.AllDirectories))
                {
                    localFiles.Add(file.FullName);
                }

                //删除本地无用文件
                for (int i = 0; i < localFiles.Count; ++i)
                {
                    string fullname = FilePathTool.Instance.Normalization(localFiles[i], Application.platform);
                    if (!_md5Dic.ContainsKey(fullname))
                    {
                        File.Delete(fullname);
                        localFiles.RemoveAt(i);
                        Debug.Log("删除无用文件:" + fullname);
                        --i;
                    }
                }
                dirInfo.Refresh();//删除后刷新路径

                //找出差异文件，准备更新
                List<string> ReadyUpdate = new List<string>();
                foreach (KeyValuePair<string, string> v in _md5Dic)
                {
                    string fullpath = v.Key;
                    bool needUpdate = false;
                    if (!File.Exists(fullpath))
                    {
                        needUpdate = true;
                    }
                    else
                    {
                        string Servermd5 = v.Value;
                        string Localmd5 = MD5Tool.Instance.GetMD5(fullpath);

                        if (Localmd5 != Servermd5)
                        {
                            needUpdate = true;
                        }
                    }
                    if (needUpdate)
                    {
                        string localPath = v.Key.Replace(LocalRootPath, "");
                        ReadyUpdate.Add(localPath);
                    }
                }

                return ReadyUpdate;
            }
            catch (System.Exception ex)
            {
                ResetFix();
                Debug.LogError("修复文件异常: " + ex);
                return null;
            }
        }
        private void DownloadFile(string serverRootPath, List<string> ReadyUpdate)
        {
            //构造远端路径和本地储存路径
            List<KeyValuePair<string, string>> downloadInfo = new List<KeyValuePair<string, string>>();
            for (int i = 0; i < ReadyUpdate.Count; ++i)
            {
                string localPath = ReadyUpdate[i];
                KeyValuePair<string, string> info =
                    new KeyValuePair<string, string>(serverRootPath + FilePathTool.Instance.Normalization("/" + localPath, Application.platform)
                    , FilePathTool.Instance.Normalization(LocalRootPath + "/" + localPath, Application.platform));
                downloadInfo.Add(info);
            }

            //开始下载更新
            Download = DownLoad.CreateDownLoad();
            Download.m_OnFailed += (a, b) =>
            {
                MonoBehaviour.Destroy(Download);
                Download = null;
            };
            Download.m_OnAllSuccess += (a, b) =>
            {
                MonoBehaviour.Destroy(Download);
                Download = null;
                ResetFix();
            };
            Download.DownloadFile(downloadInfo.ToArray());
        }

        public IEnumerator DownloadConfigLua(List<string> urlList, System.Action<string> sucAction, System.Action failAction)
        {
            bool isSuc = false;
            string jsonString = "";
            for(int i= 0; i<urlList.Count; i++)
            {
                WWW downloader = new WWW(urlList[i]);

                int count = 0;
                bool isGetText = true;
                while (!downloader.isDone)
                {
                    yield return new WaitForSeconds(0.1f);
                    count++;
                    if (count >= 25)
                    {
                        isGetText = false;
                        break;
                    }
                }

                if (isGetText)
                {
                    if (!string.IsNullOrEmpty(downloader.error))
                    {
                        Debug.Log("request.error:" + downloader.error.ToString());
                        isGetText = false;
                    }
                    else
                    {
                        Debug.Log("request:" + downloader.text.ToString());
                    }
                }

                if (!isGetText)
                {
                    downloader.Dispose();
                    continue;
                }

                try
                {
                    /*//检查本地储存路径
                    string LocalRootPath = Path.GetFullPath(ResPathHelper.Instance.LocalFilePath());
                    LocalRootPath = FilePathTool.Instance.Normalization(LocalRootPath, Application.platform);
                    if (!Directory.Exists(LocalRootPath))//如果不存在本地储存路径，则创建
                    {
                        Directory.CreateDirectory(LocalRootPath);
                    }
                    Debug.Log("LocalRootPath:" + LocalRootPath.ToString());

                    string filePath = LocalRootPath + "/Lua/Module/Config.lua";
                    filePath = FilePathTool.Instance.Normalization(filePath, Application.platform);

                    string savePath = Path.GetDirectoryName(filePath);
                    if (!Directory.Exists(savePath))
                    {
                        Directory.CreateDirectory(savePath).Refresh();
                    }
                    using (FileStream fs = File.Create(filePath))
                    {
                        fs.Write(downloader.bytes, 0, downloader.bytes.Length);
                        fs.Close();
                    }*/
                    isSuc = true;
                    jsonString = downloader.text;
                    downloader.Dispose();
                    break;
                }
                catch (System.Exception ex)
                {
                    Debug.LogError("储存数据到本地异常:" + ex);
                    downloader.Dispose();
                    continue;
                }
            }

            if(isSuc)
            {
                sucAction(jsonString);
            }
            else
            {
                failAction();
            }
        }
    }
}