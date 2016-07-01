package com.infratrans.taxi.onboard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.infratrans.taxi.onboard.json.JSONParser;
import com.infratrans.taxi.onboard.services.DownloadService;
import com.infratrans.taxi.onboard.shares.AppSharedPreferences;
import com.infratrans.taxi.onboard.shares.LoginHelper;
import com.infratrans.taxi.onboard.util.APIs;
import com.infratrans.taxi.onboard.util.Global;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

//import com.melody.mobile.android.qrcode.CaptureActivity;

/**
 * Created by Thanisak Piyasaksiri on 2/5/15 AD.
 * Modefied by Thanisak Piyasaksiri on 4/19/15 AD.
 */
public class LoginActivity extends FragmentActivity implements View.OnClickListener, QRCodeReaderView.OnQRCodeReadListener {



    public static final int progress_bar_type = 0;
    private ProgressDialog pDialog;
    private static long back_pressed;
    String currentDateTimeString;
    final Handler handler = new Handler();
    final String version = "1.9.1";
    //oak
    MyCount timerCount;
    private double check_last_km;
    private int check_last_km1;
    private JSONParser json;
    private HashMap<String, Object> result;
    private Bundle extras;
    private String taxi_id = "";
    private AQuery aq;
    private IntentFilter filter;
    private Intent download_service;
    private Timer timerNetwork;
    private TimerTask timerTaskNetwork;
    private String status="";
    private boolean qrshown = false;


    private MediaPlayer StatusSpeak;
    private URI StausSpeakerUri;
    String str_preTextUrl_status = "http://translate.google.com/translate_tts?ie=UTF-8&q=";
    String str_textUrl_status = "";
    String str_postTextUrl_status = "&tl=th";
    public void StatusSpeak(String textSpeaker) {
        try {
            if(textSpeaker != null && !textSpeaker.isEmpty()) {
                StatusSpeak = new MediaPlayer();
                StatusSpeak.setAudioStreamType(AudioManager.STREAM_MUSIC);
                str_textUrl_status = textSpeaker;
                StausSpeakerUri = new URI(str_preTextUrl_status + str_textUrl_status + str_postTextUrl_status);
                URL url = new URL(StausSpeakerUri.toASCIIString());
                StatusSpeak.setDataSource(url.toString());
                StatusSpeak.prepare();
                StatusSpeak.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (DownloadService.BROADCAST_ACTION.equalsIgnoreCase(intent.getAction())) {

                if (intent.hasExtra("status")) {

                    if (intent.getStringExtra("status").equalsIgnoreCase("500")) {

                        stopService(download_service);
                        download_service = null;
                        showErrorDialog();
                    } else if (intent.getStringExtra("status").equalsIgnoreCase("200")) {

                        if (intent.getBooleanExtra("completed", false) == false) {
                            //false
                            //if(aq != null)
                            //aq.id(R.id.login_progress_bar).progress(intent.getIntExtra("progress", 0));

                        } else {
                            //true
                            stopService(download_service);

                            String PATH = Environment.getExternalStorageDirectory() + "/" + getString(R.string.download_path) + "/download/" + getString(R.string.app_file_name);
                            Intent intent_apk = new Intent(Intent.ACTION_VIEW);
                            intent_apk.setDataAndType(Uri.fromFile(new File(PATH)), "application/vnd.android.package-archive");
                            intent_apk.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent_apk);

                            //aq.id(R.id.login_downloading_frame).visibility(View.GONE);
                            //aq.id(R.id.login_progress_bar).progress(0);
                        }
                    }
                }
            }
        }
    };
    private QRCodeReaderView qrCodeReaderView;
    private Timer timer;
    private TimerTask timerTask;
/////////////////////////////////////////////////

