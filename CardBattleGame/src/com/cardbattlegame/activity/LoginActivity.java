package com.cardbattlegame.activity;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cardbattlegame.connect.MyConnection;
import com.cardbattlegame.connect.MyConnection.OnMessageListener;
import com.cardbattlegame.domain.Hero;
import com.cardbattlegame.util.ThreadUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText username;
	private EditText password;
	private Button btnLogin;
	private Button btnRegister;
	
	//��������
	MyConnection mc;
	
	//�����JSON�ַ���
	protected String jsonStr;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		btnRegister = (Button)findViewById(R.id.RegisterBtn);
		btnLogin = (Button)findViewById(R.id.loginBtn);

		
		//ȫ����ʾ�����ر�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//����״̬��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//���ֺ���
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		ThreadUtils.runInSubThread(new Runnable() {

			public void run() {
				try {
					mc = new MyConnection("192.168.0.148", 8090);// Socket
					mc.connect();// ��������
					// ��������֮�󣬽���������ӵ���������
					mc.addOnMessageListener(listener);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		
		btnLogin.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO ��¼
				username = (EditText)findViewById(R.id.accountEt);
				password = (EditText)findViewById(R.id.pwdEt);
				JSONArray jsonarray = new JSONArray();
				try {
					jsonarray.put(0, username.getText().toString().trim());
					jsonarray.put(1, password.getText().toString().trim());
					JSONObject param = new JSONObject();
					param.put("actioncode", 1);//actioncode:1��ʾ��¼
					param.put("data", jsonarray);//data,������data[0]�û���,data[1]����
					final String JSONstr = param.toString();
					ThreadUtils.runInSubThread(new Runnable() {
						public void run() {
							try {
								mc.sendMessage(JSONstr);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		
		btnRegister.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO ע��,��ûд,�ݵ��˳�
				System.exit(0);//�˳�
			}
		});
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity���ٵ�ʱ��ȡ������
		mc.removeOnMessageListener(listener);
	}

	
	
	
	
	protected void handleResult(String jsonStr){
		try {
			JSONObject json = new JSONObject(jsonStr);
			String loginflag;
			String type;
			type = json.getString("type");
			loginflag = json.getString("success");
			if(loginflag.equals("true") && type.equals("1")){
				Toast.makeText(LoginActivity.this, "��½�ɹ�", Toast.LENGTH_SHORT).show();
				Hero hero = (Hero) getApplication();
				hero.setName(json.getString("name"));  
				hero.setLevel(json.getString("level"));
				hero.setRank(json.getString("rank"));
				Intent it = new Intent(LoginActivity.this,
					MenuActivity.class);
				it.putExtra("name", hero.getName());
				it.putExtra("level", hero.getLevel());
				it.putExtra("rank", hero.getRank());
				startActivity(it);//�ɹ���ת����Ϸ��
				finish();
			}
			else{
				Toast.makeText(LoginActivity.this, "��¼ʧ��", Toast.LENGTH_SHORT).show();
			    System.out.print("��¼ʧ��");	
			}
		} catch (Exception e) {
			Toast.makeText(LoginActivity.this, "����JSONʧ��", Toast.LENGTH_SHORT).show();
		}
	
	}
	
	// ʹ�ý�����Ϣ�ļ�����
		private OnMessageListener listener = new OnMessageListener() {

			public void onReveive(final Message msg) {
				System.out.println((String)msg.obj);
				// ���շ��������صĽ��.�������ݵ���ʾ,���������߳���
				ThreadUtils.runInUiThread(new Runnable() {
					public void run() {
						switch(msg.what){
						case 0://TODO ����JSON�ַ���
							//����,��������������
								handleResult((String) msg.obj);
							break;
						case -1:
							break;
						}
					}
				});

			}
		};
}

