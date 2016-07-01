package com.infratrans.taxi.onboard.shares;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;

import java.util.HashMap;

public class AppSharedPreferences {

    public static final String STATE_SETTING_REMEMBER_APP_GLOBAL = "com.infratrans.taxi.onboard.remember.app.global";
    public static final String STATE_SETTING_MAPMODE = "com.infratrans.taxi.onboard.shares.mapmode";
    public static final String STATE_SETTING_PARKMARKER = "com.infratrans.taxi.onboard.shares.park";
    public static final String APP_LATITUDE = "com.infratrans.taxi.onboard.shares.latitude";
    public static final String APP_LONGITUDE = "com.infratrans.taxi.onboard.shares.longitude";
    public static final String STATE_JOB_TYPE = "com.infratrans.taxi.onboard.shares.jobtype";
    public static final String STATE_JOB_ID = "com.infratrans.taxi.onboard.shares.job_id";
    public static final String STATE_ON_JOB = "com.infratrans.taxi.onboard.shares.onjob";
    public static final String STATE_THUMBING_MODE = "com.infratrans.taxi.onboard.shares.thumbing_mode";
    public static final String STATE_STEP1 = "com.infratrans.taxi.onboard.shares.step1";
    public static final String STATE_STEP1_MANUAL = "com.infratrans.taxi.onboard.shares.step1_manual";
    public static final String STATE_STEP1_RESERVE_ID = "com.infratrans.taxi.onboard.shares.reserveid";
    public static final String STATE_STEP1_PICK_LATITUDE = "com.infratrans.taxi.onboard.shares.pick_latitude";
    public static final String STATE_STEP1_PICK_LONGITUDE = "com.infratrans.taxi.onboard.shares.pick_longitude";
    public static final String STATE_STEP1_PICK_TITLE = "com.infratrans.taxi.onboard.shares.pick_title";
    public static final String STATE_STEP1_DESTINATION_LATITUDE = "com.infratrans.taxi.onboard.shares.destination_latitude";
    public static final String STATE_STEP1_DESTINATION_LONGITUDE = "com.infratrans.taxi.onboard.shares.destination_longitude";
    public static final String STATE_STEP1_DESTIONATION_TITLE = "com.infratrans.taxi.onboard.shares.destination_title";
    public static final String STATE_STEP1_CUSTOMER_NAME = "com.infratrans.taxi.onboard.shares.customer_name";
    public static final String STATE_STEP1_CUSTOMER_PIC = "com.infratrans.taxi.onboard.shares.customer_pic";
    public static final String STATE_STEP1_PICK_CUSTOMER = "com.infratrans.taxi.onboard.shares.pick_customer";
    public static final String STATE_STEP2 = "com.infratrans.taxi.onboard.shares.step2";
    public static final String STATE_STEP3 = "com.infratrans.taxi.onboard.shares.step3";
    public static final String STATE_HUB_NAME = "com.infratrans.taxi.onboard.shares.hubname";
    public static final String STATE_HUB_LAT = "com.infratrans.taxi.onboard.shares.hublat";
    public static final String STATE_HUB_LNG = "com.infratrans.taxi.onboard.shares.hublng";
    public static final String STATE_HUB_GAS_PAYMENT = "com.infratrans.taxi.onboard.shares.gas_payment";
    public static final String STATE_COST_KM = "com.infratrans.taxi.onboard.shares.cost.km";
    public static final String STATE_COST_FARE = "com.infratrans.taxi.onboard.shares.cost.fare";
    public static final String STATE_COST_TRIP_TIME = "com.infratrans.taxi.onboard.shares.cost.trip_time";
    public static final String STATE_COST_BOOKING_BY = "com.infratrans.taxi.onboard.shares.cost.booking_by";
    public static final String STATE_CONNECTION = "com.infratrans.taxi.onboard.shares.connection";
    public static final String STATE_RESERVED_TYPE = "com.infratrans.taxi.onboard.share.reserved_type";
    public static final String STATE_CREDIT_CARD = "com.infratrans.taxi.onboard.share.enable_credit_card";
    public static final String STATE_KICK_TIME = "com.infratrans.taxi.onboard.share.kicktime";
    public static final String STATE_SERVICE_DIS = "com.infratrans.taxi.onboard.share.service_discount";
    public static final String STATE_FARE_DIS = "com.infratrans.taxi.onboard.share.fare_discount";
    public static final String STATE_TOTAL_DIS = "com.infratrans.taxi.onboard.share.total_discount";
    public static final String STATE_ONJOB = "com.infratrans.taxi.onboard.share.time_onjob";
    public static final String STATE_STANDBY = "com.infratrans.taxi.onboard.share.time_standby";
    public static final String STATE_BAHT_DAY = "com.infratrans.taxi.onboard.share.baht_day";
    public static final String STATE_BAHT_HOUR = "com.infratrans.taxi.onboard.share.baht_hour";
    public static final String STATE_API_KM = "com.infratrans.taxi.onboard.share.api_km";
    public static final String STATE_LOGIN_KM = "com.infratrans.taxi.onboard.share.login_km";
    public static final String SHIFT_START = "com.infratrans.taxi.onboard.share.shiftstart";
    public static final String REMEMBER_USER = "com.infratrans.taxi.onboard.share.remember_user";
    static private AppSharedPreferences instance;
    private final Context ctx;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private AppSharedPreferences(Context ctx) {

        this.ctx = ctx;
        sharedPref = ((Activity) this.ctx).getSharedPreferences(STATE_SETTING_REMEMBER_APP_GLOBAL, Context.MODE_PRIVATE);
    }

