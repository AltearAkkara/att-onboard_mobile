package com.google.zxing.core.oned.rss.expanded.decoders;

public final class CurrentParsingState {

	int position;
	private int encoding;

	private static final int NUMERIC     = 1;
	private static final int ALPHA       = 2;
	private static final int ISO_IEC_646 = 4;

	public CurrentParsingState(){
		this.position = 0;
		this.encoding = NUMERIC;
	}

	public boolean isAlpha(){
		return this.encoding == ALPHA;
	}

	public boolean isNumeric(){
		return this.encoding == NUMERIC;
	}

	public boolean isIsoIec646(){
		return this.encoding == ISO_IEC_646;
	}

	public void setNumeric(){
		this.encoding = NUMERIC;
	}

	public void setAlpha(){
		this.encoding = ALPHA;
	}

	public void setIsoIec646(){
		this.encoding = ISO_IEC_646;
	}
}
