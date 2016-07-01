package com.google.zxing.core.oned;

import java.util.Vector;
import java.util.Hashtable;
import com.google.zxing.core.Result;
import com.google.zxing.core.Reader;
import com.google.zxing.core.BarcodeFormat;
import com.google.zxing.core.DecodeHintType;
import com.google.zxing.core.ReaderException;
import com.google.zxing.core.common.BitArray;
import com.google.zxing.core.NotFoundException;
import com.google.zxing.core.oned.rss.RSS14Reader;
import com.google.zxing.core.oned.rss.expanded.RSSExpandedReader;

public final class MultiFormatOneDReader extends OneDReader {

	private final Vector readers;

	public MultiFormatOneDReader(Hashtable hints) {
		
		Vector possibleFormats = hints == null ? null : (Vector) hints.get(DecodeHintType.POSSIBLE_FORMATS);
		boolean useCode39CheckDigit = hints != null && hints.get(DecodeHintType.ASSUME_CODE_39_CHECK_DIGIT) != null;
    
		readers = new Vector();
		if (possibleFormats != null) {
			
			if (possibleFormats.contains(BarcodeFormat.EAN_13) || possibleFormats.contains(BarcodeFormat.UPC_A) || possibleFormats.contains(BarcodeFormat.EAN_8) || possibleFormats.contains(BarcodeFormat.UPC_E)) {
				readers.addElement(new MultiFormatUPCEANReader(hints));
			}
			
			if (possibleFormats.contains(BarcodeFormat.CODE_39)) {
				readers.addElement(new Code39Reader(useCode39CheckDigit));
			}
			
			if (possibleFormats.contains(BarcodeFormat.CODE_93)) {
				readers.addElement(new Code93Reader());
			}
			
			if (possibleFormats.contains(BarcodeFormat.CODE_128)) {
				readers.addElement(new Code128Reader());
			}
			
			if (possibleFormats.contains(BarcodeFormat.ITF)) {
				readers.addElement(new ITFReader());
			}
			
			if (possibleFormats.contains(BarcodeFormat.CODABAR)) {
				readers.addElement(new CodaBarReader());
			}
			
			if (possibleFormats.contains(BarcodeFormat.RSS14)) {
				readers.addElement(new RSS14Reader());
			}
			
			if (possibleFormats.contains(BarcodeFormat.RSS_EXPANDED)){
				readers.addElement(new RSSExpandedReader());
			}
		}
		
		if(readers.isEmpty()) {
			
			readers.addElement(new MultiFormatUPCEANReader(hints));
			readers.addElement(new Code39Reader());
			//readers.addElement(new CodaBarReader());
			readers.addElement(new Code93Reader());      
			readers.addElement(new Code128Reader());
			readers.addElement(new ITFReader());
			readers.addElement(new RSS14Reader());      
			readers.addElement(new RSSExpandedReader());      
		}
		
	}

	public Result decodeRow(int rowNumber, BitArray row, Hashtable hints) throws NotFoundException {
    
		int size = readers.size();
		
		for (int i = 0; i < size; i++) {
			OneDReader reader = (OneDReader) readers.elementAt(i);
			try {
				return reader.decodeRow(rowNumber, row, hints);
			} catch (ReaderException re) {
				// continue
			}
		}
		throw NotFoundException.getNotFoundInstance();
	}

	public void reset() {
    
		int size = readers.size();
    
		for (int i = 0; i < size; i++) {
			Reader reader = (Reader) readers.elementAt(i);
			reader.reset();
		}
	}

}
