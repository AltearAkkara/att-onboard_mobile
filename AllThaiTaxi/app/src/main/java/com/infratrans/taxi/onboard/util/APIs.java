package com.infratrans.taxi.onboard.util;

/**
 * Created by Thanisak Piyasaksiri on 2/5/15 AD.
 * Modified by Thanisak Piyasaksiri on 3/31/15 AD.
 */
public class APIs {

    //private static String DOMAIN = "http://demo.wmdcom.com/taxi/api/index.php/car/";
    //private static String DOMAIN = "http://169.254.123.1:8080/taxi/api/index.php/car/";

    //Test P'Keaw
//    private static String DOMAIN = "http://110.169.188.177:8080/taxi/api/index.php/car/";

    //On Car
    //public static String DOMAIN = "http://192.168.0.58:8080/taxi/api/index.php/car/";

    //New com//
//    private static String DOMAIN = "http://192.168.123.1:8080/taxi/api/index.php/car/";
      public static String DOMAIN = "";

    private static String APIKEY_PARAM = "api_key";
    private static String APIKEY_VALUE = "1234";

    public static String getRegisterAPI() {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("register_taxi?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);

        return api.toString();
    }

    public static String DestroyApp()
    {
        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("destroyApp?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);

        return api.toString();
    }

    public static String getLoginAPI(String stuff_id, String taxi_id, String km, String version) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("login?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("taxi_id=" + taxi_id);
        api.append("&");
        api.append("staff_id=" + stuff_id);
        api.append("&");
        api.append("km=" + km);
        api.append("&");
        api.append("version=" + version);


        return api.toString();
    }

    public static String getLoginAPIwithUser(String stuff_id,String password, String taxi_id, String km, String version) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("loginwithpass?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("taxi_id=" + taxi_id);
        api.append("&");
        api.append("password=" + password);
        api.append("&");
        api.append("staff_id=" + stuff_id);
        api.append("&");
        api.append("km=" + km);
        api.append("&");
        api.append("version=" + version);


        return api.toString();
    }


    public static String getMeterAPI() {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("meter_status?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);

        return api.toString();
    }

//    http://192.168.0.58:8080/taxi/api/index.php/car/request_job?api_key=1234&taxi_id=193
    public static String getRequestJobAPI(String taxi_id) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("request_job?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("taxi_id=" + taxi_id);
        //api.append("&");
        //api.append("job_type=9");

        return api.toString();
    }

    public static String fillgas(String taxi_id) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("fillgas?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("taxi_id=" + taxi_id);

        return api.toString();
    }

    public static String getTaxiStatusAPI(String taxi_id, String job_type, String reserved_id) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("taxi_status?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("taxi_id=" + taxi_id);
        api.append("&");
        api.append("job_type=" + job_type);
        api.append("&");
        api.append("reserved_id=" + reserved_id);

        return api.toString();
    }

    public static String arrivedPickLocation(String job_id) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("arrived_picklocation?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("job_id=" + job_id);

        return api.toString();
    }

    public static String ManMeterstart() {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("manual_meter?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("status=1");
        return api.toString();
    }

    public static String ManMeterstop() {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("manual_meter?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("status=0");
        return api.toString();
    }

    public static String startJob(String job_id, String job_type) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("start_job?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("job_id=" + job_id);
        api.append("&");
        api.append("job_type=" + job_type);

        return api.toString();
    }

    public static String getEndJobAPI(String job_id,String enable_credit_card , String reserved_type) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("end_job?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("job_id=" + job_id);
        api.append("&");
        api.append("enable_credit_card=" + enable_credit_card);
        api.append("&");
        api.append("reserved_type=" + reserved_type);

        return api.toString();
    }

    public static String getTripCostAPI(String job_id, String total_distance, String total_time, String job_type, String payment_type, String payment_channel, String total_cost, String fare, String toll, String km) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("end_trip?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("job_id=" + job_id);
        api.append("&");
        api.append("total_distance=" + total_distance);
        api.append("&");
        api.append("total_time=" + total_time);
        api.append("&");
        api.append("job_type=" + job_type);
        api.append("&");
        api.append("payment_type=" + payment_type);
        api.append("&");
        api.append("payment_channel=" + payment_channel);
        api.append("&");
        api.append("total_cost=" + total_cost);
        api.append("&");
        api.append("fare=" + fare);
        api.append("&");
        api.append("toll=" + toll);
        api.append("&");
        api.append("km=" + km);

        return api.toString();
    }

    public static String getLogoutAPI(String driver_id, String taxi_id, String operation_id, String input_km) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("logout?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("driver_id=" + driver_id);
        api.append("&");
        api.append("taxi_id=" + taxi_id);
        api.append("&");
        api.append("operation_id=" + operation_id);
        api.append("&");
        api.append("input_km=" + input_km);

        return api.toString();
    }

    public static String getSOSAPI() {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("sos?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);

        return api.toString();
    }

    public static String getReportEmergency(String service_status, String service_type) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("emergency?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("service_status=" + service_status);
        api.append("&");
        api.append("service_type=" + service_type);

        return api.toString();
    }


    public static String getAcceptReservedAPI(String reserved_id, String taxi_id, String operation_id) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("accept_reserve?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("reserved_id=" + reserved_id);
        api.append("&");
        api.append("taxi_id=" + taxi_id);
        api.append("&");
        api.append("operation_id=" + operation_id);

        return api.toString();
    }

    public static String getThumbingAPI(String taxi_id, String driver_id, String operation_id) {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("thumbing?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);
        api.append("&");
        api.append("driver_id=" + driver_id);
        api.append("&");
        api.append("taxi_id=" + taxi_id);
        api.append("&");
        api.append("operation_id=" + operation_id);

        return api.toString();
    }

    public static String getStandbyAPI() {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("standy?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);

        return api.toString();
    }

    public static String getBreakAPI() {

        StringBuffer api = new StringBuffer();
        api.append(DOMAIN);
        api.append("breaking?");
        api.append(APIKEY_PARAM + "=" + APIKEY_VALUE);

        return api.toString();
    }
}
