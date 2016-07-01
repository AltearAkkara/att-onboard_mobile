package com.infratrans.taxi.onboard.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.infratrans.taxi.onboard.HomeActivity;
import com.infratrans.taxi.onboard.R;
import com.infratrans.taxi.onboard.Status;
import com.infratrans.taxi.onboard.shares.AppSharedPreferences;
import com.infratrans.taxi.onboard.util.APIs;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by Thanisak Piyasaksiri on 2/7/15 AD.
 * Modified by Thanisak Piyasaksiri on 3/31/15 AD.
 */
public class ViewTripReport extends RelativeLayout {

    private static ViewTripReport instance;

    DecimalFormat format = new DecimalFormat("###.#");
    MyCount timerCount;
    private AQuery aq;
    private Context context;
    private HashMap<String, Object> data;
    private View v = null;
    private LayoutInflater vi = null;
    private ViewTripReportListener listener;
    private Double km = 0.0;
    private int service_charge = 0;
    private int enable = 0;
    private int reserved = 0;


//    public void ChangeText(String Check){
//        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/myFont.ttf");
//        TextView myTextView = (TextView)findViewById(R.id.payment_desc_1);
//        myTextView.setTypeface(myTypeface);
//    }

    private MediaPlayer mp = null;
    private void playAudio(int resId)
    {
        if(mp != null) {
            if (!mp.isPlaying()) {
                mp = MediaPlayer.create(context, resId);
                mp.start();

            } else if (mp.isPlaying()) {
                mp.stop();
                mp = null;
                mp = MediaPlayer.create(context, resId);
                mp.start();
            }
        } else {
            mp = MediaPlayer.create(context, resId);
            mp.start();
        }

    }

    public ViewTripReport(Context context, HashMap<String, Object> data, ViewTripReportListener listener) {

        super(context);
        this.data = data;
        this.context = context;
        this.listener = listener;




        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.dialog_payment, null);
        addView(v);

