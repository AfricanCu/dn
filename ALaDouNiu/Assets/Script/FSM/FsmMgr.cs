using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class FsmMgr: MonoBehaviour {
    private Dictionary<string, FsmShell> shellDic = null;
    private FsmShell m_curShell= null;
    private string s_curShell = string.Empty;

    void Awake()
    {
        shellDic = new Dictionary<string, FsmShell>();
        m_curShell = null;
    }
	
    void Update()
    {
        if(m_curShell != null)
        {
            m_curShell.Update(Time.deltaTime);
        }
    }

    public void AddFsmShell(string shellName, FsmShell shell)
    {
        shellDic[shellName] = shell;
    }

    public void ChangeShell(string shellName, bool isForced = false)
    {
        if (s_curShell == shellName && !isForced) return;

        if (m_curShell != null)
        {
            m_curShell.End();
            m_curShell = null;
        }

        if(!shellDic.ContainsKey(shellName))
        {
            Debug.LogError("the shellName:" + shellName + " not exist!!!");
        }

        m_curShell = shellDic[shellName];
        s_curShell = shellName;

        m_curShell.Begin();
    }
}
