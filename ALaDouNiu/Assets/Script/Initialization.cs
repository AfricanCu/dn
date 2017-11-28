/*
/*code is far away from bug with the animal protecting
    *  ┏┓　　　┏┓
    *┏┛┻━━━┛┻┓
    *┃　　　　　　　┃
    *┃　　　━　　　┃
    *┃　┳┛　┗┳　┃
    *┃　　　　　　　┃
    *┃      ┻　  　┃
    *┃　　　　　　　┃
    *┗━┓　　　┏━┛
    *　　┃　　　┃神兽保佑
    *　　┃　　　┃代码无BUG！
    *　　┃　　　┗━━━┓
    *　　┃　　　　　　　┣┓
    *　　┃　　　　　　　┏┛
    *　　┗┓┓┏━┳┓┏┛
    *　　　┃┫┫　┃┫┫
    *　　　┗┻┛　┗┻┛ 
    *　　　
    *              
*/

using UnityEngine;
using System.Collections;
using LuaInterface;

/// <summary>
/// 程序最先执行的地方，在这里启动程序的初始化引导
/// </summary>
public class Initialization : MonoBehaviour
{
    private static Initialization _Initialization = null;

    private void Awake()
    {
        if (null == _Initialization)
        {
            _Initialization = this;
        }
        else
        {
            Destroy(this);
        }
    }

    private void Start()
    {
        Init();
    }

    private void Init()
    {
        ConnectionManager.Reset();
        ProcessMsg Instance = ProcessMsg.Instance;
        LuaMgr.Dispose();
        LuaMgr.Create();
    }

    private void Reset()
    {
        this.gameObject.name = this.GetType().Name;
    }
}
