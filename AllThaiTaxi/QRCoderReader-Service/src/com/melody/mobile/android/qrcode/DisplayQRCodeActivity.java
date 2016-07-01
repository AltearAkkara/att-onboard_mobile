package com.melody.mobile.android.qrcode;

import android.os.Bundle;
import android.view.Window;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;
import android.view.WindowManager;
import com.google.zxing.core.BarcodeFormat;
import com.google.zxing.core.common.BitMatrix;
import com.google.zxing.core.MultiFormatWriter;
import com.truelife.mobile.android.qrcode.R;

public class DisplayQRCodeActivity extends Activity {
	
	private ImageView qrcode_view = null;
	private BitMatrix result = null;
	private String sStrToEncode = "kenji.melody";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.qrcodewriter);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
		qrcode_view = (ImageView) findViewById(R.id.qrcode_view);
		encodeQRCode();
	}
	
	@SuppressWarnings("unchecked")
	private void encodeQRCode() {
	    
		try {
			
			result = new MultiFormatWriter().encode(sStrToEncode, BarcodeFormat.QR_CODE, 400, 400);
			Bitmap bmp = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
			for (int x = 0; x < result.getWidth(); x++) {
				
				for (int y = 0; y < result.getHeight(); y++) {
					
					if (!result.get(x, y)) {
						bmp.setPixel(x, y, Color.argb(255, 255, 255, 255));
					} else {
						bmp.setPixel(x, y, Color.argb(255, 0, 0, 0));
					}
				}
			}
            qrcode_view.setImageBitmap(bmp);
		
		} catch(Exception e) {
		
		}
		
	}

}
