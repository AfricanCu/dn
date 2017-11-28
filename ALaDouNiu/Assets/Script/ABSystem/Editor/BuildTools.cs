using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEditor;
using System.IO;
using Asset.Script.Tools;
using Asset.Script.Update;

namespace Asset.Script.ABSystem
{
    public class BuildTools
    {
        public const string version = CheckVersion.Version;

        [MenuItem("工具/BuildTools/当前版本号" + version)]
        public static void BuildAll()
        {
// #if (UNITY_EDITOR_OSX)
//              BuildAB(BuildTarget.iOS);
// #else
//             BuildAB(BuildTarget.Android);
//             BuildAB(BuildTarget.StandaloneWindows64);    
// #endif
        }
        [MenuItem("工具/BuildTools/打开输出目录")]
        public static void OpenOutputPath()
        {
            string path = FilePathTool.Instance.Normalization(ABSystemConfig.Instance.ABOutputPath, BuildTarget.iOS);
            if (!Directory.Exists(path))
            {
                Directory.CreateDirectory(path);
            }
            Application.OpenURL(path);
            Debug.Log("输出路径：" + path);
        }

        [MenuItem("工具/BuildTools/ClearABTag")]
        public static void ClearABTag()
        {
            string[] all = AssetDatabase.GetAllAssetPaths();
            for(int i=0;i< all.Length;++i)
            {
                AssetImporter importer = AssetImporter.GetAtPath(all[i]);
                importer.assetBundleName = "";
            }
            Debug.Log("清除AB标记完成");
        }

        [MenuItem("工具/BuildTools/TagAB")]
        public static void TagAB()
        {
            TagRes(BuildTarget.Android);
        }

#if (UNITY_EDITOR_OSX)
        [MenuItem("工具/BuildTools/打包AB for IOS")]
        public static void BuildForIOS()
        {
             BuildAB(BuildTarget.iOS);
        }
#else
        [MenuItem("工具/BuildTools/打包AB for Win64")]
        public static void BuildForWin64()
        {
            BuildAB(BuildTarget.StandaloneWindows64);
        }
        [MenuItem("工具/BuildTools/打包AB for Android")]
        public static void BuildForAndroid()
        {
            BuildAB(BuildTarget.Android);
        }
#endif