        init();

    }

    public Map getData()
    {
        return data;
    }
    private void setProgressbarVisibility(boolean visibility) {

        if (visibility) {
            aq.id(R.id.home_loading_frame).getView().setVisibility(View.VISIBLE);
        } else
            aq.id(R.id.home_loading_frame).getView().setVisibility(View.GONE);
    }

    private void init() {

        aq = new AQuery((Activity) this.context, v);

        aq.id(R.id.payment_btn_money).visible();

        TextView dis_service = (TextView)findViewById(R.id.dis_service);
        TextView dis_fare = (TextView)findViewById(R.id.dis_fare);
        TextView dis_total = (TextView)findViewById(R.id.dis_total);
        TextView unit1 = (TextView)findViewById(R.id.unit1);
        TextView unit2 = (TextView)findViewById(R.id.unit2);
        TextView unit3 = (TextView)findViewById(R.id.unit3);
        if(!String.valueOf(data.get("service_discount")).equalsIgnoreCase("")&&!String.valueOf(data.get("service_discount")).equalsIgnoreCase("null") && data.get("service_discount")!=null) {
            if(!String.valueOf(data.get("service_discount")).contains("%")) {
                dis_service.setText(String.valueOf(data.get("service_discount")));
                unit1.setText(getResources().getString(R.string.payment_desc_11));
            }
            else{
                dis_service.setText(String.valueOf(data.get("service_discount")));
            }
        }
        else{
            dis_service.setText("0");
            unit1.setText(getResources().getString(R.string.payment_desc_11));
        }

        if(!String.valueOf(data.get("fare_discount")).equalsIgnoreCase("") && !String.valueOf(data.get("fare_discount")).equalsIgnoreCase("null") && data.get("fare_discount")!=null) {
            if(!String.valueOf(data.get("fare_discount")).contains("%")) {
                dis_fare.setText(String.valueOf(data.get("fare_discount")));
                unit2.setText(getResources().getString(R.string.payment_desc_11));
            }
            else {
                dis_fare.setText(String.valueOf(data.get("fare_discount")));
            }
        }
        else {
            dis_fare.setText("0");
            unit2.setText(getResources().getString(R.string.payment_desc_11));
        }

        if(!String.valueOf(data.get("total_discount")).equalsIgnoreCase("") && !String.valueOf(data.get("total_discount")).equalsIgnoreCase("null")&& data.get("total_discount")!=null) {
            if(!String.valueOf(data.get("total_discount")).contains("%")) {
                dis_total.setText(String.valueOf(data.get("total_discount")));
                unit3.setText(getResources().getString(R.string.payment_desc_11));
            }
            else{
                dis_total.setText(String.valueOf(data.get("total_discount")));
            }
        }
        else{
            dis_total.setText("0");
            unit3.setText(getResources().getString(R.string.payment_desc_11));
        }
        ImageView credit = (ImageView) findViewById(R.id.payment_btn_creditcard);
        credit.setVisibility(View.VISIBLE);
        ImageView cash = (ImageView) findViewById(R.id.payment_btn_money);
        cash.setVisibility(View.VISIBLE);
        Status.checkPaymentAccept = true;

        enable = Integer.parseInt(String.valueOf(data.get("enable_credit_card")));
//        enable = 0;
        reserved = Integer.parseInt(String.valueOf(data.get("reserved_type")));

        if(enable == 0){
            ImageView iv = (ImageView)v.findViewById(R.id.payment_btn_creditcard);
            iv.setVisibility(View.GONE);
        }
        if(String.valueOf(data.get("enable_credit_card"))==null || String.valueOf(data.get("enable_credit_card")).equals("")){
            ImageView iv = (ImageView)v.findViewById(R.id.payment_btn_creditcard);
            iv.setVisibility(View.GONE);
        }
        if (reserved == 1) {
            service_charge = 20;
//            if(enable ==1){
//                cash.setVisibility(View.GONE);
//            }
            ImageView imgview = (ImageView)findViewById(R.id.IconPayment);
            imgview.setBackgroundResource(R.drawable.icon_job_app);
        }
        else if (reserved == 2) {
            service_charge = 100;
            ImageView imgview = (ImageView)findViewById(R.id.IconPayment);
            imgview.setBackgroundResource(R.drawable.icon_job_reserved);
        }
        else{
            service_charge = 0;
            ImageView imgview = (ImageView)findViewById(R.id.IconPayment);
            imgview.setBackgroundResource(R.drawable.icon_job_thrumping);
        }

        km = Double.parseDouble(String.valueOf(data.get("km"))) / 1000;





        aq.id(R.id.payment_desc_1).enabled(true);
        aq.id(R.id.payment_desc_2).enabled(true);
        aq.id(R.id.payment_amount).enabled(true);
        aq.id(R.id.payment_desc_6).enabled(true);

        if (Status.enable) {
//            aq.id(R.id.payment_desc_1).text("1");
//            aq.id(R.id.payment_desc_2).text("1");
//            aq.id(R.id.payment_amount).text("35");



        } else {
            if(service_charge >0)
            {
                aq.id(R.id.payment_desc_6).enabled(false);
            }
            if(Double.parseDouble(String.valueOf(data.get("trip_time")))>0)
            {
                aq.id(R.id.payment_desc_1).enabled(false);
            }
            if(Double.parseDouble(String.valueOf(data.get("km")))>0)
            {
                aq.id(R.id.payment_desc_2).enabled(false);
            }
            if(Double.parseDouble(String.valueOf(data.get("fare")))>0)
            {
                aq.id(R.id.payment_amount).enabled(false);
            }
            // reserved can edit meter
            if(reserved == 2 || reserved == 1){
                aq.id(R.id.payment_amount).enabled(true);
            }

            aq.id(R.id.payment_desc_1).text((Double.parseDouble(String.valueOf(data.get("trip_time")).substring(0, String.valueOf(data.get("trip_time")).length() - 3))) + "");
            aq.id(R.id.payment_desc_2).text(format.format(km));
            aq.id(R.id.payment_amount).text(String.valueOf(data.get("fare")));
        }

        aq.id(R.id.payment_desc_5).text("0");
        aq.id(R.id.payment_desc_6).text(service_charge + "");


//        ((EditText) aq.id(R.id.payment_amount).getView()).addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                getTotalCost();
//            }
//        });

//        ((EditText) aq.id(R.id.payment_desc_5).getView()).addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                getTotalCost();
//            }
//        });


        aq.id(R.id.button3).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
               getTotalCost();
                TextView txt = (TextView)findViewById(R.id.textView12);
                txt.setText(String.valueOf(getDiscount())+ " บาท");
//                aq.id(R.id.button3).text(String.valueOf(getDiscount()));
            }
        });




        aq.id(R.id.payment_btn_money).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(R.raw.cash);
                String title = "ยืนยันข้อมูล" ;
                String message = "ต้องการยืนยันข้อมูลนี้ใช่หรือไม่ ?"+"\n"+"เวลา : "+String.valueOf(aq.id(R.id.payment_desc_1).getText()).toString()+" นาที "+"ระยะทาง : "+String.valueOf(aq.id(R.id.payment_desc_2).getText()).toString()+" กิโลเมตร "+"ค่าโดยสาร : " +String.valueOf(getDiscount())+" บาท";
                if(Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_1).getText()).toString())<= 0.1 || Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_2).getText()).toString())<= 0.1 || Double.parseDouble(String.valueOf(aq.id(R.id.payment_amount).getText()).toString()) <= 35){
                    title =  "ตรวจพบความผิดปกติของข้อมูล หากมีการร้องเรียน กรุณาชี้แจงต่อเจ้าหน้าที่";
                }
                new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                btn_payment_click(false);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


        aq.id(R.id.payment_btn_creditcard).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(R.raw.credit);
                String title = "ยืนยันข้อมูล" ;
                String s= getResources().getString(R.string.credit_name);
                String message = s+"\n"+"ต้องการยืนยันข้อมูลนี้ใช่หรือไม่ ?"+"\n"+"เวลา : "+String.valueOf(aq.id(R.id.payment_desc_1).getText()).toString()+" นาที "+"ระยะทาง : "+String.valueOf(aq.id(R.id.payment_desc_2).getText()).toString()+" กิโลเมตร "+"ค่าโดยสาร : " +String.valueOf(getDiscount())+" บาท";
                if(Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_1).getText()).toString())<= 0.1 || Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_2).getText()).toString())<= 0.1 || Double.parseDouble(String.valueOf(aq.id(R.id.payment_amount).getText()).toString()) <= 35){
                    title =  "ตรวจพบความผิดปกติของข้อมูล หากมีการร้องเรียน กรุณาชี้แจงต่อเจ้าหน้าที่";
                }

                new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("ตัดเงินผ่านบัตรเครดิต", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                btn_payment_click(true);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        Status.enable = false;
        Log.d("TAGGGG", Status.enable + "");
    }

    public void btn_payment_click(boolean isCard){
        //Toast.makeText(getContext(), "Force Click", Toast.LENGTH_LONG).show();

            Status.check = true;
            Status.checkPaymentAccept = false;


            String url = "";
            if (ViewTripReport.this.data == null) {
                url = "";
            } else {

                if(String.valueOf(aq.id(R.id.payment_desc_1)) != null && !String.valueOf(aq.id(R.id.payment_desc_1).getText()).toString().isEmpty()
                        && String.valueOf(aq.id(R.id.payment_desc_2)) != null && !String.valueOf(aq.id(R.id.payment_desc_2).getText()).toString().isEmpty()
                        && String.valueOf(aq.id(R.id.payment_amount)) != null && !String.valueOf(aq.id(R.id.payment_amount).getText()).toString().isEmpty()
                        && String.valueOf(aq.id(R.id.payment_desc_5)) != null && !String.valueOf(aq.id(R.id.payment_desc_5).getText()).toString().isEmpty()
                        && String.valueOf(aq.id(R.id.payment_desc_6)) != null && !String.valueOf(aq.id(R.id.payment_desc_6).getText()).toString().isEmpty()
                        && Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_1).getText()).toString())>= 0.1
                        && Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_2).getText()).toString())>= 0.1
                        && Double.parseDouble(String.valueOf(aq.id(R.id.payment_amount).getText()).toString()) >= 35
                        )
                {


                    km = Double.parseDouble(aq.id(R.id.payment_desc_2).getText().toString());


                    data.put("trip_time", aq.id(R.id.payment_desc_1).getText().toString());

                    url = APIs.getTripCostAPI(AppSharedPreferences.getInstance(context).getJobID(),
                            String.valueOf(format.format(km)),
                            String.valueOf(((int) Double.parseDouble(String.valueOf(data.get("trip_time"))))),
                            AppSharedPreferences.getInstance(context).getJobType(),
                            "1",
                            isCard?"2":"1",
                            String.valueOf(getDiscount()),
                            String.valueOf(aq.id(R.id.payment_amount).getText()),
                            String.valueOf(aq.id(R.id.payment_desc_5).getText()),
                            "");



                    setProgressbarVisibility(true);
                    timerCount = new MyCount(5000, 1000);
                    timerCount.start();


                    if (listener != null)
                        if(!isCard) {
                            getTotalCost();
                            listener.viewTripReportSendTripReport(url, String.valueOf(getDiscount()), "cash");
                            AppSharedPreferences.getInstance(context).setStateBahtDay((float) getDiscount() +  AppSharedPreferences.getInstance(context).getStateBahtDay());
                            AppSharedPreferences.getInstance(context).setStateBahtHour((float) getDiscount() + AppSharedPreferences.getInstance(context).getStateBahtHour());
                        }
                    else if(isCard){
                            getTotalCost();
                            listener.viewTripReportSendTripReport(url, String.valueOf(getDiscount()), "creditcard");
                            AppSharedPreferences.getInstance(context).setStateBahtDay((float) getDiscount() +  AppSharedPreferences.getInstance(context).getStateBahtDay());
                            AppSharedPreferences.getInstance(context).setStateBahtHour((float) getDiscount() + AppSharedPreferences.getInstance(context).getStateBahtHour());
                        }
                }
                else
                {
                    Toast.makeText(context.getApplicationContext(),R.string.alert_input1,
                            Toast.LENGTH_LONG).show();
                }
            }



    }


    private void getTotalCost() {

        double fare = 0;
        double total_cost = 0;
        double service = 0;

        try {
            service = Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_6).getText()));
            fare = Double.parseDouble(String.valueOf(aq.id(R.id.payment_amount).getText()));
        } catch (Exception e) {
            fare = 0;
            service = 0;
        }

        try {
            if (String.valueOf(aq.id(R.id.payment_desc_5).getText()) == "") {
                aq.id(R.id.payment_desc_5).text("0");
            }
            if(String.valueOf(aq.id(R.id.payment_desc_6).getText()) == ""){
                aq.id(R.id.payment_desc_6).text("0");
            }
            Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_5).getText()));
            total_cost = fare + Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_5).getText())) + service;
        } catch (Exception e) {
            total_cost = fare + Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_5).getText())) + service;

        }
        aq.id(R.id.payment_total_cost).text(String.valueOf(total_cost));

