package com.cardbattlegame.scene;

import com.cardbattlegame.activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements 
		SurfaceHolder.Callback{
	
	private SurfaceHolder holder;
	private MySurfaceViewThread mySVThread;
	
	public MySurfaceView(Context context){
		super(context);
		holder = getHolder();
		holder.addCallback(this);
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height){
		
	}
	
	public void surfaceCreated(SurfaceHolder holder){
		//启动线程
		mySVThread = new MySurfaceViewThread();
		mySVThread.start();
	}
	
	public void surfaceDestroyed(SurfaceHolder holder){
		//停止线程
		if(mySVThread != null){
			mySVThread.requestExitAndWait();
			mySVThread = null;
		}
	}
	class MySurfaceViewThread extends Thread{
		Boolean done;
		MySurfaceViewThread(){
			super();
			done = false;
		}
		
		//完成线程合并后回到主线程
		public void requestExitAndWait() {
			done = true;
			try{
				join();
			} catch(InterruptedException e){
			}
		}

		public void run(){
			SurfaceHolder surfaceHolder = holder;
			//反复执行
			while(!done){
				//锁定surface并返回,carvas用于绘图
				Canvas canvas = surfaceHolder.lockCanvas() ;
				synchronized(surfaceHolder){
					//TODO:绘图
					Bitmap gbImage = BitmapFactory.decodeResource(
							getResources(), R.drawable.background);
					canvas.drawBitmap(gbImage, 0, 0, null);					
				}
				//解锁画布
				surfaceHolder.unlockCanvasAndPost(canvas);
				try{
					Thread.sleep(1000/60);//60帧频
				} catch(InterruptedException e){
					
				}
			}
		}
	}
	
}
