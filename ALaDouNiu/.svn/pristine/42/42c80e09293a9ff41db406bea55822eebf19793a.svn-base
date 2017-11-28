using UnityEngine;
using System.Collections;

public class UVScroller : MonoBehaviour {

	public Vector2 scrollSpeed = new Vector2(0.5f, 0.5f);
	public UITexture nguiTexture;
	
	void Start()
	{
		if (nguiTexture)
			nguiTexture.mainTexture.wrapMode = TextureWrapMode.Repeat;
	}
	
    void Update()
	{
		Vector2 offset = Time.time * scrollSpeed;

        if (nguiTexture)
            nguiTexture.uvRect = new Rect(offset.x, offset.y, nguiTexture.uvRect.width, nguiTexture.uvRect.height);
		else
			GetComponent<Renderer>().material.mainTextureOffset = offset;
    }
}
