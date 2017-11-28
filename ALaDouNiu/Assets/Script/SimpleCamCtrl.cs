using UnityEngine;
using System.Collections;

/// <summary>
/// 简单摄像机控制
/// </summary>
public class SimpleCamCtrl : MonoBehaviour
{
    public Vector2 degrees = new Vector2(30f, 30f);
    public float DragSpeed = 5f;
    public float BackSpeed = 1f;
    public float BackDelay = 1f;
    [Range(-1, 1f)]
    public float starX = 0;
    [Range(-1, 1f)]
    public float starY = 0;

    private Transform _Trans;
    private Quaternion _Start;
    private Vector2 _Rot = Vector2.zero;
    private bool _isBacking = true;
    private float _backDelayTiming = 0f;
    private Vector2 _lastPos = Vector2.zero;
    private Vector2 _targetPos = Vector2.zero;
    private bool _isDraging = false;


    void Start()
    {
        _Trans = transform;
        _Start = _Trans.localRotation;
    }

    void Update()
    {
        if (Input.touchCount > 0 || Input.GetMouseButton(0)) //垃圾鼠标按键会抖，MDZZ
        {
            if(!_isDraging)
            {
                Camera UICam = NGUITools.FindCameraForLayer(LayerMask.NameToLayer("UI"));
                if (null != UICam)
                {
                    Ray ray = UICam.ScreenPointToRay(curPos);
                    if (Physics.Raycast(ray, int.MaxValue, 1 << LayerMask.NameToLayer("UI")))
                    {
                        return;
                    }
                }
            }

            ProcessDrag(DragSpeed);
            _isBacking = false;
        }
        else
        {
            _isDraging = false;
            GoBack();
        }
    }

    private void ProcessDrag(float speed)
    {
        Vector2 delta = UpdatePosDelta();
        _targetPos += delta;
        ProcessRota(_targetPos, DragSpeed, RealTime.deltaTime);
    }

    private Vector2 UpdatePosDelta()
    {
        if (!_isDraging)
        {
            _lastPos = curPos;
            _isDraging = true;
            return Vector2.zero;
        }

        Vector2 cur = curPos;
        Vector2 d = cur - _lastPos;

        _lastPos = cur;

        if (d.magnitude > 1)
        {
            return d;
        }
        else
        {
            return Vector2.zero;
        }
    }

    private void GoBack()
    {
        if (!_isBacking)
        {
            _backDelayTiming += RealTime.deltaTime;
            if (_backDelayTiming >= Mathf.Clamp(BackDelay, 0, float.MaxValue))
            {
                _isBacking = true;
            }
            return;
        }
        _backDelayTiming = 0;
        _targetPos = new Vector2(Screen.width * starX, Screen.height * starY) + new Vector2(Screen.width / 2, Screen.height / 2);
        float deltaTime = RealTime.deltaTime;
        _lastPos = Vector2.Lerp(_lastPos, _targetPos, deltaTime * BackSpeed);
        ProcessRota(_targetPos, BackSpeed, deltaTime);
    }
    private void ProcessRota(Vector2 pos, float speed, float deltaTime)
    {
        float halfWidth = Screen.width * 0.5f;
        float halfHeight = Screen.height * 0.5f;

        float x = Mathf.Clamp((pos.x - halfWidth) / halfWidth, -1f, 1f);
        float y = Mathf.Clamp((pos.y - halfHeight) / halfHeight, -1f, 1f);

        _Rot = Vector2.Lerp(_Rot, new Vector2(x, y), deltaTime * speed);

        _Trans.localRotation = _Start * Quaternion.Euler(-_Rot.y * degrees.y, _Rot.x * degrees.x, 0f);
    }


    private Vector2 curPos
    {
        get
        {
            Vector2 pos = Input.mousePosition;
            if (Input.touchCount > 0)
            {
                pos = Input.GetTouch(0).position;
            }
            return pos;
        }
    }
}
