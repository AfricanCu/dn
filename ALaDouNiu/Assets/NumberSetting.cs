using UnityEngine;
using System.Collections;

public class NumberSetting : MonoBehaviour {

    private static NumberSetting _instance;
    private DiceRotate diceOne;
    private DiceRotate diceTow;

    public static NumberSetting Instance
    {
        get
        {
            if (null == _instance)
            {
                GameObject go = new GameObject();
                go.name = typeof(NumberSetting).Name;
                go.AddComponent<NumberSetting>();
            }
            return _instance;
        }
    }
 
    private void Awake()
    {
        if (null != _instance)
        {
            Destroy(this);
        }
        else
        {
            _instance = this;
        }
        diceOne = GameObject.Find("shaizi").GetComponent<DiceRotate>();
        diceTow = GameObject.Find("shaizi (1)").GetComponent<DiceRotate>();
    }

    public void SetNum(int ran1, int ran2)
    {
        diceOne.SetNum(ran1);
        diceTow.SetNum(ran2);
        diceOne.isRot = true;
        diceTow.isRot = true;
    }

    public void RotateSetting(float speedWorld, float speedSelf,float speedAdjust,float time)//公转速度，自转速度，旋转到点数时旋转速度，旋转时间
    {
        diceOne.spWorld = speedWorld;
        diceOne.spSelf = speedSelf;
        diceOne.spAdjust = speedAdjust;
        diceOne.endTime = time;
        diceTow.spWorld = speedWorld;
        diceTow.spSelf = speedSelf;
        diceTow.spAdjust = speedAdjust;
        diceTow.endTime = time;
    }

}
