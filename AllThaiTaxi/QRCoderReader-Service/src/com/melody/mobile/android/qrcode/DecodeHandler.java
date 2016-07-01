package com.melody.mobile.android.qrcode;

import android.util.Log;
import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;
import android.os.Message;
import java.util.Hashtable;
import com.google.zxing.core.Result;
import com.google.zxing.core.BinaryBitmap;
import com.google.zxing.core.DecodeHintType;
import com.google.zxing.core.ReaderException;
import com.google.zxing.core.MultiFormatReader;
import com.google.zxing.core.common.HybridBinarizer;
import com.melody.mobile.android.qrcode.camera.CameraManager;
import com.truelife.mobile.android.qrcode.R;

public final class DecodeHandler extends Handler {

	private static final String TAG = DecodeHandler.class.getSimpleName();

	private final CaptureActivity activity;
	private final MultiFormatReader multiFormatReader;

	DecodeHandler(CaptureActivity activity, Hashtable<DecodeHintType, Object> hints) {
		multiFormatReader = new MultiFormatReader();
	    multiFormatReader.setHints(hints);
	    this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
		
		if(message.what == R.id.decode) {
			decode((byte[]) message.obj, message.arg1, message.arg2);
		} else if(message.what == R.id.quit) {
			Looper.myLooper().quit();
		}
	}
	
	private void decode(byte[] data, int width, int height) {
		
	    long start = System.currentTimeMillis();
	    Result rawResult = null;
	    PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(data, width, height);
	    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	    
	    try {
	    	rawResult = multiFormatReader.decodeWithState(bitmap);
	    } catch (ReaderException re) {
	      // continue
	    } finally {
	    	multiFormatReader.reset();
	    }

	    if (rawResult != null) {
	    	
	    	long end = System.currentTimeMillis();
	    	Log.d(TAG, "Found barcode (" + (end - start) + " ms):\n" + rawResult.toString());
	    	
	    	Message message = Message.obtain(activity.getHandler(), R.id.decode_succeeded, rawResult);
	    	
	    	Bundle bundle = new Bundle();
	    	bundle.putParcelable(DecodeThread.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
	    	message.setData(bundle);
	    	
	    	//Log.d(TAG, "Sending decode succeeded message...");
	    	message.sendToTarget();
	    } else {
	    	Message message = Message.obtain(activity.getHandler(), R.id.decode_failed);
	    	message.sendToTarget();
	    }
	}

}
