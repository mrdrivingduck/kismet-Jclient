package iot.zjt.jclient.util;

import java.util.HashMap;
import java.util.Map;

public class CryptoMap {
    
    public static final Map<Integer, String> typeMap = new HashMap<Integer, String>() {
        private static final long serialVersionUID = 802385814998698738L;
		{
            put(0, "");
            put(1, "UNKNOWN");
            put(1 << 1, "WEP");
            put(1 << 2, "LAYER3");
            put(1 << 3, "WEP40");
            put(1 << 4, "WEP104");
            put(1 << 5, "TKIP");
            // put(1 << 6, "WPA");
            put(1 << 7, "PSK");
            put(1 << 8, "AES_OCB");
            put(1 << 9, "AES_CCM");
            put(1 << 10, "WPA_MIGMODE");
            put(1 << 11, "EAP");
            put(1 << 12, "LEAP");
            put(1 << 13, "TTLS");
            put(1 << 14, "TLS");
            put(1 << 15, "PEAP");
            put(1 << 20, "ISAKMP");
            put(1 << 21, "PPTP");
            put(1 << 22, "FORTRESS");
            put(1 << 23, "KEYGUARD");
            put(1 << 24, "UNKNOWN_PROTECTED");
            put(1 << 25, "UNKNOWN_NONWEP");
            put(1 << 26, "WPS");
            put(1 << 27, "WPA");
            put(1 << 28, "WPA2");
        }
    };
}