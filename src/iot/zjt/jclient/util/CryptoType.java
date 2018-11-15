package iot.zjt.jclient.util;

public enum CryptoType {
    crypt_none 			(0),
    crypt_unknown		(1),
    crypt_wep			(1 << 1),
    crypt_layer3		(1 << 2),
    crypt_wep40			(1 << 3),
    crypt_wep104		(1 << 4),
    crypt_tkip			(1 << 5),
    crypt_wpa			(1 << 6),
    crypt_psk			(1 << 7),
    crypt_aes_ocb		(1 << 8),
    crypt_aes_ccm		(1 << 9),
    crypt_wpa_migmode	(1 << 10),
    crypt_eap			(1 << 11),
    crypt_leap			(1 << 12),
    crypt_ttls			(1 << 13),
    crypt_tls			(1 << 14),
    crypt_peap			(1 << 15),
    crypt_isakmp		(1 << 20),
    crypt_pptp			(1 << 21),
    crypt_fortress		(1 << 22),
    crypt_keyguard		(1 << 23),
    crypt_unknown_protected 	(1 << 24),
    crypt_unknown_nonwep		(1 << 25),
    crypt_wps 					(1 << 26),
    crypt_version_wpa   (1 << 27),
    crypt_version_wpa2  (1 << 28);

    private int crypt_val;

    private CryptoType(int crypt_val) {
        this.crypt_val = crypt_val;
    }

    public int value() {
        return crypt_val;
    }
}