package com.cardbattlegame.activity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.cardbattlegame.activity.AppActivity.NetThread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public abstract class AppActivity extends Activity {
	//��־���
	protected String TAG;
	
	//���紦���߳�
	protected NetThread thread;
	
	//�����JSON�ַ���
	protected String jsonStr;
	
	//����������ű��ļ�
	protected String url;
	
	//Ŀ��URL
	final String SERVICE = "login.php";  
	
	//������Ϣ
	private Handler handler = new MyHandler();
	
	//����
	protected abstract void handleResult(String jsonStr);
	
	
	@SuppressWarnings("deprecation")
	protected void requestURL(String json, String url){
		this.url = url;
		this.jsonStr = json;
		thread = new NetThread();
		thread.start();
		showDialog(1);
	}
	
	@SuppressWarnings("deprecation")
	protected void release(){
		dismissDialog(1);
		try{
			thread.join();
		} catch(InterruptedException e){
			
		}
		;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("��Ⱥ�");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		return dialog;
	}
	
	class NetThread extends Thread{
		@Override
		public void run(){
			HttpClient httpclient= new DefaultHttpClient();
			//post����
			HttpPost httppost = new HttpPost("http://www.baidu.com");//��������ַ
			try{
				
				//������
				StringEntity se = new StringEntity(jsonStr, "utf-8");
				httppost.setEntity(se);
				//get����
				//HttpGet httpget = new HttpGet(eText.getText().toString);
				//HttpResponse response = httpclient.execute(httpget);
				
				
				//Socket��ʱ����
				httpclient.getParams().setIntParameter(
						HttpConnectionParams.SO_TIMEOUT, 10000);
				
				//���ӳ�ʱ����
				httpclient.getParams().setIntParameter(
						HttpConnectionParams.CONNECTION_TIMEOUT, 10000);
				
				
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entityOut = response.getEntity();
				if(entityOut != null){
					BufferedReader br = new BufferedReader(
							new InputStreamReader(entityOut.getContent(),"utf-8"));
					StringBuffer sb = new StringBuffer();
					String line = br.readLine();
					while(line != null){
						sb.append(line);
						sb.append("\n");
						line = br.readLine();
					}
					br.close();
					Log.i(TAG, sb.toString());
					//�ɹ��յ���Ϣ
					Message msg = new Message();
					msg.what = 0;
					msg.obj = sb == null?"":sb.toString();
					handler.sendMessage(msg);
				}
			} catch(Exception e){
				e.printStackTrace();
				handler.sendEmptyMessage(-1);//-1ʧ��
			}
		}

	}


	class MyHandler extends Handler {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch(msg.what){
			case 0://TODO ����JSON�ַ���
				//����,��������������
					handleResult((String) msg.obj);
				break;
			case -1:
				break;
			}
			try{
				thread.join();
			} catch (InterruptedException e){
				
			}
		}
	}
}
