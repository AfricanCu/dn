using UnityEngine;
using System.Collections;

namespace  Asset.Script.ABSystem
{
    public class LoadCoroutineObj : MonoBehaviour
    {
        public static LoadCoroutineObj Instance
        {
            get
            {
                if (null == _instance)
                {
                    GameObject go = new GameObject();
                    go.name = "别删我，我只想安安静静做个GO";
                    _instance = go.AddComponent<LoadCoroutineObj>();
                }
                return _instance;
            }

        }

        private static LoadCoroutineObj _instance;

        void Start()
        {

        }

        void Update()
        {

        }


        private void OnDestory()
        {
            _instance = null;
        }
    }
}
