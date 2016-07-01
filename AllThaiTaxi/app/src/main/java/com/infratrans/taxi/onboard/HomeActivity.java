package com.infratrans.taxi.onboard;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.formatter.PercentFormatter;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.infratrans.taxi.onboard.markerclusterer.MarkerCluster;
import com.infratrans.taxi.onboard.markerclusterer.MarkerClusterer;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.infratrans.taxi.onboard.json.JSONParser;
import com.infratrans.taxi.onboard.services.GMapV2Direction;
import com.infratrans.taxi.onboard.services.GoogleDirectionStep;
import com.infratrans.taxi.onboard.services.JobTracking;
import com.infratrans.taxi.onboard.services.LocationService;
import com.infratrans.taxi.onboard.services.TaxiStatusTracking;
import com.infratrans.taxi.onboard.shares.AppSharedPreferences;
import com.infratrans.taxi.onboard.shares.LoginHelper;
import com.infratrans.taxi.onboard.util.APIs;
import com.infratrans.taxi.onboard.util.Global;
import com.infratrans.taxi.onboard.view.ViewBreaking;
import com.infratrans.taxi.onboard.view.ViewCarWash;
import com.infratrans.taxi.onboard.view.ViewGasPayment;
import com.infratrans.taxi.onboard.view.ViewGasStation;
import com.infratrans.taxi.onboard.view.ViewGoToToyota;
import com.infratrans.taxi.onboard.view.ViewHubPoint;
import com.infratrans.taxi.onboard.view.ViewIncident;
import com.infratrans.taxi.onboard.view.ViewIncidentBreak;
import com.infratrans.taxi.onboard.view.ViewLimitSpeed;
import com.infratrans.taxi.onboard.view.ViewNotification;
import com.infratrans.taxi.onboard.view.ViewOutOfService;
import com.infratrans.taxi.onboard.view.ViewShift;
import com.infratrans.taxi.onboard.view.ViewTripReport;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Thanisak Piyasaksiri on 2/6/15 AD.
 * Modified by Thanisak Piyasaksiri on 4/24/15 AD.
 */
public class HomeActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener,
        View.OnClickListener,
        ViewNotification.ViewNotifyIncidentListener,
        ViewTripReport.ViewTripReportListener,
        ViewHubPoint.ViewHubPointListener,
        ViewIncident.ViewIncidentListener,
        ViewIncidentBreak.ViewIncidentListener,
        ViewOutOfService.ViewOutOfServiceListener,
        ViewGasStation.ViewGasStationListener,
        ViewShift.ViewShiftListener,
        ViewGoToToyota.ViewGoToToyotaListener,
        ViewCarWash.ViewCarWashListener,
        ViewGasPayment.ViewGasPaymentListener,
        ViewBreaking.ViewBreakingListener,
        AnimationListener,SensorEventListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    static MapFragment fragment;
    private static long back_pressed;
    final Handler handler = new Handler();
    //oak
    private boolean check_thum_re = true;
    MyCount timerCount;
    MyCount1 timerCount1;

    private int hours = 1;
    private int hoursshifttime = 1;
    private String loginTime;
    private GoogleMap mapview;
    private LatLng myLocation;
    private Marker myLocation_maker;
    private CameraPosition panTo_myLocation;
    private CameraPosition panTo_myLocation_navigation = null;
    private Marker customer_maker;
    private LatLng customer_location;
    private Marker destination_maker;
    private LatLng destination_location;
    private Marker others_marker;
    private LatLng others_location;
    private JSONParser json;
    private HashMap<String, Object> result;
    private HashMap<String, Object> profile;
    private AQuery aq;
    private Dialog dialog;
    private Dialog dialog_speed;
    private Dialog dialog_outofservice;
    private GMapV2Direction direction;
    private Animation fade_in;
    private Bundle extras;

    //Add by Beat
    private boolean pantoLocation = true;
    public double bearing;
    private float mDeclination;
    private SensorManager mSensorManager = null;
    private Sensor mRotVectSensor;
    private static final int SENSOR_DELAY = 500 * 1000;
    private int countStep = 0;
    private float current_zoom = 15.0f;
    private boolean isNewlyOpened = true;
    private ArrayList<GoogleDirectionStep> googleDirectionStepArrayList = null;
    private TextView tv_naviText;
//    private WebView webView;

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation
        // check for fade in animation


    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // Animation is repeating
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // Animation started
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (LocationService.BROADCAST_ACTION.equalsIgnoreCase(intent.getAction())) {

                if (intent.hasExtra("opensetting") && intent.getBooleanExtra("opensetting", false) == true) {

                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle(R.string.enable_gps)
                            .setMessage(R.string.enable_gps_dialog)
                            .setPositiveButton(R.string.enable_gps, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            HomeActivity.this.startActivity(settingsIntent);
                                        }
                                    }
                            ).create().show();
                } else {

                    AppSharedPreferences.getInstance(HomeActivity.this).setLocation(String.valueOf(intent.getStringExtra("latitude")), String.valueOf(intent.getStringExtra("longitude")));
                    if (mapview != null) {
                        String[] mylocation = AppSharedPreferences.getInstance(HomeActivity.this).getLocation();
                        Location location = new Location("");
                        location.setLatitude(Double.parseDouble(mylocation[0]));
                        location.setLongitude(Double.parseDouble(mylocation[1]));
                        CurrentLocation.currentLocation = location;
                        updateUILocation(mylocation[0], mylocation[1], pantoLocation);
                    }
                }

                //Edit by Beatta
            } else if (TaxiStatusTracking.BROADCAST_ACTION.equalsIgnoreCase(intent.getAction())) {

                getTaxiStatus(String.valueOf(intent.getStringExtra("data")));

            } else if (JobTracking.BROADCAST_ACTION.equalsIgnoreCase(intent.getAction())) {
//                aq.id(R.id.textView8).text(String.valueOf(intent.getStringExtra("data")));

                if (intent.hasExtra("data") && !AppSharedPreferences.getInstance(HomeActivity.this).getOnJob()) {
                    getJobRequested(String.valueOf(intent.getStringExtra("data")));

                    String job_type = "";
                    JSONParser json;
                    try {
                        json = new JSONParser(String.valueOf(intent.getStringExtra("data")));
                        HashMap<String, Object> dataObj = (HashMap<String, Object>) json.convertJson2HashMap();
                        Global.printLog(true, "dataObj", String.valueOf(dataObj));

                        if (String.valueOf(dataObj.get("code")).equalsIgnoreCase("200") && dialog == null) {

                            HashMap<String, Object> reserveObj = (HashMap<String, Object>) dataObj.get("data");
                            job_type = String.valueOf(reserveObj.get("job_type"));
                        }

                    }catch (Exception e){

                    }

//                            aq.id(R.id.textView7).text(String.valueOf(intent.getStringExtra("debug"))+" , job_type: "+job_type);
                }
            }
        }
    };


//    private MediaPlayer playAudio;
//    private MediaPlayer playAudio;
//    private URI StausSpeakerUri;
//    String str_preTextUrl_status = "http://translate.google.com/translate_tts?ie=UTF-8&q=";
//    String str_textUrl_status = "";
//    String str_postTextUrl_status = "&tl=th";
//    public void playAudio(String textSpeaker) {
//        try {
//            if(textSpeaker != null && !textSpeaker.isEmpty()) {
//                playAudio = new MediaPlayer();
//                playAudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                str_textUrl_status = textSpeaker;
//                StausSpeakerUri = new URI(str_preTextUrl_status + str_textUrl_status + str_postTextUrl_status);
//                URL url = new URL(StausSpeakerUri.toASCIIString());
//                playAudio.setDataSource(url.toString());
//                playAudio.prepare();
//                playAudio.start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //    Play audio from file.
    private MediaPlayer mp = null;
    private void playAudio(int resId)
    {
        if(mp != null) {
            if (!mp.isPlaying()) {
                mp = MediaPlayer.create(this, resId);
                mp.start();

            } else if (mp.isPlaying()) {
                mp.stop();
                mp = null;
                mp = MediaPlayer.create(this, resId);
                mp.start();
            }
        } else {
            mp = MediaPlayer.create(this, resId);
            mp.start();
        }

    }



    private Polyline taxi2customer;
    private Polyline taxi2othersJob;
    private Polyline customer2destination;
    private IntentFilter filter;
    private Intent intent_service;
    private Intent intent_jobTracking;
    private Intent intent_taxi_status;
    private MediaPlayer mediaPlayer;
    private Bitmap cust_pix;
    private Bitmap driver_pix;
    private String msisdn = "";
    private Timer timer;
    private TimerTask timerTaskReserve;
    private TimerTask timerTask_UpdateChart;
    private Timer timer_UpdateChart;
    private Timer timerReserve;
    private Timer timerMeter;

    private TimerTask timerTask;
    private TimerTask timerTaskMeter;
    private String taxi_id_home = "";

    private Timer timerNetwork;
    private TimerTask timerTaskNetwork;

    private Timer timerNews;
    private TimerTask timerTaskNews;

    private FrameLayout layout_destinationMarker;


    public void checkKillProcess(){
        String killProcess = APIs.DestroyApp();
        aq.ajax(killProcess, String.class, new AjaxCallback<String>() {
        });
    }

    public void onDestroy() {
        checkKillProcess();

        mediaPlayer.setLooping(false);
//        if(viewtrip != null){
//            viewtrip.btn_payment_click("1");
//        }

        if(timer != null){
            timer.cancel();
            timer = null;
        }
        if(timerMeter != null){
            timerMeter.cancel();
            timerMeter = null;
        }
        if(timerNetwork != null){
            timerNetwork.cancel();
            timerNetwork = null;
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }

        if(outOfTheWayTimer != null) {
            outOfTheWayTimer.cancel();
            outOfTheWayTimer = null;
        }

        super.onDestroy();
        stopService(intent_service);
        stopService(intent_jobTracking);
        stopService(intent_taxi_status);
        //android.os.Process.killProcess(android.os.Process.myPid());
    }


    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis())
            HomeActivity.this.finish();
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();

        back_pressed = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        reserved_type = AppSharedPreferences.getInstance(HomeActivity.this).getReserved_Type();
        enable_credit_card = AppSharedPreferences.getInstance(HomeActivity.this).getEnable_Credit();

//        Toast.makeText(getBaseContext(), AppSharedPreferences.getInstance(HomeActivity.this).getReserved_Type().toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getBaseContext(), AppSharedPreferences.getInstance(HomeActivity.this).getEnable_Credit().toString(), Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_home);



        taxi_id_home = LoginHelper.getInstance(this).getTaxiID();

        filter = new IntentFilter();
        filter.addAction(LocationService.BROADCAST_ACTION);
        filter.addAction(JobTracking.BROADCAST_ACTION);
        filter.addAction(TaxiStatusTracking.BROADCAST_ACTION);


        intent_service = new Intent(this, LocationService.class);
        intent_jobTracking = new Intent(this, JobTracking.class);
        intent_taxi_status = new Intent(this, TaxiStatusTracking.class);

        aq = new AQuery(this);
        direction = new GMapV2Direction();
        setProgressbarVisibility(false);

        mediaPlayer = MediaPlayer.create(this, R.raw.sonar);
        mediaPlayer.setLooping(true);



        cust_pix = BitmapFactory.decodeResource(getResources(), R.drawable.home_pic_passenger);
        driver_pix = BitmapFactory.decodeResource(getResources(), R.drawable.home_pic_staff);


        fade_in = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        fade_in.setAnimationListener(this);

        extras = getIntent().getExtras();
