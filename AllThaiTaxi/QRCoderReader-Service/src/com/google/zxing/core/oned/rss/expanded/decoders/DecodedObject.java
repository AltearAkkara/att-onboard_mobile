package com.google.zxing.core.oned.rss.expanded.decoders;

public abstract class DecodedObject {

	protected final int newPosition;

	public DecodedObject(int newPosition){
		this.newPosition = newPosition;
	}

	public int getNewPosition() {
		return this.newPosition;
	}

}
