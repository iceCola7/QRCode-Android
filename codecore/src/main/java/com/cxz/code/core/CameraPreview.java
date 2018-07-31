package com.cxz.code.core;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    /**
     * 自动对焦成功后，再次对焦的延迟
     */
    public static final long DEFAULT_AUTO_FOCUS_SUCCESS_DELAY = 1000L;

    /**
     * 自动对焦失败后，再次对焦的延迟
     */
    public static final long DEFAULT_AUTO_FOCUS_FAILURE_DELAY = 500L;

    private long mAutoFocusSuccessDelay = DEFAULT_AUTO_FOCUS_SUCCESS_DELAY;
    private long mAutoFocusFailureDelay = DEFAULT_AUTO_FOCUS_FAILURE_DELAY;
    private Camera mCamera;
    private CameraConfigurationManager mCameraConfigurationManager;
    private boolean mSurfaceCreated = false;
    private boolean mPreviewing = true;

    public CameraPreview(Context context) {
        super(context);
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        if (mCamera != null) {
            mCameraConfigurationManager = new CameraConfigurationManager(getContext());
            mCameraConfigurationManager.initFromCameraParameters(mCamera);
            getHolder().addCallback(this);

            if (mPreviewing) {
                requestLayout();
            } else {
                showCameraPreview();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceCreated = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null) {
            return;
        }
        stopCameraPreview();
        showCameraPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceCreated = false;
        stopCameraPreview();
    }

    private void showCameraPreview() {
        post(new Runnable() {
            @Override
            public void run() {
                if (mCamera != null) {
                    try {
                        mPreviewing = true;
                        mCamera.setPreviewDisplay(getHolder());
                        mCameraConfigurationManager.setDesiredCameraParameters(mCamera);
                        mCamera.startPreview();
                        mCamera.autoFocus(autoFocusCB);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void stopCameraPreview() {
        if (mCamera != null) {
            try {
                removeCallbacks(doAutoFocus);

                mPreviewing = false;
                mCamera.cancelAutoFocus();
                mCamera.setOneShotPreviewCallback(null);
                mCamera.stopPreview();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (mCamera != null && mPreviewing && mSurfaceCreated) {
                try {
                    mCamera.autoFocus(autoFocusCB);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                postDelayed(doAutoFocus, getAutoFocusSuccessDelay());
            } else {
                postDelayed(doAutoFocus, getAutoFocusFailureDelay());
            }
        }
    };

    private boolean flashLightAvailable() {
        return mCamera != null && mPreviewing && mSurfaceCreated && getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * 自动对焦成功后，再次对焦的延迟
     */
    public long getAutoFocusSuccessDelay() {
        return mAutoFocusSuccessDelay;
    }

    /**
     * 自动对焦成功后，再次对焦的延迟
     */
    public void setAutoFocusSuccessDelay(long autoFocusSuccessDelay) {
        mAutoFocusSuccessDelay = autoFocusSuccessDelay;
    }

    /**
     * 自动对焦失败后，再次对焦的延迟
     */
    public long getAutoFocusFailureDelay() {
        return mAutoFocusFailureDelay;
    }

    /**
     * 自动对焦失败后，再次对焦的延迟
     */
    public void setAutoFocusFailureDelay(long autoFocusFailureDelay) {
        mAutoFocusFailureDelay = autoFocusFailureDelay;
    }

    /**
     * 打开闪光灯
     */
    public void openFlashlight() {
        if (flashLightAvailable()) {
            mCameraConfigurationManager.openFlashlight(mCamera);
        }
    }

    /**
     * 关闭闪光灯
     */
    public void closeFlashlight() {
        if (flashLightAvailable()) {
            mCameraConfigurationManager.closeFlashlight(mCamera);
        }
    }

}
