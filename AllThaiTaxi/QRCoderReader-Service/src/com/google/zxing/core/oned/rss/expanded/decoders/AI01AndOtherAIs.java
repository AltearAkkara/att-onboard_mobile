package com.google.zxing.core.oned.rss.expanded.decoders;

import com.google.zxing.core.common.BitArray;
import com.google.zxing.core.NotFoundException;

public final class AI01AndOtherAIs extends AI01decoder {

	private static final int HEADER_SIZE = 1 + 1 + 2; //first bit encodes the linkage flag,
                          //the second one is the encodation method, and the other two are for the variable length
	public AI01AndOtherAIs(BitArray information) {
		super(information);
	}

	public String parseInformation() throws NotFoundException {
    
		StringBuffer buff = new StringBuffer();

		buff.append("(01)");
		int initialGtinPosition = buff.length();
		int firstGtinDigit = this.generalDecoder.extractNumericValueFromBitArray(HEADER_SIZE, 4);
		buff.append(firstGtinDigit);

		this.encodeCompressedGtinWithoutAI(buff, HEADER_SIZE + 4, initialGtinPosition);

		return this.generalDecoder.decodeAllCodes(buff, HEADER_SIZE + 44);
	}
}
