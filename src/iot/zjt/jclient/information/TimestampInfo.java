package iot.zjt.jclient.information;

import iot.zjt.jclient.annotation.ApiUrl;
import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.annotation.FieldPath;
import iot.zjt.jclient.annotation.InfoType;

/**
 * @version 2018.10.27
 * @author Mr Dk.
 */

@InfoType("TIMESTAMP")
@ApiUrl("/system/timestamp.json")
public class TimestampInfo extends KismetInfo {

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
        return "TimestampInfo: {" + "sec=" + sec + ", usec=" + usec + "}";
    }
}