    static public AppSharedPreferences getInstance(Context ctx) {

        if (instance == null)
            instance = new AppSharedPreferences(ctx);

        return instance;
    }

    public int getMapMode() {

        return sharedPref.getInt(STATE_SETTING_MAPMODE, GoogleMap.MAP_TYPE_NORMAL);
    }

    public void setMapMode(int mode) {

        editor = sharedPref.edit();
        editor.putInt(STATE_SETTING_MAPMODE, mode);
        editor.commit();
    }

    public boolean getPickCustomer() {

        return sharedPref.getBoolean(STATE_STEP1_PICK_CUSTOMER, false);
    }

    public void setPickCustomer(boolean enable) {
        editor = sharedPref.edit();
        editor.putBoolean(STATE_STEP1_PICK_CUSTOMER, enable);
        editor.commit();
    }

    public void removePickCustomer() {

        editor = sharedPref.edit();
        editor.remove(STATE_STEP1_PICK_CUSTOMER);
        editor.commit();
    }

    public boolean getParkMarker() {

        return sharedPref.getBoolean(STATE_SETTING_PARKMARKER, true);
    }

    public void setParkMarker(boolean enabled) {

        editor = sharedPref.edit();
        editor.putBoolean(STATE_SETTING_PARKMARKER, enabled);
        editor.commit();
    }

    public void setLocation(String latitude, String longitude) {

        editor = sharedPref.edit();
        editor.putString(APP_LATITUDE, latitude);
        editor.putString(APP_LONGITUDE, longitude);
        editor.commit();
    }

    public String[] getLocation() {

        String[] location = new String[2];
        location[0] = sharedPref.getString(APP_LATITUDE, "13.829338");
        location[1] = sharedPref.getString(APP_LONGITUDE, "100.554813");
        return location;
    }

    public boolean getOnJob() {

        return sharedPref.getBoolean(STATE_ON_JOB, false);
    }

    public void setOnJob(boolean enabled) {

        editor = sharedPref.edit();
        editor.putBoolean(STATE_ON_JOB, enabled);
        editor.commit();
    }

    public void removeOnJob() {

        editor = sharedPref.edit();
        editor.remove(STATE_ON_JOB);
        editor.commit();
    }

    public boolean getThumbingMode() {

        return sharedPref.getBoolean(STATE_THUMBING_MODE, false);
    }

    public void setThumbingMode(boolean enabled) {

        editor = sharedPref.edit();
        editor.putBoolean(STATE_THUMBING_MODE, enabled);
        editor.commit();
    }

    public void removeThumbingMode() {

        editor = sharedPref.edit();
        editor.remove(STATE_THUMBING_MODE);
        editor.commit();
    }



    public String getReserved_Type()
    {
        return sharedPref.getString(STATE_RESERVED_TYPE, "0");
    }
    public String getEnable_Credit()
    {
        return sharedPref.getString(STATE_CREDIT_CARD,"0");
    }

    public String getStateFareDis()
    {
        return sharedPref.getString(STATE_FARE_DIS,"0");
    }
    public String getStateServiceDis()
    {
        return sharedPref.getString(STATE_SERVICE_DIS,"0");
    }
    public String getStateTotalDis()
    {
        return sharedPref.getString(STATE_TOTAL_DIS, "0");
    }



