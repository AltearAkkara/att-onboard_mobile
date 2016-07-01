package com.infratrans.taxi.onboard.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.infratrans.taxi.onboard.R;
import com.infratrans.taxi.onboard.json.JSONParser;
import com.infratrans.taxi.onboard.util.APIs;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Thanisak Piyasaksiri on 3/25/15 AD.
 * Modified by Thanisak Piyasaksiri on 3/29/15 AD.
 */
public class MeterCheckerService extends Service {

    public static final String BROADCAST_ACTION = "com.infratrans.taxi.onboard.service.meter_checker";
    private AQuery aq;
    private Timer timer;
    private Context context;
    private int sync_time = 2 * 1000; // 2 Sec.

    @Override
    public void onDestroy() {

        stopChecking();
        super.onDestroy();
    }

    @Override
    public void onCreate() {

        super.onCreate();

        aq = new AQuery(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        this.timer = new Timer();
        startChecking();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public void startChecking() {

        timer.schedule(new MeterChecker(), 1000, sync_time);
    }

    public void stopChecking() {

        timer.cancel();
        timer.purge();
    }

    class MeterChecker extends TimerTask {

        @Override
        public void run() {

            String url = APIs.getMeterAPI();
            aq.ajax(url, String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String data, AjaxStatus status) {

                    JSONParser json;
                    try {
                        json = new JSONParser(data);
                        HashMap<String, Object> resultObj = (HashMap<String, Object>) json.convertJson2HashMap();
                        if (String.valueOf(resultObj.get("code")).equalsIgnoreCase("200")) {


                            HashMap<String, Object> dataObj = (HashMap<String, Object>) resultObj.get("data");
                            Intent intent = new Intent(BROADCAST_ACTION);
                            intent.putExtra("status", String.valueOf(dataObj.get("meter")));
                            intent.putExtra("check", "200");
                            ((FragmentActivity) context).sendBroadcast(intent);
                        }
                        else
                        {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