        private static void ExportLua(string OutputPath)
        {
            string exportPath = OutputPath + "/Lua";
#if (UNITY_EDITOR_OSX)
            exportPath =  FilePathTool.Instance.Normalization(exportPath,BuildTarget.iOS);
#endif
            if (!Directory.Exists(exportPath))
            {
                Directory.CreateDirectory(exportPath).Refresh();
            }
            DirectoryInfo oldInfo = new DirectoryInfo(exportPath);
            foreach (FileInfo file in oldInfo.GetFiles("*.lua", SearchOption.AllDirectories))
            {
                file.Delete();
            }

            string formPath = Path.GetFullPath(Application.dataPath + @"\lua");
            CopyLuaScript(formPath, exportPath);
            formPath = Path.GetFullPath(Application.dataPath + @"\ToLua\Lua");
            CopyLuaScript(formPath, exportPath);
        }
        private static void CopyLuaScript(string formPath, string exportPath)
        {
#if (UNITY_EDITOR_OSX)
            formPath =  FilePathTool.Instance.Normalization(formPath,BuildTarget.iOS);
#endif
            DirectoryInfo dirInfo = new DirectoryInfo(formPath);
            foreach (FileInfo file in dirInfo.GetFiles("*.lua", SearchOption.AllDirectories))
            {
                string subPath = file.FullName.Replace(formPath, "");
                string newfullPath = exportPath + subPath;
                string newPath = Path.GetDirectoryName(newfullPath);
                if (!Directory.Exists(newPath))
                {
                    Directory.CreateDirectory(newPath);
                }

                using (FileStream SourceFs = File.OpenRead(file.FullName))
                {
                    using (FileStream targetFs = File.Create(newfullPath))
                    {
                        byte[] source = new byte[SourceFs.Length];
                        SourceFs.Read(source, 0, source.Length);
                        source = EncryptionTool.Instance.Encryption(source);//对Lua代码进行加密
                        targetFs.Write(source, 0, source.Length);
                    }
                }
            }
        }
        private static void TagRes(BuildTarget target)
        {
            List<string> ignorelist = new List<string>();//忽略列表，列表中路径下的资源将不会被打包
            ignorelist.AddRange(new string[]
            {
                "Assets/Res/AtlasOrigin",
                "Assets/Resources/Default",
            });

            string[] paths = 
            {
                "/Res",
                "/Prefab",
                "/Scenes",
                "/Resources",
            };
            for (int i = 0; i < paths.Length; ++i)
            {
                string path = Application.dataPath + paths[i];
                DirectoryInfo dir = new DirectoryInfo(path);
                FileInfo[] files = dir.GetFiles("*.*",SearchOption.AllDirectories);

                for (int j=0;j < files.Length;++j )
                {
                    FileInfo cur = files[j];
                    if (cur.Name.EndsWith(".meta"))
                        continue;

                    string fullPath = FilePathTool.Instance.Normalization(cur.FullName, target);
                    string basePath = fullPath.Replace(Application.dataPath, "Assets");
                    string fileName = Path.GetFileName(fullPath);

                    AssetImporter importer = AssetImporter.GetAtPath(basePath);

                    if(null  == importer)
                    {
                        continue;
                    }

                    bool NoIgnore = string.IsNullOrEmpty(ignorelist.Find(a => basePath.Contains(a))) ;
                    if (!NoIgnore)
                    {
                        importer.assetBundleName = "";
                        continue;
                    }

                    if (fileName.Contains(" "))
                    {
                        Debug.LogError("文件 " + fileName + " 命名非法!!!!!");
                    }
                    importer.assetBundleName = Path.GetFileNameWithoutExtension(cur.Name).ToLower();
                }
            }

            AssetDatabase.Refresh();
        }
        private static void BuildAB(BuildTarget target)
        {
            string OutputPath = ABSystemConfig.Instance.GetOutputPath(target);

            if (!Directory.Exists(OutputPath))
            {
                Directory.CreateDirectory(OutputPath);
            }

            string ManifestName = Path.GetFileName(OutputPath);

            TagRes(target);

            AssetBundleManifest NowManifest = BuildPipeline.BuildAssetBundles(OutputPath,
                BuildAssetBundleOptions.None, target);

            ExportLua(OutputPath);

            //创建MD5列表和版本号文件
            CreateMD5ListAndVersionFile(OutputPath, target);
            Application.OpenURL(OutputPath);
            Debug.Log("输出目录：" + OutputPath);
        }
        private static void CreateMD5ListAndVersionFile(string OutputPath, BuildTarget target)
        {
            string subPath = ABSystemConfig.Instance.GetOutputSubPath(target);
            OutputPath = FilePathTool.Instance.Normalization(OutputPath, target);
            //创建版本号文件
            string versionFilePath = FilePathTool.Instance.Normalization(OutputPath + "/" + UpdateConfig.Instance.VersionFileName, target);
            using (StreamWriter sw = File.CreateText(versionFilePath))
            {
                sw.Write(version);
                sw.Flush();
            }

            string md5path = FilePathTool.Instance.Normalization(OutputPath + @"/md5.txt", target);
            using (StreamWriter sw = File.CreateText(md5path))
            {
                DirectoryInfo dirInfo = new DirectoryInfo(OutputPath);
                FileInfo[] files = dirInfo.GetFiles("*.*", SearchOption.AllDirectories);
                foreach (FileInfo file in files)
                {
                    string extension = Path.GetExtension(file.FullName);

                    if (extension == Path.GetExtension(UpdateConfig.Instance.VersionFileName))//跳过版本号文件
                    {
                        continue;
                    }

                    if (extension != "" 
                        && extension != ".lua")
                    {
                        continue;
                    }
                    string fullPath = FilePathTool.Instance.Normalization(file.FullName, target);
                    string localPath = fullPath.Replace(OutputPath, "");
                    localPath = FilePathTool.Instance.Normalization(localPath, target);
                    string line = localPath
                        + "\t" + MD5Tool.Instance.GetMD5(file.FullName);

                    sw.WriteLine(line);
                }

                //最后写入版本号文件，这样只有在所有下载完成后，才会更新版本号
                string versionlocalPath = versionFilePath.Replace(FilePathTool.Instance.Normalization(OutputPath, target), "");
                versionlocalPath = FilePathTool.Instance.Normalization(versionlocalPath, target);
                string versionline = versionlocalPath
                    + "\t" + MD5Tool.Instance.GetMD5(versionFilePath);
                sw.WriteLine(versionline);

                sw.Flush();
            }

            ZipTools.PackResZip(OutputPath,target);

        }
    }
}