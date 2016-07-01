package com.infratrans.taxi.onboard.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UtilGenerator {

    private static java.text.NumberFormat num = new java.text.DecimalFormat("00");
    private static java.util.Random rand = new java.util.Random(System.currentTimeMillis());

    public static String genTransactionId() {

        String str = "";
        Calendar now = Calendar.getInstance(Locale.US);
        String year = "" + (now.get(Calendar.YEAR));
        String month = "" + (num.format(now.get(Calendar.MONTH) + 1));
        String date = "" + (num.format(now.get(Calendar.DAY_OF_MONTH)));
        String hour = "" + (num.format(now.get(Calendar.HOUR_OF_DAY)));
        String min = "" + (num.format(now.get(Calendar.MINUTE)));
        String sec = "" + (num.format(now.get(Calendar.SECOND)));
        String ran = generateDigit(6);
        str = year + month + date + hour + min + sec + ran;
        return str;
    }

    public static String genTransactionIdFromDateTime(String dateime) {

        String str = dateime;
        String ran = generateDigit(6);
        str = str + ran;
        return str;
    }

    private static String generateDigit(int maxLength) {

        String pass = "";
        String pwd = "";
        pwd += "00112233445566778899";
        try {
            char[] pwdChars = new char[pwd.length()];
            for (int i = 0; i < pwd.length(); i++) {
                pwdChars[i] = pwd.charAt(i);
            }
            pwdChars = shuffleCharachters(pwdChars);
            for (int i = 0; i < maxLength; i++) {
                pass += pwdChars[rand.nextInt(pwdChars.length - 1)];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pass;
    }

    private static char[] shuffleCharachters(char[] characters) {

        for (int j = 0; j < characters.length; j++) {
            int from = rand.nextInt(characters.length - 1);
            char old = characters[j];
            characters[j] = characters[from];
            characters[from] = old;
        }
        return characters;
    }

    public static String getDateTime(String datetime_format) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(datetime_format);
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    public static String getDate() {

        String strDate = "";
        Calendar now = Calendar.getInstance(Locale.US);
        String year = "" + (now.get(Calendar.YEAR));
        String month = "" + (num.format(now.get(Calendar.MONTH) + 1));
        String date = "" + (num.format(now.get(Calendar.DAY_OF_MONTH)));
        String hour = "" + (num.format(now.get(Calendar.HOUR_OF_DAY)));
        String min = "" + (num.format(now.get(Calendar.MINUTE)));
        String sec = "" + (num.format(now.get(Calendar.SECOND)));
        strDate = year + "-" + month + "-" + date + " " + hour + ":" + min + ":" + sec;
        return strDate;
    }

    public static String getWeek() {

        String weekResult = "";
        Calendar now = Calendar.getInstance(Locale.US);
        weekResult = "" + (num.format(now.get(Calendar.WEEK_OF_YEAR)));
        return weekResult;
    }

    public static String getDay() {

        String dayResult = "";
        Calendar now = Calendar.getInstance(Locale.US);
        dayResult = "" + (num.format(now.get(Calendar.DAY_OF_MONTH)));
        return dayResult;
    }

    public static String getMonth() {

        String monthResult = "";
        Calendar now = Calendar.getInstance(Locale.US);
        monthResult = "" + (num.format(now.get(Calendar.MONTH) + 1));
        return monthResult;
    }

    public static String getYear() {
        String yearResult = "";
        Calendar now = Calendar.getInstance(Locale.US);
        yearResult = "" + (now.get(Calendar.YEAR));
        return yearResult;
    }

    public static long getMilisec() {

        Calendar now = Calendar.getInstance(Locale.US);
        return now.getTimeInMillis();
    }

    public static long convertDatetoMilisec(String datetime) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(datetime.substring(0, 4)));
        calendar.set(Calendar.MONTH, Integer.parseInt(datetime.substring(5, 7)) - 1);
        calendar.set(Calendar.DATE, Integer.parseInt(datetime.substring(8, 10)));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(datetime.substring(11, 13)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(datetime.substring(14, 16)));
        return calendar.getTime().getTime();
    }

    public static String millisec2Date(String dateformat, long milisec) {

        Date date = new Date(milisec);
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat, Locale.US);
        return dateFormat.format(date);
    }

    public static String getDeviceID(Context ctx) {
        TelephonyManager TelephonyMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return TelephonyMgr.getDeviceId();
    }

}
