using UnityEngine;
using UnityEditor;
using System.Collections;

public class InputWindow : EditorWindow {

    public delegate void OnInputDone(string value);

    public static InputWindow ShowModalInputWindow(string title, string tips, OnInputDone call_back)
    {
        InputWindow window = (InputWindow)EditorWindow.GetWindow(typeof(InputWindow));
        window.titleContent = new GUIContent(title);
        window.tips = tips;
        window.call_back = call_back;
        window.Show();

        return window;
    }

    private OnInputDone call_back = null;
    private string tips = null;
    private string value = "";

    void OnLostFocus()
    {
        Focus();
    }

    void OnGUI()
    {
        if (tips != null)
        {
            value = EditorGUILayout.TextField(tips, value);
        }
        else
        {
            value = EditorGUILayout.TextField(value);
        }


        if (GUILayout.Button("确定"))
        {
            if (call_back != null)
            {
                call_back(value);
            }
        }
    }

}
