using UnityEngine;
using UnityEditor;
using UnityEngine.SceneManagement;
using System.Collections.Generic;
using System.Reflection;
using System;
using System.IO;
using System.Text;
using Asset.Scripts.Table;
using Assets.Scripts.Attribute;

public class SceneChairEditor
{
    private static string savePath = Application.dataPath + "/Res/SceneData/TSceneChair{0}.txt";
    private static string camSavePath = Application.dataPath + "/Res/SceneData/TSceneCamera{0}.txt";

    [MenuItem("工具/场景凳子/保存配置")]
    public static void SaveData()
    {
        if (SavePointData())
        {
            EditorUtility.DisplayDialog("成功", "保存数据成功！\n", "确定");
            return;
        }

        EditorUtility.DisplayDialog("错误", "保存数据失败！", "确定");
    }

    private static bool SavePointData()
    {
        List<TSceneChair> _Spawn_Points = new List<TSceneChair>();
        GameObject[] chairs = GameObject.FindGameObjectsWithTag("Chair");

        int configNum = chairs.Length;
        if (configNum == 0)
        {
            return false;
        }

        TSceneChairMgr.Instance.UpdateTableInfo();
        TSceneChair[] _points = TSceneChairMgr.Instance.GetAllItem(SceneManager.GetActiveScene().name);
        if (_points != null && _points.Length > 0)
        {
            _Spawn_Points.AddRange(_points);
        }

        _Spawn_Points.RemoveAll(item => item.ConfigNum == configNum);

        Transform chair1 = null;
        for(int i = 0; i< chairs.Length; i++)
        {
            Debug.Log("chairs[i].name:" + chairs[i].name);
            string[] chairName = chairs[i].name.Split('_');
            if(chairName.Length < 2)
            {
                continue;
            }
            int num = int.Parse(chairName[1]);

            int id = configNum * 100 + num;
            TSceneChair data = new TSceneChair();
            data.ID = id;
            data.ConfigNum = configNum;
            data.PosX = chairs[i].transform.localPosition.x;
            data.PosY = chairs[i].transform.localPosition.y;
            data.PosZ = chairs[i].transform.localPosition.z;
            data.RotX = chairs[i].transform.localEulerAngles.x;
            data.RotY = chairs[i].transform.localEulerAngles.y;
            data.RotZ = chairs[i].transform.localEulerAngles.z;

            _Spawn_Points.Add(data);

            if(num == 1)
            {
                chair1 = chairs[i].transform;
            }
        }

        //SpawnPoint排序，第一优先级为LevelID，第二优先级为PointID
        _Spawn_Points.Sort((x, y) =>
        {
            return ((int)x.ID).CompareTo((int)y.ID);
        });


        string point_datas = SerialerPropertsName(typeof(TSceneChair));
        point_datas += SerialerProperts(typeof(TSceneChair));
        for (int i = 0; i < _Spawn_Points.Count; ++i)
        {
            point_datas += SerialerData<TSceneChair>(_Spawn_Points[i]);
        }

        try
        {
            using (StreamWriter sw = new StreamWriter(string.Format(savePath, SceneManager.GetActiveScene().name), false, Encoding.Unicode))
            {
                sw.Write(point_datas);
                sw.Close();
            }
        }
        catch (Exception ex)
        {
            EditorUtility.DisplayDialog("错误", ex.Message, "确定");
            return false;
        }

        SaveCameraData(chair1, configNum);

        return true;
    }

    private static bool SaveCameraData(Transform chair1, int configNum)
    {
        List<TSceneCamera> _Spawn_Cameras = new List<TSceneCamera>();
        TSceneCameraMgr.Instance.UpdateTableInfo();
        TSceneCamera[] _cameraConfig = TSceneCameraMgr.Instance.GetAllItem(SceneManager.GetActiveScene().name);
        if (_cameraConfig != null && _cameraConfig.Length > 0)
        {
            _Spawn_Cameras.AddRange(_cameraConfig);
        }

        _Spawn_Cameras.RemoveAll(item => item.ID == configNum);

        Camera.main.transform.parent = chair1;

        Vector3 gap = Camera.main.transform.localPosition;
        Vector3 q = Camera.main.transform.localEulerAngles;
        SimpleCamCtrl ctrl = Camera.main.transform.GetComponent<SimpleCamCtrl>();
        TSceneCamera sceneCamera = new TSceneCamera();
        sceneCamera.ID = configNum;
        sceneCamera.PosX = gap.x;
        sceneCamera.PosY = gap.y;
        sceneCamera.PosZ = gap.z;
        sceneCamera.RotX = q.x;
        sceneCamera.RotY = q.y;
        sceneCamera.RotZ = q.z;
        sceneCamera.DegreesX = ctrl.degrees.x;
        sceneCamera.DegreesY = ctrl.degrees.y;
        sceneCamera.DragSpeed = ctrl.DragSpeed;
        sceneCamera.BackSpeed = ctrl.BackSpeed;
        sceneCamera.BackDelay = ctrl.BackDelay;
        sceneCamera.StarX = ctrl.starX;
        sceneCamera.StarY = ctrl.starY;

        Camera.main.transform.parent = null;

        _Spawn_Cameras.Add(sceneCamera);
        _Spawn_Cameras.Sort((x, y) =>
        {
            return ((int)x.ID).CompareTo((int)y.ID);
        });

        string camera_datas = SerialerPropertsName(typeof(TSceneCamera));
        camera_datas += SerialerProperts(typeof(TSceneCamera));
        for (int i = 0; i < _Spawn_Cameras.Count; ++i)
        {
            camera_datas += SerialerData<TSceneCamera>(_Spawn_Cameras[i]);
        }

        try
        {
            using (StreamWriter sw = new StreamWriter(string.Format(camSavePath, SceneManager.GetActiveScene().name), false, Encoding.Unicode))
            {
                sw.Write(camera_datas);
                sw.Close();
            }
        }
        catch (Exception ex)
        {
            EditorUtility.DisplayDialog("错误", ex.Message, "确定");
            return false;
        }

        return true;
    }


