package com.google.zxing.core.oned.rss;

public final class Pair extends DataCharacter {

	private final FinderPattern finderPattern;
	private int count;

	public Pair(int value, int checksumPortion, FinderPattern finderPattern) {
		super(value, checksumPortion);
		this.finderPattern = finderPattern;
	}

	public FinderPattern getFinderPattern() {
		return finderPattern;
	}

	public int getCount() {
		return count;
	}

	public void incrementCount() {
		count++;
	}

}