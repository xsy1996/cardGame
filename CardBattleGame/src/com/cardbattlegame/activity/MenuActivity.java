package com.cardbattlegame.activity;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity {

	private Button battle;
	private Button guide;//不然先退出？logout
	private Hero hero;
	private TextView name;
	private TextView level;
	private TextView rank;
	

	protected void handleResult(String jsonStr) {
		try {
			JSONObject json = new JSONObject(jsonStr);
			String flag;
			String type;
			type = json.getString("type");
			flag = json.getString("success");
			if(flag.equals("true") && type.equals("2")){
				if(type.equals("2"))
				Toast.makeText(MenuActivity.this, "发起对战成功", Toast.LENGTH_SHORT).show();
				Intent it = new Intent(MenuActivity.this,
					GameActivity.class);
				it.putExtra("competitor_name", json.getString("competitor_name"));
				it.putExtra("competitor_state1", json.getString("competitor_state1"));
				it.putExtra("competitor_state2", json.getString("competitor_state2"));
				it.putExtra("state1", json.getString("state1"));
				it.putExtra("state2", json.getString("state2"));
				it.putExtra("Card1", json.getString("Card1"));
				it.putExtra("Card2", json.getString("Card2"));
				it.putExtra("Card3", json.getString("Card3"));
				it.putExtra("Card4", json.getString("Card4"));
				startActivity(it);//成功则转到游戏中
			}
			else{
				Toast.makeText(MenuActivity.this, "无人对战", Toast.LENGTH_SHORT).show();
			    System.out.print("无人对战");	
			}
		} catch (Exception e) {
			Toast.makeText(MenuActivity.this, "解析JSON失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		name = (TextView)findViewById(R.id.name);
		level = (TextView)findViewById(R.id.level);
		rank =(TextView)findViewById(R.id.rank);
		battle = (Button)findViewById(R.id.battle);
		guide = (Button)findViewById(R.id.guide);
		
		Bundle bundle = this.getIntent().getExtras(); 
		name.setText(name.getText().toString().trim()+bundle.getString("name"));
		level.setText(level.getText().toString().trim()+bundle.getString("level"));
		rank.setText(rank.getText().toString().trim()+bundle.getString("rank"));		
		
		
		hero = (Hero) getApplication();
		// 开启监听
		hero.getMyConn().addOnMessageListener(listener);
		//全屏显示，隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//保持横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		battle.setOnClickListener(myclicklistener);
		guide.setOnClickListener(myclicklistener);
	}
	
	private View.OnClickListener myclicklistener = new View.OnClickListener(){
		@Override
		public void onClick(View v){
			switch(v.getId()){
				case R.id.battle:
					Toast.makeText(MenuActivity.this, "发起对战", Toast.LENGTH_SHORT).show();
					JSONArray jsonarray = new JSONArray();
					try {
						jsonarray.put(0, name.getText().toString().trim());
						JSONObject param = new JSONObject();
						param.put("actioncode", 2);//actioncode:2表示发起对战
						param.put("data", jsonarray);//data,data[0]是角色名
						final String JSONstr = param.toString();
						ThreadUtils.runInSubThread(new Runnable() {
							public void run() {
								try {
									hero.getMyConn().sendMessage(JSONstr);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case R.id.guide:
					Toast.makeText(MenuActivity.this, "退出", Toast.LENGTH_SHORT).show();
					setResult(10);//暂时无用
					MenuActivity.this.finish();
					break;
				default:
					break;
					
			}
		}
	};
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
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hero.getMyConn().removeOnMessageListener(listener);
	}

}
