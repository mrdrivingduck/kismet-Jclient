package iot.zjt.jclient.information;

public class MessageInfo extends KismetInfo {

    private String content;
    private int flags;
    private long time;


    public void setContent(String content) {
        this.content = content;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

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
}
