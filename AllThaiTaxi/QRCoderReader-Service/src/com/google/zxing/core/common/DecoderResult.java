package com.google.zxing.core.common;

import java.util.Vector;
import com.google.zxing.core.qrcode.decoder.ErrorCorrectionLevel;

public final class DecoderResult {

	private final byte[] rawBytes;
	private final String text;
	private final Vector byteSegments;
	private final ErrorCorrectionLevel ecLevel;

	public DecoderResult(byte[] rawBytes, String text, Vector byteSegments, ErrorCorrectionLevel ecLevel) {
    
		if (rawBytes == null && text == null) {
			throw new IllegalArgumentException();
		}
		
		this.rawBytes = rawBytes;
		this.text = text;
		this.byteSegments = byteSegments;
		this.ecLevel = ecLevel;
	}

	public byte[] getRawBytes() {
		return rawBytes;
	}

	public String getText() {
		return text;
	}

	public Vector getByteSegments() {
		return byteSegments;
	}

	public ErrorCorrectionLevel getECLevel() {
		return ecLevel;
	}

}