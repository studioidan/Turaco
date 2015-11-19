package com.studioidan.turaco.Fragments;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.studioidan.turaco.Base.BaseFragment;
import com.studioidan.turaco.MainActivity;
import com.studioidan.turaco.R;
import com.studioidan.turaco.alarm.AlarmService;
import com.studioidan.turaco.entities.Panel;
import com.studioidan.turaco.entities.PanelManager;
import com.studioidan.turaco.singeltones.Factory;

/**
 * Created by popApp on 8/25/15.
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {
    public static final String ACTION_ARM = "action.arm";
    public static final String ACTION_ARM_STAY = "action.arm.stay";
    public static final String ACTION_DISARM = "action.disarm";

    public static final String EXTRA_OP_CODE = "extra.opCode";
    public static final String EXTRA_PANEL = "extra.panel";

    public boolean isARmStay = false;
    Context mContext;
    public static boolean isLocked = false;
    public static boolean isShown = false;

    //Views
    View mView;
    ImageView btnArm, btnDisarm, btnArmStay;
    TextView mState;
    RelativeLayout mStateContainer;
    private Panel mPanel;
    LinearLayout indicator, indicatorHolder;

    public static MainFragment newInstance(Context mComingcontext) {
        MainFragment mFragment = new MainFragment();
        mFragment.setContext(mComingcontext);
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).getmBar().showHeaderBar();
        ((MainActivity) getActivity()).getmBar().updateHeaderBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.panal_state_fragment, null);
        isShown = true;
        btnArm = (ImageView) mView.findViewById(R.id.imgArm);
        btnArmStay = (ImageView) mView.findViewById(R.id.imgArmStay);
        btnDisarm = (ImageView) mView.findViewById(R.id.imgDisarm);
        mState = (TextView) mView.findViewById(R.id.tvPanelState);
        mStateContainer = (RelativeLayout) mView.findViewById(R.id.ll_state_panal_state);
        indicator = (LinearLayout) mView.findViewById(R.id.indicator);
        indicatorHolder = (LinearLayout) mView.findViewById(R.id.indicatorHolder);

        btnArm.setOnClickListener(this);
        btnArmStay.setOnClickListener(this);
        btnDisarm.setOnClickListener(this);
        loadPanelStatus();
        return mView;
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AlarmService.ACTION_PANEL);
        intentFilter.addAction(PanelManager.ACTION_PANEL_STATUS);
        intentFilter.addAction(AlarmService.ACTION_START_REQUEST);

        /*PUSH*/
        intentFilter.addAction(ACTION_ARM);
        intentFilter.addAction(ACTION_ARM_STAY);
        intentFilter.addAction(ACTION_DISARM);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerReceiver();
    }

    private void loadPanelStatus() {
        //showProgressDialog("Loading panel status", "Please wait...");
        PanelManager.getPanelStatus(getActivity(), new Factory.GenericCallback() {
            @Override
            public void onDone(boolean success, Object result) {
                //hideProgressDialog();
                if (success) {
                    mPanel = (Panel) result;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initViews(false);
                        }
                    }, 100);
                }
            }
        });
    }

    @Override
    public void onClick(final View view) {
        isLocked = true;
        switch (view.getId()) {
            case R.id.imgArm:
                Factory.showOkDialog(getActivity(), "Panel Command", "Do you want to arm the system?", "Yes", new Factory.GenericCallbackOne() {
                    @Override
                    public void onDone(Object result) {
                        makePanelCommand("arm");
                    }
                });
                break;
            case R.id.imgArmStay:
                isARmStay = true;
                Factory.showOkDialog(getActivity(), "Panel Command", "Do you want to arm stay the system?", "Yes", new Factory.GenericCallbackOne() {
                    @Override
                    public void onDone(Object result) {
                        //makePanelCommand("arm_stay");
                        makePanelCommand("arm");
                    }
                });
                break;
            case R.id.imgDisarm:
                Factory.showOkDialog(getActivity(), "Panel Command", "Do you want to disarm the system?", "Yes", new Factory.GenericCallbackOne() {
                    @Override
                    public void onDone(Object result) {
                        makePanelCommand("disarm");
                    }
                });
                break;
        }
    }

    private void makePanelCommand(final String command) {
        isLocked = true;
        showProgressDialog("Connecting to panel " + ds.getPanel(), "Please wait...");
        PanelManager.makePanelCommand(getActivity(), command, new Factory.GenericCallback() {
            @Override
            public void onDone(boolean success, Object result) {
                hideProgressDialog();
                if (success == true) {
                    switch (command) {
                        case "arm":
                            mPanel.arm = Panel.PANEL_STATUS_ARMED;
                            break;
                        case "disarm":
                            mPanel.arm = Panel.PANEL_STATUS_DISARMED;
                            break;
                    }
                    initViews(true);

                }
                isLocked = false;
            }
        });
    }

    private void initViews(boolean fromClick) {
        int[] t = new int[2];
        if (mPanel.arm == Panel.PANEL_STATUS_DISARMED) {
            mState.setText("System Disarmed");
            mStateContainer.setBackgroundResource(R.drawable.disarm_view);
            btnDisarm.getLocationOnScreen(t);
            btnArm.setEnabled(true);
            if (fromClick) YoYo.with(Techniques.Bounce).duration(800).playOn(btnDisarm);
            btnArmStay.setEnabled(true);
            btnDisarm.setEnabled(false);
        } else if (mPanel.arm == Panel.PANEL_STATUS_ARMED) {
            btnArm.setEnabled(false);
            btnArmStay.setEnabled(false);
            btnDisarm.setEnabled(true);
            if (isARmStay) {
                if (fromClick) YoYo.with(Techniques.Bounce).duration(800).playOn(btnArmStay);
                mState.setText("System Arm stayed");
                mStateContainer.setBackgroundResource(R.drawable.arm_stay_view);
                btnArmStay.getLocationOnScreen(t);

            } else {
                mState.setText("System Armed");
                if (fromClick) YoYo.with(Techniques.Bounce).duration(800).playOn(btnArm);
                mStateContainer.setBackgroundResource(R.drawable.arm_view);
                btnArm.getLocationOnScreen(t);
            }
        }
        playLineAnimation(t[0]);
        isARmStay = false;
    }

    private void playLineAnimation(float to) {
        //animate position
        int delta = btnArmStay.getMeasuredWidth() / 2;
        ObjectAnimator.ofFloat(indicatorHolder, "x", to + delta - indicatorHolder.getMeasuredWidth() / 2)
                .setDuration(600)
                .start();

        //Animate color
        int color;
        if (mPanel.arm == 1) // arm
        {
            color = ContextCompat.getColor(getActivity(), R.color.redColor);
        } else {
            color = isARmStay ? ContextCompat.getColor(getActivity(), R.color.orangColor) : ContextCompat.getColor(getActivity(), R.color.greenColor);
        }
        ObjectAnimator.ofObject(indicator, "backgroundColor", new ArgbEvaluator(), ((ColorDrawable) indicator.getBackground()).getColor(), color)
                .setDuration(600)
                .start();
    }

    @Override
    protected void setContext(Context mComingContext) {
        mContext = mComingContext;
    }

    @Override
    public void onStop() {
        super.onStop();
        isShown = false;
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isShown = false;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            /*Push actions*/
            if (ACTION_ARM.equals(action) || ACTION_ARM_STAY.equals(action) || ACTION_DISARM.equals(action)) {
                mPanel = (Panel) intent.getSerializableExtra(EXTRA_PANEL);
                initViews(true);
            }

            /*old action used by the service*/
            /*
            if (AlarmService.ACTION_START_REQUEST.equals(intent.getAction())) {
                ((MainActivity) getActivity()).getmBar().showWheel(true);
            }
            if (AlarmService.ACTION_PANEL.equals(intent.getAction())) {
                ((MainActivity) getActivity()).getmBar().showWheel(false);
                if (isLocked) return;
                mPanel = (Panel) intent.getSerializableExtra(AlarmService.EXTRA_PANEL);
                initViews(false);
            } else if (PanelManager.ACTION_PANEL_STATUS.equals(intent.getAction())) {
                ((MainActivity) getActivity()).getmBar().showWheel(false);
                int panelStatus = intent.getIntExtra(PanelManager.EXTRA_PANEL_STATUS, PanelManager.PANEL_STATUS_DISARM);
                mPanel.arm = panelStatus;
                initViews(false);
        }
        */
        }
    };
}
