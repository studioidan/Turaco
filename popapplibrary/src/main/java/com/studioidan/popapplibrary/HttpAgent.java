package com.studioidan.popapplibrary;

import android.app.Dialog;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by PopApp_laptop on 14/06/2015.
 */
public class HttpAgent extends AsyncTask<String, String, String> {
    private static final int TIME_OUT = 10000;
    public List<NameValuePair> mParams = null;
    public Dialog mDialog = null;
    public String mUrl;
    public String mMethodName;

    private String mError;

    //calbacks
    private IRequestCallback mCallbackRequest = null;
    private IProgressCallback mCallbackProgress = null;

    public interface IRequestCallback {
        void onRequestStart(String mMethodName);

        void onRequestEnd(String mMethodName, String e, String response);
    }

    public interface IProgressCallback {
        void onRequestProgress(String methodName, int progress);
    }

    // contractors
    public HttpAgent(String url, String methodName, List<NameValuePair> params) {
        this.mUrl = url;
        this.mMethodName = methodName;
        this.mParams = params;
    }

    public HttpAgent(String url, String methodName, List<NameValuePair> params, IRequestCallback callbackRequest) {
        this.mUrl = url;
        this.mMethodName = methodName;
        this.mParams = params;
        this.mCallbackRequest = callbackRequest;
    }

    public HttpAgent(String url, String methodName, List<NameValuePair> params, IRequestCallback callbackRequest, IProgressCallback callbackProgress) {
        this.mUrl = url;
        this.mMethodName = methodName;
        this.mParams = params;
        this.mCallbackRequest = callbackRequest;
        this.mCallbackProgress = callbackProgress;
    }

    public void setDialog(Dialog dialog) {
        this.mDialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        if (mCallbackRequest != null)
            mCallbackRequest.onRequestStart(mMethodName);
    }

    @Override
    protected String doInBackground(String... strings) {
        String responseStr = "";
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, TIME_OUT);
        HttpClient httpClient = new DefaultHttpClient(httpParameters);


        String paramsString = "";

        if (strings[0].toLowerCase().equals("get")) {
            if (mParams != null)
                paramsString = URLEncodedUtils.format(this.mParams, "UTF-8");

            HttpGet httpGet = new HttpGet(mUrl + "?" + paramsString);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
                mError = e.getMessage();
            }
        } else {
            HttpPost httpPost = new HttpPost(mUrl);
            if (httpPost == null || mUrl.trim().equals("")) {
                mError = "url is not correct";
                return null;
            }
            try {
                if (mParams != null)
                    httpPost.setEntity(new UrlEncodedFormEntity(this.mParams, "UTF-8"));
                HttpResponse response = httpClient.execute(httpPost);
                responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
                mError = e.getMessage();
            }
        }
        return responseStr;
    }

    @Override
    protected void onPostExecute(String res) {
        //super.onPostExecute(s);
        if (mCallbackRequest != null)
            mCallbackRequest.onRequestEnd(mMethodName, mError, res);
    }
}
