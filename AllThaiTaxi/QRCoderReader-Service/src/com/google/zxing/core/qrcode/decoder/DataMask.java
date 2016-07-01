package com.google.zxing.core.qrcode.decoder;

import com.google.zxing.core.common.BitMatrix;

public abstract class DataMask {

	private static final DataMask[] DATA_MASKS = {
		new DataMask000(),
		new DataMask001(),
		new DataMask010(),
		new DataMask011(),
		new DataMask100(),
		new DataMask101(),
		new DataMask110(),
		new DataMask111(),
	};

	private DataMask() {
	
	}

	/**
	 * <p>Implementations of this method reverse the data masking process applied to a QR Code and
	 * make its bits ready to read.</p>
	 *
	 * @param bits representation of QR Code bits
	 * @param dimension dimension of QR Code, represented by bits, being unmasked
	 */
	public final void unmaskBitMatrix(BitMatrix bits, int dimension) {
		
		for (int i = 0; i < dimension; i++) {
    	
			for (int j = 0; j < dimension; j++) {
        
				if (isMasked(i, j)) {
					bits.flip(j, i);
				}
			}
		}
	}

	public abstract boolean isMasked(int i, int j);

	/**
	 * @param reference a value between 0 and 7 indicating one of the eight possible
	 * data mask patterns a QR Code may use
	 * @return {@link DataMask} encapsulating the data mask pattern
	 */
	public static DataMask forReference(int reference) {
    
		if (reference < 0 || reference > 7) {
			throw new IllegalArgumentException();
		}
		return DATA_MASKS[reference];
	}

	/**
	 * 000: mask bits for which (x + y) mod 2 == 0
	 */
	private static class DataMask000 extends DataMask {
		
		public boolean isMasked(int i, int j) {
			return ((i + j) & 0x01) == 0;
		}
	}

	/**
	 * 001: mask bits for which x mod 2 == 0
	 */
	private static class DataMask001 extends DataMask {
		
		public boolean isMasked(int i, int j) {
			return (i & 0x01) == 0;
		}
	}

	/**
	 * 010: mask bits for which y mod 3 == 0
	 */
	private static class DataMask010 extends DataMask {
		
		public boolean isMasked(int i, int j) {
			return j % 3 == 0;
		}
	}

	/**
	 * 011: mask bits for which (x + y) mod 3 == 0
	 */
	private static class DataMask011 extends DataMask {
   
		public boolean isMasked(int i, int j) {
			return (i + j) % 3 == 0;
		}
	}

	/**
	 * 100: mask bits for which (x/2 + y/3) mod 2 == 0
	 */
	private static class DataMask100 extends DataMask {
    
		public boolean isMasked(int i, int j) {
			return (((i >>> 1) + (j /3)) & 0x01) == 0;
		}
	}

	/**
	 * 101: mask bits for which xy mod 2 + xy mod 3 == 0
	 */
	private static class DataMask101 extends DataMask {
    
		public boolean isMasked(int i, int j) {
			int temp = i * j;
			return (temp & 0x01) + (temp % 3) == 0;
		}
	}

	/**
	 * 110: mask bits for which (xy mod 2 + xy mod 3) mod 2 == 0
	 */
	private static class DataMask110 extends DataMask {
		
		public boolean isMasked(int i, int j) {
			int temp = i * j;
			return (((temp & 0x01) + (temp % 3)) & 0x01) == 0;
		}
	}

	/**
	 * 111: mask bits for which ((x+y)mod 2 + xy mod 3) mod 2 == 0
	 */
	private static class DataMask111 extends DataMask {
    
		public boolean isMasked(int i, int j) {
			return ((((i + j) & 0x01) + ((i * j) % 3)) & 0x01) == 0;
		}
	}
}