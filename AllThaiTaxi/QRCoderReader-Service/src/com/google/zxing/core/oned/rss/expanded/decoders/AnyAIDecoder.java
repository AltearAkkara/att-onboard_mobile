package com.google.zxing.core.oned.rss.expanded.decoders;

import com.google.zxing.core.common.BitArray;
import com.google.zxing.core.NotFoundException;

public final class AnyAIDecoder extends AbstractExpandedDecoder {

	private static final int HEADER_SIZE = 2 + 1 + 2;

	public AnyAIDecoder(BitArray information) {
		super(information);
	}

	public String parseInformation() throws NotFoundException {
		StringBuffer buf = new StringBuffer();
		return this.generalDecoder.decodeAllCodes(buf, HEADER_SIZE);
	}
}
