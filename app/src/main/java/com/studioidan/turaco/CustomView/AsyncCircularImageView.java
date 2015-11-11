package com.studioidan.turaco.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.studioidan.turaco.Model.Camera;
import com.studioidan.turaco.R;
import com.studioidan.turaco.connection.manager.CamerasManager;

import java.util.List;


/**
 * Created by macbook on 8/20/15.
 */
public class AsyncCircularImageView extends ImageView implements OnPropertyChanged{
    DisplayImageOptions displayImageOptions;
    String uri;
    public AsyncCircularImageView(Context context)
    {
        super(context);
    }

    public AsyncCircularImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AsyncCircularImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
//        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.AsyncCircularImageView);
//
//        arr.recycle();
        initImageLoaderOptions();
    }

    public void setImageURIAsync(String uri)
    {
        initImageLoaderOptions();
//        if (this.uri == null || !this.uri.equals(uri))
        {
            this.uri = uri;

            ImageLoader.getInstance().displayImage(uri, this, displayImageOptions);
        }
    }

    protected void initImageLoaderOptions()
    {
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo_nav)
                .showImageForEmptyUri(R.drawable.logo_nav)
                .showImageOnFail(R.drawable.logo_nav)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(100))
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().defaultDisplayImageOptions(displayImageOptions)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();





        ImageLoader.getInstance().init(config);
    }

    @Override
    public void OnPropertyChanged(ProperyChanges mComingProperty,int mcomingposition) {
        if(mComingProperty.equals(ProperyChanges.CameraPictureUpdate)){
            if(getTag()!= null){
            setImageURIAsync(((Camera)((List) CamerasManager.getSharedManager(getContext()).getManagers()).get(mcomingposition)).getPictureUrl());
            }
        }
    }
}