    public void setStateShiftStart(int shiftstart) {

        editor = sharedPref.edit();
        editor.putInt(SHIFT_START, shiftstart);
        editor.commit();
    }
    public int getStateShiftStart()
    {
        return sharedPref.getInt(SHIFT_START, 0);
    }
    public void removeShiftStart(){
        editor = sharedPref.edit();
        editor.remove(SHIFT_START);
        editor.commit();
    }



    public void setStateLoginKM(float login_km) {

        editor = sharedPref.edit();
        editor.putFloat(STATE_LOGIN_KM, login_km);
        editor.commit();
    }
    public float getStateLoginKM()
    {
        return sharedPref.getFloat(STATE_LOGIN_KM, 0);
    }
    public void removeLoginKM(){
        editor = sharedPref.edit();
        editor.remove(STATE_LOGIN_KM);
        editor.commit();
    }

    public void setStateApiKm(float api_km) {

        editor = sharedPref.edit();
        editor.putFloat(STATE_API_KM, api_km);
        editor.commit();

    }
    public float getStateApiKm()
    {
        return sharedPref.getFloat(STATE_API_KM, 0);
    }
    public void removeApiKm(){
        editor = sharedPref.edit();
        editor.remove(STATE_API_KM);
        editor.commit();
    }


    public void setRememberUser(String user) {

        editor = sharedPref.edit();
        editor.putString(REMEMBER_USER, user);
        editor.commit();

    }
    public String getRememberUser()
    {
        return sharedPref.getString(REMEMBER_USER, "");
    }





    public void setStateBahtHour(float baht_day) {

        editor = sharedPref.edit();
        editor.putFloat(STATE_BAHT_HOUR, baht_day);
        editor.commit();
    }
    public float getStateBahtHour()
    {
        return sharedPref.getFloat(STATE_BAHT_HOUR, 0);
    }
    public void removeStateBahtHour(){
        editor = sharedPref.edit();
        editor.remove(STATE_BAHT_HOUR);
        editor.commit();
    }


    public void setStateBahtDay(float baht_day) {

        editor = sharedPref.edit();
        editor.putFloat(STATE_BAHT_DAY, baht_day);
        editor.commit();
    }
    public float getStateBahtDay()
    {
        return sharedPref.getFloat(STATE_BAHT_DAY, 0);
    }
    public void removeStateBahtDay(){
        editor = sharedPref.edit();
        editor.remove(STATE_BAHT_DAY);
        editor.commit();
    }


    public void setStateOnjob(int onjob) {

        editor = sharedPref.edit();
        editor.putInt(STATE_ONJOB, onjob);
        editor.commit();
    }
    public int getStateOnjob()
    {
        return sharedPref.getInt(STATE_ONJOB, 0);
    }
    public void removeStateOnjob(){
        editor = sharedPref.edit();
        editor.remove(STATE_ONJOB);
        editor.commit();
    }
    public void setStateStandby(int standby) {

        editor = sharedPref.edit();
        editor.putInt(STATE_STANDBY, standby);
        editor.commit();
    }
    public int getStateStandby()
    {
        return sharedPref.getInt(STATE_STANDBY, 0);
    }

    public void removeStateStandby(){
        editor = sharedPref.edit();
        editor.remove(STATE_STANDBY);
        editor.commit();
    }



    public void setStateFareDis(String fare_dis) {

        editor = sharedPref.edit();
        editor.putString(STATE_FARE_DIS, fare_dis);
        editor.commit();
    }
    public void setStateServiceDis(String service_dis) {

        editor = sharedPref.edit();
        editor.putString(STATE_SERVICE_DIS, service_dis);
        editor.commit();
    }
    public void setStateTotalDis(String total_dis) {

        editor = sharedPref.edit();
        editor.putString(STATE_TOTAL_DIS, total_dis);
        editor.commit();
    }


    public void setReservedType(String reserved_type) {

        editor = sharedPref.edit();
        editor.putString(STATE_RESERVED_TYPE, reserved_type);
        editor.commit();
    }
    public void setEnableCard(String enable_credit_card) {

        editor = sharedPref.edit();
        editor.putString(STATE_CREDIT_CARD, enable_credit_card);
        editor.commit();
    }




    public String getStateKickTime() {

        return sharedPref.getString(STATE_KICK_TIME, "");
    }

