package com.infratrans.taxi.onboard.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UtilNetwork {

    public static boolean chkInternetAccess(Context ctx) {

        int timeout = 3 * 1000;
        try {

            Process p = Runtime.getRuntime().exec("ping -c 3 -w 4 www.google.com");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            String res = "";
            while ((s = stdInput.readLine()) != null) {
                res += s + "\n";
            }
            p.destroy();

            if (res.toLowerCase().indexOf("100% packet loss") > -1)
                return false;
            else
                return true;

        } catch (IOException e) {
            return false;
        }
    }

    public static boolean chkNetworkConnect(Context ctx) {

        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {

            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {

                for (int i = 0; i < info.length; i++) {

                    if (String.valueOf(info[i].getTypeName()).equalsIgnoreCase("MOBILE") || String.valueOf(info[i].getTypeName()).equalsIgnoreCase("WIFI") || String.valueOf(info[i].getTypeName()).equalsIgnoreCase("ETH")) {

                        if (info[i].getState() == NetworkInfo.State.CONNECTED)
                            return true;
                    }
                }
            }
        }
        return false;
    }
}
