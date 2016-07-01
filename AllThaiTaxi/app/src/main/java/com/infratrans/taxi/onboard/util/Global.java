package com.infratrans.taxi.onboard.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.infratrans.taxi.onboard.R;

public class Global {

    public static final int ACTION_REQUEST_QRCODE_READER = 101;

    public static String getDeviceID(Context context) {

        //return "64fd5d3cef3b6ea6";
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public static void printLog(boolean enable, String key, String value) {

        if (enable)
            Log.e(key, value);
    }

    public static int getScreenWidth(Context context) {

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.heightPixels;
    }

    public static void showAlertDialog(Context context, String message) {

        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.dialog_message, null);

        TextView dialog_msg = (TextView) v.findViewById(R.id.alert_msg);
        dialog_msg.setText(message);

        TextView dialog_confirm = (TextView) v.findViewById(R.id.alert_btn_ok);

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);


        dialog_confirm.setTag((Dialog) dialog);
        dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = (Dialog) v.getTag();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void startCall(Context context, String tel) {

        ((Activity) context).startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel)));
    }
}
