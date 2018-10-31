package iot.zjt.jclient.message;

public class WLClientMessage extends KismetMessage {
    
    private int type;
    private String bssid;
    private String mac;
    private String nickname;
    private String personname;
    private String telephone;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "WLCLIENTMessage{" + "BSSID=" + bssid + ", SSID=" + mac + ", NICKNAME=" + nickname + ", PERSONNAME=" + personname + '}';
    }
}
