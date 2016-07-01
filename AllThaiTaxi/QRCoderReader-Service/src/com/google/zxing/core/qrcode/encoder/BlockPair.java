package com.google.zxing.core.qrcode.encoder;

public final class BlockPair {

	private final byte[] dataBytes;
	private final byte[] errorCorrectionBytes;

	public BlockPair(byte[] data, byte[] errorCorrection) {
		dataBytes = data;
		errorCorrectionBytes = errorCorrection;
	}

	public byte[] getDataBytes() {
		return dataBytes;
	}

	public byte[] getErrorCorrectionBytes() {
		return errorCorrectionBytes;
	}

}
