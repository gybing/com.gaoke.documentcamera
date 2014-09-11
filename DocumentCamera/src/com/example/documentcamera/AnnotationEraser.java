package com.example.documentcamera;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class AnnotationEraser implements AnnotationTool {
	private Path mPath;
	private Paint mPaint;

	private float mPosX, mPosY;
	private static final float TOUCH_TOLERANCE = 4.0f;
	private Rect eraserArea;
	private int RECT_LENGTH = 5;

	public AnnotationEraser(float strokeWidth) {
		// TODO Auto-generated constructor stub
		mPath = new Path();

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFF000000);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(strokeWidth);
		mPaint.setStrokeCap(Paint.Cap.SQUARE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
	}

	@Override
	public void drawLine(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawColor(Color.TRANSPARENT);
		canvas.drawPath(mPath, mPaint);
	}

	@Override
	public void onTouchDown(float x, float y) {
		// TODO Auto-generated method stub
		float dx = Math.abs(x - RECT_LENGTH);
		float dy = Math.abs(y - RECT_LENGTH);

		mPath.moveTo(x, y);

		eraserArea = new Rect(10, 20, 40, 50);
		
		
		mPosX = x;
		mPosY = y;
	}

	@Override
	public void onTouchMove(float x, float y) {
		// TODO Auto-generated method stub
		float dx = Math.abs(x - mPosX);
		float dy = Math.abs(y - mPosY);

		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mPosX, mPosY, (x + mPosX) / 2, (y + mPosY) / 2);

			mPosX = x;
			mPosY = y;
		}
	}

	@Override
	public void onTouchUp(float x, float y) {
		// TODO Auto-generated method stub
		mPath.lineTo(x, y);
	}
}
