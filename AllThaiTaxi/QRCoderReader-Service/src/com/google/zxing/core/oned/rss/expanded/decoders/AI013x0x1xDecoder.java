package com.google.zxing.core.oned.rss.expanded.decoders;

import com.google.zxing.core.common.BitArray;
import com.google.zxing.core.NotFoundException;

public final class AI013x0x1xDecoder extends AI01weightDecoder {

	private static final int headerSize = 7 + 1;
	private static final int weightSize = 20;
	private static final int dateSize   = 16;

	private final String dateCode;
	private final String firstAIdigits;

	public AI013x0x1xDecoder(BitArray information, String firstAIdigits, String dateCode) {
		super(information);
		this.dateCode      = dateCode;
		this.firstAIdigits = firstAIdigits;
	}

	public String parseInformation() throws NotFoundException {
		
		if (this.information.size != headerSize + gtinSize + weightSize + dateSize) {
			throw NotFoundException.getNotFoundInstance();
		}

		StringBuffer buf = new StringBuffer();

		encodeCompressedGtin(buf, headerSize);
		encodeCompressedWeight(buf, headerSize + gtinSize, weightSize);
		encodeCompressedDate(buf, headerSize + gtinSize + weightSize);

		return buf.toString();
	}

	private void encodeCompressedDate(StringBuffer buf, int currentPos) {
    
		int numericDate = this.generalDecoder.extractNumericValueFromBitArray(currentPos, dateSize);
		if(numericDate == 38400) {
			return;
		}

		buf.append('(');
		buf.append(this.dateCode);
		buf.append(')');

		int day = numericDate % 32;
		numericDate /= 32;
		
		int month = numericDate % 12 + 1;
		numericDate /= 12;
		
		int year  = numericDate;
		if (year / 10 == 0) {
			buf.append('0');
		}
		buf.append(year);
		if (month / 10 == 0) {
			buf.append('0');
		}
		buf.append(month);
		if (day / 10 == 0) {
			buf.append('0');
		}
		buf.append(day);
	}

	protected void addWeightCode(StringBuffer buf, int weight) {
    
		int lastAI = weight / 100000;
		buf.append('(');
		buf.append(this.firstAIdigits);
   		buf.append(lastAI);
   		buf.append(')');
	}

	protected int checkWeight(int weight) {
		return weight % 100000;
	}
}
