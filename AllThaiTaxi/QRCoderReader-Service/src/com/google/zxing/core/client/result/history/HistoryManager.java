package com.google.zxing.core.client.result.history;

import java.io.File;
import java.util.Date;
import java.util.List;
import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.text.DateFormat;
import android.os.Environment;
import android.app.AlertDialog;
import android.database.Cursor;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.io.OutputStreamWriter;
import com.google.zxing.core.Result;
import android.content.ContentValues;
import android.content.res.Resources;
import android.content.DialogInterface;
import com.google.zxing.core.BarcodeFormat;
import com.melody.mobile.android.qrcode.CaptureActivity;
import com.melody.mobile.android.qrcode.Intents;
import com.truelife.mobile.android.qrcode.R;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class HistoryManager {

	private static final String TAG = HistoryManager.class.getSimpleName();

	private static final int MAX_ITEMS = 50;
	private static final String[] TEXT_COL_PROJECTION = { DBHelper.TEXT_COL };
	private static final String[] GET_ITEM_COL_PROJECTION = {
		DBHelper.TEXT_COL,
		DBHelper.FORMAT_COL,
		DBHelper.TIMESTAMP_COL,
	};
	
	private static final String[] EXPORT_COL_PROJECTION = {
		DBHelper.TEXT_COL,
		DBHelper.DISPLAY_COL,
		DBHelper.FORMAT_COL,
		DBHelper.TIMESTAMP_COL,
	};
	
	private static final String[] ID_COL_PROJECTION = { DBHelper.ID_COL };
	private static final DateFormat EXPORT_DATE_TIME_FORMAT = DateFormat.getDateTimeInstance();
	
	private final CaptureActivity activity;

	public HistoryManager(CaptureActivity activity) {
		this.activity = activity;
	}

	List<Result> getHistoryItems() {
		SQLiteOpenHelper helper = new DBHelper(activity);
	    List<Result> items = new ArrayList<Result>();
	    SQLiteDatabase db = helper.getReadableDatabase();
	    Cursor cursor = null;
	    
	    try {
	    	cursor = db.query(DBHelper.TABLE_NAME, GET_ITEM_COL_PROJECTION, null, null, null, null, DBHelper.TIMESTAMP_COL + " DESC");
	    	while (cursor.moveToNext()) {
	    		Result result = new Result(cursor.getString(0), null, null, BarcodeFormat.valueOf(cursor.getString(1)), cursor.getLong(2));
	    		items.add(result);
	    	}
	    } finally {
	    	if (cursor != null) {
	    		cursor.close();
	    	}
	    	db.close();
	    }
	    return items;
	}

	public AlertDialog buildAlert() {
	    
		List<Result> items = getHistoryItems();
	    int size = items.size();
	    String[] dialogItems = new String[size + 2];
	    for (int i = 0; i < size; i++) {
	    	dialogItems[i] = items.get(i).getText();
	    }
	    Resources res = activity.getResources();
	    dialogItems[dialogItems.length - 2] = res.getString(R.string.history_send);
	    dialogItems[dialogItems.length - 1] = res.getString(R.string.history_clear_text);
	    DialogInterface.OnClickListener clickListener = new HistoryClickListener(this, activity, dialogItems, items);
	    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    builder.setTitle(R.string.history_title);
	    builder.setItems(dialogItems, clickListener);
	    return builder.create();
	}

	public void addHistoryItem(Result result) {
	
	    if (!activity.getIntent().getBooleanExtra(Intents.Scan.SAVE_HISTORY, true)) {
	    	return; // Do not save this item to the history.
	    }
	
	    SQLiteOpenHelper helper = new DBHelper(activity);
	    SQLiteDatabase db = helper.getWritableDatabase();
	    try {
	    	// Delete if already exists
	    	db.delete(DBHelper.TABLE_NAME, DBHelper.TEXT_COL + "=?", new String[] { result.getText() });
	    	// Insert
	    	ContentValues values = new ContentValues();
	    	values.put(DBHelper.TEXT_COL, result.getText());
	    	values.put(DBHelper.FORMAT_COL, result.getBarcodeFormat().toString());
	    	values.put(DBHelper.DISPLAY_COL, result.getText()); // TODO use parsed result display value?
	    	values.put(DBHelper.TIMESTAMP_COL, System.currentTimeMillis());
	    	db.insert(DBHelper.TABLE_NAME, DBHelper.TIMESTAMP_COL, values);
	    } finally {
	    	db.close();
	    }
	}
	
	public void trimHistory() {
	    
		SQLiteOpenHelper helper = new DBHelper(activity);
	    SQLiteDatabase db = helper.getWritableDatabase();
	    Cursor cursor = null;
	    try {
	    	cursor = db.query(DBHelper.TABLE_NAME, ID_COL_PROJECTION, null, null, null, null, DBHelper.TIMESTAMP_COL + " DESC");
	    	int count = 0;
	    	while (count < MAX_ITEMS && cursor.moveToNext()) {
	    		count++;
	    	}
	    	while (cursor.moveToNext()) {
	    		db.delete(DBHelper.TABLE_NAME, DBHelper.ID_COL + '=' + cursor.getString(0), null);
	    	}
	    } finally {
	    	if (cursor != null) {
	    		cursor.close();
	    	}
	    	db.close();
	    }
	}
	
	  /**
	   * <p>Builds a text representation of the scanning history. Each scan is encoded on one
	   * line, terminated by a line break (\r\n). The values in each line are comma-separated,
	   * and double-quoted. Double-quotes within values are escaped with a sequence of two
	   * double-quotes. The fields output are:</p>
	   *
	   * <ul>
	   *  <li>Raw text</li>
	   *  <li>Display text</li>
	   *  <li>Format (e.g. QR_CODE)</li>
	   *  <li>Timestamp</li>
	   *  <li>Formatted version of timestamp</li>
	   * </ul>
	   */
	CharSequence buildHistory() {
		StringBuilder historyText = new StringBuilder(1000);
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(DBHelper.TABLE_NAME, EXPORT_COL_PROJECTION, null, null, null, null, DBHelper.TIMESTAMP_COL + " DESC");
			while (cursor.moveToNext()) {
				
				for (int col = 0; col < EXPORT_COL_PROJECTION.length; col++) {
					historyText.append('"').append(massageHistoryField(cursor.getString(col))).append("\",");
				}
				// Add timestamp again, formatted
				long timestamp = cursor.getLong(EXPORT_COL_PROJECTION.length - 1);
				historyText.append('"').append(massageHistoryField(EXPORT_DATE_TIME_FORMAT.format(new Date(timestamp)))).append("\"\r\n");
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			db.close();
		}
		return historyText;
	}
	
	static Uri saveHistory(String history) {
	    
		File bsRoot = new File(Environment.getExternalStorageDirectory(), "BarcodeScanner");
	    File historyRoot = new File(bsRoot, "History");
	    if (!historyRoot.exists() && !historyRoot.mkdirs()) {
	    	Log.w(TAG, "Couldn't make dir " + historyRoot);
	    	return null;
	    }
	    File historyFile = new File(historyRoot, "history-" + System.currentTimeMillis() + ".csv");
	    OutputStreamWriter out = null;
	    try {
	    	out = new OutputStreamWriter(new FileOutputStream(historyFile), Charset.forName("UTF-8"));
	    	out.write(history);
	    	return Uri.parse("file://" + historyFile.getAbsolutePath());
	    } catch (IOException ioe) {
	    	Log.w(TAG, "Couldn't access file " + historyFile + " due to " + ioe);
	    	return null;
	    } finally {
	    	if (out != null) {
	    		try {
	    			out.close();
	    		} catch (IOException ioe) {
	    			// do nothing
	    		}
	    	}
	    }
	}
	
	private static String massageHistoryField(String value) {
	    return value.replace("\"","\"\"");
	}
	
	void clearHistory() {
	    
		SQLiteOpenHelper helper = new DBHelper(activity);
	    SQLiteDatabase db = helper.getWritableDatabase();
	    try {
	    	db.delete(DBHelper.TABLE_NAME, null, null);
	    } finally {
	    	db.close();
	    }
	}

}