    [MenuItem("工具/场景凳子/加载配置")]
    public static void LoadPoint()
    {
        InputWindow input_window = null;
        input_window = InputWindow.ShowModalInputWindow("加载场景凳子", "请输入配置人数", delegate (string value)
        {
            int configNum = 0;
            
            if (int.TryParse(value, out configNum))
            {
                LoadPointData(SceneManager.GetActiveScene().name, configNum);
            }
        }
        );
    }

    private static void LoadPointData(string fileSuffix, int configNum)
    {
        GameObject[] chairs = GameObject.FindGameObjectsWithTag("Chair");
        for(int i = chairs.Length -1; i >= 0; i--)
        {
            GameObject.DestroyImmediate(chairs[i]);
        }

        List<TSceneChair> _Spawn_Points = new List<TSceneChair>();

        TSceneChairMgr.Instance.UpdateTableInfo();
        TSceneChair[] _points = TSceneChairMgr.Instance.GetAllItem(fileSuffix);
        if (_points != null && _points.Length > 0)
        {
            _Spawn_Points.AddRange(_points);
        }
        
        _Spawn_Points.RemoveAll(item => item.ConfigNum != configNum);

        GameObject chairModel = GameObject.Find("ChairModel");
        Transform chairParent = chairModel.transform.parent;
        Transform chair1 = null;
        if (_Spawn_Points.Count > 0)
        {
            for (int i = 0; i < _Spawn_Points.Count; i++)
            {
                GameObject tmp = GameObject.Instantiate<GameObject>(chairModel);

                int num = _Spawn_Points[i].ID % 100;
                tmp.name = string.Format("Chair_{0}", num);
                tmp.tag = "Chair";

                tmp.transform.parent = chairParent;
                tmp.transform.localScale = Vector3.one;
                tmp.transform.localPosition = new Vector3(_Spawn_Points[i].PosX, _Spawn_Points[i].PosY, _Spawn_Points[i].PosZ);
                tmp.transform.localEulerAngles = new Vector3(_Spawn_Points[i].RotX, _Spawn_Points[i].RotY, _Spawn_Points[i].RotZ);

                if(num == 1)
                {
                    chair1 = tmp.transform;
                }
            }
        }
        else
        {
            for (int i = 0; i < configNum; i++)
            {
                GameObject tmp = GameObject.Instantiate<GameObject>(chairModel);

                tmp.name = string.Format("Chair_{0}", (i+1));
                tmp.tag = "Chair";

                tmp.transform.parent = chairParent;
                tmp.transform.localScale = Vector3.one;
                tmp.transform.localPosition = Vector3.zero;
                tmp.transform.localEulerAngles = Vector3.zero;
            }
        }

        LoadCameraData(fileSuffix, configNum, chair1);


    }

    private static void LoadCameraData(string fileSuffix, int configNum, Transform chair1)
    {
        TSceneCameraMgr.Instance.UpdateTableInfo();
        TSceneCamera sceneCamera = TSceneCameraMgr.Instance.GetItem(fileSuffix, configNum);

        if(sceneCamera != null)
        {
            Vector3 v = new Vector3(sceneCamera.PosX, sceneCamera.PosY, sceneCamera.PosZ);
            Vector3 q = new Vector3(sceneCamera.RotX, sceneCamera.RotY, sceneCamera.RotZ);
            Camera.main.transform.parent = chair1;
            Camera.main.transform.localPosition = v;
            Camera.main.transform.localEulerAngles= q;

            SimpleCamCtrl ctrl = Camera.main.transform.GetComponent<SimpleCamCtrl>();
            ctrl.degrees.x = sceneCamera.DegreesX;
            ctrl.degrees.y = sceneCamera.DegreesY;
            ctrl.DragSpeed = sceneCamera.DragSpeed;
            ctrl.BackSpeed = sceneCamera.BackSpeed;
            ctrl.BackDelay = sceneCamera.BackDelay;
            ctrl.starX = sceneCamera.StarX;
            ctrl.starY = sceneCamera.StarY;

            Camera.main.transform.parent = null;
        }
        
    }

