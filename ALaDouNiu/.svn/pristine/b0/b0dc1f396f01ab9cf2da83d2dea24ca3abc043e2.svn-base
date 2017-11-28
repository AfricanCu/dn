using UnityEngine;
using System.Collections;

public class pMove : MonoBehaviour
{
    private Material mat;
    public float speed = 0.7f;
    public float eachTime = 4.0f;
    
    public Vector2 startOffset = new Vector2(3.96f, 2);
    public float distance=0.25f;
    
    private float Timer = 0.0f;
    private float timeAdd = 0.0f;

    void Awake()
    {
        if (mat == null)
        {
            mat = this.GetComponent<MeshRenderer>().material;
        }
    }

    void Start()
    {
        
    }


    void Update()
    {
        Timer += Time.deltaTime;
        if (Timer > eachTime)
        {
            timeAdd += Time.deltaTime;
            mat.SetTextureOffset("_MainTex", Vector2.Lerp(startOffset, new Vector2(startOffset.x+distance, startOffset.y), timeAdd * speed));
            if (timeAdd > eachTime)
            {
                SetOffset();
                Timer = 0.0f;
                timeAdd = 0.0f;
            }
        }
    }
    void SetOffset() {
        startOffset.x += distance;
    }
}