package com.cardbattlegame.connect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.os.Message;


/**
 * 对核心代码进行抽取，一共有四个公共的方法，分别是连接，断开连接，发送消息，接收消息
 * 
 * @author ZHY
 * 
 */
public class MyConnection {

	private String host = "";
	private int port = 8090;
	Socket client;
    private BufferedReader in = null;
    private PrintWriter out = null;
	private WaitThread waitThread;
	public boolean isWaiting;

	/**
	 * new出QQConnection对象的时候初始化IP地址和端口
	 * 
	 * @param host
	 * @param port
	 */
	public MyConnection(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	/**
	 * 创建与服务器之间的连接
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public void connect() throws UnknownHostException, IOException {
		// 创建连接
		client = new Socket(host, port);
		in = new BufferedReader(new InputStreamReader(client
	                .getInputStream()));
	    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
	    		client.getOutputStream())), true);
		// 创建连接的时候开启等待线程
		isWaiting = true;
		waitThread = new WaitThread();
		waitThread.start();

	}

	/**
	 * 断开与服务期间的连接
	 * 
	 * @throws IOException
	 */
	public void disConnect() throws IOException {
		// 关闭连接就是释放资源
		client.close();
		in.close();
		out.close();
		isWaiting = false;
	}

	/**
	 * 发送xml消息
	 * 
	 * @param xml
	 * @throws IOException
	 */
	public void sendMessage(String JSONstr) throws IOException {
		// 发送消息要用到输入输出流，将流作为类的成员变量，在创建连接的时候初始化，断开连接的时候释放资源
		// 发送消息其实就是把消息写出去
		out.println(JSONstr);

	}

	/**
	 * 发送java对象消息
	 * 
	 * @param xml
	 * @throws IOException
	 */
	public void sendMessage(Message msg) throws IOException {
		out.println(msg.obj.toString());
	}

	/**
	 * 等待线程 接收消息,由于不知道消息什么时候到达，需要一直等待着，等待消息的到达
	 * 
	 * @author ZHY
	 * 
	 */
	private class WaitThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (isWaiting) {
				// 接收消息其实就是将消息读取到
				try {
			          	StringBuffer sb = new StringBuffer();
			        	String line = in.readLine();
			        	while(line != null){
			        		sb.append(line);
			       			sb.append("\n");
			       			line = in.readLine();
			       		}
			       		Message msg = new Message();
			       		msg.what = 0;
			       		msg.obj = sb == null?"":sb.toString();
					/*
					 * 接收到消息之后，依次调用每个监听器的onReceive方法
					 */
					for (OnMessageListener listener : listeners) {
						listener.onReveive(msg);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
	}

	// 服务器会经常给客户端发送消息，客户端会有不同的消息到来，所以新建一个监听器的集合，往集合中添加一个监听器就调用一次onReveive方法，
	/*
	 * 集合中有就调用，集合中没有就不调用
	 */
	private List<OnMessageListener> listeners = new ArrayList<OnMessageListener>();

	public void addOnMessageListener(OnMessageListener listener) {
		listeners.add(listener);
	}

	public void removeOnMessageListener(OnMessageListener listener) {
		listeners.remove(listener);
	}

	/**
	 * 消息的监听器接口，当有消息到来的时候就调用一次onReceive方法
	 * 
	 * @author ZHY
	 * 
	 */
	public static interface OnMessageListener {
		public void onReveive(Message msg);
	}
}
