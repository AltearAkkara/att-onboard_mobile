package com.google.zxing.core.oned.rss.expanded.decoders;

import com.google.zxing.core.common.BitArray;

public abstract class AI01weightDecoder extends AI01decoder {

	public AI01weightDecoder(BitArray information) {
		super(information);
	}

	protected void encodeCompressedWeight(StringBuffer buf, int currentPos, int weightSize) {
    
		int originalWeightNumeric = this.generalDecoder.extractNumericValueFromBitArray(currentPos, weightSize);
		addWeightCode(buf, originalWeightNumeric);

		int weightNumeric = checkWeight(originalWeightNumeric);

		int currentDivisor = 100000;
		for(int i = 0; i < 5; ++i){
			if (weightNumeric / currentDivisor == 0) {
				buf.append('0');
			}
			currentDivisor /= 10;
		}
		buf.append(weightNumeric);
	}

	protected abstract void addWeightCode(StringBuffer buf, int weight);
	protected abstract int checkWeight(int weight);
}
