using UnityEngine;
using System.Collections;
using UnityEditor;

[CustomEditor(typeof(GameObjectUITitel), true)]
public class GameObjectUITitelEditor : Editor
{
    public override void OnInspectorGUI()
    {
        serializedObject.Update();

        GameObjectUITitel obj = (GameObjectUITitel)target;
        obj.Target = EditorGUILayout.ObjectField("锚定的目标",obj.Target, typeof(Transform)) as Transform;
        obj.Is3D = EditorGUILayout.Toggle("是否启用3D模式",obj.Is3D);
        if (!obj.Is3D)
        {
            obj.Offset = EditorGUILayout.Vector2Field("屏幕像素偏移", obj.Offset);
            obj.MainCam = EditorGUILayout.ObjectField("主摄像机", obj.MainCam, typeof(Camera)) as Camera;
        }
        else
        {
            obj.m_3DOffset = EditorGUILayout.Vector3Field("坐标偏移", obj.m_3DOffset);
        }
        obj.UICam = EditorGUILayout.ObjectField("UI相机", obj.UICam, typeof(Camera)) as Camera;
        if (GUI.changed)
        {
            if (!EditorApplication.isPlaying)
                EditorApplication.MarkSceneDirty();
        }
    }
}
