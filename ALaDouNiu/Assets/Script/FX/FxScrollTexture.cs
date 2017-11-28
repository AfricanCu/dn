
using UnityEngine;

public class FxScrollTexture : FxContorlBase
{
    public float speed = 0.25f;

    private float _TotalTime = 0f;

    void Update()
    {
        update();
    }

    protected override void FxUpdate(float deltaTime)
    {
        _TotalTime += deltaTime;
        this.GetComponent<Renderer>().material.mainTextureOffset = new Vector2(0.0f, -(_TotalTime * this.speed));
    }
}
