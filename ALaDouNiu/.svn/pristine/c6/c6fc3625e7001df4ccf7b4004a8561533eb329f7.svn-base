using UnityEngine;
using System.Collections;

public class GameObjectUITitel : MonoBehaviour
{
    public bool Is3D = false;

    public Vector2 Offset = Vector2.zero;
    public Vector3 m_3DOffset = Vector3.zero;

    public Transform Target;
    public Camera MainCam;
    public Camera UICam;

    private Vector2 _NGUIPosOffset;


    protected void Awake()
    {
        _NGUIPosOffset = new Vector2(Screen.width / 2f, Screen.height / 2f);
    }

    protected void Start()
    {

    }

    protected virtual void Update()
    {
        if (Is3D)
        {
            if (Target && UICam)
            {
                this.transform.position = Target.transform.position + m_3DOffset;
                this.transform.LookAt(UICam.transform);
            }
        }
        else
        {
            if (!Target || !UICam || !MainCam)
            {
                return;
            }
            ProcessPos();
        }
    }


    /// <summary>
    ///处理控件屏幕坐标
    /// </summary>
    /// <param name="ScreeePos"></param>
    private void ProcessPos()
    {
        //世界坐标转屏幕坐标
        Vector2 ScreenPos = MainCam.WorldToScreenPoint(Target.position);
        //NGUI坐标系的原点是屏幕中央，需要处理一下
        Vector2 pixelPos = ScreenPos - _NGUIPosOffset + Offset;

        Rect rect = UICam.pixelRect;
        float x = (rect.xMin + rect.xMax) / 2f;
        float y = (rect.yMin + rect.yMax) / 2f;
        Vector3 v = new Vector3(x, y, 0f);
        v.x += pixelPos.x;
        v.y += pixelPos.y;

        if (UICam.orthographic)
        {
            v.x = Mathf.Round(v.x);
            v.y = Mathf.Round(v.y);
        }
        v.z = UICam.WorldToScreenPoint(this.transform.position).z;
        v = UICam.ScreenToWorldPoint(v);

        if (UICam.orthographic && this.transform.parent != null)
        {
            v = this.transform.parent.InverseTransformPoint(v);
            v.x = Mathf.RoundToInt(v.x);
            v.y = Mathf.RoundToInt(v.y);
            this.transform.localPosition = v;
        }
        else
        {
            this.transform.position = v;
        }
    }
}
