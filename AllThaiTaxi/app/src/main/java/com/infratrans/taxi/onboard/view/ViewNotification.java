package com.infratrans.taxi.onboard.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.infratrans.taxi.onboard.R;
import com.infratrans.taxi.onboard.json.JSONParser;
import com.infratrans.taxi.onboard.shares.AppSharedPreferences;
import com.infratrans.taxi.onboard.shares.LoginHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Melody on 2/6/15 AD.
 */
public class ViewNotification extends RelativeLayout{

    private AQuery aq;
    private Context context;
    private HashMap<String, Object> data;

    private View v = null;
    private LayoutInflater vi = null;
    private ViewNotifyIncidentListener listener;
    private MyCountMedia timerCountMedia;
    private MediaPlayer mediaPlayer;
    private boolean play_sound = false;
    private String reserved_type;
    private String enable_credit_card;

    public ViewNotification(Context context, HashMap<String, Object> data, boolean play_sound, ViewNotifyIncidentListener listener, String reserved_type, String enable_credit_card) {

        super(context);
        this.data = data;
        this.context = context;
        this.listener = listener;
        this.play_sound = play_sound;
        this.reserved_type = reserved_type;
        this.enable_credit_card = enable_credit_card;

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.dialog_notification, null);
        addView(v);

        mediaPlayer = MediaPlayer.create(context, R.raw.sonar);
        mediaPlayer.setLooping(true);
        timerCountMedia = new MyCountMedia(3000, 1000);
        timerCountMedia.start();


        init();
    }

    private JSONParser jsonP;
    private void takeScreenshot(String job_id) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = df.format(c.getTime());


        try {

            // image naming and path  to include sd card  appending name you choose for file
//            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture

            v.getRootView();
            v.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);
            bitmap = Bitmap.createScaledBitmap(bitmap,640,400,false);

//            File imageFile = new File(mPath);
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 20;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//            outputStream.flush();
//            outputStream.close();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream);
            byte[] array = stream.toByteArray();

//            int bytes = bitmap.getByteCount();
//            ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
//            bitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
//            byte[] array = buffer.array(); //Get the underlying array containing the data.

            sendScreenShot(array , formattedDate,job_id);
//            Toast.makeText(getContext().getApplicationContext(),array + formattedDate,Toast.LENGTH_LONG).show();



//            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            takeScreenshot(job_id);
        }
    }

    private  void sendScreenShot(final byte[] array,final String formattedDate,final String job_id){

        String url = "";
        Map<String, Object> params = null;
        url = "http://taxiapi.itsconsultancy.co.th/dtamap/modules/php/capturescreen.php";
        params = new HashMap<String, Object>();
        params.put("job_id", job_id);
        params.put("image", array);
        params.put("datetime", formattedDate);
        params.put("taxi_id", LoginHelper.getInstance(context).getTaxiID());
        aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (status.getCode() == 200) {
                    String jsonStr = json.toString();
                    try {
                        jsonP = new JSONParser(jsonStr);
                        HashMap<String, Object> resultObj = (HashMap<String, Object>) jsonP.convertJson2HashMap();
                        if (!String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {
                            sendScreenShot(array, formattedDate,job_id);
                        }
                    } catch (JSONException e) {
                        sendScreenShot(array, formattedDate,job_id);
                    }
                }
                else{
                    sendScreenShot(array, formattedDate,job_id);
                }
            }
        });
    }



    private void init() {

        aq = new AQuery((Activity) this.context, v);
        if(reserved_type.equals("1"))
        {
            aq.id(R.id.noti_app).visible();
//            if(enable_credit_card.equals("1")){
//                aq.id(R.id.noti_visa).visible();
//            }
        }
        if(reserved_type.equals("2"))
        {
            aq.id(R.id.noti_reserved).visible();
        }

        aq.id(R.id.notification_date).text(this.context.getString(R.string.notification_date) + " " + String.valueOf(data.get("request_date")));
        aq.id(R.id.notification_passenger_name).text(context.getString(R.string.notification_passenger_name) + String.valueOf(data.get("cust_name")));
        aq.id(R.id.notification_pick).text(context.getString(R.string.notification_pick) + String.valueOf(data.get("location_title")));
        aq.id(R.id.notification_destination).text(context.getString(R.string.notification_destination) + String.valueOf(data.get("destination_title")));
                new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                takeScreenshot(String.valueOf(data.get("job_id")));
            }
        }, 1000);
        aq.id(R.id.notification_btn_ok).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                aq.id(R.id.noti_app).visibility(View.GONE);
//                aq.id(R.id.noti_visa).visibility(View.GONE);
                aq.id(R.id.noti_reserved).visibility(View.GONE);

                if(!mediaPlayer.isPlaying())
                {
                    timerCountMedia.cancel();
                    timerCountMedia = null;
                    mediaPlayer = null;
                }
                else if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                }

                if (listener != null)
                    listener.viewNotifyIncidentAccept(ViewNotification.this.data);
            }
        });
    }

    //timermedia
    public class MyCountMedia extends CountDownTimer {
        public MyCountMedia(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish(){

            mediaPlayer.start();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //some script here
        }
    }

    public interface ViewNotifyIncidentListener {

        void viewNotifyIncidentAccept(HashMap<String, Object> data);
    }



}