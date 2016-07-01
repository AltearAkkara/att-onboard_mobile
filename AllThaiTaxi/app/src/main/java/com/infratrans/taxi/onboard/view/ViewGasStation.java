package com.infratrans.taxi.onboard.view;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.infratrans.taxi.onboard.R;

import java.util.HashMap;

/**
 * Created by Thanisak Piyasaksiri on 3/26/15 AD.
 */
public class ViewGasStation extends RelativeLayout {

    private AQuery aq;
    private Context context;
    private HashMap<String, Object> data;

    private View v = null;
    private LayoutInflater vi = null;
    private ViewGasStationListener listener;

    private MediaPlayer mediaPlayer;

    public ViewGasStation(Context context, HashMap<String, Object> data, ViewGasStationListener listener) {

        super(context);
        this.data = data;
        this.context = context;
        this.listener = listener;

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.dialog_gas_station, null);
        addView(v);

        mediaPlayer = MediaPlayer.create(context, R.raw.sonar);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        init();
    }

    private void init() {

//        Toast.makeText(getContext().getApplicationContext(),"3 : ViewgasStation   "+String.valueOf(data.get("job_id")), Toast.LENGTH_LONG).show();
        aq = new AQuery((Activity) this.context, v);
        aq.id(R.id.gasstation_btn_ok).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                }

                if (listener != null)
                    listener.viewGasStationAccept(data);
            }
        });

        aq.id(R.id.gasstation_navigate).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                }


                if (listener != null)
                    listener.viewGasStationNavigate(data);
            }
        });
    }

    public interface ViewGasStationListener {

        void viewGasStationAccept(HashMap<String, Object> data);

        void viewGasStationNavigate(HashMap<String, Object> data);
    }
}
