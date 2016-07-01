package com.google.zxing.core.pdf417;

import java.util.Hashtable;
import com.google.zxing.core.Reader;
import com.google.zxing.core.Result;
import com.google.zxing.core.ResultPoint;
import com.google.zxing.core.BinaryBitmap;
import com.google.zxing.core.BarcodeFormat;
import com.google.zxing.core.DecodeHintType;
import com.google.zxing.core.FormatException;
import com.google.zxing.core.common.BitMatrix;
import com.google.zxing.core.NotFoundException;
import com.google.zxing.core.qrcode.QRCodeReader;
import com.google.zxing.core.common.DecoderResult;
import com.google.zxing.core.common.DetectorResult;
import com.google.zxing.core.pdf417.decoder.Decoder;
import com.google.zxing.core.pdf417.detector.Detector;

public final class PDF417Reader implements Reader {

	private static final ResultPoint[] NO_POINTS = new ResultPoint[0];

	private final Decoder decoder = new Decoder();

	/**
	 * Locates and decodes a PDF417 code in an image.
	 *
	 * @return a String representing the content encoded by the PDF417 code
	 * @throws NotFoundException if a PDF417 code cannot be found,
	 * @throws FormatException if a PDF417 cannot be decoded
	 */
	public Result decode(BinaryBitmap image) throws NotFoundException, FormatException {
		return decode(image, null);
	}

	public Result decode(BinaryBitmap image, Hashtable hints) throws NotFoundException, FormatException {
    
		DecoderResult decoderResult;
		ResultPoint[] points;
		if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
			BitMatrix bits = QRCodeReader.extractPureBits(image.getBlackMatrix());
			decoderResult = decoder.decode(bits);
			points = NO_POINTS;
		} else {
			DetectorResult detectorResult = new Detector(image).detect();
			decoderResult = decoder.decode(detectorResult.getBits());
			points = detectorResult.getPoints();
		}
		return new Result(decoderResult.getText(), decoderResult.getRawBytes(), points, BarcodeFormat.PDF417);
	}

	public void reset() {
		// do nothing
	}

}
