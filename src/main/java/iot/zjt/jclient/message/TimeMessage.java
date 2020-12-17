package iot.zjt.jclient.message;

import iot.zjt.jclient.annotation.ApiUrl;
import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.annotation.FieldPath;
import iot.zjt.jclient.annotation.MessageType;

/**
 * @version 2018.10.30
 * @author Mr Dk.
 */

@MessageType("TIMESTAMP")
@ApiUrl("/system/timestamp.json")
public class TimeMessage extends KismetMessage {

    private long sec;
    private long usec;

    @FieldPath("kismet.system.timestamp.sec")
    @FieldAliase("kismet.system.timestamp.sec")
    public void setSec(long sec) {
        this.sec = sec;
    }

    @FieldPath("kismet.system.timestamp.usec")
    @FieldAliase("kismet.system.timestamp.usec")
    public void setUsec(long usec) {
        this.usec = usec;
    }

    public long getSec() {
        return sec;
    }

    public long getUsec() {
        return usec;
    }

    @Override
    public String toString() {
        return "TimeMessage: {" + "sec=" + sec + ", usec=" + usec + "}";
    }
}
