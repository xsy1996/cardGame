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
			// �١�����һ���߳� �������ͻ��˵�����
			final ServerSocket server = new ServerSocket(8020);
			System.out.println("---����������---" + new Date().toString());
			new Thread() {
				public void run() {
					while (true) {
						Connection conn = null;
						try {
							Socket player = server.accept();
							System.out.println("---�пͻ��˽���---" + player);
							// �ڡ�����ͻ������ӳɹ�������һ���߳�
							conn = new Connection(player);
							conn.addOnRecevieMsgListener(new Login_msg_listener(conn));
							conn.addOnRecevieMsgListener(new Logout_listener());
							conn.addOnRecevieMsgListener(new Apply_room_listener());
							conn.addOnRecevieMsgListener(new Fight_action_listener());
							// �ۡ����߳��ڵȴ��û�����
							conn.connect();
							// �ܡ�����һ���̸߳��ͻ���
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
