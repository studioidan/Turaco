package com.studioidan.turaco.singeltones;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import org.json.JSONObject;

/**
 * Created by PopApp_laptop on 03/11/2015.
 */
public class Factory {
    private static Factory instance;

    public static Factory getInstance() {
        if (instance == null)
            instance = new Factory();
        return instance;
    }


    public static void ShowErrorDialog(Context con, JSONObject msg) {
        try {
            String title = "Error";
            String message = msg.getString("message");

            AlertDialog.Builder builder = new AlertDialog.Builder(con);

            builder.setTitle(title);
            builder.setMessage(message);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception ex) {
        }
    }

    public static void showOkDialog(Context con, String title, String message, String btnText, final GenericCallbackOne callback) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(con);

            builder.setTitle(title);
            builder.setMessage(message);

            builder.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (callback != null)
                        callback.onDone(null);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception ex) {
        }
    }

    public interface GenericCallback {
        void onDone(boolean success,Object result);
    }

    public interface GenericCallbackOne {
        void onDone(Object result);
    }
}
