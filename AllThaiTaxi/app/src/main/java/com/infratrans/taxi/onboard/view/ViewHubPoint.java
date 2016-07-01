package com.infratrans.taxi.onboard.view;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.infratrans.taxi.onboard.R;
import com.infratrans.taxi.onboard.Status;

import java.util.HashMap;

/**
 * Created by Thanisak Piyasaksiri on 2/7/15 AD.
 */
public class ViewHubPoint extends RelativeLayout {

    private AQuery aq;
    private Context context;
    private HashMap<String, Object> data;

    private View v = null;
    private LayoutInflater vi = null;
    private ViewHubPointListener listener;
    private MyCountMedia timerCountMedia;
    private MediaPlayer mediaPlayer;

    public ViewHubPoint(Context context, HashMap<String, Object> data, ViewHubPointListener listener) {

        super(context);
        this.data = data;
        this.context = context;
        this.listener = listener;


        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.dialog_hub, null);
        addView(v);

        mediaPlayer = MediaPlayer.create(context, R.raw.sonar);
        mediaPlayer.setLooping(true);
        timerCountMedia = new MyCountMedia(2000, 1000);
        timerCountMedia.start();

        init();
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

    private void init() {

        aq = new AQuery((Activity) this.context, v);
        aq.id(R.id.hub_navigate).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer != null) {
                    if (!mediaPlayer.isPlaying()) {
                        timerCountMedia.cancel();
                        timerCountMedia = null;
                        mediaPlayer = null;
                    } else if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer = null;
                    }

                    if (listener != null)
                        listener.viewHubPointNavigate(data);
                }

                Status.enableNavigation = true;
            }

        });

        aq.id(R.id.hub_btn_ok).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer != null) {
                    if (!mediaPlayer.isPlaying()) {
                        timerCountMedia.cancel();
                        timerCountMedia = null;
                        mediaPlayer = null;
                    } else if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer = null;
                    }

                    if (listener != null)
                        listener.viewHibPointAccept(data);
                }

            }
        });
    }


    public interface ViewHubPointListener {

        void viewHubPointNavigate(HashMap<String, Object> data);

        void viewHibPointAccept(HashMap<String, Object> data);
    }
}