    public void onDestroy() {

        if (download_service != null)
            stopService(download_service);


        download_service = null;

        super.onDestroy();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onBackPressed() {

        if(qrshown){
            aq.id(R.id.login_qr_code_frame).visibility(View.GONE);
            qrshown = false;
        }
        else {
            if (back_pressed + 2000 > System.currentTimeMillis())
                LoginActivity.this.finish();
            else
                Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();

            back_pressed = System.currentTimeMillis();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_login);

        filter = new IntentFilter();
        filter.addAction(DownloadService.BROADCAST_ACTION);

        aq = new AQuery(this);

        extras = getIntent().getExtras();
        taxi_id = extras.getString("taxi_id");
        aq.id(R.id.textView4).text("Taxi ID : " + taxi_id);
        aq.id(R.id.textView3).text("Version. "+getResources().getText(R.string.app_version));


        qrCodeReaderView = (QRCodeReaderView) aq.id(R.id.qrCodeReaderView).getView();
        qrCodeReaderView.setOnQRCodeReadListener(null);

        setProgressbarVisibility(false);
        init();
    }


    private boolean checkGooglePlayServices() {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {

            // ask user to update google play services.
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, 1);
            dialog.show();
            return false;
        } else {

            // google play services is updated.
            //your code goes here...
            return true;
        }
    }




    @Override
    public void onResume() {

        super.onResume();
        startTimerNetwork();
        registerReceiver(broadcastReceiver, filter);

    }

