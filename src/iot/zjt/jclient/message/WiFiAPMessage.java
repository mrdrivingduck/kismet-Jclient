package iot.zjt.jclient.message;

import iot.zjt.jclient.annotation.ApiUrl;
import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.annotation.FieldPath;
import iot.zjt.jclient.annotation.MessageType;

/**
 * @version 2018.10.30
 * @author Mr Dk.
 */

@MessageType("WIFIAP")
@ApiUrl("/devices/last-time/%d/devices.json")
public class WiFiAPMessage extends KismetMessage {

    private String macAddr;
    private String deviceKey;
    private String deviceName;

    @FieldPath("kismet.device.base.key")
    @FieldAliase("kismet.device.base.key")
    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    @FieldPath("kismet.device.base.name")
    @FieldAliase("kismet.device.base.name")
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @FieldPath("kismet.device.base.macaddr")
    @FieldAliase("kismet.device.base.macaddr")
    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getMacAddr() {
        return macAddr;
    }

    @Override
    public String toString() {
        return "Wi-Fi AP Message: {" + "deviceName:" + deviceName + ", macAddr:" + macAddr + ", deviceKey:" + deviceKey + "}";
    }
}
