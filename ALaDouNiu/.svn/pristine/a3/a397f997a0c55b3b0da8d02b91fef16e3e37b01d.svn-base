using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;

namespace Asset.Script.ABSystem
{
    public class ASyncInfo 
    {
        /// <summary>
        /// 正在异步加载的AB名字
        /// </summary>
        public readonly string _abName = null;
        private readonly List<OnLoadComplate> _callBacks;

        public ASyncInfo(string abName,OnLoadComplate oncomplate)
        {
            _abName = abName;
            _callBacks = new List<OnLoadComplate>();
            AddCallBack(oncomplate);
        }

        /// <summary>
        /// 添加回调
        /// </summary>
        /// <param name="oncomplate"></param>
        public void AddCallBack(OnLoadComplate oncomplate)
        {
            _callBacks.Add(oncomplate);
        }
        
        /// <summary>
        /// 执行回调
        /// </summary>
        /// <param name="agant"></param>
        public void Execute(AssetBundleAgant agant)
        {
            for(int i=0;i< _callBacks.Count;++i)
            {
                try
                {
                    _callBacks[i](agant);
                }
                catch (Exception ex)
                {
                    Debug.LogError(ex);
                    continue;
                }
            }
        }
    }
}