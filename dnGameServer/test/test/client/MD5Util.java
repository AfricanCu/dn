package test.client;

import java.security.MessageDigest;
import java.sql.Timestamp;

public class MD5Util {

    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
public static long time=1000;
    public static void main(String[] args) {
//        long time = System.currentTimeMillis();
//        System.out.println(MD5Util.MD5("2012122166ghjdghjfghjfdgjf___g++hjfgtyutyijhjlkilhjklhjklhjkgtyuioyuiuyituygklgh").toLowerCase());
//        // System.out.println(MD5Util.MD5("加密"));
//        System.out.println(System.currentTimeMillis() - time);
//        String a = "fdsfasdsfasfsdafas";
//        String b = "fdsfasdsfasfsdafas";
//        System.out.println(a.equals(b));
        Timestamp timestamp = new Timestamp(time);
        System.out.println(timestamp);
    }

    /**
     * 检查协议是否通过
     *
     * @param md5Num
     * @param str
     * @return
     */
    public static boolean checkAgreementByMd5(String md5Num, String str) {
        String md5Num2 = MD5(str);
//		System.err.println("原文"+str);
//		System.err.println("密文"+md5Num2);
        return md5Num2.equals(md5Num);
    }
}