//        loginTime = extras.getString("loginTime");


        init();
    }

    @Override
    public void onResume() {

        super.onResume();
        startTimerNetwork();
        startTimer();
        startTimerMeter();
//        startTimer_UpdateChart();
        registerReceiver(broadcastReceiver, filter);
        startService(intent_service);
//        add check job
        startService(intent_jobTracking);
        mediaPlayer.setLooping(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(intent_service);
        stopService(intent_jobTracking);
    }

    @Override
    public void onPause() {

        super.onPause();
//        add check job
        stopService(intent_jobTracking);
        unregisterReceiver(broadcastReceiver);
    }
    public void startTimerNews()
    {
        timerNews = new Timer();
        initializeTimerTaskNews();
        timerNews.schedule(timerTaskNews, 0, 3000);
    }

    public void startTimer_UpdateChart()
    {
        timer_UpdateChart = new Timer();
        initializeTimerTask_UpdateChart();
        timer_UpdateChart.schedule(timerTask_UpdateChart, 0, 1000*10);
    }

    public void startTimerMeter() {
        timerMeter = new Timer();
        initializeTimerTaskMeter();
        timerMeter.schedule(timerTaskMeter, 0, 3000); //

    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 0, 1000); //

    }

    public void startTimerReserve() {
        timerReserve = new Timer();
        initializeTimerTaskReserve();
        timerReserve.schedule(timerTaskReserve, 0, 1000*300); //

    }

    public void initializeTimerTask_UpdateChart() {
//        timerTask_UpdateChart = new TimerTask() {
//            public void run() {
//
//                handler.post(new Runnable() {
//                    public void run() {
//                        //        createPieChartJob_HR(1, AppSharedPreferences.getInstance(HomeActivity.this).getStateOnjob()
////                , AppSharedPreferences.getInstance(HomeActivity.this).getStateStandby());
//                        createPieChartJob_HR(1, AppSharedPreferences.getInstance(HomeActivity.this).getStateOnjob()
//                                , 10 * 3600);
//                        createHorizontalChartBaht_DAY(1, AppSharedPreferences.getInstance(HomeActivity.this).getStateBahtDay(), "บาท/วัน (1500)");
////        createHorizontalChartBaht_HR(1, AppSharedPreferences.getInstance(HomeActivity.this).getStateBahtHour(), "BAHT/HRs (150)");
//                        createHorizontalChartBaht_HR(1, AppSharedPreferences.getInstance(HomeActivity.this).getStateBahtDay()/hoursshifttime, "บาท/ชม. ("+150+")");
//
//
//                        if(AppSharedPreferences.getInstance(HomeActivity.this).getStateLoginKM() != 0 &&AppSharedPreferences.getInstance(HomeActivity.this).getStateApiKm()>=AppSharedPreferences.getInstance(HomeActivity.this).getStateLoginKM()) {
//                            createHorizontalChartBaht_KM(1, AppSharedPreferences.getInstance(HomeActivity.this).getStateBahtDay()
//                                    / (AppSharedPreferences.getInstance(HomeActivity.this).getStateApiKm() - AppSharedPreferences.getInstance(HomeActivity.this).getStateLoginKM()), "บาท/กิโลเมตร");
////            createHorizontalChartBaht_KM(1, 446 / (AppSharedPreferences.getInstance(HomeActivity.this).getStateApiKm() - AppSharedPreferences.getInstance(HomeActivity.this).getStateLoginKM()), "BAHT/KMs");
//
//                        }
//                        else{
//                            createHorizontalChartBaht_KM(1,0,"บาท/กิโลเมตร");
//                        }
//                    }
//                });
//            }
//        };
    }


    public void initializeTimerTaskNews() {
        timerTaskNews = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {


                        mediaPlayer.start();
                        // get news
                        String Checknews = APIs.getMeterAPI();
                        aq.ajax(Checknews, String.class, new AjaxCallback<String>() {
                            @Override
                            public void callback(String url, String data, AjaxStatus status) {

                                try {

                                    // Toast.makeText(getApplicationContext(), "Check Timer", Toast.LENGTH_SHORT).show();
                                    JSONParser json = new JSONParser(data);
                                    HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();

                                    if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {
                                        HashMap<String, Object> dataObj = (HashMap<String, Object>) resultObj.get("data");
                                        if (dataObj.get("news") != "0" && dataObj.get("news") != "") {
                                            new AlertDialog.Builder(HomeActivity.this)
                                                    .setTitle("Alert")
                                                    .setMessage(dataObj.get("news").toString())
                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // ส่ง API อีกตัว flag
                                                            if (mediaPlayer.isPlaying()) {
                                                                mediaPlayer.stop();
                                                                mediaPlayer = null;
                                                            }

                                                            timerNews.cancel();
                                                            timerNews = null;

                                                            if (timerNews == null) {
                                                                timerNews = new Timer();
                                                                initializeTimerTaskNews();
                                                                timerNews.schedule(timerTaskNews, 0, 3000); //


                                                            }
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();

                                        }

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                    }
                });
            }
        };
    }

    public void initializeTimerTaskReserve() {
        timerTaskReserve = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        mediaPlayer.start();
                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle("ข่าวสาร")
                                .setMessage("กรุณากดมิเตอร์"+"\n"+"หรือกดปุ่มเปิดมิเตอร์ถ้าท่านได้กดมิเตอร์แล้ว")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (mediaPlayer.isPlaying()) {
                                            mediaPlayer.stop();
                                            mediaPlayer = null;
                                        }

                                        timerReserve.cancel();
                                        timerReserve = null;

                                        if (timerReserve == null) {
                                            timerReserve = new Timer();
                                            initializeTimerTaskMeter();
                                            timerReserve.schedule(timerTaskReserve, 0, 3000); //
                                        }
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            }
        };
    }

    int j = 0;
    public void initializeTimerTaskMeter() {
        timerTaskMeter = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        //add oak
                        if(check_thum_re) {
                            String url = APIs.getMeterAPI();
                            aq.ajax(url, String.class, new AjaxCallback<String>() {
                                @Override
                                public void callback(String url, String data, AjaxStatus status) {
                                    // Toast.makeText(getApplicationContext(),"callback", Toast.LENGTH_SHORT).show();
                                    JSONParser json;
                                    try {
//oakky

//                                        Toast.makeText(getApplicationContext(),"Meter : "+j+"", Toast.LENGTH_SHORT).show();
                                        json = new JSONParser(data);
                                        HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();
                                        //Toast.makeText(getApplicationContext(),"resultobj", Toast.LENGTH_SHORT).show();
                                        if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {
                                            HashMap<String, Object> dataObj = (HashMap<String, Object>) resultObj.get("data");
                                            if (String.valueOf(dataObj.get("meter")).equalsIgnoreCase("1")) {
                                                //Toast.makeText(getApplicationContext(),"Status = 1", Toast.LENGTH_SHORT).show();
                                                Status.checkStatus = 2;
                                                if(Status.checkPaymentAccept) {
                                                    viewtrip.btn_payment_click(false);
                                                    closeDialog();
                                                }
                                                else closeDialog();

                                                timerMeter.cancel();
                                                timerMeter = null;
                                                aq.id(R.id.btn_Start_Mter).visible();
                                                aq.id(R.id.icon_thum).visible();

                                                if (AppSharedPreferences.getInstance(HomeActivity.this).getJobType().equalsIgnoreCase("2")) {
                                                    //Oak
                                                    //Toast.makeText(getApplicationContext(),"Endjob1",Toast.LENGTH_SHORT).show();
                                                    endJobWithoutResult(AppSharedPreferences.getInstance(HomeActivity.this).getJobID());
                                                    endTripWithoutResult(AppSharedPreferences.getInstance(HomeActivity.this).getJobID(), AppSharedPreferences.getInstance(HomeActivity.this).getJobType());
                                                    AppSharedPreferences.getInstance(HomeActivity.this).removeJobID();
                                                    AppSharedPreferences.getInstance(HomeActivity.this).removeJobType();
                                                }

                                                AppSharedPreferences.getInstance(HomeActivity.this).removeJobID();
                                                AppSharedPreferences.getInstance(HomeActivity.this).removeJobType();
                                                AppSharedPreferences.getInstance(HomeActivity.this).removeState_Hub();
                                                resetProgressState();

//                                                aq.id(R.id.home_btn_statusbar_1).clicked(null);
//                                                aq.id(R.id.home_btn_statusbar_1).background(R.drawable.home_status2_active);
//                                                aq.id(R.id.home_btn_statusbar_1).textColorId(R.color.white);
                                                aq.id(R.id.btn_thumbing).visibility(View.GONE);
                                                aq.id(R.id.bt_jobtype_2).visibility(View.GONE);
                                                aq.id(R.id.home_btn_sos).image(R.drawable.home_sos);

                                                aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_002));

                                                Status.enable = false;
                                                // Toast.makeText(getApplicationContext(),"Status.enable = " + Status.enable, Toast.LENGTH_SHORT).show();

                                                AppSharedPreferences.getInstance(HomeActivity.this).setThumbingMode(true);
                                                stopService(intent_jobTracking);

                                                thumbingCustomer(LoginHelper.getInstance(HomeActivity.this).getTaxiID(), LoginHelper.getInstance(HomeActivity.this).getDriverID(), LoginHelper.getInstance(HomeActivity.this).getOperationID());

                                            } else if (String.valueOf(dataObj.get("meter")).equalsIgnoreCase("0")) {
                                                if(timerMeter == null) {
                                                    timerMeter = new Timer();
                                                    initializeTimerTaskMeter();
                                                    timerMeter.schedule(timerTaskMeter, 0, 3000); //


                                                }
                                            }
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            //end
                        }

                    }
                });
            }
        };
    }

    public void startTimerNetwork() {
        timerNetwork = new Timer();
        initializeTimerTaskNetwork();
        timerNetwork.schedule(timerTaskNetwork, 0, 10000); //

    }

    int i = 0 ;


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

                                    JSONParser json = new JSONParser(data);
                                    HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();

                                    if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {
                                        aq.id(R.id.networkClose).visibility(View.GONE);
                                        aq.id(R.id.networkOpen).visible();
                                    } else {
                                        aq.id(R.id.networkOpen).visibility(View.GONE);
                                        aq.id(R.id.networkClose).visible();
                                    }

                                } catch (JSONException e) {
                                    aq.id(R.id.networkOpen).visibility(View.GONE);
                                    aq.id(R.id.networkClose).visible();
                                    e.printStackTrace();
                                } catch (RuntimeException e) {
                                    aq.id(R.id.networkOpen).visibility(View.GONE);
                                    aq.id(R.id.networkClose).visible();
                                    e.printStackTrace();
                                }

                            }
                        });



                    }
                });
            }
        };
    }

    private boolean executeCommand(){


        Runtime runtime = Runtime.getRuntime();

        try
        {
            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int mExitValue = mIpAddrProcess.waitFor();
            if(mExitValue==0){
                return true;
            }else{
                return false;
            }
        }
        catch (InterruptedException ignore)
        {
            ignore.printStackTrace();
            System.out.println(" Exception:"+ignore);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println(" Exception:"+e);
        }
        return false;
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

//    double Ramda2 ;
//    double Seta2;
//    double Seta1 = Math.toRadians(CurrentLocation.currentLocation.getLongitude());
//    double Ramda1= Math.toRadians(CurrentLocation.currentLocation.getLatitude());

    double lat1=0.0;
    double lon1=0.0;
    double lat2=0.0;
    double lon2=0.0;

    public void initializeTimerTask() {


        timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                        aq.id(R.id.home_logindate).text(getString(R.string.home_title_2) + " " + currentDateTimeString);


                        if (Status.checkNavi) {
                            lat2 = CurrentLocation.currentLocation.getLatitude();
                            lon2 = CurrentLocation.currentLocation.getLongitude();

                            if(lat1 == 0.0 || lon1 == 0.0){
                                lat1 = CurrentLocation.currentLocation.getLatitude();
                                lon1 = CurrentLocation.currentLocation.getLongitude();
                            }
                            Location loc1 = new Location("");
                            loc1.setLatitude(lat1);
                            loc1.setLongitude(lon1);

                            Location loc2 = new Location("");
                            loc2.setLatitude(lat2);
                            loc2.setLongitude(lon2);

                            double distanceInMeters = loc1.distanceTo(loc2);


                            if (distanceInMeters > 10) {


                                double Ramda2 = Math.toRadians(lat2);
                                double Seta2 = Math.toRadians(lon2);
                                double Ramda1 = Math.toRadians(lat1);
                                double Seta1 = Math.toRadians(lon1);

                                double y = Math.sin(Ramda2 - Ramda1) * Math.cos(Seta2);
                                double x = (Math.cos(Seta1) * Math.sin(Seta2)) - (Math.sin(Seta1) * Math.cos(Seta2) * Math.cos(Ramda2 - Ramda1));
                                bearing = loc1.bearingTo(loc2);
//                                bearing = Math.toDegrees(Math.atan2(y, x));
//                                Toast.makeText(getApplicationContext(),+bearing + "", Toast.LENGTH_SHORT).show();
                                lat1 = lat2;
                                lon1 = lon2;


                            }

                        }

                        Date now = new Date();
                        DateFormat sdf = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        DateFormat hour = new SimpleDateFormat(
                                "H");

                        String secondStr = "";
                        String firstStr = sdf.format(now);
                        String gethour = hour.format(now);
                        if(!AppSharedPreferences.getInstance(HomeActivity.this).getStateKickTime().equalsIgnoreCase("")
                                && !AppSharedPreferences.getInstance(HomeActivity.this).getStateKickTime().equalsIgnoreCase("null"))
                        {
                             secondStr = AppSharedPreferences.getInstance(HomeActivity.this).getStateKickTime();
                        }
                        else{
                            secondStr = "2020-02-11 13:00:00";
                        }


//                        String secondStr = "2016-02-11 20:50:00";
                        Date first = null;
                        try {
                            first = sdf.parse(firstStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date second = null;
                        try {
                            second = sdf.parse(secondStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        boolean after = (first.after(second));
                        if(after) {
                            timerTask.cancel();
                            showLogoutDialog();
                        }
                        else {
//                            Toast.makeText(getApplicationContext(), "in time ", Toast.LENGTH_SHORT).show();
                        }


                        hours = Integer.parseInt(gethour);

                        if(hours-AppSharedPreferences.getInstance(HomeActivity.this).getStateShiftStart() == 0){
                            hoursshifttime =1;
                        }
                        else if(hours-AppSharedPreferences.getInstance(HomeActivity.this).getStateShiftStart() <0){
                            hoursshifttime = hours-AppSharedPreferences.getInstance(HomeActivity.this).getStateShiftStart()+24;
                        }
                        else{
                            hoursshifttime = hours-AppSharedPreferences.getInstance(HomeActivity.this).getStateShiftStart();
                        }

                        if(AppSharedPreferences.getInstance(HomeActivity.this).getOnJob()){
                            AppSharedPreferences.getInstance(HomeActivity.this).setStateOnjob(AppSharedPreferences.getInstance(HomeActivity.this).getStateOnjob()+1);
                        }
                        else{
                            AppSharedPreferences.getInstance(HomeActivity.this).setStateStandby(AppSharedPreferences.getInstance(HomeActivity.this).getStateStandby()+1);
                        }

                        count_hour++;
                        if(count_hour >= 3600){
                            count_hour = 0;
                            AppSharedPreferences.getInstance(HomeActivity.this).removeStateBahtHour();
                        }
                        else{

                        }



                    }
                });
            }
        };
    }
    int count_hour;
    public class MyCount1 extends CountDownTimer {
        public MyCount1(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {



            createClusterMarkers();
            redrawMap();
            mIsClustered = false;
            //Toast.makeText(getApplicationContext(),"Cluster", Toast.LENGTH_SHORT).show();



        }

        @Override
        public void onTick(long millisUntilFinished) {
            //some script here
        }
    }


    ArrayList<Marker> mMarkers;
    ArrayList<Marker> mClusterMarkers;
    boolean mIsClustered = true;


    private void initializeUI() {
        // get ui items
        ImageView create = (ImageView) findViewById(R.id.button);
        ImageView cluster = (ImageView) findViewById(R.id.cluster);

//        aq.id(R.id.cluster).visibility(View.GONE);
//        aq.id(R.id.create).visibility(View.GONE);

        // set listeners
        create.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar c = Calendar.getInstance();
                    int day = c.get(Calendar.DAY_OF_WEEK);
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    //Toast.makeText(getApplicationContext(),"Day : "+day, Toast.LENGTH_SHORT).show();
                    createRandomMarkers(day, hour);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                redrawMap();


//                mIsClustered = true;
//
//                timerCount1 = new MyCount1(5000,1000);
//                timerCount1.start();

                // Toast.makeText(getApplicationContext(),"Create", Toast.LENGTH_SHORT).show();
                aq.id(R.id.cluster).visible();
                aq.id(R.id.button).visibility(View.GONE);
            }
        });

        cluster.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getGoogleMap().clear();
                // clear marker lists
                for (Marker marker : mMarkers) {
                    marker.setVisible(false);
                }
                mMarkers.clear();
                //mClusterMarkers.clear();
                aq.id(R.id.button).visible();
                aq.id(R.id.cluster).visibility(View.GONE);
                //redrawMap();
                // Toast.makeText(getApplicationContext(),"Close", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void createRandomMarkers(int day , int hour) throws JSONException {
        // clear map
        getGoogleMap().clear();
        // clear marker lists
        mMarkers.clear();
        mClusterMarkers.clear();


        String json = "http://taxiapi.itsconsultancy.co.th/onboard/mapDemand/point";
        String days = String.valueOf(day);
        String times = String.valueOf(hour);
        String jsonCluster = json+"_"+days+"_"+times+".json";
        //Toast.makeText(getApplicationContext(),jsonCluster.toString(), Toast.LENGTH_LONG).show();
          //String jsonCluster = "http://taxiapi.itsconsultancy.co.th/onboard/mapDemand/point_2_15.json";
        AQuery ajax = aq.ajax(jsonCluster, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                try {


                    JSONParser json = new JSONParser(data);
                    HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();

//                            Toast.makeText(getApplicationContext(), "200"+resultObj.get("data").toString(), Toast.LENGTH_SHORT).show();
//                            JSONObject obj = (JSONObject) resultObj.get("data");
//                            JSONArray array = obj.getJSONArray(String.valueOf(obj));
//                            Toast.makeText(getApplicationContext(), "OAK"+array.toString(), Toast.LENGTH_SHORT).show();

                    JSONObject obj = new JSONObject(resultObj);

                    JSONArray array = obj.getJSONArray("data");
                    //Toast.makeText(getApplicationContext(),obj.toString(), Toast.LENGTH_SHORT).show();


                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jObj = array.getJSONObject(i);
                        double lat = jObj.getDouble("lat");
                        double lng = jObj.getDouble("lng");
                        int percentage = jObj.getInt("percentage");
                        //Toast.makeText(getApplicationContext(),String.valueOf(percentage), Toast.LENGTH_SHORT).show();

                        LatLng markerPos = new LatLng(lat, lng);
                        if (percentage >= 0 && percentage <= 10) {
                          MarkerOptions markerOptions = new MarkerOptions().position(
                                    markerPos).icon(BitmapDescriptorFactory.fromResource(R.drawable.per10)).visible(false);
                            Marker marker = getGoogleMap().addMarker(markerOptions);
                            //add to list
                            mMarkers.add(marker);
                            marker.setVisible(true);
                        } else if (percentage > 10 && percentage <= 20) {
                            MarkerOptions markerOptions = new MarkerOptions().position(
                                    markerPos).icon(BitmapDescriptorFactory.fromResource(R.drawable.per20)).visible(false);
                            Marker marker = getGoogleMap().addMarker(markerOptions);
                            //add to list
                            mMarkers.add(marker);
                            marker.setVisible(true);
                        } else if (percentage > 20 && percentage <= 30) {
                            MarkerOptions markerOptions = new MarkerOptions().position(
                                    markerPos).icon(BitmapDescriptorFactory.fromResource(R.drawable.per30)).visible(false);
                            Marker marker = getGoogleMap().addMarker(markerOptions);
                            //add to list
                            mMarkers.add(marker);
                            marker.setVisible(true);
                        } else if (percentage > 30 && percentage <= 40) {
                            MarkerOptions markerOptions = new MarkerOptions().position(
                                    markerPos).icon(BitmapDescriptorFactory.fromResource(R.drawable.per40)).visible(false);
                            Marker marker = getGoogleMap().addMarker(markerOptions);
                            //add to list
                            mMarkers.add(marker);
                            marker.setVisible(true);
                        } else if (percentage > 40 && percentage <= 50) {
                            MarkerOptions markerOptions = new MarkerOptions().position(
                                    markerPos).icon(BitmapDescriptorFactory.fromResource(R.drawable.per50)).visible(false);
                            Marker marker = getGoogleMap().addMarker(markerOptions);
                            //add to list
                            mMarkers.add(marker);
                            marker.setVisible(true);
                        } else if (percentage > 50 && percentage <= 60) {
                            MarkerOptions markerOptions = new MarkerOptions().position(
                                    markerPos).icon(BitmapDescriptorFactory.fromResource(R.drawable.per60)).visible(false);
                            Marker marker = getGoogleMap().addMarker(markerOptions);
                            //add to list
                            mMarkers.add(marker);
                            marker.setVisible(true);
                        } else if (percentage > 60 && percentage <= 70) {
                            MarkerOptions markerOptions = new MarkerOptions().position(
                                    markerPos).icon(BitmapDescriptorFactory.fromResource(R.drawable.per70)).visible(false);
                            Marker marker = getGoogleMap().addMarker(markerOptions);
                            //add to list
                            mMarkers.add(marker);
                            marker.setVisible(true);
                        } else if (percentage > 70 && percentage <= 80) {
                            MarkerOptions markerOptions = new MarkerOptions().position(
                                    markerPos).icon(BitmapDescriptorFactory.fromResource(R.drawable.per80)).visible(false);
                            Marker marker = getGoogleMap().addMarker(markerOptions);
                            //add to list
                            mMarkers.add(marker);
                            marker.setVisible(true);
                        } else if (percentage > 80 && percentage <= 90) {
                            MarkerOptions markerOptions = new MarkerOptions().position(
                                    markerPos).icon(BitmapDescriptorFactory.fromResource(R.drawable.per90)).visible(false);
                            Marker marker = getGoogleMap().addMarker(markerOptions);
                            //add to list
                            mMarkers.add(marker);
                            marker.setVisible(true);
                        } else if (percentage > 90 && percentage <= 99) {
                            MarkerOptions markerOptions = new MarkerOptions().position(
                                    markerPos).icon(BitmapDescriptorFactory.fromResource(R.drawable.per100)).visible(false);
                            Marker marker = getGoogleMap().addMarker(markerOptions);
                            //add to list
                            mMarkers.add(marker);
                            marker.setVisible(true);
                        }

                        //IconGenerator factory = new IconGenerator(HomeActivity.this);
                        //Bitmap icon = factory.makeIcon(String.valueOf(jObj.getInt("counter")));

//                        MarkerOptions markerOptions = new MarkerOptions().position(
//                                markerPos).visible(false);
                        // create marker
//                        Marker marker = getGoogleMap().addMarker(markerOptions);
//                        //add to list
//                        mMarkers.add(marker);
//                        marker.setVisible(true);
                    }

                } catch (JSONException e) {
                    aq.id(R.id.networkOpen).visibility(View.GONE);
                    aq.id(R.id.networkClose).visible();
                    e.printStackTrace();
                }


            }
        });


    }




//    private void createRandomMarkers(int numberOfMarkers) {
//        // clear map
//        getGoogleMap().clear();
//        // clear marker lists
//        mMarkers.clear();
//        mClusterMarkers.clear();
//
//        // get projection area
//        Projection projection = getGoogleMap().getProjection();
//        LatLngBounds bounds = projection.getVisibleRegion().latLngBounds;
//        // set min/max for lat/lng
//        double minLat = bounds.southwest.latitude;
//        double maxLat = bounds.northeast.latitude;
//        double minLng = bounds.southwest.longitude;
//        double maxLng = bounds.northeast.longitude;
//
//        // create random markers
//        for (int i = 0; i < numberOfMarkers; i++) {
//            // create random position
//            LatLng markerPos = new LatLng(minLat
//                    + (Math.random() * (maxLat - minLat)), minLng
//                    + (Math.random() * (maxLng - minLng)));
//
//            // create marker as non-visible
//            MarkerOptions markerOptions = new MarkerOptions().position(
//                    markerPos).visible(false);
//            // create marker
//            Marker marker = getGoogleMap().addMarker(markerOptions);
//            // add to list
//            mMarkers.add(marker);
//        }
//
//    }

    private void createClusterMarkers() {

        if (mClusterMarkers.size() == 0) {
            // set cluster parameters
            int gridSize = 100;
            boolean averageCenter = false;
            // create clusters
            Marker[] markers = mMarkers.toArray(new Marker[mMarkers.size()]);
            ArrayList<MarkerCluster> markerClusters = new MarkerClusterer(
                    getGoogleMap(), markers, gridSize, averageCenter)
                    .createMarkerClusters();
            // create cluster markers
            for (MarkerCluster cluster : markerClusters) {
                int markerCount = cluster.markers.size();
                if (markerCount == 1) {
                    mClusterMarkers.add(cluster.markers.get(0));
                } else {
                    // get marker view and set text
                    View markerView = getLayoutInflater().inflate(
                            R.layout.cluster_marker_view, null);
                    ((TextView) markerView.findViewById(R.id.marker_count))
                            .setText(String.valueOf(markerCount));

                    // create cluster marker
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(cluster.center)
                            .icon(BitmapDescriptorFactory
                                    .fromBitmap(createDrawableFromView(markerView)))
                            .visible(false);
                    Marker clusterMarker = getGoogleMap().addMarker(
                            markerOptions);
                    // add to list
                    mClusterMarkers.add(clusterMarker);
                }
            }
        }
    }

    private void recreateClusterMarkers() {
        // remove cluster markers from map
        for (Marker marker : mClusterMarkers) {
            marker.remove();
        }
        // clear cluster markers list
        mClusterMarkers.clear();
        // create mew cluster markers
        createClusterMarkers();
    }

    private void redrawMap() {

        for (Marker marker : mMarkers) {
            marker.setVisible(true);
        }
    }

    private GoogleMap getGoogleMap() {
        if (mapview == null) {
            SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.home_mapview));
            mapview = mapFragment.getMap();
        }
        return mapview;
    }

    public static Bitmap createDrawableFromView(View view) {

        view.setDrawingCacheEnabled(true);
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }

