package com.androguro.usb.horizon;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

     public static final String TAG = MainActivity.class.getSimpleName();

     private CurrentWeather currentWeather ;

     @BindView(R.id.timeLabel) TextView timeLabel;
     @BindView(R.id.tempLabel) TextView tempLabel;
     @BindView(R.id.humidityValue) TextView humidityValue;
    @BindView(R.id.precipValue) TextView precipValue;
    @BindView(R.id.summaryLabel) TextView summaryLabel;
    @BindView(R.id.iconImageView) ImageView iconImageView;
    @BindView(R.id.refreshImageView) ImageView refreshImageView;
    @BindView(R.id.progress) ProgressBar progressBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.INVISIBLE);
        final double latitude = 32.095; //37.8267;
        final double longitude=  20.188;//-122.4233;



        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getForecast(latitude,longitude);
            }
        });


        getForecast(latitude,longitude);
        Log.d(TAG,"Main UI is running !");

    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "3d37616cccc7819e3f2e8322549f47c9";
        String forecastUrl = "https://api.darksky.net/forecast/"+ apiKey +
                "/"+ latitude +"," + longitude ;


        if (isNetworkAvailable()) {
            toggleRefresh();

            /*
        1. When enqueue method is called its saying callback when you are done.
        2. When call is finished. The onResponse() callback method from the phone is executed.
        3. If there is an error then the onFailure method would work. */

            // Request a client with OKHTTP.
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    // Try / catch block.to see if response is successful.
                    try {
                        String jsonData = response.body().string(); //contains all JSON body.
                        Log.v(TAG,jsonData );

                        if (response.isSuccessful()) {

                            currentWeather = getCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertUserAboutError(); //method for creating dialog
                        }
                    }
                    catch (IOException e) { // If it fails with an IO exception, then run this code instead.
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }

                }
            });
        } else {
            Toast.makeText(this, R.string.network_unavailable,Toast.LENGTH_LONG).show();
            AlertDialogFragment dialog = new AlertDialogFragment();
            dialog.show(getFragmentManager(),"network_unavailable");
        }
    }

    private void toggleRefresh() {
        if (progressBar.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            refreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        tempLabel.setText(currentWeather.getTemperature() + "");
        timeLabel.setText("At " +currentWeather.getFormattedTime() + " it will be :");
        humidityValue.setText(currentWeather.getHumidity()+ "");
        precipValue.setText(currentWeather.getPrecipChance()+ "%");
        summaryLabel.setText(currentWeather.getSummary());

        Drawable drawble = getResources().getDrawable(currentWeather.getIconId());
        iconImageView.setImageDrawable(drawble);


    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG,"From JSON" +timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTimezone(timezone);

        Log.d(TAG,currentWeather.getFormattedTime());


        return currentWeather;
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        //Checking if there is a network and is it connected or not.
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getFragmentManager(),"error_dialog");

    }
}
