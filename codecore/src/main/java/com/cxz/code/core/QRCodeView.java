package com.cxz.code.core;

import android.content.Context;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class QRCodeView extends RelativeLayout implements Camera.PreviewCallback  {

    protected Camera mCamera;
    protected CameraPreview mCameraPreview;
    protected ScanBoxView mScanBoxView;
    private Paint mPaint;

    public QRCodeView(Context context) {
        this(context, null);
    }

    public QRCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QRCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mCameraPreview = new CameraPreview(context);
        mScanBoxView = new ScanBoxView(context);
        mScanBoxView.init(this, attrs);
        mCameraPreview.setId(R.id.qrcode_camera_preview);
        addView(mCameraPreview);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(context, attrs);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, mCameraPreview.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, mCameraPreview.getId());
        addView(mScanBoxView,layoutParams);

        mPaint = new Paint();
        mPaint.setColor(mScanBoxView.getCornerColor());
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }
}
