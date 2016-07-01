package com.google.zxing.core.oned.rss.expanded.decoders;

public final class DecodedInformation extends DecodedObject {
  
	private final String newString;
	private final int remainingValue;
	private final boolean remaining;

	public DecodedInformation(int newPosition, String newString){
    
		super(newPosition);
		this.newString      = newString;
		this.remaining      = false;
		this.remainingValue = 0;
	}

	public DecodedInformation(int newPosition, String newString, int remainingValue){
	  
		super(newPosition);
		this.remaining      = true;
		this.remainingValue = remainingValue;
		this.newString      = newString;
	}

	public String getNewString(){
		return this.newString;
	}

	public boolean isRemaining(){
		return this.remaining;
	}

	public int getRemainingValue(){
		return this.remainingValue;
	}
}
