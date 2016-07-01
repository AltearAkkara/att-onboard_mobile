package com.infratrans.taxi.onboard.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore.MediaColumns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UtilFileObject {

    private File file = null;
    private String path = null;

    public UtilFileObject(String filepath) {

        this.path = filepath;
        file = new File(this.path);
    }

    public static boolean chkDir(String path) {

        String chk = path;
        //Log.v("Path", chk);

        File chkPath = new File(chk);
        if (!chkPath.isDirectory()) {
            return chkPath.mkdirs();
        }
        return true;
    }

    public static boolean isExternalStorageAvilable() {

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isFile() {

        return file.isFile();
    }

    public boolean write(Object obj) {

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {

            fos = new FileOutputStream(this.file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete() {

        try {

            if (this.file.isFile()) {
                this.file.delete();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Object read() {

        Object obj = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {

            if (this.file.isFile()) {

                fis = new FileInputStream(this.file);
                ois = new ObjectInputStream(fis);
                obj = ois.readObject();
                ois.close();
                fis.close();
                return obj;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getMediaItemIdFromProvider(Uri providerUri, Context appContext, String fileName) {

        long ITEMID_NOT_FOUND = 0;

        //find id of the media provider item based on filename
        String[] projection = {MediaColumns._ID, MediaColumns.DATA};
        Cursor cursor = appContext.getContentResolver().query(
                providerUri, projection,
                MediaColumns.DATA + "=?", new String[]{fileName},
                null);

        if (null == cursor) {
            return ITEMID_NOT_FOUND;
        }

        long id = ITEMID_NOT_FOUND;
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
        }
        cursor.close();
        return id;
    }
}