    [MenuItem("工具/场景凳子/新配置")]
    public static void NewPoint()
    {
        InputWindow input_window = null;
        input_window = InputWindow.ShowModalInputWindow("重置场景凳子位置", "人数,圆半径:16,3", delegate (string value)
        {
            int configNum = 0;
            float configR = 0;

            string[] pars = value.Split(',');

            if(pars.Length == 2 
                && int.TryParse(pars[0], out configNum)
                && float.TryParse(pars[1], out configR))
            {
                NewPointData(SceneManager.GetActiveScene().name, configNum, configR);
            }
        }
        );
    }

    private static void NewPointData(string fileSuffix, int configNum, float configR)
    {
        GameObject[] chairs = GameObject.FindGameObjectsWithTag("Chair");
        for (int i = chairs.Length - 1; i >= 0; i--)
        {
            GameObject.DestroyImmediate(chairs[i]);
        }

        GameObject chairModel = GameObject.Find("ChairModel");
        Transform chairParent = chairModel.transform.parent;
        float angle = 360f / configNum;
        float radius = Mathf.Deg2Rad * angle;
        for (int i = 0; i < configNum; i++)
        {
            GameObject tmp = GameObject.Instantiate<GameObject>(chairModel);

            tmp.name = string.Format("Chair_{0}", (i + 1));
            tmp.tag = "Chair";

            tmp.transform.parent = chairParent;
            tmp.transform.localScale = Vector3.one;
            float tmpRadius = radius * i;
            Vector3 pos = new Vector3(Mathf.Sin(tmpRadius)*configR, -Mathf.Cos(tmpRadius)*configR, 0);
            Vector3 rot = new Vector3(0, 0, i * angle - 180);
            tmp.transform.localPosition = pos;
            tmp.transform.localEulerAngles = rot;
        }
    }


    [MenuItem("工具/场景摄像机/查看座位视角 %&O")]
    public static void LookChairView()
    {
        InputWindow input_window = null;
        input_window = InputWindow.ShowModalInputWindow("查看座位视角", "请输入座位号", delegate (string value)
        {
            int chairNum = 0;

            if (int.TryParse(value, out chairNum))
            {
                LoadChairView(SceneManager.GetActiveScene().name, chairNum);
            }
        }
        );
    }


    private static bool LoadChairView(string fileSuffix, int chairNum)
    {
        List<TSceneChair> _Spawn_Points = new List<TSceneChair>();
        GameObject[] chairs = GameObject.FindGameObjectsWithTag("Chair");

        int configNum = chairs.Length;
        if (configNum == 0)
        {
            return false;
        }

        TSceneCameraMgr.Instance.UpdateTableInfo();
        TSceneCamera sceneCamera = TSceneCameraMgr.Instance.GetItem(fileSuffix, configNum);
        GameObject lookChair = GameObject.Find(string.Format("Chair_{0}", chairNum));
        if (sceneCamera != null && lookChair != null)
        {
            Vector3 v = new Vector3(sceneCamera.PosX, sceneCamera.PosY, sceneCamera.PosZ);
            Vector3 q = new Vector3(sceneCamera.RotX, sceneCamera.RotY, sceneCamera.RotZ);

            Camera.main.transform.parent = lookChair.transform;
            Camera.main.transform.localPosition = v;
            Camera.main.transform.localEulerAngles = q;

            Camera.main.transform.parent = null;
        }

        return true;
    }

    public static string SerialerPropertsName(Type type)
    {
        string data = "";
        FieldInfo[] fields = type.GetFields();
        for (int i = 0; i < fields.Length; ++i)
        {
            DisplayNameAttribute[] names = fields[i].GetCustomAttributes(typeof(DisplayNameAttribute), false) as DisplayNameAttribute[];
            data += names[0].displayName + '\t';
        }
        data = data.TrimEnd();
        data += "\r\n";
        return data;
    }

    public static string SerialerProperts(Type type)
    {
        string data = "";
        FieldInfo[] fields = type.GetFields();
        for (int i = 0; i < fields.Length; ++i)
        {
            data += fields[i].Name + '\t';
        }
        data = data.TrimEnd();
        data += "\r\n";
        return data;
    }

    public static string SerialerData<T>(T obj)
    {
        string data = "";
        Type type = obj.GetType();
        FieldInfo[] fields = type.GetFields();
        for (int i = 0; i < fields.Length; ++i)
        {
            object value = fields[i].GetValue(obj);
            if (value != null)
            {
                data += value.ToString();
            }
            data += '\t';
        }
        data = data.TrimEnd();
        data += "\r\n";
        return data;
    }

}
