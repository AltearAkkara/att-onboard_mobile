package com.melody.mobile.android.qrcode.camera;

import android.util.Log;
import android.os.Handler;
import android.os.Message;
import android.hardware.Camera;

public class AutoFocusCallback implements Camera.AutoFocusCallback {
	
	private static final long AUTOFOCUS_INTERVAL_MS = 1500L;
	private static final String TAG = AutoFocusCallback.class.getSimpleName();
	
	private int autoFocusMessage;
	private Handler autoFocusHandler;
	
	void setHandler(Handler autoFocusHandler, int autoFocusMessage) {
		
		this.autoFocusHandler = autoFocusHandler;
	    this.autoFocusMessage = autoFocusMessage;
	}

	public void onAutoFocus(boolean success, Camera camera) {
		
		if (autoFocusHandler != null) {
			
			Message message = autoFocusHandler.obtainMessage(autoFocusMessage, success);
			autoFocusHandler.sendMessageDelayed(message, AUTOFOCUS_INTERVAL_MS);
			autoFocusHandler = null;
	    } else {
	    	Log.d(TAG, "Got auto-focus callback, but no handler for it");
	    }
	}

}
