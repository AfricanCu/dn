using UnityEngine;
using System.Collections;
using LuaInterface;

public static class EventHelper
{
    public static void BindEvent(string MonoEventType,GameObject go, LuaFunction luafun)
    {
        BehaviourEvent Event = go.AddComponent<BehaviourEvent>();
        switch (MonoEventType)
        {
            case "Start":
                {
                    Event.StartEvent = luafun;
                }
                break;
            case "OnDestroy":
                {
                    Event.OnDestoryEvent = luafun;
                }
                break;
            case "OnEnable":
                {
                    Event.OnEnableEvent = luafun;
                }
                break;
            case "OnDisable":
                {
                    Event.OnDisableEvent = luafun;
                }
                break;
            case "OnApplicationFocus":
                {
                    Event.OnApplicationFocusEvent = luafun;
                }
                break;
            case "OnApplicationPause":
                {
                    Event.OnApplicationPauseEvent = luafun;
                }
                break;
            default:
                {
                    luafun.Dispose();
                }
                break;
        }
    }
    public static void RegUpdate(LuaFunction luafun)
    {
        BehaviourUpdateEvent.Instance.UpdateEvent = luafun;
    }
}
