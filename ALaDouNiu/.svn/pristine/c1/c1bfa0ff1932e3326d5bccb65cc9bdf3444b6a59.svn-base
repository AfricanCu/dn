using UnityEngine;
using System.Collections;

/// <summary>
/// 调试切牌动画用的脚本
/// </summary>
public class CardAnimation : MonoBehaviour {
    /// <summary>
    /// 卡牌父目录
    /// </summary>
    public Transform cardBase;
    /// <summary>
    /// 牌模型
    /// </summary>
    public GameObject cardModelTemp;
    /// <summary>
    /// 每张牌之间的间隔高度
    /// </summary>
    public float cardGapZ = 0.002f;
    /// <summary>
    /// 多少张牌
    /// </summary>
    public int cardNum = 52;
    /// <summary>
    /// 提起牌的父节点
    /// </summary>
    public GameObject firstUpBase;
    /// <summary>
    /// 未被提起的牌的父节点
    /// </summary>
    public GameObject secondDownBase;
    /// <summary>
    /// 第一次提起多少张牌
    /// </summary>
    [Range(10,42)]
    public int firstUpNum = 30;
    /// <summary>
    /// 提起的高度
    /// </summary>
    public Vector3 firstUpVector = new Vector3(0, 0.12f, 0.3f);
    /// <summary>
    /// 提起的时间
    /// </summary>
    public float firstUpTime = 1f;
    /// <summary>
    /// 提起后旋转的角度
    /// </summary>
    public Vector3 firstUpRot = new Vector3(135, 0, 0);
    /// <summary>
    /// 旋转的时间
    /// </summary>
    public float firstUpRotTime = 1f;
    /// <summary>
    /// 旋转时间延迟
    /// </summary>
    public float firstUpRotDelayTime = 0.5f;
    /// <summary>
    /// 提起旋转后停留时间
    /// </summary>
    public float stopTime = 2f;
    /// <summary>
    /// 旋转回来的时间
    /// </summary>
    public float firstUpRotBackTime = 1f;
    /// <summary>
    /// 上叠排下落的位置
    /// </summary>
    public Vector3 firstUpDownVector = new Vector3(0, 0.25f, 0);
    /// <summary>
    /// 直线下落的时间
    /// </summary>
    public float firstUpDownTime = 1f;
    /// <summary>
    /// 直线下落执行的延迟时间
    /// </summary>
    public float firstUpDownDelayTime = 0.5f;
    /// <summary>
    /// 下叠牌往上提多少
    /// </summary>
    public Vector3 secondDownUpVector = new Vector3(0, 0, 0.3f);
    /// <summary>
    /// 下叠牌往上提的时间
    /// </summary>
    public float secondDownUpTime = 1f;
    /// <summary>
    /// 下叠牌往上提操作延迟时间
    /// </summary>
    public float secondDownUpDelayTime = 1f;
    /// <summary>
    /// 上叠牌归位时间
    /// </summary>
    public float firstUpBackTime = 1f;
    /// <summary>
    /// 下叠归位时间
    /// </summary>
    public float secondDownBackTime = 1f;
    /// <summary>
    /// 下叠归未操作延迟时间
    /// </summary>
    public float secondDownBackDelayTime = 0.5f;

    public GameObject[] cardModelArray;
    private TweenPosition firstUpBaseTPos;
    private TweenRotation firstUpBaseTRot;
    private UIPlayTween firstUpBasePlayTween;
    private TweenPosition secondDownBaseTPos;
    private UIPlayTween secondDownBasePlayTween;

	// Use this for initialization
	void Start () {
        cardModelArray = new GameObject[cardNum];
        firstUpBaseTPos = firstUpBase.GetComponent<TweenPosition>();
        firstUpBaseTRot = firstUpBase.GetComponent<TweenRotation>();
        firstUpBasePlayTween = firstUpBase.GetComponent<UIPlayTween>();
        secondDownBaseTPos = secondDownBase.GetComponent<TweenPosition>();
        secondDownBasePlayTween = secondDownBase.GetComponent<UIPlayTween>();
    }
	
	// Update is called once per frame
	void Update () {
	
	}
    [ContextMenu("Generate Card")]
    public void Generate()
    {
        for(int i = 0; i< cardModelArray.Length; i++)
        {
            if(cardModelArray[i] != null)
            {
                GameObject.DestroyImmediate(cardModelArray[i]);
            }
        }

        for(int j = 0; j < cardNum; j++)
        {
            cardModelArray[j] = GameObject.Instantiate<GameObject>(cardModelTemp);
            cardModelArray[j].SetActive(true);
            cardModelArray[j].transform.parent = cardBase;
            cardModelArray[j].transform.localPosition = new Vector3(0, 0, cardGapZ * j);
            cardModelArray[j].transform.localEulerAngles = Vector3.zero;
            cardModelArray[j].transform.localScale = Vector3.one;
            cardModelArray[j].name = (j + 1).ToString();
        }
    }

