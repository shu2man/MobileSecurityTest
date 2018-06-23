package com.example.hxs15.mobilesecuritytest;

import java.security.MessageDigest;

/**
 * Created by hxs15 on 2018-6-23.
 */

public class MD5Utils {
    public static String myMD5Encrypt(String psd){
        //采用MD5对密码进行加密，bcrypt有点耗时耗电
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try{
            byte[] btInput=psd.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInstance=MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInstance.update(btInput);
            //获得密文
            byte[] md=mdInstance.digest();
            //把密文转换成十六进制的字符串形式
            int len=md.length;
            char str[]=new char[len*2];
            int k=0;
            for(int i=0;i<len;i++){
                byte byte0=md[i];
                str[k++]=hexDigits[byte0 >>> 4 & 0xf];
                str[k++]=hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static boolean myMD5Verify(String psd,String encryCode){
        return myMD5Encrypt(psd).equals(encryCode);
    }

    public String myBCrypt(String psd){

        return null;
    }
}
