using UnityEngine;
using System.Collections;
using Asset.Script.Core;

public class EncryptionTool : Singleton<EncryptionTool>
{
    public byte[] Encryption(byte[] data)
    {
        System.Array.Reverse(data);
        for(int i=0;i < data.Length;++i)
        {
            data[i] = (byte)~data[i];
        }
        return data;
    }

    public byte[] Decryption(byte[] data)
    {
        for (int i = 0; i < data.Length; ++i)
        {
            data[i] = (byte)~data[i];
        }
        System.Array.Reverse(data);
        return data;
    }
}
