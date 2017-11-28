using UnityEngine;
using System.Collections;
using Asset.Script.Core;
using System.IO;
using System.Security.Cryptography;

namespace Asset.Script.Tools
{
    public class MD5Tool : Singleton<MD5Tool>
    {
        /// <summary>
        /// 获取文件MD5码
        /// </summary>
        /// <param name="filePath"></param>
        /// <returns></returns>
        public string GetMD5(string filePath)
        {
            MD5CryptoServiceProvider md5 = new MD5CryptoServiceProvider();
            using (FileStream fs = File.OpenRead(filePath))
            {
                byte[] bytValue = new byte[fs.Length];
                int total = fs.Read(bytValue, 0, bytValue.Length);
                byte[] bytHash = md5.ComputeHash(bytValue);
                md5.Clear();
                string md5code = "";
                for (int i = 0; i < bytHash.Length; i++)
                {
                    md5code += bytHash[i].ToString("X").PadLeft(2, '0');
                }
                return md5code.ToLower();
            }
        }
    }
}


