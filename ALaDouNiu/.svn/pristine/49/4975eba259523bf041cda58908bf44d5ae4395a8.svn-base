using UnityEngine;
using UnityEditor;
using System.IO;

public class LuaBehaviour : MonoBehaviour
{
    public const string defaultName = "NewLuaBehaviourScript";

    [MenuItem("Assets/Create/LuaScript", false, 0)]
    public static void CreateLuaBehaviourScript()
    {
        string path = null;//Path.GetDirectoryName (AssetDatabase.GetAssetPath (Selection.activeObject));
        if (Directory.Exists(AssetDatabase.GetAssetPath(Selection.activeObject)))
        {
            path = AssetDatabase.GetAssetPath(Selection.activeObject);
        }
        else
        {
            path = Path.GetDirectoryName(AssetDatabase.GetAssetPath(Selection.activeObject));
        }

        string luaPath = path + "/" + defaultName + ".lua";

        string[] filenames = Directory.GetFiles(path, "*.lua");

        string nextName = defaultName;
        int index = 1;

        bool hasSame = true;
        while (hasSame)
        {
            hasSame = false;
            for (int i = 0; i < filenames.Length; ++i)
            {
                string name = Path.GetFileNameWithoutExtension(filenames[i]);
                if (name.Equals(nextName))
                {
                    hasSame = true;
                }
            }
            if (hasSame)
            {
                nextName = defaultName + index.ToString();
                ++index;
            }
        }

        StreamWriter witer = File.CreateText(path + "/" + nextName + ".lua");
        witer.Dispose();

        AssetDatabase.Refresh();
    }

}
