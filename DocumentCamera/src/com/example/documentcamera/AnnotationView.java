package com.example.documentcamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class AnnotationView extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder mHolder;

	// ÿ50��ˢ��һ����Ļ
	private static final int TIME_IN_FRAME = 50;

	// ����
	private Canvas mCanvas;

	// �����߳��Ƿ����ѭ����־
	private boolean isDrawing = false;
	private boolean shouldStop = false;

	private AnnotationTool mTool;
	private static final float DEFAULT_STROKE_WIDTH = 2.0f;
	private static final int DEFAULT_PEN_COLOR = Color.RED;

	private float mStrokeWidth = DEFAULT_STROKE_WIDTH;

	public AnnotationView(Context context, AttributeSet attributeSet) {
		// TODO Auto-generated constructor stub
		super(context, attributeSet);

		init();
	}

	private void init() {
		// ���õ�ǰviewӵ�п��ƽ���
		this.setFocusable(true);

		// ���õ�ǰviewӵ�д����¼�
		this.setFocusableInTouchMode(true);

		// ���õ�ǰviewΪ���
		this.setZOrderOnTop(true);

		mHolder = getHolder();
		mHolder.setFormat(PixelFormat.TRANSPARENT);
		mHolder.addCallback(this);

		mTool = new AnnotationPen(DEFAULT_PEN_COLOR, mStrokeWidth);
	}

	public void setPenColor(int penColor) {
		stopDrawing();

		mTool = new AnnotationPen(penColor, mStrokeWidth);

		startDrawing();
	}

	public void setEraser() {
		stopDrawing();

		mTool = new AnnotationEraser(20.f);

		startDrawing();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		startDrawing();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		stopDrawing();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isDrawing) {
			// ����֮ǰʱ��
			long startTime = System.currentTimeMillis();

			synchronized (mHolder) {
				mCanvas = mHolder.lockCanvas();
				// drawLine();
				mTool.drawLine(mCanvas);
				mHolder.unlockCanvasAndPost(mCanvas);
			}

			// ����֮��ʱ��
			long endTime = System.currentTimeMillis();

			int diffTime = (int) (endTime - startTime);

			while (diffTime <= TIME_IN_FRAME) {
				diffTime = (int) (System.currentTimeMillis() - startTime);
				Thread.yield();
			}

			if (shouldStop) {
				shouldStop = false;
				break;
			}
		}
	}

	private void startDrawing() {
		isDrawing = true;
		new Thread(this).start();
	}

	private void stopDrawing() {
		if (isDrawing) {
			shouldStop = true;

			while (shouldStop) {
				try {
					Thread.sleep(100); // wait for thread stopping
				} catch (Exception e) {
				}
			}

			isDrawing = false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// mPath = new Path();
			// mPath.moveTo(event.getX(), event.getY());
			mTool.onTouchDown(event.getX(), event.getY());

			break;

		case MotionEvent.ACTION_MOVE:
			// mPath.quadTo(mPosX, mPosY, event.getX(), event.getY());
			mTool.onTouchMove(event.getX(), event.getY());
			break;

		case MotionEvent.ACTION_UP:
			mTool.onTouchUp(event.getX(), event.getY());
			break;
		}

		return true;
	}

}
