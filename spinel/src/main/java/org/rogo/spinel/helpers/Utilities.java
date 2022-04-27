package org.rogo.spinel.helpers;

public class Utilities {

    private Utilities() {

    }

    public static int getUid(long propertyId, byte tid) {
        return ((int) tid << 24) | (int) propertyId;
    }

    public static byte[] combineArrays(byte[] arr01, byte[] arr02) {
        final byte[] byteRet = new byte[arr01.length + arr02.length];
        System.arraycopy(arr01, 0, byteRet, 0, arr01.length);
        System.arraycopy(arr02, 0, byteRet, arr01.length, arr02.length);
        return byteRet;
    }

    public static byte[] combineArrays(byte[] arr01, byte[] arr02, byte[] arr03) {
        final byte[] byteRet = new byte[arr01.length + arr02.length + arr03.length];
        System.arraycopy(arr01, 0, byteRet, 0, arr01.length);
        System.arraycopy(arr02, 0, byteRet, arr01.length, arr02.length);
        System.arraycopy(arr03, 0, byteRet, arr01.length + arr02.length, arr03.length);
        return byteRet;
    }

    public static byte[] hexToBytes(String hexString) {
        return ByteUtils.hexToBytes(hexString);
    }

    public static boolean byteArrayCompare(byte[] arr1, byte[] arr2) {
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
