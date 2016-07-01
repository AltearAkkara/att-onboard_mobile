package com.infratrans.taxi.onboard.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.infratrans.taxi.onboard.Status;
import com.infratrans.taxi.onboard.shares.LoginHelper;
import com.infratrans.taxi.onboard.util.APIs;
import com.infratrans.taxi.onboard.util.Global;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Thanisak Piyasaksiri on 3/25/15 AD.
 * Modeified by Thanisak Piyasaksiri on 3/29/15 AD.
 */
public class JobTracking extends Service {

    public static final String BROADCAST_ACTION = "com.infratrans.taxi.onboard.service.job_tracking";
    private AQuery aq;
    private Timer timer;
    private Context context;
    private int sync_time = 5 * 1000; // 5 Sec.

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

        timer.schedule(new UpdateJobTracker(), 1000, sync_time);
    }

    public void stopTracking() {

        timer.cancel();
        timer.purge();
    }

    class UpdateJobTracker extends TimerTask {

        @Override
        public void run() {

            String url = APIs.getRequestJobAPI(LoginHelper.getInstance(getApplicationContext()).getTaxiID());
            Status.check_Request++;
            Global.printLog(true, "Request Job", String.valueOf(url));
            aq.ajax(url, String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String data, AjaxStatus status) {

                    Intent intent = new Intent(BROADCAST_ACTION);
                    intent.putExtra("data", String.valueOf(data));
                    intent.putExtra("debug", System.currentTimeMillis() + " , " + status.getCode() + " , " + status.getError());
                    sendBroadcast(intent);
                }
            });
        }
    }
}
