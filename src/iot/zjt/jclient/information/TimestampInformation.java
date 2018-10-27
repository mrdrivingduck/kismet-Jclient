package iot.zjt.jclient.information;

import iot.zjt.jclient.annotation.ApiUrl;
import iot.zjt.jclient.annotation.FieldSetter;
import iot.zjt.jclient.annotation.Information;

@Information("TIMESTAMP")
@ApiUrl("/system/timestamp.json")
public class TimestampInformation extends KismetInformation {

    long sec;
    long usec;

    @FieldSetter("kismet.system.timestamp.sec")
    public void setSec(long sec) {
        this.sec = sec;
    }

    @FieldSetter("kismet.system.timestamp.usec")
    public void setUsec(long usec) {
        this.usec = usec;
    }

    public long getSec() {
        return sec;
    }

    public long getUsec() {
        return usec;
    }

}
