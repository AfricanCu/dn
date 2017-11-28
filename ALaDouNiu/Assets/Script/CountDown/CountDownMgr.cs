using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using LuaInterface;

public class CountDownMgr : MonoBehaviour {

    public static CountDownMgr Instance
    {
        get
        {
            if (null == _instance)
            {
                GameObject go = new GameObject();
                go.name = typeof(CountDownMgr).Name;
                go.AddComponent<CountDownMgr>();
            }
            return _instance;
        }
    }

    private static CountDownMgr _instance;
    private Dictionary<int, CountDown> countDownDic = new Dictionary<int, CountDown>();
    private List<CountDown> countDownList = new List<CountDown>();
    //缓一帧加入countDownList中
    private List<CountDown> preCountDownList = new List<CountDown>();
    private int idSum = 0;

    void Awake()
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

    private List<CountDown> delatingList = new List<CountDown>();
    void Update()
    {
        if(countDownList.Count > 0)
        {
            for(int i = 0; i < countDownList.Count; i++)
            {
                if(countDownList[i] != null)
                {
                    bool rlt = countDownList[i].FuncRate();
                    if(!rlt)
                    {
                        delatingList.Add(countDownList[i]);
                    }
                }
            }

            if (delatingList.Count > 0)
            {
                for (int i = 0; i < delatingList.Count; i++)
                {
                    delatingList[i].FuncEnd();
                    delatingList[i].Dispose();

                    countDownDic.Remove(delatingList[i].ID);
                    countDownList.Remove(delatingList[i]);
                }

                delatingList.Clear();
            }
        }
    }

    private void LateUpdate()
    {
        if (preCountDownList.Count > 0)
        {
            lock(preCountDownList)
            {
                for (int i = 0; i < preCountDownList.Count; i++)
                {
                    if (preCountDownList[i] != null)
                    {
                        countDownList.Add(preCountDownList[i]);
                    }
                }

                preCountDownList.Clear();
            }
        }
    }

    public int CreateCountDown(float countDown, float rate, LuaFunction funcRate, LuaFunction funcEnd)
    {
        int newID = ++idSum;

        CountDown newCountDown = new CountDown(newID, countDown, rate, funcRate, funcEnd);
        countDownDic.Add(newID, newCountDown);
        lock (preCountDownList)
        {
            preCountDownList.Add(newCountDown);
        }
        return newID;
    }

    public int CreateCountDown(float countDown, float rate, LuaFunction funcEnd)
    {
        int newID = ++idSum;

        CountDown newCountDown = new CountDown(newID, countDown, rate, null, funcEnd);
        countDownDic.Add(newID, newCountDown);
        lock(preCountDownList)
        {
            preCountDownList.Add(newCountDown);
        }
        return newID;
    }


    public int CreateCountDown(float rate, LuaFunction funcRate)
    {
        int newID = ++idSum;

        CountDown newCountDown = new CountDown(newID, float.MaxValue, rate, funcRate, null);
        countDownDic.Add(newID, newCountDown);
        lock (preCountDownList)
        {
            preCountDownList.Add(newCountDown);
        }
        return newID;
    }

    public void RemoveCountDown(int id, bool doFuncEnd)
    {
        CountDown toRemove = null;
        countDownDic.TryGetValue(id, out toRemove);

        if(toRemove != null)
        {
            if (doFuncEnd)
            {
                toRemove.FuncEnd();
                toRemove.Dispose();
            }
            else
            {
                toRemove.Dispose();
            }

            countDownDic.Remove(id);
            if(countDownList.Contains(toRemove))
            {
                countDownList.Remove(toRemove);
            }
            lock (preCountDownList)
            {
                if (preCountDownList.Contains(toRemove))
                {
                    preCountDownList.Remove(toRemove);
                }
            }
        }
    }
}
