using UnityEngine;
using System.Collections;
using LuaInterface;

public class QuitTool : MonoBehaviour {

	// Use this for initialization
	void Awake() {
        DontDestroyOnLoad(this.gameObject);
    }
	
	// Update is called once per frame
	void Update () {
#if UNITY_ANDROID && !UNITY_EDITOR
        if (Input.GetKey(KeyCode.Escape))
        { 
            if(LuaMgr.GetMainState() != null)
            {
                LuaFunction func = LuaMgr.GetMainState().GetFunction("QuitTool.QuitTool.QuitGame");
                if(func != null)
                {
                    func.BeginPCall();
                    func.PCall();
                    func.EndPCall();
                    func.Dispose();
                }
            }
            
        }
#endif
    }
}
