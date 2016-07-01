package com.google.zxing.core.common;

import com.google.zxing.core.ResultPoint;

public final class DetectorResult {

	private final BitMatrix bits;
	private final ResultPoint[] points;

	public DetectorResult(BitMatrix bits, ResultPoint[] points) {
		this.bits = bits;
		this.points = points;
	}

	public BitMatrix getBits() {
		return bits;
	}

	public ResultPoint[] getPoints() {
		return points;
	}

}