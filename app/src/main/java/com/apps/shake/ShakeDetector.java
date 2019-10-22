package com.apps.shake;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {
    private static final float SHAKE_THRESHOLD = 2.7f; //mendeteksi kecepatan shake
    private static final int SHAKE_TIME = 500;
    private static final int SHAKE_COUNT_RESET = 3000;

    private onShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;

    public interface onShakeListener {
        void onShake(int count);
    }

    public ShakeDetector(onShakeListener listener) {
        mListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mListener == null) return;

        float x = event.values[0] / SensorManager.GRAVITY_EARTH;
        float y = event.values[1] / SensorManager.GRAVITY_EARTH;
        float z = event.values[2] / SensorManager.GRAVITY_EARTH;
        float gForce = (float) Math.sqrt(x * x + y * y + z * z);
        if (gForce > SHAKE_THRESHOLD) {
            long now = System.currentTimeMillis();
            if (mShakeTimestamp + SHAKE_TIME > now) return;
            if (mShakeTimestamp + SHAKE_COUNT_RESET < now) {
                mShakeCount = 0;
            }
            mShakeTimestamp = now;
            mShakeCount++;
            mListener.onShake(mShakeCount);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
