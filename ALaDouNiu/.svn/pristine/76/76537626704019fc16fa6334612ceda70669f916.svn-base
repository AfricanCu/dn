using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using Asset.Script.Core;
using System;
using Object = UnityEngine.Object;

namespace Asset.Script.ABSystem
{
    public delegate void OnLoadComplate(AssetBundleAgant abAgant);

    /// <summary>
    /// 加载模式
    /// </summary>
    public enum LoadMode
    {
        /// <summary>
        /// 同步加载
        /// </summary>
        Syn,
        /// <summary>
        /// 异步加载
        /// </summary>
        Async,
    }

    public class ABManager : Singleton<ABManager>  
    {
        private readonly Dictionary<string, AssetBundleAgant> _abDic = new Dictionary<string, AssetBundleAgant>(StringComparer.OrdinalIgnoreCase);
        private AssetBundle _MainfestAB;
        private AssetBundleManifest _Mainfest;

        /// <summary>
        /// 加载AB
        /// </summary>
        /// <param name="abPath"></param>
        /// <param name="complate"></param>
        /// <param name="owner"></param>
        /// <param name="mode"></param>
        public void Load(string abPath, OnLoadComplate complate, Object owner = null, LoadMode mode = LoadMode.Syn)
        {
            abPath = FilePathTool.Instance.Normalization(abPath, Application.platform);
            if (null == _MainfestAB)
            {
                string AssetBundleManifestName = ResPathHelper.Instance.GetPlatformResFolderName(Application.platform);
                string AssetBundleManifestPath = ResPathHelper.Instance.LocalFilePath() +
                    @"/" + AssetBundleManifestName;
                Debug.Log("MainfestAB 路径:" + AssetBundleManifestPath);
                ABLoader loader = new ABLoader(AssetBundleManifestPath,true);
                loader.SynLoad((abAgant) =>
                 {
                     _MainfestAB = abAgant.m_AB;
                     _Mainfest = _MainfestAB.LoadAsset("assetbundlemanifest") as AssetBundleManifest;
                 });
            }

            string abName = Path.GetFileNameWithoutExtension(abPath).ToLower();
            if (_abDic.ContainsKey(abName))
            {
                Retain(abName, owner);
                complate(_abDic[abName]);
            }
            else
            {
                BeginLoad(abName, complate, owner, mode);
            }
        } 
        /// <summary>
        ///   卸载所有无效的AB
        /// </summary>
        public void UnLoadUnUse()
        {
            List<string> UnUse = new List<string>();
            foreach (var i in _abDic)
            {
                if (i.Value.m_IsStatic)
                    continue;
                if(i.Value.m_IsInvalid || i.Value.m_IsDispose)
                {
                    UnUse.Add(i.Key);
                }
            }
            for(int i=0;i < UnUse.Count;++i)
            {
                _abDic[UnUse[i]].Dispose();
                _abDic.Remove(UnUse[i]);
            }
        }

        /// <summary>
        /// 为AB指定引用计数
        /// </summary>
        /// <param name="abName"></param>
        /// <param name="refObj"></param>
        public void Retain(string abName,Object refObj)
        {
            AssetBundleAgant agant = null;
            _abDic.TryGetValue(abName, out agant);
            if (null != agant)
            {
                agant.Retain(refObj);
                List<string> deps = new List<string>();//为当前获取的AB所有依赖项添加引用计数
                GetAllDependent(abName, ref deps);
                for (int i = 0; i < deps.Count; ++i)
                {
                    string cur = deps[i];
                    _abDic[cur].Retain(refObj);//按照正常流程，一个AB被加载了，他的所有依赖必然被加载，
                }
            }
        }

        public bool ABIsHas(string name)
        {
            return _abDic.ContainsKey(name);
        }
        public void AddabAgant(string name,AssetBundleAgant abAgant)
        {
            if(_abDic.ContainsKey(name))
            {
                return;
            }
            _abDic.Add(name, abAgant);
        }
        private void BeginLoad(string abName, OnLoadComplate complate,Object owner, LoadMode mode)
        {
            //检查依赖
            List<string> deplist = new List<string>();
            GetAllDependent(abName, ref deplist);

            deplist.Add(abName);

            for (int i = 0; i < deplist.Count; ++i)
            {
                if (_abDic.ContainsKey(deplist[i]))
                {
                    Retain(deplist[i],owner);//已有AB增加引用计数
                    deplist.RemoveAt(i);//移除已经存在的AB，不需要参与加载
                    --i;
                    continue;
                }

                string path = FilePathTool.Instance.Normalization(ResPathHelper.Instance.LocalFilePath() + @"/" + deplist[i], Application.platform);
                deplist[i] = Path.GetFullPath(path);
            }

            if (LoadMode.Syn == mode)
                ProcessSynLoad(deplist.ToArray(), complate,owner);
            else if (LoadMode.Async == mode)
            {
                LoadCoroutineObj.Instance.StartCoroutine(ProcessAsyncLoad(deplist.ToArray(), complate, owner));
            }
        }
        /// <summary>
        /// 获取AB所有依赖项
        /// </summary>
        /// <param name="abName"></param>
        /// <param name="deplist"></param>
        public void GetAllDependent(string abName, ref List<string> deplist)
        {
            if (null == _Mainfest)//不存在依赖数据
            {
                Debug.LogError("不存在 Mainfest!!!!!!!");
                return;
            }
            string[] depNames = _Mainfest.GetDirectDependencies(abName);
            for (int i = 0; i < depNames.Length; ++i)
            {
                if (string.IsNullOrEmpty(depNames[i]))//有时候读取依赖，会获得空字符串的AB名字，Unity的BUG？
                    continue;
                if(!deplist.Contains(depNames[i]))
                {
                    deplist.Add(depNames[i]);
                    GetAllDependent(depNames[i], ref deplist);
                }
            }
        }
        private void ProcessSynLoad(string[] paths, OnLoadComplate complate,Object owner)
        {
            for (int i = 0; i < paths.Length; ++i)
            {
                string name = Path.GetFileNameWithoutExtension(paths[i]);
                ABLoader loader = new ABLoader(paths[i]);
                if (i != paths.Length - 1)
                    loader.SynLoad((agant) => { if(null != agant) agant.Retain(owner); });
                else
                    loader.SynLoad((agant) => { if (null != agant) agant.Retain(owner); complate(agant); });
            }
        }
        private IEnumerator ProcessAsyncLoad(string[] paths, OnLoadComplate complate,Object owner)
        {
            for(int i=0;i < paths.Length;++i)
            {
                string name = Path.GetFileNameWithoutExtension(paths[i]);
                ABLoader loader = new ABLoader(paths[i]);
                if( i != paths.Length - 1)
                    yield return loader.CreateASyncLoad((agant) => { if (null != agant) agant.Retain(owner); });
                else
                    yield return loader.CreateASyncLoad((agant) => { if (null != agant) agant.Retain(owner); complate(agant); });
            }
        }

#if (UNITY_EDITOR)
        public Dictionary<string, AssetBundleAgant> ABDic
        {
            get
            {
                return _abDic;
            }
        }
#endif
    }
}