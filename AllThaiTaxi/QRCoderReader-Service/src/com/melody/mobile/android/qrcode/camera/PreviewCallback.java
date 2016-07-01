package com.melody.mobile.android.qrcode.camera;

import android.util.Log;
import android.os.Handler;
import android.os.Message;
import android.graphics.Point;
import android.hardware.Camera;

public class PreviewCallback implements Camera.PreviewCallback {
	
	private static final String TAG = PreviewCallback.class.getSimpleName();

	private int previewMessage;
	private Handler previewHandler;
	private final boolean useOneShotPreviewCallback;
	private final CameraConfigurationManager configManager;

	PreviewCallback(CameraConfigurationManager configManager, boolean useOneShotPreviewCallback) {
		
		this.configManager = configManager;
	    this.useOneShotPreviewCallback = useOneShotPreviewCallback;
	}

	void setHandler(Handler previewHandler, int previewMessage) {
		
	    this.previewHandler = previewHandler;
	    this.previewMessage = previewMessage;
	}

	public void onPreviewFrame(byte[] data, Camera camera) {
		
		Point cameraResolution = configManager.getCameraResolution();
		if(!useOneShotPreviewCallback) {
			camera.setPreviewCallback(null);
	    }
		
	    if (previewHandler != null) {
	    	
	    	Message message = previewHandler.obtainMessage(previewMessage, cameraResolution.x, cameraResolution.y, data);
	    	message.sendToTarget();
	    	previewHandler = null;
	      
	    } else {
	    	Log.d(TAG, "Got preview callback, but no handler for it");
	    }
	    
	}

}
