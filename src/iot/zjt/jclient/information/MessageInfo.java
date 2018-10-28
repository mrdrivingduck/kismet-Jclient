package iot.zjt.jclient.information;

import iot.zjt.jclient.annotation.ApiUrl;
import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.annotation.FieldPath;
import iot.zjt.jclient.annotation.InfoType;

/**
 * @version 2018.10.28
 * @author Mr Dk.
 */

@InfoType("MESSAGE")
@ApiUrl("/messagebus/last-time/%d/messages.json")
public class MessageInfo extends KismetInfo {

    private String content;
    private int flags;
    private long time;

    @FieldPath("kismet.messagebus.message_string")
    @FieldAliase("kismet.messagebus.message_string")
    public void setContent(String content) {
        this.content = content;
    }

    @FieldPath("kismet.messagebus.message_flags")
    @FieldAliase("kismet.messagebus.message_flags")
    public void setFlags(int flags) {
        this.flags = flags;
    }

    @FieldPath("kismet.messagebus.message_time")
    @FieldAliase("kismet.messagebus.message_time")
    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public int getFlags() {
        return flags;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "MessageInfo: {" + "time=" + time + ", content=\"" + content + "\", flags=" + flags + "}";
    }
}
