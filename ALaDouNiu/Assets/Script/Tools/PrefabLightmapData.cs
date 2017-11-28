using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class PrefabLightmapData : MonoBehaviour
{
	[System.Serializable]
	struct RendererInfo
	{
		public Renderer 	renderer;
		public int 			lightmapIndex;
		public Vector4 		lightmapOffsetScale;
	}

	[SerializeField]
	RendererInfo[]	m_RendererInfo;
	[SerializeField]
	Texture2D[] 	m_Lightmaps;
    [SerializeField]
    FogSeting   m_FogSeting;


    [System.Serializable]
    public struct FogSeting
    {
        public bool Fog;
        public Color FogColor;
        public FogMode FogMode;
        public float Start;
        public float End;
    }


    public bool Test;

    void Awake ()
	{
        if(this.enabled)
            Apply();
    }

    public void Apply()
    {
        if (m_RendererInfo == null || m_RendererInfo.Length == 0)
            return;

        RenderSettings.fog = m_FogSeting.Fog;
        RenderSettings.fogColor = m_FogSeting.FogColor;
        RenderSettings.fogMode = m_FogSeting.FogMode;
        RenderSettings.fogStartDistance = m_FogSeting.Start;
        RenderSettings.fogEndDistance = m_FogSeting.End;

        var lightmaps = LightmapSettings.lightmaps;
        var combinedLightmaps = new LightmapData[lightmaps.Length + m_Lightmaps.Length];

        lightmaps.CopyTo(combinedLightmaps, 0);
        for (int i = 0; i < m_Lightmaps.Length; i++)
        {
            combinedLightmaps[i + lightmaps.Length] = new LightmapData();
            combinedLightmaps[i + lightmaps.Length].lightmapFar = m_Lightmaps[i];
        }

        ApplyRendererInfo(m_RendererInfo, lightmaps.Length);
        LightmapSettings.lightmaps = combinedLightmaps;
    }

    private void Update()
    {
        if(Test)
        {
            Test = false;
            Apply();
        }
    }

    static void ApplyRendererInfo (RendererInfo[] infos, int lightmapOffsetIndex)
	{
		for (int i=0;i<infos.Length;i++)
		{
			var info = infos[i];
			info.renderer.lightmapIndex = info.lightmapIndex + lightmapOffsetIndex;
			info.renderer.lightmapScaleOffset = info.lightmapOffsetScale;
		}
	}

#if UNITY_EDITOR
	[UnityEditor.MenuItem("工具/Bake Prefab Lightmaps")]
	static void GenerateLightmapInfo ()
	{
		if (UnityEditor.Lightmapping.giWorkflowMode != UnityEditor.Lightmapping.GIWorkflowMode.OnDemand)
		{
			Debug.LogError("ExtractLightmapData requires that you have baked you lightmaps and Auto mode is disabled.");
			return;
		}
		UnityEditor.Lightmapping.Bake();

		PrefabLightmapData[] prefabs = FindObjectsOfType<PrefabLightmapData>();

		foreach (var instance in prefabs)
		{
			var gameObject = instance.gameObject;
			var rendererInfos = new List<RendererInfo>();
			var lightmaps = new List<Texture2D>();
			
			GenerateLightmapInfo(gameObject, rendererInfos, lightmaps);
			
			instance.m_RendererInfo = rendererInfos.ToArray();
			instance.m_Lightmaps = lightmaps.ToArray();

            instance.m_FogSeting = new FogSeting();
            instance.m_FogSeting.Fog = RenderSettings.fog;
            instance.m_FogSeting.FogColor = RenderSettings.fogColor;
            instance.m_FogSeting.FogMode = RenderSettings.fogMode;
            instance.m_FogSeting.Start = RenderSettings.fogStartDistance;
            instance.m_FogSeting.End = RenderSettings.fogEndDistance;

            var targetPrefab = UnityEditor.PrefabUtility.GetPrefabParent(gameObject) as GameObject;
			if (targetPrefab != null)
			{
				//UnityEditor.Prefab
				UnityEditor.PrefabUtility.ReplacePrefab(gameObject, targetPrefab);
			}

        }
    }

	static void GenerateLightmapInfo (GameObject root, List<RendererInfo> rendererInfos, List<Texture2D> lightmaps)
	{
		var renderers = root.GetComponentsInChildren<MeshRenderer>();
		foreach (MeshRenderer renderer in renderers)
		{
			if (renderer.lightmapIndex != -1)
			{
				RendererInfo info = new RendererInfo();
				info.renderer = renderer;
				info.lightmapOffsetScale = renderer.lightmapScaleOffset;

				Texture2D lightmap = LightmapSettings.lightmaps[renderer.lightmapIndex].lightmapFar;

				info.lightmapIndex = lightmaps.IndexOf(lightmap);
				if (info.lightmapIndex == -1)
				{
					info.lightmapIndex = lightmaps.Count;
					lightmaps.Add(lightmap);
				}

				rendererInfos.Add(info);
			}
		}
	}
#endif

}