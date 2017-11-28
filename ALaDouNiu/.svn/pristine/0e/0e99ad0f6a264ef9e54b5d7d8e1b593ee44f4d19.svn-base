using UnityEngine;
using Assets.Scripts.Attribute;

namespace Asset.Scripts.Table
{
    [System.Serializable]
    public class TSceneChair : ITableItem
    {
        /// <summary>
        /// 主键（仅作唯一标识）
        /// </summary>
        [DisplayName("ID")]
        public int ID;
        /// <summary>
        /// 配置人数是多少
        /// </summary>
        [DisplayName("配置人数")]
        public int ConfigNum;
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
        /// 旋转x
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

        public int Key()
        {
            return ID;
        }

        public static implicit operator bool(TSceneChair value)
        {
            return value != null;
        }
    }


    public class TSceneChairMgr : TxtReadManager<TSceneChair, TSceneChairMgr>
    {
        public override string GetFilePath()
        {
            return Application.dataPath + "/Res/SceneData";
        }

        public override string GetFileName()
        {
            return "TSceneChair";
        }
    }
}
