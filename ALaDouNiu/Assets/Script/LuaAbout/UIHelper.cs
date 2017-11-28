using UnityEngine;
using System.Collections;
using LuaInterface;

public static class UIHelper
{
    /// <summary>
    /// 获取2DUIRoot
    /// </summary>
    /// <returns></returns>
    public static UIRoot Get2DRoot()
    {
        if (null == _2dRoot)
        {
            _2dRoot = GameObject.Find("2DUIRoot").GetComponent<UIRoot>();
        }
        return _2dRoot;
    }
    /// <summary>
    /// 获取3DUIRoot
    /// </summary>
    /// <returns></returns>
    public static UIRoot Get3DRoot()
    {
        if (null == _3dRoot)
        {
            _3dRoot = GameObject.Find("3DUIRoot").GetComponent<UIRoot>();
        }
        return _3dRoot;
    }
    private static UIRoot _2dRoot = null;
    private static UIRoot _3dRoot = null;

    /// <summary>
    /// 绑定UI事件
    /// </summary>
    /// <param name="type"></param>
    /// <param name="fun"></param>
    /// <param name="go"></param>
    public static void BindUIEvnet(string type, LuaFunction fun, GameObject go)
    {
        if(null == fun || null == go)
        {
            Debug.LogError("绑定UI事件失败" + (null == fun ? " LuaFunction 为 null" : (null == go ? "GameObject 为 null" : "")));
            return;
        }
        bool isSuccess = true;
        UIEventListener eventListener = go.AddComponent<UIEventListener>();
        switch (type)
        {
            case "Click":
                {
                    eventListener.onClick = (g) =>
                    {
                        fun.BeginPCall();
                        fun.Push(g);
                        fun.PCall();
                        fun.EndPCall();
                    };
                }
                break;
            case "Press":
                {
                    eventListener.onPress = (g, b) =>
                    {
                        fun.BeginPCall();
                        fun.Push(g);
                        fun.Push(b);
                        fun.PCall();
                        fun.EndPCall();
                    };
                }
                break;
            case "Select":
                {
                    eventListener.onSelect = (g, b) =>
                    {
                        fun.BeginPCall();
                        fun.Push(g);
                        fun.Push(b);
                        fun.PCall();
                        fun.EndPCall();
                    };
                }
                break;
            case "DoubelClick":
                {
                    eventListener.onDoubleClick = (g) =>
                    {
                        fun.BeginPCall();
                        fun.Push(g);
                        fun.PCall();
                        fun.EndPCall();
                    };
                }
                break;
            case "DragStart":
                {
                    eventListener.onDragStart = (g) =>
                    {
                        fun.BeginPCall();
                        fun.Push(g);
                        fun.PCall();
                        fun.EndPCall();
                    };
                }
                break;
            case "Drag":
                {
                    eventListener.onDrag = (g, delta) =>
                    {
                        fun.BeginPCall();
                        fun.Push(g);
                        fun.Push(delta);
                        fun.PCall();
                        fun.EndPCall();
                    };
                }
                break;
            case "DragOver":
                {
                    eventListener.onDragOver = (g) =>
                    {
                        fun.BeginPCall();
                        fun.Push(g);
                        fun.PCall();
                        fun.EndPCall();
                    };
                }
                break;
            case "DragOut":
                {
                    eventListener.onDragOut = (g) =>
                    {
                        fun.BeginPCall();
                        fun.Push(g);
                        fun.PCall();
                        fun.EndPCall();
                    };
                }
                break;
            case "DragEnd":
                {
                    eventListener.onDragEnd = (g) =>
                    {
                        fun.BeginPCall();
                        fun.Push(g);
                        fun.PCall();
                        fun.EndPCall();
                    };
                }
                break;
            case "Drop":
                {
                    eventListener.onDrop = (g, to) =>
                    {
                        fun.BeginPCall();
                        fun.Push(g);
                        fun.Push(to);
                        fun.PCall();
                        fun.EndPCall();
                    };
                }
                break;
            default:
                {
                    isSuccess = false;
     
                    Debug.LogError("event type: " + type + " no right");
                }
                break;
        }
        if (isSuccess)
        {
            BehaviourEvent Event = go.AddComponent<BehaviourEvent>();
            Event.OnDestoryAction = () => { fun.Dispose(); };
        }
        else
        {
            fun.Dispose();
            MonoBehaviour.Destroy(eventListener);
        }
    }


    /// <summary>
    /// 查找子节点
    /// </summary>
    /// <param name="name"></param>
    /// <param name="parent"></param>
    /// <param name="isFindAll"></param>
    /// <returns></returns>
    public static Transform GetChildTran(string name, Transform parent, bool isFindAll)
    {
        Transform child = null;
        for (int i = 0; i < parent.childCount; ++i)
        {
            child = parent.GetChild(i);
            if (child.name != name)
            {
                if (isFindAll)
                {
                    child = GetChildTran(name, child, isFindAll);
                    if (null != child)
                    {
                        return child;
                    }
                }
            }
            else
            {
                return child;
            }
        }
        return child;
    }

    /// <summary>
    /// 创建一个EventDelegate
    /// </summary>
    /// <param name="luafun"></param>
    /// <param name="go"></param>
    /// <returns></returns>
    public static EventDelegate CreateEventDelegate(LuaFunction luafun,GameObject go)
    {
        EventDelegate eventDelegate = new EventDelegate(() =>
        {
            luafun.BeginPCall();
            luafun.PCall();
            luafun.EndPCall();
        });

        BehaviourEvent Event = go.AddComponent<BehaviourEvent>();//将luafun生命周期绑定到一个go上
        Event.OnDestoryAction = () => { luafun.Dispose(); };

        return eventDelegate;
    }

   
    public static void RestrictWithinView_Target(Transform target, Transform child)
    {
        UIPanel mPanel = NGUITools.FindInParents<UIPanel>(target.gameObject);//(target != null) ? UIPanel.Find (target) : null;
        Bounds childBounds = NGUIMath.CalculateRelativeWidgetBounds(mPanel.transform, child, false);
        Vector3 min = childBounds.min;
        Vector3 max = childBounds.max;
        Vector4 cr = mPanel.finalClipRegion;
        float offsetX = cr.z * 0.5f;
        float offsetY = cr.w * 0.5f;
        Vector2 minRect = new Vector2(min.x, min.y);
        Vector2 maxRect = new Vector2(max.x, max.y);
        Vector2 minArea = new Vector2(cr.x - offsetX, cr.y - offsetY);
        Vector2 maxArea = new Vector2(cr.x + offsetX, cr.y + offsetY);
        if (mPanel.clipping == UIDrawCall.Clipping.SoftClip)
        {
            minArea.x += mPanel.clipSoftness.x;
            minArea.y += mPanel.clipSoftness.y;
            maxArea.x -= mPanel.clipSoftness.x;
            maxArea.y -= mPanel.clipSoftness.y;
        }
        Vector3 constraint = NGUIMath.ConstrainRect(minRect, maxRect, minArea, maxArea);
        if (constraint.magnitude > 1f)
        {
            // Spring back into place
            Vector3 pos = target.localPosition + constraint;
            pos.x = Mathf.Round(pos.x);
            pos.y = Mathf.Round(pos.y);
            SpringPosition.Begin(target.gameObject, pos, 13f);
        }
    }
}
