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
	
	//网络链接
	MyConnection mc;
	
	//请求的JSON字符串
	protected String jsonStr;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		btnRegister = (Button)findViewById(R.id.RegisterBtn);
		btnLogin = (Button)findViewById(R.id.loginBtn);

		
		//全屏显示，隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//保持横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		ThreadUtils.runInSubThread(new Runnable() {

			public void run() {
				try {
					mc = new MyConnection("192.168.0.148", 8090);// Socket
					mc.connect();// 建立连接
					// 建立连接之后，将监听器添加到连接里面
					mc.addOnMessageListener(listener);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		
		btnLogin.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO 登录
				username = (EditText)findViewById(R.id.accountEt);
				password = (EditText)findViewById(R.id.pwdEt);
				JSONArray jsonarray = new JSONArray();
				try {
					jsonarray.put(0, username.getText().toString().trim());
					jsonarray.put(1, password.getText().toString().trim());
					JSONObject param = new JSONObject();
					param.put("actioncode", 1);//actioncode:1表示登录
					param.put("data", jsonarray);//data,里面是data[0]用户名,data[1]密码
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
				// TODO 注册,还没写,暂当退出
				System.exit(0);//退出
			}
		});
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity销毁的时候取消监听
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
				Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
				Hero hero = (Hero) getApplication();
				hero.setName(json.getString("name"));  
				hero.setLevel(json.getString("level"));
				hero.setRank(json.getString("rank"));
				Intent it = new Intent(LoginActivity.this,
					MenuActivity.class);
				it.putExtra("name", hero.getName());
				it.putExtra("level", hero.getLevel());
				it.putExtra("rank", hero.getRank());
				startActivity(it);//成功则转到游戏中
				finish();
			}
			else{
				Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
			    System.out.print("登录失败");	
			}
		} catch (Exception e) {
			Toast.makeText(LoginActivity.this, "解析JSON失败", Toast.LENGTH_SHORT).show();
		}
	
	}
	
	// 使用接收消息的监听器
		private OnMessageListener listener = new OnMessageListener() {

			public void onReveive(final Message msg) {
				System.out.println((String)msg.obj);
				// 接收服务器返回的结果.处理数据的显示,运行在主线程中
				ThreadUtils.runInUiThread(new Runnable() {
					public void run() {
						switch(msg.what){
						case 0://TODO 接受JSON字符串
							//解码,具体问题具体分析
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

