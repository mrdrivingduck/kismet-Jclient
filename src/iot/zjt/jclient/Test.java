package iot.zjt.jclient;

import iot.zjt.jclient.information.TimestampInformation;
import iot.zjt.jclient.util.HttpUtil;
import iot.zjt.jclient.util.UriGenerator;

public class Test {

	public static void main(String[] args) {
		
		String json = HttpUtil.doGet(UriGenerator.buildUri(
			"192.168.137.133", 2501, TimestampInformation.class)
		);
		
		System.out.println(json);
	}

}
