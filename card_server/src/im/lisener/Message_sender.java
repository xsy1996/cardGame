package im.lisener;

import java.io.IOException;

import org.json.JSONObject;

import im.core.Connection;


public class Message_sender {

	public void toClient(JSONObject msg, Connection conn) throws IOException {
		System.out.println("单发当前客户端to Client \n" + msg.toString());
		if (conn != null) {
			conn.out.write(msg.toString());
		}
	}

}
