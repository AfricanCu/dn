using UnityEngine;
using System.Collections;

public static class LayerHelper
{
    public static void ApplyLayerTo(int layer,GameObject go,bool isApplyToChild)
    {
        go.layer = layer;
        if(isApplyToChild)
        {
            for (int i = 0; i < go.transform.childCount; ++i)
            {
                ApplyLayerTo(layer, go.transform.GetChild(i).gameObject, isApplyToChild);
            }
        }
    }
}
