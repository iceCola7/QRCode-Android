package com.cxz.code.core;

import android.graphics.PointF;

public class ScanResult {
    String result;
    PointF[] resultPoints;

    public ScanResult(String result) {
        this.result = result;
    }

    public ScanResult(String result, PointF[] resultPoints) {
        this.result = result;
        this.resultPoints = resultPoints;
    }

}
