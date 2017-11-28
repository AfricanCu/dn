using UnityEngine;
using System.Collections;
using ICSharpCode.SharpZipLib.Zip;
using UnityEditor;
using System.IO;

namespace Asset.Script.ABSystem
{

    public static class ZipTools 
    {
        public static void PackResZip(string sourcePath,BuildTarget target)
        {
            string Zipfullpath = sourcePath + ".zip";
            Zipfullpath = FilePathTool.Instance.Normalization(Zipfullpath, target);
            FastZip fastZip = new FastZip();
            fastZip.CreateZip(Zipfullpath, sourcePath,true,"");
        }
    }

}
