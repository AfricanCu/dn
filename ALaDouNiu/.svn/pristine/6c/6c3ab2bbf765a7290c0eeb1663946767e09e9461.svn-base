/// <summary>
/// 
/// FxAnimationSpriteSheet.cs
/// 
/// Created by 朱晓亮(Castwings)
/// 
/// Copyright © 2015 Shader7 Studio. All rights reserved.
/// 
/// </summary>

using UnityEngine;

[AddComponentMenu("S7 Framework/FX/Fx Animation Sprite Sheet")]

public class FxAnimationSpriteSheet : FxContorlBase
{
	public int uvX = 4;  
	public int uvY = 2; 
	public float fps = 24;

    private float _totalTime = 0;

    void Start()
    {

    }

	void Update()
	{
        update();
	}


    protected override void FxUpdate(float deltaTime)
    {
        _totalTime += deltaTime;
        int index = (int)(_totalTime * fps);
        index = index % (uvX * uvY);
        Vector2 size = new Vector2(1f / uvX, 1f / uvY);

        float uIndex = index % uvX;
        float vIndex = index / uvX;
        Vector2 offset = new Vector2(uIndex * size.x, 1 - size.y - vIndex * size.y);

        GetComponent<Renderer>().material.SetTextureOffset("_MainTex", offset);
        GetComponent<Renderer>().material.SetTextureScale("_MainTex", size);
    }
}
