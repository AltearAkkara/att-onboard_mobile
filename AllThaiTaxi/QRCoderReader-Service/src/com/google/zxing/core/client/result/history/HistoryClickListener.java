package com.google.zxing.core.client.result.history;

import java.util.List;
import android.net.Uri;
import android.os.Message;
import android.content.Intent;
import android.app.AlertDialog;
import com.google.zxing.core.Result;
import com.melody.mobile.android.qrcode.CaptureActivity;

import android.content.DialogInterface;
import com.truelife.mobile.android.qrcode.R;

final class HistoryClickListener implements DialogInterface.OnClickListener {

	  private final HistoryManager historyManager;
	  private final CaptureActivity activity;
	  private final String[] dialogItems;
	  private final List<Result> items;

	  HistoryClickListener(HistoryManager historyManager, CaptureActivity activity, String[] dialogItems, List<Result> items) {
	    this.historyManager = historyManager;
	    this.activity = activity;
	    this.dialogItems = dialogItems;
	    this.items = items;
	  }

	  public void onClick(DialogInterface dialogInterface, int i) {
    
		  if (i == dialogItems.length - 1) {
			  
			  historyManager.clearHistory();
		  } else if (i == dialogItems.length - 2) {
      
			  CharSequence history = historyManager.buildHistory();
			  Uri historyFile = HistoryManager.saveHistory(history.toString());
			  if (historyFile == null) {
				  AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				  builder.setMessage(R.string.msg_unmount_usb);
				  builder.setPositiveButton(R.string.button_ok, null);
				  builder.show();
				  return;
			  }
      
			  Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
			  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			  String subject = activity.getResources().getString(R.string.history_email_title);
			  intent.putExtra(Intent.EXTRA_SUBJECT, subject);
			  intent.putExtra(Intent.EXTRA_TEXT, subject);
			  intent.putExtra(Intent.EXTRA_STREAM, historyFile);
			  intent.setType("text/csv");
			  activity.startActivity(intent);
		  } else {
			  Result result = items.get(i);
			  Message message = Message.obtain(activity.getHandler(), R.id.decode_succeeded, result);
			  message.sendToTarget();
		  }
}

}
