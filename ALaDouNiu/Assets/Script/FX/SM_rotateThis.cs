using UnityEngine;
using System.Collections;

public class SM_rotateThis : FxContorlBase
{
    public float rotationSpeedX =90;
    public float rotationSpeedY =0;
    public float rotationSpeedZ =0;

    private Vector3 rotationVector
    {
        get { return new Vector3(rotationSpeedX, rotationSpeedY, rotationSpeedZ); }
    }

    // Use this for initialization
    void Start () {
	
	}

    // Update is called once per frame
    void Update()
    {
        update();
    }

    protected override void FxUpdate(float deltaTime)
    {
        transform.Rotate(rotationVector * deltaTime);
    }
}
