using UnityEngine;
using System.Collections;
using LuaInterface;
using System;

public class BehaviourEvent : MonoBehaviour
{
    public LuaFunction StartEvent;
    public LuaFunction OnDestoryEvent;
    public LuaFunction OnEnableEvent;
    public LuaFunction OnDisableEvent;
    public LuaFunction OnApplicationFocusEvent;
    public LuaFunction OnApplicationPauseEvent;

    public Action StartAction;
    public Action OnDestoryAction;
    public Action OnEnableAction;
    public Action OnDisableAction;
    public Action OnApplicationFocusAction;
    public Action OnApplicationPauseAction;


    private void Start()
    {
        if (null != StartEvent)
        {
            StartEvent.BeginPCall();
            StartEvent.PCall();
            StartEvent.EndPCall();
        }
        if (null != StartAction)
            StartAction();
    }
    private void OnDestroy()
    {
        if (null != OnDestoryEvent)
        {
            OnDestoryEvent.BeginPCall();
            OnDestoryEvent.PCall();
            OnDestoryEvent.EndPCall();
        }
        if (null != OnDestoryAction)
            OnDestoryAction();
        Release();
    }
    private void OnEnable()
    {
        if (null != OnEnableEvent)
        {
            OnEnableEvent.BeginPCall();
            OnEnableEvent.PCall();
            OnEnableEvent.EndPCall();
        }
        if (null != OnEnableAction)
            OnEnableAction();
    }
    private  void OnDisable()
    {
        if (null != OnDisableEvent)
        {
            OnDisableEvent.BeginPCall();
            OnDisableEvent.PCall();
            OnDisableEvent.EndPCall();
        }
        if (null != OnDisableAction)
            OnDisableAction();
    }

    private void OnApplicationFocus(bool focusStatus)
    {
        if (null != OnApplicationFocusEvent)
        {
            OnApplicationFocusEvent.BeginPCall();
            OnApplicationFocusEvent.Push(focusStatus);
            OnApplicationFocusEvent.PCall();
            OnApplicationFocusEvent.EndPCall();
        }
        if (null != OnApplicationFocusAction)
            OnApplicationFocusAction();
    }

    private void OnApplicationPause(bool pause)
    {
        if(null != OnApplicationPauseEvent)
        {
            OnApplicationPauseEvent.BeginPCall();
            OnApplicationPauseEvent.Push(pause);
            OnApplicationPauseEvent.PCall();
            OnApplicationPauseEvent.EndPCall();
        }

        if (null != OnApplicationPauseAction)
            OnApplicationPauseAction();
    }

    public virtual void Release()
    {
        if (null != StartEvent) { StartEvent.Dispose(); }
        if (null != OnDestoryEvent) { OnDestoryEvent.Dispose(); }
        if (null != OnEnableEvent) { OnEnableEvent.Dispose(); }
        if (null != OnDisableEvent) { OnDisableEvent.Dispose(); }
        if (null != OnApplicationFocusEvent) { OnApplicationFocusEvent.Dispose(); }
        if (null != OnApplicationPauseEvent) { OnApplicationPauseEvent.Dispose(); }
    }
}

public class BehaviourUpdateEvent : BehaviourEvent
{
    public LuaFunction UpdateEvent;
    public Action UpdateAction;

    public static BehaviourUpdateEvent Instance
    {
        get
        {
            if(null == _Instance)
            {
                GameObject go = new GameObject();
                go.AddComponent<BehaviourUpdateEvent>();
            }
            return _Instance;
        }
    }
    private static BehaviourUpdateEvent _Instance;
    private void Awake()
    {
        if(null != _Instance)
        {
            Destroy(this);
            return;
        }
        else
        {
            _Instance = this;
        }
    }
    private void Update()
    {
        if (null != UpdateEvent)
        {
            UpdateEvent.BeginPCall();
            UpdateEvent.PCall();
            UpdateEvent.EndPCall();
        }
        if(null != UpdateAction)
            UpdateAction();
    }
    public override void Release()
    {
        base.Release();
        if (null != UpdateEvent) { UpdateEvent.Dispose(); }
    }
    private void OnDestory()
    {
        Release();
    }
}