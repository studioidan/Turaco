package com.studioidan.turaco.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.studioidan.turaco.Adapters.AdapterCamera;
import com.studioidan.turaco.CustomView.AsyncCircularImageView;
import com.studioidan.turaco.Fragments.Dialogs.DialogSetCameraSettings;
import com.studioidan.turaco.Model.Camera;
import com.studioidan.turaco.R;
import com.studioidan.turaco.connection.APIErrorManager;
import com.studioidan.turaco.connection.SignalManager;
import com.studioidan.turaco.connection.manager.CamerasManager;

import java.util.List;

/**
 * Created by macbook on 8/30/15.
 */
public class SetupCameraFragment extends BaseFragment implements View.OnClickListener, SignalManager.SignalCompletedObserver, SignalManager.SignalLoadingObserver, SignalManager.SignalErrorObserver {

    TextView mChange2Cameras, mChangeAllCameras;
    ImageView mImageCamera2camer, mImageCamera2All;
    GridView mGrid;
    AdapterCamera mAdapter;

    public static SetupCameraFragment newInstance() {
        return new SetupCameraFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.setup_camera_layout, container, false);

        mChange2Cameras = (TextView) mView.findViewById(R.id.tv_changecamer2camera);
        mChangeAllCameras = (TextView) mView.findViewById(R.id.tv_changeCamerAll);

        mImageCamera2All = (ImageView) mView.findViewById(R.id.imv_camera2all);
        mImageCamera2camer = (ImageView) mView.findViewById(R.id.imv_caner2acamer);
        mGrid = (GridView) mView.findViewById(R.id.gv_cameralist);

        mChangeAllCameras.setOnClickListener(this);
        mChange2Cameras.setOnClickListener(this);
        mImageCamera2All.setOnClickListener(this);
        mImageCamera2camer.setOnClickListener(this);

        mAdapter = new AdapterCamera(ds.getCameras(), getActivity());
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startChagneCameraUrl(position);

