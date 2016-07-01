package com.melody.mobile.android.qrcode.result;

import android.app.Activity;
import com.google.zxing.core.Result;
import com.google.zxing.core.client.result.ParsedResult;
import com.google.zxing.core.client.result.ResultParser;
import com.google.zxing.core.client.result.ParsedResultType;

public final class ResultHandlerFactory {
  
	private ResultHandlerFactory() {
	
	}

	public static ResultHandler makeResultHandler(Activity activity, Result rawResult) {
    
		ParsedResult result = parseResult(rawResult);
		ParsedResultType type = result.getType();
		/*
		if (type.equals(ParsedResultType.ADDRESSBOOK)) {
			return new AddressBookResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.EMAIL_ADDRESS)) {
			return new EmailAddressResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.PRODUCT)) {
			return new ProductResultHandler(activity, result, rawResult);
		} else if (type.equals(ParsedResultType.URI)) {
			return new URIResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.WIFI)) {
			return new WifiResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.TEXT)) {
			return new TextResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.GEO)) {
			return new GeoResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.TEL)) {
			return new TelResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.SMS)) {
			return new SMSResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.CALENDAR)) {
			return new CalendarResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.ISBN)) {
			return new ISBNResultHandler(activity, result, rawResult);
		} else {
			// The TextResultHandler is the fallthrough for unsupported formats.
			return new TextResultHandler(activity, result);
		}
		*/
		return new TextResultHandler(activity, result);
	}

	private static ParsedResult parseResult(Result rawResult) {
		return ResultParser.parseResult(rawResult);
	}
}
