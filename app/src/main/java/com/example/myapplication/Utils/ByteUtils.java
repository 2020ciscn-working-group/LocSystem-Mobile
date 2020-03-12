package com.example.myapplication.Utils;

import android.app.Person;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class ByteUtils {
        /**
         * 对象转Byte数组
         * @param obj
         * @return
         */
        public static byte[] objectToByteArray(Object obj) {
            byte[] bytes = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(obj);
                objectOutputStream.flush();
                bytes = byteArrayOutputStream.toByteArray();

            } catch (IOException e) {
                Log.e("ByteUtils","objectToByteArray error");
            }
             finally {
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (IOException e) {
                        Log.e("ByteUtils","objectOutputStream.close error");
                    }
                }
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e) {
                       Log.e("ByteUtils","byteArrayOutputStream.close error");
                    }
                }

            }
            return bytes;
        }

        /**
         * Byte数组转对象
         * @param bytes
         * @return
         */
        public static Object byteArrayToObject(byte[] bytes) {
            Object obj = null;
            ByteArrayInputStream byteArrayInputStream = null;
            ObjectInputStream objectInputStream = null;
            try {
                byteArrayInputStream = new ByteArrayInputStream(bytes);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                obj = objectInputStream.readObject();
            } catch (Exception e) {
                Log.e("ByteUtils","byteArrayToObject error");
            } finally {
                if (byteArrayInputStream != null) {
                    try {
                        byteArrayInputStream.close();
                    } catch (IOException e) {
                        Log.e("ByteUtils","byteArrayInputStream.close error");
                    }
                }
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e) {
                        Log.e("ByteUtils","byteArrayOutputStream.close error");
                    }
                }
            }
            return obj;
        }
}


