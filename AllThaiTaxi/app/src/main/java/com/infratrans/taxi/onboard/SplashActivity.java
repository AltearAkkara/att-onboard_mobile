package com.infratrans.taxi.onboard;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.crashlytics.android.Crashlytics;
import com.infratrans.taxi.onboard.json.JSONParser;
import com.infratrans.taxi.onboard.shares.LoginHelper;
import com.infratrans.taxi.onboard.util.APIs;
import com.infratrans.taxi.onboard.util.Global;

import org.json.JSONException;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Thanisak Piyasaksiri on 2/5/15 AD.
 */

public class SplashActivity extends FragmentActivity {

    boolean forcedStop = false;
    private AQuery aq;
    DhcpInfo d;
    public int   s_gateway;
    WifiManager wifii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        wifii= (WifiManager) getSystemService(Context.WIFI_SERVICE);
        d=wifii.getDhcpInfo();
        s_gateway = d.gateway;

        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
//                car
//            APIs.DOMAIN = "http://192.168.0.58:8080/taxi/api/index.php/car/";

//          TEST 480
          APIs.DOMAIN = "http://171.101.138.95:8080/taxi/api/index.php/car/";
        initeFade();
    }


    public void initeFade() {

        aq = new AQuery(this);
        aq.id(R.id.splash_frame).visibility(View.GONE);
        new Handler().postDelayed(new Runnable() {

            public void run() {

                Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, android.R.anim.fade_in);
                anim.setDuration(500);
                anim.setFillAfter(true);
                aq.id(R.id.splash_frame).getView().startAnimation(anim);
                aq.id(R.id.splash_frame).visibility(View.VISIBLE);
            }

        }, 500);

        new Handler().postDelayed(new Runnable() {

            public void run() {

                forcedStop = true;
                if (LoginHelper.getInstance(SplashActivity.this).checkLogin()) {

                    startApplication();

                } else {

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

                                        startLogin(String.valueOf(dataObj.get("taxi_id")));



                                } else {
                                    Global.showAlertDialog(SplashActivity.this, String.valueOf(result.get("message")));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        }, 3000);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        boolean r = false;

        if (keyCode == 4) {

            System.exit(0);
            r = true;
        }
        return r;
    }

    private void startApplication() {

        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void startLogin(String taxi_id) {

//        Intent intent = new Intent(this, LoginActivity1.class);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("taxi_id", taxi_id);
        startActivity(intent);
        finish();
    }
}
