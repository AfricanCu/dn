using UnityEngine;
using System.Collections;
using System.Collections.Generic;

/// <summary>
/// Debug控制台
/// </summary>
public class DebugConsole : MonoBehaviour
{
    private class Styles
    {
        public  GUIStyle Button { private set; get; }
        public  GUIStyle TextArea { private set; get; }
        public GUIStyle HScrollbar { private set; get; }
        public GUIStyle VScrollbar { private set; get; }



        public Styles()
        {
            Button = new GUIStyle(GUI.skin.button);
            Button.fontSize = 40;

            TextArea = new GUIStyle(GUI.skin.textArea);
            TextArea.fontSize = 30;
            TextArea.normal.textColor = Color.cyan;
            TextArea.hover.textColor = Color.cyan;

            HScrollbar = new GUIStyle(GUI.skin.horizontalScrollbar);
            HScrollbar.fixedHeight = 20;

            VScrollbar = new GUIStyle(GUI.skin.verticalScrollbar);
            VScrollbar.fixedWidth = 20;
        }
    }


    private bool _stop = false;
    private bool _close = true;
    private Styles _Styles;

    private void Awake()
    {
        _logs = new List<string>();
        DontDestroyOnLoad(this.gameObject);
        Application.logMessageReceived += Onlog;
    }

    // Use this for initialization
    void Start()
    {
        if(!Application.isEditor)
        {
            _close = false;
        }
        else
        {
            this.enabled = false;
        }
    }

    // Update is called once per frame
    void Update()
    {

    }

    private void Onlog(string condition, string stackTrace, LogType type)
    {
        if (!_stop)
        {
            if (type == LogType.Error || type == LogType.Exception)
                AddLog(condition + "\n" + stackTrace);
            else
                AddLog(condition);
            _Scroll = new Vector2(0, int.MaxValue);
        }
    }

    private void AddLog(string log)
    {
        if (_logs.Count > 150)
        {
            _logs.RemoveAt(0);
        }
        _logs.Add(log);
    }

    private List<string> _logs;
    private Vector2 _Scroll;

    private void OnGUI()
    {
        if(null == _Styles)
        {
            _Styles = new Styles();
        }

        if (_close)
        {
            if(GUI.Button(new Rect(0, Screen.height - Screen.height / 8, Screen.width / 8, Screen.height / 8),"打开Log", _Styles.Button))
            {
                _close = false;
            }
            return;
        }

        GUILayout.BeginVertical();
        _Scroll = GUILayout.BeginScrollView(_Scroll, false,false,_Styles.HScrollbar,_Styles.VScrollbar,
            GUILayout.Width(1000), GUILayout.Width(1000),
            GUILayout.Width(Screen.width), GUILayout.Height(Screen.height * 0.8f));

        GUILayout.TextArea(GetLogText(), _Styles.TextArea);

        GUILayout.EndScrollView();

        GUILayout.Space(Screen.height * 0.08f);

        GUILayout.BeginHorizontal();

        if (GUILayout.Button("清除", _Styles.Button, GUILayout.Height((int)(Screen.height * 0.1))))
        {
            _logs.Clear();
        }
        if (!_stop)
        {
            if (GUILayout.Button("停止", _Styles.Button, GUILayout.Height((int)(Screen.height * 0.1))))
            {
                _stop = true;
            }
        }
        else
        {
            if (GUILayout.Button("继续", _Styles.Button, GUILayout.Height((int)(Screen.height * 0.1))))
            {
                _stop = false;
            }
        }
        if (GUILayout.Button("关闭", _Styles.Button, GUILayout.Height((int)(Screen.height * 0.1))))
        {
            _close = true;
        }
        GUILayout.EndHorizontal();
        GUILayout.EndVertical();

        
    }

    private string GetLogText()
    {
        string text = "";
        for (int i = 0; i < _logs.Count; ++i)
        {
            text += _logs[i] + "\n";
        }
        return text;
    }


    private void OnDestroy()
    {
        Application.logMessageReceived -= Onlog;
    }
}
