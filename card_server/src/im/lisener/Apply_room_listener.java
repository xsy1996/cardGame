package im.lisener;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import im.core.Connection;
import im.core.Connection.OnRecevieMsgListener;
import im.core.Connection_manager;
import im.domain.DB;
import im.domain.Player;
import im.domain.User;
import im.main.game_im_server;

public class Apply_room_listener extends Message_sender implements OnRecevieMsgListener {
	
	public Apply_room_listener() {
		super();
	}

	public void onReceive(JSONObject fromuser) {
		try {
			if("2".equals(fromuser.getString("actioncode"))){
				if(game_im_server.wait_player.equals("$$")){
					game_im_server.wait_player=fromuser.getString("account");
					
				}else{
					User player1=Connection_manager.get_player(fromuser.getString("account")),player2=Connection_manager.get_player(game_im_server.wait_player);
					player1.role=new Player();
					player2.role=new Player();
					Connection_manager.put_enemy(player1.account,player2.account);
					Connection conn=Connection_manager.get(player1.account);
					JSONObject touser = new JSONObject();
					touser.put("find","ture");
					touser.put("competitor_name",player2.name);
					touser.put("competitor_state1",player2.role.HP);
					touser.put("competitor_state2",player2.role.MP);
					touser.put("state1",player1.role.HP);
					touser.put("state1",player1.role.MP);
					touser.put("Card1",1);
					touser.put("Card1",2);
					touser.put("Card1",3);
					touser.put("Card1",4);
					try {
						toClient(touser,conn);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					conn=Connection_manager.get(player2.account);
					touser = new JSONObject();
					touser.put("find","ture");
					touser.put("competitor_name",player1.name);
					touser.put("competitor_state1",player1.role.HP);
					touser.put("competitor_state2",player1.role.MP);
					touser.put("state1",player2.role.HP);
					touser.put("state1",player2.role.MP);
					touser.put("Card1",1);
					touser.put("Card1",2);
					touser.put("Card1",3);
					touser.put("Card1",4);
					try {
						toClient(touser,conn);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
	}
}

