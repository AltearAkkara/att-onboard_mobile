package com.google.zxing.core.oned;

import java.util.Hashtable;
import com.google.zxing.core.BarcodeFormat;
import com.google.zxing.core.WriterException;
import com.google.zxing.core.common.BitMatrix;

public final class EAN8Writer extends UPCEANWriter {

	private static final int codeWidth = 3 + // start guard
      	(7 * 4) + // left bars
      	5 + // middle guard
      	(7 * 4) + // right bars
      	3; // end guard

	public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Hashtable hints) throws WriterException {
    
		if (format != BarcodeFormat.EAN_8) {
			throw new IllegalArgumentException("Can only encode EAN_8, but got " + format);
		}
		return super.encode(contents, format, width, height, hints);
	}

	/** @return a byte array of horizontal pixels (0 = white, 1 = black) */
	public byte[] encode(String contents) {
		
		if (contents.length() != 8) {
			throw new IllegalArgumentException("Requested contents should be 8 digits long, but got " + contents.length());
		}

		byte[] result = new byte[codeWidth];
		
		int pos = 0;
		pos += appendPattern(result, pos, UPCEANReader.START_END_PATTERN, 1);

		for (int i = 0; i <= 3; i++) {
			int digit = Integer.parseInt(contents.substring(i, i + 1));
			pos += appendPattern(result, pos, UPCEANReader.L_PATTERNS[digit], 0);
		}
		pos += appendPattern(result, pos, UPCEANReader.MIDDLE_PATTERN, 0);

		for (int i = 4; i <= 7; i++) {
			int digit = Integer.parseInt(contents.substring(i, i + 1));
			pos += appendPattern(result, pos, UPCEANReader.L_PATTERNS[digit], 1);
		}
		pos += appendPattern(result, pos, UPCEANReader.START_END_PATTERN, 1);

		return result;
	}

}
