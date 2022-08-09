package com.example.healthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivityLog";
    private final String URL = "http://192.168.0.174:3000";

    private Retrofit retrofit;
    private ApiService service;

    RadioGroup RadioGroup1, RadioGroup2;
    RadioButton RB13, RB12, RB11, RB10, RB23, RB22, RB21, RB20;
    Button Button1;
    int q1 = 0;
    int q2 = 0;
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button1 = findViewById(R.id.btn1);
        RadioGroup1 = findViewById(R.id.rg_q1);
        RadioGroup2 = findViewById(R.id.rg_q2);
        RB13 = findViewById(R.id.q1_a3);
        RB12 = findViewById(R.id.q1_a2);
        RB11 = findViewById(R.id.q1_a1);
        RB10 = findViewById(R.id.q1_a0);
        RB23 = findViewById(R.id.q2_a3);
        RB22 = findViewById(R.id.q2_a2);
        RB21 = findViewById(R.id.q2_a1);
        RB20 = findViewById(R.id.q2_a0);

        /**
         * Init
         */
        retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        service = retrofit.create(ApiService.class);


        RadioGroup1.setOnCheckedChangeListener((RadioGroup1, i) -> {
            if(RB13.isChecked())
                q1 = 3;
            else if(RB12.isChecked())
                q1 = 2;
            else if(RB11.isChecked())
                q1 = 1;
            else
                q1 = 0;
            result = q1+q2;
        });
        RadioGroup2.setOnCheckedChangeListener((RadioGroup2, i) -> {
            if(RB23.isChecked())
                q2 = 3;
            else if(RB22.isChecked())
                q2 = 2;
            else if(RB21.isChecked())
                q2 = 1;
            else
                q2 = 0;
            result = q1+q2;
        });

        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resulttext=Integer.toString(result);;
                Toast.makeText(getApplicationContext(), "최종 점수는 "+resulttext+"점", Toast.LENGTH_SHORT).show();

                //여기부터 수정
                Call<ResponseBody> call_post = service.postFunc("post data");
                call_post.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                Log.v(TAG, "result = " + result);
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.v(TAG, "error = " + String.valueOf(response.code()));
                            Toast.makeText(getApplicationContext(), "error = " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v(TAG, "Fail");
                        Toast.makeText(getApplicationContext(), "Response Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}