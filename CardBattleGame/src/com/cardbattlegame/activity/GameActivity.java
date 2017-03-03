package com.cardbattlegame.activity;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cardbattlegame.connect.MyConnection.OnMessageListener;
import com.cardbattlegame.domain.Hero;
import com.cardbattlegame.util.ThreadUtils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {

	private Button Card1;
	private Button Card2;
	private Button Card3;
	private Button Card4;
	private Button cardtemp = null;
	
	private TextView battleLog;
	private ImageView win;
	private ImageView fail;
	private TextView competitor_name;
	private TextView competitor_state1;
	private TextView competitor_state2;
	
	private TextView state1;
	private TextView state2;
	
	private Hero hero;
	

	protected void handleResult(String jsonStr) {
		try {
			JSONObject json = new JSONObject(jsonStr);
			String flag;
			String type;
			flag = json.getString("success");
			type = json.getString("type");
			if(flag.equals("true") && type.equals("3")){
				Toast.makeText(GameActivity.this, "出牌", Toast.LENGTH_SHORT).show();
				cardtemp.setBackgroundDrawable(getResources().getDrawable(R.drawable.b1));
				cardtemp.setClickable(false);
				cardtemp = null;
				competitor_state1.setText(R.string.state1+json.getString("competitor_state1"));
				competitor_state2.setText(R.string.state2+json.getString("competitor_state2"));
				state1.setText(R.string.state1+json.getString("state1"));
				state2.setText(R.string.state2+json.getString("state2"));
				battleLog.setText("");//
			}
			if(flag.equals("true") && type.equals("4")){
				Toast.makeText(GameActivity.this, "对方出牌", Toast.LENGTH_SHORT).show();
				competitor_state1.setText(R.string.state1+json.getString("competitor_state1"));
				competitor_state2.setText(R.string.state2+json.getString("competitor_state2"));
				state1.setText(R.string.state1+json.getString("state1"));
				state2.setText(R.string.state2+json.getString("state2"));
				battleLog.setText(battleLog.getText().toString()+"/n/r"+"...");//
			}
			else if(flag.equals("true") && type.equals("5")){
				//结局，时间
				String Result = json.getString("result");
				if(Result.equals("win")){
					// VISIBLE, INVISIBLE, or GONE，想对应的三个常量值：0、4、8
					fail.setVisibility(8);
					win.setVisibility(0);
					setResult(11);
					GameActivity.this.finish();
				}
				else{
					fail.setVisibility(0);
					win.setVisibility(8);
					setResult(12);
					GameActivity.this.finish();
				}
			}
			else{
				Toast.makeText(GameActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
			    System.out.print("登录失败");	
			}
		} catch (Exception e) {
			Toast.makeText(GameActivity.this, "解析JSON失败", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		battleLog = (TextView)findViewById(R.id.battleLog);
		competitor_name = (TextView)findViewById(R.id.competitor_name);
		competitor_state1 = (TextView)findViewById(R.id.competitor_state1);
		competitor_state2 = (TextView)findViewById(R.id.competitor_state2);
		state1 = (TextView)findViewById(R.id.state1);
		state2 = (TextView)findViewById(R.id.state2);
		Card1 = (Button)findViewById(R.id.Card1);
		Card2 = (Button)findViewById(R.id.Card2);
		Card3 = (Button)findViewById(R.id.Card3);
		Card4 = (Button)findViewById(R.id.Card4);
		win = (ImageView)findViewById(R.id.win);
		fail = (ImageView)findViewById(R.id.fail);
		
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
		Card1.setOnClickListener(myclicklistener);
		Card2.setOnClickListener(myclicklistener);
		Card3.setOnClickListener(myclicklistener);
		Card4.setOnClickListener(myclicklistener);
		battleLog.setText("");
		Bundle bundle = this.getIntent().getExtras(); 
		competitor_name.setText(competitor_name.getText().toString().trim()+bundle.getString("competitor_name"));
		competitor_state1.setText(competitor_state1.getText().toString().trim()+bundle.getString("competitor_state1"));
		competitor_state2.setText(competitor_state2.getText().toString().trim()+bundle.getString("competitor_state2"));
		state1.setText(state1.getText().toString().trim()+bundle.getString("state1"));
		state2.setText(state2.getText().toString().trim()+bundle.getString("state2"));
		Card1.setText(bundle.getString("Card1"));
		Card2.setText(bundle.getString("Card2"));
		Card3.setText(bundle.getString("Card3"));
		Card4.setText(bundle.getString("Card4"));
		// VISIBLE, INVISIBLE, or GONE，想对应的三个常量值：0、4、8
		fail.setVisibility(8);
		win.setVisibility(8);
		
	}
	
	private View.OnClickListener myclicklistener = new View.OnClickListener(){
		@Override
		public void onClick(View v){
			String card = "";
			switch(v.getId()){
				case R.id.Card1:
					Toast.makeText(GameActivity.this, "card1", Toast.LENGTH_SHORT).show();
					card = Card1.getText().toString().trim();
					cardtemp = Card1;
					break;
				case R.id.Card2:
					Toast.makeText(GameActivity.this, "card2", Toast.LENGTH_SHORT).show();
					card = Card2.getText().toString().trim();
					cardtemp = Card2;
					break;
				case R.id.Card3:
					Toast.makeText(GameActivity.this, "card3", Toast.LENGTH_SHORT).show();
					card = Card3.getText().toString().trim();
					cardtemp = Card3;
					break;
				case R.id.Card4:
					Toast.makeText(GameActivity.this, "card4", Toast.LENGTH_SHORT).show();
					card = Card4.getText().toString().trim();
					cardtemp = Card4;
					break;
				default:
					break;
					
			}
			try {
				JSONArray jsonarray = new JSONArray();
				jsonarray.put(0, card);
				JSONObject param = new JSONObject();
				param.put("actioncode", 3);//actioncode:3表示打出牌
				param.put("data", jsonarray);//data,data[0]是打出的牌
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
			} catch (JSONException e1) {
				e1.printStackTrace();
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
