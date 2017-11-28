using UnityEngine;
using System.Collections;
using LuaInterface;

public class DayNightMgr : MonoBehaviour
{
    public static DayNightMgr Instance
    {
        get
        {
            if (null == _instance)
            {
                GameObject go = new GameObject();
                go.name = typeof(DayNightMgr).Name;
                go.AddComponent<DayNightMgr>();
            }
            return _instance;
        }
    }

    private static DayNightMgr _instance;

    void Awake()
    {
        if (null != _instance)
        {
            Destroy(this);
        }
        else
        {
            twirl = Camera.main.GetComponent<CC_ContrastVignette>();
            s_sharpness = twirl.sharpness;
            s_darkness = twirl.darkness;
            isWork = false;
            _instance = this;
        }
    }

    private CC_ContrastVignette twirl;
    private float sharpness;
    private float darkness;
    private float d_sharpness;
    private float d_darkness;
    private float s_sharpness;
    private float s_darkness;
    private float duration = 1f;
    private float startDelay = 0f;
    private float usedTime = 0f;
    private bool isWork = false;
    private LuaFunction startFunc = null;
    private LuaFunction midFunc = null;
    private LuaFunction endFunc = null;
    private EasingCurvesUtil.EaseType easeType = EasingCurvesUtil.EaseType.easeOutQuad;

    [ContextMenu("Day")]
    public void Switch(LuaFunction startFunc, LuaFunction midFunc, LuaFunction endFunc, float duration, float startDelay)
    {
        if (twirl == null)
            return;

        twirl.enabled = true;
        sharpness = twirl.sharpness;
        darkness = twirl.darkness;
        d_sharpness = -250;
        d_darkness = 200;
        this.duration = duration;
        this.startDelay = startDelay;
        this.startFunc = startFunc;
        this.midFunc = midFunc;
        this.endFunc = endFunc;
        usedTime = 0;
        isWork = true;
    }

    public void SetParams(float sharpness, float darkness)
    {
        if (twirl == null)
            return;

        twirl.enabled = true;
        twirl.sharpness = sharpness;
        twirl.darkness = darkness;
    }

    public void ResetParams()
    {
        if (twirl == null)
            return;

        twirl.sharpness = s_sharpness;
        twirl.darkness = s_darkness;
        twirl.enabled = false;
    }

    void Update()
    {
        if(isWork)
        {
            if (twirl == null)
            {
                isWork = false;
                return;
            }
                
            //第一帧时间比较久
            if (usedTime <= startDelay)
            {
                usedTime += Time.deltaTime;
                return;
            }
            else if(usedTime <= duration + startDelay)
            {
                float v = (usedTime - startDelay) / duration;
                float tmp_sharpness = EasingCurvesUtil.Easing(s_sharpness, d_sharpness, v, easeType);
                float tmp_darkness = EasingCurvesUtil.Easing(s_darkness, d_darkness, v, easeType);

                twirl.sharpness = tmp_sharpness;
                twirl.darkness = tmp_darkness;
            }
            else if(usedTime <= duration*2f + startDelay)
            {
                float v = (usedTime - duration - startDelay) / duration;
                float tmp_sharpness = EasingCurvesUtil.Easing(d_sharpness, s_sharpness, v, easeType);
                float tmp_darkness = EasingCurvesUtil.Easing(d_darkness, s_darkness, v, easeType);

                twirl.sharpness = tmp_sharpness;
                twirl.darkness = tmp_darkness;
            }
            else
            {
                twirl.enabled = false;
                if (startFunc != null)
                {
                    startFunc.BeginPCall();
                    startFunc.PCall();
                    startFunc.EndPCall();
                    startFunc.Dispose();
                    startFunc = null;
                }

                if (midFunc != null)
                {
                    midFunc.BeginPCall();
                    midFunc.PCall();
                    midFunc.EndPCall();
                    midFunc.Dispose();
                    midFunc = null;
                }

                if (endFunc != null)
                {
                    endFunc.BeginPCall();
                    endFunc.PCall();
                    endFunc.EndPCall();
                    endFunc.Dispose();
                    endFunc = null;
                }
                isWork = false;
            }

            usedTime += Time.deltaTime;

            if (usedTime > startDelay)
            {
                if (startFunc != null)
                {
                    startFunc.BeginPCall();
                    startFunc.PCall();
                    startFunc.EndPCall();
                    startFunc.Dispose();
                    startFunc = null;
                }
            }

            if (usedTime > duration + startDelay)
            {
                if (midFunc != null)
                {
                    midFunc.BeginPCall();
                    midFunc.PCall();
                    midFunc.EndPCall();
                    midFunc.Dispose();
                    midFunc = null;
                }
            }

        }
    }

}