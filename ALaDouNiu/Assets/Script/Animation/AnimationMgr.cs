using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class AnimationMgr : MonoBehaviour {
    private Animation ani = null;
    private Dictionary<string, float> clipTimes = new Dictionary<string, float>();
	// Use this for initialization
	void Awake () {
        ani = this.GetComponent<Animation>();

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
	
    public void PlayAni(string clipName)
    {
        if(ani != null)
        {
            ani.CrossFade(clipName, 0.5f);
        }
    }

    public float GetAniTime(string clipName)
    {
        if(clipTimes.ContainsKey(clipName))
        {
            return clipTimes[clipName];
        }
        else
        {
            return 0;
        }
    }
    
}
