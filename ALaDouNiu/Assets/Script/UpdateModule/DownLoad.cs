using UnityEngine;
using UnityEngine.Networking;
using System.Collections;
using System.IO;
using System.Collections.Generic;

namespace Asset.Script.Update
{
    /// <summary>
    ///下载组件
    /// </summary>
    public class DownLoad : MonoBehaviour
    {
        /// <summary>
        /// 创建一个下载组件
        /// </summary>
        /// <returns></returns>
        public static DownLoad CreateDownLoad()
        {
            GameObject go = new GameObject();
            go.name = "Download";
            DownLoad download = go.AddComponent<DownLoad>();
            return download;
        }

        public delegate void OnDownloadEvent(string DdownloadAddress, string FilePaht);

        /// <summary>
        /// 当每个文件下载成功时调用
        /// </summary>
        public event OnDownloadEvent m_OnSuccess;
        /// <summary>
        /// 当每个文件下载失败调用
        /// </summary>
        public event OnDownloadEvent m_OnFailed;
        /// <summary>
        /// 当所有文件下载成功完成后调用
        /// </summary>
        public event OnDownloadEvent m_OnAllSuccess;


        public int CurProgress
        {
            get
            {
                return _curProgress;
            }
        }

        public int Total
        {
            get
            {
                return _total;
            }
        }

        public bool Runing
        {
            get
            {
                return _runing;
            }
        }

        private bool _runing = false;
        private int _curProgress = 0;
        private int _total = 0;

        /// <summary>
        /// 下载文件，参数为待下载列表， KeyValuePair<下载地址,文件储存地址>
        /// </summary>
        /// <param name="info"></param>
        public void DownloadFile(KeyValuePair<string, string>[] info)
        {
            StartCoroutine(StartDownloadFile(info));
        }

        private IEnumerator StartDownloadFile(KeyValuePair<string, string>[] info)
        {
            _runing = true;
            _curProgress = 0;
            _total = info.Length;
            bool allSuccess = true;
            for (int i = 0; i < info.Length; ++i)
            {
                WWW downloader = new WWW(info[i].Key);
                yield return downloader;

                if (!string.IsNullOrEmpty(downloader.error))
                {
                    if (null != m_OnFailed)
                        m_OnFailed(info[i].Key, info[i].Value);
                    Debug.LogError("下载文件 " + info[i].Key + " 异常：" + downloader.error);
                    downloader.Dispose();
                    allSuccess = false;
                    continue;
                }
                try
                {
                    string savePath = Path.GetDirectoryName(info[i].Value);
                    if (!Directory.Exists(savePath))
                    {
                        Directory.CreateDirectory(savePath).Refresh();
                    }
                    using (FileStream fs = File.Create(info[i].Value))
                    {
                        fs.Write(downloader.bytes, 0, downloader.bytes.Length);
                        fs.Close();
                    }
                }
                catch (System.Exception ex)
                {
                    if (null != m_OnFailed)
                        m_OnFailed(info[i].Key, info[i].Value);
                    Debug.LogError("储存数据到本地异常:" + ex);
                    downloader.Dispose();
                    allSuccess = false;
                    continue;
                }
                if (null != m_OnSuccess)
                {
                    m_OnSuccess(info[i].Key, info[i].Value);
                }
                ++_curProgress;
                Debug.Log("下载成功" + info[i].Key);
                Debug.Log(info[i].Value);
                downloader.Dispose();
            }

            if(allSuccess && null != m_OnAllSuccess)
            {
                m_OnAllSuccess(null, null);
            }
            _runing = false;
        }
    }
}