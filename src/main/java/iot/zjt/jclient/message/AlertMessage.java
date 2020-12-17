package iot.zjt.jclient.message;

import java.math.BigDecimal;
import java.util.Date;

import iot.zjt.jclient.annotation.ApiUrl;
import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.annotation.FieldPath;
import iot.zjt.jclient.annotation.MessageType;

@MessageType("ALERT")
@ApiUrl("/alerts/last-time/%d/alerts.json")
public class AlertMessage extends KismetMessage {

    private String source;
    private String destination;
    private String bssid;
    private Date firstTime;
    private Date lastTime;
    private String header;
    private String text;
    private String channel;

    @FieldPath("kismet.alert.source_mac")
    @FieldAliase("kismet.alert.source_mac")
    public void setSource(String source) {
        this.source = source;
    }

    @FieldPath("kismet.alert.dest_mac")
    @FieldAliase("kismet.alert.dest_mac")
    public void setDestination(String destination) {
        this.destination = destination;
    }

    @FieldPath("kismet.alert.transmitter_mac")
    @FieldAliase("kismet.alert.transmitter_mac")
    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    @FieldPath("kismet.alert.timestamp")
    @FieldAliase("kismet.alert.timestamp")
    public void setFirstTime(BigDecimal firstTime) {
        this.firstTime = new Date(firstTime.longValue());
    }

    @FieldPath("kismet.alert.timestamp")
    @FieldAliase("kismet.alert.timestamp")
    public void setLastTime(BigDecimal lastTime) {
        this.lastTime = new Date(lastTime.longValue());
    }

    @FieldPath("kismet.alert.header")
    @FieldAliase("kismet.alert.header")
    public void setHeader(String header) {
        this.header = header;
    }
    
    @FieldPath("kismet.alert.text")
    @FieldAliase("kismet.alert.text")
    public void setText(String text) {
        this.text = text;
    }
    
    @FieldPath("kismet.alert.channel")
    @FieldAliase("kismet.alert.channel")
    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    @Override
    public String toString() {
        return "AlertMessage: {" + "header:\"" + header + ", source:" + source + ", dest:" + destination + ", transmitter:" + bssid +  "\", text:\"" + text + "\"}";
    }

    public String getChannel() {
        return channel;
    }

    public String getText() {
        return text;
    }

    public String getHeader() {
        return header;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public String getBssid() {
        return bssid;
    }

    public String getDestination() {
        return destination;
    }
    
    public String getSource() {
        return source;
    }
}