                DialogSetCameraSettings dialogSetCameraSettings = DialogSetCameraSettings.getInstance(position);
                dialogSetCameraSettings.show(getChildFragmentManager(), "TAG");
            }
        });

        return mView;
    }

    AlertDialog mDialog;

    @Override
    public void onClick(View view) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle(R.string.camera_changeurl);

        switch (view.getId()) {
            case R.id.imv_camera2all:
            case R.id.tv_changeCamerAll:

                LinearLayout mLayout = new LinearLayout(getActivity());
                mLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                TextView mCameraNumbmer = new TextView(getActivity());
                mCameraNumbmer.setText("Camera Source ID: ");

                final EditText mText = new EditText(getActivity());
                mText.setInputType(InputType.TYPE_CLASS_NUMBER);
                mText.setWidth(100);
                mLayout.addView(mCameraNumbmer);
                mLayout.addView(mText);

                Button mButton2 = new Button(getActivity());
                mButton2.setText("Change Url");
                mButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String m = mText.getText().toString();
                        if (m.isEmpty()) {
                            mText.setError("*");
                        } else if (Integer.parseInt(m) > CamerasManager.getSharedManager(getActivity()).getManagers().size()) {
                            mText.setError("ID does not exist");
                        } else {
                            CamerasManager.getSharedManager(getActivity()).changeCamera(((Camera) ((List) CamerasManager.getSharedManager(getActivity()).getManagers()).get(Integer.parseInt(m))), null, 0, 0);
                            mAdapter.notifyDataSetChanged();
                            mDialog.dismiss();
                        }
                    }
                });
                mLayout.addView(mButton2);
                mBuilder.setView(mLayout);
                break;
            case R.id.tv_changecamer2camera:
            case R.id.imv_caner2acamer:
                LinearLayout mLayout2 = new LinearLayout(getActivity());
                mLayout2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                TextView mCameraNumbmer1 = new TextView(getActivity());
                mCameraNumbmer1.setText("Camera 1: ");


                TextView mCameraNumbmer2 = new TextView(getActivity());
                mCameraNumbmer2.setText("Camera 2:");

                final EditText mText1 = new EditText(getActivity());
                mText1.setInputType(InputType.TYPE_CLASS_NUMBER);
                mLayout2.addView(mCameraNumbmer1);
                mLayout2.addView(mText1);
                mText1.setWidth(100);
                final EditText mText2 = new EditText(getActivity());
                mText2.setInputType(InputType.TYPE_CLASS_NUMBER);
                mLayout2.addView(mCameraNumbmer2);
                mLayout2.addView(mText2);
                mText2.setWidth(100);
                Button mButton = new Button(getActivity());
                mButton.setText("Change Url");
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String m1 = mText1.getText().toString();
                        String m2 = mText2.getText().toString();

                        if (m1.isEmpty()) {
                            mText1.setError("*");
                        } else if (m2.isEmpty()) {
                            mText2.setError("*");
                        } else if (Integer.parseInt(m1) > CamerasManager.getSharedManager(getActivity()).getManagers().size()) {
                            mText1.setError("ID does not exist");
                        } else if (Integer.parseInt(m2) > CamerasManager.getSharedManager(getActivity()).getManagers().size()) {
                            mText2.setError("ID does not exist");
                        } else {
                            CamerasManager.getSharedManager(getActivity()).changeCamera((Camera) ((List) CamerasManager.getSharedManager(getActivity()).getManagers()).get(Integer.parseInt(m1))
                                    , ((Camera) ((List) CamerasManager.getSharedManager(getActivity()).getManagers()).get(Integer.parseInt(m2)))
                                    , Integer.parseInt(m1), Integer.parseInt(m2));
                            mAdapter.notifyDataSetChanged();
                            mDialog.dismiss();
                        }
                    }
                });
                mLayout2.addView(mButton);
                mBuilder.setView(mLayout2);
                break;
        }

        mDialog = mBuilder.create();
        mDialog.show();
    }

    @Override
    public void onCompleted(SignalManager signal, boolean completed) {

    }

    @Override
    public void onError(SignalManager signal, APIErrorManager error) {

    }

    @Override
    public void onLoading(SignalManager signal, boolean loading) {

    }

    EditText mCameraLink, mCameraName, mPictureLink;
    ImageView mIconImage;
    TextView mIconLbl;
    AlertDialog mIconDialog = null;

    private void startChagneCameraUrl(final int position) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        final ScrollView mView = (ScrollView) LayoutInflater.from(getActivity()).inflate(R.layout.camer_setup_dilog_layout, null);
        mCameraName = (EditText) mView.findViewById(R.id.etCameraImageUrl);
        mCameraName.setText(((Camera) ((List) CamerasManager.getSharedManager(getActivity()).getManagers()).get(position)).getTitle());
        mCameraLink = (EditText) mView.findViewById(R.id.etCameraImageUrl);
        mCameraLink.setText(((Camera) ((List) CamerasManager.getSharedManager(getActivity()).getManagers()).get(position)).getCameraUrl());
        mPictureLink = (EditText) mView.findViewById(R.id.etCameraImageUrl);
        mIconImage = (ImageView) mView.findViewById(R.id.camer_icon_setup_layout);
        mIconLbl = (TextView) mView.findViewById(R.id.lbl_camera_name);
        if (((Camera) ((List) CamerasManager.getSharedManager(getActivity()).getManagers()).get(position)).getmIconLogo() != 0) {
            mPictureLink.setVisibility(View.INVISIBLE);
            mIconLbl.setVisibility(View.VISIBLE);
            mIconLbl.setText(mCameraName.getText().toString());
            mIconImage.setVisibility(View.VISIBLE);
            mIconImage.setBackgroundResource(((Camera) ((List) CamerasManager.getSharedManager(getActivity()).getManagers()).get(position)).getmIconLogo());
        } else {
            mPictureLink.setText(((Camera) ((List) CamerasManager.getSharedManager(getActivity()).getManagers()).get(position)).getPictureUrl());
        }


        Button mButton = (Button) mView.findViewById(R.id.btnLoadIcon);
        Button mBloadICon = (Button) mView.findViewById(R.id.btnPanelAddressSave);
        mBloadICon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPictureLink.setVisibility(View.INVISIBLE);
                mPictureLink.setText("");
                mIconLbl.setVisibility(View.VISIBLE);
                mIconLbl.setText(mCameraName.getText().toString());

                mIconImage.setVisibility(View.VISIBLE);
                AlertDialog.Builder mIconBuilder = new AlertDialog.Builder(getActivity());

                LinearLayout mLinearLayout = new LinearLayout(getActivity());
                LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                mLinearLayout.setLayoutParams(mParams);
                mLinearLayout.setBackgroundColor(getResources().getColor(R.color.background_dark_blue));
                mLinearLayout.setOrientation(LinearLayout.VERTICAL);
                HorizontalScrollView mscrollView = new HorizontalScrollView(getActivity());
                LinearLayout mImageContainer = new LinearLayout(getActivity());
                LinearLayout.LayoutParams mImageContainerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mImageContainer.setPadding(10, 10, 10, 10);
                mImageContainer.setLayoutParams(mImageContainerParams);
                mImageContainer.setOrientation(LinearLayout.HORIZONTAL);
                int hieght = 0, width = 0;
                for (int i = 0; i < CamerasManager.getSharedManager(getActivity()).getmPhotos().length; i++) {
                    AsyncCircularImageView mImageView = (AsyncCircularImageView) LayoutInflater.from(getActivity()).inflate(R.layout.icon_item_layout, null);
                    mImageView.setBackgroundResource(CamerasManager.getSharedManager(getActivity()).getmPhotos()[i]);

                    LinearLayout.LayoutParams mParamss = new LinearLayout.LayoutParams(140, 140);
                    mParamss.setMargins(10, 10, 10, 10);
                    mImageView.setLayoutParams(mParamss);

                    final int j = i;
                    mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            mIconImage.setBackground(((ImageView)view).getDrawable());
                            mIconImage.setBackgroundResource(CamerasManager.getSharedManager(getActivity()).getmSelectedPhotos()[j]);
                            mIconImage.setTag(CamerasManager.getSharedManager(getActivity()).getmPhotos()[j]);
                            mIconLbl.setText(CamerasManager.getSharedManager(getActivity()).getmCameraName()[j]);
                            mPictureLink.setText("");
                            mIconDialog.dismiss();
                        }
                    });
                    mImageContainer.addView(mImageView);

                }
                mscrollView.addView(mImageContainer);
                mLinearLayout.addView(mscrollView);

                mIconBuilder.setView(mLinearLayout);
                mIconDialog = mIconBuilder.create();
                mIconDialog.show();


            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraLink.getText().toString().contains("=disarm")) {
                    Toast.makeText(mCameraLink.getContext(), "please use arm panel url", Toast.LENGTH_SHORT).show();
                } else {
                    if (((Camera) ((List) CamerasManager.getSharedManager(getActivity()).getManagers()).get(position)).getCameraUrl() != mCameraLink.getText().toString()) {
                        String mg = mCameraLink.getText().toString();
                        String mName = mCameraName.getText().toString();
                        CamerasManager.getSharedManager(getActivity()).updateCameraInfo(position, mg, mName);//.onCompleted().onLoading().onError()
                        //if (mPictureLink.getVisibility() == View.VISIBLE) {
                        String mPictureLinkd = mPictureLink.getText().toString();
                        CamerasManager.getSharedManager(getActivity()).updateCameraPictureLink(position, mPictureLinkd);
                        //} else {
                        if (mIconImage.getVisibility() == View.VISIBLE) {
                            int mIconImageResource = (int) mIconImage.getTag();
                            CamerasManager.getSharedManager(getActivity()).updateCameraIconResource(position, mIconImageResource);
                        } else {
                            CamerasManager.getSharedManager(getActivity()).updateCameraIconResource(position, -1);
                        }

                        mAdapter.notifyDataSetChanged();
                        mDialog.dismiss();
                    }
                }
            }
        });

        mView.setBackgroundResource(android.R.color.white);
        mBuilder.setView(mView);


        mDialog = mBuilder.create();
        mDialog.show();

    }

    @Override
    public String tag() {
        return getClass().getName();
    }
}
