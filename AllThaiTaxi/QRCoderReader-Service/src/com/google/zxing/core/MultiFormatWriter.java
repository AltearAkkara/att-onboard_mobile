package com.google.zxing.core;

import java.util.Hashtable;
import com.google.zxing.core.common.BitMatrix;
import com.google.zxing.core.qrcode.QRCodeWriter;

public class MultiFormatWriter implements Writer {
	
	public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {

		return encode(contents, format, width, height, null);
	}

	public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Hashtable hints) throws WriterException {

		Writer writer;
		/*
		if (format == BarcodeFormat.EAN_8) {
			writer = new EAN8Writer();
		} else if (format == BarcodeFormat.EAN_13) {
			writer = new EAN13Writer();
		} else if (format == BarcodeFormat.QR_CODE) {
			writer = new QRCodeWriter();
		} else if (format == BarcodeFormat.CODE_39) {
			writer = new Code39Writer();
		} else if (format == BarcodeFormat.CODE_128) {
			writer = new Code128Writer();
		} else if (format == BarcodeFormat.ITF) {
			writer = new ITFWriter();
		} else {
			throw new IllegalArgumentException("No encoder available for format " + format);
		}
		*/
		writer = new QRCodeWriter();
		return writer.encode(contents, format, width, height, hints);
	}

}
