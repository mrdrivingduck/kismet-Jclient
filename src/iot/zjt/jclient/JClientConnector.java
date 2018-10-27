package iot.zjt.jclient;

import com.alibaba.fastjson.JSON;

import iot.zjt.jclient.information.TimestampInfo;
import iot.zjt.jclient.util.HttpRequestGenerator;
import iot.zjt.jclient.util.InfoGenerator;
import iot.zjt.jclient.util.UriGenerator;

public class JClientConnector {

    // private final String host;
    // private final int port;

    public JClientConnector(String host, int port) {
        // this.host = host;
        // this.port = port;

        String json = HttpRequestGenerator.doGet(
            UriGenerator.buildUri(
                host, port, TimestampInfo.class
            )
        );
        
        System.out.println(InfoGenerator.generateInfo(JSON.parseObject(json), TimestampInfo.class));
    }

}
