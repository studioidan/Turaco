package com.studioidan.turaco.entities;

import android.content.Context;

import com.google.gson.Gson;
import com.studioidan.popapplibrary.HttpAgent;
import com.studioidan.turaco.data.UserManager;
import com.studioidan.turaco.singeltones.DataStore;
import com.studioidan.turaco.singeltones.Factory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import static com.studioidan.turaco.singeltones.Factory.showOkDialog;

/**
 * Created by PopApp_laptop on 04/11/2015.
 */
public class PanelManager {

    public static final int PANEL_STATUS_DISARM = 0;
    public static final int PANEL_STATUS_ARM = 1;

    public static final String ACTION_PANEL_STATUS = "action.panel.status";
    public static final String EXTRA_PANEL_STATUS = "extra.panel.status";

    static private DataStore ds = DataStore.getInstance();

    public static void getCurrentPanelStatus(Context con, final Factory.GenericCallback callback) {
        Panel panel = UserManager.getInstance().getCurrentPanel();

        new HttpAgent(ds.getBaseUrl() + ":8080/api/PanelsStatus/" + panel.getPanelId(), "panelStatus", null, new HttpAgent.IRequestCallback() {
            @Override
            public void onRequestStart(String mMethodName) {}

            @Override
            public void onRequestEnd(String mMethodName, String e, String response) {
                if (e == null) {
                    try {
                        PanelStatus ps = new Gson().fromJson(response, PanelStatus.class);
                        callback.onDone(true, ps);
                    } catch (Exception ex) {
                        callback.onDone(false, ex.getMessage());
                    }
                } else
                    callback.onDone(false, e);
            }
        }).execute("get");
    }

    public static void makePanelCommand(final Context con, final String command, final Factory.GenericCallback callback) {
        //http://69.64.63.136:8080/api/panelCommand/8?command=arm&parameter=a&ttl=0&user=e
        //http://69.64.63.136:8080/api/panelCommand/8?command=disarm&parameter=a&ttl=0&user=e
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("command", command));
        params.add(new BasicNameValuePair("parameter", "a"));
        params.add(new BasicNameValuePair("ttl", "0"));
        params.add(new BasicNameValuePair("user", ds.getUserName()));

        new HttpAgent(ds.getBaseUrl() + ":8080/api/panelCommand/" + UserManager.getInstance().getCurrentPanel().getPanelId(), "panelCommand", params, new HttpAgent.IRequestCallback() {
            @Override
            public void onRequestStart(String mMethodName) {
            }

            @Override
            public void onRequestEnd(String mMethodName, String e, String response) {
                if (e == null) {
                    if (response.replace("\"", "").toLowerCase().trim().equals("ok")) {
                        callback.onDone(true, null);
                    } else {
                        showOkDialog(con, "Error", response.replace("\"", ""), "Ok", null);
                        callback.onDone(false, null);
                    }
                } else {
                    showOkDialog(con, "Connection Error", "Please try again later", "Ok", null);
                    callback.onDone(false, null);
                }
            }
        }).execute("get");
    }

}
