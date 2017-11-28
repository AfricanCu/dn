using UnityEngine;
using System.Collections;
using Spine.Unity;

public class SpineCtrl : MonoBehaviour {
    [SerializeField]
    private SkeletonAnimation spineAnim;
    // Use this for initialization
    public void DoAni(string aniName, bool loop, bool needClear)
    {
        if(needClear)
        {
            spineAnim.state.ClearTracks();
        }
        spineAnim.state.SetAnimation(0, aniName, loop);
    }
}
