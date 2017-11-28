using UnityEngine;
using System.Collections;

public class AutoTransparent : FxContorlBase
{

    public float startAlpha = 1.0f;
    public float endAlpha = 0.0f;

    public float time = 1.0f;
    public bool destroy = false;

    private float time_line = 0.0f;

    private Renderer[] renderers = null;

    void Start()
    {
        renderers = GetComponentsInChildren<Renderer>();
    }

    // Update is called once per frame
    void Update()
    {
        update();
    }

    protected override void FxUpdate(float deltaTime)
    {
        if (time_line <= time)
        {
            float per = time_line / time;
            float alpha = startAlpha * (1.0f - per) + endAlpha * per;

            if (null != renderers && renderers.Length > 0)
            {
                for (int i = 0; i < renderers.Length; ++i)
                {
                    if (renderers[i].material.HasProperty("_Color"))
                    {
                        Color color = renderers[i].material.color;
                        renderers[i].material.color = new Color(color.r, color.g, color.b, alpha);
                    }
                }
            }
        }
        else
        {
            DestroyImmediate(gameObject);
        }

        time_line += deltaTime;
    }
}
