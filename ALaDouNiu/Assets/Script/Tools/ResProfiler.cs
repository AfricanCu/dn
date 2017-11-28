using UnityEngine;
using System.Collections;
using System.IO;

public class ResProfiler : MonoBehaviour
{

    private class Styles
    {
        public GUIStyle Button { private set; get; }
        public GUIStyle TextArea { private set; get; }
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


    private Styles _Styles;
    private bool _close = true;
    private string _text = "";


    private void Awake()
    {

    }

    // Use this for initialization
    void Start()
    {
        if (!Application.isEditor)
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


    private Vector2 _Scroll;

    private void OnGUI()
    {
        if (null == _Styles)
        {
            _Styles = new Styles();
        }

        if (_close)
        {
            if (GUI.Button(new Rect(0, 0, Screen.width / 8, Screen.height / 8), "查看", _Styles.Button))
            {
                _close = false;
            }
            return;
        }

        GUILayout.BeginVertical();
        _Scroll = GUILayout.BeginScrollView(_Scroll, false, false, _Styles.HScrollbar, _Styles.VScrollbar,
            GUILayout.Width(1000), GUILayout.Width(1000),
            GUILayout.Width(Screen.width * 4f / 5f), GUILayout.Height(Screen.height * 0.8f));

        GUILayout.TextArea(_text, _Styles.TextArea);

        GUILayout.EndScrollView();

        GUILayout.Space(Screen.height * 0.08f);

        GUILayout.BeginHorizontal();
        if (GUILayout.Button("关闭", _Styles.Button, GUILayout.Height((int)(Screen.height * 0.1))))
        {
            _close = true;
        }
        if (GUILayout.Button("刷新", _Styles.Button, GUILayout.Height((int)(Screen.height * 0.1))))
        {
            _text = GetResLsitText();
        }
        GUILayout.EndHorizontal();
        GUILayout.EndVertical();


    }


    private string GetResLsitText()
    {
        string text = "";
        string path = ResPathHelper.Instance.LocalFilePath();
        path = FilePathTool.Instance.Normalization(path,Application.platform);

        DirectoryInfo folder = new DirectoryInfo(path);
        foreach (FileInfo file in folder.GetFiles("*.*"))
        {
            text += file.FullName + "\n";
        }
        return text;
    }
}
