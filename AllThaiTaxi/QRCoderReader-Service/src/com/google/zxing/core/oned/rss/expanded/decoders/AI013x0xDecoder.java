package com.google.zxing.core.oned.rss.expanded.decoders;

import com.google.zxing.core.common.BitArray;
import com.google.zxing.core.NotFoundException;

public abstract class AI013x0xDecoder extends AI01weightDecoder {

	private static final int headerSize = 4 + 1;
	private static final int weightSize = 15;

	public AI013x0xDecoder(BitArray information) {
		super(information);
	}

	public String parseInformation() throws NotFoundException {
    
		if (this.information.size != headerSize + gtinSize + weightSize) {
			throw NotFoundException.getNotFoundInstance();
		}

		StringBuffer buf = new StringBuffer();

		encodeCompressedGtin(buf, headerSize);
		encodeCompressedWeight(buf, headerSize + gtinSize, weightSize);

		return buf.toString();
	}
}
