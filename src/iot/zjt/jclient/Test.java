package iot.zjt.jclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import iot.zjt.jclient.information.KismetInfo;
import iot.zjt.jclient.information.TimestampInfo;

public class Test {

	public static void main(String[] args) {

		JClientConnector conn = new JClientConnector("192.168.137.133", 2501);

		JClientListener listener = new JClientListener(){
		
			@Override
			public void OnTerminate(String reason) {
				System.out.println(reason);
			}
		
			@Override
			public void OnInformation(KismetInfo info) {
				System.out.println(info);
			}
		};

		listener.subscribe(TimestampInfo.class);
		conn.register(listener);
	}

}
