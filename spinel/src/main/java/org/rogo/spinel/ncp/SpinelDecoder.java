package org.rogo.spinel.ncp;


import java.util.ArrayList;

public class SpinelDecoder {
    private byte[] frameBuffer; // Frame buffer.
    private int frameLength; // Frame length (number of bytes).
    private int frameIndex; // Current read index.
    private int frameEndIndex; // Current end index (end of struct if in a struct, or end of buffer otherwise).

    public byte frameHeader;

    public long frameCommand;

    public int framePropertyId;

    public SpinelDecoder() {
        this.frameLength = 0;
        this.frameIndex = 0;
        this.frameEndIndex = 0;
    }

    public void init(byte[] frameIn) {
        frameBuffer = frameIn;
        frameLength = frameIn.length;
        reset();

        frameHeader = readUint8();
        frameCommand = readUintPacked();

        if (frameCommand != SpinelCommands.RSP_PROP_VALUE_IS && frameCommand != SpinelCommands.RSP_PROP_VALUE_INSERTED && frameCommand != SpinelCommands.RSP_PROP_VALUE_REMOVED) {
            throw new RuntimeException();
        }

        framePropertyId = readUintPacked();
    }

    public void reset() {
        frameIndex = 0;
        frameEndIndex = frameLength;
    }

    public byte[] getFrameLoad() {
        byte[] frameLoad = new byte[frameBuffer.length - frameIndex];
        System.arraycopy(frameBuffer, frameIndex, frameLoad, 0, frameLoad.length);
        return frameLoad;
    }

    public String readUtf8() {
        int indexOfNull = -1;
        for (int i = frameIndex; i < frameBuffer.length; i++) {
            if (frameBuffer[i] == (byte) 0) {
                indexOfNull = i;
                break;
            }
        }
        if (indexOfNull == -1) {
            throw new RuntimeException();
        }
        int arrayLength = indexOfNull - frameIndex;
        byte[] segment = new byte[arrayLength];
        System.arraycopy(frameBuffer, frameIndex, segment, 0, arrayLength);
        frameIndex += arrayLength + 1; // +1 \0 char removed from the array
        return new String(segment);
    }

    public int readUintPacked() {
        int valueDecoded = 0;
        var value_mul = 1;
        var value_len_max = frameIndex + 4;

        while (frameIndex < value_len_max) {
            var packet = frameBuffer[frameIndex];

            valueDecoded += ((packet & 0x7F) * value_mul);
            frameIndex += 2;

            if (packet < 0x80) {
                break;
            }

            value_mul *= 0x80;
        }

        return valueDecoded;
    }

    public byte[] readData() {
        int aDataLen = frameEndIndex - frameIndex;
        return readItems(aDataLen);
    }

    public byte[] readDataWithLen() {
        int aDataLen = readUint16();
        return readItems(aDataLen);
    }

    public boolean readBool() {
        byte valueToDecode = readUint8();

        if (valueToDecode == 0x00) {
            return false;
        } else if (valueToDecode == 0x01) {
            return true;
        } else {
            throw new RuntimeException();
        }
    }

    public byte readUint8() {
        byte decodedValue = frameBuffer[frameIndex];
        frameIndex += 1;

        return decodedValue;
    }

    public byte readInt8() {
        return (byte) readUint8();
    }

    public int readUint16() {
        int aUint16 = (int) (frameBuffer[frameIndex] | (frameBuffer[frameIndex + 1] << 8));
        frameIndex += 2;
        return aUint16;
    }

    public short readInt16() {
        return (short) readUint16();
    }

    public long readUint32() {
        long aUint32 = (long) ((frameBuffer[frameIndex + 3] << 24) | (frameBuffer[frameIndex + 2] << 16) | (frameBuffer[frameIndex + 1] << 8) | frameBuffer[frameIndex]);
        frameIndex += 4;

        return aUint32;
    }

    public int readInt32() {
        return (int) readUint32();
    }

    public SpinelIPv6Address readIp6Address() {
        SpinelIPv6Address ipAddress = new SpinelIPv6Address();
        ipAddress.bytes = readItems(ipAddress.bytes.length);
        return ipAddress;
    }

    public SpinelEUI64 readEui64() {
        SpinelEUI64 eui64 = new SpinelEUI64();
        eui64.bytes = readItems(eui64.bytes.length);
        return eui64;
    }

    public SpinelEUI48 readEui48() {
        SpinelEUI48 eui48 = new SpinelEUI48();
        eui48.bytes = readItems(eui48.bytes.length);
        return eui48;
    }


    private Object spinelDatatypeValueUnpack(char spinel_format) {
        switch (spinel_format) {
            case 'b':
                return readBool();

            case 'c':
                return readInt8();

            case 'C':
                return readUint8();

            case 's':
                return readInt16();

            case 'S':
                return readUint16();

            case 'L':
                return readUint32();

            case 'l':
                return readInt32();

            case '6':
                return readIp6Address();

            case 'E':
                return readEui64();

            case 'e':
                return readEui48();

            case 'U':
                return readUtf8();

            case 'D':
                return readData();

            case 'd':
                return readDataWithLen();

            case 'i':
                return readUintPacked();

            default:
                throw new RuntimeException("Parsing frame data error.");
        }
    }


    private byte[] readItems(int SizeToRead) {
        byte[] tempArray = new byte[SizeToRead];

        for (int i = 0; i < SizeToRead; i++) {
            tempArray[i] = readUint8();
        }

        return tempArray;
    }

    public ArrayList<Object> readFields(String spinelFormat) {
        int indexFormat = 0;
        ArrayList<Object> result = new ArrayList<>();

        while (indexFormat < spinelFormat.length()) {
            char format = spinelFormat.charAt(indexFormat);

            if (format == 'A') {
                if (spinelFormat.charAt(indexFormat + 1) != '(') {
                    throw new RuntimeException("Incorrect structure format.");
                }

                int arrayEnd = getIndexOfEndingBrace(spinelFormat, indexFormat + 1);
                String arrayFormat = spinelFormat.substring(indexFormat + 2, arrayEnd - 2 + indexFormat);

                ArrayList<Object> array = new ArrayList<>();

                while (frameIndex <= frameBuffer.length - 1) {
                    array.addAll(readFields(arrayFormat));
                }

                indexFormat = arrayEnd + 1;
                result.addAll(array);
            } else if (format == 't') {
                if (spinelFormat.charAt(indexFormat + 1) != '(') {
                    throw new RuntimeException("Incorrect structure format.");
                }

                int structEnd = getIndexOfEndingBrace(spinelFormat, indexFormat + 1);
                String structFormat = spinelFormat.substring(indexFormat + 2, structEnd - indexFormat - 2);
                readUint16();

                result.add(readFields(structFormat));
                indexFormat = structEnd + 1;

            } else {
                result.add(spinelDatatypeValueUnpack(format));
                indexFormat += 1;
            }
        }

        return result;
    }

    private int getIndexOfEndingBrace(String spinelFormat, int idx) {
        int count = 1;

        while (count > 0 && idx < spinelFormat.length() - 1) {
            idx += 1;

            if (spinelFormat.charAt(idx) == ')') {
                count -= 1;
            }

            if (spinelFormat.charAt(idx) == '(') {
                count += 1;
            }
        }

        if (count != 0) {
            throw new RuntimeException("Unbalanced parenthesis in format string {0}" + spinelFormat + ", idx=" + idx);
        }

        return idx;
    }
}
