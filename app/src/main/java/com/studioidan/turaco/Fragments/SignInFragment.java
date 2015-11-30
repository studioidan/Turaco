package com.studioidan.turaco.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.studioidan.popapplibrary.CPM;
import com.studioidan.popapplibrary.HttpAgent;
import com.studioidan.turaco.Base.BaseFragment;
import com.studioidan.turaco.Fragments.Dialogs.DialogSetPanelAddress;
import com.studioidan.turaco.Gcm.GCMClientManager;
import com.studioidan.turaco.R;
import com.studioidan.turaco.entities.Keys;
import com.studioidan.turaco.singeltones.DataStore;
import com.studioidan.turaco.singeltones.Factory;
import com.studioidan.turaco.singeltones.TLog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by macbook on 8/19/15.
 */
public class SignInFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public final String TAG = getClass().getName();

    Button btnSignIn;
    CheckBox mCheckBox;
    EditText etUserName, etPassword;
    ImageView imgLogo;
    private DataStore ds = DataStore.getInstance();


    public static SignInFragment newInstance() {
        SignInFragment mFragment = new SignInFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mLayout = inflater.inflate(R.layout.sign_in_layout, null);
        btnSignIn = (Button) mLayout.findViewById(R.id.btn_sign_in_layout);
        etUserName = (EditText) mLayout.findViewById(R.id.ed_user_name_sign_in_layout);
        etPassword = (EditText) mLayout.findViewById(R.id.ed_user_pass_sign_in_layout);
        mCheckBox = (CheckBox) mLayout.findViewById(R.id.ch_rember_sign_in_layout);
        imgLogo = (ImageView) mLayout.findViewById(R.id.imgSignLogo);
        btnSignIn.setOnClickListener(this);


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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideVirtualKeyBoard();
        startAnimation();
        setPushStuff();
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

        DialogSetPanelAddress setPanelAddressDialog = new DialogSetPanelAddress();
        //setPanelAddressDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
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
        String regId = CPM.getString(Keys.REG_ID, "", getActivity());
        params.add(new BasicNameValuePair("GcmToken", regId));
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
                                addContentFragment(MainFragment.newInstance(getActivity()), false);
                                //getActivity().startService(new Intent(getActivity(), AlarmService.class));
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

    private void setPushStuff() {
        GCMClientManager pushClientManager = new GCMClientManager(getActivity(), getString(R.string.gcm_id));
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                Log.d(getClass().getName(), "got regId: " + registrationId);
                CPM.putString(Keys.REG_ID, registrationId, getActivity());
                // SEND async device registration to your back-end server
                // linking user with device registration id
                // POST https://my-back-end.com/devices/register?user_id=123&device_id=abc
                btnSignIn.setEnabled(true);
                btnSignIn.setText("Login");
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
                TLog.e(TAG, ex);
                Toast.makeText(getActivity(), "Could not get push token -> " + ex, Toast.LENGTH_LONG).show();
                // If there is an error registering, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off when retrying.
            }
        });
    }

}