    public void setStateKickTime(String kickTime) {

        editor = sharedPref.edit();
        editor.putString(STATE_KICK_TIME, kickTime);
        editor.commit();
    }

    public void removeStateKickTime(){
        editor = sharedPref.edit();
        editor.remove(STATE_KICK_TIME);
        editor.commit();
    }




    public String getJobType() {

        return sharedPref.getString(STATE_JOB_TYPE, "");
    }

    public void setJobType(String job_type) {

        editor = sharedPref.edit();
        editor.putString(STATE_JOB_TYPE, job_type);
        editor.commit();
    }

    public void removeJobType() {

        editor = sharedPref.edit();
        editor.remove(STATE_JOB_TYPE);
        editor.commit();
    }

    public String getJobID() {

        return sharedPref.getString(STATE_JOB_ID, "");
    }

    public void setJobID(String job_id) {

        editor = sharedPref.edit();
        editor.putString(STATE_JOB_ID, job_id);
        editor.commit();
    }

    public String getReservedID() {

        return sharedPref.getString(STATE_STEP1_RESERVE_ID, "");
    }
    public void removeReservedID(){
        editor = sharedPref.edit();
        editor.remove(STATE_STEP1_RESERVE_ID);
        editor.commit();

    }

    public void removeJobID() {

        editor = sharedPref.edit();
        editor.remove(STATE_JOB_ID);
        editor.commit();
    }

    public boolean getOnState1() {

        return sharedPref.getBoolean(STATE_STEP1, false);
    }

    public void setOnState1(boolean enabled) {

        editor = sharedPref.edit();
        editor.putBoolean(STATE_STEP1, enabled);
        editor.commit();
    }

    public void removeOnState1() {

        editor = sharedPref.edit();
        editor.remove(STATE_STEP1);
        editor.commit();
    }

    public HashMap<String, Object> getState_1() {

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("reserve_id", sharedPref.getString(STATE_STEP1_RESERVE_ID, ""));
        data.put("pick_lat", sharedPref.getString(STATE_STEP1_PICK_LATITUDE, ""));
        data.put("pick_lng", sharedPref.getString(STATE_STEP1_PICK_LONGITUDE, ""));
        data.put("dest_lat", sharedPref.getString(STATE_STEP1_DESTINATION_LATITUDE, ""));
        data.put("dest_lng", sharedPref.getString(STATE_STEP1_DESTINATION_LONGITUDE, ""));
        data.put("cust_name", sharedPref.getString(STATE_STEP1_CUSTOMER_NAME, ""));
        data.put("cust_pic", sharedPref.getString(STATE_STEP1_CUSTOMER_PIC, ""));
        data.put("location_title", sharedPref.getString(STATE_STEP1_PICK_TITLE, ""));
        data.put("destination_title", sharedPref.getString(STATE_STEP1_DESTIONATION_TITLE, ""));
        return data;
    }

    public void setState_1(HashMap<String, Object> data) {

        editor = sharedPref.edit();
        editor.putString(STATE_STEP1_RESERVE_ID, String.valueOf(data.get("reserved_id")));
        editor.putString(STATE_STEP1_PICK_LATITUDE, String.valueOf(data.get("pick_lat")));
        editor.putString(STATE_STEP1_PICK_LONGITUDE, String.valueOf(data.get("pick_lng")));
        editor.putString(STATE_STEP1_PICK_TITLE, String.valueOf(data.get("location_title")));
        editor.putString(STATE_STEP1_DESTINATION_LATITUDE, String.valueOf(data.get("dest_lat")));
        editor.putString(STATE_STEP1_DESTINATION_LONGITUDE, String.valueOf(data.get("dest_lng")));
        editor.putString(STATE_STEP1_DESTIONATION_TITLE, String.valueOf(data.get("destination_title")));
        editor.putString(STATE_STEP1_CUSTOMER_NAME, String.valueOf(data.get("cust_name")));
        editor.putString(STATE_STEP1_CUSTOMER_PIC, String.valueOf(data.get("cust_pic")));
        editor.commit();
    }

