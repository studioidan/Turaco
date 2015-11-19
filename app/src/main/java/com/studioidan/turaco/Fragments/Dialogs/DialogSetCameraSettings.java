package com.studioidan.turaco.Fragments.Dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewAnimator;

import com.squareup.picasso.Picasso;
import com.studioidan.turaco.Adapters.AdapterGallery;
import com.studioidan.turaco.R;
import com.studioidan.turaco.entities.Camera;
import com.studioidan.turaco.entities.CameraManager;
import com.studioidan.turaco.singeltones.DataStore;

/**
 * Created by PopApp_laptop on 18/11/2015.
 */
public class DialogSetCameraSettings extends DialogFragment implements View.OnClickListener, AdapterGallery.GalleryCallback {

    public static final String ARG_CAMERA_POSITION = "arg.camera";
    private Camera mCamera;
    private boolean isIconSourceUrl = false;
    private int iconResource;


    EditText etCameraName, etCameraVideoLink, etCameraImageUrl;
    Button btnIconSource, btnSave, btnLoadUrl;
    RecyclerView rv;
    ImageView imgIcon;
    ViewAnimator viewAnimator;

    public static DialogSetCameraSettings getInstance(int position) {
        DialogSetCameraSettings answer = new DialogSetCameraSettings();
        Bundle bundle = new Bundle(1);
        bundle.putInt(ARG_CAMERA_POSITION, position);
        answer.setArguments(bundle);
        return answer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = getArguments().getInt(ARG_CAMERA_POSITION);
        mCamera = DataStore.getInstance().getCameras().get(position);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.camer_setup_dilog_layout, container);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etCameraName = (EditText) view.findViewById(R.id.etCameraName);
        etCameraVideoLink = (EditText) view.findViewById(R.id.etCameraVideoUrl);
        etCameraImageUrl = (EditText) view.findViewById(R.id.etCameraImageUrl);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
        btnIconSource = (Button) view.findViewById(R.id.btnIconSource);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnLoadUrl = (Button) view.findViewById(R.id.btnLoadUrlIcon);
        viewAnimator = (ViewAnimator) view.findViewById(R.id.viewAnimator);
        viewAnimator.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
        viewAnimator.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right));
        setAdapter();

        btnIconSource.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnLoadUrl.setOnClickListener(this);


        etCameraName.setText(mCamera.name);
        etCameraVideoLink.setText(mCamera.url);
        etCameraImageUrl.setText(mCamera.imageUrl);
        iconResource = mCamera.image;

        if (mCamera.imageUrl != null && !mCamera.imageUrl.isEmpty()) {
            isIconSourceUrl = true;
            viewAnimator.showNext();
        }

        updateIconView();
    }

    private void setAdapter() {
        rv.setAdapter(new AdapterGallery(getActivity(), CameraManager.cameraIcons).setCallback(this));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(llm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                /*save camera data*/
                mCamera.image = isIconSourceUrl ? 0 : iconResource;
                mCamera.imageUrl = isIconSourceUrl ? etCameraImageUrl.getText().toString().trim() : "";

                mCamera.url = etCameraVideoLink.getText().toString().trim();
                mCamera.name = etCameraName.getText().toString().trim();

                CameraManager.getInstance().SaveCameras();

                dismiss();
                break;
            case R.id.btnLoadUrlIcon:
                updateIconView();
                break;
            case R.id.btnIconSource:
                isIconSourceUrl = !isIconSourceUrl;
                updateIconView();
                viewAnimator.showNext();
                break;
        }
    }

    private void updateIconView() {
        btnIconSource.setText(isIconSourceUrl ? "Local Image" : "Url Source");
        if (isIconSourceUrl) {
            String url = etCameraImageUrl.getText().toString().trim();
            if (url.length() == 0) {
                imgIcon.setImageResource(0);
                return;
            }
            Picasso.with(getActivity())
                    .load(url)
                    .into(imgIcon);
        } else {
            if (iconResource == 0) {
                imgIcon.setImageResource(0);
                return;
            }
            Picasso.with(getActivity())
                    .load(iconResource)
                    .into(imgIcon);
        }
    }

    @Override
    public void onImageClick(int pos) {
        iconResource = CameraManager.cameraIconsSelected[pos];
        imgIcon.setImageResource(iconResource);
    }
}