//        return total_cost;
    }

    private double getDiscount(){
        double total_discount = 0;
        double service_discount = 0;
        double fare_discount = 0;
        double total = 0;
        double service_discount_percent = 0;
        double fare_discount_percent = 0;
        double total_discount_percent = 0;

        try {
            if(String.valueOf(aq.id(R.id.payment_desc_6).getText()) != "" && String.valueOf(aq.id(R.id.payment_amount).getText()) != "" && String.valueOf(aq.id(R.id.payment_desc_6).getText()) != null && String.valueOf(aq.id(R.id.payment_amount).getText()) != null) {
                if (!aq.id(R.id.dis_service).getText().toString().contains("%")) {
                    service_discount = Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_6).getText())) - Double.parseDouble(String.valueOf(aq.id(R.id.dis_service).getText()));
                }
                else {
                    String a = aq.id(R.id.dis_service).getText().toString().replace("%", "");
                    service_discount_percent = Double.parseDouble(a);
                        service_discount = Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_6).getText())) - (Double.parseDouble(String.valueOf(aq.id(R.id.payment_desc_6).getText())) * service_discount_percent / 100);

                }
                if (!aq.id(R.id.dis_fare).getText().toString().contains("%")) {
                    fare_discount = Double.parseDouble(String.valueOf(aq.id(R.id.payment_amount).getText())) - Double.parseDouble(String.valueOf(aq.id(R.id.dis_fare).getText()));
                }
                else {
                    String a = aq.id(R.id.dis_fare).getText().toString().replace("%", "");
                    fare_discount_percent = Double.parseDouble(a);
                        fare_discount = Double.parseDouble(String.valueOf(aq.id(R.id.payment_amount).getText())) - (Double.parseDouble(String.valueOf(aq.id(R.id.payment_amount).getText())) * fare_discount_percent / 100);
                    }

                    if(fare_discount <0){
                        fare_discount = 0;
                    }
                    if(service_discount <0){
                        service_discount = 0;
                    }

                if (!aq.id(R.id.dis_total).getText().toString().contains("%")) {
                    total_discount = (fare_discount + service_discount) - Double.parseDouble(String.valueOf(aq.id(R.id.dis_total).getText()));
                }
                else {
                    String a = aq.id(R.id.dis_total).getText().toString().replace("%", "");
                    total_discount_percent = Double.parseDouble(a);
                    total_discount = (fare_discount + service_discount) - ((fare_discount + service_discount) * total_discount_percent / 100);

                }
            }
            else{
                Toast.makeText(getContext().getApplicationContext(),"ไม่สามารถคำนวณได้ เนื่องจากข้อมูลไม่ครบ",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(getContext().getApplicationContext(),"ไม่สามารถคำนวณได้ เนื่องจากข้อมูลไม่ครบ",Toast.LENGTH_LONG).show();

        }


//        Toast.makeText(context.getApplicationContext(),total_discount+"="+service_discount+"+"+fare_discount,Toast.LENGTH_LONG).show();



        return Math.round(total_discount);
    }

    public interface ViewTripReportListener {

        void viewTripReportSendTripReport(String url_callback, String cash , String check);
    }
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            setProgressbarVisibility(false);
            if (Status.check) {
                new AlertDialog.Builder(context)
                        .setTitle("Internet Interuption")
                        .setMessage("ข้อมูลยังไม่ถูกส่งโปรดส่งอีกครั้ง" + "\n" + "ยอดที่ผู้โดยสารต้องจ่าย : " + String.valueOf(getDiscount()))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //some script here
        }
    }
}
