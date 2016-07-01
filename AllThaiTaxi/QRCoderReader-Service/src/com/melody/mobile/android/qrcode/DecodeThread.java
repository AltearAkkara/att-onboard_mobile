package com.melody.mobile.android.qrcode;

import java.util.Vector;
import android.os.Looper;
import android.os.Handler;
import java.util.Hashtable;
import android.content.SharedPreferences;
import java.util.concurrent.CountDownLatch;
import com.google.zxing.core.BarcodeFormat;
import android.preference.PreferenceManager;
import com.google.zxing.core.DecodeHintType;
import com.google.zxing.core.ResultPointCallback;

public final class DecodeThread extends Thread {
	
	private Handler handler;
	private final CaptureActivity activity;
	private final CountDownLatch handlerInitLatch;
	private final Hashtable<DecodeHintType, Object> hints;
	public static final String BARCODE_BITMAP = "barcode_bitmap";

	public DecodeThread(CaptureActivity activity, Vector<BarcodeFormat> decodeFormats, String characterSet, ResultPointCallback resultPointCallback) {

		this.activity = activity;
	    handlerInitLatch = new CountDownLatch(1);

	    hints = new Hashtable<DecodeHintType, Object>(3);

	    // The prefs can't change while the thread is running, so pick them up once here.
	    if (decodeFormats == null || decodeFormats.isEmpty()) {
	    	
	    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
	    	decodeFormats = new Vector<BarcodeFormat>();
	    	if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_1D, true)) {
	    		decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
	    	}
	    	if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_QR, true)) {
	    		decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
	    	}
	    	if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_DATA_MATRIX, true)) {
	    		decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
	    	}
	    }
	    hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

	    if (characterSet != null) {
	    	hints.put(DecodeHintType.CHARACTER_SET, characterSet);
	    }

	    hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
	}

	Handler getHandler() {
		
	    try {
	    	handlerInitLatch.await();
	    } catch (InterruptedException ie) {
	    	//continue?
	    }
	    return handler;
	}

	@Override
	public void run() {
		
	    Looper.prepare();
	    handler = new DecodeHandler(activity, hints);
	    handlerInitLatch.countDown();
	    Looper.loop();
	}

}
