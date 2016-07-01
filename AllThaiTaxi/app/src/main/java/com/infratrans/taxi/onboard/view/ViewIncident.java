package com.infratrans.taxi.onboard.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.infratrans.taxi.onboard.R;

/**
 * Created by Thanisak Piyasaksiri on 3/25/15 AD.
 */
public class ViewIncident extends RelativeLayout {

    private AQuery aq;
    private Context context;
    private ViewIncidentListener listener;

    private View v = null;
    private LayoutInflater vi = null;

    public ViewIncident(Context context, ViewIncidentListener listener) {

        super(context);
        this.context = context;
        this.listener = listener;

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.dialog_incident, null);
        addView(v);

        init();
    }

    private void init() {

        aq = new AQuery((Activity) this.context, v);

        aq.id(R.id.incident_btn_accident).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null)
                    listener.viewClickAccident();
            }
        });

        aq.id(R.id.incident_btn_car).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null)
                    listener.viewClickCarBrokeDown();
            }
        });

        aq.id(R.id.incident_btn_wheel).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null)
                    listener.viewClickWheel();
            }
        });


        aq.id(R.id.incident_btn_incident).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null)
                    listener.viewClickIncident();
            }
        });

        aq.id(R.id.incident_btn_close).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null)
                    listener.viewClickIncidentClose();
            }
        });

        aq.id(R.id.incident_btn_break_time).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null)
                    listener.viewClickBreakingTime();
            }
        });
    }

    public interface ViewIncidentListener {

        void viewClickAccident();

        void viewClickCarBrokeDown();

        void viewClickWheel();

        void viewClickIncident();

        void viewClickIncidentClose();

        void viewClickBreakingTime();
    }
}
