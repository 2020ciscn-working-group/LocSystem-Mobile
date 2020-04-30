package com.example.myapplication.Utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class UsbHelper {
    private UsbManager   usbManager;
    private UsbDevice    usbDevice;
    private UsbInterface mUsbInterface;
    private UsbEndpoint  in,out,ctl,intin,intout;
    private UsbDeviceConnection conn;
    private Activity mActivity;

    private UsbHelper(){}
    public UsbHelper(Activity activity){
        mActivity=activity;
    }

    public void setUsbDevice(UsbDevice usbDevice) {
        this.usbDevice = usbDevice;
    }


    private UsbDevice findUSB(int VENDORID, int PRODUCTID) {
        //1)创建usbManager
        usbManager = (UsbManager) mActivity.getSystemService(Context.USB_SERVICE);
        //2)获取到所有设备 选择出满足的设备
        assert usbManager != null;
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        for (UsbDevice device : deviceList.values()) {
            //Log.e(TAG, "vendorID--" + device.getVendorId() + "ProductId--" + device.getProductId());
            if (device.getVendorId() == VENDORID && device.getProductId() == PRODUCTID) {
                return device; // 获取USBDevice
            }
        }
        //statue = USBContent.usb_find_this_fail;
        return null;
    }
    public List<UsbDevice> getUsbDevice() {
        //1)创建usbManager
        if (usbManager == null)
            usbManager = (UsbManager) mActivity.getSystemService(Context.USB_SERVICE);
        //2)获取到所有设备 选择出满足的设备
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        //创建返回数据
        List<UsbDevice> lists = new ArrayList<>();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            //Log.e(TAG, "vendorID--" + device.getVendorId() + "ProductId--" + device.getProductId());
            lists.add(device);
        }
        return lists;
    }

    public void connection(int vendorId, int productId) {
        usbDevice = (UsbDevice) findUSB(vendorId, productId);

        //3)查找设备接口
        if (usbDevice == null) {
            //Log.e(TAG, "未找到目标设备，请确保供应商ID" + vendorId + "和产品ID" + productId + "是否配置正确");
            return;
        }
        UsbInterface usbInterface = null;

            //一个设备上面一般只有一个接口，有两个端点，分别接受和发送数据
            usbInterface = usbDevice.getInterface(0);
            Log.e("USBHelper","usbInterface.getEndpointCount()="+usbInterface.getEndpointCount());
        //4)获取usb设备的通信通道endpoint
        for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
            UsbEndpoint ep = usbInterface.getEndpoint(i);
            switch (ep.getType()) {
                case UsbConstants.USB_ENDPOINT_XFER_BULK://USB端口传输
                    if (UsbConstants.USB_DIR_OUT == ep.getDirection()) {//输出
                        out = ep;
                        //Log.e(TAG, "获取发送数据的端点");
                    } else {
                        in = ep;
                        //Log.e(TAG, "获取接收数据的端点");
                    }
                    break;
                case UsbConstants.USB_ENDPOINT_XFER_CONTROL://控制端点
                    ctl = ep;
                    //Log.e(TAG, "find the ControlEndPoint:" + "index:" + i + "," + epControl.getEndpointNumber());
                    break;
                case UsbConstants.USB_ENDPOINT_XFER_INT://中断端点
                    if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {//输出
                        intout = ep;
                        //Log.e(TAG, "find the InterruptEndpointOut:" + "index:" + i + "," + epIntEndpointOut.getEndpointNumber());
                    }
                    if (ep.getDirection() == UsbConstants.USB_DIR_IN) {
                        intin = ep;
                        //Log.e(TAG, "find the InterruptEndpointIn:" + "index:" + i + "," + epIntEndpointIn.getEndpointNumber());
                    }
                    break;
                default:
                    break;
            }
        }
        //5)打开conn连接通道
        if (usbManager.hasPermission(usbDevice)) {
            //有权限，那么打开
            conn = usbManager.openDevice(usbDevice);
        }
        if (null == conn) {
            //Log.e(TAG, "不能连接到设备");
            //statue = USBContent.usb_open_fail;
            return;
        }
        //打开设备
        if (conn.claimInterface(usbInterface, true)) {
            if (conn != null){// 到此你的android设备已经连上设备
                Log.e("TAG", "open设备成功！");
            final String mySerial = conn.getSerial();
            //Log.e(TAG, "设备serial number：" + mySerial);
            //statue = USBContent.usb_ok;
                }
        } else {
            //Log.e(TAG, "无法打开连接通道。");
            //statue = USBContent.usb_passway_fail;
            conn.close();
        }
    }
    public void sendData(byte[] buffer) {
        if (conn == null || out == null) return;
        int res = conn.bulkTransfer(out, buffer, buffer.length, 1000);
        Log.e("usb", "res="+res);

        if (res  >= 0) {
            //0 或者正数表示成功
            Log.e("usb", "发送成功");
            //statue = USBContent.usb_permission_ok;
        } else {
            Log.e("usb", "发送失败的");
            //statue = USBContent.usb_permission_fail;
        }
    }
    public void close() {
        if (conn != null) { //关闭USB设备
            conn.close();
            conn = null;
        }
    }
    private byte[] receiveMessageFromPoint() {
        byte[] buffer = new byte[15];
        if (conn.bulkTransfer(in, buffer, buffer.length,
                2000) < 0)
            Log.d("usb","bulkIn返回输出为  负数");
        else {
            Log.d("usb","Receive Message Succese！"
                    // + "数据返回"
                    // + myDeviceConnection.bulkTransfer(epBulkIn, buffer,
                    // buffer.length, 3000)
            );
        }
        return buffer;
    }

    public String readFromUsb() {
        //读取数据2
        int inMax = in.getMaxPacketSize();
        byte[] retData=null;
        ByteBuffer byteBuffer = ByteBuffer.allocate(inMax);
        UsbRequest usbRequest = new UsbRequest();
        usbRequest.initialize(conn, in);
        usbRequest.queue(byteBuffer);
        while(true){
            if (conn.requestWait() == usbRequest) {
                retData = byteBuffer.array();
                break;
            }
        }
        return new String(retData);
    }


}
