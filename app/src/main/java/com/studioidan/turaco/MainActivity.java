package com.studioidan.turaco;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.studioidan.popapplibrary.CPM;
import com.studioidan.turaco.CustomView.HeaderBar;
import com.studioidan.turaco.Fragments.BaseFragment;
import com.studioidan.turaco.Fragments.SignInFragment;
import com.studioidan.turaco.Gcm.GCMClientManager;
import com.studioidan.turaco.entities.Keys;


public class MainActivity extends FragmentActivity {

    public final String TAG = getClass().getName();

    public HeaderBar getmBar() {
        return mBar;
    }

    //Button mButton;
    HeaderBar mBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPushStuff();
        //Handle HeaderBar configuration change
        mBar = (HeaderBar) findViewById(R.id.header_bar_main_activity);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("BarShown") && !savedInstanceState.getBoolean("BarShown")) {
                mBar.hideHeaderBar();
            } else {
                mBar.updateHeaderBar();
            }
        } else {
            mBar.hideHeaderBar();
        }

        if (getIntent() != null) {
            boolean mbool = getIntent().getBooleanExtra("ALARM", false);
            if (mbool) {
            } else {

                FragmentManager fragmentManager = getSupportFragmentManager();
                BaseFragment fragment = (BaseFragment) fragmentManager
                        .findFragmentByTag("FRAGMENTTAG");
                if (fragment == null) {
                    addContentFragment(SignInFragment.newInstance(), false);
                } else {
                }
            }
        }


    }

    public void addContentFragment(BaseFragment mComingFragment, boolean addtobackstack) {


        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();

        String tag = (((BaseFragment) mComingFragment)).tag();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            mTransaction.show(fragment);
            return;
        }

        mTransaction.replace(R.id.ll_content_fragment, mComingFragment, tag);
        if (addtobackstack)
            mTransaction.addToBackStack(mComingFragment.getClass().getSimpleName());


        mTransaction.commitAllowingStateLoss();
    }

    public void hideVirtualKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//			if(getCurrentFocus() != null)
//			{
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			}
        } catch (NullPointerException mException) {
            mException.printStackTrace();
//            Logger.LogInfo(this.getClass().getSimpleName(), "null pointer to hide keyboard");
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mBar.getVisibility() == View.VISIBLE) {
            outState.putBoolean("BarShown", true);
        } else {
            outState.putBoolean("BarShown", false);
        }
        super.onSaveInstanceState(outState);
    }

    private void setPushStuff() {
        GCMClientManager pushClientManager = new GCMClientManager(this, getString(R.string.gcm_id));
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                //Toast.makeText(MainActivity.this, registrationId, Toast.LENGTH_SHORT).show();
                Log.d(getClass().getName(), "got regId: " + registrationId);
                CPM.putString(Keys.REG_ID, registrationId, MainActivity.this);
                // SEND async device registration to your back-end server
                // linking user with device registration id
                // POST https://my-back-end.com/devices/register?user_id=123&device_id=abc
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
                Log.e(TAG, ex);
                // If there is an error registering, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off when retrying.
            }
        });

    }
}
