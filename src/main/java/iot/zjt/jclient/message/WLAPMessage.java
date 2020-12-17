package iot.zjt.jclient.message;

public class WLAPMessage extends KismetMessage {
    
    private int type;
    private String bssid;
    private String ssid;
    private String nickname;
    private String location;
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


    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        return "WLAPMessage{" + "BSSID=" + bssid + ", SSID=" + ssid + ", NICKNAME=" + nickname + ", LOCATION=" + location +",TYPE=" + type + '}';
    }
}
