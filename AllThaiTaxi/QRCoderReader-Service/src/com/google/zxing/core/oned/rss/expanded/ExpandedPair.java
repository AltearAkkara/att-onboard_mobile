package com.google.zxing.core.oned.rss.expanded;

import com.google.zxing.core.oned.rss.DataCharacter;
import com.google.zxing.core.oned.rss.FinderPattern;

public final class ExpandedPair {

	private final boolean mayBeLast;
	private final DataCharacter leftChar;
	private final DataCharacter rightChar;
	private final FinderPattern finderPattern;

	public ExpandedPair(DataCharacter leftChar, DataCharacter rightChar, FinderPattern finderPattern, boolean mayBeLast) {
    
		this.leftChar = leftChar;
		this.rightChar = rightChar;
		this.finderPattern = finderPattern;
		this.mayBeLast = mayBeLast;
	}

	public boolean mayBeLast(){
		return this.mayBeLast;
	}

	public DataCharacter getLeftChar() {
		return this.leftChar;
	}

	public DataCharacter getRightChar() {
		return this.rightChar;
	}

	public FinderPattern getFinderPattern() {
		return this.finderPattern;
	}

	public boolean mustBeLast() {
		return this.rightChar == null;
	}
}
