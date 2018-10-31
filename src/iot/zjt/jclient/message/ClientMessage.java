package iot.zjt.jclient.message;

import java.util.Date;

import iot.zjt.jclient.annotation.ApiUrl;
import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.annotation.FieldPath;
import iot.zjt.jclient.annotation.MessageType;

@MessageType("CLIENT")
@ApiUrl("/devices/last-time/%d/devices.json")
public class ClientMessage extends KismetMessage {

    private String bssid;
    private String mac;
    private Date lastTime;
    private Date firstTime;
    private int signalDBM;
    private int signalDBMMin;
    private int signalDBMMax;
    private int noiseDBM;
    private int noiseDBMMin;
    private int noiseDBMMax;
    private String nickname;
    private int llcPackets;
    private int dataPackets;
    private int encryptedPackets;
    private long dataSize;

    @FieldPath("kismet.device.base.macaddr")
    @FieldAliase("kismet.device.base.macaddr")
    public void setMac(String mac) {
        this.mac = mac;
    }

    @FieldPath("kismet.device.base.macaddr")
    @FieldAliase("kismet.device.base.macaddr")
    public void setBssid(String bssid) {
        this.bssid = bssid;
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
    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }
    
    @FieldPath("kismet.device.base.commonname")
    @FieldAliase("kismet.device.base.commonname")
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public long getDataSize() {
        return dataSize;
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

    public int getLlcPackets() {
        return llcPackets;
    }

    public int getEncryptedPackets() {
        return encryptedPackets;
    }

    public int getDataPackets() {
        return dataPackets;
    }

    public String getBssid() {
        return bssid;
    }

    public String getMac() {
        return mac;
    }

}
