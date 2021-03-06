﻿using UnityEngine;

using System.Linq;

public class LightMapSwitcher : MonoBehaviour
{
    //public Texture2D[] DayNear;
    public Texture2D[] DayFar;
   // public Texture2D[] NightNear;
    public Texture2D[] NightFar;

    public GameObject DayFX;
    public GameObject NightFx;

    private LightmapData[] dayLightMaps;
    private LightmapData[] nightLightMaps;

    public static LightMapSwitcher Instance
    {
        get
        {
            return _instance;
        }
    }

    private static LightMapSwitcher _instance = null;

    void Awake()
    {
        _instance = this;
    }

    void Start()
    {
        //if ((DayNear.Length != DayFar.Length) || (NightNear.Length != NightFar.Length))
        //{
        //    Debug.Log("In order for LightMapSwitcher to work, the Near and Far LightMap lists must be of equal length");
        //    return;
        //}

        // Sort the Day and Night arrays in numerical order, so you can just blindly drag and drop them into the inspector
        //DayNear = DayNear.OrderBy(t2d => t2d.name, new NaturalSortComparer<string>()).ToArray();
        //DayFar = DayFar.OrderBy(t2d => t2d.name, new NaturalSortComparer<string>()).ToArray();
        //NightNear = NightNear.OrderBy(t2d => t2d.name, new NaturalSortComparer<string>()).ToArray();
        //NightFar = NightFar.OrderBy(t2d => t2d.name, new NaturalSortComparer<string>()).ToArray();

        // Put them in a LightMapData structure
        dayLightMaps = new LightmapData[DayFar.Length];
        for (int i = 0; i < DayFar.Length; i++)
        {
            dayLightMaps[i] = new LightmapData();
            //dayLightMaps[i].lightmapNear = DayNear[i];
            dayLightMaps[i].lightmapFar = DayFar[i];
        }

        nightLightMaps = new LightmapData[NightFar.Length];
        for (int i = 0; i < NightFar.Length; i++)
        {
            nightLightMaps[i] = new LightmapData();
            //nightLightMaps[i].lightmapNear = NightNear[i];
            nightLightMaps[i].lightmapFar = NightFar[i];
        }
    }

    #region Publics
    public void SetToDay()
    {
        LightmapSettings.lightmaps = dayLightMaps;
        DayFX.SetActive(true);
        NightFx.SetActive(false);
    }

    public void SetToNight()
    {
        LightmapSettings.lightmaps = nightLightMaps;
        DayFX.SetActive(false);
        NightFx.SetActive(true);
    }
    #endregion

    #region Debug
    [ContextMenu("Set to Night")]
    void Debug00()
    {
        SetToNight();
    }

    [ContextMenu("Set to Day")]
    void Debug01()
    {
        SetToDay();
    }
    #endregion
}