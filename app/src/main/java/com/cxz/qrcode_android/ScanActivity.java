package com.cxz.qrcode_android;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cxz.code.core.BarcodeType;
import com.cxz.code.core.QRCodeView;
import com.cxz.zarlibrary.BarcodeFormat;
import com.cxz.zarlibrary.ZBarView;

import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends AppCompatActivity implements QRCodeView.Delegate {

    private static final String TAG = "ScanActivity";
    ZBarView zbarView;
    Button btn_scan_qrcode, btn_scan_barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        btn_scan_qrcode = findViewById(R.id.btn_scan_qrcode);
        btn_scan_barcode = findViewById(R.id.btn_scan_barcode);
        zbarView = findViewById(R.id.zbarView);
        zbarView.setDelegate(this);

        btn_scan_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zbarView.changeToScanQRCodeStyle();
                zbarView.setType(BarcodeType.ONLY_QR_CODE, null);
                zbarView.startSpotAndShowRect();
            }
        });

        btn_scan_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zbarView.changeToScanBarcodeStyle();
                List<BarcodeFormat> list = new ArrayList<>();
                list.add(BarcodeFormat.CODE128);
                list.add(BarcodeFormat.CODE93);
                zbarView.setType(BarcodeType.CUSTOM, list);
                zbarView.startSpotAndShowRect();
            }
        });

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
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = zbarView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                zbarView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                zbarView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

}
