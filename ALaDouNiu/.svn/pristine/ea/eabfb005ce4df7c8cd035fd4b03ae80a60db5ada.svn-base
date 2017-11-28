using UnityEngine;
using System.Collections;
using LuaInterface;

public class TwirlMgr : MonoBehaviour
{
    public static TwirlMgr Instance
    {
        get
        {
            if (null == _instance)
            {
                GameObject go = new GameObject();
                go.name = typeof(TwirlMgr).Name;
                go.AddComponent<TwirlMgr>();
            }
            return _instance;
        }
    }

    private static TwirlMgr _instance;

    void Awake()
    {
        if (null != _instance)
        {
            Destroy(this);
        }
        else
        {
            if(twirl == null)
            {
                twirl = Camera.main.GetComponent<TwirlScripts>();
            }
            setAngle = 0f;
            haveTask = false;
            _instance = this;
        }
    }

    private TwirlScripts twirl;
    private float usedTime = 0f;
    private float gap = 0f;
    private float speed = 0f;
    public bool haveTask = false;
    public float setAngle = 0f;
    public float duration = 1.5f;
    private LuaFunction switchFunc = null;
    private LuaFunction endFunc = null;
    [ContextMenu("Day")]
    public void SetToDay(LuaFunction func, LuaFunction endFunc)
    {
        setAngle = 180f;
        usedTime = 0f;
        if (twirl != null)
        {
            gap = twirl.angle - setAngle;
            speed = gap / duration;
            switchFunc = func;
            this.endFunc = endFunc;
            haveTask = true;
        }
        else
        {
            gap = 0f;
            haveTask = false;
            if(func != null)
            {
                func.BeginPCall();
                func.PCall();
                func.EndPCall();
            }

            if (endFunc != null)
            {
                endFunc.BeginPCall();
                endFunc.PCall();
                endFunc.EndPCall();
            }
        }
        
    }

    [ContextMenu("Night")]
    public void SetToNight(LuaFunction func, LuaFunction endFun)
    {
        setAngle = -180f;
        usedTime = 0f;
        if (twirl != null)
        {
            gap = twirl.angle - setAngle;
            speed = gap / duration;
            switchFunc = func;
            this.endFunc = endFun;
            haveTask = true;
        }
        else
        {
            gap = 0f;
            haveTask = false;
            if (func != null)
            {
                func.BeginPCall();
                func.PCall();
                func.EndPCall();
            }

            if (endFunc != null)
            {
                endFunc.BeginPCall();
                endFunc.PCall();
                endFunc.EndPCall();
            }
        }
    }

    void Update()
    {
        if(haveTask && twirl != null && gap != 0)
        {
            float change = speed * Time.deltaTime;
            float newGap = gap - change;

            if(usedTime >= duration*0.5f && switchFunc != null)
            {
                switchFunc.BeginPCall();
                switchFunc.PCall();
                switchFunc.EndPCall();
                switchFunc.Dispose();
                switchFunc = null;
            }

            if(newGap*gap <= 0)
            {
                twirl.angle = 0;
                haveTask = false;
                gap = 0;
                usedTime = 0f;

                if (endFunc != null)
                {
                    endFunc.BeginPCall();
                    endFunc.PCall();
                    endFunc.EndPCall();
                    endFunc.Dispose();
                    endFunc = null;
                }
            }
            else
            {
                twirl.angle += change;
                gap = newGap;
                usedTime += Time.deltaTime;
            }
        }
    }

}