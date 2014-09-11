package com.example.documentcamera;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private CameraPreview mPreview;
	private int mediaType = MultiMediaFileSave.MEDIA_TYPE_IMAGE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 隐藏应用程序标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 隐藏系统标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		mPreview = new CameraPreview(this);

		FrameLayout mLayout = (FrameLayout) findViewById(R.id.surfaceview_container);
		mLayout.addView(mPreview);
	}

	public void buttonClick(View v) {
		switch (v.getId()) {
		case R.id.button_capture:
			if (mediaType == MultiMediaFileSave.MEDIA_TYPE_IMAGE) {
				Toast.makeText(this, "Taking photo ...", Toast.LENGTH_SHORT)
						.show();

				mPreview.takePicture();
			} else if (mediaType == MultiMediaFileSave.MEDIA_TYPE_VIDEO) {
				Toast.makeText(this, "Start recording video ...",
						Toast.LENGTH_SHORT).show();

				mPreview.videoRecord();
			}

			break;

		case R.id.button_switch:
			if (mediaType == MultiMediaFileSave.MEDIA_TYPE_IMAGE) {
				mediaType = MultiMediaFileSave.MEDIA_TYPE_VIDEO;

				Toast.makeText(this, "video", Toast.LENGTH_SHORT).show();

				((ImageButton) findViewById(R.id.button_capture))
						.setImageResource(R.drawable.ic_take_video);
			} else if (mediaType == MultiMediaFileSave.MEDIA_TYPE_VIDEO) {
				mediaType = MultiMediaFileSave.MEDIA_TYPE_IMAGE;

				Toast.makeText(this, "photo", Toast.LENGTH_SHORT).show();

				((ImageButton) findViewById(R.id.button_capture))
						.setImageResource(R.drawable.ic_take_photo);
			}

			break;

		case R.id.button_settings:

			break;

		case R.id.button_gallery:

			break;

		case R.id.button_pen:

			break;

		case R.id.button_color_selector:
			((LinearLayout) findViewById(R.id.layout_color_board))
					.setVisibility(View.VISIBLE);
			break;

		case R.id.button_eraser:
			((AnnotationView) findViewById(R.id.annotation_view)).setEraser();
			break;
		}
	}

	public void colorSelector(View v) {
		switch (v.getId()) {
		case R.id.color_black:
			((AnnotationView) findViewById(R.id.annotation_view))
					.setPenColor(Color.BLACK);

			((LinearLayout) findViewById(R.id.layout_color_board))
					.setVisibility(View.GONE);
			break;

		case R.id.color_blue:
			((AnnotationView) findViewById(R.id.annotation_view))
					.setPenColor(Color.BLUE);

			((LinearLayout) findViewById(R.id.layout_color_board))
					.setVisibility(View.GONE);
			break;

		case R.id.color_cyan:
			((AnnotationView) findViewById(R.id.annotation_view))
					.setPenColor(Color.CYAN);

			((LinearLayout) findViewById(R.id.layout_color_board))
					.setVisibility(View.GONE);
			break;

		case R.id.color_darkgray:
			((AnnotationView) findViewById(R.id.annotation_view))
					.setPenColor(Color.DKGRAY);

			((LinearLayout) findViewById(R.id.layout_color_board))
					.setVisibility(View.GONE);
			break;

		case R.id.color_green:
			((AnnotationView) findViewById(R.id.annotation_view))
					.setPenColor(Color.GREEN);

			((LinearLayout) findViewById(R.id.layout_color_board))
					.setVisibility(View.GONE);
			break;

		case R.id.color_red:
			((AnnotationView) findViewById(R.id.annotation_view))
					.setPenColor(Color.RED);

			((LinearLayout) findViewById(R.id.layout_color_board))
					.setVisibility(View.GONE);
			break;

		case R.id.color_yellow:
			((AnnotationView) findViewById(R.id.annotation_view))
					.setPenColor(Color.YELLOW);

			((LinearLayout) findViewById(R.id.layout_color_board))
					.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mPreview.releaseMediaRecorder();
		mPreview.releaseCamera();
	}

}
