package iot.zjt.jclient.message;

import java.util.Date;
import java.util.Map;

import iot.zjt.jclient.annotation.ApiUrl;
import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.annotation.FieldPath;
import iot.zjt.jclient.annotation.MessageType;
import iot.zjt.jclient.util.CryptoType;
import iot.zjt.jclient.util.VendorUtil;

@MessageType("BSSID")
@ApiUrl("/devices/last-time/%d/devices.json")
public class BSSIDMessage extends KismetMessage {

    private String mac;
    private String ssid;
    private String channel;
    private int dataPackets;
    private int encryptedPackets;
    private int llcPackets;
    private String manufacturer;
    private Date firstTime;
    private Date lastTime;
    private int signalDBM;
    private int signalDBMMin;
    private int signalDBMMax;
    private int noiseDBM;
    private int noiseDBMMin;
    private int noiseDBMMax;
    private long dataBytes;
    private String nickname;
    private String cryptType;
    private long checksum;

    private void addDeliminator(StringBuilder strBuilder, char del) {
        if (strBuilder.length() > 0) {
            strBuilder.append(del);
        }
    }

    private String cryptToString(int crypt_val) {
        StringBuilder strBuilder = new StringBuilder();
        
        if (crypt_val == CryptoType.crypt_none.value()) {
            return new String("None");
        }

        if (crypt_val == CryptoType.crypt_unknown.value()) {
            return new String("Unknown");
        }

        if ((crypt_val & CryptoType.crypt_wps.value()) != 0) {
            strBuilder.append("WPS");
        }

        if ((crypt_val & CryptoType.crypt_protectmask.value()) == CryptoType.crypt_wep.value()) {
            addDeliminator(strBuilder, '/');
            return strBuilder.append("WEP").toString();
        }

        if ((crypt_val & CryptoType.crypt_version_wpa2.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("WPA2");
        } else if ((crypt_val & CryptoType.crypt_version_wpa.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("WPA");
        }

        if ((crypt_val & CryptoType.crypt_psk.value()) != 0) {
            addDeliminator(strBuilder, '-');
            strBuilder.append("PSK");
        }

        if ((crypt_val & CryptoType.crypt_eap.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("EAP");
        }

        if ((crypt_val & CryptoType.crypt_peap.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("WPA-PEAP");
        }

        if ((crypt_val & CryptoType.crypt_leap.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("WPA-LEAP");
        }

        if ((crypt_val & CryptoType.crypt_ttls.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("WPA-TTLS");
        }

        if ((crypt_val & CryptoType.crypt_tls.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("WPA-TLS");
        }

        if ((crypt_val & CryptoType.crypt_wpa_migmode.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("WPA-MIGRATION");
        }

        if ((crypt_val & CryptoType.crypt_wep40.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("WEP40");
        }

        if ((crypt_val & CryptoType.crypt_wep104.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("WEP104");
        }

        if ((crypt_val & CryptoType.crypt_tkip.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("TKIP");
        }

        if ((crypt_val & CryptoType.crypt_aes_ocb.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append(("AES-OCB"));
        }

        if ((crypt_val & CryptoType.crypt_aes_ccm.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("AES-CCMP");
        }

        if ((crypt_val & CryptoType.crypt_layer3.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("Layer 3");
        }

        if ((crypt_val & CryptoType.crypt_isakmp.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("ISA KMP");
        }

        if ((crypt_val & CryptoType.crypt_pptp.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("PPTP");
        }

        if ((crypt_val & CryptoType.crypt_fortress.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("Fortress");
        }

        if ((crypt_val & CryptoType.crypt_keyguard.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("Keyguard");
        }

        if ((crypt_val & CryptoType.crypt_unknown_protected.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("L3/Unknown");
        }

        if ((crypt_val & CryptoType.crypt_unknown_nonwep.value()) != 0) {
            addDeliminator(strBuilder, '/');
            strBuilder.append("Non-WEP/Unknown");
        }

        return strBuilder.toString();
    }

    // private String cryptToString(int crypt_val) {
    //     if (crypt_val == 0) {
    //         return CryptoMap.typeMap.get(0);
    //     }
    //     List<String> builder = new ArrayList<>();
    //     for (int tester = 1 << 28; tester > 0; tester >>>= 1) {
    //         if ((tester & crypt_val) > 0 &&
    //             CryptoMap.typeMap.containsKey(tester)) {

    //             builder.add(CryptoMap.typeMap.get(tester));
    //         }
    //     }
    //     return builder.stream().collect(Collectors.joining("/"));
    // }
    
    @FieldPath("dot11.device/dot11.device.advertised_ssid_map")
    @FieldAliase("dot11.device.advertised_ssid_map")
    public void setCryptType(Map<String, Map<String, Object>> ssid_map) {
        this.cryptType = "";
        if (ssid_map.containsKey(String.valueOf(checksum))) {
            Map<String, Object> inner_map = ssid_map.get(String.valueOf(checksum));
            if (inner_map.containsKey("dot11.advertisedssid.crypt_set")) {
                int crypt_val = (int) inner_map.get("dot11.advertisedssid.crypt_set");
                this.cryptType = cryptToString(crypt_val);
            }
        }
    }

    public void setCryptType(String cryptType) {
        this.cryptType = cryptType;
    }

    @FieldPath("dot11.device/dot11.device.last_beaconed_ssid_checksum")
    @FieldAliase("dot11.device.last_beaconed_ssid_checksum")
    public void setChecksum(long checksum) {
        this.checksum = checksum;
    }

    @FieldPath("kismet.device.base.macaddr")
    @FieldAliase("kismet.device.base.macaddr")
    public void setMac(String mac) {
        this.mac = mac;
    }

    @FieldPath("dot11.device/dot11.device.last_beaconed_ssid")
    @FieldAliase("dot11.device.last_beaconed_ssid")
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    @FieldPath("kismet.device.base.channel")
    @FieldAliase("kismet.device.base.channel")
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @FieldPath("kismet.device.base.packets.data")
    @FieldAliase("kismet.device.base.packets.data")
    public void setDataPackets(int dataPackets) {
        this.dataPackets = dataPackets;
    }
    
    @FieldPath("kismet.device.base.packets.crypt")
    @FieldAliase("kismet.device.base.packets.crypt")
    public void setEncryptedPackets(int encryptedPackets) {
        this.encryptedPackets = encryptedPackets;
    }

    @FieldPath("kismet.device.base.packets.llc")
    @FieldAliase("kismet.device.base.packets.llc")
    public void setLlcPackets(int llcPackets) {
        this.llcPackets = llcPackets;
    }
    
    @FieldPath("kismet.device.base.macaddr")
    @FieldAliase("kismet.device.base.macaddr")
    public void setManufacturer(String mac) {
        this.manufacturer = VendorUtil.getInstance().getOrganization(mac);
    }

    @FieldPath("kismet.device.base.first_time")
    @FieldAliase("kismet.device.base.first_time")
    public void setFirstTime(long firstTime) {
        this.firstTime = new Date(firstTime);
    }
    
    @FieldPath("kismet.device.base.last_time")
    @FieldAliase("kismet.device.base.last_time")
    public void setLastTime(long lastTime) {
        this.lastTime = new Date(lastTime);
    }

    @FieldPath("kismet.device.base.signal/kismet.common.signal.last_signal")
    @FieldAliase("kismet.common.signal.last_signal")
    public void setSignalDBM(int signalDBM) {
        this.signalDBM = signalDBM;
    }

    @FieldPath("kismet.device.base.signal/kismet.common.signal.min_signal")
    @FieldAliase("kismet.common.signal.min_signal")
    public void setSignalDBMMin(int signalDBMMin) {
        this.signalDBMMin = signalDBMMin;
    }
    
    @FieldPath("kismet.device.base.signal/kismet.common.signal.max_signal")
    @FieldAliase("kismet.common.signal.max_signal")
    public void setSignalDBMMax(int signalDBMMax) {
        this.signalDBMMax = signalDBMMax;
    }

    @FieldPath("kismet.device.base.signal/kismet.common.signal.last_noise")
    @FieldAliase("kismet.common.signal.last_noise")
    public void setNoiseDBM(int noiseDBM) {
        this.noiseDBM = noiseDBM;
    }
    
    @FieldPath("kismet.device.base.signal/kismet.common.signal.min_noise")
    @FieldAliase("kismet.common.signal.min_noise")
    public void setNoiseDBMMin(int noiseDBMMin) {
        this.noiseDBMMin = noiseDBMMin;
    }

    @FieldPath("kismet.device.base.signal/kismet.common.signal.max_noise")
    @FieldAliase("kismet.common.signal.max_noise")
    public void setNoiseDBMMax(int noiseDBMMax) {
        this.noiseDBMMax = noiseDBMMax;
    }

    @FieldPath("kismet.device.base.datasize")
    @FieldAliase("kismet.device.base.datasize")
    public void setDataBytes(long dataBytes) {
        this.dataBytes = dataBytes;
    }
    
    @FieldPath("kismet.device.base.commonname")
    @FieldAliase("kismet.device.base.commonname")
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "BSSIDMessage: {" + "mac:" + mac + ", ssid:" + ssid + ", crypt:" + cryptType + "}";
    }

    public String getCryptType() {
        return cryptType;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public long getDataBytes() {
        return dataBytes;
    }

    public int getNoiseDBMMax() {
        return noiseDBMMax;
    }

    public int getNoiseDBMMin() {
        return noiseDBMMin;
    }

    public int getNoiseDBM() {
        return noiseDBM;
    }

    public int getSignalDBMMax() {
        return signalDBMMax;
    }
    
    public int getSignalDBMMin() {
        return signalDBMMin;
    }

    public int getSignalDBM() {
        return signalDBM;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getLlcPackets() {
        return llcPackets;
    }

    public int getEncryptedPackets() {
        return encryptedPackets;
    }

    public int getDataPackets() {
        return dataPackets;
    }

    public String getChannel() {
        return channel;
    }
    
    public String getSsid() {
        return ssid;
    }

    public String getMac() {
        return mac;
    }

    public long getChecksum() {
        return checksum;
    }

}
