using UnityEngine;
using LuaInterface;

public class CardDrag : MonoBehaviour
{
	private float speed = 0.005555556f;
    private float yToOut = 0;
    private int index = 0;
    private LuaFunction clickFunc = null;
    private LuaFunction dragOutFunc = null;

    private Vector3 mLocPos = Vector3.zero;
    private Transform mTrans;
    private Transform father;
    private Camera cam;

	void Awake ()
	{
		mTrans = this.transform.parent;
        father = mTrans.parent;
    }

    public void SetParams(Camera cam, float yToOut, int index, LuaFunction clickFunc, LuaFunction dragOutFunc)
    {
        this.cam = cam;
        //this.speed = speed;
        this.index = index;
        mLocPos = mTrans.localPosition;
        this.yToOut = yToOut;
        this.index = index;
        this.clickFunc = clickFunc;
        this.dragOutFunc = dragOutFunc;
    }

    public void SetIndex(int index, Vector3 newLocPos)
    {
        this.index = index;
        mLocPos = newLocPos;
    }

    /// <summary>
    /// 如果牌只点击一下，又去点击其他的牌，需要将之前那张牌归位
    /// </summary>
    public void Reset()
    {
        mTrans.transform.localPosition = mLocPos;
    }

	void OnDrag (Vector2 delta)
	{
        //Debug.Log("Input:" + Input.mousePosition.ToString());
        //判断是否可以出牌
        Vector3 now = cam.ScreenToWorldPoint(Input.mousePosition);
        mTrans.transform.position = new Vector3(now.x, now.y-0.25f, 0.5f);
        //Debug.Log("now.x;" + now.x.ToString() + " pos:" + mTrans.transform.position.x.ToString());
        //Debug.Log("now.y:" + now.y.ToString() + " pos:" + mTrans.transform.position.y.ToString());
    }

    void OnPress(bool pressed)
    {
        if(!pressed)
        {
            //Debug.Log("unPress!!!!!!!!!!");
            //mTrans.transform.parent = father;

            Vector3 now = mTrans.transform.localPosition;
            if (now.y - mLocPos.y >= yToOut)
            {
                if (dragOutFunc != null)
                {
                    dragOutFunc.BeginPCall();
                    dragOutFunc.Push(this.index);
                    dragOutFunc.PCall();
                    dragOutFunc.EndPCall();
                }
            }

            mTrans.transform.localPosition = mLocPos;
        }
        else
        {
            //Debug.Log("press!!!!!!!!!!");
            //mTrans.transform.parent = null;
        }
    }

    void OnClick()
    {
        //Debug.Log("Click~~~~~~~~~~~~~``");
        if (clickFunc != null)
        {
            clickFunc.BeginPCall();
            clickFunc.Push(this.index);
            clickFunc.PCall();
            clickFunc.EndPCall();
        }
    }
}