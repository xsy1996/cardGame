package im.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import im.core.Connection;
import im.lisener.Apply_room_listener;
import im.lisener.Fight_action_listener;
import im.lisener.Login_msg_listener;
import im.lisener.Logout_listener;


public class game_im_server {
	public static String wait_player="$$";
	public static void main(String[] args){
		try {
			// ①　创建一个线程 等其他客户端的连接
			final ServerSocket server = new ServerSocket(8020);
			System.out.println("---服务器启动---" + new Date().toString());
			new Thread() {
				public void run() {
					while (true) {
						Connection conn = null;
						try {
							Socket player = server.accept();
							System.out.println("---有客户端接入---" + player);
							// ②　如果客户端连接成功分配置一个线程
							conn = new Connection(player);
							conn.addOnRecevieMsgListener(new Login_msg_listener(conn));
							conn.addOnRecevieMsgListener(new Logout_listener());
							conn.addOnRecevieMsgListener(new Apply_room_listener());
							conn.addOnRecevieMsgListener(new Fight_action_listener());
							// ③　该线程内等待用户数据
							conn.connect();
							// ④　分配一个线程给客户端
						} catch (IOException e) {
							e.printStackTrace();
							conn.disconnect();
						}
					}
				};
			}.start();

		} catch (Exception e) {//
			e.printStackTrace();
		}
	} 
}
