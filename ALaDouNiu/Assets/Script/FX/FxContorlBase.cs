using UnityEngine;
using System.Collections;

public class FxContorlBase : MonoBehaviour
{
    [RangeAttribute(0, 1)]
    public float TimeScale = 1f;

    private bool _Runing = true;

    void Start()
    {

    }

    void Update()
    {

    }

    protected void update()
    {
        if (!_Runing)
            return;
        FxUpdate(Time.deltaTime * TimeScale);
    }

    public void Play()
    {
        _Runing = true;
        FxPlay();
    }

    public void Pause()
    {
        _Runing = false;
        FxPause();
    }

    public void Reset()
    {
        _Runing = false;
        FxReset();
    }

    protected virtual void FxPlay()
    {

    }

    protected virtual void FxPause()
    {

    }

    protected virtual void FxReset()
    {

    }

    protected virtual void FxUpdate(float deltaTime)
    {

    }
}