//    private void setDataPie(int count, float[] range) {
//
//
//
//        ArrayList<com.github.mikephil.charting.data.Entry> yVals1 = new ArrayList<com.github.mikephil.charting.data.Entry>();
//
//        // IMPORTANT: In a PieChart, no values (Entry) should have the same
//        // xIndex (even if from different DataSets), since no values can be
//        // drawn above each other.
//        for (int i = 0; i < count + 1; i++) {
//            yVals1.add(new com.github.mikephil.charting.data.Entry((float) range[i], i));
//        }
//
//        ArrayList<String> xVals = new ArrayList<String>();
//        xVals.add("JOB");
//        xVals.add("STAND BY");
//
//        for (int i = 0; i < count + 1; i++)
//            xVals.add(String.valueOf(i));
//        PieDataSet dataSet = new PieDataSet(yVals1, "");
//        dataSet.setSliceSpace(3f);
//        dataSet.setSelectionShift(5f);
//
//        // add a lot of colors
//
//        ArrayList<Integer> colors = new ArrayList<Integer>();
//
//        int[] array = new int[2];
//        array[1] = Color.rgb(255, 0, 0);
//        array[0] = Color.rgb(0, 255, 0);
//        for (int c : array)
//            colors.add(c);
//
//
//        dataSet.setColors(colors);
//        //dataSet.setSelectionShift(0f);
//
//        PieData data = new PieData(xVals, dataSet);
//        data.setValueFormatter(new PercentFormatter());
//        data.setValueTextSize(11f);
//        data.setValueTextColor(Color.BLACK);
//        mChart.setData(data);
//
//        // undo all highlights
//        mChart.highlightValues(null);
//
//        mChart.invalidate();
//    }
//
//
//    private void createPieChartJob_HR(int set ,float value, float value1)
//    {
//        mChart = (PieChart)findViewById(R.id.chart1);
//        mChart.setUsePercentValues(true);
//        mChart.setDescription("");
//        mChart.setExtraOffsets(5, 10, 5, 5);
//
//
//        mChart.setDrawHoleEnabled(true);
//        mChart.setHoleColor(Color.WHITE);
//
//        mChart.setTransparentCircleColor(Color.WHITE);
//        mChart.setTransparentCircleAlpha(110);
//
//        mChart.setHoleRadius(58f);
//        mChart.setTransparentCircleRadius(61f);
//
//        mChart.setDrawCenterText(true);
//
//        mChart.setRotationAngle(0);
//        // enable rotation of the chart by touch
//        mChart.setRotationEnabled(true);
//        mChart.setHighlightPerTapEnabled(true);
//
//        float[] valuearr = new float[2];
//        valuearr[0] = value;
//        valuearr[1]= value1;
//        setDataPie(set, valuearr);
//
//        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
//        // mChart.spin(2000, 0, 360);
//
//        Legend l = mChart.getLegend();
//        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);
//    }
//
//    public void setDataRadarChart() {
//
//        float mult = 150;
//        int cnt = 9;
//
//        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
//        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
//
//        // IMPORTANT: In a PieChart, no values (Entry) should have the same
//        // xIndex (even if from different DataSets), since no values can be
//        // drawn above each other.
//        for (int i = 0; i < cnt; i++) {
//            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
//        }
//
//        for (int i = 0; i < cnt; i++) {
//            yVals2.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
//        }
//
//        ArrayList<String> xVals = new ArrayList<String>();
//
//        for (int i = 0; i < cnt; i++)
//            xVals.add(String.valueOf(i));
//
//        RadarDataSet set1 = new RadarDataSet(yVals1, "Set 1");
//        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
//        set1.setFillColor(ColorTemplate.VORDIPLOM_COLORS[0]);
//        set1.setDrawFilled(true);
//        set1.setLineWidth(2f);
//
//        RadarDataSet set2 = new RadarDataSet(yVals2, "Set 2");
//        set2.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
//        set2.setFillColor(ColorTemplate.VORDIPLOM_COLORS[4]);
//        set2.setDrawFilled(true);
//        set2.setLineWidth(2f);
//
//        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
//        sets.add(set1);
//        sets.add(set2);
//
//        RadarData data = new RadarData(xVals, sets);
//        data.setValueTextSize(8f);
//        data.setDrawValues(false);
//
//        mChart1.setData(data);
//
//        mChart1.invalidate();
//    }
//    private void createRadarChart(){
//        mChart1 = (RadarChart) findViewById(R.id.chart2);
//        mChart1.setDescription("");
//
//        mChart1.setWebLineWidth(1.5f);
//        mChart1.setWebLineWidthInner(0.75f);
//        mChart1.setWebAlpha(100);
//        setDataRadarChart();
//
//        mChart1.animateXY(
//                1400, 1400,
//                Easing.EasingOption.EaseInOutQuad,
//                Easing.EasingOption.EaseInOutQuad);
//
//        XAxis xAxis = mChart1.getXAxis();
//        xAxis.setTextSize(9f);
//
//        YAxis yAxis = mChart1.getYAxis();
//        yAxis.setLabelCount(5, false);
//        yAxis.setTextSize(9f);
//        yAxis.setAxisMinValue(0f);
//
//        Legend l = mChart1.getLegend();
//        l.setPosition(LegendPosition.RIGHT_OF_CHART);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(5f);
//    }
//
//    private void createHorizontalChartBaht_HR(int set , float value,String Des){
//        mChart2 = (HorizontalBarChart) findViewById(R.id.chart3);
//        // mChart.setHighlightEnabled(false);
//
//        mChart2.setDrawBarShadow(false);
//
//        mChart2.setDrawValueAboveBar(true);
//
//        mChart2.setDescription("");
//
//        // if more than 60 entries are displayed in the chart, no values will be
//        // drawn
//        mChart2.setMaxVisibleValueCount(60);
//
//        // scaling can now only be done on x- and y-axis separately
//        mChart2.setPinchZoom(false);
//
//        // draw shadows for each bar that show the maximum value
//        // mChart.setDrawBarShadow(true);
//
//        // mChart.setDrawXLabels(false);
//
//        mChart2.setDrawGridBackground(false);
//
//        // mChart.setDrawYLabels(false);
//
//
//        XAxis xl = mChart2.getXAxis();
//        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xl.setDrawAxisLine(true);
//        xl.setDrawGridLines(true);
//        xl.setGridLineWidth(0.3f);
//
//        YAxis setmax = mChart2.getAxisLeft();
//        setmax.setAxisMaxValue(300);
//        YAxis setmax1 = mChart2.getAxisRight();
//        setmax1.setAxisMaxValue(300);
//
//
//
//
//        YAxis yl = mChart2.getAxisLeft();
//        yl.setDrawAxisLine(true);
//        yl.setDrawGridLines(true);
//        yl.setGridLineWidth(0.3f);
//        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
////        yl.setInverted(true);
//
//        YAxis yr = mChart2.getAxisRight();
//        yr.setDrawAxisLine(true);
//        yr.setDrawGridLines(false);
//        yr.setAxisMinValue(0f); // this replaces setStartAtZero(true)
////        yr.setInverted(true);
//
////        if(value >= 50){
//
//
//            if(value >= 150){
//                setDataHorizontal(set, value, R.color.green, Des);
//            }
//            else{
//                setDataHorizontal(set, value,R.color.red, Des);
//            }
//
//
//        mChart2.animateY(2500);
//
//
//        Legend l = mChart2.getLegend();
//        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
//        l.setFormSize(8f);
//        l.setXEntrySpace(4f);
//
//        // mChart.setDrawLegend(false);
//
//    }
//    private void createHorizontalChartBaht_DAY(int set , float value,String Des){
//        mChart2 = (HorizontalBarChart) findViewById(R.id.chart2);
//        // mChart.setHighlightEnabled(false);
//
//        mChart2.setDrawBarShadow(false);
//
//        mChart2.setDrawValueAboveBar(true);
//
//        mChart2.setDescription("");
//
//        // if more than 60 entries are displayed in the chart, no values will be
//        // drawn
//        mChart2.setMaxVisibleValueCount(60);
//
//        // scaling can now only be done on x- and y-axis separately
//        mChart2.setPinchZoom(false);
//
//        // draw shadows for each bar that show the maximum value
//        // mChart.setDrawBarShadow(true);
//
//        // mChart.setDrawXLabels(false);
//
//        mChart2.setDrawGridBackground(false);
//
//        // mChart.setDrawYLabels(false);
//
//
//        XAxis xl = mChart2.getXAxis();
//        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xl.setDrawAxisLine(true);
//        xl.setDrawGridLines(true);
//        xl.setGridLineWidth(0.3f);
//
//        YAxis setmax = mChart2.getAxisLeft();
//        setmax.setAxisMaxValue(3000);
//        YAxis setmax1 = mChart2.getAxisRight();
//        setmax1.setAxisMaxValue(3000);
//
//        YAxis yl = mChart2.getAxisLeft();
//        yl.setDrawAxisLine(true);
//        yl.setDrawGridLines(true);
//        yl.setGridLineWidth(0.3f);
//        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
////        yl.setInverted(true);
//
//        YAxis yr = mChart2.getAxisRight();
//        yr.setDrawAxisLine(true);
//        yr.setDrawGridLines(false);
//        yr.setAxisMinValue(0f); // this replaces setStartAtZero(true)
////        yr.setInverted(true);
//        if(value >= 1500){
//            setDataHorizontal(set, value, R.color.green, Des);
//        }
//        else{
//            setDataHorizontal(set, value,R.color.red, Des);
//        }
//
//        mChart2.animateY(2500);
//
//
//        Legend l = mChart2.getLegend();
//        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
//        l.setFormSize(8f);
//        l.setXEntrySpace(4f);
//
//        // mChart.setDrawLegend(false);
//
//    }
//    private void createHorizontalChartBaht_KM(int set , float value,String Des){
//        mChart2 = (HorizontalBarChart) findViewById(R.id.chart4);
//        // mChart.setHighlightEnabled(false);
//
//        mChart2.setDrawBarShadow(false);
//
//        mChart2.setDrawValueAboveBar(true);
//
//        mChart2.setDescription("");
//
//        // if more than 60 entries are displayed in the chart, no values will be
//        // drawn
//        mChart2.setMaxVisibleValueCount(60);
//
//        // scaling can now only be done on x- and y-axis separately
//        mChart2.setPinchZoom(false);
//
//        // draw shadows for each bar that show the maximum value
//        // mChart.setDrawBarShadow(true);
//
//        // mChart.setDrawXLabels(false);
//
//        mChart2.setDrawGridBackground(false);
//
//        // mChart.setDrawYLabels(false);
//
//
//        XAxis xl = mChart2.getXAxis();
//        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xl.setDrawAxisLine(true);
//        xl.setDrawGridLines(true);
//        xl.setGridLineWidth(0.3f);
//
//
//
//
//
//        YAxis yl = mChart2.getAxisLeft();
//        yl.setDrawAxisLine(true);
//        yl.setDrawGridLines(true);
//        yl.setGridLineWidth(0.3f);
//        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
////        yl.setInverted(true);
//
//        YAxis yr = mChart2.getAxisRight();
//        yr.setDrawAxisLine(true);
//        yr.setDrawGridLines(false);
//        yr.setAxisMinValue(0f); // this replaces setStartAtZero(true)
////        yr.setInverted(true);
//
//        setDataHorizontal(set, value,R.color.green, Des);
//        mChart2.animateY(2500);
//
//
//        Legend l = mChart2.getLegend();
//        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
//        l.setFormSize(8f);
//        l.setXEntrySpace(4f);
//
//        // mChart.setDrawLegend(false);
//
//    }
//    private void setDataHorizontal(int count, float range , int color ,String Des) {
//
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//        ArrayList<String> xVals = new ArrayList<String>();
//
//        for (int i = 0; i < count; i++) {
//            xVals.add(String.valueOf(i));
////            yVals1.add(new BarEntry((float) (Math.random() * range), i));
//            yVals1.add(new BarEntry(range, i));
//
//        }
//
//        BarDataSet set1;
//
//            set1 = new BarDataSet(yVals1, Des);
//            set1.setColor(getResources().getColor(color));
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//
//            BarData data = new BarData(xVals, dataSets);
//            data.setValueTextSize(10f);
////            data.setValueTextSize(5f);
//            mChart2.setData(data);
//
//    }


    private PieChart mChart;
    private RadarChart mChart1;
    private HorizontalBarChart mChart2;

    private void init() {
////        createPieChartJob_HR(1, AppSharedPreferences.getInstance(HomeActivity.this).getStateOnjob()
////                , AppSharedPreferences.getInstance(HomeActivity.this).getStateStandby());
//        createPieChartJob_HR(1, AppSharedPreferences.getInstance(HomeActivity.this).getStateOnjob()
//                , 10 * 3600);
//        createHorizontalChartBaht_DAY(1, AppSharedPreferences.getInstance(HomeActivity.this).getStateBahtDay(), "บาท/วัน (1500)");
////        createHorizontalChartBaht_HR(1, AppSharedPreferences.getInstance(HomeActivity.this).getStateBahtHour(), "BAHT/HRs (150)");
//        createHorizontalChartBaht_HR(1, AppSharedPreferences.getInstance(HomeActivity.this).getStateBahtDay()/hoursshifttime, "บาท/ชม. ("+150+")");
//
//
//        if(AppSharedPreferences.getInstance(HomeActivity.this).getStateLoginKM() != 0 &&AppSharedPreferences.getInstance(HomeActivity.this).getStateApiKm()>=AppSharedPreferences.getInstance(HomeActivity.this).getStateLoginKM()) {
//            createHorizontalChartBaht_KM(1, AppSharedPreferences.getInstance(HomeActivity.this).getStateBahtDay()
//                    / (AppSharedPreferences.getInstance(HomeActivity.this).getStateApiKm() - AppSharedPreferences.getInstance(HomeActivity.this).getStateLoginKM()), "บาท/กิโลเมตร");
////            createHorizontalChartBaht_KM(1, 446 / (AppSharedPreferences.getInstance(HomeActivity.this).getStateApiKm() - AppSharedPreferences.getInstance(HomeActivity.this).getStateLoginKM()), "BAHT/KMs");
//
//        }
//        else{
//            createHorizontalChartBaht_KM(1,0,"บาท/กิโลเมตร");
//        }


//        check_get_job = true;
        //cluster
        initializeUI();
        mMarkers = new ArrayList<Marker>();
        mClusterMarkers = new ArrayList<Marker>();
        //cluster


        aq.id(R.id.log_text).text("Operation ID: " + LoginHelper.getInstance(this).getOperationID() + ", Taxi ID: " + LoginHelper.getInstance(this).getTaxiID() + ", Version: " + getResources().getText(R.string.app_version));

        profile = (HashMap<String, Object>) LoginHelper.getInstance(this).getRememberLogin();
        aq.id(R.id.home_name).text(getString(R.string.home_title_1) + " " + String.valueOf(profile.get("staff_name")));
        //aq.id(R.id.home_logindate).text(getString(R.string.home_title_2) + " " + String.valueOf(profile.get("date")));


        aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_001));

        if (!String.valueOf(profile.get("display_pic")).equalsIgnoreCase("") && !String.valueOf(profile.get("display_pic")).equalsIgnoreCase("null"))
            Picasso.with(HomeActivity.this).load(String.valueOf(profile.get("display_pic"))).resize(driver_pix.getWidth(), driver_pix.getHeight()).placeholder(R.drawable.home_pic_staff).error(R.drawable.home_pic_staff).into((ImageView) aq.id(R.id.home_profile_pix).getView());

        aq.id(R.id.home_btn_mylocation).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pantoLocation == false) {
                    pantoLocation = true;
                    aq.id(R.id.home_btn_mylocation).image(R.drawable.icon_track_location_active);
                } else {
                    pantoLocation = false;
                    aq.id(R.id.home_btn_mylocation).image(R.drawable.icon_track_location_inactive);
                }

                String[] mylocation = AppSharedPreferences.getInstance(HomeActivity.this).getLocation();
                Location location = new Location("");
                location.setLatitude(Double.parseDouble(mylocation[0]));
                location.setLongitude(Double.parseDouble(mylocation[1]));
                CurrentLocation.currentLocation = location;
                updateUILocation(mylocation[0], mylocation[1], pantoLocation);
            }
        });


        mapview = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.home_mapview)).getMap();
        mapview.setMapType(AppSharedPreferences.getInstance(this).getMapMode());
//        mapview.getUiSettings().setZoomControlsEnabled(true);
        mapview.setOnMarkerClickListener(this);
        mapview.setOnInfoWindowClickListener(this);
        mapview.setTrafficEnabled(false);
        mapview.getUiSettings().setCompassEnabled(false);

        //marker
//        markerImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
//        mapview.getUiSettings().setMyLocationButtonEnabled(true);
//        LatLng position = new LatLng(initLat2, initLng2);
//        mapview.animateCamera(CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM_LEVEL));
//        mapview.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//                if (cameraPosition.zoom != oldZoom) {
//                    try {
//                        clusters = MarkersClusterizer.clusterMarkers(mapview, markers, INTERVAL);
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                oldZoom = cameraPosition.zoom;
//            }
//        });
//        createMarkers(mapview);

        //

        aq.id(R.id.icon).clicked(this);
        aq.id(R.id.icon).visibility(View.GONE);
//        aq.id(R.id.button2).clicked(this);
//        aq.id(R.id.button2).visibility(View.GONE);
//        aq.id(R.id.webView1).visibility(View.GONE);
//        aq.id(R.id.cluster).visibility(View.GONE);
//        aq.id(R.id.create).visibility(View.GONE);

        aq.id(R.id.cam_on).visibility(View.GONE);
        aq.id(R.id.cam_off).visibility(View.GONE);

        aq.id(R.id.log_out).clicked(this);
        aq.id(R.id.icon_thum).visibility(View.GONE);
        aq.id(R.id.icon_app).visibility(View.GONE);
//        aq.id(R.id.icon_visa).visibility(View.GONE);
        aq.id(R.id.icon_reserve).visibility(View.GONE);

//        aq.id(R.id.internetOpen).visibility(View.GONE);
//        aq.id(R.id.internetClose).visibility(View.GONE);
        aq.id(R.id.networkClose).visibility(View.GONE);
        aq.id(R.id.networkOpen).visibility(View.GONE);

        aq.id(R.id.btn_menu).clicked(this);
        aq.id(R.id.home_customer_pix).clicked(this);
        aq.id(R.id.btn_Call).clicked(this);

        aq.id(R.id.btn_traffic_non).clicked(this);
        aq.id(R.id.btn_traffic_non).visibility(View.GONE);
        aq.id(R.id.btn_traffic).clicked(this);
        aq.id(R.id.btn_traffic).visibility(View.GONE);

        aq.id(R.id.btn_menu_close).clicked(this);
        aq.id(R.id.btn_menu_close).visibility(View.GONE);
        aq.id(R.id.home_btn_mylocation).visibility(View.GONE);


        aq.id(R.id.home_btn_gas).clicked(this);
        aq.id(R.id.home_btn_gas).visibility(View.GONE);

        aq.id(R.id.btn_Start_Mter).visibility(View.GONE);
        aq.id(R.id.btn_Start_Mter).clicked(this);
        aq.id(R.id.btn_Stop_Meter).visibility(View.GONE);
        aq.id(R.id.btn_Stop_Meter).clicked(this);

        aq.id(R.id.home_topframe2).visibility(View.GONE);
        aq.id(R.id.home_customer_pix).visibility(View.GONE);
        aq.id(R.id.btn_Call).visibility(View.GONE);
        aq.id(R.id.home_customer_name).text(getString(R.string.home_title_4));

        //aq.id(R.id.home_profile_pix).clicked(this);

        aq.id(R.id.home_btn_incident).clicked(this);
        aq.id(R.id.home_btn_incident).visibility(View.GONE);

//        aq.id(R.id.home_btn_statusbar_1).clicked(this);
        aq.id(R.id.btn_thumbing).clicked(this);
        aq.id(R.id.home_btn_outofservice).clicked(this);
        aq.id(R.id.home_btn_sos).clicked(this);
        aq.id(R.id.btn_pick_customer).clicked(HomeActivity.this);
        aq.id(R.id.bt_jobtype_1).clicked(this);
        aq.id(R.id.bt_jobtype_2).clicked(this);
        aq.id(R.id.bt_jobtype_3).clicked(this);
        aq.id(R.id.bt_jobtype_4).clicked(this);
        aq.id(R.id.bt_jobtype_5).clicked(this);
        aq.id(R.id.bt_jobtype_6_8).clicked(this);
        aq.id(R.id.bt_jobtype_9).clicked(this);
        aq.id(R.id.home_btn_hubpoint).clicked(this);

        //Add by Beat
        aq.id(R.id.btn_navigation).clicked(this);

        //((ImageView)aq.id(R.id.home_btn_hubpoint).getView()).setAlpha(0.5f);

        mapview.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if(layout_destinationMarker.getVisibility() == View.VISIBLE) {

                    latLng_destination = mapview.getCameraPosition().target;

                    if(latLng_destination != null) {
                        try {
                            //mAutocompleteTextView.setFocusable(true);

                            if (getAddressTask != null) {
                                getAddressTask.cancel(true);
                            }
                            getAddressTask = new GetAddressTask();
                            getAddressTask.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //destination_maker.setPosition(latLng_destination);
                    //destination_maker = mapview.addMarker(new MarkerOptions().position(latLng_destination).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin_destination)));
                }
            }
        });

        layout_destinationMarker = (FrameLayout) findViewById(R.id.layout_destinationMarker);


        checkState();
    }

    private void setProgressbarVisibility(boolean visibility) {

        if (visibility) {
            aq.id(R.id.home_loading_frame).getView().setVisibility(View.VISIBLE);
        } else
            aq.id(R.id.home_loading_frame).getView().setVisibility(View.GONE);
    }

    public void updateUILocation(String latitude, String longitude, boolean pantoLocation) {
        System.out.println("lat: "+latitude+" lng: "+longitude);
        // Edit by Beat

        if(!latitude.equalsIgnoreCase("") && !longitude.equalsIgnoreCase("")) {

            if (Status.enableNavigation) {


                //Add by Beat
                // Geo Magnetic Field
                GeomagneticField field = new GeomagneticField(
                        (float) CurrentLocation.currentLocation.getLatitude(),
                        (float) CurrentLocation.currentLocation.getLongitude(),
                        (float) CurrentLocation.currentLocation.getAltitude(),
                        System.currentTimeMillis()
                );

                // getDeclination returns degrees
                mDeclination = field.getDeclination();


                if (myLocation_maker != null)
                    myLocation_maker.remove();

                myLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                myLocation_maker = mapview.addMarker(new MarkerOptions().position(myLocation)
                        .title("I'm here.")
                        .flat(true)
                        .rotation((float) bearing +180)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));

                if (pantoLocation) {
                    isNewlyOpened = false;
                    panTo_myLocation_navigation = new CameraPosition.Builder().target(myLocation).zoom(current_zoom).tilt(25).bearing((float)bearing).build();
                    changeCamera(CameraUpdateFactory.newCameraPosition(panTo_myLocation_navigation));
                }

                if(googleDirectionStepArrayList != null) {

                    for(int i = 0; i < googleDirectionStepArrayList.size(); i++) {
                        if (googleDirectionStepArrayList.get(i).iswarnning){
                            if (checkIntoLatlngBound(googleDirectionStepArrayList.get(i).getBounds(), new LatLng(CurrentLocation.currentLocation.getLatitude(), CurrentLocation.currentLocation.getLongitude()))) {

                                if(CurrentLocation.currentLocation != null && googleDirectionStepArrayList.get(i).getPolyline() != null && i > 0) {
                                    LatLng currentLatLng = new LatLng(CurrentLocation.currentLocation.getLatitude(), CurrentLocation.currentLocation.getLongitude());
                                    if(PolyUtil.isLocationOnPath(currentLatLng, googleDirectionStepArrayList.get(i-1).getPolyline(), false, 10)) {
                                        Toast.makeText(getApplicationContext(), googleDirectionStepArrayList.get(i).getManeuver(), Toast.LENGTH_SHORT).show();
                                        checkManeuver(googleDirectionStepArrayList.get(i).getManeuver());
                                        ImageView imageView = (ImageView) findViewById(R.id.im_navigation);
                                        TextView tv_naviText = (TextView) findViewById(R.id.tv_naviText);
                                        textManeuver(tv_naviText, googleDirectionStepArrayList.get(i).getManeuver());
                                        imageManeuver(imageView, googleDirectionStepArrayList.get(i).getManeuver());
                                        googleDirectionStepArrayList.get(i).iswarnning = false;
                                        break;
                                    }
                                }
                            }

                        }

                        if(i == googleDirectionStepArrayList.size() - 1) {
                            if (googleDirectionStepArrayList.get(i).iswarnning_end){
                                if (checkIntoLatlngBound(googleDirectionStepArrayList.get(i).getBounds_end(), new LatLng(CurrentLocation.currentLocation.getLatitude(), CurrentLocation.currentLocation.getLongitude()))) {

                                    if(CurrentLocation.currentLocation != null && googleDirectionStepArrayList.get(i).getPolyline() != null && i > 0) {
                                        LatLng currentLatLng = new LatLng(CurrentLocation.currentLocation.getLatitude(), CurrentLocation.currentLocation.getLongitude());
                                        if(PolyUtil.isLocationOnPath(currentLatLng, googleDirectionStepArrayList.get(i-1).getPolyline(), false, 15)) {
                                            Toast.makeText(getApplicationContext(), googleDirectionStepArrayList.get(i).getManeuver(), Toast.LENGTH_SHORT).show();
                                            playAudio(R.raw.todestination);
                                            googleDirectionStepArrayList.get(i).iswarnning_end = false;
                                            break;
                                        }
                                    }
                                }

                            }
                        }
                    }

                }


            } else {

                if (myLocation_maker != null)
                    myLocation_maker.remove();

                myLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                myLocation_maker = mapview.addMarker(new MarkerOptions().position(myLocation).title("I'm here.").icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin_taxi)));
                if (pantoLocation) {
                    isNewlyOpened = false;
                    panTo_myLocation = new CameraPosition.Builder().target(myLocation).zoom(current_zoom).bearing(0).tilt(25).build();
                    changeCamera(CameraUpdateFactory.newCameraPosition(panTo_myLocation));
                }
            }
        }
    }

    private void changeCamera(CameraUpdate update) {
        mapview.animateCamera(update, null);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onClick(View v) {


        HashMap<String, Object> data = (HashMap<String, Object>) AppSharedPreferences.getInstance(HomeActivity.this).getState_1();
        switch (v.getId()) {
            case R.id.btn_thumbing:
//                check_get_job = false;
                //add
                stopService(intent_jobTracking);
                check_thum_re = false;
                if(timerMeter != null) {
                    timerMeter.cancel();
                    timerMeter = null;
                }
                aq.id(R.id.btn_Start_Mter).visible();
                aq.id(R.id.icon_thum).visible();

                if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2")) {
                    //Oak
                    //Toast.makeText(getApplicationContext(),"Endjob1",Toast.LENGTH_SHORT).show();
                    endJobWithoutResult(AppSharedPreferences.getInstance(this).getJobID());
                    endTripWithoutResult(AppSharedPreferences.getInstance(this).getJobID(), AppSharedPreferences.getInstance(this).getJobType());
                    AppSharedPreferences.getInstance(this).removeJobID();
                    AppSharedPreferences.getInstance(this).removeJobType();
                }

                if(Status.enableNavigation) {
                    closeNavigation();
                    Status.enableNavigation = false;
                }

                AppSharedPreferences.getInstance(HomeActivity.this).removeJobID();
                AppSharedPreferences.getInstance(HomeActivity.this).removeJobType();
                AppSharedPreferences.getInstance(HomeActivity.this).removeState_Hub();
                resetProgressState();

//                aq.id(R.id.home_btn_statusbar_1).clicked(null);
//                aq.id(R.id.home_btn_statusbar_1).background(R.drawable.home_status2_active);
//                aq.id(R.id.home_btn_statusbar_1).textColorId(R.color.white);
                aq.id(R.id.btn_thumbing).visibility(View.GONE);
                aq.id(R.id.bt_jobtype_2).visibility(View.GONE);
                aq.id(R.id.home_btn_sos).image(R.drawable.home_sos);
                aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_002));

                Status.enable = false;
                // Toast.makeText(getApplicationContext(),"Status.enable = " + Status.enable, Toast.LENGTH_SHORT).show();

                AppSharedPreferences.getInstance(HomeActivity.this).setThumbingMode(true);
                stopService(intent_jobTracking);

                thumbingCustomer(LoginHelper.getInstance(this).getTaxiID(), LoginHelper.getInstance(this).getDriverID(), LoginHelper.getInstance(this).getOperationID());
                break;
