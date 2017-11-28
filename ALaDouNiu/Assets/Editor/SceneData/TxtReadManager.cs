using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System;


namespace Asset.Scripts.Table
{
    public class Singleton<T>
    {
        protected static readonly T ms_instance = Activator.CreateInstance<T>();
        public static T Instance { get { return ms_instance; } }
        protected Singleton() { }
    }

    public interface ITableItem
    {
        int Key();
    }

    public interface ITableManager
    {
        string TableName();
        object TableData { get; }
    }

    public abstract class TxtReadManager<T, TU> : Singleton<TU> where T : ITableItem
    {
        protected string tableName;
        public object TableData { get { return _mItemArray; } }

        protected T[] _mItemArray;
        protected Dictionary<int, int> _mKeyItemMap = new Dictionary<int, int>();


        public abstract string GetFilePath();
        public abstract string GetFileName();

        protected string index = string.Empty;

        public T GetItem(string index, int key)
        {
            if (index != this.index)
            {
                this.index = index;
                UpdateTableInfo();
            }

            int itemIndex;
            if (_mKeyItemMap.TryGetValue(key, out itemIndex))
            {
                if (_mItemArray[itemIndex] == null)
                    Debug.LogWarning(typeof(T).ToString() + "------未找到key----key = " + key);
                return _mItemArray[itemIndex];
            }
            return default(T);
        }

        public T[] GetAllItem(string index)
        {
            if (index != this.index)
            {
                this.index = index;
                UpdateTableInfo();
            }
            return _mItemArray;
        }

        public void UpdateTableInfo()
        {
            if (string.IsNullOrEmpty(index))
                return;
            _mItemArray = PraseTable();
            if (_mItemArray == null)
            {
                return;
            }
            for (int i = 0; i < _mItemArray.Length; i++)
                _mKeyItemMap[_mItemArray[i].Key()] = i;
        }

        private T[] PraseTable()
        {
            //var fileAddress = Path.Combine(GetFilePath(), GetFileName() + index.ToString() + ".txt");
            string path = GetFilePath();
            string name = GetFileName() + index+".txt";
            try
            {
                StreamReader r = File.OpenText(path + "//" + name);
                string s = r.ReadToEnd();
                r.Close();
                r.Dispose();

                string[] lines = s.Split("\r\n".ToCharArray(), StringSplitOptions.RemoveEmptyEntries);
                if (lines.Length < 3)
                {
                    Debug.LogError("表格文件行数错误，【1】属性名称【2】变量名称【3-...】值：" + GetFileName() + index);
                    return null;
                }

                Dictionary<int, FieldInfo> propertyInfos = GetPropertyInfos<T>(lines[1]);

                T[] array = new T[lines.Length - 2];
                for (int i = 0; i < lines.Length - 2; i++)
                    array[i] = ParseObject<T>(lines, i + 2, propertyInfos);

                return array;
            }
            catch(Exception e)
            {
                Debug.LogError(e.ToString());
                return null;
            }

        }

        private Dictionary<int, FieldInfo> GetPropertyInfos<T>(string memberLine)
        {
            Type objType = typeof(T);

            string[] members = memberLine.Split("\t".ToCharArray(), StringSplitOptions.RemoveEmptyEntries);
            Dictionary<int, FieldInfo> propertyInfos = new Dictionary<int, FieldInfo>();
            for (int i = 0; i < members.Length; i++)
            {
                FieldInfo fieldInfo = objType.GetField(members[i]);
                if (fieldInfo == null)
                    continue;
                propertyInfos[i] = fieldInfo;
            }

            return propertyInfos;
        }

        private T ParseObject<T>(string[] lines, int idx, Dictionary<int, FieldInfo> propertyInfos)
        {
            T obj = Activator.CreateInstance<T>();
            string line = lines[idx];
            string[] values = line.Split('\t');
            foreach (KeyValuePair<int, FieldInfo> pair in propertyInfos)
            {
                if (pair.Key >= values.Length)
                    continue;

                string value = values[pair.Key];
                if (string.IsNullOrEmpty(value))
                    continue;

                try
                {
                    ParsePropertyValue(obj, pair.Value, value);
                }
                catch (Exception)
                {
                    Debug.LogError(string.Format("ParseError: Row={0} Column={1} Name={2} Want={3} Get={4}",
                        idx + 1,
                        pair.Key + 1,
                        pair.Value.Name,
                        pair.Value.FieldType.Name,
                        value));
                    throw;
                }
            }
            return obj;
        }

        private void ParsePropertyValue<T>(T obj, FieldInfo fieldInfo, string valueStr)
        {
            System.Object value;
            if (fieldInfo.FieldType.IsEnum)
                value = Enum.Parse(fieldInfo.FieldType, valueStr);
            else
            {
                if (fieldInfo.FieldType == typeof(int))
                    value = int.Parse(valueStr);
                else if (fieldInfo.FieldType == typeof(byte))
                    value = byte.Parse(valueStr);
                else if (fieldInfo.FieldType == typeof(float))
                    value = float.Parse(valueStr);
                else if (fieldInfo.FieldType == typeof(double))
                    value = double.Parse(valueStr);
                else
                {
                    if (valueStr.Contains("\"\""))
                        valueStr = valueStr.Replace("\"\"", "\"");

                    if (valueStr.Length > 2 && valueStr[0] == '\"' && valueStr[valueStr.Length - 1] == '\"')
                        valueStr = valueStr.Substring(1, valueStr.Length - 2);

                    value = valueStr;
                }
            }

            fieldInfo.SetValue(obj, value);
        }

    }
}
