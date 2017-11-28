using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.IO;

namespace Asset.Script.ABSystem
{
    public class ABLoader
    {
        private string _path;
        private string _Name;
        private bool _isStatic;

        private static Dictionary<string, ASyncInfo> _ASyncDic = new Dictionary<string, ASyncInfo>();

        public ABLoader(string path, bool isStatic = false)
        {
            _path = path;
            _Name = Path.GetFileNameWithoutExtension(_path);
            _isStatic = isStatic;
        }

        public void SynLoad(OnLoadComplate complate)
        {
            if (!File.Exists(_path))
            {
                Debug.Log("不存在 AssetBundle :" + _path);
                if (null != complate)
                    complate(null);
                return;
            }

            int start = System.Environment.TickCount;
            AssetBundle ab = AssetBundle.LoadFromFile(_path);
            int end = System.Environment.TickCount;

            if (null == ab)
            {
                if (null != complate)
                    complate(null);
                return;
            }

            AssetBundleAgant abAgant = new AssetBundleAgant(_Name, ab, end - start, this._isStatic);
            ABManager.Instance.AddabAgant(_Name, abAgant);

            if (null != complate)
                complate(abAgant);
        }

        public IEnumerator CreateASyncLoad(OnLoadComplate complate)
        {   
            if(_ASyncDic.ContainsKey(this._Name))
            {
                _ASyncDic[_Name].AddCallBack(complate);
                yield break;
            }
            _ASyncDic.Add(_Name, new ASyncInfo(_Name,complate));
            yield return AsyncLoad(_ASyncDic[_Name].Execute);
            _ASyncDic.Remove(_Name);
        }

        public IEnumerator AsyncLoad(OnLoadComplate complate)
        {
            if (!File.Exists(_path))
            {
                Debug.Log("不存在 AssetBundle :" + _path);
                if (null != complate)
                    complate(null);
                yield break;
            }

            int start = System.Environment.TickCount;
            AssetBundleCreateRequest abcReq = AssetBundle.LoadFromFileAsync(_path);
            yield return abcReq;
            int end = System.Environment.TickCount;

            if (null == abcReq.assetBundle || !abcReq.isDone)
            {
                if (null != complate)
                    complate(null);
                Debug.LogError("读取AB异常 :" + _path);
                yield break;
            }

            AssetBundleAgant abAgant = new AssetBundleAgant(_Name, abcReq.assetBundle, end - start, this._isStatic);
            ABManager.Instance.AddabAgant(_Name, abAgant);

            if (null != complate)
                complate(abAgant);
        }

    }
}
