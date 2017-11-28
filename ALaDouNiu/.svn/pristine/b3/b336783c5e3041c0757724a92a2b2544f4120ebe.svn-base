using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class DiceRotate : MonoBehaviour
{


    private Transform centerPoint;//旋转中心点

    private Transform myTransform;//骰子

    public float spWorld = 2000;//公转速度

    private float speedWorld ;

    public float spSelf = 3500;//自转速度

    private float speedSelf ;

    public float spAdjust = 7;//调整点数时旋转的速度

    private float myTime = 0;

    private float rotateTime = 0;

    private int index = 1;//用于随机骰子自转方向

    private Quaternion targetQua;//目标点数的四元数

    public Vector3 targetPos;//骰子开始旋转扩大点

    public Vector3 initPos;//骰子结束旋转缩小点

    public float endTime = 0.9f;//骰子转动时间

    public bool isRot = false;//骰子是否开始运动

    public bool isAdjust = false;//是否开始调整骰子的点数

    public int num;//点数

    
    private void Awake()
    {
		
        centerPoint = GameObject.Find("Center").GetComponent<Transform>();
        myTransform = this.transform;
    }
    void Start()
    {
        //SetNum();
    }
    void Update()
    {
        //if (Input.GetMouseButtonDown(0))
        //{
        //    SetNum(2);
        //    isRot = true;
        //}
        if (rotateTime >= endTime)
        {
            isRot = false;
            isAdjust = true;
        }
        if (rotateTime>(endTime/2)) {
            speedWorld = spWorld/3 ;
            speedSelf = spSelf/3;
        }
        if (isRot)
        {
            rotateTime += Time.deltaTime;
            myTime += Time.deltaTime;
            this.transform.RotateAround(centerPoint.position, Vector3.up, speedWorld * Time.deltaTime);
            if (myTime >= 1)
            {
                index = Random.Range(1, 4);
                myTime = 0;
            }
            if (index == 1)
            {
                this.transform.Rotate(Vector3.left * speedSelf * Time.deltaTime, Space.Self);
            }
            if (index == 2)
            {
                this.transform.Rotate(Vector3.up * speedSelf * Time.deltaTime, Space.Self);
            }
            if (index == 3)
            {
                this.transform.Rotate(Vector3.forward * speedSelf * Time.deltaTime, Space.Self);
            }
        }
        if (!isRot && isAdjust)
        {
            myTransform.rotation = Quaternion.Slerp(myTransform.rotation, targetQua, spAdjust * Time.deltaTime);
            rotateTime = 0;
        }
    }
    public void SetNum(int number)
    {
        speedSelf = spSelf;
        speedWorld = spWorld;
        num = number;
        myTransform.LookAt(centerPoint);
        switch (num)
        {
            case 1:
                targetQua = myTransform.rotation * Quaternion.AngleAxis(-90, Vector3.left);
                break;
            case 2:
                targetQua = myTransform.rotation * Quaternion.AngleAxis(180, Vector3.forward);
                break;
            case 3:
                targetQua = myTransform.rotation * Quaternion.AngleAxis(-90, Vector3.forward);
                break;
            case 4:
                targetQua = myTransform.rotation * Quaternion.AngleAxis(90, Vector3.forward);
                break;
            case 5:
                targetQua = myTransform.rotation * Quaternion.AngleAxis(95, Vector3.up);
                break;
            case 6:
                targetQua = myTransform.rotation * Quaternion.AngleAxis(90, Vector3.left);
                break;
        }
    }
}
