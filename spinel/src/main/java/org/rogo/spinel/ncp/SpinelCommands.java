package org.rogo.spinel.ncp;

import org.rogo.spinel.helpers.ByteUtils;

public class SpinelCommands {
    // Singular class that contains all Spinel final staticants. """

    public final static byte HEADER_ASYNC = ByteUtils.getUByte(0x80);

    public final static byte HEADER_DEFAULT = ByteUtils.getUByte(0x81);

    public final static byte HEADER_EVENT_HANDLER = ByteUtils.getUByte(0x82);

    //// =========================================
    //// Spinel Commands: Host -> NCP
    //// =========================================

    public final static int CMD_NOOP = 0;

    public final static int CMD_RESET = 1;

    public final static int CMD_PROP_VALUE_GET = 2;

    public final static int CMD_PROP_VALUE_SET = 3;

    public final static int CMD_PROP_VALUE_INSERT = 4;

    public final static int CMD_PROP_VALUE_REMOVE = 5;

    //// =========================================
    //// Spinel Command Responses: NCP -> Host
    //// =========================================

    public final static int RSP_PROP_VALUE_IS = 6;

    public final static int RSP_PROP_VALUE_INSERTED = 7;

    public final static int RSP_PROP_VALUE_REMOVED = 8;
}
