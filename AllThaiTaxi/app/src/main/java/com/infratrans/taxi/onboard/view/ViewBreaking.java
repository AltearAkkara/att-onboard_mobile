package com.infratrans.taxi.onboard.view;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.infratrans.taxi.onboard.R;

import java.net.URI;
import java.net.URL;

/**
 * Created by Thanisak Piyasaksiri on 4/8/15 AD.
 */
public class ViewBreaking extends RelativeLayout {

    private AQuery aq;
    private Context context;
    private ViewBreakingListener listener;
    //private String checkStatus;

    private View v = null;
    private LayoutInflater vi = null;

    public ViewBreaking(Context context, ViewBreakingListener listener) {

        super(context);
        this.listener = listener;
        this.context = context;
        //this.checkStatus = checkStatus;

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.dialog_breaking, null);
        addView(v);

        init();
    }

//    private MediaPlayer StatusSpeak;
//    private URI StausSpeakerUri;
//    String str_preTextUrl_status = "http://translate.google.com/translate_tts?ie=UTF-8&q=";
//    String str_textUrl_status = "";
//    String str_postTextUrl_status = "&tl=th";
//    public void StatusSpeak(String textSpeaker) {
//        try {
//            if(textSpeaker != null && !textSpeaker.isEmpty()) {
//                StatusSpeak = new MediaPlayer();
//                StatusSpeak.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                str_textUrl_status = textSpeaker;
//                StausSpeakerUri = new URI(str_preTextUrl_status + str_textUrl_status + str_postTextUrl_status);
//                URL url = new URL(StausSpeakerUri.toASCIIString());
//                StatusSpeak.setDataSource(url.toString());
//                StatusSpeak.prepare();
//                StatusSpeak.start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void init() {

        aq = new AQuery((Activity) this.context, v);
        aq.id(R.id.breaking_btn_ok).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.viewBreakingAccept();
                }

            }
        });
    }

    public interface ViewBreakingListener {

        void viewBreakingAccept();
    }
}
