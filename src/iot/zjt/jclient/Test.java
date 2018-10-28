package iot.zjt.jclient;

import iot.zjt.jclient.information.KismetInfo;
import iot.zjt.jclient.information.MessageInfo;
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

		// listener.subscribe(TimestampInfo.class);
		// listener.subscribe(MessageInfo.class);
		conn.register(listener);
	}

}
