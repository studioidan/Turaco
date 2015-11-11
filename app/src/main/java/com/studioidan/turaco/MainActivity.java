package com.studioidan.turaco;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.studioidan.turaco.CustomView.HeaderBar;
import com.studioidan.turaco.Fragments.BaseFragment;
import com.studioidan.turaco.Fragments.SignInFragment;
import com.studioidan.turaco.singeltones.Factory;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btn_fetch_main_activity:
//
//                break;
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
}
