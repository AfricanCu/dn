using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using LuaInterface;

public class FsmShell {
    public delegate void FsmShellEvent();

    private FsmShellEvent beginEvent = null;
    private FsmShellEvent endEvent = null;
    private Dictionary<float, FsmShellEvent> updateEvent = null;
    private List<float> updateTimeStamp = null;
    private float passedTime = 0;
    private int updateShellIndex = 0;

    public FsmShell()
    {
        if (updateEvent == null)
        {
            updateEvent = new Dictionary<float, FsmShellEvent>();
        }

        if(updateTimeStamp == null)
        {
            updateTimeStamp = new List<float>();
        }

        UnRegisterAllUpdateEvent();
        passedTime = 0;
    }

    public void RegisterBeginEvent(LuaFunction beginEvent)
    {
        if (beginEvent != null)
        {
            this.beginEvent = () =>
            {
                beginEvent.BeginPCall();
                beginEvent.PCall();
                beginEvent.EndPCall();
            };
        }

    }

    public void RegisterEndEvent(LuaFunction endEvent)
    {
        if (endEvent != null)
        {
            this.endEvent = () =>
            {
                endEvent.BeginPCall();
                endEvent.PCall();
                endEvent.EndPCall();
            };
        }
    }


    public void UnRegisterAllUpdateEvent()
    {
        updateEvent.Clear();
        updateTimeStamp.Clear();
        updateShellIndex = 0;
    }

    public void RegisterUpdateEvent(float timeStamp, LuaFunction updateShellEvent)
    {
        float tmpTimeStamp = timeStamp + passedTime;
        updateTimeStamp.Add(tmpTimeStamp);
        updateEvent[tmpTimeStamp] = () =>
        {
            updateShellEvent.BeginPCall();
            updateShellEvent.PCall();
            updateShellEvent.EndPCall();
        };

        updateTimeStamp.Sort();
    }

    public void Begin()
    {
        if(beginEvent != null)
        {
            beginEvent();
        }
    }

    public void Update(float delataTime)
    {
        if(updateShellIndex < updateTimeStamp.Count)
        {
            float timeStamp = updateTimeStamp[updateShellIndex];
            if (timeStamp <= passedTime)
            {
                updateShellIndex++;
                if (updateEvent[timeStamp] != null)
                {
                    updateEvent[timeStamp]();
                }
            }
        }

        passedTime += delataTime;
    }

    public void End()
    {
        if (endEvent != null)
        {
            endEvent();
        }

        UnRegisterAllUpdateEvent();
    }
}
