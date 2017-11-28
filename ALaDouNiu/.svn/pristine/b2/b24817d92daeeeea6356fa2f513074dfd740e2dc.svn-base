using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using LuaInterface;

/// <summary>
/// 场景交互体
/// </summary>
public class InteractiveObj : MonoBehaviour
{
    public string Name;

    public LuaFunction LuaCallBack;
    public List<EventDelegate> CallBacks;
         
    /// <summary>
    /// 执行
    /// </summary>
    /// <param name="obj"></param>
    public void Execute()
    {
        if (null != LuaCallBack)
        {
            LuaCallBack.BeginPCall();
            LuaCallBack.Push(this.gameObject);
            LuaCallBack.PCall();
            LuaCallBack.EndPCall();
        }
        if (null != CallBacks)
        {
            EventDelegate.Execute(CallBacks);
        }
    }

    private void Awake()
    {
        InteractiveObjMgr.Instance.AddObj(this);
    }

    // Use this for initialization
    void Start () {
	
	}
	

    private void Reset()
    {
        this.gameObject.layer = LayerMask.NameToLayer("SceneInteractive");
    }

    private void OnDestroy()
    {
        if (null != LuaCallBack)
        {
            LuaCallBack.Dispose();
        }
    }
}
