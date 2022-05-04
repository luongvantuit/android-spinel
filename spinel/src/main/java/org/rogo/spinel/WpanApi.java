package org.rogo.spinel;

import android.content.Context;

import org.rogo.spinel.helpers.ByteUtils;
import org.rogo.spinel.helpers.SpinelLogger;
import org.rogo.spinel.helpers.Utilities;
import org.rogo.spinel.interfaces.ISpinelFrameDataCallBack;
import org.rogo.spinel.interfaces.IStream;
import org.rogo.spinel.ncp.FrameData;
import org.rogo.spinel.ncp.Hdlc;
import org.rogo.spinel.ncp.PropertyFormat;
import org.rogo.spinel.ncp.SpinelCommands;
import org.rogo.spinel.ncp.SpinelDecoder;
import org.rogo.spinel.ncp.SpinelEUI64;
import org.rogo.spinel.ncp.SpinelEncoder;
import org.rogo.spinel.ncp.SpinelIPv6Address;
import org.rogo.spinel.ncp.SpinelProperties;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class WpanApi {

    private final static String TAG = WpanApi.class.getSimpleName();

    private final byte SpinelHeaderFlag = ByteUtils.getUByte(0x80);
    private IStream stream;
    private Hdlc hdlcInterface;
    private Queue<Object> waitingQueue = new LinkedList<>();
    private boolean isSyncFrameExpecting = false;
    private ISpinelFrameDataCallBack frameDataReceived;


    public WpanApi(IStream stream) {
        this.stream = stream;
        this.hdlcInterface = new Hdlc(this.stream);
    }

    public void open(Context context) {
        this.stream.open(context);
    }

    public void doReset() {
        transact(SpinelCommands.CMD_RESET);
    }

    public int doLastStatus() {
        FrameData frameData = propertyGetValue(SpinelProperties.PROP_LAST_STATUS);
        try {
            return (int) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("Interface type format violation");
        }
    }

    public long[] doProtocolVersion() {
        FrameData frameData = propertyGetValue(SpinelProperties.PROP_PROTOCOL_VERSION);

        try {
            return (long[]) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("Protocol version format violation");
        }
    }

    public String doNCPVersion() {
        FrameData frameData = propertyGetValue(SpinelProperties.PROP_NCP_VERSION);

        try {
            return frameData.getResponse().toString();
        } catch (Exception e) {
            throw new RuntimeException("Protocol ncp version format violation");
        }
    }

    public String doVendor() {
        FrameData frameData = propertyGetValue(SpinelProperties.PROP_VENDOR_ID);

        try {
            return frameData.getResponse().toString();
        } catch (Exception e) {
            throw new RuntimeException("Vendor id format violation");
        }
    }


    public long doInterfaceType() {
        FrameData frameData = propertyGetValue(SpinelProperties.PROP_INTERFACE_TYPE);

        try {
            return (long) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("Interface type format violation");
        }
    }

    public int[] doCaps() {
        FrameData frameData = propertyGetValue(SpinelProperties.PROP_CAPS);

        try {
            return (int[]) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("Caps format violation");
        }
    }

    public String doNetworkName() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_NET_NETWORK_NAME);

        try {
            return frameData.getResponse().toString();
        } catch (Exception e) {
            throw new RuntimeException("Network name format violation");
        }
    }

    public boolean doNetworkName(String networkName) {
        FrameData frameData = propertySetValue(SpinelProperties.SPINEL_PROP_NET_NETWORK_NAME, networkName, PropertyFormat.U);
        return frameData != null && frameData.getResponse().toString().equals(networkName);
    }

    public byte doNetRole() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_NET_ROLE);
        try {
            return (byte) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("Role id format violation");
        }
    }

    public boolean doNetRole(byte role) {
        FrameData frameData = propertySetValue(SpinelProperties.SPINEL_PROP_NET_ROLE, role, PropertyFormat.C);
        return frameData != null && (byte) (frameData.getResponse()) == role;
    }

    public byte doPowerState() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_MCU_POWER_STATE);

        try {
            return (byte) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("Power state format violation");
        }
    }

    public boolean doPowerState(byte powerState) {
        FrameData frameData = propertySetValue(SpinelProperties.SPINEL_PROP_MCU_POWER_STATE, powerState, PropertyFormat.C);
        return frameData != null && (byte) (frameData.getResponse()) == powerState;
    }

    public byte doChannel() {
        FrameData frameData = propertyGetValue(SpinelProperties.PROP_PHY_CHAN);

        try {
            return (byte) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("Channel number format violation");
        }
    }

    public boolean doChannel(byte channel) {
        FrameData frameData = propertySetValue(SpinelProperties.PROP_PHY_CHAN, channel, PropertyFormat.C);
        return frameData != null && ((byte) frameData.getResponse() == channel);
    }

    public byte[] doChannels() {
        FrameData frameData = propertyGetValue(SpinelProperties.PROP_PHY_CHAN_SUPPORTED);

        try {
            return (byte[]) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("Supported channels format violation");
        }
    }

    public byte[] doChannelsMask() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_MAC_SCAN_MASK);

        try {
            return (byte[]) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("Channels mask format violation");
        }
    }

    public boolean doChannelsMask(byte[] channels) {
        FrameData frameData = propertySetValue(SpinelProperties.SPINEL_PROP_MAC_SCAN_MASK, channels, PropertyFormat.D);
        return frameData != null && Utilities.byteArrayCompare((byte[]) frameData.getResponse(), channels);
    }

    public int doPanId() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_MAC_15_4_PANID);

        try {
            return (int) (frameData.getResponse());
        } catch (Exception e) {
            throw new RuntimeException("Pan id format violation");
        }
    }

    public boolean doPanId(int panId) {
        FrameData frameData = propertySetValue(SpinelProperties.SPINEL_PROP_MAC_15_4_PANID, panId, PropertyFormat.S);
        return frameData != null && (int) (frameData.getResponse()) == panId;
    }

    public byte[] DoXPanId() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_NET_XPANID);

        try {
            return (byte[]) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("XPan id format violation");
        }
    }

    public boolean doXPanId(byte[] xPanId) {
        FrameData frameData = propertySetValue(SpinelProperties.SPINEL_PROP_NET_XPANID, xPanId, PropertyFormat.D);
        return frameData != null && Utilities.byteArrayCompare((byte[]) frameData.getResponse(), xPanId);
    }

    public SpinelIPv6Address[] doIPAddresses() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_IPV6_ADDRESS_TABLE);

        try {
            return (SpinelIPv6Address[]) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("IP address format violation");
        }
    }

    public SpinelIPv6Address doIPLinkLocal64() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_IPV6_LL_ADDR);

        try {
            return (SpinelIPv6Address) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("IP address format violation");
        }
    }

    public SpinelEUI64 doExtendedAddress() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_MAC_15_4_LADDR);

        try {
            return (SpinelEUI64) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("IP address format violation");
        }
    }

    public SpinelEUI64 doPhysicalAddress() {
        FrameData frameData = propertyGetValue(SpinelProperties.PROP_HWADDR);

        try {
            return (SpinelEUI64) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("IP address format violation");
        }
    }


    public SpinelIPv6Address doIPMeshLocal64() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_IPV6_ML_ADDR);

        try {
            return (SpinelIPv6Address) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("IP address format violation");
        }
    }

    public boolean doInterfaceConfig() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_NET_IF_UP);
        try {
            return (boolean) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("XPan id format violation");
        }
    }

    public boolean doInterfaceConfig(boolean interfaceState) {
        FrameData frameData;
        if (interfaceState) {
            frameData = propertySetValue(SpinelProperties.SPINEL_PROP_NET_IF_UP, 1, PropertyFormat.b);
        } else {
            frameData = propertySetValue(SpinelProperties.SPINEL_PROP_NET_IF_UP, 0, PropertyFormat.b);
        }
        return frameData != null && (boolean) (frameData.getResponse()) == interfaceState;
    }

    public boolean doThread() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_NET_STACK_UP);
        try {
            return (boolean) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("Stack up format violation");
        }
    }

    public boolean doThread(boolean threadState) {
        FrameData frameData;

        if (threadState) {
            frameData = propertySetValue(SpinelProperties.SPINEL_PROP_NET_STACK_UP, 1, PropertyFormat.b);
        } else {
            frameData = propertySetValue(SpinelProperties.SPINEL_PROP_NET_STACK_UP, 0, PropertyFormat.b);
        }
        return frameData != null && (boolean) (frameData.getResponse()) == threadState;
    }

    public byte[] doMasterkey() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_NET_MASTER_KEY);

        try {
            return (byte[]) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("XPan id format violation");
        }
    }

    public boolean doMasterkey(byte[] masterKey) {
        FrameData frameData = propertySetValue(SpinelProperties.SPINEL_PROP_NET_MASTER_KEY, masterKey, PropertyFormat.D);
        return frameData != null && Utilities.byteArrayCompare((byte[]) frameData.getResponse(), masterKey);
    }

    public long doPartitionId() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_NET_PARTITION_ID);
        try {
            return (long) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("Partition id format violation");
        }
    }

    public void doScan(byte scanState) {
        propertySetValue(SpinelProperties.SPINEL_PROP_MAC_SCAN_STATE, scanState, PropertyFormat.C);
    }

    public boolean doProperty_NET_REQUIRE_JOIN_EXISTING(boolean state) {
        FrameData frameData;

        if (state) {
            frameData = propertySetValue(SpinelProperties.SPINEL_PROP_NET_REQUIRE_JOIN_EXISTING, 1, PropertyFormat.b);
        } else {
            frameData = propertySetValue(SpinelProperties.SPINEL_PROP_NET_REQUIRE_JOIN_EXISTING, 0, PropertyFormat.b);
        }

        return frameData != null && (boolean) (frameData.getResponse()) == state;
    }

