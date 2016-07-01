package com.infratrans.taxi.onboard.shares;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class LoginHelper {

    public static final String STATE_LOGIN_REMEMBER_GROUP = "com.infratrans.taxi.onboard.remember.app.login";
    public static final String STATE_LOGIN_REMEMBER_ISLOGIN = "com.infratrans.taxi.onboard.remember.app.login.islogin";
    public static final String STATE_LOGIN_REMEMBER_TAXI_ID = "com.infratrans.taxi.onboard.remember.app.login.taxi_id";
    public static final String STATE_LOGIN_REMEMBER_OPERATION_ID = "com.infratrans.taxi.onboard.remember.app.login.operation_id";
    public static final String STATE_LOGIN_REMEMBER_STAFFID_01 = "com.infratrans.taxi.onboard.remember.app.id_01";
    public static final String STATE_LOGIN_REMEMBER_STAFFNAME_01 = "com.infratrans.taxi.onboard.remember.app.name_01";
    public static final String STATE_LOGIN_REMEMBER_STAFFGENDER_01 = "com.infratrans.taxi.onboard.remember.app.gender_01";
    public static final String STATE_LOGIN_REMEMBER_STAFFMOBILE_01 = "com.infratrans.taxi.onboard.remember.app.mobile_01";
    public static final String STATE_LOGIN_REMEMBER_STAFFAVATAR_01 = "com.infratrans.taxi.onboard.remember.app.avatar_01";
    public static final String STATE_LOGIN_REMEMBER_STAFFDATE_01 = "com.infratrans.taxi.onboard.remember.app.date_01";
    static private LoginHelper instance;
    private final Context ctx;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;


    private LoginHelper(Context ctx) {

        this.ctx = ctx;
        sharedPref = ((Activity) this.ctx).getSharedPreferences(STATE_LOGIN_REMEMBER_GROUP, Context.MODE_PRIVATE);
    }

    static public LoginHelper getInstance(Context ctx) {

        if (instance == null)
            instance = new LoginHelper(ctx);

        return instance;
    }

    public void rememberLogin(HashMap<String, Object> officer) {

        editor = sharedPref.edit();
        editor.putBoolean(STATE_LOGIN_REMEMBER_ISLOGIN, true);
        editor.putString(STATE_LOGIN_REMEMBER_TAXI_ID, String.valueOf(officer.get("taxi_id")));
        editor.putString(STATE_LOGIN_REMEMBER_OPERATION_ID, String.valueOf(officer.get("operation_id")));
        editor.putString(STATE_LOGIN_REMEMBER_STAFFID_01, String.valueOf(officer.get("driver_id")));
        editor.putString(STATE_LOGIN_REMEMBER_STAFFNAME_01, String.valueOf(officer.get("driver_name")));
        editor.putString(STATE_LOGIN_REMEMBER_STAFFGENDER_01, String.valueOf(officer.get("gender")));
        editor.putString(STATE_LOGIN_REMEMBER_STAFFMOBILE_01, String.valueOf(officer.get("mobile")));
        editor.putString(STATE_LOGIN_REMEMBER_STAFFAVATAR_01, String.valueOf(officer.get("display_pic")));
        editor.putString(STATE_LOGIN_REMEMBER_STAFFDATE_01, String.valueOf(officer.get("date")));
        editor.commit();
    }

    public boolean checkLogin() {

        return sharedPref.getBoolean(STATE_LOGIN_REMEMBER_ISLOGIN, false);
    }

    public String getTaxiID() {

        return sharedPref.getString(STATE_LOGIN_REMEMBER_TAXI_ID, "");
    }

    public String getOperationID() {

        return sharedPref.getString(STATE_LOGIN_REMEMBER_OPERATION_ID, "");
    }

    public String getDriverID() {

        return sharedPref.getString(STATE_LOGIN_REMEMBER_STAFFID_01, "");
    }

    public HashMap<String, Object> getRememberLogin() {

        HashMap<String, Object> item = new HashMap<String, Object>();
        item.put("taxi_id", sharedPref.getString(STATE_LOGIN_REMEMBER_TAXI_ID, ""));
        item.put("staff_id", sharedPref.getString(STATE_LOGIN_REMEMBER_STAFFID_01, ""));
        item.put("operation_id", sharedPref.getString(STATE_LOGIN_REMEMBER_OPERATION_ID, ""));
        item.put("staff_name", sharedPref.getString(STATE_LOGIN_REMEMBER_STAFFNAME_01, ""));
        item.put("gender", sharedPref.getString(STATE_LOGIN_REMEMBER_STAFFGENDER_01, ""));
        item.put("mobile", sharedPref.getString(STATE_LOGIN_REMEMBER_STAFFMOBILE_01, ""));
        item.put("display_pic", sharedPref.getString(STATE_LOGIN_REMEMBER_STAFFAVATAR_01, ""));
        item.put("date", sharedPref.getString(STATE_LOGIN_REMEMBER_STAFFDATE_01, ""));
        return item;
    }

    public void deleteRememberLogin() {

        editor = sharedPref.edit();
        editor.remove(STATE_LOGIN_REMEMBER_ISLOGIN);
        editor.remove(STATE_LOGIN_REMEMBER_STAFFID_01);
        editor.remove(STATE_LOGIN_REMEMBER_STAFFNAME_01);
        editor.remove(STATE_LOGIN_REMEMBER_STAFFGENDER_01);
        editor.remove(STATE_LOGIN_REMEMBER_STAFFAVATAR_01);
        editor.remove(STATE_LOGIN_REMEMBER_STAFFMOBILE_01);
        editor.remove(STATE_LOGIN_REMEMBER_STAFFDATE_01);
        editor.remove(STATE_LOGIN_REMEMBER_OPERATION_ID);
        editor.commit();
    }

}
