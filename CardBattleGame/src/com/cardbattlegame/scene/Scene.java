package com.cardbattlegame.scene;


import java.util.ArrayList;

import com.cardbattlegame.domain.Spirit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Scene extends SurfaceView implements SurfaceHolder.Callback {

	private final static String TAG = "Scene";

	private SurfaceHolder holder;
	private Director mDirector;

	private ArrayList<Spirit> spirits = new ArrayList<Spirit>();

	public Scene(Context context) {
		super(context);
		// ����һ���µ�SurfaceHolder
		holder = getHolder();
		holder.addCallback(this);
	}

	public void addSpirit(Spirit sp) {
		spirits.add(sp);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() != MotionEvent.ACTION_DOWN) {
			return false;
		}

		float X = event.getX();
		float Y = event.getY();
		// TODO ����x��y�������жϴ������Ǹ�����
		Log.v(TAG, "X=" + X + " Y=" + Y);

		synchronized (holder) {
			for (Spirit sp : spirits) {

				Spirit.Coordinates coord;
				coord = sp.getCoordinates();

				// �ж��Ƿ�����ĳ������
				if ((X >= coord.getX() && X <= (coord.getX() + sp.getImage()
						.getWidth()))
						&& (Y >= coord.getY() && Y <= (coord.getY() + sp
								.getImage().getHeight()))) {
					sp.onTouchEvent(this, event);
				}

			}
			return true;
		}
	}

	@Override
	public void onDraw(Canvas canvas) {

		for (Spirit graphic : spirits) {

			Spirit.Coordinates coord;
			Spirit.Speed speed;

			coord = graphic.getCoordinates();
			speed = graphic.getSpeed();

			// Direction
			if (speed.getXDirection() == Spirit.Speed.X_DIRECTION_RIGHT) {
				coord.setX(coord.getX() + speed.getX());
			} else {
				coord.setX(coord.getX() - speed.getX());
			}
			if (speed.getYDirection() == Spirit.Speed.Y_DIRECTION_DOWN) {
				coord.setY(coord.getY() + speed.getY());
			} else {
				coord.setY(coord.getY() - speed.getY());
			}

			// borders for x...
			if (coord.getX() < 0) {
				speed.toggleXDirection();
				coord.setX(-coord.getX());
			} else if (coord.getX() + graphic.getImage().getWidth() > getWidth()) {
				speed.toggleXDirection();
				coord.setX(coord.getX() + getWidth()
						- (coord.getX() + graphic.getImage().getWidth()));
			}

			// borders for y...
			if (coord.getY() < 0) {
				speed.toggleYDirection();
				coord.setY(-coord.getY());
			} else if (coord.getY() + graphic.getImage().getHeight() > getHeight()) {
				speed.toggleYDirection();
				coord.setY(coord.getY() + getHeight()
						- (coord.getY() + graphic.getImage().getHeight()));
			}

			Bitmap bitmap = graphic.getImage();

			canvas.drawBitmap(bitmap, coord.getX(), coord.getY(), null);
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// �����߳�
		mDirector = new Director();
		mDirector.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// ͣ���߳�
		if (mDirector != null) {
			mDirector.requestExitAndWait();
			mDirector = null;
		}
	}

	class Director extends Thread {

		private boolean done;

		Director() {
			super();
			done = false;
		}

		@Override
		public void run() {
			SurfaceHolder surfaceHolder = holder;
			// ����ѭ��ִ���߳�
			while (!done) {
				// ����surface������canvas���ڻ���2Dͼ��
				Canvas canvas = surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					// �ص�MySurfaceView��onDraw����
					onDraw(canvas);
				}
				// ������������Ⱦ��ǰͼ��
				surfaceHolder.unlockCanvasAndPost(canvas);
				try {
					Thread.sleep(1000 / 60);// 60֡��
				} catch (InterruptedException e) {
				}
			}

		}

		public void requestExitAndWait() {
			// ����̲߳��ϲ����̵߳����߳�
			done = true;
			try {
				join();
			} catch (InterruptedException ex) {
			}
		}

	}

}
