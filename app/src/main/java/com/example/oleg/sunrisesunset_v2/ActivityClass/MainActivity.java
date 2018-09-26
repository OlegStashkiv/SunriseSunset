package com.example.oleg.sunrisesunset_v2.ActivityClass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oleg.sunrisesunset_v2.SunriseSunsetApi.PostList;
import com.example.oleg.sunrisesunset_v2.R;
import com.example.oleg.sunrisesunset_v2.SunriseSunsetApi.SandSApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView sunrise, sunset, solar_noon, day_length, civil_twilight_begin, civil_twilight_end,
            nautical_twilight_begin, nautical_twilight_end, astronomical_twilight_begin, astronomical_twilight_end;

    private LocationManager locationManager;
    private LinearLayout content_ss;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sunrise = (TextView) findViewById(R.id.sunrise);
        sunset = (TextView) findViewById(R.id.sunset);
        solar_noon = (TextView) findViewById(R.id.solar_noon);
        day_length = (TextView) findViewById(R.id.day_length);
        civil_twilight_begin = (TextView) findViewById(R.id.civil_twilight_begin);
        civil_twilight_end = (TextView) findViewById(R.id.civil_twilight_end);
        nautical_twilight_begin = (TextView) findViewById(R.id.nautical_twilight_begin);
        nautical_twilight_end = (TextView) findViewById(R.id.nautical_twilight_end);
        astronomical_twilight_begin = (TextView) findViewById(R.id.astronomical_twilight_begin);
        astronomical_twilight_end = (TextView) findViewById(R.id.astronomical_twilight_end);
        content_ss = (LinearLayout) findViewById(R.id.content_ss);

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle(getString(R.string.string_getting_json_title));
        dialog.setMessage(getString(R.string.string_getting_json_message));
        dialog.show();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }


    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    return;
        }
    }
    @Override

    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                locationListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            getData(Double.toString(latitude), Double.toString(longitude));
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    private void getData(String lat, String lng){
        Call<PostList> call = SandSApi.getPostService(SandSApi.url).getPostCity(lat, lng);
        call.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(@NonNull Call<PostList> call, @NonNull Response<PostList> response) {
                if(response.isSuccessful()){
                    PostList resultslist = response.body();

                    dialog.dismiss();
                    content_ss.setVisibility(View.VISIBLE);

                    sunrise.setText(resultslist.getResults().getSunrise());
                    sunset.setText(resultslist.getResults().getSunset());
                    solar_noon.setText(resultslist.getResults().getSolarNoon());
                    day_length.setText(resultslist.getResults().getDayLength());
                    civil_twilight_begin.setText(resultslist.getResults().getCivilTwilightBegin());
                    civil_twilight_end.setText(resultslist.getResults().getCivilTwilightEnd());
                    nautical_twilight_begin.setText(resultslist.getResults().getNauticalTwilightBegin());
                    nautical_twilight_end.setText(resultslist.getResults().getNauticalTwilightEnd());
                    astronomical_twilight_begin.setText(resultslist.getResults().getAstronomicalTwilightBegin());
                    astronomical_twilight_end.setText(resultslist.getResults().getAstronomicalTwilightEnd());
                }
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
