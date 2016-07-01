package com.google.zxing.core;

public abstract class ReaderException extends Exception {

	ReaderException() {
	    // do nothing
	}
	
	public final Throwable fillInStackTrace() {
	    return null;
	}
	
}
