package org.rogo.spinel.ncp;

import org.rogo.spinel.helpers.ByteUtils;
import org.rogo.spinel.helpers.Utilities;

import java.nio.charset.StandardCharsets;

public class SpinelEncoder {

    public SpinelEncoder() {

    }

    public static byte[] encodeValue(int propertyValue) {
        return SpinelEncoder.encodeValue(String.valueOf(propertyValue), PropertyFormat.B);
    }

    public static byte[] encodeValue(byte[] propertyValue, PropertyFormat format) {
        if (format != PropertyFormat.D) {
            throw new RuntimeException();
        }
        return SpinelEncoder.encodeData(propertyValue);
    }

    public static byte[] encodeValue(byte propertyValue, PropertyFormat format) {
        if (format != PropertyFormat.C) {
            throw new RuntimeException();
        }
        return SpinelEncoder.encodeInt8(propertyValue);
    }

    public static byte[] encodeValue(int propertyValue, PropertyFormat format) {
        if (format == PropertyFormat.S) {
            return SpinelEncoder.encodeInt16(propertyValue);
        } else if (format == PropertyFormat.i) {
            return SpinelEncoder.encodeUIntPacked(propertyValue);
        }
        throw new RuntimeException();
    }

    public static byte[] encodeValue(String propertyValue, PropertyFormat format) {
        if (format == PropertyFormat.U) {
            return SpinelEncoder.encodeUtf8(propertyValue);
        }
        return ByteUtils.intToBytes(Integer.parseInt(propertyValue));
    }

    private static byte[] encodeUtf8(String propertyValue) {
        byte[] bytes = propertyValue.getBytes(StandardCharsets.UTF_8);
        byte[] ret = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, ret, 0, bytes.length);
        ret[bytes.length] = 0x00;
        return ret;
    }


    private static byte[] encodeData(byte[] propertyValue) {
        return propertyValue;
    }

    public static byte[] encodeDataWithLength(byte[] propertyValue) {
        byte[] dataLen = ByteUtils.shortToBytes((short) propertyValue.length);
        return Utilities.combineArrays(dataLen, propertyValue);
    }

    private static byte[] encodeInt8(byte propertyValue) {
        byte[] byteArray = new byte[1];
        byteArray[0] = ByteUtils.intToBytes(propertyValue)[0];
        return byteArray;
    }

    private static byte[] encodeInt16(int propertyValue) {
        return ByteUtils.shortToBytes(propertyValue);
    }

    private static byte[] encodeUIntPacked(int valueToEncode) {
        int encodedSize = SpinelEncoder.packedUIntSize(valueToEncode);
        byte[] tempByte = new byte[encodedSize];
        int index;
        for (index = 0; index != encodedSize - 1; index++) {
            tempByte[index] = (byte) ((valueToEncode & 0x7F) | 0x80);
            valueToEncode = valueToEncode >> 7;
        }
        tempByte[index] = (byte) (valueToEncode & 0x7F);
        return tempByte;
    }


    private static int packedUIntSize(int value) {
        int ret = 0;
        if (value < (1 << 7)) {
            ret = 1;
        } else if (value < (1 << 14)) {
            ret = 2;
        } else if (value < (1 << 21)) {
            ret = 3;
        } else if (value < (1 << 28)) {
            ret = 4;
        } else {
            ret = 5;
        }
        return ret;
    }
}
