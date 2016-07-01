package com.google.zxing.core.oned.rss.expanded.decoders;

public final class BlockParsedResult {

	private final DecodedInformation decodedInformation;
	private final boolean finished;

	public BlockParsedResult() {
		this.finished = true;
		this.decodedInformation = null;
	}

	public BlockParsedResult(boolean finished) {
		this.finished = finished;
		this.decodedInformation = null;
	}

	public BlockParsedResult(DecodedInformation information, boolean finished) {
		this.finished = finished;
		this.decodedInformation = information;
	}

	public DecodedInformation getDecodedInformation() {
		return this.decodedInformation;
	}

	public boolean isFinished() {
		return this.finished;
	}
}
