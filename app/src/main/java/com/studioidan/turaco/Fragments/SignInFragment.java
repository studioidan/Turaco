package com.studioidan.turaco.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.studioidan.popapplibrary.HttpAgent;
import com.studioidan.turaco.R;
import com.studioidan.turaco.alarm.AlarmService;
import com.studioidan.turaco.singeltones.DataStore;
import com.studioidan.turaco.singeltones.Factory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by macbook on 8/19/15.
 */
public class SignInFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    Button bSignIn;
    CheckBox mCheckBox;
    EditText etUserName, etPassword;
    ImageView imgLogo;
    private DataStore ds = DataStore.getInstance();

    public static SignInFragment newInstance() {
        SignInFragment mFragment = new SignInFragment();
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mLayout = inflater.inflate(R.layout.sign_in_layout, null);
        bSignIn = (Button) mLayout.findViewById(R.id.btn_sign_in_layout);
        etUserName = (EditText) mLayout.findViewById(R.id.ed_user_name_sign_in_layout);
        etPassword = (EditText) mLayout.findViewById(R.id.ed_user_pass_sign_in_layout);
        mCheckBox = (CheckBox) mLayout.findViewById(R.id.ch_rember_sign_in_layout);
        imgLogo = (ImageView) mLayout.findViewById(R.id.imgSignLogo);
        bSignIn.setOnClickListener(this);


        etUserName.setText(ds.getUserName());
        etPassword.setText(ds.getPassword());
        mCheckBox.setChecked(true);
        mCheckBox.setOnCheckedChangeListener(this);

        mLayout.findViewById(R.id.imgSignLogo).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showSetupDialog();
                return false;
            }
        });

        return mLayout;

    }

    @Override
    public String tag() {
        return getClass().getName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideVirtualKeyBoard();
        startAnimation();
    }

    private void startAnimation() {
        YoYo.with(Techniques.FadeIn)
                .duration(2000)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(imgLogo);

        YoYo.with(Techniques.ZoomIn)
                .duration(1400)
                .playOn(imgLogo);
    }

    private void showSetupDialog() {

        SetPanelAddressDialog setPanelAddressDialog = new SetPanelAddressDialog();
        setPanelAddressDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setPanelAddressDialog.show(getChildFragmentManager(), "TAG");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in_layout:
                if (etUserName.getText().toString().equalsIgnoreCase("") || etUserName.getText().toString().contains(":")) {
                    if (etUserName.getText().toString().contains(":")) {
                        etUserName.setError(": not included");
                    } else if (etUserName.getText().toString().equalsIgnoreCase("")) {
                        etUserName.setError("*");
                    }
                } else if (etPassword.getText().toString().equalsIgnoreCase("")) {
                    etPassword.setError("*");
                } else {
                    hideVirtualKeyBoard();
                    if (mCheckBox.isChecked()) {
                        DataStore.getInstance().setUserName(etUserName.getText().toString().trim());
                        DataStore.getInstance().setPassword(etPassword.getText().toString().trim());
                    }
                    if (isOnline())
                        login(etUserName.getText().toString().trim(), etPassword.getText().toString().trim());
                    else {
                        Factory.showOkDialog(getActivity(), "Error", "No internet connection", "Ok", null);
                    }

                }
                break;
        }
    }

    private void login(String userName, String pass) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("user", userName));
        params.add(new BasicNameValuePair("password", pass));
        new HttpAgent(ds.getBaseUrl() + ":8080/authenticateUser", "login", params, new HttpAgent.IRequestCallback() {
            @Override
            public void onRequestStart(String mMethodName) {
                showProgressDialog("Login is in process", "Please wait...");
            }

            @Override
            public void onRequestEnd(String mMethodName, String e, String response) {
                hideProgressDialog();
                if (e == null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.has("success")) {
                            boolean success = object.getBoolean("success");
                            if (success) {
                                addContentFragment(PanelStateFragment.newInstance(getActivity()), false);
                                getActivity().startService(new Intent(getActivity(), AlarmService.class));
                            } else {
                                Factory.ShowErrorDialog(getActivity(), object);
                            }
                        }
                    } catch (Exception ex) {
                        Factory.showOkDialog(getActivity(), "Error", ex.getMessage(), "Ok", null);
                    }
                } else {
                    Factory.showOkDialog(getActivity(), "Error", e, "Ok", null);
                }
            }
        }).execute("get");
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("CHECKED", b).commit();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
