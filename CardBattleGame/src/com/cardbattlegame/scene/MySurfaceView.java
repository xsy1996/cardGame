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
		//�����߳�
		mySVThread = new MySurfaceViewThread();
		mySVThread.start();
	}
	
	public void surfaceDestroyed(SurfaceHolder holder){
		//ֹͣ�߳�
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
		
		//����̺߳ϲ���ص����߳�
		public void requestExitAndWait() {
			done = true;
			try{
				join();
			} catch(InterruptedException e){
			}
		}

		public void run(){
			SurfaceHolder surfaceHolder = holder;
			//����ִ��
			while(!done){
				//����surface������,carvas���ڻ�ͼ
				Canvas canvas = surfaceHolder.lockCanvas() ;
				synchronized(surfaceHolder){
					//TODO:��ͼ
					Bitmap gbImage = BitmapFactory.decodeResource(
							getResources(), R.drawable.background);
					canvas.drawBitmap(gbImage, 0, 0, null);					
				}
				//��������
				surfaceHolder.unlockCanvasAndPost(canvas);
				try{
					Thread.sleep(1000/60);//60֡Ƶ
				} catch(InterruptedException e){
					
				}
			}
		}
	}
	
}
