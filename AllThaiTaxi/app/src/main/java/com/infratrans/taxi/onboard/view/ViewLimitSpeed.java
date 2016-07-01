package com.infratrans.taxi.onboard.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.infratrans.taxi.onboard.R;

/**
 * Created by Thanisak Piyasaksiri on 3/29/15 AD.
 */
public class ViewLimitSpeed extends RelativeLayout {

    private Context context;

    private View v = null;
    private LayoutInflater vi = null;

    public ViewLimitSpeed(Context context) {

        super(context);
        this.context = context;

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.dialog_limitspeed, null);
        addView(v);
    }
}
