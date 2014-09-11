package com.example.documentcamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private MediaRecorder mMediaRecorder;

	private boolean isRecording = false;

	private static final String TAG = "preview";

	public CameraPreview(Context context) {
		// TODO Auto-generated constructor stub
		super(context);

		mCamera = getCameraInstance();

		mHolder = getHolder();
		mHolder.addCallback(this);

		setPictureFormat();
	}

	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
			c.release();
			c = null;
		}
		return c; // returns null if camera is unavailable
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();

		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	private void setPictureFormat() {
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPictureFormat(ImageFormat.JPEG);
		mCamera.setParameters(parameters);
	}

	public void takePicture() {
		mCamera.takePicture(null, null, mPicture);
		mCamera.startPreview();
	}

	private class saveFileTask extends AsyncTask<byte[], Void, Void> {
		@Override
		protected Void doInBackground(byte[]... params) {
			// TODO Auto-generated method stub
			File pictureFile = MultiMediaFileSave
					.getOutputMediaFile(MultiMediaFileSave.MEDIA_TYPE_IMAGE);
			if (pictureFile == null) {
				Log.d(TAG,
						"Error creating media file, check storage permissions");
				return null;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(params[0]);
				fos.close();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}

			return null;
		}
	}

	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			new saveFileTask().execute(data);
		}
	};

	private boolean prepareVideoRecorder() {

		// mCamera = getCameraInstance();
		mMediaRecorder = new MediaRecorder();

		// Step 1: Unlock and set camera to MediaRecorder
		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);

		// Step 2: Set sources
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
		mMediaRecorder.setProfile(CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH));

		// Step 4: Set output file
		mMediaRecorder.setOutputFile(MultiMediaFileSave.getOutputMediaFile(
				MultiMediaFileSave.MEDIA_TYPE_VIDEO).toString());

		// Step 5: Set the preview output
		mMediaRecorder.setPreviewDisplay(getHolder().getSurface());

		// Step 6: Prepare configured MediaRecorder
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			Log.d(TAG,
					"IllegalStateException preparing MediaRecorder: "
							+ e.getMessage());
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		}
		return true;
	}

	public void videoRecord() {
		if (isRecording) {
			// stop recording and release camera
			mMediaRecorder.stop(); // stop the recording
			releaseMediaRecorder(); // release the MediaRecorder object
			mCamera.lock(); // take camera access back from MediaRecorder

			// inform the user that recording has stopped
			isRecording = false;
		} else {
			// initialize video camera
			if (prepareVideoRecorder()) {
				// Camera is available and unlocked, MediaRecorder is prepared,
				// now you can start recording
				mMediaRecorder.start();

				// inform the user that recording has started
				isRecording = true;
			} else {
				// prepare didn't work, release the camera
				releaseMediaRecorder();
				// inform user
			}
		}

	}

	public void releaseMediaRecorder() {
		if (mMediaRecorder != null) {
			mMediaRecorder.reset(); // clear recorder configuration
			mMediaRecorder.release(); // release the recorder object
			mMediaRecorder = null;
			mCamera.lock(); // lock camera for later use
		}
	}

	public void releaseCamera() {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

}
