using UnityEngine;
using Assets.Scripts.Attribute;

namespace Asset.Scripts.Table
{
    [System.Serializable]
    public class TSceneCamera : ITableItem
    {
        /// <summary>
        /// 主键（仅作唯一标识）
        /// </summary>
        [DisplayName("ID")]
        public int ID;
        /// <summary>
        /// 坐标x
        /// </summary>
        [DisplayName("坐标x")]
        public float PosX;
        /// <summary>
        /// 坐标y
        /// </summary>
        [DisplayName("坐标y")]
        public float PosY;
        /// <summary>
        /// 坐标z
        /// </summary>
        [DisplayName("坐标z")]
        public float PosZ;
        /// <summary>
        /// 旋转y
        /// </summary>
        [DisplayName("旋转x")]
        public float RotX;
        /// <summary>
        /// 旋转y
        /// </summary>
        [DisplayName("旋转y")]
        public float RotY;
        /// <summary>
        /// 旋转z
        /// </summary>
        [DisplayName("旋转z")]
        public float RotZ;
        [DisplayName("相机旋转x")]
        public float DegreesX;
        [DisplayName("相机旋转y")]
        public float DegreesY;
        [DisplayName("DragSpeed")]
        public float DragSpeed;
        [DisplayName("BackSpeed")]
        public float BackSpeed;
        [DisplayName("BackDelay")]
        public float BackDelay;
        [DisplayName("starX")]
        public float StarX;
        [DisplayName("starY")]
        public float StarY;


        public int Key()
        {
            return ID;
        }

        public static implicit operator bool(TSceneCamera value)
        {
            return value != null;
        }
    }


    public class TSceneCameraMgr : TxtReadManager<TSceneCamera, TSceneCameraMgr>
    {
        public override string GetFilePath()
        {
            return Application.dataPath + "/Res/SceneData";
        }

        public override string GetFileName()
        {
            return "TSceneCamera";
        }
    }
}
