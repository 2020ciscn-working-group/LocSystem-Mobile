package com.example.myapplication.Servers;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class CardServer extends HostApduService {
    private static final String TAG = "CardService";
    // AID for our loyalty card service.
    private static final String SAMPLE_LOYALTY_CARD_AID = "F123466666";
    // ISO-DEP command HEADER for selecting an AID.
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
    private static final String SELECT_APDU_HEADER = "00A40400";
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
    private static final String GET_DATA_APDU_HEADER = "00CA0000";
    // "OK" status word sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_OK_SW = HexStringToByteArray("9000");
    // "UNKNOWN" status word sent in response to invalid APDU command (0x0000)
    private static final byte[] UNKNOWN_CMD_SW = HexStringToByteArray("0000");
    private static final byte[] SELECT_APDU = BuildSelectApdu(SAMPLE_LOYALTY_CARD_AID);
    private static final byte[] GET_DATA_APDU = BuildGetDataApdu();

    private static final String WRITE_DATA_APDU_HEADER = "00DA0000";
    private static final String READ_DATA_APDU_HEADER = "00EA0000";
    private static final byte[] WRITE_DATA_APDU = BuildWriteDataApdu();
    private static final byte[] READ_DATA_APDU = BuildReadDataApdu();
    private static String dataStr = null;

    File          sdcard = Environment.getExternalStorageDirectory();
    //File          file   = new File(sdcard,"file.txt");
    StringBuilder text   = new StringBuilder();
    int           pointer;
    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        Log.i(TAG, "Received APDU: " + ByteArrayToHexString(commandApdu));
        byte[] cmd = null;
        //length 6 is define by WRITE_DATA_APDU from read and emulatior
        //长度由reader端及卡端的命令WRITE_DATA_APDU来协商的
        if(commandApdu.length >= 6){
            cmd = Arrays.copyOf(commandApdu,6);
        }
        // If the APDU matches the SELECT AID command for this service,
        // send the loyalty card account number, followed by a SELECT_OK status trailer (0x9000).
        if (Arrays.equals(SELECT_APDU, commandApdu)) {
            String account = "some string random data";
            byte[] accountBytes = account.getBytes();
            Log.i(TAG, "Sending account number: " + account);
            //readFromFile();
            return ConcatArrays(accountBytes, SELECT_OK_SW);
        } else if ((Arrays.equals(GET_DATA_APDU, commandApdu))) {
            String stringToSend;
            try {
                stringToSend = text.toString().substring(pointer, pointer + 200);
            } catch (IndexOutOfBoundsException e) {
                Toast.makeText(this, "Reached the end of the file", Toast.LENGTH_SHORT).show();
                stringToSend = "END";
            }
            pointer += 200;byte[] accountBytes = stringToSend.getBytes();
            Log.i(TAG, "Sending substring, pointer : " + pointer + " , " + stringToSend);
            return ConcatArrays(accountBytes, SELECT_OK_SW);
        } else if (cmd != null && Arrays.equals(WRITE_DATA_APDU, cmd)){
            //length 6 is define by WRITE_DATA_APDU from read and emulatior
            //长度由reader端及卡端的命令WRITE_DATA_APDU来协商的
            byte[] data = Arrays.copyOfRange(commandApdu,6,commandApdu.length);
            try {
                dataStr = new String(data, "UTF-8");
                Log.i(TAG, "dataStr:" + dataStr);
            }catch (Exception e){
                e.printStackTrace();
            }
            String account = "write success";
            byte[] accountBytes = account.getBytes();
            Log.i(TAG, "Sending account number: " + account);
            return ConcatArrays(accountBytes, SELECT_OK_SW);
        } else if (Arrays.equals(READ_DATA_APDU, cmd)){
            if(dataStr!=null) {
                byte[] accountBytes = dataStr.getBytes();
                Log.i(TAG, "Sending account number: " + dataStr);
                return ConcatArrays(accountBytes, SELECT_OK_SW);
            }else {
                byte[] accountBytes = "data error".getBytes();
                Log.i(TAG, "Sending account number: " + dataStr);
                return ConcatArrays(accountBytes, SELECT_OK_SW);
            }
        } else {
            return UNKNOWN_CMD_SW;

        }
    }

    @Override
    public void onDeactivated(int reason) {

    }
    /**
     * Build APDU for SELECT AID command. This command indicates which service a reader is
     * interested in communicating with. See ISO 7816-4.
     *
     * @param aid Application ID (AID) to select
     * @return APDU for SELECT AID command
     */
    public static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(SELECT_APDU_HEADER + String.format("%02X",
                aid.length() / 2) + aid);
    }
    /**
     * Build APDU for GET_DATA command. See ISO 7816-4.
     *
     * @return APDU for SELECT AID command
     */
    public static byte[] BuildGetDataApdu() {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(GET_DATA_APDU_HEADER + "0FFF");
    }

    /**
     * Utility method to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
            hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
        }
        return new String(hexChars);
    }

    /**
     * Utility method to convert a hexadecimal string to a byte string.
     *
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     * @throws java.lang.IllegalArgumentException if input length is incorrect
     */
    public static byte[] HexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
        for (int i = 0; i < len; i += 2) {
            // Convert each character into a integer (base-16), then bit-shift into place
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Utility method to concatenate two byte arrays.
     * @param first First array
     * @param rest Any remaining arrays
     * @return Concatenated copy of input arrays
     */
    public static byte[] ConcatArrays(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
    public static byte[] BuildWriteDataApdu() {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(WRITE_DATA_APDU_HEADER + "0FFF");
    }
    public static byte[] BuildReadDataApdu() {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(READ_DATA_APDU_HEADER + "0FFF");
    }
}
