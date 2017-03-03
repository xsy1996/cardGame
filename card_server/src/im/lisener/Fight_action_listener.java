package im.lisener;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import im.core.Connection;
import im.core.Connection.OnRecevieMsgListener;
import im.core.Connection_manager;
import im.domain.User;
import im.main.game_im_server;

public class Fight_action_listener extends Message_sender implements OnRecevieMsgListener {

	public Fight_action_listener(){
		super();
	}
	public void onReceive(JSONObject fromuser) {
		
		try {
			if("3".equals(fromuser.getString("actioncode"))){
				Connection conn;
				JSONObject touser;
				User player1=Connection_manager.get_player(fromuser.getString("account")),player2=Connection_manager.get_player(game_im_server.wait_player);
				player2.role.HP-=fromuser.getInt("Card")*20;
				if(player2.role.HP<=0){
					conn=Connection_manager.get(player1.account);
					touser=new JSONObject();
					touser.put("battletype","3");
					touser.put("result","win");
					toClient(touser,conn);
					conn=Connection_manager.get(player2.account);
					touser=new JSONObject();
					touser.put("battletype","3");
					touser.put("result","fail");
					toClient(touser,conn);
					Connection_manager.remove_room(player1.account,player2.account);
				}else{
					conn=Connection_manager.get(player1.account);
					touser=new JSONObject();
					touser.put("battletype","1");
					touser.put("competitor_state1",player2.role.HP);
					touser.put("competitor_state2",player2.role.MP);
					touser.put("state1",player1.role.HP);
					touser.put("state2",player1.role.MP);
					toClient(touser,conn);
					conn=Connection_manager.get(player2.account);
					touser=new JSONObject();
					touser.put("battletype","2");
					touser.put("competitor_state1",player1.role.HP);
					touser.put("competitor_state2",player1.role.MP);
					touser.put("state1",player2.role.HP);
					touser.put("state2",player2.role.MP);
					toClient(touser,conn);
					
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
