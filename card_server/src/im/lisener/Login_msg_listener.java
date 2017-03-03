package im.lisener;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import im.core.Connection;
import im.core.Connection_manager;
import im.core.Connection.OnRecevieMsgListener;
import im.domain.DB;
import im.domain.User;


public class Login_msg_listener extends Message_sender implements OnRecevieMsgListener {
	private Connection conn = null;

	public Login_msg_listener(Connection conn) {
		super();
		this.conn = conn;
	}
	public void onReceive(JSONObject fromuser) {
		try {
			if ("1".equals(fromuser.getString("actioncode"))) {
					JSONObject touser = new JSONObject();
					User user=Connection_manager.get_player(fromuser.getString("account"));
					if(user!=null && user.password.equals(fromuser.getString("password"))){
						touser.put("state", "success");
						touser.put("name", user.name);
						touser.put("rank",user.rank);
						touser.put("level", user.level);
						conn.user=user;
						Connection_manager.put(user.account, conn);
					}
					else{
						touser.put("state", "failure");
					}
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
