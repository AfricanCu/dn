using UnityEngine;
using System.Collections;
using LuaInterface;

public class CountDown {
    /// <summary>
    /// 序列号
    /// </summary>
    private int id = 0;
    /// <summary>
    /// 倒计时总时长
    /// </summary>
    private float countDown = 0f;
    /// <summary>
    /// 固定频率函数执行频率
    /// </summary>
    private float rate = 0f;
    /// <summary>
    /// 固定频率执行函数
    /// </summary>
    private LuaFunction funcRate = null;
    /// <summary>
    /// 结束函数
    /// </summary>
    private LuaFunction funcEnd = null;

    private float waitTime = 0f;

    public CountDown(int id, float countDown, float rate, LuaFunction funcRate, LuaFunction funcEnd)
    {
        this.id = id;
        this.countDown = countDown;
        this.rate = rate;
        this.funcRate = funcRate;
        this.funcEnd = funcEnd;

        this.waitTime = rate;
    }

    public int ID
    {
        get { return this.id; }
    }

    public bool FuncRate()
    {
        if(countDown <= 0f)
        {
            return false;
        }

        float deltaTime = RealTime.deltaTime;
        waitTime += deltaTime;
        countDown -= deltaTime;
        if (waitTime >= rate)
        {
            waitTime = 0f;
            if (funcRate != null)
            {
                funcRate.BeginPCall();
                int iCountDown = (int)countDown;
                iCountDown = iCountDown >= 0 ? iCountDown : 0;
                funcRate.Push(iCountDown.ToString());
                funcRate.Push(countDown);
                funcRate.PCall();
                funcRate.EndPCall();
            }
        }

        if(countDown <= 0f)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void FuncEnd()
    {
        if (funcEnd != null)
        {
            funcEnd.BeginPCall();
            funcEnd.PCall();
            funcEnd.EndPCall();
        }
    }

    public void Dispose()
    {
        if (funcEnd != null)
        {
            funcEnd.EndPCall();
            funcEnd.Dispose();
        }

        if (funcRate != null)
        {
            funcRate.EndPCall();
            funcRate.Dispose();
        }
    }
}
