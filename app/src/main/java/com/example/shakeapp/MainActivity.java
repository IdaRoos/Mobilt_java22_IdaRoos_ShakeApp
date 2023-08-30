package com.example.shakeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView imageView;
    private Button button;
    private TextView textView;
    private FrameLayout frameLayout;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor humiditySensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referera till UI-komponenterna i layouten
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
       button = findViewById(R.id.button);
      frameLayout = findViewById(R.id.frameLayout);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);


        // När knappen klickas på visas fragmentets TextView
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().add(R.id.frameLayout, Fragment.class, null)
                        .commit();
            }
        });
    }

    // Hanterar sensordata och uppdaterar UI-komponenter baserat på sensorhändelser.
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // Beräkna bakgrundsfärg baserat på sensordata
            int backgroundColor = calculateBackgroundColor(x, y, z);
            frameLayout.setBackgroundColor(backgroundColor);

            // Beräkna rotationsvinkel baserat på sensordata
            float rotation = calculateRotation(x, y);

            // Uppdatera bilden med den nya rotationsvinkeln
            imageView.setRotation(rotation);
            button.setRotation(rotation);

            // Visa sensordata i TextView
            String sensorData = "X: " + x + "\nY: " + y + "\nZ: " + z;
            textView.setText(sensorData);

            // Kontrollera om sensordata indikerar en skakning
            if (isShakeDetected(x, y, z)) {
                Log.d("Shakeapp", "Skakning detekterad! " + sensorData);
            }
        }


        if (sensorEvent.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            float humidity = sensorEvent.values[0];

            if (humidity > 70) {
                findViewById(R.id.constraintlayout).setBackgroundColor(Color.RED);
                Toast.makeText(this, "HIGH Humidity", Toast.LENGTH_SHORT).show();

            } else {
                findViewById(R.id.constraintlayout).setBackgroundColor(Color.WHITE);
            }
        }
    }


    // Beräknar och returnerar den genomsnittliga rotationsvinkeln baserat på sensordata för X- och Y-axlarna
    private float calculateRotation(float x, float y) {

        float maxRotationDegrees = 3.0f;
        float scalingFactor = 1.5f;


        float rotationX = maxRotationDegrees * x * scalingFactor;
        float rotationY = maxRotationDegrees * y * scalingFactor;

        // Returnera den totala rotationsvinkeln
        return (rotationX + rotationY) / 2;
    }

    // Avregistrera lyssnare när appen går i pausläge
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        sensorManager.unregisterListener(this, humiditySensor);
    }

    // Registrera lyssnare igen när appen återupptas
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);

    }


    // Avgör om en skakning har detekterats baserat på sensordatan
    private boolean isShakeDetected(float x, float y, float z) {
        float threshold = 13f; // Värdet för skakningströskeln

        if (Math.abs(x) > threshold || Math.abs(y) > threshold || Math.abs(z) > threshold) {
            return true;
        } else {
            return false;
        }
    }

    // Beräknar en bakgrundsfärg baserat på sensordatan
    private int calculateBackgroundColor(float x, float y, float z) {
        int red = (int) (255 * (x + 1) / 2);
        int green = (int) (255 * (y + 1) / 2);
        int blue = (int) (255 * (z + 1) / 2);

        return Color.rgb(red, green, blue);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}


