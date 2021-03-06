package com.infratrans.taxi.onboard.view;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.infratrans.taxi.onboard.R;

import java.util.HashMap;

/**
 * Created by Thanisak Piyasaksiri on 3/29/15 AD.
 */
public class ViewCarWash extends RelativeLayout {

    private AQuery aq;
    private Context context;
    private HashMap<String, Object> data;

    private View v = null;
    private LayoutInflater vi = null;
    private ViewCarWashListener listener;

    private MediaPlayer mediaPlayer;

    public ViewCarWash(Context context, HashMap<String, Object> data, ViewCarWashListener listener) {

        super(context);
        this.data = data;
        this.context = context;
        this.listener = listener;

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.dialog_carwash, null);
        addView(v);

        mediaPlayer = MediaPlayer.create(context, R.raw.sonar);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        init();
    }

    private void init() {

        aq = new AQuery((Activity) this.context, v);
        aq.id(R.id.carwash_desc).text(String.valueOf(this.data.get("popup_desc")));

        aq.id(R.id.carwash_navigate).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                mediaPlayer = null;

                if (listener != null)
                    listener.viewCarWashNavigate(data);
            }
        });

        aq.id(R.id.carwash_btn_ok).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                mediaPlayer = null;

                if (listener != null)
                    listener.viewCarWashAccept(data);
            }
        });
    }

    public interface ViewCarWashListener {

        void viewCarWashNavigate(HashMap<String, Object> data);

        void viewCarWashAccept(HashMap<String, Object> data);
    }
}
