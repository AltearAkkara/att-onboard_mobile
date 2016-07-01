package com.google.zxing.core.qrcode.detector;

import com.google.zxing.core.ResultPoint;

public final class FinderPattern extends ResultPoint {

	private final float estimatedModuleSize;
	private int count;

	public FinderPattern(float posX, float posY, float estimatedModuleSize) {
		super(posX, posY);
		this.estimatedModuleSize = estimatedModuleSize;
		this.count = 1;
	}

	public float getEstimatedModuleSize() {
		return estimatedModuleSize;
	}

	public int getCount() {
		return count;
	}

	public void incrementCount() {
		this.count++;
	}

	/**
	 * <p>Determines if this finder pattern "about equals" a finder pattern at the stated
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
