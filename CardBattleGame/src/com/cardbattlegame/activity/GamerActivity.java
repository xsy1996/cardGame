package com.cardbattlegame.activity;

import com.cardbattlegame.scene.mainScene;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GamerActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// È«ÆÁÏÔÊ¾£¬Òþ²Ø±êÌâÀ¸
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Òþ²Ø×´Ì¬À¸
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ±£³ÖºáÆÁ
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setContentView(new mainScene(this));
		
	}

}