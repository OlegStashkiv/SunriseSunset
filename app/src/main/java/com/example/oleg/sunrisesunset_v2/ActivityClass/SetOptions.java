package com.example.oleg.sunrisesunset_v2.ActivityClass;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oleg.sunrisesunset_v2.GoogleMapApi.CordinatesList;
import com.example.oleg.sunrisesunset_v2.GoogleMapApi.Geometry;
import com.example.oleg.sunrisesunset_v2.GoogleMapApi.GoogleMapApi;
import com.example.oleg.sunrisesunset_v2.GoogleMapApi.Result;
import com.example.oleg.sunrisesunset_v2.R;
import com.example.oleg.sunrisesunset_v2.SunriseSunsetApi.PostList;
import com.example.oleg.sunrisesunset_v2.SunriseSunsetApi.SandSApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.oleg.sunrisesunset_v2.GoogleMapApi.GoogleMapApi.KEY;

public class SetOptions extends AppCompatActivity {

    private EditText city_name;
    private TextView sunrise;
    private TextView sunset;
    private TextView solar_noon;
    private TextView day_length;
    private TextView civil_twilight_begin;
    private TextView civil_twilight_end;
    private TextView nautical_twilight_begin;
    private TextView nautical_twilight_end;
    private TextView astronomical_twilight_begin;
    private TextView astronomical_twilight_end;
    private LinearLayout content;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_options);

        sunrise = (TextView) findViewById(R.id.sunrise);
        sunset = (TextView) findViewById(R.id.sunset);
        solar_noon = (TextView) findViewById(R.id.solar_noon);
        day_length = (TextView) findViewById(R.id.day_length);
        civil_twilight_begin = (TextView) findViewById(R.id.civil_twilight_begin);
        civil_twilight_end = (TextView) findViewById(R.id.civil_twilight_end);
        nautical_twilight_begin = (TextView) findViewById(R.id.nautical_twilight_begin);
        nautical_twilight_end= (TextView) findViewById(R.id.nautical_twilight_end);
        astronomical_twilight_begin = (TextView) findViewById(R.id.astronomical_twilight_begin);
        astronomical_twilight_end= (TextView) findViewById(R.id.astronomical_twilight_end);

        content = (LinearLayout) findViewById(R.id.content);

        city_name = (EditText) findViewById(R.id.city_name);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String address =  city_name.getText().toString();
                dialog = new ProgressDialog(SetOptions.this);
                dialog.setTitle(getString(R.string.string_getting_json_title));
                dialog.setMessage(getString(R.string.string_getting_json_message));
                dialog.show();

              Call<CordinatesList> call = GoogleMapApi.getgoogleService(GoogleMapApi.g_m_url).getAdress( address , KEY);
              call.enqueue(new Callback<CordinatesList>() {
                  @Override
                  public void onResponse(Call<CordinatesList> call, Response<CordinatesList> response) {
                      if(response.isSuccessful()){
                          CordinatesList cordinatesList =  response.body();

                          Result result = new Result();

                          Double l =  cordinatesList.getResults().listIterator().next().getGeometry().getLocation().getLat();
                          Double n =  cordinatesList.getResults().listIterator().next().getGeometry().getLocation().getLng();
                          String lat = Double.toString(l);
                          String lng = Double.toString(n);
                          getInformation(lat, lng);


                      Toast.makeText(SetOptions.this, "Success", Toast.LENGTH_SHORT).show();}
                      else {
                          Toast.makeText(SetOptions.this, "NOTFound", Toast.LENGTH_SHORT).show();
                      }
                  }

                  @Override
                  public void onFailure(Call<CordinatesList> call, Throwable t) {
                      Toast.makeText(SetOptions.this, "Error" + t, Toast.LENGTH_SHORT).show();

                  }
              });
            }
        });


    }

    public void getInformation(String lat, String lng){

        Call<PostList> call = SandSApi.getPostService(SandSApi.url).getPostCity(lat, lng);
        call.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(@NonNull Call<PostList> call, @NonNull Response<PostList> response) {
                if(response.isSuccessful()){
                    PostList resultslist = response.body();
                    dialog.dismiss();
                    content.setVisibility(View.VISIBLE);
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
                Toast.makeText(SetOptions.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
