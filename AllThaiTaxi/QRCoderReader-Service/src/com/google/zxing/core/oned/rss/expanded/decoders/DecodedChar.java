package com.google.zxing.core.oned.rss.expanded.decoders;

public final class DecodedChar extends DecodedObject {

	private final char value;

	public static final char FNC1 = '$'; // It's not in Alphanumeric neither in ISO/IEC 646 charset

	public DecodedChar(int newPosition, char value) {
		super(newPosition);
		this.value = value;
	}

	public char getValue(){
		return this.value;
	}

	public boolean isFNC1(){
		return this.value == FNC1;
	}

}
