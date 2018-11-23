package iot.zjt.jclient.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import iot.zjt.jclient.annotation.ApiUrl;
import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.annotation.FieldPath;
import iot.zjt.jclient.annotation.MessageType;
import iot.zjt.jclient.util.CryptoMap;
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

    private String cryptToString(int crypt_val) {
        if (crypt_val == 0) {
            return CryptoMap.typeMap.get(0);
        }
        List<String> builder = new ArrayList<>();
        for (int tester = 1 << 28; tester > 0; tester >>>= 1) {
            if ((tester & crypt_val) > 0 &&
                CryptoMap.typeMap.containsKey(tester)) {

                builder.add(CryptoMap.typeMap.get(tester));
            }
        }
        return builder.stream().collect(Collectors.joining("/"));
    }
    
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
        return "BSSIDMessage: {" + "mac:" + mac + ", ssid:" + ssid + ", manufactor:" + manufacturer + "}";
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