//            case R.id.home_btn_statusbar_1:
//
//                if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2"))
//                    //Oak
//                    //Toast.makeText(getApplicationContext(),"Endjob2",Toast.LENGTH_SHORT).show();
//                    endJobWithoutResult(AppSharedPreferences.getInstance(this).getJobID());
//
//                AppSharedPreferences.getInstance(HomeActivity.this).removeJobID();
//                AppSharedPreferences.getInstance(HomeActivity.this).removeJobType();
//                AppSharedPreferences.getInstance(HomeActivity.this).removeState_Hub();
//                resetProgressState();
//
//                aq.id(R.id.home_btn_statusbar_1).clicked(null);
//                aq.id(R.id.home_btn_statusbar_1).background(R.drawable.home_status2_active);
//                aq.id(R.id.home_btn_statusbar_1).textColorId(R.color.white);
//                aq.id(R.id.btn_thumbing).visibility(View.GONE);
//
//                aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_002));
//
//                AppSharedPreferences.getInstance(HomeActivity.this).setThumbingMode(true);
//                thumbingCustomer(LoginHelper.getInstance(this).getTaxiID(), LoginHelper.getInstance(this).getDriverID(), LoginHelper.getInstance(this).getOperationID());
//                break;

            case R.id.btn_pick_customer:
                arrivedPickLocation(AppSharedPreferences.getInstance(this).getJobID());
                break;

            case R.id.btn_traffic:
                aq.id(R.id.btn_traffic).visibility(View.GONE);
                aq.id(R.id.btn_traffic_non).visible();
                mapview.setTrafficEnabled(false);
                break;

            case R.id.btn_traffic_non:
                aq.id(R.id.btn_traffic_non).visibility(View.GONE);
                aq.id(R.id.btn_traffic).visible();
                mapview.setTrafficEnabled(true);

                break;

            case R.id.btn_menu:

                aq.id(R.id.button).visible();
                aq.id(R.id.btn_menu).visibility(View.GONE);
                aq.id(R.id.btn_menu_close).visible();
                aq.id(R.id.btn_traffic_non).visible();
                aq.id(R.id.home_btn_gas).visible();
                aq.id(R.id.home_btn_incident).visible();
                aq.id(R.id.btn_navigation).visible();
                aq.id(R.id.home_btn_mylocation).visible();

                aq.id(R.id.btn_traffic_non).animate(fade_in);
                aq.id(R.id.home_btn_gas).animate(fade_in);
                aq.id(R.id.home_btn_incident).animate(fade_in);
                aq.id(R.id.btn_navigation).animate(fade_in);
                aq.id(R.id.home_btn_mylocation).animate(fade_in);


                break;

            case R.id.btn_menu_close:

                aq.id(R.id.button).visibility(View.GONE);
                aq.id(R.id.cluster).visibility(View.GONE);
                aq.id(R.id.btn_menu_close).visibility(View.GONE);

                aq.id(R.id.btn_menu).visible();
                aq.id(R.id.home_btn_gas).visibility(View.GONE);
                aq.id(R.id.home_btn_incident).visibility(View.GONE);
                aq.id(R.id.home_btn_mylocation).visibility(View.GONE);
                aq.id(R.id.btn_navigation).visibility(View.GONE);
                aq.id(R.id.btn_traffic).visibility(View.GONE);
                aq.id(R.id.btn_traffic_non).visibility(View.GONE);



                break;

            case R.id.home_btn_gas:
                //add
                stopService(intent_jobTracking);
                String fillgas = APIs.fillgas(taxi_id_home);
                aq.ajax(fillgas, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String data, AjaxStatus status) {
                        if (status.getCode() == 200) {
                            try {
                                JSONParser json = new JSONParser(data);
                                HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();
                                if(String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {
                                    HashMap<String, Object> dataObj = (HashMap<String, Object>) resultObj.get("data");
                                    startJob(String.valueOf(dataObj.get("job_id")), String.valueOf(dataObj.get("job_type")));
                                    showGasStationDialog(dataObj);
                                    endJob(String.valueOf(dataObj.get("job_id")), enable_credit_card, reserved_type);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"code != 200",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),"Json exception",Toast.LENGTH_LONG).show();
                            }

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"status.getCode != 200",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                break;

//            case R.id.button2:
//            if(Status.checkweb){
//                aq.id(R.id.button2).text("open");
//                aq.id(R.id.webView1).visibility(View.GONE);
//                Status.checkweb = false;
//            }
//                else{
//                aq.id(R.id.webView1).visible();
//                aq.id(R.id.button2).text("close");
//                webView = (WebView) findViewById(R.id.webView1);
//                webView.setWebViewClient(new WebViewClient());
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.loadUrl("http://www.google.com");
//                Status.checkweb = true;
//            }
//                break;
//
//            case R.id.home_btn_statusbar_2:
//                Status.enable = false;
//                //Toast.makeText(getApplicationContext(),"Status.enable = " + Status.enable, Toast.LENGTH_SHORT).show();
//                AppSharedPreferences.getInstance(this).setJobType("1");
//                arrivedPickLocation(AppSharedPreferences.getInstance(this).getJobID());
//                startJob(AppSharedPreferences.getInstance(this).getJobID(), AppSharedPreferences.getInstance(this).getJobType());
//                break;

            case R.id.btn_Start_Mter:
                String manStart = APIs.ManMeterstart();
                aq.ajax(manStart, String.class, new AjaxCallback<String>() {
                });
                aq.id(R.id.btn_Start_Mter).visibility(View.GONE);
                aq.id(R.id.btn_Stop_Meter).visible();

                Status.enable = false;
                AppSharedPreferences.getInstance(this).setJobType("1");
                arrivedPickLocation(AppSharedPreferences.getInstance(this).getJobID());
                //startJob(AppSharedPreferences.getInstance(this).getJobID(), AppSharedPreferences.getInstance(this).getJobType());
                break;

            case R.id.btn_Stop_Meter:
                //not use
                String manStop = APIs.ManMeterstop();
                aq.ajax(manStop, String.class, new AjaxCallback<String>(){});
                endJob(AppSharedPreferences.getInstance(this).getJobID(), enable_credit_card, reserved_type);
//                check_get_job = false;
                if(Status.enableNavigation) {
                    closeNavigation();
                }
                aq.id(R.id.btn_Stop_Meter).visibility(View.GONE);
                Status.enable = true;

                //endJob(AppSharedPreferences.getInstance(this).getJobID(),enable_credit_card,reserved_type);


                break;

//            case R.id.home_btn_statusbar_3:
//                aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_status2_active);
//                aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.white);
//
//                //Add by Beat
//                /*HashMap<String, Object> dataObj1 = new HashMap<String, Object>();
//                dataObj1.put("job_id",AppSharedPreferences.getInstance(this).getJobID());
//                dataObj1.put("total_distance","0");
//                dataObj1.put("total_time","0");
//                dataObj1.put("job_type",AppSharedPreferences.getInstance(this).getJobType());
//                dataObj1.put("payment_type","0");
//                dataObj1.put("payment_channel","0");
//                dataObj1.put("total_cost","0");
//                dataObj1.put("fare","0");
//                dataObj1.put("toll", "0");
//                dataObj1.put("trip_time", "0");
//                dataObj1.put("km", "0");
//                //AppSharedPreferences.getInstance(HomeActivity.this).setState_Payment(dataObj1);
//                showManualTripReportDialog(dataObj1);
//                //End
//                endJob(AppSharedPreferences.getInstance(this).getJobID());
//                //enableAuto = true;*/
//                Status.enable = true;
//                //Toast.makeText(getApplicationContext(),"Status.enable = " + Status.enable, Toast.LENGTH_SHORT).show();
//
//                endJob(AppSharedPreferences.getInstance(this).getJobID(),enable_credit_card,reserved_type);
//                break;
//            case R.id.home_btn_statusbar_4:
//
//                if (AppSharedPreferences.getInstance(HomeActivity.this).getJobType().equalsIgnoreCase("9")) {
//                    HashMap<String, Object> dataObj = new HashMap<String, Object>();
//                    dataObj.put("job_id", AppSharedPreferences.getInstance(HomeActivity.this).getJobID());
//                    dataObj.put("job_type", AppSharedPreferences.getInstance(HomeActivity.this).getJobType());
//                    showGasPaymentDialog(dataObj);
//                } else if (AppSharedPreferences.getInstance(HomeActivity.this).getJobType().equalsIgnoreCase("1")) {
//
//                    HashMap<String, Object> dataObj = AppSharedPreferences.getInstance(HomeActivity.this).getState_Payment();
//                    showTripReportDialog(dataObj);
//                }
//                break;
//            case R.id.home_profile_pix:
//                showLogoutDialog();
//                break;
            case R.id.log_out:
                showLogoutDialog();
                break;
            case R.id.home_btn_incident:
                showIncidentDialog();
                break;
            case R.id.home_btn_outofservice:

                String breaking_api = APIs.getBreakAPI();
                aq.ajax(breaking_api, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String data, AjaxStatus status) {

                        try {
                            setProgressbarVisibility(false);
                            JSONParser json = new JSONParser(data);
                            HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();

                            if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {
                                showBreakingDialog();
                            } else {
                                Global.showAlertDialog(HomeActivity.this, String.valueOf(resultObj.get("message")));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.home_btn_sos:
                callSOS();
                break;
//            case R.id.home_customer_pix:
//                if (!msisdn.equalsIgnoreCase("") && !msisdn.equalsIgnoreCase("null") && AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("1"))
//                    alertCall(msisdn);
//                break;
            case R.id.btn_Call:
                if (!msisdn.equalsIgnoreCase("") && !msisdn.equalsIgnoreCase("null") && AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("1"))
                    alertCall(msisdn);
                break;

            case R.id.home_btn_hubpoint:
                standByMode();
                break;
            case R.id.bt_jobtype_1:
                //เมื่อถึงจุดรับ
                arrivedPickLocation(AppSharedPreferences.getInstance(this).getJobID());
                break;
            case R.id.bt_jobtype_2:
                //เมื่อถึงจุดจอด
//                aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_status2_active);
//                aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.white);
                //oak
                //Toast.makeText(getApplicationContext(),"Endjob4",Toast.LENGTH_SHORT).show();
                endJob(AppSharedPreferences.getInstance(this).getJobID(),enable_credit_card,reserved_type);
                break;
            case R.id.bt_jobtype_3:
                //เมื่อถึงจุดเปลี่ยนกะ
//                aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_status2_active);
//                aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.white);
                //oak
                //Toast.makeText(getApplicationContext(),"Endjob5",Toast.LENGTH_SHORT).show();
                endJob(AppSharedPreferences.getInstance(this).getJobID(),enable_credit_card,reserved_type);
                break;
            case R.id.bt_jobtype_4:
                //เมื่อถึงนครชัยแอร์
//                aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_status2_active);
//                aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.white);
                //oak
                //Toast.makeText(getApplicationContext(),"Endjob6",Toast.LENGTH_SHORT).show();
                endJob(AppSharedPreferences.getInstance(this).getJobID(),enable_credit_card,reserved_type);
                break;
            case R.id.bt_jobtype_5:
                //เมื่อถึงศูนย์ Toyota
//                aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_status2_active);
//                aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.white);
//                //oak
                //Toast.makeText(getApplicationContext(),"Endjob7",Toast.LENGTH_SHORT).show();
                endJob(AppSharedPreferences.getInstance(this).getJobID(),enable_credit_card,reserved_type);
                break;
            case R.id.bt_jobtype_6_8:
                //เมื่อถึงจุดล้างรถ
//                aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_status2_active);
//                aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.white);
                //oak
                //Toast.makeText(getApplicationContext(),"Endjob8",Toast.LENGTH_SHORT).show();
                endJob(AppSharedPreferences.getInstance(this).getJobID(),enable_credit_card,reserved_type);
                break;
            case R.id.bt_jobtype_9:
                //เมื่อถึงปั้มน้ำมัน
//                aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_status2_active);
//                aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.white);
                //oak
                //Toast.makeText(getApplicationContext(),"Endjob9",Toast.LENGTH_SHORT).show();
                endJob(AppSharedPreferences.getInstance(this).getJobID(),enable_credit_card,reserved_type);
                break;
            //Add by Beat
            case R.id.btn_navigation:
                Status.checkStatus = 2;
                if(Status.navigation){

                    aq.id(R.id.btn_navigation).image(R.drawable.home_navigation);
                    layout_destinationMarker.setVisibility(View.GONE);
                    pantoLocation = true;

                    if (taxi2othersJob != null)
                        taxi2othersJob.remove();

                    if (customer2destination != null)
                        customer2destination.remove();

                    if (taxi2customer != null)
                        taxi2customer.remove();

                    taxi2customer = null;
                    taxi2othersJob = null;
                    customer2destination = null;

                    Status.enableNavigation = false;
                    current_zoom = 15;

                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    if(p != null) {
                        p.addRule(RelativeLayout.BELOW, R.id.home_topframe);
                    }

                    LinearLayout lyt_home_topframe2 = (LinearLayout) findViewById(R.id.home_topframe2);

                    if(lyt_home_topframe2 != null) {
                        lyt_home_topframe2.setLayoutParams(p);
                    }

                    unRegisteerSensor();
                    if(outOfTheWayTimer != null) {
                        outOfTheWayTimer.cancel();
                        outOfTheWayTimer = null;
                    }
                    checkSound = true;
                    aq.id(R.id.btn_navigation).image(R.drawable.home_navigation);
                    aq.id(R.id.im_navigation).image(0);
                    aq.id(R.id.tv_naviText).text("");
                    aq.id(R.id.home_topframe_navigation).visibility(View.GONE);
                    aq.id(R.id.home_topframe_autocomplete).visibility(View.GONE);
                    aq.id(R.id.home_topframe).visibility(View.VISIBLE);
                    googleDirectionStepArrayList = null;

                    if(mAutocompleteTextView != null) {
                        mAutocompleteTextView.setText("");
                        mAutocompleteTextView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mAutocompleteTextView.setText("");
                            }
                        });
                    }
                    Status.navigation = false;

                } else {
                    Status.navigation = true;
                    aq.id(R.id.btn_navigation).image(R.drawable.home_navigation_active);
                    layout_destinationMarker.setVisibility(View.VISIBLE);
                    pantoLocation = false;
                    openNavigation();
                }
                break;
            case R.id.home_btn_sos2:
                callSOS();
                break;
            case R.id.icon:
                getShownData();
            default:
                break;
        }
    }

    private void getTaxiStatus(String data) {
//        Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
//        aq.id(R.id.textView6).text("ReservedID = "+AppSharedPreferences.getInstance(this).getReservedID());
        JSONParser json;
        try {
            json = new JSONParser(data);
            HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();
            if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {

                HashMap<String, Object> dataObj = (HashMap<String, Object>) resultObj.get("data");

                if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("1")) {

                    if (String.valueOf(dataObj.get("connection")).equalsIgnoreCase("false")) {

                        closeDialog();
                        AppSharedPreferences.getInstance(this).setStateConnection(true);
                        if (dialog_outofservice == null)
                            showNotificationOutOfServiceDialog();

                    } else if (String.valueOf(dataObj.get("connection")).equalsIgnoreCase("true") && AppSharedPreferences.getInstance(this).getStateConnection()) {

                        if (dialog_outofservice != null)
                            dialog_outofservice.dismiss();

                        dialog_outofservice = null;
                        AppSharedPreferences.getInstance(this).removeStateConnection();
                        resetProgressState();
                    }

                    if (String.valueOf(dataObj.get("job_cancel")).equalsIgnoreCase("true") && AppSharedPreferences.getInstance(this).getOnJob() && !AppSharedPreferences.getInstance(this).getJobID().equalsIgnoreCase("")) {

                        alertCancelJob();
                    }

                    String str_meter_status = "";
                    try {
                        str_meter_status = String.valueOf(dataObj.get("meter_status"));
                    } catch (NullPointerException ex) {
                        str_meter_status = "0";
                        ex.printStackTrace();
                    }

                    if (AppSharedPreferences.getInstance(HomeActivity.this).getPickCustomer() && str_meter_status.equalsIgnoreCase("1") &&
                            AppSharedPreferences.getInstance(HomeActivity.this).getOnState2() == false &&
                            AppSharedPreferences.getInstance(HomeActivity.this).getOnState3() == false) {

                        msisdn = "";
                        aq.id(R.id.home_btn_sos).image(R.drawable.home_sos);
                        startJob(AppSharedPreferences.getInstance(HomeActivity.this).getJobID(), AppSharedPreferences.getInstance(HomeActivity.this).getJobType());
                        //enableAuto = true;

                    } else if (AppSharedPreferences.getInstance(HomeActivity.this).getPickCustomer() && str_meter_status.equalsIgnoreCase("0") &&
                            AppSharedPreferences.getInstance(HomeActivity.this).getOnState2() &&
                            AppSharedPreferences.getInstance(HomeActivity.this).getOnState3() == false) {

                        //Test Oak
                        //Toast.makeText(getApplicationContext(),"Endjob10",Toast.LENGTH_SHORT).show();
                        //enableAuto = true;
                        endJob(AppSharedPreferences.getInstance(HomeActivity.this).getJobID(),enable_credit_card,reserved_type);
                    }

                } else if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("0")) {

                    if (String.valueOf(dataObj.get("connection")).equalsIgnoreCase("true")) {

                        AppSharedPreferences.getInstance(HomeActivity.this).removeJobType();
                        stopService(intent_taxi_status);
                        startService(intent_jobTracking);
                    }
                }

                double double_speed = 0;

                try {
                    AppSharedPreferences.getInstance(HomeActivity.this).setStateApiKm(Float.parseFloat(String.valueOf(dataObj.get("km"))));
                } catch (Exception ex) {
                    AppSharedPreferences.getInstance(HomeActivity.this).setStateApiKm(0);
                    ex.printStackTrace();
                }
                try {
                    double_speed = Double.parseDouble(String.valueOf(dataObj.get("speed")));
                } catch (NullPointerException ex) {
                    double_speed = 0;
                    ex.printStackTrace();
                }

                if (double_speed > 90 && dialog_outofservice == null) {
                    if (dialog_speed == null)
                        showLimitSpeedDialog();

                    if (!mediaPlayer.isPlaying())
                        mediaPlayer.start();

                } else {

                    if (mediaPlayer != null && mediaPlayer.isPlaying() && dialog_speed != null) {
                        mediaPlayer.stop();
                    }

                    if (dialog_speed != null) {
                        dialog_speed.dismiss();
                        dialog_speed = null;
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    String reserved_type = "0";
    String enable_credit_card = "0";
    boolean check_get_job = true;
    int q = 0;
    private void getJobRequested(String data) {
//        if(check_get_job)
//        {
//        q++;
//        aq.id(R.id.textView7).text("Running :"+q+"  Data"+data);
            JSONParser json;
            try {
                json = new JSONParser(data);
                HashMap<String, Object> dataObj = (HashMap<String, Object>) json.convertJson2HashMap();
                Global.printLog(true, "dataObj", String.valueOf(dataObj));

                if (String.valueOf(dataObj.get("code")).equalsIgnoreCase("200") && dialog == null) {

                    HashMap<String, Object> reserveObj = (HashMap<String, Object>) dataObj.get("data");
                    if (String.valueOf(reserveObj.get("job_type")).equalsIgnoreCase("0")) {

                        //Out of Service
                    } else if (String.valueOf(reserveObj.get("job_type")).equalsIgnoreCase("1")) {
                        //Job Reserved
                        if (String.valueOf(reserveObj.get("enable_credit_card")).equalsIgnoreCase("0")) {
                            enable_credit_card = "0";
                            AppSharedPreferences.getInstance(HomeActivity.this).setEnableCard(enable_credit_card);
                        }

                        if (String.valueOf(reserveObj.get("enable_credit_card")).equalsIgnoreCase("1")) {
                            enable_credit_card = "1";
                            AppSharedPreferences.getInstance(HomeActivity.this).setEnableCard(enable_credit_card);
                        }

                        if (String.valueOf(reserveObj.get("reserved_type")).equalsIgnoreCase("1")) {
                            //aq.id(R.id.icon_app).visible();
                            reserved_type = "1";
                            AppSharedPreferences.getInstance(HomeActivity.this).setReservedType(reserved_type);
                        }
                        if (String.valueOf(reserveObj.get("reserved_type")).equalsIgnoreCase("2")) {
                            //aq.id(R.id.icon_reserve).visible();
                            reserved_type = "2";
                            AppSharedPreferences.getInstance(HomeActivity.this).setReservedType(reserved_type);
                        }

                        if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2")) {

                            resetProgressState();
                            //Oak
                            //Toast.makeText(getApplicationContext(),"Endjob11",Toast.LENGTH_SHORT).show();
                            endJobWithoutResult(AppSharedPreferences.getInstance(this).getJobID());

//                            aq.id(R.id.home_btn_statusbar_1).clicked(HomeActivity.this);
//                            aq.id(R.id.home_btn_statusbar_2).clicked(null);
//                            aq.id(R.id.home_btn_statusbar_3).clicked(null);
                            aq.id(R.id.btn_thumbing).visibility(View.VISIBLE);

                            aq.id(R.id.btn_Start_Mter).visibility(View.GONE);
                            aq.id(R.id.btn_Stop_Meter).visibility(View.VISIBLE);

                            aq.id(R.id.btn_pick_customer).visibility(View.GONE);

//                            aq.id(R.id.home_btn_statusbar_1).textColorId(R.color.black);
//                            aq.id(R.id.home_btn_statusbar_2).textColorId(R.color.black);
//                            aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.black);
//
//                            aq.id(R.id.home_btn_statusbar_1).background(R.drawable.home_btn_statusbar);
//                            aq.id(R.id.home_btn_statusbar_2).background(R.drawable.home_btn_statusbar);
//                            aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_btn_statusbar);

                            endTripWithoutResult(AppSharedPreferences.getInstance(this).getJobID(), AppSharedPreferences.getInstance(this).getJobType());

                            AppSharedPreferences.getInstance(HomeActivity.this).removeJobID();
                            AppSharedPreferences.getInstance(HomeActivity.this).removeJobType();
                            AppSharedPreferences.getInstance(HomeActivity.this).removeState_Hub();
                            aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_001));
                            startService(intent_jobTracking);

                            if (others_marker != null) {
                                others_marker.remove();
                                others_marker = null;
                                others_location = null;
                            }

                            if (taxi2othersJob != null) {
                                taxi2othersJob.remove();
                                taxi2othersJob = null;
                            }

                        }

                        msisdn = String.valueOf(reserveObj.get("mobile"));
                        showNotificationDialog(reserveObj, true);


                        getDataToShown(String.valueOf(reserveObj.get("request_date")), String.valueOf(reserveObj.get("cust_name")), String.valueOf(reserveObj.get("location_title")), String.valueOf(reserveObj.get("destination_title")));
                    } else if (String.valueOf(reserveObj.get("job_type")).equalsIgnoreCase("2") && !AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2")) {
                        //Go to Hub
                        showHubPointDialog(reserveObj);
                    } else if (String.valueOf(reserveObj.get("job_type")).equalsIgnoreCase("3") && !AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2")) {
                        //Assign Shift
                        resetProgressState();
                        endJobWithoutResult(AppSharedPreferences.getInstance(this).getJobID());
                        endTripWithoutResult(AppSharedPreferences.getInstance(this).getJobID(), AppSharedPreferences.getInstance(this).getJobType());
                        AppSharedPreferences.getInstance(HomeActivity.this).removeJobID();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeJobType();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeState_Hub();
                        showShiftDialog(reserveObj);
                    } else if (String.valueOf(reserveObj.get("job_type")).equalsIgnoreCase("4") && !AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2")) {
                        //Go to HQ
                        showOutOfServiceDialog(reserveObj);
                    } else if (String.valueOf(reserveObj.get("job_type")).equalsIgnoreCase("5") && !AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2")) {
                        //Go to TOYOTA
                        showGoToToyotaDialog(reserveObj);
                    } else if (String.valueOf(reserveObj.get("job_type")).equalsIgnoreCase("6") && !AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2")) {
                        //Go to Cleaning Full
                        reserveObj.put("popup_desc", getString(R.string.carwash_desc_1));
                        showGoToCarWash(reserveObj);
                    } else if (String.valueOf(reserveObj.get("job_type")).equalsIgnoreCase("7") && !AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2")) {
                        //Go to Cleaning Medium
                        reserveObj.put("popup_desc", getString(R.string.carwash_desc_1_2));
                        showGoToCarWash(reserveObj);
                    } else if (String.valueOf(reserveObj.get("job_type")).equalsIgnoreCase("8") && !AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2")) {
                        //Go to Cleaning Fast
                        reserveObj.put("popup_desc", getString(R.string.carwash_desc_1_3));
                        showGoToCarWash(reserveObj);
                    } else if (String.valueOf(reserveObj.get("job_type")).equalsIgnoreCase("9") && !AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2")) {
                        //Go to Gas Station
                        showGasStationDialog(reserveObj);
                    }
                }
//                else
                //getJobRequested(data);

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
//    }




    String dateS = "";
    String cus_nameS = "";
    String arrivedS = "";
    String destT = "";
    public void getDataToShown(String date , String cus_name , String arrived , String dest){
        dateS = date;
        cus_nameS = cus_name;
        arrivedS = arrived;
        destT = dest;

    }
    public void getShownData(){
        Global.showAlertDialog(HomeActivity.this, "เวลา: " + dateS + "\n" + "ชื่อลูกค้า: " + cus_nameS + "\n" + arrivedS + "\n" + destT);
    }

    //Dialog Listener
    @Override
    public void viewGasStationAccept(HashMap<String, Object> data) {
//        Toast.makeText(getApplicationContext(),"3.1 : ViewGasStationAccept   "+String.valueOf(data.get("job_id")),Toast.LENGTH_LONG).show();

        closeDialog();
        setOthersMarker(data);
        settingOthersJobStatusBar(data);
        stopService(intent_jobTracking);
        aq.id(R.id.btn_thumbing).visibility(View.GONE);
        //
        showGasPaymentDialog(data);
    }

    @Override
    public void viewGasStationNavigate(HashMap<String, Object> data) {
//        Toast.makeText(getApplicationContext(),"3.2 : Navigate   "+String.valueOf(data.get("job_id")),Toast.LENGTH_LONG).show();
        Status.enableNavigation = true;
        closeDialog();
        setOthersMarker(data);
        settingOthersJobStatusBar(data);
        stopService(intent_jobTracking);
        aq.id(R.id.btn_thumbing).visibility(View.GONE);
        aq.id(R.id.btn_navigation).image(R.drawable.home_navigation_active);

        String[] mylocation = AppSharedPreferences.getInstance(HomeActivity.this).getLocation();
        aq.ajax(direction.getGoogleDirectionAPI(new LatLng(Double.parseDouble(mylocation[0]), Double.parseDouble(mylocation[1])), others_location, GMapV2Direction.MODE_DRIVING), String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {
//                Global.printLog(true, "GoogleAPI", String.valueOf(url));
//                setTaxi2OthersDirection(data);
                if (Status.enableNavigation) {
                    setTaxi2OthersDirection(data);
                }
            }
        });
    }

    @Override
    public void viewHibPointAccept(HashMap<String, Object> data) {

        closeDialog();
        setOthersMarker(data);
        settingOthersJobStatusBar(data);
        //Toast.makeText(getApplicationContext(),"viewhubpoint_Accept",Toast.LENGTH_SHORT).show();
        //stopService(intent_jobTracking);
    }

    @Override
    public void viewHubPointNavigate(HashMap<String, Object> data) {

        closeDialog();
        setOthersMarker(data);
        settingOthersJobStatusBar(data);
        //Toast.makeText(getApplicationContext(),"viewhubpoint_Navigate",Toast.LENGTH_SHORT).show();
        //stopService(intent_jobTracking);

        aq.id(R.id.btn_navigation).image(R.drawable.home_navigation_active);
        String[] mylocation = AppSharedPreferences.getInstance(HomeActivity.this).getLocation();
        aq.ajax(direction.getGoogleDirectionAPI(new LatLng(Double.parseDouble(mylocation[0]), Double.parseDouble(mylocation[1])), others_location, GMapV2Direction.MODE_DRIVING), String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {
                if (Status.enableNavigation) {
                    setTaxi2OthersDirection(data);
                }
            }
        });
    }

    @Override
    public void viewShiftAccept(HashMap<String, Object> data) {

        closeDialog();
        setOthersMarker(data);
        settingOthersJobStatusBar(data);
        stopService(intent_jobTracking);
        aq.id(R.id.btn_thumbing).visibility(View.GONE);
    }

    @Override
    public void viewShiftNavigate(HashMap<String, Object> data) {

        closeDialog();
        setOthersMarker(data);
        settingOthersJobStatusBar(data);
        stopService(intent_jobTracking);

        String[] mylocation = AppSharedPreferences.getInstance(HomeActivity.this).getLocation();
        aq.ajax(direction.getGoogleDirectionAPI(new LatLng(Double.parseDouble(mylocation[0]), Double.parseDouble(mylocation[1])), others_location, GMapV2Direction.MODE_DRIVING), String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {
                setTaxi2OthersDirection(data);
            }
        });
    }

    @Override
    public void viewGoToToyotaAccept(HashMap<String, Object> data) {

        closeDialog();
        setOthersMarker(data);
        settingOthersJobStatusBar(data);
        stopService(intent_jobTracking);
    }

    @Override
    public void viewGoToToyotaNavigate(HashMap<String, Object> data) {

        closeDialog();
        setOthersMarker(data);
        settingOthersJobStatusBar(data);
        stopService(intent_jobTracking);

        String[] mylocation = AppSharedPreferences.getInstance(HomeActivity.this).getLocation();
        aq.ajax(direction.getGoogleDirectionAPI(new LatLng(Double.parseDouble(mylocation[0]), Double.parseDouble(mylocation[1])), others_location, GMapV2Direction.MODE_DRIVING), String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {
                setTaxi2OthersDirection(data);
            }
        });
    }

    @Override
    public void viewCarWashAccept(HashMap<String, Object> data) {

        closeDialog();
        setOthersMarker(data);
        settingOthersJobStatusBar(data);
        stopService(intent_jobTracking);
    }

    @Override
    public void viewCarWashNavigate(HashMap<String, Object> data) {

        closeDialog();
        setOthersMarker(data);
        settingOthersJobStatusBar(data);
        stopService(intent_jobTracking);

        String[] mylocation = AppSharedPreferences.getInstance(HomeActivity.this).getLocation();
        aq.ajax(direction.getGoogleDirectionAPI(new LatLng(Double.parseDouble(mylocation[0]), Double.parseDouble(mylocation[1])), others_location, GMapV2Direction.MODE_DRIVING), String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {
                setTaxi2OthersDirection(data);
            }
        });
    }

    @Override
    public void viewClickAccident() {

        closeDialog();
        reportEmergency("6", "1");
    }

    @Override
    public void viewClickCarBrokeDown() {

        closeDialog();
        reportEmergency("6", "2");
    }

    @Override
    public void viewClickWheel() {

        closeDialog();
        reportEmergency("6", "3");
    }

    @Override
    public void viewClickIncident() {

        closeDialog();
        reportEmergency("6", "4");
    }

    @Override
    public void viewClickIncidentClose() {
//        check_get_job = true;
        //add
        startService(intent_jobTracking);
        closeDialog();
    }

    @Override
    public void viewClickBreakingTime() {

        closeDialog();
        String breaking_api = APIs.getBreakAPI();
        aq.ajax(breaking_api, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                try {
                    setProgressbarVisibility(false);
                    JSONParser json = new JSONParser(data);
                    HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();

                    if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {
                        showBreakingDialog();
                    } else {
                        Global.showAlertDialog(HomeActivity.this, String.valueOf(resultObj.get("message")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void viewNotifyIncidentAccept(HashMap<String, Object> data_item) {
        aq.id(R.id.icon).visible();
        //Toast.makeText(getApplicationContext(),reserved_type,Toast.LENGTH_SHORT).show();
        closeDialog();

        if (others_marker != null) {
            others_marker.remove();
            others_marker = null;
            others_location = null;
        }

        if (taxi2othersJob != null) {
            taxi2othersJob.remove();
            taxi2othersJob = null;
        }
//abcdef


        setProgressbarVisibility(true);
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        AppSharedPreferences.getInstance(HomeActivity.this).setOnJob(true);
        AppSharedPreferences.getInstance(HomeActivity.this).setOnState1(true);
        AppSharedPreferences.getInstance(HomeActivity.this).setJobType(String.valueOf(data_item.get("job_type")));
        AppSharedPreferences.getInstance(HomeActivity.this).setState_1(data_item);
        Global.printLog(true, "data_item", String.valueOf(data_item));

        String url = APIs.getAcceptReservedAPI(String.valueOf(data_item.get("reserved_id")), LoginHelper.getInstance(this).getTaxiID(), LoginHelper.getInstance(this).getOperationID());
        Global.printLog(true, "AcceptReservedAPI", String.valueOf(url));
        aq.ajax(url, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                try {
                    Global.printLog(true, "AcceptReservedAPI data", String.valueOf(data));
                    setProgressbarVisibility(false);

                    JSONParser json = new JSONParser(data);
                    HashMap<String, Object> dataObj = (HashMap<String, Object>) json.convertJson2HashMap();
                    Global.printLog(true, "AcceptReservedAPI dataObj", String.valueOf(dataObj));


                    if (String.valueOf(dataObj.get("code")).equalsIgnoreCase("200")) {

                        HashMap<String, Object> data_item = AppSharedPreferences.getInstance(HomeActivity.this).getState_1();

                        aq.id(R.id.btn_pick_customer).visibility(View.VISIBLE);
                        //aq.id(R.id.home_btn_statusbar_2).clicked(HomeActivity.this);

                        aq.id(R.id.btn_thumbing).visibility(View.GONE);

                        aq.id(R.id.btn_Start_Mter).visibility(View.VISIBLE);
                        aq.id(R.id.btn_Stop_Meter).visibility(View.VISIBLE);

                        aq.id(R.id.bt_jobtype_1).visibility(View.VISIBLE);
                        aq.id(R.id.bt_jobtype_2).visibility(View.GONE);
                        aq.id(R.id.bt_jobtype_3).visibility(View.GONE);
                        aq.id(R.id.bt_jobtype_4).visibility(View.GONE);
                        aq.id(R.id.bt_jobtype_5).visibility(View.GONE);
                        aq.id(R.id.bt_jobtype_6_8).visibility(View.GONE);
                        aq.id(R.id.bt_jobtype_9).visibility(View.GONE);
                        aq.id(R.id.home_btn_sos).image(R.drawable.home_sos);

                        aq.id(R.id.home_topframe2).visibility(View.VISIBLE);
                        aq.id(R.id.home_customer_pix).visibility(View.VISIBLE);
                        aq.id(R.id.btn_Call).visibility(View.VISIBLE);

                        ((TextView) aq.id(R.id.home_place_title).getView()).setCompoundDrawablesWithIntrinsicBounds(R.drawable.home_pin_passenger, 0, 0, 0);
                        aq.id(R.id.home_place_title).text(String.valueOf(data_item.get("location_title")));
                        aq.id(R.id.home_customer_name_img).visibility(View.VISIBLE);
                        aq.id(R.id.home_customer_name).text(getString(R.string.home_title_4) + " " + String.valueOf(data_item.get("cust_name")));

                        if(reserved_type.equals("1"))
                        {
                            //Toast.makeText(getApplicationContext(),"app",Toast.LENGTH_SHORT).show();
                            aq.id(R.id.icon_app).visible();
                            if(enable_credit_card.equals("1")){
//                                aq.id(R.id.icon_visa).visible();
                            }
                        }
                        if(reserved_type.equals("2"))
                        {
                            //Toast.makeText(getApplicationContext(),"TMC",Toast.LENGTH_SHORT).show();
                            aq.id(R.id.icon_reserve).visible();
                        }

                        if (!String.valueOf(data_item.get("cust_pic")).equalsIgnoreCase("") && !String.valueOf(data_item.get("cust_pic")).equalsIgnoreCase("null"))
                            Picasso.with(HomeActivity.this).load(String.valueOf(data_item.get("cust_pic"))).resize(cust_pix.getWidth(), cust_pix.getHeight()).placeholder(R.drawable.home_pic_passenger).error(R.drawable.home_pic_passenger).into((ImageView) aq.id(R.id.home_customer_pix).getView());
                        else
                            Picasso.with(HomeActivity.this).load(R.drawable.home_pic_passenger).resize(cust_pix.getWidth(), cust_pix.getHeight()).placeholder(R.drawable.home_pic_passenger).error(R.drawable.home_pic_passenger).into((ImageView) aq.id(R.id.home_customer_pix).getView());


                        HashMap<String, Object> data_temp = (HashMap<String, Object>) dataObj.get("data");
                        AppSharedPreferences.getInstance(HomeActivity.this).setJobID(String.valueOf(data_temp.get("job_id")));
                        stopService(intent_jobTracking);
                        startService(intent_taxi_status);
                        setCustomerMarker();

                    } else {

                        AppSharedPreferences.getInstance(HomeActivity.this).removeOnJob();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeOnState1();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeJobType();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeState_1();
                        Global.showAlertDialog(HomeActivity.this, String.valueOf(dataObj.get("message")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void viewOutOfServiceAccept(HashMap<String, Object> data) {

        closeDialog();
        setOthersMarker(data);
        settingOthersJobStatusBar(data);
        stopService(intent_jobTracking);
    }

    @Override
    public void viewTripReportSendTripReport(String url_callback, final String cash , final String check) {
        //viewtrip = null;
        aq.id(R.id.icon).visibility(View.GONE);
//        check_get_job = false;
        //add
        stopService(intent_jobTracking);
        setProgressbarVisibility(true);
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        //abcdef
//        Toast.makeText(getApplicationContext(),"Viewtrip : "+url_callback,Toast.LENGTH_LONG).show();
        aq.ajax(url_callback, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {
//                Toast.makeText(getApplicationContext(),"data : "+data,Toast.LENGTH_LONG).show();
                try {
                    setProgressbarVisibility(false);

                    JSONParser json = new JSONParser(data);
                    HashMap<String, Object> dataObj = (HashMap<String, Object>) json.convertJson2HashMap();

                    Global.printLog(true, "viewTripReportSendTripReport", String.valueOf(dataObj));
                    if (String.valueOf(dataObj.get("code")).equalsIgnoreCase("200")) {
                        Status.check = false;

                        closeDialog();

                        AppSharedPreferences.getInstance(HomeActivity.this).removeOnJob();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeState_1();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeOnState1();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeOnState2();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeOnState3();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeState_Payment();
                        AppSharedPreferences.getInstance(HomeActivity.this).removePickCustomer();

                        resetProgressState();

                        if (customer_maker != null)
                            customer_maker.remove();

                        if (destination_maker != null)
                            destination_maker.remove();

                        customer_location = null;
                        destination_location = null;

                        if (customer2destination != null)
                            customer2destination.remove();

                        customer2destination = null;

                        if (taxi2customer != null)
                            taxi2customer.remove();

                        taxi2customer = null;

                        aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_001));

                        stopService(intent_taxi_status);
                        startService(intent_jobTracking);

                        alertfinalCash(cash, check);

                    } else {

                        Global.showAlertDialog(HomeActivity.this, String.valueOf(dataObj.get("message")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void viewGasPaymentAccept(HashMap<String, Object> data) {
        closeDialog();
//        endJob(String.valueOf(data.get("job_id")), enable_credit_card, reserved_type);
//        Toast.makeText(getApplicationContext() , "6 : ViewgasPayment   "+String.valueOf(data.get("job_id")),Toast.LENGTH_LONG).show();


        //String url = APIs.getTripCostAPI(String.valueOf(data.get("job_id")), "", "", String.valueOf(data.get("job_type")), "", "", String.valueOf(data.get("price")), String.valueOf(data.get("total_gas")), "");
        //Oak
        String url = APIs.getTripCostAPI(String.valueOf(data.get("job_id")), "", "", String.valueOf(data.get("job_type")), "", "", String.valueOf(data.get("total_cost")), String.valueOf(data.get("fare")), "", String.valueOf(data.get("km")));

        setProgressbarVisibility(true);
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        //abcdef

        aq.ajax(url, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                try {
                    setProgressbarVisibility(false);
                    JSONParser json = new JSONParser(data);
                    HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();

                    if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {


//                        aq.id(R.id.home_btn_statusbar_1).clicked(HomeActivity.this);
//                        aq.id(R.id.home_btn_statusbar_2).clicked(null);
//                        aq.id(R.id.home_btn_statusbar_3).clicked(null);
                        aq.id(R.id.btn_pick_customer).visibility(View.GONE);

//                        aq.id(R.id.home_btn_statusbar_1).textColorId(R.color.black);
//                        aq.id(R.id.home_btn_statusbar_2).textColorId(R.color.black);
//                        aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.black);

//                        aq.id(R.id.home_btn_statusbar_1).background(R.drawable.home_btn_statusbar);
//                        aq.id(R.id.home_btn_statusbar_2).background(R.drawable.home_btn_statusbar);
//                        aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_btn_statusbar);

                        AppSharedPreferences.getInstance(HomeActivity.this).removeJobID();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeJobType();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeState_Hub();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeOnGasStaion();
                        aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_001));
                        resetProgressState();
                        startService(intent_jobTracking);

                        if (others_marker != null) {
                            others_marker.remove();
                            others_marker = null;
                            others_location = null;
                        }

                        if (taxi2othersJob != null) {
                            taxi2othersJob.remove();
                            taxi2othersJob = null;
                        }

                    } else {

//                        aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_btn_statusbar);
//                        aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.black);
                        Global.showAlertDialog(HomeActivity.this, String.valueOf(resultObj.get("message")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    //End of Dialog Listener

    @Override
    public void viewBreakingAccept() {
//        check_get_job = true;
        startService(intent_jobTracking);
        playAudio(R.raw.sound_standby);
        standByMode();
    }

    //API Function
    private void thumbingCustomer(String taxi_id, String driver_id, String operation_id) {

        aq.id(R.id.log_out).visibility(View.GONE);
        String url = APIs.getThumbingAPI(taxi_id, driver_id, operation_id);
//        Toast.makeText(getApplicationContext(),"URL : "+url,Toast.LENGTH_LONG).show();
        setProgressbarVisibility(true);
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        //abcdef

        if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2"))
            //Oak
            //Toast.makeText(getApplicationContext(),"Endjob12",Toast.LENGTH_SHORT).show();
            endJobWithoutResult(AppSharedPreferences.getInstance(this).getJobID());

        Global.printLog(true, "thumbing", url);
        aq.ajax(url, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                try {
//                    Toast.makeText(getApplicationContext(),"Return : "+data,Toast.LENGTH_LONG).show();
                    setProgressbarVisibility(false);
                    JSONParser json = new JSONParser(data);
                    HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();

                    Global.printLog(true, "thumbingCustomer result", String.valueOf(resultObj));
                    if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {

                        HashMap<String, Object> reserveObj = (HashMap<String, Object>) resultObj.get("data");
                        AppSharedPreferences.getInstance(HomeActivity.this).setOnJob(true);
                        AppSharedPreferences.getInstance(HomeActivity.this).setOnState1(true);
                        AppSharedPreferences.getInstance(HomeActivity.this).setJobType("1");
                        AppSharedPreferences.getInstance(HomeActivity.this).setJobID(String.valueOf(reserveObj.get("job_id")));
                        AppSharedPreferences.getInstance(HomeActivity.this).setPickCustomer(true);

//                        aq.id(R.id.home_btn_statusbar_2).background(R.drawable.home_status2_active);
//                        aq.id(R.id.home_btn_statusbar_2).textColorId(R.color.white);

                        aq.id(R.id.btn_thumbing).visibility(View.GONE);

                        aq.id(R.id.bt_jobtype_1).visibility(View.GONE);
                        aq.id(R.id.home_taxi_status).image(R.drawable.status_reserved);
                        //sound
                        playAudio(R.raw.sound_reserve);


                        aq.id(R.id.btn_Start_Mter).visibility(View.VISIBLE);
                        aq.id(R.id.btn_Stop_Meter).visibility(View.GONE);

                        startService(intent_taxi_status);

                    } else {

                        aq.id(R.id.btn_thumbing).visibility(View.VISIBLE);
//                        aq.id(R.id.home_btn_statusbar_1).clicked(HomeActivity.this);
//                        aq.id(R.id.home_btn_statusbar_1).background(R.drawable.home_btn_statusbar);
//                        aq.id(R.id.home_btn_statusbar_1).textColorId(R.color.black);
                        aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_001));


                        //aq.id(R.id.btn_Start_Mter).visibility(View.GONE);
                        //aq.id(R.id.btn_Stop_Meter).visibility(View.GONE);

                        aq.id(R.id.home_taxi_status).image(R.drawable.status_stand_by);
                        playAudio(R.raw.sound_standby);
                        //sound
                        aq.id(R.id.btn_Start_Mter).visibility(View.GONE);
                        aq.id(R.id.btn_Stop_Meter).visibility(View.GONE);
                        AppSharedPreferences.getInstance(HomeActivity.this).removeThumbingMode();
                        Global.showAlertDialog(HomeActivity.this, String.valueOf(resultObj.get("message")));

                        startService(intent_jobTracking);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void reportEmergency(String service_status, String service_type) {

        String emergency_api = APIs.getReportEmergency(service_status, service_type);
        setProgressbarVisibility(true);
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        //abcdef

        aq.ajax(emergency_api, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                try {
                    setProgressbarVisibility(false);
                    JSONParser json = new JSONParser(data);
                    HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();
                    closeDialog();
                    alertIncident();
                    //Global.showAlertDialog(HomeActivity.this, String.valueOf(resultObj.get("message")));
                    ((ImageView) aq.id(R.id.home_btn_hubpoint).getView()).setAlpha(1f);
                    aq.id(R.id.home_btn_hubpoint).clicked(HomeActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void callSOS() {


        //abcdef


        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.dialog_confirm, null);

        TextView dialog_msg = (TextView) v.findViewById(R.id.confirm_msg);
        dialog_msg.setText("ยืนยันการแจ้งขอความช่วยเหลือ ไปยังเจ้าหน้าที่ตำรวจ");
        dialog_msg.setGravity(Gravity.CENTER);

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
                String sos_api = APIs.getSOSAPI();
                setProgressbarVisibility(true);
                timerCount = new MyCount(10000, 1000);
                timerCount.start();
                aq.ajax(sos_api, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String data, AjaxStatus status) {

                        try {
                            setProgressbarVisibility(false);
                            JSONParser json = new JSONParser(data);
                            HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();
//                            Toast.makeText(getApplicationContext(),String.valueOf(resultObj.get("message")),Toast.LENGTH_LONG).show();
//                    Global.showAlertDialog(HomeActivity.this, String.valueOf(resultObj.get("message")));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                closeDialog();

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

    private void arrivedPickLocation(String job_id) {
        //Toast.makeText(getApplicationContext(),"arrivedPickLocation",Toast.LENGTH_SHORT).show();
        Status.checkStatus = 1;
        String startJob = APIs.arrivedPickLocation(job_id);
        setProgressbarVisibility(true);
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        //abcdef

        aq.ajax(startJob, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                try {
                    setProgressbarVisibility(false);
                    JSONParser json = new JSONParser(data);
                    HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();

                    if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {

                        //Start Meter
                        //startService(intent_taxi_status);
                        AppSharedPreferences.getInstance(HomeActivity.this).setPickCustomer(true);

//                        aq.id(R.id.home_btn_statusbar_2).background(R.drawable.home_status2_active);
//                        aq.id(R.id.home_btn_statusbar_2).textColorId(R.color.white);
//                        aq.id(R.id.home_btn_statusbar_2).clicked(HomeActivity.this);
                        aq.id(R.id.btn_pick_customer).visibility(View.GONE);
                        //Toast.makeText(getApplicationContext(), "pickup_gone", Toast.LENGTH_SHORT).show();

                        aq.id(R.id.home_customer_name_img).visibility(View.GONE);
                        aq.id(R.id.bt_jobtype_1).visibility(View.GONE);

                        HashMap<String, Object> data_item = AppSharedPreferences.getInstance(HomeActivity.this).getState_1();
                        ((TextView) aq.id(R.id.home_place_title).getView()).setCompoundDrawablesWithIntrinsicBounds(R.drawable.home_pin_destination, 0, 0, 0);
                        aq.id(R.id.home_place_title).text(String.valueOf(data_item.get("destination_title")));
                        aq.id(R.id.home_customer_name_img).visibility(View.GONE);

                    } else
                        Global.showAlertDialog(HomeActivity.this, String.valueOf(resultObj.get("message")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startJob(String job_id, String job_type) {

        //Oak Edit
        // String manMeter = APIs.ManMeterstart();
        //Toast.makeText(getApplicationContext(),manMeter,Toast.LENGTH_LONG).show();
        //aq.ajax(manMeter, String.class, new AjaxCallback<String>(){});
        //
        String startJob = APIs.startJob(job_id, job_type);
//        Toast.makeText(getApplicationContext(),"Startjob : "+startJob,Toast.LENGTH_LONG).show();
        setProgressbarVisibility(true);
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        //abcdef


        aq.ajax(startJob, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                try {
//                    Toast.makeText(getApplicationContext(),"Return : "+data,Toast.LENGTH_LONG).show();
                    setProgressbarVisibility(false);
                    JSONParser json = new JSONParser(data);
                    HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();

                    if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {
                        //Toast.makeText(getApplicationContext(),resultObj.toString(),Toast.LENGTH_LONG).show();
                        if (AppSharedPreferences.getInstance(HomeActivity.this).getJobType().equalsIgnoreCase("1")) {

                            msisdn = "";
                            aq.id(R.id.home_btn_sos).image(R.drawable.home_sos);
                            AppSharedPreferences.getInstance(HomeActivity.this).setOnState2(true);

                            aq.id(R.id.home_taxi_status).image(R.drawable.status_on_job);
                            //sound
                            playAudio(R.raw.sound_onjob);
                            aq.id(R.id.btn_Start_Mter).visibility(View.GONE);
                            aq.id(R.id.btn_Stop_Meter).visibility(View.VISIBLE);
                            setTakeCustomerToDestination();
                        }

                    } else
                        Global.showAlertDialog(HomeActivity.this, String.valueOf(resultObj.get("message")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void endJob(final String job_id , String enable_credit_card , String reserved_type) {
//        check_get_job = true;
        Status.checkStatus = 2;
        aq.id(R.id.log_out).visible();
        mapview.clear();


        String endJob_api = APIs.getEndJobAPI(job_id ,enable_credit_card , reserved_type);
//         Toast.makeText(getApplicationContext(),endJob_api,Toast.LENGTH_LONG).show();

        setProgressbarVisibility(true);
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        //abcdef

        aq.ajax(endJob_api, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                try {
//                    Toast.makeText(getApplicationContext(),"Return : "+data,Toast.LENGTH_LONG).show();
                    setProgressbarVisibility(false);
                    JSONParser json = new JSONParser(data);
                    HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();


                    if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {
                        aq.id(R.id.icon_thum).visibility(View.GONE);
                        aq.id(R.id.icon_reserve).visibility(View.GONE);
                        aq.id(R.id.icon_app).visibility(View.GONE);
//                        aq.id(R.id.icon_visa).visibility(View.GONE);

                        if (!AppSharedPreferences.getInstance(HomeActivity.this).getJobType().equalsIgnoreCase("1")) {

                            if (!AppSharedPreferences.getInstance(HomeActivity.this).getJobType().equalsIgnoreCase("9")) {

                                aq.id(R.id.btn_pick_customer).visibility(View.GONE);
                                aq.id(R.id.bt_jobtype_2).visibility(View.GONE);
                                aq.id(R.id.bt_jobtype_3).visibility(View.GONE);
                                aq.id(R.id.bt_jobtype_4).visibility(View.GONE);
                                aq.id(R.id.bt_jobtype_5).visibility(View.GONE);
                                aq.id(R.id.bt_jobtype_6_8).visibility(View.GONE);
                                aq.id(R.id.bt_jobtype_9).visibility(View.GONE);

//                                aq.id(R.id.home_btn_statusbar_1).textColorId(R.color.black);
//                                aq.id(R.id.home_btn_statusbar_2).textColorId(R.color.black);
//                                aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.black);
//
//                                aq.id(R.id.home_btn_statusbar_1).background(R.drawable.home_btn_statusbar);
//                                aq.id(R.id.home_btn_statusbar_2).background(R.drawable.home_btn_statusbar);
//                                aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_btn_statusbar);

                                resetProgressState();
                                //endTripWithoutResult(AppSharedPreferences.getInstance(HomeActivity.this).getJobID(), AppSharedPreferences.getInstance(HomeActivity.this).getJobType());

                                AppSharedPreferences.getInstance(HomeActivity.this).removeJobID();
                                AppSharedPreferences.getInstance(HomeActivity.this).removeJobType();
                                AppSharedPreferences.getInstance(HomeActivity.this).removeState_Hub();
                                aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_001));
                                startService(intent_jobTracking);

                                if (others_marker != null) {
                                    others_marker.remove();
                                    others_marker = null;
                                    others_location = null;
                                }

                                if (taxi2othersJob != null) {
                                    taxi2othersJob.remove();
                                    taxi2othersJob = null;
                                }
                            } else {

//                                aq.id(R.id.home_btn_statusbar_4).clicked(HomeActivity.this);
//                                aq.id(R.id.home_btn_statusbar_3).clicked(null);
                                aq.id(R.id.btn_Stop_Meter).visibility(View.GONE);
//                                aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.white);
//                                aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_status2_active);

                                HashMap<String, Object> dataObj = new HashMap<String, Object>();
                                dataObj.put("job_id", AppSharedPreferences.getInstance(HomeActivity.this).getJobID());
                                dataObj.put("job_type", AppSharedPreferences.getInstance(HomeActivity.this).getJobType());
                                AppSharedPreferences.getInstance(HomeActivity.this).setOnGasStaion(true);
                                showGasPaymentDialog(dataObj);
                            }

                        } else {
                            //Job Type = 1
                            AppSharedPreferences.getInstance(HomeActivity.this).setOnState3(true);
//                            aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.white);
//                            aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_status2_active);
//                            aq.id(R.id.home_btn_statusbar_4).background(R.drawable.home_status4_active);
//                            aq.id(R.id.home_btn_statusbar_4).textColorId(R.color.white);
//                            aq.id(R.id.home_btn_statusbar_4).clicked(null);
                            aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_004));
                            aq.id(R.id.btn_Stop_Meter).visibility(View.GONE);

                            //Toast.makeText(getApplicationContext(),"call hashmap data",Toast.LENGTH_LONG).show();
                            HashMap<String, Object> dataObj = (HashMap<String, Object>) resultObj.get("data");
                            //oak
                            //Toast.makeText(getApplicationContext(),dataObj.toString(),Toast.LENGTH_LONG).show();
                            //
                            AppSharedPreferences.getInstance(HomeActivity.this).setState_Payment(dataObj);
                            showTripReportDialog(dataObj);
                        }

                        stopService(intent_taxi_status);

                    } else {

//                        aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_btn_statusbar);
//                        aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.black);
                        Global.showAlertDialog(HomeActivity.this, String.valueOf(resultObj.get("message")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void endJobWithoutResult(String job_id) {
        Status.checkclosejob = false;
        String endJob_api = APIs.getEndJobAPI(job_id, "0", "0");
        setProgressbarVisibility(true);
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        //abcdef
        try{
            aq.ajax(endJob_api, String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String data, AjaxStatus status) {

                    JSONParser json = null;
                    try {
                        json = new JSONParser(data);
                        HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();
                        if(String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")){
                            setProgressbarVisibility(false);
                        }
                        else{
                            Status.checkclosejob = true;
                        }

                    } catch (JSONException e) {
                        Status.checkclosejob = true;
                    }
                }
            });
        }
        catch (Exception e){
            Status.checkclosejob = true;
        }

    }

    private void endTripWithoutResult(String job_id, String job_type) {
        Status.checkclosejob = false;
        String endJob_api = APIs.getTripCostAPI(job_id, "", "", job_type, "", "", "", "", "", "");
        setProgressbarVisibility(true);
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        //abcdef

        try{
            aq.ajax(endJob_api, String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String data, AjaxStatus status) {

                    JSONParser json = null;
                    try {
                        json = new JSONParser(data);
                        HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();
                        if(String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")){
                            setProgressbarVisibility(false);
                        }
                        else{
                            Status.checkclosejob = true;
                        }

                    } catch (JSONException e) {
                        Status.checkclosejob = true;
                    }
                }
            });
        }
        catch (Exception e){
            Status.checkclosejob = true;
        }
    }
    //End Of API Function

    private void standByMode() {

        String standby_api = APIs.getStandbyAPI();
        setProgressbarVisibility(true);
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        if (!AppSharedPreferences.getInstance(this).getJobID().equalsIgnoreCase("")) {
            endJobWithoutResult(AppSharedPreferences.getInstance(this).getJobID());
            endTripWithoutResult(AppSharedPreferences.getInstance(this).getJobID(), AppSharedPreferences.getInstance(this).getJobType());
            stopService(intent_taxi_status);
            startService(intent_jobTracking);
        }

        aq.ajax(standby_api, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                if (dialog != null)
                    closeDialog();

                setProgressbarVisibility(false);
            }
        });

    }

    //Dialog
    private void showBreakingDialog() {
//        check_get_job = false;
        stopService(intent_jobTracking);
        playAudio(R.raw.sound_outofservice);
        closeDialog();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if (AppSharedPreferences.getInstance(this).getOnState1()) {
            dialog.setContentView(new ViewBreaking(this, this));
        } else if (AppSharedPreferences.getInstance(this).getOnState3()) {
            dialog.setContentView(new ViewBreaking(this, this));
        } else {
            dialog.setContentView(new ViewBreaking(this, this));
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void showNotificationDialog(HashMap<String, Object> data, boolean play_sound) {
        Status.checkStatus = 0;
        //Toast.makeText(getApplicationContext(),"shownotificationDialog",Toast.LENGTH_LONG).show();
        aq.id(R.id.btn_thumbing).visibility(View.GONE);
        aq.id(R.id.log_out).visibility(View.GONE);
        playAudio(R.raw.sound_app);
        closeDialog();
        check_thum_re = false;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(new ViewNotification(this, data, play_sound, this, reserved_type,enable_credit_card));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                takeScreenshot();
//            }
//        }, 1000);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

//    private void takeScreenshot() {
//        Date now = new Date();
//        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
//        try {
//
//            // image naming and path  to include sd card  appending name you choose for file
//            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
//
//            // create bitmap screen capture
//
//
//            View v1 =getWindow().getDecorView().getRootView();
////            View v1 = findViewById(R.id.ScreenNotification).getRootView();
//
//            v1.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//            v1.setDrawingCacheEnabled(false);
//            bitmap = Bitmap.createScaledBitmap(bitmap,640,400,false);
//
//            File imageFile = new File(mPath);
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            int quality = 20;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//            outputStream.flush();
//            outputStream.close();
//
////            ByteArrayOutputStream stream = new ByteArrayOutputStream();
////            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
////            byte[] byteArray = stream.toByteArray();
////
//
//
////            openScreenshot(imageFile);
//        } catch (Throwable e) {
//            // Several error may come out with file handling or OOM
//            e.printStackTrace();
//        }
//    }
//
//
//    private void openScreenshot(File imageFile) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        Uri uri = Uri.fromFile(imageFile);
//        intent.setDataAndType(uri, "image/*");
//        startActivity(intent);
//    }



    private ViewTripReport viewtrip;

    private void showTripReportDialog(HashMap<String, Object> data) {
        check_thum_re = true;
        playAudio(R.raw.sound_endtrip);
        // if(enableAuto) {


        closeDialog();

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        viewtrip = new ViewTripReport(this, data, this);
        dialog.setContentView(new ViewTripReport(this, data, this));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        startTimerMeter();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        // aq.id(R.id.home_customer_pix).clear();
        // }
    }

    private void showManualTripReportDialog(HashMap<String, Object> data) {

        closeDialog();

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(new ViewTripReport(this, data, this));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }

    private void showHubPointDialog(HashMap<String, Object> data) {
        playAudio(R.raw.sound_hub);
        closeDialog();

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(new ViewHubPoint(this, data, this));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void showShiftDialog(HashMap<String, Object> data) {

        closeDialog();

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(new ViewShift(this, data, this));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void showOutOfServiceDialog(HashMap<String, Object> data) {

        closeDialog();

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(new ViewOutOfService(this, data, true, this));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void showGoToToyotaDialog(HashMap<String, Object> data) {

        closeDialog();

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(new ViewGoToToyota(this, data, this));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void showGoToCarWash(HashMap<String, Object> data) {

        closeDialog();

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(new ViewCarWash(this, data, this));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void showGasStationDialog(HashMap<String, Object> data) {
//        Toast.makeText(getApplicationContext(),"2 : showGasStationDialog   "+String.valueOf(data.get("job_id")),Toast.LENGTH_LONG).show();
        closeDialog();

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(new ViewGasStation(this, data, this));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void showGasPaymentDialog(HashMap<String, Object> data) {

//        Toast.makeText(getApplicationContext(),"4 : showgaspaymentdialog   "+String.valueOf(data.get("job_id")),Toast.LENGTH_LONG).show();
        closeDialog();

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //Oak Test Gaspayment
        //Toast.makeText(getApplicationContext(),AppSharedPreferences.getInstance(HomeActivity.this).getJobID().toString(),Toast.LENGTH_LONG).show();
        //home.this to this
        dialog.setContentView(new ViewGasPayment(this, data, this, String.valueOf(data.get("job_id")), String.valueOf(data.get("job_type"))));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void showIncidentDialog() {

        closeDialog();
//        check_get_job = false;

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //Oak
        if (AppSharedPreferences.getInstance(this).getOnState1() || AppSharedPreferences.getInstance(this).getOnState3()) {
            dialog.setContentView(new ViewIncidentBreak(this, this));
        } else {
            dialog.setContentView(new ViewIncident(this, this));
        }
        //
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void showLimitSpeedDialog() {
        playAudio(R.raw.sound_speed);
        if (dialog_speed != null)
            dialog_speed.dismiss();

        dialog_speed = null;

        dialog_speed = new Dialog(this);
        dialog_speed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_speed.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_speed.setContentView(new ViewLimitSpeed(this));
        dialog_speed.setCanceledOnTouchOutside(false);
        dialog_speed.setCancelable(false);
        dialog_speed.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog_speed.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }
    //End of Dialog

    private void showNotificationOutOfServiceDialog() {

        if (dialog_outofservice != null)
            dialog_outofservice.dismiss();

        dialog_outofservice = null;

        dialog_outofservice = new Dialog(this);
        dialog_outofservice.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_outofservice.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_outofservice.setContentView(new ViewOutOfService(this, null, false, this));
        dialog_outofservice.setCanceledOnTouchOutside(false);
        dialog_outofservice.setCancelable(false);
        dialog_outofservice.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog_outofservice.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    //Set Map
    private void settingOthersJobStatusBar(HashMap<String, Object> data) {

        AppSharedPreferences.getInstance(this).setJobType(String.valueOf(data.get("job_type")));
        AppSharedPreferences.getInstance(this).setJobID(String.valueOf(data.get("job_id")));
        AppSharedPreferences.getInstance(this).setState_Hub(data);
        //Toast.makeText(getApplicationContext(),"settingOthersJobStatusBar   "+String.valueOf(data.get("job_id"))+"   "+String.valueOf(data.get("job_type")),Toast.LENGTH_SHORT).show();
//        startJob(AppSharedPreferences.getInstance(this).getJobID(), AppSharedPreferences.getInstance(this).getJobType());

        startService(intent_taxi_status);


        if (!AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2")) {

//            aq.id(R.id.home_btn_statusbar_1).background(R.drawable.home_status2_active);
//            aq.id(R.id.home_btn_statusbar_1).textColorId(R.color.white);
//            aq.id(R.id.home_btn_statusbar_2).background(R.drawable.home_status2_active);
//            aq.id(R.id.home_btn_statusbar_2).textColorId(R.color.white);
//            aq.id(R.id.home_btn_statusbar_1).clicked(null);
            //aq.id(R.id.home_btn_statusbar_3).clicked(this);
            aq.id(R.id.btn_pick_customer).visibility(View.GONE);
        } else {

            aq.id(R.id.btn_pick_customer).visibility(View.GONE);
//            aq.id(R.id.home_btn_statusbar_2).background(R.drawable.home_status2_active);
//            aq.id(R.id.home_btn_statusbar_2).textColorId(R.color.white);
            //aq.id(R.id.home_btn_statusbar_3).clicked(this);
        }

        if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("2")) {

            aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.job_type_2));
            aq.id(R.id.btn_thumbing).visibility(View.VISIBLE);

            aq.id(R.id.btn_Start_Mter).visibility(View.GONE);
            aq.id(R.id.btn_Stop_Meter).visibility(View.GONE);

            aq.id(R.id.bt_jobtype_1).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_2).visibility(View.VISIBLE);
            aq.id(R.id.bt_jobtype_3).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_4).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_5).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_6_8).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_9).visibility(View.GONE);

        } else if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("3")) {

            aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.job_type_3));
            aq.id(R.id.home_taxi_status).image(R.drawable.status_out_of_service);
            //sound
            playAudio(R.raw.sound_outofservice);
            aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.job_type_2));

            aq.id(R.id.btn_thumbing).visibility(View.GONE);
            aq.id(R.id.btn_Start_Mter).visibility(View.VISIBLE);

            aq.id(R.id.bt_jobtype_1).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_2).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_3).visibility(View.VISIBLE);
            aq.id(R.id.bt_jobtype_4).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_5).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_6_8).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_9).visibility(View.GONE);

        } else if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("4")) {

            aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.job_type_4));
            aq.id(R.id.home_taxi_status).image(R.drawable.status_out_of_service);
            //sound
            playAudio(R.raw.sound_outofservice);
            aq.id(R.id.btn_thumbing).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_1).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_2).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_3).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_4).visibility(View.VISIBLE);
            aq.id(R.id.bt_jobtype_5).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_6_8).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_9).visibility(View.GONE);

        } else if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("5")) {

            aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.job_type_5));
            aq.id(R.id.home_taxi_status).image(R.drawable.status_out_of_service);
            //sound
            playAudio(R.raw.sound_outofservice);
            aq.id(R.id.btn_thumbing).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_1).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_2).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_3).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_4).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_5).visibility(View.VISIBLE);
            aq.id(R.id.bt_jobtype_6_8).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_9).visibility(View.GONE);

        } else if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("6")) {

            aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.job_type_6));
            aq.id(R.id.home_taxi_status).image(R.drawable.status_out_of_service);
            //sound
            playAudio(R.raw.sound_outofservice);
            aq.id(R.id.btn_thumbing).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_1).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_2).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_3).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_4).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_5).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_6_8).visibility(View.VISIBLE);
            aq.id(R.id.bt_jobtype_9).visibility(View.GONE);

        } else if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("7")) {

            aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.job_type_7));
            aq.id(R.id.home_taxi_status).image(R.drawable.status_out_of_service);
            //sound
            playAudio(R.raw.sound_outofservice);
            aq.id(R.id.btn_thumbing).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_1).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_2).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_3).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_4).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_5).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_6_8).visibility(View.VISIBLE);
            aq.id(R.id.bt_jobtype_9).visibility(View.GONE);

        } else if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("8")) {

            aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.job_type_8));
            aq.id(R.id.home_taxi_status).image(R.drawable.status_out_of_service);
            //sound
            playAudio(R.raw.sound_outofservice);
            aq.id(R.id.btn_thumbing).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_1).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_2).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_3).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_4).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_5).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_6_8).visibility(View.VISIBLE);
            aq.id(R.id.bt_jobtype_9).visibility(View.GONE);

        } else if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("9")) {

            aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.job_type_9));
            aq.id(R.id.home_taxi_status).image(R.drawable.status_out_of_service);
            //sound
            playAudio(R.raw.sound_outofservice);
            aq.id(R.id.btn_thumbing).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_1).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_2).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_3).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_4).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_5).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_6_8).visibility(View.GONE);
            aq.id(R.id.bt_jobtype_9).visibility(View.VISIBLE);
        }
    }

    private void setOthersMarker(HashMap<String, Object> data) {

        others_location = new LatLng(Double.parseDouble(String.valueOf(data.get("hub_lat"))), Double.parseDouble(String.valueOf(data.get("hub_lng"))));
        others_marker = mapview.addMarker(new MarkerOptions().position(others_location).title(String.valueOf(data.get("hub_name"))).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin_hub)));
    }

    private void setCustomerMarker() {

        HashMap<String, Object> data = (HashMap<String, Object>) AppSharedPreferences.getInstance(HomeActivity.this).getState_1();
        Global.printLog(true, "setCustomerMarker data", String.valueOf(data));

        try {
            customer_location = new LatLng(Double.parseDouble(String.valueOf(data.get("pick_lat"))), Double.parseDouble(String.valueOf(data.get("pick_lng"))));
            customer_maker = mapview.addMarker(new MarkerOptions().position(customer_location).title(String.valueOf(data.get("location_title"))).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin_passenger)));

        } catch (Exception e) {

        }

        try {
            destination_location = new LatLng(Double.parseDouble(String.valueOf(data.get("dest_lat"))), Double.parseDouble(String.valueOf(data.get("dest_lng"))));
            destination_maker = mapview.addMarker(new MarkerOptions().position(destination_location).title(String.valueOf(data.get("destination_title"))).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin_destination)));
        } catch (Exception e) {

        }