    public void removeState_1() {

        editor = sharedPref.edit();
        editor.remove(STATE_STEP1_RESERVE_ID);
        editor.remove(STATE_STEP1_PICK_LATITUDE);
        editor.remove(STATE_STEP1_PICK_LONGITUDE);
        editor.remove(STATE_STEP1_DESTINATION_LATITUDE);
        editor.remove(STATE_STEP1_DESTINATION_LONGITUDE);
        editor.remove(STATE_STEP1_CUSTOMER_NAME);
        editor.remove(STATE_STEP1_CUSTOMER_PIC);
        editor.remove(STATE_STEP1_PICK_TITLE);
        editor.remove(STATE_STEP1_DESTIONATION_TITLE);
        editor.commit();
    }

    public boolean getOnState2() {

        return sharedPref.getBoolean(STATE_STEP2, false);
    }

    public void setOnState2(boolean enabled) {

        editor = sharedPref.edit();
        editor.putBoolean(STATE_STEP2, enabled);
        editor.commit();
    }

    public void removeOnState2() {

        editor = sharedPref.edit();
        editor.remove(STATE_STEP2);
        editor.commit();
    }

    public boolean getOnState3() {

        return sharedPref.getBoolean(STATE_STEP3, false);
    }

    public void setOnState3(boolean enabled) {

        editor = sharedPref.edit();
        editor.putBoolean(STATE_STEP3, enabled);
        editor.commit();
    }

    public void removeOnState3() {

        editor = sharedPref.edit();
        editor.remove(STATE_STEP3);
        editor.commit();
    }

    public HashMap<String, Object> getState_Hub() {

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("hub_name", sharedPref.getString(STATE_HUB_NAME, ""));
        data.put("hub_lat", sharedPref.getString(STATE_HUB_LAT, ""));
        data.put("hub_lng", sharedPref.getString(STATE_HUB_LNG, ""));
        return data;
    }

    public void setState_Hub(HashMap<String, Object> data) {

        editor = sharedPref.edit();
        editor.putString(STATE_HUB_NAME, String.valueOf(data.get("hub_name")));
        editor.putString(STATE_HUB_LAT, String.valueOf(data.get("hub_lat")));
        editor.putString(STATE_HUB_LNG, String.valueOf(data.get("hub_lng")));
        editor.commit();
    }

    public void removeState_Hub() {

        editor = sharedPref.edit();
        editor.remove(STATE_HUB_NAME);
        editor.remove(STATE_HUB_LAT);
        editor.remove(STATE_HUB_LNG);
        editor.commit();
    }

    public HashMap<String, Object> getState_Payment() {

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("km", sharedPref.getString(STATE_COST_KM, ""));
        data.put("fare", sharedPref.getString(STATE_COST_FARE, ""));
        data.put("trip_time", sharedPref.getString(STATE_COST_TRIP_TIME, ""));
        data.put("booking_by", sharedPref.getString(STATE_COST_BOOKING_BY, ""));
        return data;
    }

    public void setState_Payment(HashMap<String, Object> data) {

        editor = sharedPref.edit();
        editor.putString(STATE_COST_KM, String.valueOf(data.get("km")));
        editor.putString(STATE_COST_FARE, String.valueOf(data.get("fare")));
        editor.putString(STATE_COST_TRIP_TIME, String.valueOf(data.get("trip_time")));
        editor.putString(STATE_COST_BOOKING_BY, String.valueOf(data.get("booking_by")));
        editor.commit();
    }

    public void removeState_Payment() {

        editor = sharedPref.edit();
        editor.remove(STATE_COST_KM);
        editor.remove(STATE_COST_FARE);
        editor.remove(STATE_COST_TRIP_TIME);
        editor.remove(STATE_COST_BOOKING_BY);
        editor.commit();
    }

    public boolean getOnGasStaion() {

        return sharedPref.getBoolean(STATE_HUB_GAS_PAYMENT, false);
    }

    public void setOnGasStaion(boolean enabled) {

        editor = sharedPref.edit();
        editor.putBoolean(STATE_HUB_GAS_PAYMENT, enabled);
        editor.commit();
    }

    public void removeOnGasStaion() {

        editor = sharedPref.edit();
        editor.remove(STATE_HUB_GAS_PAYMENT);
        editor.commit();
    }

    public boolean getStateConnection() {

        return sharedPref.getBoolean(STATE_CONNECTION, false);
    }

    public void setStateConnection(boolean enabled) {

        editor = sharedPref.edit();
        editor.putBoolean(STATE_CONNECTION, enabled);
        editor.commit();
    }

    public void removeStateConnection() {

        editor = sharedPref.edit();
        editor.remove(STATE_CONNECTION);
        editor.commit();
    }

}
