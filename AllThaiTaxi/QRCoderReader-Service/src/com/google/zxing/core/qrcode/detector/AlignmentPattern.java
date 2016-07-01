package com.google.zxing.core.qrcode.detector;

import com.google.zxing.core.ResultPoint;

public final class AlignmentPattern extends ResultPoint {

	private final float estimatedModuleSize;

	public AlignmentPattern(float posX, float posY, float estimatedModuleSize) {
		
		super(posX, posY);
		this.estimatedModuleSize = estimatedModuleSize;
	}

	/**
	 * <p>Determines if this alignment pattern "about equals" an alignment pattern at the stated
	 * position and size -- meaning, it is at nearly the same center with nearly the same size.</p>
	 */
	public boolean aboutEquals(float moduleSize, float i, float j) {
		
		if (Math.abs(i - getY()) <= moduleSize && Math.abs(j - getX()) <= moduleSize) {
			float moduleSizeDiff = Math.abs(moduleSize - estimatedModuleSize);
			return moduleSizeDiff <= 1.0f || moduleSizeDiff / estimatedModuleSize <= 1.0f;
		}
		return false;
	}

}