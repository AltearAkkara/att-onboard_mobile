package com.infratrans.taxi.onboard.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.infratrans.taxi.onboard.HomeActivity;
import com.infratrans.taxi.onboard.R;
import com.infratrans.taxi.onboard.util.APIs;

import java.util.HashMap;

/**
 * Created by Thanisak Piyasaksiri on 3/29/15 AD.
 */
public class ViewGasPayment extends RelativeLayout {

    private AQuery aq;
    private Context context;
    private HashMap<String, Object> data;

    private View v = null;
    private LayoutInflater vi = null;
    private ViewGasPaymentListener listener;
    private String job_id;
    private String job_type;




    public ViewGasPayment(Context context, HashMap<String, Object> data, ViewGasPaymentListener listener, String job_id, String job_type) {


        super(context);
        this.data = data;
        this.context = context;
        this.listener = listener;
        this.job_id = job_id;
        this.job_type = job_type;


        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.dialog_gas_payment, null);


        addView(v);

        init();
    }

    private void init() {
//        Toast.makeText(getContext().getApplicationContext() , "5 : ViewgasPayment   "+String.valueOf(data.get("job_id")),Toast.LENGTH_LONG).show();
        aq = new AQuery((Activity) this.context, v);
        aq.id(R.id.gaspayment_btn_ok).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder adb = new AlertDialog.Builder(context);


//                adb.setView(R.layout.dialog_message);


                adb.setTitle(getResources().getText(R.string.confirm_gas));


                adb.setIcon(android.R.drawable.ic_dialog_alert);


                adb.setPositiveButton(getResources().getText(R.string.confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if(String.valueOf(aq.id(R.id.gaspayment_edit_amount)) != null && !String.valueOf(aq.id(R.id.gaspayment_edit_amount).getText()).toString().isEmpty()
                                && String.valueOf(aq.id(R.id.gaspayment_edit_gas)) != null && !String.valueOf(aq.id(R.id.gaspayment_edit_gas).getText()).toString().isEmpty()
                                && String.valueOf(aq.id(R.id.input_km_gas)) != null && !String.valueOf(aq.id(R.id.input_km_gas).getText()).toString().isEmpty())
                        {

                            String url = APIs.getTripCostAPI(job_id, "", "", job_type, "", "", String.valueOf(aq.id(R.id.gaspayment_edit_amount).getText()), String.valueOf(aq.id(R.id.gaspayment_edit_gas).getText()), "", String.valueOf(aq.id(R.id.input_km_gas).getText()));
                            aq.ajax(url, String.class, new AjaxCallback<String>() {


                            });

                            // if(!String.valueOf(aq.id(R.id.gaspayment_edit_amount).getText()).trim().equalsIgnoreCase("") && !String.valueOf(aq.id(R.id.gaspayment_edit_gas).getText()).trim().equalsIgnoreCase("")) {
                            data.put("job_id", job_id);
                            data.put("job_type", job_type);
                            data.put("total_cost", String.valueOf(aq.id(R.id.gaspayment_edit_amount).getText()));
                            data.put("fare", String.valueOf(aq.id(R.id.gaspayment_edit_gas).getText()));
                            data.put("km", String.valueOf(aq.id(R.id.input_km_gas).getText()));

                            if (listener != null)
                                listener.viewGasPaymentAccept(data);
                        }
                        else {
                            Toast.makeText(context.getApplicationContext(),R.string.alert_input,
                                    Toast.LENGTH_LONG).show();
                        }


                    } });


                adb.setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    } });
                adb.show();



//                if(String.valueOf(aq.id(R.id.gaspayment_edit_amount)) != null && !String.valueOf(aq.id(R.id.gaspayment_edit_amount).getText()).toString().isEmpty()
//                    && String.valueOf(aq.id(R.id.gaspayment_edit_gas)) != null && !String.valueOf(aq.id(R.id.gaspayment_edit_gas).getText()).toString().isEmpty()
//                        && String.valueOf(aq.id(R.id.input_km_gas)) != null && !String.valueOf(aq.id(R.id.input_km_gas).getText()).toString().isEmpty())
//                {
//
//                    String url = APIs.getTripCostAPI(job_id, "", "", job_type, "", "", String.valueOf(aq.id(R.id.gaspayment_edit_amount).getText()), String.valueOf(aq.id(R.id.gaspayment_edit_gas).getText()), "", String.valueOf(aq.id(R.id.input_km_gas).getText()));
//                    aq.ajax(url, String.class, new AjaxCallback<String>() {
//
//
//                    });
//
//                    // if(!String.valueOf(aq.id(R.id.gaspayment_edit_amount).getText()).trim().equalsIgnoreCase("") && !String.valueOf(aq.id(R.id.gaspayment_edit_gas).getText()).trim().equalsIgnoreCase("")) {
//                    data.put("job_id", job_id);
//                    data.put("job_type", job_type);
//                    data.put("total_cost", String.valueOf(aq.id(R.id.gaspayment_edit_amount).getText()));
//                    data.put("fare", String.valueOf(aq.id(R.id.gaspayment_edit_gas).getText()));
//                    data.put("km", String.valueOf(aq.id(R.id.input_km_gas).getText()));
//
//                    if (listener != null)
//                        listener.viewGasPaymentAccept(data);
//                }
//                else {
//                    Toast.makeText(context.getApplicationContext(),R.string.alert_input,
//                            Toast.LENGTH_LONG).show();
//                }



            }
        });
    }

    public interface ViewGasPaymentListener {

        void viewGasPaymentAccept(HashMap<String, Object> data);
    }
}
