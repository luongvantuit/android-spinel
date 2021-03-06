package org.rogo.spinel.ncp;

public class Capabilities {
    public static final int SPINEL_CAP_LOCK = 1;
    public static final int SPINEL_CAP_NET_SAVE = 2;
    public static final int SPINEL_CAP_HBO = 3;
    public static final int SPINEL_CAP_POWER_SAVE = 4;
    public static final int SPINEL_CAP_COUNTERS = 5;
    public static final int SPINEL_CAP_JAM_DETECT = 6;

    public static final int SPINEL_CAP_PEEK_POKE = 7;
    public static final int SPINEL_CAP_WRITABLE_RAW_STREAM = 8;
    public static final int SPINEL_CAP_GPIO = 9;
    public static final int SPINEL_CAP_TRNG = 10;
    public static final int SPINEL_CAP_CMD_MULTI = 11;
    public static final int SPINEL_CAP_UNSOL_UPDATE_FILTER = 12;
    public static final int SPINEL_CAP_MCU_POWER_STATE = 13;
    public static final int SPINEL_CAP_PCAP = 14;
    public static final int SPINEL_CAP_802_15_4__BEGIN = 16;
    public static final int SPINEL_CAP_802_15_4_2003 = (SPINEL_CAP_802_15_4__BEGIN + 0);
    public static final int SPINEL_CAP_802_15_4_2006 = (SPINEL_CAP_802_15_4__BEGIN + 1);
    public static final int SPINEL_CAP_802_15_4_2011 = (SPINEL_CAP_802_15_4__BEGIN + 2);
    public static final int SPINEL_CAP_802_15_4_PIB = (SPINEL_CAP_802_15_4__BEGIN + 5);
    public static final int SPINEL_CAP_802_15_4_2450MHZ_OQPSK = (SPINEL_CAP_802_15_4__BEGIN + 8);
    public static final int SPINEL_CAP_802_15_4_915MHZ_OQPSK = (SPINEL_CAP_802_15_4__BEGIN + 9);
    public static final int SPINEL_CAP_802_15_4_868MHZ_OQPSK = (SPINEL_CAP_802_15_4__BEGIN + 10);
    public static final int SPINEL_CAP_802_15_4_915MHZ_BPSK = (SPINEL_CAP_802_15_4__BEGIN + 11);
    public static final int SPINEL_CAP_802_15_4_868MHZ_BPSK = (SPINEL_CAP_802_15_4__BEGIN + 12);
    public static final int SPINEL_CAP_802_15_4_915MHZ_ASK = (SPINEL_CAP_802_15_4__BEGIN + 13);
    public static final int SPINEL_CAP_802_15_4_868MHZ_ASK = (SPINEL_CAP_802_15_4__BEGIN + 14);
    public static final int SPINEL_CAP_802_15_4__END = 32;
    public static final int SPINEL_CAP_CONFIG__BEGIN = 32;
    public static final int SPINEL_CAP_CONFIG_FTD = (SPINEL_CAP_CONFIG__BEGIN + 0);
    public static final int SPINEL_CAP_CONFIG_MTD = (SPINEL_CAP_CONFIG__BEGIN + 1);
    public static final int SPINEL_CAP_CONFIG_RADIO = (SPINEL_CAP_CONFIG__BEGIN + 2);
    public static final int SPINEL_CAP_CONFIG__END = 40;
    public static final int SPINEL_CAP_ROLE__BEGIN = 48;
    public static final int SPINEL_CAP_ROLE_ROUTER = (SPINEL_CAP_ROLE__BEGIN + 0);
    public static final int SPINEL_CAP_ROLE_SLEEPY = (SPINEL_CAP_ROLE__BEGIN + 1);
    public static final int SPINEL_CAP_ROLE__END = 52;
    public static final int SPINEL_CAP_NET__BEGIN = 52;
    public static final int SPINEL_CAP_NET_THREAD_1_0 = (SPINEL_CAP_NET__BEGIN + 0);
    public static final int SPINEL_CAP_NET_THREAD_1_1 = (SPINEL_CAP_NET__BEGIN + 1);
    public static final int SPINEL_CAP_NET__END = 64;
    public static final int SPINEL_CAP_OPENTHREAD__BEGIN = 512;
    public static final int SPINEL_CAP_MAC_WHITELIST = (SPINEL_CAP_OPENTHREAD__BEGIN + 0);
    public static final int SPINEL_CAP_MAC_RAW = (SPINEL_CAP_OPENTHREAD__BEGIN + 1);
    public static final int SPINEL_CAP_OOB_STEERING_DATA = (SPINEL_CAP_OPENTHREAD__BEGIN + 2);
    public static final int SPINEL_CAP_CHANNEL_MONITOR = (SPINEL_CAP_OPENTHREAD__BEGIN + 3);
    public static final int SPINEL_CAP_ERROR_RATE_TRACKING = (SPINEL_CAP_OPENTHREAD__BEGIN + 4);
    public static final int SPINEL_CAP_CHANNEL_MANAGER = (SPINEL_CAP_OPENTHREAD__BEGIN + 5);
    public static final int SPINEL_CAP_OPENTHREAD_LOG_METADATA = (SPINEL_CAP_OPENTHREAD__BEGIN + 6);
    public static final int SPINEL_CAP_TIME_SYNC = (SPINEL_CAP_OPENTHREAD__BEGIN + 7);
    public static final int SPINEL_CAP_CHILD_SUPERVISION = (SPINEL_CAP_OPENTHREAD__BEGIN + 8);
    public static final int SPINEL_CAP_POSIX_APP = (SPINEL_CAP_OPENTHREAD__BEGIN + 9);
    public static final int SPINEL_CAP_SLAAC = (SPINEL_CAP_OPENTHREAD__BEGIN + 10);
    public static final int SPINEL_CAP_OPENTHREAD__END = 640;
    public static final int SPINEL_CAP_THREAD__BEGIN = 1024;
    public static final int SPINEL_CAP_THREAD_COMMISSIONER = (SPINEL_CAP_THREAD__BEGIN + 0);
    public static final int SPINEL_CAP_THREAD_TMF_PROXY = (SPINEL_CAP_THREAD__BEGIN + 1);
    public static final int SPINEL_CAP_THREAD_UDP_FORWARD = (SPINEL_CAP_THREAD__BEGIN + 2);
    public static final int SPINEL_CAP_THREAD_JOINER = (SPINEL_CAP_THREAD__BEGIN + 3);
    public static final int SPINEL_CAP_THREAD_BORDER_ROUTER = (SPINEL_CAP_THREAD__BEGIN + 4);
    public static final int SPINEL_CAP_THREAD_SERVICE = (SPINEL_CAP_THREAD__BEGIN + 5);
    public static final int SPINEL_CAP_THREAD__END = 1152;
}
