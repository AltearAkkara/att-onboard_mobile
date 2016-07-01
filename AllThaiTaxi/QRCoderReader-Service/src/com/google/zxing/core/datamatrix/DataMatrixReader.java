package com.google.zxing.core.datamatrix;

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
import com.google.zxing.core.ChecksumException;
import com.google.zxing.core.ResultMetadataType;
import com.google.zxing.core.common.DecoderResult;
import com.google.zxing.core.common.DetectorResult;
import com.google.zxing.core.datamatrix.decoder.Decoder;
import com.google.zxing.core.datamatrix.detector.Detector;

public final class DataMatrixReader implements Reader {

	private static final ResultPoint[] NO_POINTS = new ResultPoint[0];
	private final Decoder decoder = new Decoder();
	
	public Result decode(BinaryBitmap image) throws NotFoundException, ChecksumException, FormatException {
		
		return decode(image, null);
	}

	public Result decode(BinaryBitmap image, Hashtable hints) throws NotFoundException, ChecksumException, FormatException {
	    
		DecoderResult decoderResult;
	    ResultPoint[] points;
	    if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
	    	BitMatrix bits = extractPureBits(image.getBlackMatrix());
	    	decoderResult = decoder.decode(bits);
	    	points = NO_POINTS;
	    } else {
	    	DetectorResult detectorResult = new Detector(image.getBlackMatrix()).detect();
	    	decoderResult = decoder.decode(detectorResult.getBits());
	    	points = detectorResult.getPoints();
	    }
	    
	    Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points, BarcodeFormat.DATA_MATRIX);
	    if (decoderResult.getByteSegments() != null) {
	    	result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, decoderResult.getByteSegments());
	    }
	    
	    if (decoderResult.getECLevel() != null) {
	    	result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, decoderResult.getECLevel().toString());
	    }
	    
	    return result;
	}

	public void reset() {
	    // do nothing
	}

	/**
	 * This method detects a Data Matrix code in a "pure" image -- that is, pure monochrome image
	 * which contains only an unrotated, unskewed, image of a Data Matrix code, with some white border
	 * around it. This is a specialized method that works exceptionally fast in this special
	 * case.
	 *
	 * @see com.google.zxing.qrcode.QRCodeReader#extractPureBits(BitMatrix)
	 */
	private static BitMatrix extractPureBits(BitMatrix image) throws NotFoundException {

	    int height = image.getHeight();
	    int width = image.getWidth();
	    int minDimension = Math.min(height, width);

	    // And then keep tracking across the top-left black module to determine module size
	    //int moduleEnd = borderWidth;
	    int[] leftTopBlack = image.getTopLeftOnBit();
	    if (leftTopBlack == null) {
	    	throw NotFoundException.getNotFoundInstance();
	    }
	    
	    int x = leftTopBlack[0];
	    int y = leftTopBlack[1];
	    while (x < minDimension && y < minDimension && image.get(x, y)) {
	    	x++;
	    }
	    
	    if (x == minDimension) {
	    	throw NotFoundException.getNotFoundInstance();
	    }

	    int moduleSize = x - leftTopBlack[0];

	    // And now find where the rightmost black module on the first row ends
	    int rowEndOfSymbol = width - 1;
	    while (rowEndOfSymbol >= 0 && !image.get(rowEndOfSymbol, y)) {
	    	rowEndOfSymbol--;
	    }
	    
	    if (rowEndOfSymbol < 0) {
	    	throw NotFoundException.getNotFoundInstance();
	    }
	    rowEndOfSymbol++;

	    // Make sure width of barcode is a multiple of module size
	    if ((rowEndOfSymbol - x) % moduleSize != 0) {
	    	throw NotFoundException.getNotFoundInstance();
	    }
	    int dimension = 2 + ((rowEndOfSymbol - x) / moduleSize);
	    y += moduleSize;

	    // Push in the "border" by half the module width so that we start
	    // sampling in the middle of the module. Just in case the image is a
	    // little off, this will help recover.
	    x -= moduleSize >> 1;
	    y -= moduleSize >> 1;

	    if ((x + (dimension - 1) * moduleSize) >= width || (y + (dimension - 1) * moduleSize) >= height) {
	    	throw NotFoundException.getNotFoundInstance();
	    }

	    // Now just read off the bits
	    BitMatrix bits = new BitMatrix(dimension);
	    for (int i = 0; i < dimension; i++) {
	    	int iOffset = y + i * moduleSize;
	    	for (int j = 0; j < dimension; j++) {
	    		if (image.get(x + j * moduleSize, iOffset)) {
	    			bits.set(j, i);
	    		}
	    	}
	    }
	    return bits;
	}
	
}
