using UnityEngine;
using System.Collections;
using Asset.Script.Core;

namespace Asset.Script.ABSystem
{
    public class ABSystemConfig : Singleton<ABSystemConfig>
    {

        public readonly string ABOutputPath = Application.persistentDataPath + @"\AssetBundlesOutput";

        public readonly string ChangedFileOutputPath = @"\Changes";
        public readonly string ALLZipPackageOutputPath = @"\ALL.zip";
        public readonly string UpdateZipPackageOutputPath = @"\Changes\Update.zip";
        /// <summary>
        /// 获取AB打包输出路径
        /// </summary>
        /// <param name="target"></param>
        /// <returns></returns>
        public string GetOutputPath(UnityEditor.BuildTarget target)
        {
            return ABOutputPath + GetOutputSubPath(target);
        }

        public string GetOutputSubPath(UnityEditor.BuildTarget target)
        {
            switch (target)
            {
                case UnityEditor.BuildTarget.StandaloneWindows64:
                    {
                        return @"\Win";
                    }
                case UnityEditor.BuildTarget.Android:
                    {
                        return  @"\Android";
                    }
                case UnityEditor.BuildTarget.iOS:
                    {
                        return  @"\iOS";
                    }
                default:
                    {
                        return string.Empty;
                    }
            }
        }

    }
}