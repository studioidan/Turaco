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

import com.studioidan.turaco.Base.BaseFragment;
import com.studioidan.turaco.CustomView.HeaderBar;
import com.studioidan.turaco.Fragments.SignInFragment;


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

    public void addContentFragment(BaseFragment ComingFragment, boolean addtobackstack) {
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();

        String tag = ComingFragment.getClass().getSimpleName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

        Fragment topFragment = getTopFragment(R.id.ll_content_fragment);
        if (topFragment != null && tag.equals(topFragment.getClass().getSimpleName())) {
            Log.d("Transactions", tag + " already on top");
            return;
        }


        if (fragment != null) {
            mTransaction.replace(R.id.ll_content_fragment, fragment, tag).commitAllowingStateLoss();
            Log.d("Transactions", tag + " was replaced from memory");
            return;
        }

        mTransaction.replace(R.id.ll_content_fragment, ComingFragment, tag);
        if (addtobackstack)
            mTransaction.addToBackStack(tag);

        Log.d("Transactions", tag + " was replaced from new instance: backstack: " + addtobackstack);

        mTransaction.commitAllowingStateLoss();
    }


    public Fragment getFragmentAt(int index) {
        if (getFragmentCount() <= 0)
            return null;
        else {
            return getSupportFragmentManager().findFragmentByTag(Integer.toString(index));
        }
    }

    public Fragment getTopFragment(int containderId) {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(containderId);
        return fragment;

    }

    public int getFragmentCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }

    public void hideVirtualKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException mException) {
            mException.printStackTrace();
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




}
