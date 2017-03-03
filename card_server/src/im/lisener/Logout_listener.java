package im.lisener;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;


import im.core.Connection;
import im.core.Connection.OnRecevieMsgListener;
import im.core.Connection_manager;

public  class Logout_listener extends Message_sender implements OnRecevieMsgListener {
	public Logout_listener() {
		super();
	}

	public void onReceive(JSONObject fromuser) {
		try {
			if("4".equals(fromuser.getString("actioncode"))){
				JSONObject touser=new JSONObject();
				touser.put("state","success");
				Connection conn=Connection_manager.get(fromuser.getString("account"));
				Connection_manager.remove(fromuser.getString("account"));
				try {
					toClient(touser,conn);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
}