    [ContextMenu("Clear")]
    public void Clear()
    {
        for (int i = 0; i < cardModelArray.Length; i++)
        {
            if (cardModelArray[i] != null)
            {
                GameObject.DestroyImmediate(cardModelArray[i]);
            }
        }
    }

    [ContextMenu("DoAnimation")]
    public void DoAnimation()
    {
        for (int i = 0; i < cardModelArray.Length; i++)
        {
            if (cardModelArray[i] != null)
            {
                cardModelArray[i].transform.parent = cardBase.transform;
            }
        }
        firstUpBase.transform.localPosition = Vector3.zero;
        firstUpBase.transform.localEulerAngles = Vector3.zero;
        secondDownBase.transform.localPosition = Vector3.zero;
        secondDownBase.transform.localEulerAngles = Vector3.zero;

        GameObject[] tmpArray = new GameObject[cardNum];

        int j = 0;
        for(int i = cardNum - firstUpNum; i < cardNum; i++, j++)
        {
            cardModelArray[i].transform.parent = firstUpBase.transform;
            tmpArray[j] = cardModelArray[i];
        }

        for(int k = 0; k < cardNum - firstUpNum; k++, j++)
        {
            cardModelArray[k].transform.parent = secondDownBase.transform;
            tmpArray[j] = cardModelArray[k];
        }
        cardModelArray = tmpArray;

        firstUpBaseTPos.from = Vector3.zero;
        firstUpBaseTPos.to = firstUpVector;
        firstUpBaseTPos.duration = firstUpTime;
        firstUpBaseTPos.delay = 0;

        firstUpBaseTRot.from = Vector3.zero;
        firstUpBaseTRot.to = firstUpRot;
        firstUpBaseTRot.duration = firstUpRotTime;
        firstUpBaseTRot.delay = firstUpRotDelayTime;

        firstUpBasePlayTween.resetOnPlay = true;
        firstUpBasePlayTween.Play(true);

        StartCoroutine(DoRotBackAndDown());
    }

    private IEnumerator DoRotBackAndDown()
    {
        Vector3 firstUpDownVectorTmp = new Vector3(firstUpDownVector.x, firstUpDownVector.y, -(cardNum - firstUpNum) * cardGapZ);
        yield return new WaitForSeconds(stopTime);

        firstUpBaseTPos.from = firstUpVector;
        firstUpBaseTPos.to = firstUpDownVectorTmp;
        firstUpBaseTPos.duration = firstUpDownTime;
        firstUpBaseTPos.delay = firstUpDownDelayTime;

        firstUpBaseTRot.from = firstUpRot;
        firstUpBaseTRot.to = Vector3.zero;
        firstUpBaseTRot.duration = firstUpRotBackTime;

        firstUpBasePlayTween.resetOnPlay = true;
        firstUpBasePlayTween.Play(true);

        secondDownBaseTPos.from = Vector3.zero;
        secondDownBaseTPos.to = secondDownUpVector;
        secondDownBaseTPos.duration = secondDownUpTime;
        secondDownBaseTPos.delay = secondDownUpDelayTime;
        secondDownBasePlayTween.resetOnPlay = true;
        secondDownBasePlayTween.Play(true);

        yield return new WaitForSeconds(secondDownUpTime + secondDownUpDelayTime);

        firstUpBaseTPos.from = firstUpDownVectorTmp;
        firstUpBaseTPos.to = new Vector3(0, 0, firstUpDownVectorTmp.z);
        firstUpBaseTPos.duration = firstUpBackTime;
        firstUpBaseTRot.from = Vector3.zero;
        firstUpBaseTRot.to = Vector3.zero;
        firstUpBaseTPos.duration = firstUpBackTime;
        firstUpBasePlayTween.resetOnPlay = true;
        firstUpBasePlayTween.Play(true);

        secondDownBaseTPos.from = secondDownUpVector;
        secondDownBaseTPos.to = new Vector3(0,0, firstUpNum*cardGapZ);
        secondDownBaseTPos.duration = secondDownBackTime;
        secondDownBaseTPos.delay = secondDownBackDelayTime;
        secondDownBasePlayTween.resetOnPlay = true;
        secondDownBasePlayTween.Play(true);

        yield return null;
    }
}
