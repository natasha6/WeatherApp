package com.example.park.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather mCurrentWeather;
//    private TextView mTempLabel;

    @InjectView(R.id.tempLabel) TextView mTempLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

//        mTempLabel = (TextView) findViewById(R.id.tempLabel);

        String apiKey = "be15834fe42cd10ed172bb1c4009ed9a";
        double latitude = 37.8267;
        double longitude = -122.423;

        String forecastURL = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String data = response.body().string();
                        Log.v(TAG, data);
                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentData(data);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateUserInterface();
                                }
                            });


                        } else {
                            alertAboutError();
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);

                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, "Network is not available!", Toast.LENGTH_LONG).show();
        }

    }

    private void updateUserInterface() {
        Log.d(TAG, mCurrentWeather.getTemperature()+"");
        mTempLabel.setText(mCurrentWeather.getTemperature() + "");


    }

    private CurrentWeather getCurrentData(String data) throws JSONException {
        JSONObject forecast = new JSONObject(data);
        String timezone = forecast.getString("timezone");
//        Log.i(TAG, "From JSON: " + timezone);
        //JSONObject
        JSONObject currently = forecast.getJSONObject("currently");
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTimezone(timezone);
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));

        Log.d(TAG, currentWeather.getProperTime());
        Log.d(TAG, currentWeather.getSummary());
        Log.d(TAG, currentWeather.getTemperature() + "");

        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvail = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvail = true;
        }
        return isAvail;
    }

    private void alertAboutError() {

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
