package com.infratrans.taxi.onboard.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.infratrans.taxi.onboard.R;
import com.infratrans.taxi.onboard.util.Global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Thanisak Piyasaksiri on 4/16/15 AD.
 */
public class DownloadService extends Service {

    public static final String BROADCAST_ACTION = "com.infratrans.taxi.onboard.service.download_apk";
    private Bundle extras;
    private String PATH = "";
    private Thread t = null;
    private String url = null;
    private HttpURLConnection c;
    private FileOutputStream fo = null;
    private int progress = 0;
    private int offset = 0;
    private double numRead = 0;
    private int filesize = 0;

    @Override
    public void onDestroy() {

        if (t != null) {

            if (t.isAlive())
                t.interrupt();

            t = null;
        }
        super.onDestroy();
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        extras = intent.getExtras();

        if (extras.containsKey("url")) {

            url = extras.getString("url");
            PATH = extras.getString("path");
            startDownload(url);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private void startDownload(final String url) {

        Global.printLog(false, "Download URL", String.valueOf(url));
        //this.PATH = Environment.getExternalStorageDirectory() + "/" + getString(R.string.download_path) + "/download";
        //Global.printLog(true, "PATH", String.valueOf(PATH));
        //UtilFileObject.chkDir(PATH);
        /*
        if(new File(this.PATH + "/" + getString(R.string.app_file_name)).isFile())
            new File(this.PATH + "/" + getString(R.string.app_file_name)).delete();
        */
        t = new Thread(new Runnable() {
            @Override
            public void run() {

                URL u;
                OutputStream output = null;
                try {

                    progress = 0;
                    u = new URL(url);
                    c = (HttpURLConnection) u.openConnection();
                    c.setRequestMethod("GET");
                    c.setDoOutput(false);
                    c.connect();

                    String content_length = String.valueOf(c.getContentLength());
                    Global.printLog(false, "Content Length", String.valueOf(content_length));

                    try {
                        filesize = Integer.parseInt(content_length);
                    } catch (Exception e) {
                        filesize = 0;
                    }

                    Global.printLog(false, "Get Header Fields", String.valueOf(c.getHeaderFields()));
                    Global.printLog(false, "HTTP RESPONSE", String.valueOf(c.getResponseCode()));
                    Global.printLog(false, "filesize", String.valueOf(filesize));

                    if (c.getResponseCode() == 200) {
                        //if(filesize > 0) {

                        if (c.getResponseCode() == 200) {

                            File file = new File(PATH);
                            file.mkdirs();
                            File outputFile = new File(file, getString(R.string.app_file_name));

                            fo = new FileOutputStream(outputFile);

                            InputStream is = c.getInputStream();

                            byte[] buffer = new byte[1024];

                            offset = 0;
                            numRead = 0;

                            while ((offset = is.read(buffer)) > 0) {

                                fo.write(buffer, 0, offset);
                                /*
                                numRead += offset;
                                if( progress < ((int)((100 * numRead) / filesize))) {
                                    progress = (int) (100 * numRead) / filesize;
                                    Global.printLog(false, "progress", String.valueOf(progress));
                                }
                                */
                                /*
                                Intent intent = new Intent(BROADCAST_ACTION);
                                intent.putExtra("status", "200");
                                //intent.putExtra("progress", progress);
                                intent.putExtra("progress", 20);
                                intent.putExtra("completed", false);
                                DownloadService.this.sendBroadcast(intent);
                                */
                            }

                            fo.flush();
                            fo.close();
                            is.close();
                            c.disconnect();

                            Intent intent = new Intent(BROADCAST_ACTION);
                            intent.putExtra("status", "200");
                            intent.putExtra("progress", progress);
                            intent.putExtra("completed", true);
                            intent.putExtra("path", PATH + "/" + getString(R.string.app_file_name));
                            DownloadService.this.sendBroadcast(intent);

                        } else {

                            Global.printLog(true, "else", "true");
                            Intent intent = new Intent(BROADCAST_ACTION);
                            intent.putExtra("status", "500");
                            intent.putExtra("progress", 100);
                            DownloadService.this.sendBroadcast(intent);
                        }

                    } else {

                        Global.printLog(true, "else", "true");
                        Intent intent = new Intent(BROADCAST_ACTION);
                        intent.putExtra("status", "500");
                        intent.putExtra("progress", 100);
                        DownloadService.this.sendBroadcast(intent);
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                    try {
                        new File(PATH + "/" + getString(R.string.app_file_name)).delete();
                    } catch (Exception e2) {
                    }

                    Intent intent = new Intent(BROADCAST_ACTION);
                    intent.putExtra("status", "500");
                    intent.putExtra("progress", 100);
                    DownloadService.this.sendBroadcast(intent);

                } catch (Exception e) {

                    Global.printLog(true, "Exception", "true");
                    e.getMessage();
                    e.getLocalizedMessage();
                    e.printStackTrace();
                    try {
                        new File(PATH + "/" + getString(R.string.app_file_name)).delete();
                    } catch (Exception e2) {
                    }

                    Intent intent = new Intent(BROADCAST_ACTION);
                    intent.putExtra("status", "500");
                    intent.putExtra("progress", 100);
                    DownloadService.this.sendBroadcast(intent);
                }
            }
        });
        t.start();
    }
}
