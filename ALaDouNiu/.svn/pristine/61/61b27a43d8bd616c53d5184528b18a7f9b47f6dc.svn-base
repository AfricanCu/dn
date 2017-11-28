using UnityEngine;
using System.Collections;
using UnityEditor;
using Asset.Script.ABSystem;
using System.Collections.Generic;
using System;

/// <summary>
/// AB分析窗口
/// </summary>
public class ABprofiler : EditorWindow
{

    [MenuItem("工具/ABprofiler")]
    public static void OpenABprofiler()
    {
        EditorWindow win = GetWindow<ABprofiler>();
        win.Show();
    }


    private class Style
    {
        public GUIStyle GreenLabel { private set; get; }
        public GUIStyle RedLabel { private set; get; }

        public GUIStyle SelectedButton { private set; get; }

        public GUIStyle ErrorButton { private set; get; }
        public Style()
        {
            GreenLabel = new GUIStyle(GUI.skin.label);
            GreenLabel.normal.textColor = Color.green;

            RedLabel = new GUIStyle(GUI.skin.label);
            RedLabel.normal.textColor = Color.red;

            SelectedButton = new GUIStyle(GUI.skin.button);
            SelectedButton.normal.textColor = Color.green;
            SelectedButton.hover.textColor = Color.green;
            SelectedButton.active.textColor = Color.green;

            ErrorButton = new GUIStyle(GUI.skin.button);
            ErrorButton.normal.textColor = Color.red;
            ErrorButton.hover.textColor = Color.red;
            ErrorButton.active.textColor = Color.red;
        }

    }

    private Style _Style;
    private Vector2 _scrollValue;
    private Vector2 _scrollValue2;
    private List<string> deps = new List<string>();

    private WeakReference _AgantRef;

    private void OnGUI()
    {
        if(!EditorApplication.isPlaying)
        {
            return;
        }

        if(null == _Style)
        {
            _Style = new Style();
        }

        Dictionary<string, AssetBundleAgant> dic = ABManager.Instance.ABDic;

        GUILayout.BeginVertical();
        GUILayout.BeginHorizontal();
        GUILayout.Label("当前AB数量：" + dic.Count,GUILayout.Width(120));
        if (GUILayout.Button("UnLoadUnUse"))
        {
            ResHelper.UnLoadUnUse();
        }
        GUILayout.EndHorizontal();

        GUILayout.BeginHorizontal();
        _scrollValue = GUILayout.BeginScrollView(_scrollValue, GUILayout.Width(this.position.width * 2f / 3f));
        foreach (var v in dic)
        {
            DrawABItem(v.Key, v.Value);
        }
        GUILayout.EndScrollView();
        DrawAgantDetails();
        GUILayout.EndHorizontal();
        GUILayout.EndVertical();
    }


    private void DrawABItem(string name, AssetBundleAgant agant)
    {
        GUILayout.BeginHorizontal();
        if(null == _AgantRef
           || null == _AgantRef.Target
           || agant != _AgantRef.Target as AssetBundleAgant)
        {
            if (GUILayout.Button(name, GUILayout.Width(300)))
            {
                _AgantRef = new WeakReference(agant);
            }
        }
        else
        {
            if (GUILayout.Button(name, _Style.SelectedButton, GUILayout.Width(300)))
            {
                _AgantRef = new WeakReference(agant);
            }
        }

        string info = "";
        if (agant.m_IsStatic)
        {
            info += "IsStatic";
        }
        else
        {
            info += "RefCount:" + agant.m_CurRefCount;
        }
        info += "   LoadingTime:" + agant.m_LoadUseTime + "ms";
        GUILayout.Label(info);

        if (agant.m_IsDispose)
        {
            if (GUILayout.Button("Dispose", _Style.ErrorButton, GUILayout.Width(75)))
            {

            }
        }
        else
        {
            if (GUILayout.Button("Dispose", GUILayout.Width(75)))
            {
                agant.Dispose();
            }
        }
        GUILayout.EndHorizontal();
    }
    private void DrawAgantDetails()
    {
        if (null == _AgantRef
            || null == _AgantRef.Target)
            return;
        AssetBundleAgant agant = _AgantRef.Target as AssetBundleAgant;
        if(agant.m_IsDispose)
        {
            _AgantRef = null;
            return;
        }
        _scrollValue2 = GUILayout.BeginScrollView(_scrollValue2, GUILayout.Width(this.position.width * 1f / 3f));
        GUILayout.BeginVertical();
        GUILayout.Label("AB名称：" + agant.m_ABName);
        GUILayout.Label("引用计数：" + agant.m_CurRefCount);
        deps.Clear();
        ABManager.Instance.GetAllDependent(agant.m_ABName, ref deps);
        GUILayout.Label("AB依赖项 " + deps.Count + " 个");
        for (int i = 0; i < deps.Count; ++i)
        {
            GUILayout.BeginHorizontal();
            bool Has = ABManager.Instance.ABIsHas(deps[i]);
            if (GUILayout.Button(deps[i], GUILayout.Width(this.position.width * 1f / 4f)))
            {
                if(Has)
                {
                    _AgantRef = new WeakReference(ABManager.Instance.ABDic[deps[i]]);
                }
            }
            if (Has)
            {
                GUILayout.Label("已加载", _Style.GreenLabel);
            }
            else
            {
                GUILayout.Label("未加载", _Style.RedLabel);
            }
            GUILayout.EndHorizontal();
        }

        GUILayout.EndHorizontal();
        GUILayout.EndScrollView();
    }
}
