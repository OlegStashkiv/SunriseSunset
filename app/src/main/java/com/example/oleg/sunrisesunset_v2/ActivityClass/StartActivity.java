package com.example.oleg.sunrisesunset_v2.ActivityClass;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.oleg.sunrisesunset_v2.R;


public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button b_default = (Button) findViewById(R.id.b_default);
        Button b_search = (Button) findViewById(R.id.b_search);

        b_default.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            }
        });

        b_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, SetOptions.class);
                startActivity(intent);
            }
        });


    }

}