//    public void doSendData(byte[] frame) {
//        doSendData(frame, true);
//    }

//    public void doSendData(byte[] frame, boolean waitResponse) {
//        byte[] dataCombined = SpinelEncoder.encodeDataWithLength(frame);
//        propertySetValue(SpinelProperties.PROP_STREAM_NET, dataCombined, "dD", 129, waitResponse);
//    }


    public void doCountersReset() {
        propertySetValue(SpinelProperties.SPINEL_PROP_CNTR_RESET, 1, PropertyFormat.C);
    }

    public int[] doCountersMessageBuffer() {
        FrameData frameData = propertyGetValue(SpinelProperties.SPINEL_PROP_MSG_BUFFER_COUNTERS);

        try {
            return (int[]) frameData.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("Buffer counters format violation");
        }
    }


    public void transact(int commandId, byte[] payload, byte tID) {
        byte[] packet = encodePacket(commandId, tID, payload);
        streamTx(packet);
    }

    public void transact(int commandId, byte[] payload) {
        this.transact(commandId, payload, SpinelCommands.HEADER_DEFAULT);
    }

    public void transact(int commandId, byte tID) {
        transact(commandId, null, tID);
    }

    public void transact(int commandId) {
        transact(commandId, null, SpinelCommands.HEADER_DEFAULT);
    }

    public byte[] encodePacket(int commandId, byte tid, byte[] payload) {
        byte[] tidBytes = new byte[]{tid};
        byte[] commandBytes = SpinelEncoder.encodeValue(commandId);
        byte[] packet = new byte[commandBytes.length + tidBytes.length + (payload == null ? 0 : payload.length)];

        if (payload != null) {
            packet = Utilities.combineArrays(tidBytes, commandBytes, payload);
        } else {
            packet = Utilities.combineArrays(tidBytes, commandBytes);
        }
        return packet;
    }


    private void streamDataReceived() {
        synchronized (WpanApi.class) {
            streamRX();
        }

        // receivedPacketWaitHandle.Set();

        if (isSyncFrameExpecting) {
            return;
        }

        while (waitingQueue.size() != 0) {

            FrameData frameData = (FrameData) waitingQueue.poll();

            if (frameDataReceived != null) {
                frameDataReceived.received(frameData);
            }
        }

        //  receivedPacketWaitHandle.Reset();
    }

    private FrameData propertyChangeValue(int commandId, int propertyId, byte[] propertyValue) {
        return propertyChangeValue(commandId, propertyId, propertyValue, PropertyFormat.B);
    }

    private FrameData propertyChangeValue(int commandId, int propertyId, byte[] propertyValue, PropertyFormat propertyFormat) {
        return propertyChangeValue(commandId, propertyId, propertyValue, propertyFormat, SpinelCommands.HEADER_DEFAULT);
    }

    private FrameData propertyChangeValue(int commandId, int propertyId, byte[] propertyValue, PropertyFormat propertyFormat, byte tid) {
        return propertyChangeValue(commandId, propertyId, propertyValue, propertyFormat, tid, true);
    }

    private FrameData propertyChangeValue(int commandId, int propertyId, byte[] propertyValue, PropertyFormat propertyFormat, byte tid, boolean waitResponse) {
        FrameData responseFrame = null;
        isSyncFrameExpecting = true;
        byte[] payload = SpinelEncoder.encodeValue(propertyId);

        if (propertyFormat != null) {
            payload = Utilities.combineArrays(payload, propertyValue);
        }

        int uid = Utilities.getUid(propertyId, tid);

        synchronized (WpanApi.class) {
            transact(commandId, payload, tid);
        }

        if (!waitResponse) {
            isSyncFrameExpecting = false;
            return null;
        }

//          receivedPacketWaitHandle.Reset();
//
//         if (!receivedPacketWaitHandle.WaitOne(155000, false)) {
//             throw new SpinelProtocolExceptions("Timeout for sync packet " + commandId);
//         }

//        try {
//            Thread.sleep(155000);
//        } catch (InterruptedException e) {
//            SpinelLogger.getInstance().error(TAG, e.getMessage());
//        }

//        int counter = 0;
//
//        while (counter++ < 1500 || waitingQueue.size() == 0) {
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                SpinelLogger.getInstance().error(TAG, e.getMessage());
//            }
//        }

        if (waitingQueue.size() > 0) {
            while (waitingQueue.size() != 0) {
                FrameData frameData = (FrameData) waitingQueue.poll();

                if (frameData != null && frameData.getUid() == uid) {
                    responseFrame = frameData;
                    isSyncFrameExpecting = false;
                } else {
                    if (frameDataReceived != null) {
                        frameDataReceived.received(frameData);
                    }
                }
            }
        } else {
            throw new RuntimeException("No response packet for command" + commandId);
        }

        return responseFrame;
    }

    private void streamTx(byte[] packet) {
        hdlcInterface.write(packet);
    }

    private void streamRX(int timeout) {
        long start = System.currentTimeMillis();

        boolean dataPooled = false;

        while (true) {
            long elapsed = System.currentTimeMillis() - start;

            if (timeout != 0) {
                if (elapsed / 1000 > timeout) {
                    break;
                }
            }

            if (stream.getIsDataAvailable()) {
                byte[] frameDecoded = hdlcInterface.read();
                parseRX(frameDecoded);
                dataPooled = true;
            }

            if (!stream.getIsDataAvailable() && dataPooled) {
                break;
            }
        }
    }

    private void streamRX() {
        streamRX(0);
    }

    private void parseRX(byte[] frameIn) {

        SpinelDecoder mDecoder = new SpinelDecoder();
        Object ncpResponse = null;
        mDecoder.init(frameIn);

        byte header = mDecoder.frameHeader;

        if ((SpinelHeaderFlag & header) != SpinelHeaderFlag) {
            throw new RuntimeException("Header parsing error.");
        }

        long command = mDecoder.frameCommand;
        int propertyId = mDecoder.framePropertyId;

        if (propertyId == SpinelProperties.SPINEL_PROP_THREAD_CHILD_TABLE) {
            if (command == SpinelCommands.RSP_PROP_VALUE_INSERTED || command == SpinelCommands.RSP_PROP_VALUE_REMOVED) {
                return;
            }
        }

        ArrayList<Object> tempObj = null;

        switch (propertyId) {
            case SpinelProperties.PROP_NCP_VERSION:

            case SpinelProperties.SPINEL_PROP_NET_NETWORK_NAME:

                ncpResponse = mDecoder.readUtf8();
                break;

            case SpinelProperties.PROP_LAST_STATUS:

            case SpinelProperties.PROP_INTERFACE_TYPE:

            case SpinelProperties.PROP_VENDOR_ID:
                ncpResponse = mDecoder.readUIntPacked();
                break;

            case SpinelProperties.SPINEL_PROP_MAC_SCAN_STATE:

            case SpinelProperties.PROP_PHY_CHAN:

            case SpinelProperties.SPINEL_PROP_NET_ROLE:

            case SpinelProperties.SPINEL_PROP_MCU_POWER_STATE:
                ncpResponse = mDecoder.readUInt8();
                break;

            case SpinelProperties.SPINEL_PROP_MAC_SCAN_MASK:
            case SpinelProperties.PROP_PHY_CHAN_SUPPORTED:
                tempObj = mDecoder.readFields("A(C)");
                if (tempObj != null) {
                    byte[] bytes = new byte[((ArrayList<Object>) tempObj).size()];
                    for (int i = 0; i < ((ArrayList<Object>) tempObj).size(); i++) {
                        bytes[i] = (byte) ((ArrayList<Object>) tempObj).get(i);
                    }
                    ncpResponse = bytes;
                }
                break;
            case SpinelProperties.SPINEL_PROP_MAC_SCAN_PERIOD:

            case SpinelProperties.SPINEL_PROP_MAC_15_4_PANID:
                ncpResponse = mDecoder.readUInt16();
                break;

            case SpinelProperties.SPINEL_PROP_MAC_SCAN_BEACON:
                ncpResponse = mDecoder.readFields("Cct(ESSC)t(iCUdd)");
                break;

            case SpinelProperties.SPINEL_PROP_MAC_ENERGY_SCAN_RESULT:
                ncpResponse = mDecoder.readFields("Cc");
                break;

            case SpinelProperties.PROP_PROTOCOL_VERSION:
                tempObj = mDecoder.readFields("ii");
                if (tempObj != null) {
                    long[] longs = new long[((ArrayList<Object>) tempObj).size()];
                    for (int i = 0; i < ((ArrayList<Object>) tempObj).size(); i++) {
                        longs[i] = (long) ((ArrayList<Object>) tempObj).get(i);
                    }
                    ncpResponse = longs;
                }
                break;
            case SpinelProperties.PROP_CAPS:
                tempObj = mDecoder.readFields("A(i)");
                if (tempObj != null) {
                    ArrayList<Object> caps = tempObj;
                    int[] capsArray = new int[caps.size()];
                    int index = 0;
                    for (Object capsValue : caps) {
                        capsArray[index] = (int) (capsValue);
                        index++;
                    }
                    ncpResponse = capsArray;
                }

                break;

            case SpinelProperties.SPINEL_PROP_MSG_BUFFER_COUNTERS:

                tempObj = mDecoder.readFields("SSSSSSSSSSSSSSSS");

                if (tempObj != null) {
                    int[] ints = new int[((ArrayList<Object>) tempObj).size()];
                    for (int i = 0; i < ((ArrayList<Object>) tempObj).size(); i++) {
                        ints[i] = (int) ((ArrayList<Object>) tempObj).get(i);
                    }
                    ncpResponse = ints;
                }
                break;
            case SpinelProperties.SPINEL_PROP_IPV6_ADDRESS_TABLE:
                tempObj = mDecoder.readFields("A(t(6CLL))");
                ArrayList<Object> ipAddresses = new ArrayList<>();
                if (tempObj != null) {
                    ArrayList<Object> addressArray = (ArrayList<Object>) tempObj;
                    for (Object addressInfo : addressArray) {
                        Object[] ipProps = ((ArrayList<Object>) addressInfo).toArray();
                        SpinelIPv6Address ipaddr = (SpinelIPv6Address) ipProps[0];
                        ipAddresses.add(ipaddr);
                    }
                }
                if (ipAddresses.size() > 0) {
                    ncpResponse = ipAddresses.toArray();
                }
                break;
            case SpinelProperties.SPINEL_PROP_NET_IF_UP:
            case SpinelProperties.SPINEL_PROP_NET_STACK_UP:
            case SpinelProperties.SPINEL_PROP_NET_REQUIRE_JOIN_EXISTING:
                ncpResponse = mDecoder.readBool();
                break;
            case SpinelProperties.SPINEL_PROP_NET_XPANID:
            case SpinelProperties.SPINEL_PROP_NET_MASTER_KEY:
                ncpResponse = mDecoder.readData();
                break;
            case SpinelProperties.PROP_STREAM_NET:
                tempObj = mDecoder.readFields("dD");
                if (tempObj != null) {
                    ncpResponse = ((ArrayList<Object>) tempObj).get(0);
                }
                break;
            case SpinelProperties.SPINEL_PROP_IPV6_LL_ADDR:
            case SpinelProperties.SPINEL_PROP_IPV6_ML_ADDR:
                ncpResponse = mDecoder.readIp6Address();
                break;
            case SpinelProperties.SPINEL_PROP_MAC_15_4_LADDR:

            case SpinelProperties.PROP_HWADDR:
                ncpResponse = mDecoder.readEui64();
                break;
//            case SpinelProperties.SPINEL_PROP_IPV6_ML_PREFIX:
//                ncpResponse = mDecoder.readFields("6C");
//                break;
        }
        FrameData frameData = new FrameData(mDecoder.framePropertyId, mDecoder.frameHeader, mDecoder.getFrameLoad(), ncpResponse);
        waitingQueue.add(frameData);
    }

    private FrameData propertyGetValue(int propertyId) {
        return propertyGetValue(propertyId, SpinelCommands.HEADER_DEFAULT);
    }

    private FrameData propertyGetValue(int propertyId, byte tid) {
        return propertyChangeValue(SpinelCommands.CMD_PROP_VALUE_GET, propertyId, null, null, tid);
    }

    private FrameData propertySetValue(int propertyId, int propertyValue) {
        return propertySetValue(propertyId, propertyValue, PropertyFormat.B);
    }

    private FrameData propertySetValue(int propertyId, int propertyValue, PropertyFormat propertyFormat) {
        return propertySetValue(propertyId, propertyValue, propertyFormat, SpinelCommands.HEADER_DEFAULT);
    }

    private FrameData propertySetValue(int propertyId, int propertyValue, PropertyFormat propertyFormat, byte tid) {
        byte[] propertyValueArray = SpinelEncoder.encodeValue(propertyValue, propertyFormat);
        return propertySetValue(propertyId, propertyValueArray, propertyFormat, tid);
    }

    private FrameData propertySetValue(int propertyId, byte propertyValue) {
        return propertySetValue(propertyId, propertyValue, PropertyFormat.B);
    }

    private FrameData propertySetValue(int propertyId, byte propertyValue, PropertyFormat propertyFormat) {
        return propertySetValue(propertyId, propertyValue, propertyFormat, SpinelCommands.HEADER_DEFAULT);
    }

    private FrameData propertySetValue(int propertyId, byte propertyValue, PropertyFormat propertyFormat, byte tid) {
        byte[] propertyValueArray = SpinelEncoder.encodeValue(propertyValue, propertyFormat);
        return propertySetValue(propertyId, propertyValueArray, propertyFormat, tid);
    }

    private FrameData propertySetValue(int propertyId, String propertyValue) {
        return propertySetValue(propertyId, propertyValue, PropertyFormat.B);
    }

    private FrameData propertySetValue(int propertyId, String propertyValue, PropertyFormat propertyFormat) {
        return propertySetValue(propertyId, propertyValue, propertyFormat, SpinelCommands.HEADER_DEFAULT);
    }

    private FrameData propertySetValue(int propertyId, String propertyValue, PropertyFormat propertyFormat, byte tid) {
        byte[] propertyValueArray = SpinelEncoder.encodeValue(propertyValue, propertyFormat);
        return propertySetValue(propertyId, propertyValueArray, propertyFormat, tid);
    }

    private FrameData propertySetValue(int propertyId, byte[] propertyValue) {
        return propertySetValue(propertyId, propertyValue, PropertyFormat.B);
    }

    private FrameData propertySetValue(int propertyId, byte[] propertyValue, PropertyFormat propertyFormat) {
        return propertySetValue(propertyId, propertyValue, propertyFormat, SpinelCommands.HEADER_DEFAULT);
    }

    private FrameData propertySetValue(int propertyId, byte[] propertyValue, PropertyFormat propertyFormat, byte tid) {
        return propertySetValue(propertyId, propertyValue, propertyFormat, tid, true);
    }

    private FrameData propertySetValue(int propertyId, byte[] propertyValue, PropertyFormat propertyFormat, byte tid, boolean waitResponse) {
        return propertyChangeValue(SpinelCommands.CMD_PROP_VALUE_SET, propertyId, propertyValue, propertyFormat, tid, waitResponse);
    }

}
