package com.google.zxing.core.oned.rss.expanded.decoders;

public final class DecodedNumeric extends DecodedObject {

	private final int firstDigit;
	private final int secondDigit;

	public static final int FNC1 = 10;

	public DecodedNumeric(int newPosition, int firstDigit, int secondDigit){
    
		super(newPosition);

		this.firstDigit  = firstDigit;
		this.secondDigit = secondDigit;

		if (this.firstDigit < 0 || this.firstDigit > 10) {
			throw new IllegalArgumentException("Invalid firstDigit: " + firstDigit);
		}

		if (this.secondDigit < 0 || this.secondDigit > 10) {
			throw new IllegalArgumentException("Invalid secondDigit: " + secondDigit);
		}
	}

	public int getFirstDigit(){
		return this.firstDigit;
	}

	public int getSecondDigit(){
		return this.secondDigit;
	}

	public int getValue(){
		return this.firstDigit * 10 + this.secondDigit;
	}

	public boolean isFirstDigitFNC1(){
		return this.firstDigit == FNC1;
	}

	public boolean isSecondDigitFNC1(){
		return this.secondDigit == FNC1;
	}

	public boolean isAnyFNC1(){
		return this.firstDigit == FNC1 || this.secondDigit == FNC1;
	}

}
