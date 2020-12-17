package iot.zjt.jclient;

import io.vertx.core.Vertx;
import iot.zjt.jclient.message.AlertMessage;
import iot.zjt.jclient.message.BSSIDMessage;
import iot.zjt.jclient.message.ClientMessage;
import iot.zjt.jclient.message.KismetMessage;
import iot.zjt.jclient.message.MsgMessage;
import iot.zjt.jclient.message.TimeMessage;

public class Main {

	public static void main(String[] args) {

		JClientConnector conn = new JClientConnector("192.168.2.106", 2501, Vertx.vertx(), 3000);

		JClientListener listener = new JClientListener(){
		
			@Override
			public void onTerminate(String reason) {
				System.out.println(reason);
			}
		
			@Override
			public void onMessage(KismetMessage msg) {
				System.out.println(msg);
			}
		};

		listener.subscribe(TimeMessage.class);
		listener.subscribe(MsgMessage.class);
		listener.subscribe(BSSIDMessage.class);
		listener.subscribe(ClientMessage.class);
		listener.subscribe(AlertMessage.class);
		conn.register(listener);
	}

}
