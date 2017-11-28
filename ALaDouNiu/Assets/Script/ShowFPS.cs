using UnityEngine;
using System.Collections;

public class ShowFPS : MonoBehaviour
{

    public float fpsMeasuringDelta = 2.0f;

    private float timePassed;
    private int m_FrameCount = 0;
    private float m_FPS = 0.0f;
    private uint systemTime = 0;
    private double time = 0;

    private void Start()
    {
        Screen.sleepTimeout = SleepTimeout.NeverSleep;
        timePassed = 0.0f;

        systemTime = (uint)System.Environment.TickCount;
        time = (double)systemTime;
        time /= 1000f;

        int tmp = int.MinValue + 1;

        if(tmp < 0)
        {
            systemTime = (uint)int.MaxValue  + 1 + (uint)(tmp - int.MinValue);
        }

    }

    private void Update()
    {
        m_FrameCount = m_FrameCount + 1;
        timePassed = timePassed + Time.deltaTime;

        if (timePassed > fpsMeasuringDelta)
        {
            m_FPS = m_FrameCount / timePassed;

            timePassed = 0.0f;
            m_FrameCount = 0;
        }
    }

    private void OnGUI()
    {
        GUIStyle bb = new GUIStyle();
        bb.normal.background = null;    //这是设置背景填充的
        bb.normal.textColor = new Color(1.0f, 0.5f, 0.0f);   //设置字体颜色的
        bb.fontSize = 40;       //当然，这是字体大小

        //居中显示FPS
        GUI.Label(new Rect((Screen.width / 2) - 40, 0, 200, 200), "FPS: " + m_FPS, bb);

        //GUI.Label(new Rect((Screen.width / 2) - 40, 40, 200, 200), "time: " + systemTime, bb);
    }
}
