using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class HanderCom : MonoBehaviour {
    public Animation ani;
    public Transform centerFinger;
    public Transform firstFinger;
    private Dictionary<string, float> clipTimes = new Dictionary<string, float>();
	// Use this for initialization
    void Awake()
    {
        if (ani != null)
        {
            int i = 0;
            int count = ani.GetClipCount();

            foreach (AnimationState state in ani)
            {
                clipTimes[state.name] = state.length;
                i++;
            }
        }
	}
	
	// Update is called once per frame
	void Update () {

	}

    public void CrossAni(string clipName, float len)
    {
        if (ani != null)
        {
            ani.CrossFade(clipName, len);
        }
    }

    public void PlayAni(string clipName)
    {
        //Debug.LogError("xxxxx");
        if (ani != null)
        {
            ani.Play(clipName);
        }
    }

    public float GetAniTime(string clipName)
    {
        if (clipTimes.ContainsKey(clipName))
        {
            return clipTimes[clipName];
        }
        else
        {
            return 0;
        }
    }
}
