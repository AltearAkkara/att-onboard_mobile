package com.google.zxing.core.oned.rss.expanded.decoders;

import com.google.zxing.core.common.BitArray;

public final class AI013103decoder extends AI013x0xDecoder {

	public AI013103decoder(BitArray information) {
		super(information);
	}

	protected void addWeightCode(StringBuffer buf, int weight) {
		buf.append("(3103)");
	}

	protected int checkWeight(int weight) {
		return weight;
	}
}
