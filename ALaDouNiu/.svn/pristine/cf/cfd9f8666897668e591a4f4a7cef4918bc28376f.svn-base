using UnityEngine;
using System.Collections;

public class DirLight : MonoBehaviour {
    public Material mat;
    public Color32 startColor = new Color32(131,131,131,255);
    public Color32 endColor = new Color32(191, 191, 191, 255);
    public float lightTime = 4.8f;
    public float lightGap = 0.8f;
    private float lightTimeCount = 0;
    private float lightTurnCount = 0;
    private bool isReverse = true;
    // Use this for initialization
    void Start () {
        if(mat == null)
        {
            mat = this.GetComponent<MeshRenderer>().material;
        }
        lightTimeCount = 0;
        lightTurnCount = 0;
        isReverse = true;
    }

    private void OnEnable()
    {
        lightTimeCount = 0;
        lightTurnCount = 0;
        isReverse = true;
    }
	
	// Update is called once per frame
	void Update () {
	    if(lightTimeCount < lightTime)
        {
            lightTimeCount += Time.deltaTime;
            lightTurnCount += Time.deltaTime;
            Color32 tmpColor = new Color32(191, 191, 191, 255);
            if (lightTurnCount > lightGap)
            {
                lightTurnCount -= lightGap;
                isReverse = !isReverse;
            }

            if(isReverse)
            {
                tmpColor = Color32.Lerp(endColor, startColor, lightTurnCount/ lightGap);
            }
            else
            {
                tmpColor = Color32.Lerp(startColor, endColor, lightTurnCount/ lightGap);
            }
            
            mat.SetColor("_IlluminCol", tmpColor);
        }
	}
}
