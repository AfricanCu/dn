using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;
using Object = UnityEngine.Object;

namespace Asset.Script.ABSystem
{
    public class AssetBundleAgant 
    {
        public delegate void OnAyncLoadOver(Object asset);

        public string m_ABName
        {
            get { return _ABName; }
        }
        /// <summary>
        /// 是否失效
        /// </summary>
        public bool m_IsInvalid
        {
            get
            {
                if(_isStatic)
                {
                    return false;
                }
                return  UpdateReference() == 0;
            }
        }
        /// <summary>
        /// 当前引用计数
        /// </summary>
        public int m_CurRefCount
        {
            get { return UpdateReference(); }
        }
        /// <summary>
        /// 是否被卸载
        /// </summary>
        public bool m_IsDispose
        {
            get { return _IsDispose; }
        }
        /// <summary>
        /// 是否是静态
        /// </summary>
        public bool m_IsStatic
        {
            get
            {
                return _isStatic;
            }
        }

        public AssetBundle m_AB
        {
            get { return _AB; }
        }

        public int m_LoadUseTime
        {
            get
            {
                return _loadUseTime;
            }
        }

        private readonly AssetBundle _AB;
        private readonly List<WeakReference> _References = new List<WeakReference>();

        private float _StartTime;
        private bool _IsDispose = false;
        private string _ABName = null;
        private bool _isStatic;
        private int _loadUseTime;

        public AssetBundleAgant(string abName,AssetBundle ab, int loadUseTime,bool isStatic = false)
        {
            _AB = ab;
            _ABName = abName;
            _StartTime = Time.time;
            _loadUseTime = loadUseTime;
            _isStatic = isStatic;
        }

        public void ASyncLoadAsset(OnAyncLoadOver onLoadOver)
        {
            LoadCoroutineObj.Instance.StartCoroutine_Auto(ProcessAsyncLoad(onLoadOver));
        }

        private IEnumerator ProcessAsyncLoad(OnAyncLoadOver onLoadOver)
        {
            AssetBundleRequest request = _AB.LoadAssetAsync(_ABName);
            yield return request;
            if(!request.isDone)
            {
                onLoadOver(null);
            }
            else
            {
                onLoadOver(request.asset);
            }
        }

        public void Retain(Object owner)
        {
            if (owner == null)
            {
                return;
            }

            for (int i = 0; i < _References.Count; i++)
            {
                if (object.ReferenceEquals(owner,_References[i].Target)) //对比是否是同一引用
                    return;
            }

            WeakReference wr = new WeakReference(owner);
            _References.Add(wr);
        }

        private int UpdateReference()
        {
            for (int i = 0; i < _References.Count; i++)
            {
                Object o = (Object)_References[i].Target;
                if (null == o)
                {
                    _References.RemoveAt(i);
                    i--;
                }
            }
            return _References.Count;
        }

        public void Dispose()
        {
            if(_isStatic)
            {
                return;
            }
            if(null != _AB)
                _AB.Unload(true);
            _IsDispose = true;
        }
    }
}