    @Override
    public void onPause() {

        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    /*
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case Global.ACTION_REQUEST_QRCODE_READER:

                    String stuff_id = data.getExtras().getString("result");

                    login(stuff_id, taxi_id);
                    break;
                default:
                    break;
            }
        }
    }
    */

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            String from = "http://taxiapi.itsconsultancy.co.th/passenger/app-release.apk";
            String path = "/sdcard/ATT/";


            try {
                URL url = new URL(from);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                File file = new File(path);


                if(!file.exists()){
                    file.mkdirs();
                }

                int lenghtOfFile = conexion.getContentLength(); // Size of file
                InputStream input = new BufferedInputStream(url.openStream());

                String fileName = "app-release.apk";
                OutputStream output = new FileOutputStream(path+fileName); // save to path sd card

                byte data[] = new byte[4096];
                long total = 0 ;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_bar_type);
            Toast.makeText(LoginActivity.this,getResources().getText(R.string.install), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/ATT/" +"app-release.apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

    }


    public void startTimerNetwork() {
        timerNetwork = new Timer();
        initializeTimerTaskNetwork();
        timerNetwork.schedule(timerTaskNetwork, 0, 5000); //

    }

    public void initializeTimerTaskNetwork() {
        timerTaskNetwork = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {

                        String Checkcom = APIs.getRegisterAPI();
                        aq.ajax(Checkcom, String.class, new AjaxCallback<String>() {
                            @Override
                            public void callback(String url, String data, AjaxStatus status) {

                                try {

                                    // Toast.makeText(getApplicationContext(), "Check Timer", Toast.LENGTH_SHORT).show();
                                    JSONParser json = new JSONParser(data);
                                    HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();

                                    if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {
                                        aq.id(R.id.imageView5).visibility(View.GONE);
                                        aq.id(R.id.imageView4).visible();
                                        // Toast.makeText(getApplicationContext(), "Com ok", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        aq.id(R.id.imageView4).visibility(View.GONE);
                                        aq.id(R.id.imageView5).visible();
                                    }

                                } catch (JSONException e) {
                                    aq.id(R.id.imageView4).visibility(View.GONE);
                                    aq.id(R.id.imageView5).visible();
                                    e.printStackTrace();
                                }
                                catch (RuntimeException e) {
                                    aq.id(R.id.imageView4).visibility(View.GONE);
                                    aq.id(R.id.imageView5).visible();
                                    e.printStackTrace();
                                }

                            }
                        });

//
//                        String url = "http://www.google.com";
//
//                        aq.ajax(url, String.class, new AjaxCallback<String>() {
//
//                            @Override
//                            public void callback(String url, String html, AjaxStatus status) {
//                                if(status.getCode() == 200){
//                                    aq.id(R.id.internetClose).visibility(View.GONE);
//                                    aq.id(R.id.internetOpen).visible();
//                                }
//                                else{
//                                    aq.id(R.id.internetOpen).visibility(View.GONE);
//                                    aq.id(R.id.internetClose).visible();
//                                }
//                            }
//
//                        });


//                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                        NetworkInfo ni = cm.getActiveNetworkInfo();
//                        if (ni == null) {
//                            // There are no active networks.
//                            aq.id(R.id.internetOpen).visibility(View.GONE);
//                            aq.id(R.id.internetClose).visible();
//                            //Toast.makeText(getApplicationContext(), "Internet No", Toast.LENGTH_SHORT).show();
//                        } else
//                        {
//                            aq.id(R.id.internetClose).visibility(View.GONE);
//                            aq.id(R.id.internetOpen).visible();
//                            //Toast.makeText(getApplicationContext(), "Internet Ok", Toast.LENGTH_SHORT).show();
//                        }

                        //ping("192.168.1.123");
                    }
                });
            }
        };
    }




    public void ping(String text) {
        try {
            URL url = new URL(text);

            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
            urlc.connect();

            if (urlc.getResponseCode() == 200) {
                aq.id(R.id.cam_on).visible();
                aq.id(R.id.cam_off).visibility(View.GONE);
            }
        } catch (MalformedURLException e1) {
            aq.id(R.id.cam_off).visible();
            aq.id(R.id.cam_on).visibility(View.GONE);
        } catch (IOException e) {
            aq.id(R.id.cam_off).visible();
            aq.id(R.id.cam_on).visibility(View.GONE);
        }
    }



    public void checkVersion(){


            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.dialog_message, null);

            TextView dialog_msg = (TextView) v.findViewById(R.id.alert_msg);
            dialog_msg.setText(getResources().getString(R.string.check_update) + "\n" + getResources().getString(R.string.check_con));

            TextView dialog_confirm = (TextView) v.findViewById(R.id.alert_btn_ok);
            dialog = new Dialog(this);
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

            dialog_confirm.setText(getResources().getString(R.string.btn_check));
            dialog_confirm.setTag((Dialog) dialog);
            dialog_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Uri uri = Uri.parse("market://details?id=com.infratrans.taxi.onboard&hl=th");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.

                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.infratrans.taxi.onboard&hl=th")));
                    }
                    closeDialog();
                }
            });

            dialog.show();






    }

    private void init() {

    checkGooglePlayServices();



//aq.id(R.id.login_logo).visibility(View.GONE);

        aq.id(R.id.imageView2).visibility(View.GONE);
        aq.id(R.id.imageView3).visibility(View.GONE);
        aq.id(R.id.imageView4).visibility(View.GONE);
        aq.id(R.id.imageView5).visibility(View.GONE);
        aq.id(R.id.cam_on).visibility(View.GONE);
        aq.id(R.id.cam_off).visibility(View.GONE);



    String url = APIs.getRegisterAPI();
        aq.ajax(url, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                JSONParser json;
                try {
                    json = new JSONParser(data);
                    HashMap<String, Object> result = (HashMap<String, Object>) json.convertJson2HashMap();

                    if (String.valueOf(result.get("code")).equalsIgnoreCase("200")) {
                        HashMap<String, Object> dataObj = (HashMap<String, Object>) result.get("data");
                        check_last_km1 = Integer.parseInt((String.valueOf(dataObj.get("last_km"))));
                        aq.id(R.id.textView5).text(getResources().getString(R.string.login_km) + "" + check_last_km1);
                        if (Double.parseDouble(String.valueOf(dataObj.get("version_support"))) > Double.parseDouble(getString(R.string.app_version))) {
//                            Toast.makeText(getBaseContext(), "update", Toast.LENGTH_LONG).show();
//                            checkVersion();
                            new DownloadFileFromURL().execute();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



        if (LoginHelper.getInstance(LoginActivity.this).checkLogin()) {
            openHomeActivity();


        }
        aq.id(R.id.login_downloading_frame).clicked(this);
        aq.id(R.id.login_btn_login).clicked(this);
        aq.id(R.id.login_loading_frame).clicked(this);
        aq.id(R.id.login_btn_login_username).clicked(this);
    }

    private void login(String id, String taxi_id, String km) {

        String url = APIs.getLoginAPI(id, taxi_id, km, getString(R.string.app_version));
        AppSharedPreferences.getInstance(LoginActivity.this).setStateLoginKM(Float.parseFloat(km));
        //Toast.makeText(getBaseContext(), Resources.getSystem().getString(R.string.app_version), Toast.LENGTH_LONG).show();
        Global.printLog(true, "Login API", url);

        setProgressbarVisibility(true);
        //oak
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        //
        aq.ajax(url, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                try {
                    Global.printLog(true, "login data", String.valueOf(data));
                    json = new JSONParser(data);
                    LoginActivity.this.result = (HashMap<String, Object>) json.convertJson2HashMap();
                    //Oak Test
//                    Toast.makeText(getBaseContext(),LoginActivity.this.result.toString(), Toast.LENGTH_LONG).show();

                    if (String.valueOf(LoginActivity.this.result.get("code")).equalsIgnoreCase("200")) {


                        HashMap<String, Object> updateObj1 = (HashMap<String, Object>) LoginActivity.this.result.get("data");
                        //Toast.makeText(getBaseContext(),Double.parseDouble(getString(R.string.app_version))+"", Toast.LENGTH_LONG).show();


                        HashMap<String, Object> updateObj = (HashMap<String, Object>) LoginActivity.this.result.get("update");
                        if (updateObj != null) {

                            try {
                                if (Double.parseDouble(String.valueOf(updateObj.get("version"))) > Double.parseDouble(getString(R.string.app_version))) {
//                                    Toast.makeText(getBaseContext(),String.valueOf(updateObj.get("version")), Toast.LENGTH_LONG).show();
                                    String urlUpdate = String.valueOf(updateObj.get("url"));
                                    download_service = new Intent(LoginActivity.this, DownloadService.class);
                                    download_service.putExtra("url", urlUpdate);
                                    download_service.putExtra("path", Environment.getExternalStorageDirectory() + "/" + getString(R.string.download_path) + "/download");
                                    startService(download_service);

                                    StatusSpeak(String.valueOf(R.string.alert_Up).toString());
                                    aq.id(R.id.login_downloading_frame).visibility(View.VISIBLE);
                                    aq.id(R.id.login_progress_bar).progress(0);

//                                        Toast.makeText(getBaseContext(),"Update", Toast.LENGTH_LONG).show();


                                } else {


                                    if (updateObj1.get("isEnable").equals("1") && updateObj1.get("isCheckin").equals("1")) {
                                        loginCallback((HashMap<String, Object>) LoginActivity.this.result.get("data"));
                                        if (!String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase("") && !String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase("null") && !String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase(null)) {
                                            AppSharedPreferences.getInstance(LoginActivity.this).setStateShiftStart(Integer.parseInt(String.valueOf(updateObj1.get("shiftstart"))));

                                        }
                                        AppSharedPreferences.getInstance(LoginActivity.this).setStateKickTime(String.valueOf(updateObj1.get("kicktime")));
                                    } else {
                                        showErrorDialogLogin();
                                    }
//                                    loginCallback((HashMap<String, Object>) LoginActivity.this.result.get("data"));
                                }

                            } catch (Exception e) {
                                if (updateObj1.get("isEnable").equals("1") && updateObj1.get("isCheckin").equals("1")) {
                                    loginCallback((HashMap<String, Object>) LoginActivity.this.result.get("data"));
                                    if (!String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase("") && !String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase("null") && !String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase(null)) {
                                        AppSharedPreferences.getInstance(LoginActivity.this).setStateShiftStart(Integer.parseInt(String.valueOf(updateObj1.get("shiftstart"))));
                                    }
                                    AppSharedPreferences.getInstance(LoginActivity.this).setStateKickTime(String.valueOf(updateObj1.get("kicktime")));
                                } else {
                                    showErrorDialogLogin();
                                }
//                                loginCallback((HashMap<String, Object>) LoginActivity.this.result.get("data"));
                            }
                        } else {
                            if (updateObj1.get("isEnable").equals("1") && updateObj1.get("isCheckin").equals("1")) {
                                loginCallback((HashMap<String, Object>) LoginActivity.this.result.get("data"));
                                if (!String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase("") && !String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase("null") && !String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase(null)) {
                                    AppSharedPreferences.getInstance(LoginActivity.this).setStateShiftStart(Integer.parseInt(String.valueOf(updateObj1.get("shiftstart"))));
                                }
                                AppSharedPreferences.getInstance(LoginActivity.this).setStateKickTime(String.valueOf(updateObj1.get("shiftend")));
                            } else {
                                showErrorDialogLogin();
                            }
//                            loginCallback((HashMap<String, Object>) LoginActivity.this.result.get("data"));
                        }

                    } else {
//                        HashMap<String, Object> updateObj2 = (HashMap<String, Object>) LoginActivity.this.result.get("data");
//                        Global.showAlertDialog(LoginActivity.this, String.valueOf(updateObj2.get("message")));
                        Global.showAlertDialog(LoginActivity.this, String.valueOf(LoginActivity.this.result.get("message")));
                    }

                    setProgressbarVisibility(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loginwithUser(String id,String password, String taxi_id, String km) {

        String url = APIs.getLoginAPIwithUser(id, password, taxi_id, km, getString(R.string.app_version));
        AppSharedPreferences.getInstance(LoginActivity.this).setStateLoginKM(Float.parseFloat(km));
        //Toast.makeText(getBaseContext(), Resources.getSystem().getString(R.string.app_version), Toast.LENGTH_LONG).show();
        Global.printLog(true, "Login API", url);

        setProgressbarVisibility(true);
        //oak
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        //
        aq.ajax(url, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                try {
                    Global.printLog(true, "login data", String.valueOf(data));
                    json = new JSONParser(data);
                    LoginActivity.this.result = (HashMap<String, Object>) json.convertJson2HashMap();
                    //Oak Test
//                    Toast.makeText(getBaseContext(),LoginActivity.this.result.toString(), Toast.LENGTH_LONG).show();

                    if (String.valueOf(LoginActivity.this.result.get("code")).equalsIgnoreCase("200")) {


                        HashMap<String, Object> updateObj1 = (HashMap<String, Object>) LoginActivity.this.result.get("data");
                        //Toast.makeText(getBaseContext(),Double.parseDouble(getString(R.string.app_version))+"", Toast.LENGTH_LONG).show();


                        HashMap<String, Object> updateObj = (HashMap<String, Object>) LoginActivity.this.result.get("update");
                        if (updateObj != null) {

                            try {
                                if (Double.parseDouble(String.valueOf(updateObj.get("version"))) > Double.parseDouble(getString(R.string.app_version))) {
//                                    Toast.makeText(getBaseContext(),String.valueOf(updateObj.get("version")), Toast.LENGTH_LONG).show();
                                    String urlUpdate = String.valueOf(updateObj.get("url"));
                                    download_service = new Intent(LoginActivity.this, DownloadService.class);
                                    download_service.putExtra("url", urlUpdate);
                                    download_service.putExtra("path", Environment.getExternalStorageDirectory() + "/" + getString(R.string.download_path) + "/download");
                                    startService(download_service);

                                    StatusSpeak(String.valueOf(R.string.alert_Up).toString());
                                    aq.id(R.id.login_downloading_frame).visibility(View.VISIBLE);
                                    aq.id(R.id.login_progress_bar).progress(0);

//                                        Toast.makeText(getBaseContext(),"Update", Toast.LENGTH_LONG).show();


                                } else {


                                    if (updateObj1.get("isEnable").equals("1") && updateObj1.get("isCheckin").equals("1")) {
                                        loginCallback((HashMap<String, Object>) LoginActivity.this.result.get("data"));
                                        if (!String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase("") && !String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase("null") && !String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase(null)) {
                                            AppSharedPreferences.getInstance(LoginActivity.this).setStateShiftStart(Integer.parseInt(String.valueOf(updateObj1.get("shiftstart"))));

                                        }
                                        AppSharedPreferences.getInstance(LoginActivity.this).setStateKickTime(String.valueOf(updateObj1.get("kicktime")));
                                    } else {
                                        showErrorDialogLogin();
                                    }
//                                    loginCallback((HashMap<String, Object>) LoginActivity.this.result.get("data"));
                                }

                            } catch (Exception e) {
                                if (updateObj1.get("isEnable").equals("1") && updateObj1.get("isCheckin").equals("1")) {
                                    loginCallback((HashMap<String, Object>) LoginActivity.this.result.get("data"));
                                    if (!String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase("") && !String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase("null") && !String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase(null)) {
                                        AppSharedPreferences.getInstance(LoginActivity.this).setStateShiftStart(Integer.parseInt(String.valueOf(updateObj1.get("shiftstart"))));
                                    }
                                    AppSharedPreferences.getInstance(LoginActivity.this).setStateKickTime(String.valueOf(updateObj1.get("kicktime")));
                                } else {
                                    showErrorDialogLogin();
                                }
//                                loginCallback((HashMap<String, Object>) LoginActivity.this.result.get("data"));
                            }
                        } else {
                            if (updateObj1.get("isEnable").equals("1") && updateObj1.get("isCheckin").equals("1")) {
                                loginCallback((HashMap<String, Object>) LoginActivity.this.result.get("data"));
                                if (!String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase("") && !String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase("null") && !String.valueOf(updateObj1.get("shiftstart")).equalsIgnoreCase(null)) {
                                    AppSharedPreferences.getInstance(LoginActivity.this).setStateShiftStart(Integer.parseInt(String.valueOf(updateObj1.get("shiftstart"))));
                                }
                                AppSharedPreferences.getInstance(LoginActivity.this).setStateKickTime(String.valueOf(updateObj1.get("shiftend")));
                            } else {
                                showErrorDialogLogin();
                            }
//                            loginCallback((HashMap<String, Object>) LoginActivity.this.result.get("data"));
                        }

                    } else {
//                        HashMap<String, Object> updateObj2 = (HashMap<String, Object>) LoginActivity.this.result.get("data");
//                        Global.showAlertDialog(LoginActivity.this, String.valueOf(updateObj2.get("message")));
                        Global.showAlertDialog(LoginActivity.this, String.valueOf(LoginActivity.this.result.get("message")));
                    }

                    setProgressbarVisibility(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loginCallback(HashMap<String, Object> data) {

        LoginHelper.getInstance(LoginActivity.this).rememberLogin(data);
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        openHomeActivity();
    }

    private void setProgressbarVisibility(boolean visibility) {

        if (visibility) {
            aq.id(R.id.login_loading_frame).getView().setVisibility(View.VISIBLE);
        } else
            aq.id(R.id.login_loading_frame).getView().setVisibility(View.GONE);
    }

    private void openHomeActivity() {

//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//        finish();

        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra("loginTime",currentDateTimeString);
        startActivity(intent);
        finish();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_btn_login:

                qrshown = true;
                aq.id(R.id.login_qr_code_frame).visibility(View.VISIBLE);
//                login("5-0005","480","50");
                qrCodeReaderView.setOnQRCodeReadListener(this);
                /*
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, CaptureActivity.class);
                intent.putExtra("index", 1);
                LoginActivity.this.startActivityForResult(intent, Global.ACTION_REQUEST_QRCODE_READER);
                */
                break;
            case R.id.login_btn_login_username:
                showdialogUsernameLogin();
                break;
            default:
                break;
        }
    }

    public String toMD5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void showdialogUsernameLogin(){
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.dialog_login_username, null);

        TextView dialog_msg = (TextView) v.findViewById(R.id.confirm_msg);
        dialog_msg.setText(getString(R.string.login_msg));
        TextView KM = (TextView) v.findViewById(R.id.textView);
        KM.setText(R.string.login_km);
        final TextView input_KM = (TextView) v.findViewById(R.id.editText);
        TextView unit = (TextView) v.findViewById(R.id.textView2);
        unit.setText(R.string.login_unit);
        TextView btn_ok = (TextView) v.findViewById(R.id.confirm_btn_ok);
        TextView btn_cancel = (TextView) v.findViewById(R.id.confirm_btn_cancel);
        final EditText usernameEditText = (EditText)v.findViewById(R.id.usernameEditText);
        final EditText passwordEditText = (EditText)v.findViewById(R.id.passwordEditText);

        dialog = new Dialog(this);
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

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = APIs.getRegisterAPI();
                aq.ajax(url, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String data, AjaxStatus status) {

                        JSONParser json;
                        try {
                            json = new JSONParser(data);
                            HashMap<String, Object> result = (HashMap<String, Object>) json.convertJson2HashMap();

                            if (String.valueOf(result.get("code")).equalsIgnoreCase("200")) {
                                HashMap<String, Object> dataObj = (HashMap<String, Object>) result.get("data");
                                check_last_km = Double.parseDouble((String.valueOf(dataObj.get("last_km"))));



                                double inputKm = Double.parseDouble(input_KM.getText().toString());
                                if(input_KM != null && !input_KM.getText().toString().isEmpty()) {
                                    if (inputKm - check_last_km < 1500) {
                                        closeDialog();
                                        loginwithUser(usernameEditText.getText().toString(), toMD5(passwordEditText.getText().toString()).substring(0, 20), taxi_id, input_KM.getText().toString());
                                        AppSharedPreferences.getInstance(LoginActivity.this).setRememberUser(usernameEditText.getText().toString());
                                    }
                                    else if(inputKm - check_last_km > 1500){


                                        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        View v = vi.inflate(R.layout.dialog_confirm, null);
                                        TextView dialog_msg = (TextView) v.findViewById(R.id.confirm_msg);
                                        dialog_msg.setText(getString(R.string.alert_KM_MORE));
                                        dialog_msg.setGravity(Gravity.CENTER);

                                        TextView btn_ok = (TextView) v.findViewById(R.id.confirm_btn_ok);
                                        TextView btn_cancel = (TextView) v.findViewById(R.id.confirm_btn_cancel);



                                        dialog = new Dialog(LoginActivity.this);
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


                                        btn_ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                closeDialog();
                                                loginwithUser(usernameEditText.getText().toString(), toMD5(passwordEditText.getText().toString()).substring(0, 20), taxi_id, input_KM.getText().toString());
                                                AppSharedPreferences.getInstance(LoginActivity.this).setRememberUser(usernameEditText.getText().toString());
                                            }
                                        });

                                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                closeDialog();
                                            }
                                        });

                                        dialog.show();


                                    }
                                    else
                                    {

                                        Toast.makeText(getBaseContext(), R.string.alert_KM, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getBaseContext(), R.string.alert_KM, Toast.LENGTH_SHORT).show();
                                }




                            } else {
                                Global.showAlertDialog(LoginActivity.this, String.valueOf(result.get("message")));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }

        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDialog();
//                Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
//                startActivity(intent);
            }
        });

        dialog.show();
    }
    public void showErrorDialogLogin() {

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.dialog_message, null);

        TextView dialog_msg = (TextView) v.findViewById(R.id.alert_msg);
        dialog_msg.setText(R.string.error_02);

        TextView dialog_confirm = (TextView) v.findViewById(R.id.alert_btn_ok);

        Dialog dialog = new Dialog(this);
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

                aq.id(R.id.login_downloading_frame).visibility(View.GONE);
                aq.id(R.id.login_progress_bar).progress(0);
                //openHomeActivity();
            }
        });

        dialog.show();
    }


    public void showErrorDialog() {

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.dialog_message, null);

        TextView dialog_msg = (TextView) v.findViewById(R.id.alert_msg);
        dialog_msg.setText(R.string.error_01);

        TextView dialog_confirm = (TextView) v.findViewById(R.id.alert_btn_ok);

        Dialog dialog = new Dialog(this);
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

                aq.id(R.id.login_downloading_frame).visibility(View.GONE);
                aq.id(R.id.login_progress_bar).progress(0);
                //openHomeActivity();
            }
        });

        dialog.show();
    }

    private Dialog dialog;
    private Dialog dialog1;
    @Override
    public void onQRCodeRead(final String stuff_id, PointF[] points) {

//        Toast.makeText(getApplicationContext(),stuff_id,Toast.LENGTH_SHORT).show();
        //final String stuff_id_final = stuff_id;

        qrCodeReaderView.setOnQRCodeReadListener(null);
        aq.id(R.id.login_qr_code_frame).visibility(View.GONE);
        qrshown = false;


        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.dialog_logout, null);

        TextView dialog_msg = (TextView) v.findViewById(R.id.confirm_msg);
        dialog_msg.setText(getString(R.string.login_msg));
        TextView KM = (TextView) v.findViewById(R.id.textView);
        KM.setText(R.string.login_km);
        final TextView input_KM = (TextView) v.findViewById(R.id.editText);
        TextView unit = (TextView) v.findViewById(R.id.textView2);
        unit.setText(R.string.login_unit);
        TextView btn_ok = (TextView) v.findViewById(R.id.confirm_btn_ok);
        TextView btn_cancel = (TextView) v.findViewById(R.id.confirm_btn_cancel);

        dialog = new Dialog(this);
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

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = APIs.getRegisterAPI();
                aq.ajax(url, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String data, AjaxStatus status) {

                        JSONParser json;
                        try {
                            json = new JSONParser(data);
                            HashMap<String, Object> result = (HashMap<String, Object>) json.convertJson2HashMap();

                            if (String.valueOf(result.get("code")).equalsIgnoreCase("200")) {
                                HashMap<String, Object> dataObj = (HashMap<String, Object>) result.get("data");
                                check_last_km = Double.parseDouble((String.valueOf(dataObj.get("last_km"))));



                                double inputKm = Double.parseDouble(input_KM.getText().toString());
                                if(input_KM != null && !input_KM.getText().toString().isEmpty()) {
                                    if (inputKm - check_last_km < 1500) {
                                        closeDialog();
                                        login(stuff_id, taxi_id, input_KM.getText().toString());
                                    }
                                    else if(inputKm - check_last_km > 1500){


                                        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        View v = vi.inflate(R.layout.dialog_confirm, null);

                                        TextView dialog_msg = (TextView) v.findViewById(R.id.confirm_msg);
                                        dialog_msg.setText(getString(R.string.alert_KM_MORE));
                                        dialog_msg.setGravity(Gravity.CENTER);

                                        TextView btn_ok = (TextView) v.findViewById(R.id.confirm_btn_ok);
                                        TextView btn_cancel = (TextView) v.findViewById(R.id.confirm_btn_cancel);



                                        dialog = new Dialog(LoginActivity.this);
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

                                        btn_ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                closeDialog();
                                                login(stuff_id, taxi_id, input_KM.getText().toString());
                                            }
                                        });

                                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                closeDialog();
                                            }
                                        });

                                        dialog.show();


                                    }
                                    else
                                    {

                                        Toast.makeText(getBaseContext(), R.string.alert_KM, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getBaseContext(), R.string.alert_KM, Toast.LENGTH_SHORT).show();
                                }




                            } else {
                                Global.showAlertDialog(LoginActivity.this, String.valueOf(result.get("message")));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }

        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDialog();
                Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();

    }

    private void closeDialog() {

        if (dialog != null)
            dialog.dismiss();

        dialog = null;
    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            setProgressbarVisibility(false);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //some script here
        }
    }

    /**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage(getResources().getText(R.string.progress));
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }
}