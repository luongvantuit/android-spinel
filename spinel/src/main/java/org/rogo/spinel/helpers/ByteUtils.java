package org.rogo.spinel.helpers;


import java.util.Arrays;

@SuppressWarnings("SuspiciousSystemArraycopy")
public final class ByteUtils {

    public final static String TAG = ByteUtils.class.getSimpleName();


    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    public static byte[] intToBytes(int value) {
        final byte[] bytes=  new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value};
        int counter = 0;
        for (int i = 0; i < bytes.length -1 ; i++) {
            if (bytes[i] != 0) {
                counter++;
            }
        }
        byte[] bytesResponse = new byte[counter + 1];
        counter = 0;
        for (int i = 0; i < bytes.length -1 ; i++) {
            if (bytes[i] != 0) {
                bytesResponse[counter++] = bytes[i];
            }
        }
        bytesResponse[bytesResponse.length -1] = bytes[bytes.length -1];
        return bytesResponse;

    }

    public static byte[] shortToBytes(int value) {
        return new byte[]{getUShortB0(value), getUShortB1(value)};
    }

    public static byte[] shortToBytes(int[] values) {
        byte[] bytes = new byte[values.length * 2];
        int index = 0;
        for (int value : values) {
            bytes[index] = getUShortB0(value);
            index++;
            bytes[index] = getUShortB1(value);
            index++;
        }
        return bytes;
    }

    public static byte[] combineHexWithShort(String hexString, int shortData) {
        try {
            if (hexString.length() % 2 == 1) {
                throw new IllegalArgumentException(
                        "Invalid hexadecimal String supplied.");
            }

            byte[] bytes = new byte[(hexString.length() / 2) + 2];
            for (int i = 0; i < hexString.length(); i += 2) {
                bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
            }
            bytes[bytes.length - 2] = getUShortB0(shortData);
            bytes[bytes.length - 1] = getUShortB1(shortData);
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] combineShortToBytes(int n1, int n2) {
        return new byte[]{getUShortB0(n1), getUShortB1(n1), getUShortB0(n2), getUShortB1(n2)};
    }


    public static String getShortHex(int number) {
        return String.format("%02x%02x", getUShortB0(number), getUShortB1(number));
    }

    public static String getShortHex(int b1, int b2) {
        return String.format("%02x%02x", getUByte(b1), getUByte(b2));
    }

    public static String getIntHex(int number) {
        byte[] bytes = intToBytes(number);
        return String.format("%02x%02x%02x%02x", bytes[0], bytes[1], bytes[2], bytes[3]);
    }

    public static int combineShortToInt(int n1, int n2) {
        return getUShortB0(n1) << 24 | (getUShortB1(n1) & 0xFF) << 16 | (getUShortB0(n2) & 0xFF) << 8 | (getUShortB1(n2) & 0xFF);
    }

    public static int bytesToUInt(byte[] bytes) {
        if (bytes == null)
            return -1;
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }


    public static long bytesToULong(final byte[] b) {
        if (b == null)
            return -1;
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

    public static synchronized byte getUByte(int number) {
        byte b = (byte) number;
        return (byte) (b & 0xFF);
    }


    public synchronized static int getUByte2Int(byte b) {
        return b & 0xff;
    }

    public static synchronized byte getUShortB0(int number) {
        return (byte) ((number >> 8) & 0xFF);
    }

    public static synchronized byte getUShortB1(int number) {
        return (byte) (number & 0xFF);
    }

    public static synchronized byte[] getUShortBytes(int number) {
        return new byte[]{(byte) ((number >> 8) & 0xFF), (byte) (number & 0xFF)};
    }

    public static int getUShort(byte b0, byte b1) {
        return ((b0 & 0xFF) << 8) + (b1 & 0xFF);
    }


    public static int getUShort(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 8) + (bytes[1] & 0xFF);
    }


    public static String byteToHex(byte b) {
        return String.format("%02x", b);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    public static byte[] hexToBytes(String hexString) {
        try {
            if (hexString.length() % 2 == 1) {
                throw new IllegalArgumentException(
                        "Invalid hexadecimal String supplied.");
            }

            byte[] bytes = new byte[hexString.length() / 2];
            for (int i = 0; i < hexString.length(); i += 2) {
                bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
            }
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }


    public static byte hexToByte(char hexChar0, char hexChar1) {
        int firstDigit = toDigit(hexChar0);
        int secondDigit = toDigit(hexChar1);
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if (digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: " + hexChar);
        }
        return digit;
    }


    public static byte[] concatenateBytes(byte[] array1, byte[] array2) {
        byte[] joinedArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }


    public static int[] concatenateInt(int[] array1, int[] array2) {
        int[] joinedArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static int[] expandArrays(int[] array1, int size) {
        return Arrays.copyOf(array1, array1.length + size);
    }


    public static byte[] createInts(int sizeInt, int offset, int[] initInts, int... initData) {

        byte[] result = new byte[sizeInt];
        System.arraycopy(initInts, 0, result, offset, initInts.length);
        System.arraycopy(initData, 0, result, offset + initInts.length, initData.length);
        return result;
    }

    public static int[] createInts(int[] src, int fromIndex, int toIndex) {

        int[] ints = new int[toIndex - fromIndex];
        System.arraycopy(src, fromIndex, ints, 0, ints.length);
        return ints;
    }

    public static int[] createInts(int sizeInt, int offset, int... initData) {
        int[] result = new int[sizeInt];
        System.arraycopy(initData, 0, result, offset, initData.length);
        return result;
    }

    public static int writeInts(int[] srcInt, int offSet, int... data) {
        System.arraycopy(data, 0, srcInt, offSet, data.length);
        return offSet + data.length;
    }

    public static int writeBytes(byte[] srcBytes, int offSet, int... data) {
        int i = offSet;
        for (int d : data) {
            srcBytes[i] = (byte) (d & 0xFF);
            i++;
            srcBytes[i] = (byte) ((d >> 8) & 0xFF);
            i++;
        }
        return i;
    }

    public static int writeBytes(byte[] srcBytes, int offSet, int fromIndexData, int toIndexData, int... data) {
        int i = offSet;
        for (int index = fromIndexData; index < toIndexData; index++) {
            int d = data[index];
            srcBytes[i] = (byte) ((d >> 8) & 0xFF);
            i++;
            srcBytes[i] = (byte) (d & 0xFF);
            i++;
        }
        return i;
    }


    public static byte[] createBytes(int sizeBytes, int initOffSet, byte[] initBytes, int... initData) {
        byte[] result = new byte[sizeBytes];
        System.arraycopy(initBytes, 0, result, initOffSet, initBytes.length);
        int i = initOffSet + initBytes.length;
        for (int d : initData) {
            result[i] = (byte) ((d >> 8) & 0xFF);
            i++;
            result[i] = (byte) (d & 0xFF);
            i++;
        }
        return result;
    }


    public static byte[] createBytes(byte[] data1, byte[] data2) {
        byte[] result = Arrays.copyOf(data1, data1.length + data2.length);
        int offset = data1.length;
        System.arraycopy(data2, 0, result, offset, data2.length);
        return result;
    }

    public static byte[] createBytes(int... data) {
        byte[] bytes = new byte[data.length * 2];
        int i = 0;
        for (int d : data) {
            bytes[i] = (byte) ((d >> 8) & 0xFF);
            i++;
            bytes[i] = (byte) (d & 0xFF);
            i++;
        }
        return bytes;
    }

    public static byte[] createBytes(int[] array, boolean arrayFirst, int... data) {
        byte[] bytes = new byte[data.length * 2 + (array.length * 2)];
        int i = 0;
        if (arrayFirst) {
            for (int d : array) {
                bytes[i] = getUShortB0(d);
                i++;
                bytes[i] = getUShortB1(d);
                i++;
            }

            for (int d : data) {
                bytes[i] = getUShortB0(d);
                i++;
                bytes[i] = getUShortB1(d);
                i++;
            }
        } else {
            for (int d : data) {
                bytes[i] = getUShortB0(d);
                i++;
                bytes[i] = getUShortB1(d);
                i++;
            }
            for (int d : array) {
                bytes[i] = getUShortB0(d);
                i++;
                bytes[i] = getUShortB1(d);
                i++;
            }
        }

        return bytes;
    }


    public static byte[] createBytes(boolean arrayFirst, int[] array, int... data) {
        byte[] bytes = new byte[data.length * 2 + (array.length * 2)];
        int i = 0;
        if (arrayFirst) {
            for (int d : array) {
                bytes[i] = getUShortB0(d);
                i++;
                bytes[i] = getUShortB1(d);
                i++;
            }

            for (int d : data) {
                bytes[i] = getUShortB0(d);
                i++;
                bytes[i] = getUShortB1(d);
                i++;
            }
        } else {
            for (int d : data) {
                bytes[i] = getUShortB0(d);
                i++;
                bytes[i] = getUShortB1(d);
                i++;
            }
            for (int d : array) {
                bytes[i] = getUShortB0(d);
                i++;
                bytes[i] = getUShortB1(d);
                i++;
            }
        }
        return bytes;
    }


    @SafeVarargs
    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}
