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
 * Created by Thanisak Piyasaksiri on 3/25/15 AD.
 */
public class ViewOutOfService extends RelativeLayout {

    private AQuery aq;
    private Context context;
    private HashMap<String, Object> data;
    private ViewOutOfServiceListener listener;

    private View v = null;
    private LayoutInflater vi = null;
    private boolean btn_visibility = false;

    private MediaPlayer mediaPlayer;

    public ViewOutOfService(Context context, HashMap<String, Object> data, boolean btn_visibility, ViewOutOfServiceListener listener) {

        super(context);
        this.data = data;
        this.context = context;
        this.listener = listener;
        this.btn_visibility = btn_visibility;

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.dialog_outofservice, null);
        addView(v);

        if (btn_visibility) {

            mediaPlayer = MediaPlayer.create(context, R.raw.sonar);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        init();
    }

    private void init() {

        aq = new AQuery((Activity) this.context, v);

        if (btn_visibility) {

            aq.id(R.id.outofservice_btn_ok).clicked(new OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer = null;
                    }

                    if (listener != null)
                        listener.viewOutOfServiceAccept(data);
                }
            });
        } else {
            aq.id(R.id.outofservice_btn_ok).visibility(View.GONE);
        }

    }

    public interface ViewOutOfServiceListener {

        void viewOutOfServiceAccept(HashMap<String, Object> data);
    }
}
