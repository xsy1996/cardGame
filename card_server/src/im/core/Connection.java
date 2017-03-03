package im.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import im.domain.User;



public class Connection extends Thread{
	private Socket skt = null;
    public BufferedReader in = null;
    public PrintWriter out = null;
	private boolean flag;
	public String ip;
	public int port;
	public User user = null;
	
	public Connection(Socket skt) {
		super();
		try {
			this.skt = skt;
			out = new PrintWriter(this.skt.getOutputStream());
			in = new BufferedReader(new InputStreamReader(this.skt
	                .getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Connection(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
		init(ip, port);
	}

	private void init(String ip, int port) {
		try {
			this.skt = new Socket(ip, port);
			out = new PrintWriter(this.skt.getOutputStream());
			in = new BufferedReader(new InputStreamReader(this.skt
	                .getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void connect() {
		if (this.skt == null) {
			init(ip, port);
		}
		flag = true;
		start();
	}
	
	public void disconnect() {
		try {
			flag = false;
			out.close();
			in.close();
			// stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ----------监听器------
		// 1.申明监听器与响应方法
		public static interface OnRecevieMsgListener {
			public void onReceive(JSONObject msg);
		}

		// 2.支持多个监听器
		private List<OnRecevieMsgListener> listeners = new ArrayList<OnRecevieMsgListener>();

		// 3.添加监听器
		public void addOnRecevieMsgListener(OnRecevieMsgListener listener) {
			listeners.add(listener);
		}

		// 4.删除监听器
		public void removeOnRecevieMsgListener(OnRecevieMsgListener listener) {
			listeners.remove(listener);
		}
		
		@Override
		public void run() {
			super.run();
			// 等待 数据
			while (flag) {
				try {
					String  json= in.readLine();
					System.out.println(json);
					if (json != null && !"".equals(json)) {
						JSONObject msg = new JSONObject(json);
						for (OnRecevieMsgListener l : listeners) {
							l.onReceive(msg);
						}
					}
				} catch (EOFException e) {
//					e.printStackTrace();
					System.out.println("=-=EOFException---");
					if (user != null) {
						Connection_manager.remove(user.account);
					}
					disconnect();
				} catch (Exception e) {
					e.printStackTrace();
					if (user != null) {
						Connection_manager.remove(user.account);
					}
					disconnect();
				}
			}

		}
}
