package com.cxz.qrcode_android;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cxz.code.core.QRCodeView;
import com.cxz.zarlibrary.ZbarView;

public class ScanActivity extends AppCompatActivity implements QRCodeView.Delegate {

    ZbarView zbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        zbarView = findViewById(R.id.zbarView);

        zbarView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        zbarView.startCamera();
        zbarView.startSpotAndShowRect();
    }

    @Override
    protected void onStop() {
        zbarView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zbarView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        zbarView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

}
