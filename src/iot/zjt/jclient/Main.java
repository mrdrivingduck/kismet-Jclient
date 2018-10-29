package iot.zjt.jclient;

import iot.zjt.jclient.information.KismetInfo;
import iot.zjt.jclient.information.WiFiAPInfo;

public class Main {

	public static void main(String[] args) {

		JClientConnector conn = new JClientConnector("192.168.25.130", 2501);

		JClientListener listener = new JClientListener(){
		
			@Override
			public void onTerminate(String reason) {
				System.out.println(reason);
			}
		
			@Override
			public void onInformation(KismetInfo info) {
				System.out.println(info);
			}
		};

		// listener.subscribe(TimestampInfo.class);
		// listener.subscribe(MessageInfo.class);
		listener.subscribe(WiFiAPInfo.class);
		conn.register(listener);
	}

}
