package com.example.flashonoffshaking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int SHAKE_THRESHOLD = 800;
    SensorManager sensorManager;
    CameraManager cameraManager;
    Sensor sensor;
    float x , y ,z , last_x ,last_y , last_z;
    long lastUpdate;
    String cameraId;
    boolean flashIsOn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
    cameraId = null;
    flashIsOn = false;

        sensorManager.registerListener( this,sensor
,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                x = event.values[0];
                y = event.values[1];
                z = event.values[2];

                float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {

                    try {
                        cameraId = cameraManager.getCameraIdList()[0];
                        if (!flashIsOn) {
                            cameraManager.setTorchMode(cameraId, true);
                            flashIsOn = true;
                        }
                        else {
                            cameraManager.setTorchMode(cameraId, false);
                            flashIsOn = false;
                        }
                        } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
