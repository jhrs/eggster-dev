package com.bell_inter.eggster;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EggMainActivity extends AppCompatActivity implements SensorEventListener
{
    // properties...

    // constants...
    private static final String TAG = new String("EggMainActivity");

    // controls...
    Button mButtonGo;
    Spinner mSpinnerInitialEggTemperature;
    EditText mEditTextTemperature, mEditTextSize, mEditTextCookState;

    // sensor(s)...
    private SensorManager mSensorManager;
    private Sensor mPressure;
    private double mPressureReading;

    // variables...
    double mTemperature, mSize, mCookState;

    // methods...
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // Do something here if sensor accuracy changes.

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "->onCreate(Bundle savedInstanceState)");

        // android shit...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egg_main);

        // hook up controls...
        mButtonGo = (Button) findViewById(R.id.ButtonGo);
        mSpinnerInitialEggTemperature = (Spinner) findViewById(R.id.SpinnerInitialEggTemperature);
        //mEditTextTemperature = (EditText) findViewById(R.id.EditTextEggTemperature);
        //mEditTextSize = (EditText) findViewById(R.id.EditTextEggSize);
        //mEditTextCookState = (EditText) findViewById(R.id.EditTextEggCookState);

        // sensor(s)
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        // listeners...
        // spinners...
        mSpinnerInitialEggTemperature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Log.d(TAG, "->onItemSelected(AdapterView<?> parent, View view, int pos, long id)");

                Object item = parent.getItemAtPosition(pos);
                Log.d(TAG, "Selection index : " +Integer.toString(pos));

                if(pos == 0)
                {
                    Log.d(TAG, "Tripped on selection");

                }

                Log.d(TAG, "onItemSelected(AdapterView<?> parent, View view, int pos, long id)->");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                Log.d(TAG, "->onNothingSelected(AdapterView<?> parent, View view, int pos, long id)");
                Log.d(TAG, "onNothingSelected(AdapterView<?> parent, View view, int pos, long id)->");
            }

        });

        // drop-downs (can't say spinners!)...
        // egg temperature...
        String[] EggTemperature = new String[]  // TO DO :
                {
                    "Ambient (Detect)",
                    "Ambient (Default)",
                    "Chilled (Detect)",
                    "Chilled (Default)",
                    "Enter Temperature (C)..."

                };
        //getResources().getStringArray(R.array.initialEggTemperature);
        //mSpinnerInitialEggTemperature = (Spinner)findViewById(R.id.SpinnerInitialEggTemperature);
        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_serial,EggTemperature);
        mSpinnerInitialEggTemperature.setAdapter(spinnerArrayAdapter);
        mSpinnerInitialEggTemperature.setSelection(0);

        // egg size...
        Spinner EggSize = (Spinner)findViewById(R.id.SpinnerEggSize);
        EggSize.setSelection(1);
        // egg size...
        Spinner EggCookState = (Spinner)findViewById(R.id.SpinnerInitialEggTemperature);
        EggCookState.setSelection(3);

        Log.d(TAG, "onCreate(Bundle savedInstanceState)->");

    }

    @Override
    public final void onSensorChanged(SensorEvent event)
    {
        float millibars_of_pressure = event.values[0];
        // Do something with this sensor data.
        //ShowToast(Float.toString(millibars_of_pressure), Toast.LENGTH_SHORT);
        mPressureReading = (double) millibars_of_pressure;

    }

    @Override
    protected void onResume()
    {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause()
    {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    public void OnGo (View aView)
    {
        //ShowToast("->OnGo (View aView)", Toast.LENGTH_SHORT);
        Log.d(TAG, "->OnGo (View aView)");

        Log.d(TAG, "->mSensorManager");
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Log.d(TAG, "->mPressure");
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        Log.d(TAG, "->mTemperature");
        //mTemperature = Double.parseDouble(mSpinnerInitialEggTemperature.getSelectedItem().toString());
        Log.d(TAG, "->mSize");
        mSize = Double.parseDouble(mEditTextSize.getText().toString());
        Log.d(TAG, "->mCookState");
        mCookState = Double.parseDouble(mEditTextCookState.getText().toString());

        String InputConfirmation;
        InputConfirmation = ("Temperature: " + mEditTextTemperature.getText().toString());
        InputConfirmation += ("; Size : " + mEditTextSize.getText().toString());
        InputConfirmation += ("; Cook State: " + mEditTextCookState.getText().toString());
        InputConfirmation += ("; Local BMP: " + Double.toString(mPressureReading));

        Log.d(TAG, "InputConfirmation : " + InputConfirmation);
        ShowToast(InputConfirmation, Toast.LENGTH_SHORT);

        Log.d(TAG, "->CEgg Egg");
        CEgg Egg = new CEgg(mSize, mTemperature, mCookState, mPressureReading);
        double cooktime = Egg.GetCookTime();
        ShowToast("Cooktime : " + Double.toString(cooktime), Toast.LENGTH_SHORT);
        Log.d(TAG, "Returned cook time = " + cooktime);

        //ShowToast("OnGo (View aView)->", Toast.LENGTH_SHORT);
        Log.d(TAG, "OnGo (View aView)->");

    }

    protected void ShowToast(String aMessage, int aDuration)
    {
        Context context = getApplicationContext();
        CharSequence text = (String) aMessage;

        Toast toast = Toast.makeText(context, text, aDuration);
        toast.show();

    }

}