//        aq.id(R.id.home_btn_statusbar_1).clicked(null);
//        aq.id(R.id.home_btn_statusbar_1).background(R.drawable.home_status2_active);
//        aq.id(R.id.home_btn_statusbar_1).textColorId(R.color.white);
        aq.id(R.id.btn_pick_customer).visibility(View.VISIBLE);
        aq.id(R.id.btn_thumbing).visibility(View.GONE);
        aq.id(R.id.bt_jobtype_1).visibility(View.VISIBLE);
        aq.id(R.id.home_taxi_status).image(R.drawable.status_reserved);
        //sound
        playAudio(R.raw.sound_reserve);
        aq.id(R.id.btn_Start_Mter).visibility(View.VISIBLE);
        aq.id(R.id.btn_Stop_Meter).visibility(View.GONE);

        aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_002));

        String[] mylocation = AppSharedPreferences.getInstance(HomeActivity.this).getLocation();
        aq.ajax(direction.getGoogleDirectionAPI(new LatLng(Double.parseDouble(mylocation[0]), Double.parseDouble(mylocation[1])), customer_location, GMapV2Direction.MODE_DRIVING), String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                setTaxi2customerDirection(data);
            }
        });
    }
    //End of Set Map

    private void setTakeCustomerToDestination() {

//        aq.id(R.id.home_btn_statusbar_2).background(R.drawable.home_status2_active);
//        aq.id(R.id.home_btn_statusbar_2).textColorId(R.color.white);
//        aq.id(R.id.home_btn_statusbar_2).clicked(null);
        aq.id(R.id.bt_jobtype_1).visibility(View.GONE);
        aq.id(R.id.btn_pick_customer).visibility(View.GONE);

        aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_003));

        if (!AppSharedPreferences.getInstance(this).getThumbingMode()) {

            aq.ajax(direction.getGoogleDirectionAPI(customer_location, destination_location, GMapV2Direction.MODE_DRIVING), String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String data, AjaxStatus status) {
                    closeNavigation();
                    setCustomer2DestinationDirection(data);
                }
            });
        }
    }

    //Set Direction
    private void setTaxi2OthersDirection(String data) {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(data)));
            ArrayList<LatLng> directionPoint = direction.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(15).color(Color.parseColor("#FF00a8ff"));
            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            taxi2othersJob = mapview.addPolyline(rectLine);

            //Add by Beat
            aq.id(R.id.home_topframe_navigation).visibility(View.VISIBLE);
            aq.id(R.id.home_btn_sos2).clicked(this);
            registeerSensor();
            Status.enableNavigation = true;
            googleDirectionStepArrayList = direction.getDirectionStep(doc);
            listGeoPoint = direction.getDirection(doc);
            current_zoom = 16.0f;

            updateUILocation(myLocation.latitude + "", myLocation.longitude + "", pantoLocation);

            outOfTheWaySchedule();

            //Add marker
            /*for(int i = 0; i < googleDirectionStepArrayList.size(); i++) {
                mapview.addMarker(new MarkerOptions().position(googleDirectionStepArrayList.get(i).getEndLocation())
                        .flat(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }*/

            String duration = direction.getDurationText(doc);
            String distance = direction.getDistanceText(doc);
            Global.printLog(true, "duration", String.valueOf(duration));
            Global.printLog(true, "distance", String.valueOf(distance));

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void setCustomer2DestinationDirection(String data) {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc2 = builder.parse(new InputSource(new StringReader(data)));
            ArrayList<LatLng> directionPoint2 = direction.getDirection(doc2);
            PolylineOptions rectLine2 = new PolylineOptions().width(10).color(Color.parseColor("#FF00a8ff"));
            for (int i = 0; i < directionPoint2.size(); i++) {
                rectLine2.add(directionPoint2.get(i));
            }
            customer2destination = mapview.addPolyline(rectLine2);

            //Add by Beat
            aq.id(R.id.home_topframe_navigation).visibility(View.VISIBLE);
            aq.id(R.id.home_btn_sos2).clicked(this);
            registeerSensor();
            Status.enableNavigation = true;
            googleDirectionStepArrayList = direction.getDirectionStep(doc2);
            listGeoPoint = direction.getDirection(doc2);
            current_zoom = 16.0f;

            updateUILocation(myLocation.latitude + "", myLocation.longitude + "", pantoLocation);

            aq.id(R.id.btn_navigation).image(R.drawable.home_navigation_active);

            outOfTheWaySchedule();

            //Add marker
            /*for(int i = 0; i < googleDirectionStepArrayList.size(); i++) {
                mapview.addMarker(new MarkerOptions().position(googleDirectionStepArrayList.get(i).getEndLocation())
                        .flat(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }*/

            String duration = direction.getDurationText(doc2);
            String distance = direction.getDistanceText(doc2);
            Global.printLog(true, "duration", String.valueOf(duration));
            Global.printLog(true, "distance", String.valueOf(distance));

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    //End of Direction

    private void setTaxi2customerDirection(String data) {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(data)));
            ArrayList<LatLng> directionPoint = direction.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.parseColor("#FF00a8ff"));
            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            taxi2customer = mapview.addPolyline(rectLine);

            //Add by Beat
            aq.id(R.id.home_topframe_navigation).visibility(View.VISIBLE);
            aq.id(R.id.home_btn_sos2).clicked(this);
            registeerSensor();
            Status.enableNavigation = true;
            googleDirectionStepArrayList = direction.getDirectionStep(doc);
            listGeoPoint = direction.getDirection(doc);
            current_zoom = 16.0f;

            aq.id(R.id.btn_navigation).image(R.drawable.home_navigation_active);

            updateUILocation(myLocation.latitude + "", myLocation.longitude + "", pantoLocation);

            outOfTheWaySchedule();

            //Add marker
            /*for(int i = 0; i < googleDirectionStepArrayList.size(); i++) {
                mapview.addMarker(new MarkerOptions().position(googleDirectionStepArrayList.get(i).getEndLocation())
                        .flat(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }*/

            String duration = direction.getDurationText(doc);
            String distance = direction.getDistanceText(doc);
            Global.printLog(true, "duration", String.valueOf(duration));
            Global.printLog(true, "distance", String.valueOf(distance));

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void checkState() {

        startService(intent_service);

        if (AppSharedPreferences.getInstance(this).getOnJob()) {
            //Job Type = 1
            if (AppSharedPreferences.getInstance(this).getOnState1()) {

                if (!AppSharedPreferences.getInstance(this).getThumbingMode()) {

                    HashMap<String, Object> data = (HashMap<String, Object>) AppSharedPreferences.getInstance(HomeActivity.this).getState_1();
                    aq.id(R.id.home_topframe2).visibility(View.VISIBLE);
                    aq.id(R.id.home_customer_pix).visibility(View.VISIBLE);
                    aq.id(R.id.btn_Call).visibility(View.VISIBLE);
                    aq.id(R.id.home_customer_name).text(getString(R.string.home_title_4) + " " + String.valueOf(data.get("cust_name")));

                    if (!String.valueOf(data.get("cust_pic")).equalsIgnoreCase("") && !String.valueOf(data.get("cust_pic")).equalsIgnoreCase("null"))
                        Picasso.with(HomeActivity.this).load(String.valueOf(data.get("cust_pic"))).resize(cust_pix.getWidth(), cust_pix.getHeight()).placeholder(R.drawable.home_pic_passenger).error(R.drawable.home_pic_passenger).into((ImageView) aq.id(R.id.home_customer_pix).getView());
                    setCustomerMarker();
                } else {
                    Picasso.with(HomeActivity.this).load(R.drawable.home_pic_passenger).resize(cust_pix.getWidth(), cust_pix.getHeight()).placeholder(R.drawable.home_pic_passenger).error(R.drawable.home_pic_passenger).into((ImageView) aq.id(R.id.home_customer_pix).getView());


                    aq.id(R.id.btn_thumbing).visibility(View.GONE);
//                    aq.id(R.id.home_btn_statusbar_1).clicked(null);
//                    aq.id(R.id.home_btn_statusbar_1).background(R.drawable.home_status2_active);
//                    aq.id(R.id.home_btn_statusbar_1).textColorId(R.color.white);
                }

                aq.id(R.id.home_taxi_status).image(R.drawable.status_reserved);
                //sound
                playAudio(R.raw.sound_reserve);
                aq.id(R.id.btn_Start_Mter).visibility(View.VISIBLE);
                aq.id(R.id.btn_Stop_Meter).visibility(View.GONE);

            }

            if (AppSharedPreferences.getInstance(this).getOnState2()) {

                if (!AppSharedPreferences.getInstance(HomeActivity.this).getThumbingMode())
                    setTakeCustomerToDestination();
                else {
                    aq.id(R.id.bt_jobtype_1).visibility(View.GONE);
//                    aq.id(R.id.home_btn_statusbar_2).background(R.drawable.home_status2_active);
//                    aq.id(R.id.home_btn_statusbar_2).textColorId(R.color.white);
//                    aq.id(R.id.home_btn_statusbar_2).clicked(null);
                    aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_003));
                }
            }

            if (AppSharedPreferences.getInstance(this).getOnState3()) {

//                aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.white);
//                aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_status2_active);
//                aq.id(R.id.home_btn_statusbar_4).clicked(this);
                aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_004));
                aq.id(R.id.home_taxi_status).image(R.drawable.status_on_job);
                //sound
                playAudio(R.raw.sound_onjob);
                aq.id(R.id.btn_Start_Mter).visibility(View.GONE);
                aq.id(R.id.btn_Stop_Meter).visibility(View.VISIBLE);


            }

            stopService(intent_jobTracking);
            startService(intent_taxi_status);

        } else {

            if (!AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("1") && !AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("")) {

                HashMap<String, Object> data = AppSharedPreferences.getInstance(this).getState_Hub();
                if (AppSharedPreferences.getInstance(this).getJobType().equalsIgnoreCase("9")) {

                    setOthersMarker(data);
                    settingOthersJobStatusBar(data);

                    if (AppSharedPreferences.getInstance(HomeActivity.this).getOnGasStaion()) {

                        aq.id(R.id.btn_thumbing).visibility(View.GONE);
//                        aq.id(R.id.home_btn_statusbar_4).clicked(HomeActivity.this);
//                        aq.id(R.id.home_btn_statusbar_3).clicked(HomeActivity.this);
                        //aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.white);
                        // aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_status2_active);
                    }

                    stopService(intent_jobTracking);

                } else {
                    setOthersMarker(data);
                    settingOthersJobStatusBar(data);
                    stopService(intent_jobTracking);
                }

            } else {
                startService(intent_jobTracking);
            }
        }
    }

    private void resetProgressState() {

        /*aq.id(R.id.home_btn_statusbar_1).clicked(this);
        aq.id(R.id.home_btn_statusbar_2).clicked(this);
        aq.id(R.id.home_btn_statusbar_3).clicked(this);
        aq.id(R.id.home_btn_statusbar_4).clicked(null);*/
        aq.id(R.id.home_btn_gas).clicked(this);
//        aq.id(R.id.home_btn_statusbar_1).clicked(null);
//        aq.id(R.id.home_btn_statusbar_2).clicked(null);
//        aq.id(R.id.home_btn_statusbar_3).clicked(null);
//        aq.id(R.id.home_btn_statusbar_4).clicked(null);

        aq.id(R.id.btn_thumbing).visibility(View.VISIBLE);
        aq.id(R.id.btn_Start_Mter).visibility(View.GONE);
        aq.id(R.id.btn_Stop_Meter).visibility(View.GONE);


        aq.id(R.id.btn_pick_customer).visibility(View.GONE);
        aq.id(R.id.home_taxi_status).image(R.drawable.status_stand_by);
        //sound
        //playAudio("สแตนบาย");
        aq.id(R.id.btn_Start_Mter).visibility(View.GONE);
        aq.id(R.id.btn_Stop_Meter).visibility(View.GONE);
        aq.id(R.id.home_btn_sos).image(R.drawable.home_sos);

        aq.id(R.id.bt_jobtype_1).visibility(View.GONE);
        aq.id(R.id.bt_jobtype_2).visibility(View.GONE);
        aq.id(R.id.bt_jobtype_3).visibility(View.GONE);
        aq.id(R.id.bt_jobtype_4).visibility(View.GONE);
        aq.id(R.id.bt_jobtype_5).visibility(View.GONE);
        aq.id(R.id.bt_jobtype_6_8).visibility(View.GONE);
        aq.id(R.id.bt_jobtype_9).visibility(View.GONE);

//        aq.id(R.id.home_btn_statusbar_1).background(R.drawable.home_btn_statusbar);
//        aq.id(R.id.home_btn_statusbar_2).background(R.drawable.home_btn_statusbar);
//        aq.id(R.id.home_btn_statusbar_3).background(R.drawable.home_btn_statusbar);
//        aq.id(R.id.home_btn_statusbar_4).background(R.drawable.home_btn_statusbar_lastitem);
//
//        aq.id(R.id.home_btn_statusbar_1).textColorId(R.color.black);
//        aq.id(R.id.home_btn_statusbar_2).textColorId(R.color.black);
//        aq.id(R.id.home_btn_statusbar_3).textColorId(R.color.black);
//        aq.id(R.id.home_btn_statusbar_4).textColorId(R.color.black);

        aq.id(R.id.home_topframe2).visibility(View.GONE);
        aq.id(R.id.home_customer_pix).visibility(View.GONE);
        aq.id(R.id.btn_Call).visibility(View.GONE);
        aq.id(R.id.home_customer_name).text(getString(R.string.home_title_4));
        aq.id(R.id.home_status).text(getString(R.string.home_title_3) + " " + getString(R.string.status_001));

        AppSharedPreferences.getInstance(this).removePickCustomer();
        AppSharedPreferences.getInstance(this).removeJobType();
        AppSharedPreferences.getInstance(this).removeJobID();
        AppSharedPreferences.getInstance(this).removeOnJob();
        AppSharedPreferences.getInstance(this).removeOnState1();
        AppSharedPreferences.getInstance(this).removeOnState2();
        AppSharedPreferences.getInstance(this).removeOnState3();
        AppSharedPreferences.getInstance(this).removeThumbingMode();
        AppSharedPreferences.getInstance(this).removeState_Payment();
        AppSharedPreferences.getInstance(this).removeOnGasStaion();
        AppSharedPreferences.getInstance(this).removeState_Hub();
        AppSharedPreferences.getInstance(this).removeReservedID();

        // Add by Beat
        closeNavigation();

        if (destination_maker != null)
            destination_maker.remove();

        destination_maker = null;

        if (destination_location != null)
            destination_location = null;


        if (taxi2othersJob != null)
            taxi2othersJob.remove();

        if (customer2destination != null)
            customer2destination.remove();

        if (taxi2customer != null)
            taxi2customer.remove();

        taxi2customer = null;
        taxi2othersJob = null;
        customer2destination = null;

        //reset Service
        stopService(intent_taxi_status);
        startService(intent_jobTracking);
    }

    private double check_last_km;


    private void showLogoutDialog() {

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.dialog_logout, null);

        TextView dialog_msg = (TextView) v.findViewById(R.id.confirm_msg);
        dialog_msg.setText(getString(R.string.logout_msg));
        TextView KM = (TextView) v.findViewById(R.id.textView);
        KM.setText("เลขกิโลเมตร : ");
        final TextView input_KM = (TextView) v.findViewById(R.id.editText);
        TextView unit = (TextView) v.findViewById(R.id.textView2);
        unit.setText("กิโลเมตร");
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
                                // check_last_km = Double.parseDouble((String.valueOf(dataObj.get("last_km"))));
                                // double inputKm = Double.parseDouble(input_KM.getText().toString());

                                mediaPlayer.setLooping(false);
                                if(input_KM != null && !input_KM.getText().toString().isEmpty())  {
                                    //if(inputKm > check_last_km || inputKm == 999999999){
                                    closeDialog();
                                    logout(input_KM.getText().toString());

                                    if(mediaPlayer.isPlaying()) {

                                        mediaPlayer.stop();
                                        mediaPlayer = null;
                                    }
                                    if(mp.isPlaying())
                                    {
                                        mp.stop();
                                        mp = null;
                                    }

                                    // }
                                    // else {
                                    //     Toast.makeText(getBaseContext(), R.string.alert_KM, Toast.LENGTH_SHORT).show();
                                    // }

                                } else {
                                    Toast.makeText(getBaseContext(), R.string.alert_KM, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                //Global.showAlertDialog(SplashActivity.this, String.valueOf(result.get("message")));
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
                timerTask.run();
                closeDialog();
            }
        });

        dialog.show();
    }

    private void logout(String input_km) {

        String url = APIs.getLogoutAPI(String.valueOf(profile.get("staff_id")), String.valueOf(profile.get("taxi_id")), String.valueOf(profile.get("operation_id")), input_km);

        setProgressbarVisibility(true);
        timerCount = new MyCount(10000, 1000);
        timerCount.start();
        //abcdef
        aq.ajax(url, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String data, AjaxStatus status) {

                try {
                    setProgressbarVisibility(false);
                    JSONParser json = new JSONParser(data);
                    HashMap<String, Object> dataObj = (HashMap<String, Object>) json.convertJson2HashMap();

                    if (String.valueOf(dataObj.get("code")).equalsIgnoreCase("200")) {
                        LoginHelper.getInstance(HomeActivity.this).deleteRememberLogin();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeOnJob();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeState_1();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeOnState1();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeOnState2();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeOnState3();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeState_Payment();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeJobType();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeState_Hub();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeStateConnection();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeThumbingMode();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeJobID();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeOnGasStaion();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeStateKickTime();

                        AppSharedPreferences.getInstance(HomeActivity.this).removeStateOnjob();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeStateStandby();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeStateBahtDay();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeStateBahtHour();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeLoginKM();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeApiKm();
                        AppSharedPreferences.getInstance(HomeActivity.this).removeShiftStart();



                        registerTaxi();
                    } else {
                        Global.showAlertDialog(HomeActivity.this, String.valueOf(dataObj.get("message")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void closeDialog() {

        if (dialog != null)
            dialog.dismiss();

        dialog = null;
    }

    private void registerTaxi() {

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
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.putExtra("taxi_id", String.valueOf(dataObj.get("taxi_id")));
                        startActivity(intent);
                        HomeActivity.this.finish();

                    } else {
                        Global.showAlertDialog(HomeActivity.this, String.valueOf(result.get("message")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void alertfinalCash(String cash , String check) {
        enable_credit_card = "0";
        reserved_type = "0";
//        check_get_job = true;
        //add
        startService(intent_jobTracking);

        if (dialog == null) {

            if (check == "cash") {
                playAudio(R.raw.sound_cash);
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.dialog_message, null);

                TextView dialog_msg = (TextView) v.findViewById(R.id.alert_msg);
                dialog_msg.setText("ชำระค่าบริการด้วยเงินสด"+"\n"+"ค่าโดยสารรวมทั้งหมด " + cash + " บาท");
//            playAudio(String.valueOf(cash)+"บาท");

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

                dialog_confirm.setTag((Dialog) dialog);
                dialog_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //playAudio(R.raw.sound_standby);
                        closeDialog();
                    }
                });

                dialog.show();
            }
            else if (check == "creditcard") {
                playAudio(R.raw.sound_creditcard);
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.dialog_message, null);

                TextView dialog_msg = (TextView) v.findViewById(R.id.alert_msg);
                dialog_msg.setText("ชำระค่าบริการด้วยบัตรเครดิต"+"\n"+"ค่าโดยสารรวมทั้งหมด " + cash + " บาท");
//            playAudio(String.valueOf(cash)+"บาท");

                TextView dialog_confirm = (TextView) v.findViewById(R.id.alert_btn_ok);
                dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(v);
                dialog.setCanceledOnTouchOutside(false);

                dialog_confirm.setTag((Dialog) dialog);
                dialog_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //playAudio(R.raw.sound_standby);
                        closeDialog();
                    }
                });

                dialog.show();
            }
        }
    }

    private void alertCall(final String msisdn) {

        if (dialog == null) {

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.dialog_confirm, null);

            TextView dialog_msg = (TextView) v.findViewById(R.id.confirm_msg);
            dialog_msg.setText(msisdn);

            TextView btn_ok = (TextView) v.findViewById(R.id.confirm_btn_ok);
            TextView btn_cancel = (TextView) v.findViewById(R.id.confirm_btn_cancel);

            btn_ok.setText("โทรออก");

            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(v);
            dialog.setCanceledOnTouchOutside(false);

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    closeDialog();
                    Global.startCall(HomeActivity.this, msisdn);
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
    }

    private void alertIncident() {

        if (dialog == null) {
            playAudio(R.raw.sound_outofservice);
            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.dialog_message, null);

            TextView dialog_msg = (TextView) v.findViewById(R.id.alert_msg);
            dialog_msg.setText("กด \"พร้อมปฏิบัติงาน\" เมื่อพร้อม");

            TextView dialog_confirm = (TextView) v.findViewById(R.id.alert_btn_ok);
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(v);
            dialog.setCanceledOnTouchOutside(false);

            dialog_confirm.setText("พร้อมปฏิบัติงาน");
            dialog_confirm.setTag((Dialog) dialog);
            dialog_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    check_get_job = true;
                    //add
                    startService(intent_jobTracking);
                    playAudio(R.raw.sound_standby);
                    standByMode();
                    closeDialog();
                }
            });

            dialog.show();
        }
    }

    private void alertCancelJob() {

        if (dialog == null) {

            aq.id(R.id.icon).visibility(View.GONE);
            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.dialog_message, null);

            TextView dialog_msg = (TextView) v.findViewById(R.id.alert_msg);
            dialog_msg.setText("ลูกค้าขอยกเลิกการให้ไปรับที่จุดรับผู้โดยสาร");

            TextView dialog_confirm = (TextView) v.findViewById(R.id.alert_btn_ok);
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(v);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
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

                    reserved_type = "0";
                    enable_credit_card = "0";
                    endJobWithoutResult(AppSharedPreferences.getInstance(HomeActivity.this).getJobID());
                    endTripWithoutResult(AppSharedPreferences.getInstance(HomeActivity.this).getJobID(), AppSharedPreferences.getInstance(HomeActivity.this).getJobType());

                    aq.id(R.id.icon_reserve).visibility(View.GONE);
                    aq.id(R.id.icon_app).visibility(View.GONE);
//                    aq.id(R.id.icon_visa).visibility(View.GONE);
                    aq.id(R.id.icon_thum).visibility(View.GONE);
                    aq.id(R.id.log_out).visible();
                    resetProgressState();
                    if (customer_location != null) {
                        customer_location = null;
                    }

                    if (customer_maker != null) {
                        customer_maker.remove();
                        customer_maker = null;
                    }

                    if (destination_maker != null) {
                        destination_maker.remove();
                        destination_maker = null;
                    }

                    if (destination_location != null)
                        destination_location = null;

                    if(!Status.checkclosejob) {
                        closeDialog();
                    }
                }
            });

            dialog.show();
        }
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

    public void registeerSensor() {
//        unRegisteerSensor();
//        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        mRotVectSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
//        mSensorManager.registerListener(this, mRotVectSensor, SENSOR_DELAY);
        Status.checkNavi = true;
    }

    public void unRegisteerSensor() {
//        if(mSensorManager != null) {
//            mSensorManager.unregisterListener(this);
//            mSensorManager = null;
//        }
        Status.checkNavi = false;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

            float[] mRotationMatrix = new float[16];

            SensorManager.getRotationMatrixFromVector(mRotationMatrix, sensorEvent.values);
            SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, mRotationMatrix);

            float[] orientation = new float[3];
            SensorManager.getOrientation(mRotationMatrix, orientation);

            bearing = Math.toDegrees(orientation[0]) + mDeclination;
            Toast.makeText(getApplicationContext(),bearing+"",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    Timer outOfTheWayTimer;
    TimerTask outOfTheWayTimerTask;
    ArrayList<LatLng> listGeoPoint = null;
    boolean checkSound = true;
    private void outOfTheWaySchedule() {

        outOfTheWayTimer = new Timer();
        outOfTheWayTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Checking out of the way(direction)
                        if (checkSound) {
                            if (CurrentLocation.currentLocation != null && listGeoPoint != null) {
                                LatLng currentLatLng = new LatLng(CurrentLocation.currentLocation.getLatitude(), CurrentLocation.currentLocation.getLongitude());
                                if (!PolyUtil.isLocationOnPath(currentLatLng, listGeoPoint, false, 50)) {
//                                    String[] mylocation = AppSharedPreferences.getInstance(HomeActivity.this).getLocation();

                                    if(Status.checkStatus == 0) {
                                        aq.ajax(direction.getGoogleDirectionAPI(new LatLng(CurrentLocation.currentLocation.getLatitude(), CurrentLocation.currentLocation.getLongitude()), customer_location, GMapV2Direction.MODE_DRIVING), String.class, new AjaxCallback<String>() {
                                            @Override
                                            public void callback(String url, String data, AjaxStatus status) {
                                                closeNavigation();
                                                setTaxi2customerDirection(data);
                                                checkSound =true;
                                            }
                                        });
                                    }
                                    if(Status.checkStatus == 1){
                                        aq.ajax(direction.getGoogleDirectionAPI(new LatLng(CurrentLocation.currentLocation.getLatitude(), CurrentLocation.currentLocation.getLongitude()), destination_location, GMapV2Direction.MODE_DRIVING), String.class, new AjaxCallback<String>() {
                                            @Override
                                            public void callback(String url, String data, AjaxStatus status) {
                                                closeNavigation();
                                                setCustomer2DestinationDirection(data);
                                                checkSound = true;
                                            }
                                        });
                                    }
                                    if(Status.checkStatus == 2){
                                        aq.ajax(direction.getGoogleDirectionAPI(new LatLng(CurrentLocation.currentLocation.getLatitude(), CurrentLocation.currentLocation.getLongitude()), latLng_destination, GMapV2Direction.MODE_DRIVING), String.class, new AjaxCallback<String>() {                                            @Override
                                            public void callback(String url, String data, AjaxStatus status) {
                                                closeNavigation();
                                                Status.enableGetDirection = true;
                                                pantoLocation = true;
                                                layout_destinationMarker.setVisibility(View.GONE);
                                                getDataDirection();
                                                checkSound = true;
                                            }
                                        });
                                    }

                                    playAudio(R.raw.outoftheway);
                                    checkSound = false;
                                }
                            }
                        }
                    }
                });
            }
        };
        outOfTheWayTimer.schedule(outOfTheWayTimerTask, 1000,  10000);
    }

    private void checkManeuver(String strManeuver) {
        if(strManeuver.equals("turn-sharp-left")) {
            playAudio(R.raw.turnsharpleft);
        } else if(strManeuver.equals("uturn-right")) {
            playAudio(R.raw.uturnright);
        } else if(strManeuver.equals("turn-slight-right")) {
            playAudio(R.raw.turnslightright);
        } else if(strManeuver.equals("merge")) {
            playAudio(R.raw.merge);
        } else if(strManeuver.equals("roundabout-left")) {
            playAudio(R.raw.roundaboutleft);
        } else if(strManeuver.equals("roundabout-right")) {
            playAudio(R.raw.roundaboutright);
        } else if(strManeuver.equals("uturn-left")) {
            playAudio(R.raw.uturnleft);
        } else if(strManeuver.equals("turn-slight-left")) {
            playAudio(R.raw.turnslightleft);
        } else if(strManeuver.equals("turn-left")) {
            playAudio(R.raw.turnleft);
        } else if(strManeuver.equals("ramp-right")) {
            playAudio(R.raw.rampright);
        } else if(strManeuver.equals("turn-right")) {
            playAudio(R.raw.turnright);
        } else if(strManeuver.equals("fork-right")) {
            playAudio(R.raw.forkright);
        } else if(strManeuver.equals("straight")) {
            playAudio(R.raw.straight);
        } else if(strManeuver.equals("fork-left")) {
            playAudio(R.raw.forkleft);
        } else if(strManeuver.equals("ferry-train")) {
            playAudio(R.raw.ferrytrain);
        } else if(strManeuver.equals("turn-sharp-right")) {
            playAudio(R.raw.turnsharpright);
        } else if(strManeuver.equals("ramp-left")) {
            playAudio(R.raw.rampleft);
        } else if(strManeuver.equals("ferry")) {
            playAudio(R.raw.ferry);
        } else if(strManeuver.equals("keep-left")) {
            playAudio(R.raw.keepleft);
        } else if(strManeuver.equals("keep-right")) {
            playAudio(R.raw.keepright);
        }
    }

    //Open navigation

    private static final String LOG_TAG = "HomeActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;

    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private LatLng latLng_destination = null;
    Button btn_navigation;

    private void openNavigation() {
        aq.id(R.id.home_topframe).visibility(View.GONE);
        aq.id(R.id.home_topframe_autocomplete).visibility(View.VISIBLE);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        p.addRule(RelativeLayout.BELOW, R.id.home_topframe_autocomplete);

        LinearLayout lyt_home_topframe2 = (LinearLayout) findViewById(R.id.home_topframe2);
        lyt_home_topframe2.setLayoutParams(p);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(HomeActivity.this)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                    .addConnectionCallbacks(this)
                    .build();
        } else {
            mGoogleApiClient.reconnect();
        }
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.atv_place);
        mAutocompleteTextView.setText("");
        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        mAutocompleteTextView.setTextColor(Color.BLACK);

        mAutocompleteTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mAutocompleteTextView.setText("");
            }
        });
        btn_navigation = (Button) findViewById(R.id.btn_open_navigation);
        btn_navigation.setText("ค้นหา");
        Status.enableNavigation = true;
        btn_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Status.enableGetDirection = true;
                pantoLocation = true;
                layout_destinationMarker.setVisibility(View.GONE);
                getDataDirection();
            }
        });
    }

    private void getDataDirection() {
        if(latLng_destination != null) {

//            Toast.makeText(getApplicationContext(), "getDataDirection", Toast.LENGTH_SHORT).show();
            Status.enableNavigation = true;
            setProgressbarVisibility(true);
            timerCount = new MyCount(10000, 1000);
            timerCount.start();
            aq.ajax(direction.getGoogleDirectionAPI(new LatLng(CurrentLocation.currentLocation.getLatitude(), CurrentLocation.currentLocation.getLongitude()), latLng_destination, GMapV2Direction.MODE_DRIVING), String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String data, AjaxStatus status) {
                    if(Status.enableGetDirection) {
                        Global.printLog(true, "GoogleAPI", String.valueOf(url));
                        Status.enableGetDirection = false;
                        aq.id(R.id.home_topframe_autocomplete).visibility(View.GONE);
                        aq.id(R.id.home_topframe_navigation).visibility(View.VISIBLE);
                        //
                        aq.id(R.id.home_topframe).visibility(View.VISIBLE);
                        //
                        aq.id(R.id.home_btn_sos2).clicked(HomeActivity.this);

                        setTaxi2SearchDestination(data);
                    }
                }
            });
        }
    }

    private void setTaxi2SearchDestination(String data) {
        if(Status.enableNavigation) {
            try {

                if(mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }

                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                p.addRule(RelativeLayout.BELOW, R.id.home_topframe_navigation);

                LinearLayout lyt_home_topframe2 = (LinearLayout) findViewById(R.id.home_topframe2);
                lyt_home_topframe2.setLayoutParams(p);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(data)));

                ArrayList<LatLng> directionPoint = direction.getDirection(doc);
                PolylineOptions rectLine = new PolylineOptions().width(15).color(Color.parseColor("#FF00a8ff"));
                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                taxi2othersJob = mapview.addPolyline(rectLine);
                setProgressbarVisibility(false);
                //Add by Beat
                aq.id(R.id.home_topframe_navigation).visibility(View.VISIBLE);
                aq.id(R.id.home_btn_sos2).clicked(this);
                registeerSensor();
                Status.enableNavigation = true;
                googleDirectionStepArrayList = direction.getDirectionStep(doc);
                listGeoPoint = direction.getDirection(doc);
                current_zoom = 16.0f;

                updateUILocation(myLocation.latitude + "", myLocation.longitude + "", pantoLocation);

                outOfTheWaySchedule();

                //Add marker
                /*for(int i = 0; i < googleDirectionStepArrayList.size(); i++) {
                    mapview.addMarker(new MarkerOptions().position(googleDirectionStepArrayList.get(i).getEndLocation())
                            .flat(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }*/


                String duration = direction.getDurationText(doc);
                String distance = direction.getDistanceText(doc);
                Global.printLog(true, "duration", String.valueOf(duration));
                Global.printLog(true, "distance", String.valueOf(distance));

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeNavigation() {

        if(Status.enableNavigation) {
            checkSound = true;
            aq.id(R.id.btn_navigation).image(R.drawable.home_navigation);
            aq.id(R.id.im_navigation).image(0);
            aq.id(R.id.tv_naviText).text("");
            aq.id(R.id.home_topframe_navigation).visibility(View.GONE);
            aq.id(R.id.home_topframe_autocomplete).visibility(View.GONE);
            aq.id(R.id.home_topframe).visibility(View.VISIBLE);
            googleDirectionStepArrayList = null;
            unRegisteerSensor();
            if(outOfTheWayTimer != null) {
                outOfTheWayTimer.cancel();
                outOfTheWayTimer = null;
            }
            countStep = 0;
            current_zoom = 15.0f;

            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            if(p != null) {
                p.addRule(RelativeLayout.BELOW, R.id.home_topframe);
            }

            LinearLayout lyt_home_topframe2 = (LinearLayout) findViewById(R.id.home_topframe2);

            if(lyt_home_topframe2 != null) {
                lyt_home_topframe2.setLayoutParams(p);
            }

            Status.enableNavigation = false;

        }
        Status.enableNavigation = false;
        countStep = 0;
        Status.enableTimerNavigation = true;

        if(taxi2othersJob != null)
            taxi2othersJob.remove();

        if(myLocation != null) {
            updateUILocation(myLocation.latitude + "", myLocation.longitude + "", pantoLocation);
        } else {
            if(CurrentLocation.currentLocation!=null)
                updateUILocation(CurrentLocation.currentLocation.getLatitude() + "", CurrentLocation.currentLocation.getLongitude() + "", pantoLocation);
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places
                    .GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            LatLng latLng = place.getLatLng();
            latLng_destination = place.getLatLng();

            panTo_myLocation_navigation = new CameraPosition.Builder().target(latLng_destination).zoom(current_zoom).tilt(25).bearing((float)bearing).build();
            changeCamera(CameraUpdateFactory.newCameraPosition(panTo_myLocation_navigation));
            btn_navigation.setText("นำทาง");
        }
    };


    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG,
                "Google Places API connection failed with error code: " + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" + connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    private boolean checkIntoLatlngBound(LatLngBounds bounds, LatLng point) {
        if(bounds != null && point != null) {
            if(bounds.contains(point)) {
                //If point is into the bound, do something.
                // 1.Play sound.
                // 2.Set boolean flag that defined pass the step.
                // 3.If true, break external loop.
                // 4.If false, continue external loop.
                return true;
            } else {
                return false;
            }
        } else {
            Log.i("Check into the bound: ", "Bound or point is null.");
            return false;
        }
    }

    private void imageManeuver(ImageView imageView, String strManeuver) {
        if(strManeuver.equals("turn-sharp-left")) {
            imageView.setImageResource(R.drawable.turnsharpleft);
        } else if(strManeuver.equals("uturn-right")) {
            imageView.setImageResource(R.drawable.uturnright);
        } else if(strManeuver.equals("turn-slight-right")) {
            imageView.setImageResource(R.drawable.turnslightright);
        } else if(strManeuver.equals("merge")) {
            imageView.setImageResource(R.drawable.merge);
        } else if(strManeuver.equals("roundabout-left")) {
            imageView.setImageResource(R.drawable.turnleft_keepleft);
        } else if(strManeuver.equals("roundabout-right")) {
            imageView.setImageResource(R.drawable.turnright_keepright);
        } else if(strManeuver.equals("uturn-left")) {
            imageView.setImageResource(R.drawable.uturnleft);
        } else if(strManeuver.equals("turn-slight-left")) {
            imageView.setImageResource(R.drawable.turnslightleft);
        } else if(strManeuver.equals("turn-left")) {
            imageView.setImageResource(R.drawable.turnleft_keepleft);
        } else if(strManeuver.equals("ramp-right")) {
            imageView.setImageResource(R.drawable.rampright);
        } else if(strManeuver.equals("turn-right")) {
            imageView.setImageResource(R.drawable.turnright_keepright);
        } else if(strManeuver.equals("fork-right")) {
            imageView.setImageResource(R.drawable.turnright_keepright);
        } else if(strManeuver.equals("straight")) {
            imageView.setImageResource(R.drawable.straight);
        } else if(strManeuver.equals("fork-left")) {
            imageView.setImageResource(R.drawable.turnleft_keepleft);
        } else if(strManeuver.equals("ferry-train")) {
            imageView.setImageResource(R.drawable.ferrytrain);
        } else if(strManeuver.equals("turn-sharp-right")) {
            imageView.setImageResource(R.drawable.turnsharpright);
        } else if(strManeuver.equals("ramp-left")) {
            imageView.setImageResource(R.drawable.rampleft);
        } else if(strManeuver.equals("ferry")) {
            imageView.setImageResource(R.drawable.ferrytrain);
        } else if(strManeuver.equals("keep-left")) {
            imageView.setImageResource(R.drawable.turnleft_keepleft);
        } else if(strManeuver.equals("keep-right")) {
            imageView.setImageResource(R.drawable.turnright_keepright);
        }
    }

    private void textManeuver(TextView tv, String strManeuver) {
        if(strManeuver.equals("turn-sharp-left")) {
            tv.setText("เลี้ยวซ้ายหักซอก");
        } else if(strManeuver.equals("uturn-right")) {
            tv.setText("กลับรถทางขวา");
        } else if(strManeuver.equals("turn-slight-right")) {
            tv.setText("เลี้ยวขวาเล็กน้อย");
        } else if(strManeuver.equals("merge")) {
            tv.setText("ทางร่วม");
        } else if(strManeuver.equals("roundabout-left")) {
            tv.setText("วงเวียนซ้าย");
        } else if(strManeuver.equals("roundabout-right")) {
            tv.setText("วงเวียนขวา");
        } else if(strManeuver.equals("uturn-left")) {
            tv.setText("กลับรถทางซ้าย");
        } else if(strManeuver.equals("turn-slight-left")) {
            tv.setText("เลี้ยวซ้ายเล็กน้อย");
        } else if(strManeuver.equals("turn-left")) {
            tv.setText("เลี้ยวซ้าย");
        } else if(strManeuver.equals("ramp-right")) {
            tv.setText("ทางลาดขวา");
        } else if(strManeuver.equals("turn-right")) {
            tv.setText("เลี้ยวขวา");
        } else if(strManeuver.equals("fork-right")) {
            tv.setText("แยกขวา");
        } else if(strManeuver.equals("straight")) {
            tv.setText("ตรงไป");
        } else if(strManeuver.equals("fork-left")) {
            tv.setText("แยกซ้าย");
        } else if(strManeuver.equals("ferry-train")) {
            tv.setText("ข้ามทางรถไฟ");
        } else if(strManeuver.equals("turn-sharp-right")) {
            tv.setText("เลี้ยวขวาหักซอก");
        } else if(strManeuver.equals("ramp-left")) {
            tv.setText("ทางลาดซ้าย");
        } else if(strManeuver.equals("ferry")) {
            tv.setText("ข้ามฝาก");
        } else if(strManeuver.equals("keep-left")) {
            tv.setText("ชิดซ้าย");
        } else if(strManeuver.equals("keep-right")) {
            tv.setText("ชิดขวา");
        }
    }

    private GetAddressTask getAddressTask;
    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Toast.makeText(getApplicationContext(), ev.getAction() + "", Toast.LENGTH_LONG).show();
        if(layout_destinationMarker.getVisibility() == View.VISIBLE) {
            if(latLng_destination != null) {
                if(ev.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(latLng_destination.latitude, latLng_destination.longitude, 1);
                        if(addresses.size() > 0) {
                            mAutocompleteTextView.setText(addresses.get(0).getAddressLine(0) + " " + addresses.get(0).getLocality() + " " + addresses.get(0).getAdminArea());
                        }
                        mAutocompleteTextView.dismissDropDown();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //destination_maker.setPosition(latLng_destination);
            //destination_maker = mapview.addMarker(new MarkerOptions().position(latLng_destination).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin_destination)));
        }
        return super.dispatchTouchEvent(ev);
    }

}*/
//
//class TimeCheck extends IllegalArgumentException {
//    public TimeCheck() { super(); }
//    public TimeCheck(String message) { super(message); }
//    public TimeCheck(String message, Throwable cause) { super(message, cause); }
//    public TimeCheck(Throwable cause) { super(cause); }
//}

    private class GetAddressTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String addressStr = "";
                Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latLng_destination.latitude, latLng_destination.longitude, 1);
                if (addresses.size() > 0) {
                    addressStr += (addresses.get(0).getAddressLine(0) + " " + addresses.get(0).getLocality() + " " + addresses.get(0).getAdminArea());
                }
                return addressStr;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                mAutocompleteTextView.setText(result);

                //mAutocompleteTextView.dismissDropDown();
            } catch(Exception ex) {
                mAutocompleteTextView.setText("");
            }
        }
    }

}