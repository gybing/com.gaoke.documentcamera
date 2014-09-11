package com.example.documentcamera;

import android.graphics.Canvas;

public interface AnnotationTool {
	public void drawLine(Canvas canvas);

	public void onTouchDown(float x, float y);

	public void onTouchMove(float x, float y);

	public void onTouchUp(float x, float y);
}
