package com.cardbattlegame.scene;

import com.cardbattlegame.activity.R;
import com.cardbattlegame.domain.Spirit;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;

public class mainScene extends Scene {

	private final static String TAG = "MainScene";
	
	public mainScene(Context context) {
		super(context);

		// ±³¾°
		Spirit bgSpirit = new Spirit(BitmapFactory.decodeResource(
				getResources(), R.drawable.battle), 0, 0, 0, 0, null);
		addSpirit(bgSpirit);

		// Ê¤Àû
		Spirit winSpirit = new Spirit(BitmapFactory.decodeResource(
				getResources(), R.drawable.win), 300, 30, 0, 0,
				"onTouchEventWinSpirit");
		addSpirit(winSpirit);

		// Ê§°Ü
		Spirit failSpirit = new Spirit(BitmapFactory.decodeResource(
				getResources(), R.drawable.fail), 0, 160, 2, 0,
				"onTouchEventFailSpirit");
		addSpirit(failSpirit);
		
		//¿¨ÅÆ1
		//Spirit Card1 = new Spirit(
			//	);
		//addSpirit(Card1);
		
	}
	

	void onTouchEventWinSpirit(Spirit sp, MotionEvent event) {
		Log.v(TAG, "X=" + event.getX() + " Y=" + event.getY());
		Log.v(TAG, "onTouchEventWinSpirit ... sp " + sp.getCoordinates());
	}

	void onTouchEventFailSpirit(Spirit sp, MotionEvent event) {
		Log.v(TAG, "X=" + event.getX() + " Y=" + event.getY());
		Log.v(TAG, "onTouchEventFailSpirit ... sp " + sp.getCoordinates());
	}

}
