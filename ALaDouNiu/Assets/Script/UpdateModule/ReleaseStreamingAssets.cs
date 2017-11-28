using UnityEngine;
using System.Collections;
using Asset.Script.Core;
using System.IO;
using System;
using ICSharpCode.SharpZipLib.Zip;
using System.Threading;

namespace Asset.Script.Update
{
    /// <summary>
    /// 初次安装，释放StreamingAssets到本地路径
    /// </summary>
    public class ReleaseStreamingAssets : Singleton<ReleaseStreamingAssets>
    {
        /// <summary>
        /// 是否首次安装
        /// </summary>
        public bool IsFirstSetup
        {
            get
            {
                string LocalRootPath = Path.GetFullPath(ResPathHelper.Instance.LocalFilePath());
                string destPath = FilePathTool.Instance.Normalization(LocalRootPath, Application.platform);
                return !Directory.Exists(destPath);
            }
        }


        public float ReleaseProgress
        {
            get
            {
                if (null != _www)
                {
                    return _www.progress;
                }
                return -1;
            }
        }

        public float DecompressionProgress
        {
            get
            {
                return _decProgress;
            }
        }

        private float _decProgress = -1;
        private WWW _www = null;

        public void ReleaseAssets(MonoBehaviour mono, Action callBack)
        {
            string sourcePath = Application.streamingAssetsPath;
            string LocalRootPath = Path.GetFullPath(ResPathHelper.Instance.LocalFilePath());
            string destPath = FilePathTool.Instance.Normalization(LocalRootPath, Application.platform);
            if (!Directory.Exists(destPath))
            {
                Directory.CreateDirectory(destPath).Refresh();
            }

            string zipFileName = ResPathHelper.Instance.GetPlatformResFolderName(Application.platform) + ".zip";

            string zipFileSourcePath = ResPathHelper.Instance.StreamingAssetsPath + @"/" + zipFileName;
            string zipFileDestPath = Application.persistentDataPath + @"/" + zipFileName;

            Debug.Log("ZIP源文件路径：" + zipFileSourcePath);

            mono.StartCoroutine_Auto(ReleaseZip(zipFileSourcePath, zipFileDestPath, () =>
            {
                Decompression(zipFileDestPath);
                //绕开子线程访问主线程的问题
                GameObject CallBackObj = new GameObject();
                BehaviourUpdateEvent UpdateEvent = CallBackObj.AddComponent<BehaviourUpdateEvent>();
                UpdateEvent.UpdateAction = () =>
                {
                    if (-1 == _decProgress)
                    {
                        callBack();
                        GameObject.DestroyImmediate(CallBackObj);
                    }
                };
            }));
        }

        private void Decompression(string zipFilePath)
        {
            _decProgress = 0;

            Debug.Log("开始解压文件 " + zipFilePath);
            string outputPath = Application.persistentDataPath + @"/" + ResPathHelper.Instance.GetPlatformResFolderName(Application.platform);
            Debug.Log("解压到：" + outputPath);

            System.Action decAction = () =>
            {
                try
                {
                    long totalEntryCount = 0;
                    using (ZipFile zipfile = new ZipFile(zipFilePath))
                    {
                        totalEntryCount = zipfile.Count;
                    }

                    using (ZipInputStream Zips = new ZipInputStream(File.OpenRead(zipFilePath)))
                    {
                        int curCount = 0;
                        ZipEntry theEntry = null;
                        while ((theEntry = Zips.GetNextEntry()) != null)
                        {
                            ++curCount;
                            _decProgress = (float)curCount / (float)totalEntryCount * 100;

                            string fileoutputPath = outputPath + @"/" + theEntry.Name;

                            fileoutputPath = FilePathTool.Instance.Normalization(fileoutputPath, Application.platform);
                            string directoryName = Path.GetDirectoryName(fileoutputPath);

                            if (!Directory.Exists(directoryName))
                            {
                                DirectoryInfo dir = Directory.CreateDirectory(directoryName);
                                dir.Refresh();
                            }
                            if (theEntry.IsDirectory) //如果实体是文件夹
                            {
                                continue;
                            }
                            if (!theEntry.IsFile)//如果实体不是文件
                            {
                                continue;
                            }
                            using (FileStream streamWriter = File.Create(fileoutputPath))
                            {
                                int size = 2048;
                                byte[] data = new byte[2048];
                                while (true)
                                {
                                    size = Zips.Read(data, 0, data.Length);
                                    if (size > 0)
                                    {
                                        streamWriter.Write(data, 0, size);
                                    }
                                    else
                                    {
                                        break;
                                    }
                                }
                            }
                            Thread.Sleep(1);
                        }
                    }
                    Debug.Log("释放文件到本地完成");
                    File.Delete(zipFilePath);
                    Debug.Log("删除压缩包");
                }
                catch (Exception ex)
                {
                    Debug.LogError("解压ZIP到沙盒文件夹爆炸了：" + ex);
                    _decProgress = -1;
                }
                _decProgress = -1;
            };
            Thread thread = new Thread(new ThreadStart(decAction));//启动一个线程去解压ZIP
            thread.Start();
        }

        /// <summary>
        /// 释放资源ZIP包从StreamingAssets到沙盒文件夹
        /// </summary>
        /// <param name="zipFileName"></param>
        /// <param name="callBack"></param>
        /// <returns></returns>
        private IEnumerator ReleaseZip(string zipFileSourcePath, string zipFileDestPath, Action callBack)
        {
            _www = new WWW(zipFileSourcePath);
            yield return _www;
            if (!string.IsNullOrEmpty(_www.error))
            {
                Debug.LogError("移动ZIP包从 streaming 失败：" + _www.error);
                callBack();
                yield break;
            }

            using (FileStream fs = File.Create(zipFileDestPath))
            {
                fs.Write(_www.bytes, 0, _www.bytes.Length);
                fs.Close();
            }

            _www.Dispose();
            _www = null;

            callBack();
        }
    }
}