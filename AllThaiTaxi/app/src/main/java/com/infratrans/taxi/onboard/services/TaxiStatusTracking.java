package com.infratrans.taxi.onboard.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.infratrans.taxi.onboard.CurrentLocation;
import com.infratrans.taxi.onboard.R;
import com.infratrans.taxi.onboard.json.JSONParser;
import com.infratrans.taxi.onboard.shares.AppSharedPreferences;
import com.infratrans.taxi.onboard.shares.LoginHelper;
import com.infratrans.taxi.onboard.util.APIs;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Thanisak Piyasaksiri on 3/29/15 AD.
 */
public class TaxiStatusTracking extends Service {
    public static final String BROADCAST_ACTION = "com.infratrans.taxi.onboard.service.taxi_status";
    private AQuery aq;
    private Timer timer;
    private Context context;
    private int sync_time = 2 * 1000; // 2 Sec.
    private JSONParser json;

    @Override
    public void onDestroy() {

        stopTracking();
        super.onDestroy();
    }

    @Override
    public void onCreate() {

        super.onCreate();

        this.aq = new AQuery(this);
        this.timer = new Timer();
        startTracking();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public void startTracking() {

        timer.schedule(new UpdateTaxiStatusTracker(), 1000, sync_time);

    }

    public void stopTracking() {

        timer.cancel();
        timer.purge();
    }

    class UpdateTaxiStatusTracker extends TimerTask {


        @Override
        public void run() {

            String url = APIs.getTaxiStatusAPI(LoginHelper.getInstance(TaxiStatusTracking.this).getTaxiID(), AppSharedPreferences.getInstance(TaxiStatusTracking.this).getJobType(), AppSharedPreferences.getInstance(TaxiStatusTracking.this).getReservedID());

            aq.ajax(url, String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String data, AjaxStatus status) {


                    Intent intent = new Intent(BROADCAST_ACTION);
                    intent.putExtra("data", String.valueOf(data));

                    intent.putExtra("debug",System.currentTimeMillis()+" , "+status.getCode()+" , "+status.getError());
                    sendBroadcast(intent);


                }
            });
        }
    }

}
