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
 * �Ժ��Ĵ�����г�ȡ��һ�����ĸ������ķ������ֱ������ӣ��Ͽ����ӣ�������Ϣ��������Ϣ
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
	 * new��QQConnection�����ʱ���ʼ��IP��ַ�Ͷ˿�
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
	 * �����������֮�������
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public void connect() throws UnknownHostException, IOException {
		// ��������
		client = new Socket(host, port);
		in = new BufferedReader(new InputStreamReader(client
	                .getInputStream()));
	    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
	    		client.getOutputStream())), true);
		// �������ӵ�ʱ�����ȴ��߳�
		isWaiting = true;
		waitThread = new WaitThread();
		waitThread.start();

	}

	/**
	 * �Ͽ�������ڼ������
	 * 
	 * @throws IOException
	 */
	public void disConnect() throws IOException {
		// �ر����Ӿ����ͷ���Դ
		client.close();
		in.close();
		out.close();
		isWaiting = false;
	}

	/**
	 * ����xml��Ϣ
	 * 
	 * @param xml
	 * @throws IOException
	 */
	public void sendMessage(String JSONstr) throws IOException {
		// ������ϢҪ�õ������������������Ϊ��ĳ�Ա�������ڴ������ӵ�ʱ���ʼ�����Ͽ����ӵ�ʱ���ͷ���Դ
		// ������Ϣ��ʵ���ǰ���Ϣд��ȥ
		out.println(JSONstr);

	}

	/**
	 * ����java������Ϣ
	 * 
	 * @param xml
	 * @throws IOException
	 */
	public void sendMessage(Message msg) throws IOException {
		out.println(msg.obj.toString());
	}

	/**
	 * �ȴ��߳� ������Ϣ,���ڲ�֪����Ϣʲôʱ�򵽴��Ҫһֱ�ȴ��ţ��ȴ���Ϣ�ĵ���
	 * 
	 * @author ZHY
	 * 
	 */
	private class WaitThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (isWaiting) {
				// ������Ϣ��ʵ���ǽ���Ϣ��ȡ��
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
					 * ���յ���Ϣ֮�����ε���ÿ����������onReceive����
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

	// �������ᾭ�����ͻ��˷�����Ϣ���ͻ��˻��в�ͬ����Ϣ�����������½�һ���������ļ��ϣ������������һ���������͵���һ��onReveive������
	/*
	 * �������о͵��ã�������û�оͲ�����
	 */
	private List<OnMessageListener> listeners = new ArrayList<OnMessageListener>();

	public void addOnMessageListener(OnMessageListener listener) {
		listeners.add(listener);
	}

	public void removeOnMessageListener(OnMessageListener listener) {
		listeners.remove(listener);
	}

	/**
	 * ��Ϣ�ļ������ӿڣ�������Ϣ������ʱ��͵���һ��onReceive����
	 * 
	 * @author ZHY
	 * 
	 */
	public static interface OnMessageListener {
		public void onReveive(Message msg);
	}
}
