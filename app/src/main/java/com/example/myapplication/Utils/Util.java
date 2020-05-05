package com.example.myapplication.Utils;

import android.util.Log;

import com.example.myapplication.DateStract.Guest;
import com.example.myapplication.DateStract.Loc;
import com.example.myapplication.DateStract.LocalKey;
import com.example.myapplication.Defin.Defin_crypto;
import com.example.myapplication.Utils.utils.sm4.SM4Utils;
import com.example.myapplication.owner_date;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    /**
     * 整形转换成网络传输的字节流（字节数组）型数据
     *
     * @param num 一个整型数据
     * @return 4个字节的自己数组
     */
    public static byte[] intToBytes(int num) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (0xff & (num >> 0));
        bytes[1] = (byte) (0xff & (num >> 8));
        bytes[2] = (byte) (0xff & (num >> 16));
        bytes[3] = (byte) (0xff & (num >> 24));
        return bytes;
    }

    /**
     * 四个字节的字节数据转换成一个整形数据
     *
     * @param bytes 4个字节的字节数组
     * @return 一个整型数据
     */
    public static int byteToInt(byte[] bytes) {
        int num = 0;
        int temp;
        temp = (0x000000ff & (bytes[0])) << 0;
        num = num | temp;
        temp = (0x000000ff & (bytes[1])) << 8;
        num = num | temp;
        temp = (0x000000ff & (bytes[2])) << 16;
        num = num | temp;
        temp = (0x000000ff & (bytes[3])) << 24;
        num = num | temp;
        return num;
    }

    /**
     * 长整形转换成网络传输的字节流（字节数组）型数据
     *
     * @param num 一个长整型数据
     * @return 4个字节的自己数组
     */
    public static byte[] longToBytes(long num) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (0xff & (num >> (i * 8)));
        }

        return bytes;
    }

    /**
     * 大数字转换字节流（字节数组）型数据
     *
     * @param n
     * @return
     */
    public static byte[] byteConvert32Bytes(BigInteger n) {
        byte[] tmpd = null;
        if (n == null) {
            return null;
        }

        if (n.toByteArray().length == 33) {
            tmpd = new byte[32];
            System.arraycopy(n.toByteArray(), 1, tmpd, 0, 32);
        } else if (n.toByteArray().length == 32) {
            tmpd = n.toByteArray();
        } else {
            tmpd = new byte[32];
            for (int i = 0; i < 32 - n.toByteArray().length; i++) {
                tmpd[i] = 0;
            }
            System.arraycopy(n.toByteArray(), 0, tmpd, 32 - n.toByteArray().length, n.toByteArray().length);
        }
        return tmpd;
    }

    /**
     * 换字节流（字节数组）型数据转大数字
     *
     * @param b
     * @return
     */
    public static BigInteger byteConvertInteger(byte[] b) {
        if (b[0] < 0) {
            byte[] temp = new byte[b.length + 1];
            temp[0] = 0;
            System.arraycopy(b, 0, temp, 1, b.length);
            return new BigInteger(temp);
        }
        return new BigInteger(b);
    }

    /**
     * 根据字节数组获得值(十六进制数字)
     *
     * @param bytes
     * @return
     */
    public static String getHexString(byte[] bytes) {
        return getHexString(bytes, true);
    }

    /**
     * 根据字节数组获得值(十六进制数字)
     *
     * @param bytes
     * @param upperCase
     * @return
     */
    public static String getHexString(byte[] bytes, boolean upperCase) {
        String ret = "";
        for (int i = 0; i < bytes.length; i++) {
            ret += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return upperCase ? ret.toUpperCase() : ret;
    }

    /**
     * 打印十六进制字符串
     *
     * @param bytes
     */
    public static void printHexString(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print("0x" + hex.toUpperCase() + ",");
        }
        System.out.println();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }

        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 用于建立十六进制字符的输出的大写字符数组
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data byte[]
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制char[]
     */
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data byte[]
     * @return 十六进制String
     */
    public static String encodeHexString(byte[] data) {
        return encodeHexString(data, true);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
     * @return 十六进制String
     */
    public static String encodeHexString(byte[] data, boolean toLowerCase) {
        return encodeHexString(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制String
     */
    protected static String encodeHexString(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }

    /**
     * 将十六进制字符数组转换为字节数组
     *
     * @param data 十六进制char[]
     * @return byte[]
     * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    public static byte[] decodeHex(char[] data) {
        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * 将十六进制字符转换成一个整数
     *
     * @param ch    十六进制char
     * @param index 十六进制字符在字符数组中的位置
     * @return 一个整数
     * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch
                    + " at index " + index);
        }
        return digit;
    }

    /**
     * 数字字符串转ASCII码字符串
     *
     * @param content 字符串
     * @return ASCII字符串
     */
    public static String StringToAsciiString(String content) {
        String result = "";
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            String b = Integer.toHexString(c);
            result = result + b;
        }
        return result;
    }

    /**
     * 十六进制转字符串
     *
     * @param hexString  十六进制字符串
     * @param encodeType 编码类型4：Unicode，2：普通编码
     * @return 字符串
     */
    public static String hexStringToString(String hexString, int encodeType) {
        String result = "";
        int max = hexString.length() / encodeType;
        for (int i = 0; i < max; i++) {
            char c = (char) hexStringToAlgorism(hexString
                    .substring(i * encodeType, (i + 1) * encodeType));
            result += c;
        }
        return result;
    }

    /**
     * 十六进制字符串装十进制
     *
     * @param hex 十六进制字符串
     * @return 十进制数值
     */
    public static int hexStringToAlgorism(String hex) {
        hex = hex.toUpperCase();
        int max = hex.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = hex.charAt(i - 1);
            int algorism = 0;
            if (c >= '0' && c <= '9') {
                algorism = c - '0';
            } else {
                algorism = c - 55;
            }
            result += Math.pow(16, max - i) * algorism;
        }
        return result;
    }

    /**
     * 十六转二进制
     *
     * @param hex 十六进制字符串
     * @return 二进制字符串
     */
    public static String hexStringToBinary(String hex) {
        hex = hex.toUpperCase();
        String result = "";
        int max = hex.length();
        for (int i = 0; i < max; i++) {
            char c = hex.charAt(i);
            switch (c) {
                case '0':
                    result += "0000";
                    break;
                case '1':
                    result += "0001";
                    break;
                case '2':
                    result += "0010";
                    break;
                case '3':
                    result += "0011";
                    break;
                case '4':
                    result += "0100";
                    break;
                case '5':
                    result += "0101";
                    break;
                case '6':
                    result += "0110";
                    break;
                case '7':
                    result += "0111";
                    break;
                case '8':
                    result += "1000";
                    break;
                case '9':
                    result += "1001";
                    break;
                case 'A':
                    result += "1010";
                    break;
                case 'B':
                    result += "1011";
                    break;
                case 'C':
                    result += "1100";
                    break;
                case 'D':
                    result += "1101";
                    break;
                case 'E':
                    result += "1110";
                    break;
                case 'F':
                    result += "1111";
                    break;
            }
        }
        return result;
    }

    /**
     * ASCII码字符串转数字字符串
     *
     * @param content ASCII字符串
     * @return 字符串
     */
    public static String AsciiStringToString(String content) {
        String result = "";
        int length = content.length() / 2;
        for (int i = 0; i < length; i++) {
            String c = content.substring(i * 2, i * 2 + 2);
            int a = hexStringToAlgorism(c);
            char b = (char) a;
            String d = String.valueOf(b);
            result += d;
        }
        return result;
    }

    /**
     * 将十进制转换为指定长度的十六进制字符串
     *
     * @param algorism  int 十进制数字
     * @param maxLength int 转换后的十六进制字符串长度
     * @return String 转换后的十六进制字符串
     */
    public static String algorismToHexString(int algorism, int maxLength) {
        String result = "";
        result = Integer.toHexString(algorism);

        if (result.length() % 2 == 1) {
            result = "0" + result;
        }
        return patchHexString(result.toUpperCase(), maxLength);
    }

    /**
     * 字节数组转为普通字符串（ASCII对应的字符）
     *
     * @param bytearray byte[]
     * @return String
     */
    public static String byteToString(byte[] bytearray) {
        String result = "";
        char temp;

        int length = bytearray.length;
        for (int i = 0; i < length; i++) {
            temp = (char) bytearray[i];
            result += temp;
        }
        return result;
    }

    /**
     * 二进制字符串转十进制
     *
     * @param binary 二进制字符串
     * @return 十进制数值
     */
    public static int binaryToAlgorism(String binary) {
        int max = binary.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = binary.charAt(i - 1);
            int algorism = c - '0';
            result += Math.pow(2, max - i) * algorism;
        }
        return result;
    }

    /**
     * 十进制转换为十六进制字符串
     *
     * @param algorism int 十进制的数字
     * @return String 对应的十六进制字符串
     */
    public static String algorismToHEXString(int algorism) {
        String result = "";
        result = Integer.toHexString(algorism);

        if (result.length() % 2 == 1) {
            result = "0" + result;

        }
        result = result.toUpperCase();

        return result;
    }

    /**
     * HEX字符串前补0，主要用于长度位数不足。
     *
     * @param str       String 需要补充长度的十六进制字符串
     * @param maxLength int 补充后十六进制字符串的长度
     * @return 补充结果
     */
    static public String patchHexString(String str, int maxLength) {
        String temp = "";
        for (int i = 0; i < maxLength - str.length(); i++) {
            temp = "0" + temp;
        }
        str = (temp + str).substring(0, maxLength);
        return str;
    }

    /**
     * 将一个字符串转换为int
     *
     * @param s          String 要转换的字符串
     * @param defaultInt int 如果出现异常,默认返回的数字
     * @param radix      int 要转换的字符串是什么进制的,如16 8 10.
     * @return int 转换后的数字
     */
    public static int parseToInt(String s, int defaultInt, int radix) {
        int i = 0;
        try {
            i = Integer.parseInt(s, radix);
        } catch (NumberFormatException ex) {
            i = defaultInt;
        }
        return i;
    }

    /**
     * 将一个十进制形式的数字字符串转换为int
     *
     * @param s          String 要转换的字符串
     * @param defaultInt int 如果出现异常,默认返回的数字
     * @return int 转换后的数字
     */
    public static int parseToInt(String s, int defaultInt) {
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            i = defaultInt;
        }
        return i;
    }

    /**
     * 十六进制串转化为byte数组
     *
     * @return the array of byte
     */
    public static byte[] hexToByte(String hex)
            throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }

    /**
     * 字节数组转换为十六进制字符串
     *
     * @param b byte[] 需要转换的字节数组
     * @return String 十六进制字符串
     */
    public static String byteToHex(byte[] b) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toLowerCase();
        //return hs.toUpperCase();
    }

    public static byte[] subByte(byte[] input, int startIndex, int length) {
        byte[] bt = new byte[length];
        for (int i = 0; i < length; i++) {
            bt[i] = input[i + startIndex];
        }
        return bt;
    }
    public static void saveSm2Key(byte[] pubkey,byte[] prikey,Object entity,int type,int subtype,String filepath){
        Gm_sm2_3 gm=Gm_sm2_3.getInstance();
        switch (type){
            case 0:{//owner的秘钥系列
                byte []sm3ret=new byte[32];
                owner_date owner=(owner_date)entity;
                String info=owner.getname()+owner.getUuid()+owner.getInfo();
                String _keyname=gm.sm3(info.getBytes(),info.getBytes().length,sm3ret);
                switch (subtype){
                    case 0:{//根密钥
                        String keyname=_keyname.substring(0,32);
                        keyname="/ow/"+keyname+"_PIK_pub";
                        File pubkey_f=new File(filepath,keyname);
                        File pub_dir=pubkey_f.getParentFile();
                        if(pub_dir!=null&& !pub_dir.exists())
                            pub_dir.mkdirs();
                        if(!pubkey_f.exists()){
                            try {
                                pubkey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("PIK_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pub;
                        try{
                            fileOutputStream_pub=new FileOutputStream(pubkey_f);
                            fileOutputStream_pub.write(pubkey);
                            fileOutputStream_pub.flush();
                            fileOutputStream_pub.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        keyname=_keyname.substring(0,32);
                        keyname="/ow/"+keyname+"_PIK_pri";
                        File prikey_f=new File(filepath,keyname);
                        File pri_dir=prikey_f.getParentFile();
                        if(pri_dir!=null&& !pri_dir.exists())
                            pri_dir.mkdirs();
                        if(!prikey_f.exists()){
                            try {
                                prikey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("PIK_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pri;
                        try{
                            fileOutputStream_pri=new FileOutputStream(prikey_f);
                            fileOutputStream_pri.write(prikey);
                            fileOutputStream_pri.flush();
                            fileOutputStream_pri.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 1:{//签名密钥
                        String keyname=_keyname.substring(0,32);
                        keyname="/ow/"+keyname+"_sign_pub";
                        File pubkey_f=new File(filepath,keyname);
                        File pub_dir=pubkey_f.getParentFile();
                        if(pub_dir!=null&& !pub_dir.exists())
                            pub_dir.mkdirs();
                        if(!pubkey_f.exists()){
                            try {
                                pubkey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("sign_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pub;
                        try{
                            fileOutputStream_pub=new FileOutputStream(pubkey_f);
                            fileOutputStream_pub.write(pubkey);
                            fileOutputStream_pub.flush();
                            fileOutputStream_pub.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        keyname=_keyname.substring(0,32);
                        keyname="/ow/"+keyname+"_sign_pri";
                        File prikey_f=new File(filepath,keyname);
                        File pri_dir=prikey_f.getParentFile();
                        if(pri_dir!=null&& !pri_dir.exists())
                            pri_dir.mkdirs();
                        if(!prikey_f.exists()){
                            try {
                                prikey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("sign_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pri;
                        try{
                            fileOutputStream_pri=new FileOutputStream(prikey_f);
                            fileOutputStream_pri.write(prikey);
                            fileOutputStream_pri.flush();
                            fileOutputStream_pri.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 2:{//绑定密钥
                        String keyname=_keyname.substring(0,32);
                        keyname="/ow/"+keyname+"_bind_pub";
                        File pubkey_f=new File(filepath,keyname);
                        File pub_dir=pubkey_f.getParentFile();
                        if(pub_dir!=null&& !pub_dir.exists())
                            pub_dir.mkdirs();
                        if(!pubkey_f.exists()){
                            try {
                                pubkey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("bind_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pub;
                        try{
                            fileOutputStream_pub=new FileOutputStream(pubkey_f);
                            fileOutputStream_pub.write(pubkey);
                            fileOutputStream_pub.flush();
                            fileOutputStream_pub.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        keyname=_keyname.substring(0,32);
                        keyname="/ow/"+keyname+"_bind_pri";
                        File prikey_f=new File(filepath,keyname);
                        File pri_dir=prikey_f.getParentFile();
                        if(pri_dir!=null&& !pri_dir.exists())
                            pri_dir.mkdirs();
                        if(!prikey_f.exists()){
                            try {
                                prikey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("bind_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pri;
                        try{
                            fileOutputStream_pri=new FileOutputStream(prikey_f);
                            fileOutputStream_pri.write(prikey);
                            fileOutputStream_pri.flush();
                            fileOutputStream_pri.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                break;
            }
            case 1:{//loc的密钥集合
                byte[]sm3ret=new byte[32];
                Loc loc=(Loc)entity;
                String info=loc.getHub_uuid()+ loc.getDesc();
                String _keyname=gm.sm3(info.getBytes(),info.getBytes().length,sm3ret);
                switch (subtype){
                    case 0:{//根密钥
                        String keyname=_keyname.substring(0,32);
                        keyname="/loc/"+loc.getHub_uuid()+"/"+keyname+"_PIK_pub";
                        File pubkey_f=new File(filepath,keyname);
                        File pub_dir=pubkey_f.getParentFile();
                        if(pub_dir!=null&& !pub_dir.exists())
                            pub_dir.mkdirs();
                        if(!pubkey_f.exists()){
                            try {
                                pubkey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("PIK_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pub;
                        try{
                            fileOutputStream_pub=new FileOutputStream(pubkey_f);
                            fileOutputStream_pub.write(pubkey);
                            fileOutputStream_pub.flush();
                            fileOutputStream_pub.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        keyname=_keyname.substring(0,32);
                        keyname="/loc/"+loc.getHub_uuid()+"/"+keyname+"_PIK_pri";
                        File prikey_f=new File(filepath,keyname);
                        File pri_dir=prikey_f.getParentFile();
                        if(pri_dir!=null&& !pri_dir.exists())
                            pri_dir.mkdirs();
                        if(!prikey_f.exists()){
                            try {
                                prikey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("PIK_prikey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pri;
                        try{
                            fileOutputStream_pri=new FileOutputStream(prikey_f);
                            fileOutputStream_pri.write(prikey);
                            fileOutputStream_pri.flush();
                            fileOutputStream_pri.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 1:{//签名密钥
                        String keyname=_keyname.substring(0,32);
                        keyname="/loc/"+loc.getHub_uuid()+"/"+keyname+"_sign_pub";
                        File pubkey_f=new File(filepath,keyname);
                        File pub_dir=pubkey_f.getParentFile();
                        if(pub_dir!=null&& !pub_dir.exists())
                            pub_dir.mkdirs();
                        if(!pubkey_f.exists()){
                            try {
                                pubkey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("sign_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pub;
                        try{
                            fileOutputStream_pub=new FileOutputStream(pubkey_f);
                            fileOutputStream_pub.write(pubkey);
                            fileOutputStream_pub.flush();
                            fileOutputStream_pub.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        keyname=_keyname.substring(0,32);
                        keyname="/loc/"+loc.getHub_uuid()+"/"+keyname+"_sign_pri";
                        File prikey_f=new File(filepath,keyname);
                        File pri_dir=prikey_f.getParentFile();
                        if(pri_dir!=null&& !pri_dir.exists())
                            pri_dir.mkdirs();
                        if(!prikey_f.exists()){
                            try {
                                prikey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("sign_prikey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pri;
                        try{
                            fileOutputStream_pri=new FileOutputStream(prikey_f);
                            fileOutputStream_pri.write(prikey);
                            fileOutputStream_pri.flush();
                            fileOutputStream_pri.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 2:{//绑定密钥
                        String keyname=_keyname.substring(0,32);
                        keyname="/loc/"+loc.getHub_uuid()+"/"+keyname+"_bind_pub";
                        File pubkey_f=new File(filepath,keyname);
                        File pub_dir=pubkey_f.getParentFile();
                        if(pub_dir!=null&& !pub_dir.exists())
                            pub_dir.mkdirs();
                        if(!pubkey_f.exists()){
                            try {
                                pubkey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("bind_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pub;
                        try{
                            fileOutputStream_pub=new FileOutputStream(pubkey_f);
                            fileOutputStream_pub.write(pubkey);
                            fileOutputStream_pub.flush();
                            fileOutputStream_pub.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        keyname=_keyname.substring(0,32);
                        keyname="/loc/"+loc.getHub_uuid()+"/"+keyname+"_bind_pri";
                        File prikey_f=new File(filepath,keyname);
                        File pri_dir=prikey_f.getParentFile();
                        if(pri_dir!=null&& !pri_dir.exists())
                            pri_dir.mkdirs();
                        if(!prikey_f.exists()){
                            try {
                                prikey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("bind_prikey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pri;
                        try{
                            fileOutputStream_pri=new FileOutputStream(prikey_f);
                            fileOutputStream_pri.write(prikey);
                            fileOutputStream_pri.flush();
                            fileOutputStream_pri.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                break;
            }
            case 2:{//guest的密钥集合
                byte[]sm3ret=new byte[32];
                Guest guest=(Guest)entity;
                String info= guest.getId() + guest.getUuid() + guest.getInfo();
                String _keyname=gm.sm3(info.getBytes(),info.getBytes().length,sm3ret);
                switch (subtype){
                    case 0:{//根密钥
                        String keyname=_keyname.substring(0,32);
                        keyname="/Guest/"+guest.getUuid()+"/"+keyname+"_PIK_pub";
                        File pubkey_f=new File(filepath,keyname);
                        File pub_dir=pubkey_f.getParentFile();
                        if(pub_dir!=null&& !pub_dir.exists())
                            pub_dir.mkdirs();
                        if(!pubkey_f.exists()){
                            try {
                                pubkey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("PIK_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pub;
                        try{
                            fileOutputStream_pub=new FileOutputStream(pubkey_f);
                            fileOutputStream_pub.write(pubkey);
                            fileOutputStream_pub.flush();
                            fileOutputStream_pub.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        keyname=_keyname.substring(0,32);
                        keyname="/Guest/"+guest.getUuid()+"/"+keyname+"_PIK_pri";
                        File prikey_f=new File(filepath,keyname);
                        File pri_dir=prikey_f.getParentFile();
                        if(pri_dir!=null&& !pri_dir.exists())
                            pri_dir.mkdirs();
                        if(!prikey_f.exists()){
                            try {
                                prikey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("PIK_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pri;
                        try{
                            fileOutputStream_pri=new FileOutputStream(prikey_f);
                            fileOutputStream_pri.write(prikey);
                            fileOutputStream_pri.flush();
                            fileOutputStream_pri.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 1:{//签名密钥
                        String keyname=_keyname.substring(0,32);
                        keyname="/Guest/"+guest.getUuid()+"/"+keyname+"_sign_pub";
                        File pubkey_f=new File(filepath,keyname);
                        File pub_dir=pubkey_f.getParentFile();
                        if(pub_dir!=null&& !pub_dir.exists())
                            pub_dir.mkdirs();
                        if(!pubkey_f.exists()){
                            try {
                                pubkey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("sign_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pub;
                        try{
                            fileOutputStream_pub=new FileOutputStream(pubkey_f);
                            fileOutputStream_pub.write(pubkey);
                            fileOutputStream_pub.flush();
                            fileOutputStream_pub.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        keyname=_keyname.substring(0,32);
                        keyname="/Guest/"+guest.getUuid()+"/"+keyname+"_sign_pri";
                        File prikey_f=new File(filepath,keyname);
                        File pri_dir=prikey_f.getParentFile();
                        if(pri_dir!=null&& !pri_dir.exists())
                            pri_dir.mkdirs();
                        if(!prikey_f.exists()){
                            try {
                                prikey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("sign_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pri;
                        try{
                            fileOutputStream_pri=new FileOutputStream(prikey_f);
                            fileOutputStream_pri.write(prikey);
                            fileOutputStream_pri.flush();
                            fileOutputStream_pri.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 2:{//绑定密钥
                        String keyname=_keyname.substring(0,32);
                        keyname="/Guest/"+guest.getUuid()+"/"+keyname+"_bind_pub";
                        File pubkey_f=new File(filepath,keyname);
                        File pub_dir=pubkey_f.getParentFile();
                        if(pub_dir!=null&& !pub_dir.exists())
                            pub_dir.mkdirs();
                        if(!pubkey_f.exists()){
                            try {
                                pubkey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("bind_pubkey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pub;
                        try{
                            fileOutputStream_pub=new FileOutputStream(pubkey_f);
                            fileOutputStream_pub.write(pubkey);
                            fileOutputStream_pub.flush();
                            fileOutputStream_pub.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        keyname=_keyname.substring(0,32);
                        keyname="/Guest/"+guest.getUuid()+"/"+keyname+"_bind_pri";
                        File prikey_f=new File(filepath,keyname);
                        File pri_dir=prikey_f.getParentFile();
                        if(pri_dir!=null&& !pri_dir.exists())
                            pri_dir.mkdirs();
                        if(!prikey_f.exists()){
                            try {
                                prikey_f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("bind_prikey save error",e.toString());
                            }
                        }
                        FileOutputStream fileOutputStream_pri;
                        try{
                            fileOutputStream_pri=new FileOutputStream(prikey_f);
                            fileOutputStream_pri.write(prikey);
                            fileOutputStream_pri.flush();
                            fileOutputStream_pri.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                break;
            }
        }

    }
    public static void loadSm2Key(byte[] pubkey,byte[] prikey,Object entity,int type,int subtype,String filepath) throws IOException {
        Gm_sm2_3 gm=Gm_sm2_3.getInstance();
        switch (type){
            case 0:{//owner的秘钥系列
                byte []sm3ret=new byte[32];
                owner_date owner=(owner_date)entity;
                String info=owner.getname()+owner.getUuid()+owner.getInfo();
                String _keyname=gm.sm3(info.getBytes(),info.getBytes().length,sm3ret);
                switch (subtype){
                    case 0:{//根密钥
                        String keyname=_keyname.substring(0,32);
                        keyname="/ow/"+keyname+"_PIK_pub";
                        File pubkey_f=new File(filepath+keyname);
                        FileInputStream fileInputStream=new FileInputStream(pubkey_f);
                        fileInputStream.read(pubkey);
                        fileInputStream.close();
                        keyname=_keyname.substring(0,32);
                        keyname="/ow/"+keyname+"_PIK_pri";
                        File prikey_f=new File(filepath+keyname);
                        FileInputStream _fileInputStream=new FileInputStream(prikey_f);
                        _fileInputStream.read(prikey);
                        _fileInputStream.close();
                        break;
                    }
                    case 1:{//签名密钥
                        String keyname=_keyname.substring(0,32);
                        keyname="/ow/"+keyname+"_sign_pub";
                        File pubkey_f=new File(filepath+keyname);
                        FileInputStream fileInputStream=new FileInputStream(pubkey_f);
                        fileInputStream.read(pubkey);
                        fileInputStream.close();
                        keyname=_keyname.substring(0,32);
                        keyname="/ow/"+keyname+"_sign_pri";
                        File prikey_f=new File(filepath+keyname);
                        FileInputStream _fileInputStream=new FileInputStream(prikey_f);
                        _fileInputStream.read(prikey);
                        _fileInputStream.close();
                        break;
                    }
                    case 2:{
                        String keyname=_keyname.substring(0,32);
                        keyname="/ow/"+keyname+"_bind_pub";
                        File pubkey_f=new File(filepath+keyname);
                        FileInputStream fileInputStream=new FileInputStream(pubkey_f);
                        fileInputStream.read(pubkey);
                        fileInputStream.close();
                        keyname=_keyname.substring(0,32);
                        keyname="/ow/"+keyname+"_bind_pri";
                        File prikey_f=new File(filepath+keyname);
                        FileInputStream _fileInputStream=new FileInputStream(prikey_f);
                        _fileInputStream.read(prikey);
                        _fileInputStream.close();
                        break;
                    }
                }
                break;
            }
            case 1:{
                byte[]sm3ret=new byte[32];
                Loc loc=(Loc)entity;
                String info=loc.getHub_uuid()+ loc.getDesc();
                String _keyname=gm.sm3(info.getBytes(),info.getBytes().length,sm3ret);
                switch (subtype){
                    case 0:{
                        String keyname=_keyname.substring(0,32);
                        keyname="/loc/"+loc.getHub_uuid()+"/"+keyname+"_PIK_pub";
                        File pubkey_f=new File(filepath+keyname);
                        FileInputStream fileInputStream=new FileInputStream(pubkey_f);
                        fileInputStream.read(pubkey);
                        fileInputStream.close();
                        keyname=_keyname.substring(0,32);
                        keyname="/loc/"+loc.getHub_uuid()+keyname+"_PIK_pri";
                        File prikey_f=new File(filepath+keyname);
                        FileInputStream _fileInputStream=new FileInputStream(prikey_f);
                        _fileInputStream.read(prikey);
                        _fileInputStream.close();
                        break;
                    }
                    case 1:{
                        String keyname=_keyname.substring(0,32);
                        keyname="/loc/"+loc.getHub_uuid()+keyname+"_sign_pub";
                        File pubkey_f=new File(filepath+keyname);
                        FileInputStream fileInputStream=new FileInputStream(pubkey_f);
                        fileInputStream.read(pubkey);
                        fileInputStream.close();
                        keyname=_keyname.substring(0,32);
                        keyname="/loc/"+loc.getHub_uuid()+keyname+"_sign_pri";
                        File prikey_f=new File(filepath+keyname);
                        FileInputStream _fileInputStream=new FileInputStream(prikey_f);
                        _fileInputStream.read(prikey);
                        _fileInputStream.close();
                        break;
                    }
                    case 2:{
                        String keyname=_keyname.substring(0,32);
                        keyname="/loc/"+loc.getHub_uuid()+keyname+"_bind_pub";
                        File pubkey_f=new File(filepath+keyname);
                        FileInputStream fileInputStream=new FileInputStream(pubkey_f);
                        fileInputStream.read(pubkey);
                        fileInputStream.close();
                        keyname=_keyname.substring(0,32);
                        keyname="/loc/"+loc.getHub_uuid()+keyname+"_bind_pri";
                        File prikey_f=new File(filepath+keyname);
                        FileInputStream _fileInputStream=new FileInputStream(prikey_f);
                        _fileInputStream.read(prikey);
                        _fileInputStream.close();
                        break;
                    }
                }
                break;
            }
            case 2:{
                byte[]sm3ret=new byte[32];
                Guest guest=(Guest)entity;
                String info= guest.getId() + guest.getUuid() + guest.getInfo();
                String _keyname=gm.sm3(info.getBytes(),info.getBytes().length,sm3ret);
                switch (subtype){
                    case 0:{
                        String keyname=_keyname.substring(0,32);
                        keyname="/Guest/"+guest.getUuid()+"/"+keyname+"_PIK_pub";
                        File pubkey_f=new File(filepath+keyname);
                        FileInputStream fileInputStream=new FileInputStream(pubkey_f);
                        fileInputStream.read(pubkey);
                        fileInputStream.close();
                        keyname=_keyname.substring(0,32);
                        keyname="/Guest/"+guest.getUuid()+"/"+keyname+"_PIK_pri";
                        File prikey_f=new File(filepath+keyname);
                        FileInputStream _fileInputStream=new FileInputStream(prikey_f);
                        _fileInputStream.read(prikey);
                        _fileInputStream.close();
                        break;
                    }
                    case 1:{
                        String keyname=_keyname.substring(0,32);
                        keyname="/Guest/"+guest.getUuid()+"/"+keyname+"_sign_pub";
                        File pubkey_f=new File(filepath+keyname);
                        FileInputStream fileInputStream=new FileInputStream(pubkey_f);
                        fileInputStream.read(pubkey);
                        fileInputStream.close();
                        keyname=_keyname.substring(0,32);
                        keyname="/Guest/"+guest.getUuid()+"/"+keyname+"_sign_pri";
                        File prikey_f=new File(filepath+keyname);
                        FileInputStream _fileInputStream=new FileInputStream(prikey_f);
                        _fileInputStream.read(prikey);
                        _fileInputStream.close();
                        break;
                    }
                    case 2:{
                        String keyname=_keyname.substring(0,32);
                        keyname="/Guest/"+guest.getUuid()+"/"+keyname+"_bind_pub";
                        File pubkey_f=new File(filepath+keyname);
                        FileInputStream fileInputStream=new FileInputStream(pubkey_f);
                        fileInputStream.read(pubkey);
                        fileInputStream.close();
                        keyname=_keyname.substring(0,32);
                        keyname="/Guest/"+guest.getUuid()+"/"+keyname+"_bind_pri";
                        File prikey_f=new File(filepath+keyname);
                        FileInputStream _fileInputStream=new FileInputStream(prikey_f);
                        _fileInputStream.read(prikey);
                        _fileInputStream.close();
                        break;
                    }
                }
                break;
            }
        }
    }
    public static void Gen_LocalKey(LocalKey sign,LocalKey bind,String info,byte[]pik_pri,String id){
        if(sign==null||bind==null)
            throw new NullPointerException();
        Gm_sm2_3 gm=Gm_sm2_3.getInstance();
        //sign=new LocalKey();
        //bind=new LocalKey();
        sign.setInfo(info);
        bind.setInfo(info);
        sign.setType(Defin_crypto.SIGN);
        bind.setType(Defin_crypto.BIND);
        byte[]sign_pub=new byte[64];
        byte[]bind_pub=new byte[64];
        byte[]sign_pri=new byte[32];
        byte[]bind_pri=new byte[32];
        gm.GM_GenSM2keypair(sign_pub,sign_pri);
        gm.GM_GenSM2keypair(bind_pub,bind_pri);
        sign.setPubkey(sign_pub);
        sign.setPrikey(sign_pri);
        bind.setPubkey(bind_pub);
        bind.setPrikey(bind_pri);

        byte[]byte_info= info.getBytes();

        byte[]buf_sign=new byte[byte_info.length+sign_pri.length+sign_pub.length];
        for(int i = 0; i<byte_info.length+1+sign_pub.length; i++){
            if(i<sign_pub.length)
                buf_sign[i]=sign_pub[i];
            else if(i==sign_pub.length)
                buf_sign[i]=Integer.valueOf(sign.getType()).byteValue();
            else buf_sign[i]=byte_info[i-sign_pub.length-1];
        }
        byte[] sign_sign=new byte[64];
        gm.GM_SM2Sign(sign_sign,buf_sign,buf_sign.length,id.toCharArray(),id.toCharArray().length,pik_pri);
        sign.setSigndata(sign_sign);

        byte[]buf_bind=new byte[byte_info.length+ bind_pri.length+ bind_pub.length];
        for(int i = 0; i<byte_info.length+1+ bind_pub.length; i++){
            if(i< bind_pub.length)
                buf_bind[i]= bind_pub[i];
            else if(i== bind_pub.length)
                buf_bind[i]=Integer.valueOf( bind.getType()).byteValue();
            else buf_bind[i]=byte_info[i- bind_pub.length-1];
        }
        byte[] bind_sign=new byte[64];
        gm.GM_SM2Sign( bind_sign,buf_bind,buf_bind.length,id.toCharArray(),id.toCharArray().length,pik_pri);
        bind.setSigndata(bind_sign);

        byte[]buf_sign_cert=new byte[byte_info.length+ bind_pri.length+ bind_pub.length+sign_sign.length];
        for(int i = 0; i<byte_info.length+1+ bind_pub.length; i++){
            if(i< bind_pub.length)
                buf_sign_cert[i]= bind_pub[i];
            else if(i== bind_pub.length)
                buf_sign_cert[i]=Integer.valueOf( bind.getType()).byteValue();
            else if(i<bind_pub.length+1+byte_info.length)
                buf_sign_cert[i]=byte_info[i- bind_pub.length-1];
            else buf_sign_cert[i]=sign_sign[i-1-byte_info.length-bind_pub.length];
        }
        byte[] sign_sign_cert=new byte[64];
        gm.GM_SM2Sign( sign_sign_cert,buf_sign_cert,buf_sign_cert.length,id.toCharArray(),id.toCharArray().length,pik_pri);
        bind.setCertdata(sign_sign_cert);

        byte[]buf_bind_cert=new byte[byte_info.length+ bind_pri.length+ bind_pub.length+bind_sign.length];
        for(int i = 0; i<byte_info.length+1+ bind_pub.length; i++){
            if(i< bind_pub.length)
                buf_bind_cert[i]= bind_pub[i];
            else if(i== bind_pub.length)
                buf_bind_cert[i]=Integer.valueOf( bind.getType()).byteValue();
            else if(i<bind_pub.length+1+byte_info.length)
                buf_bind_cert[i]=byte_info[i- bind_pub.length-1];
            else buf_bind_cert[i]=bind_sign[i-1-byte_info.length-bind_pub.length];
        }
        byte[] bind_sign_cert=new byte[64];
        gm.GM_SM2Sign( bind_sign_cert,buf_bind_cert,buf_bind_cert.length,id.toCharArray(),id.toCharArray().length,pik_pri);
        bind.setCertdata(sign_sign_cert);
    }
    public static byte[] sm4inc(byte[] src,String sm4k,String sm4iv){
        SM4Utils sm4=new SM4Utils();
        sm4.iv = sm4iv;
        sm4.secretKey=sm4k;
        sm4.hexString=true;
        return sm4.encryptData_CBC(src);
    }
    public static byte[] sm4dec(byte[] src,String sm4k,String sm4iv){
        SM4Utils sm4=new SM4Utils();
        sm4.iv = sm4iv;
        sm4.secretKey=sm4k;
        sm4.hexString=true;
        return sm4.decryptData_CBC(src);
    }
    public static String sm2inc(String src_str,byte[] pub){
        Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();
        byte[] src= src_str.getBytes();
        int len=gm_sm2_3.GetEncLen(src,src.length,pub);
        byte[]src_inc=new byte[len];
        int ret= gm_sm2_3.GM_SM2Encrypt(src_inc,len,src,src.length,pub);
        if(ret==0)
            Log.d("encrpty","success");
        return Util.byteToHex(src_inc);
    }
    public static String sm2dec(String src_str,byte[] pri){
        Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();
        byte[] src=Util.hexStringToBytes(src_str);
        int len=gm_sm2_3.GetDecLen(src,src.length,pri);
        byte[]src_dec=new byte[len];
        int ret= gm_sm2_3.GM_SM2Decrypt(src_dec,len,src,src.length,pri);
        if(ret==0)
            Log.d("decrpty","success");
        return new String(src_dec);
    }
}
