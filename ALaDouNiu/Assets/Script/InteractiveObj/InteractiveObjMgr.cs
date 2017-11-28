using UnityEngine;
using System.Collections;
using System.Collections.Generic;

/// <summary>
/// 场景交互体管理器
/// </summary>
public class InteractiveObjMgr : MonoBehaviour
{
    public static InteractiveObjMgr Instance
    {
        get
        {
            if(null == _instance)
            {
                GameObject go = new GameObject();
                go.name = typeof(InteractiveObjMgr).Name;
                go.AddComponent<InteractiveObjMgr>();
            }
            return _instance;
        }
    }

    private static InteractiveObjMgr _instance;

    private readonly List<InteractiveObj> _Objs = new List<InteractiveObj>();

    private bool _acitve = false;

    private void Awake()
    {
        if (null != _instance)
        {
            Destroy(this);
        }
        else
        {
            _instance = this;
        }
    }


    // Use this for initialization
    void Start () {
	
	}
	
	// Update is called once per frame
	void Update ()
    {
        if ((Input.touchCount > 0 && Input.GetTouch(0).deltaPosition == Vector2.zero) || Input.GetMouseButtonDown(0))
        {
            if (_acitve)
                return;
            _acitve = true;

            Ray ray = UICamera.currentCamera.ScreenPointToRay(Input.mousePosition);
            if (Physics.Raycast(ray,int.MaxValue,1 << LayerMask.NameToLayer("UI")))
            {
                return;
            }
            ray = Camera.main.ScreenPointToRay(Input.mousePosition);
            RaycastHit hit;
            if (Physics.Raycast(ray,out hit,int.MaxValue, 1 << LayerMask.NameToLayer("SceneInteractive")))
            {
                InteractiveObj Interactive = hit.collider.gameObject.GetComponent<InteractiveObj>();
                if(null != Interactive)
                {
                    Interactive.Execute();
                }
            }
        }
        else if(Input.touchCount <= 0 && !Input.GetMouseButtonDown(0))
        {
            _acitve = false;
        }
    }

    /// <summary>
    /// 添加交互体到管理器
    /// </summary>
    /// <param name="obj"></param>
    public void AddObj(InteractiveObj obj)
    {
        _Objs.Add(obj);
    }

    /// <summary>
    /// 查找交互体
    /// </summary>
    /// <param name="name"></param>
    /// <returns></returns>
    public InteractiveObj GetByName(string name)
    {
        return _Objs.Find(obj => !string.IsNullOrEmpty(obj.Name)  && obj.Name == name);
    }
}
