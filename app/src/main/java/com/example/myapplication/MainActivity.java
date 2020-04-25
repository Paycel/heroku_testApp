package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceConfigurationError;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    TextView lastnameF;
    String answerHTTP;
    String lastnameS, firstnameS;
    private String SERV_URL = "http://192.168.0.158:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastnameF = (TextView) findViewById(R.id.lastnameF);
    }

    public void sendPOST(View view) {
        EditText lastname = (EditText) findViewById(R.id.lastname);
        EditText firstname = (EditText) findViewById(R.id.firstname);
        lastnameS = lastname.getText().toString();
        firstnameS = firstname.getText().toString();
        new Task(1).execute("");
    }

    public void sendGET(View view){
        new Task(2).execute("");
    }

    class Task extends AsyncTask<String, String, String> {
        private int mode;
        Task(int mode){
            this.mode = mode;
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            Response response = null;
            RequestBody requsestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("lastname", lastnameS)
                    .addFormDataPart("firstname", firstnameS)
                    .build();
            Request request = new Request.Builder()
                    .url(SERV_URL)
                    .post(requsestBody)
                    .build();
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null && response.code() == 200) {
                try {
                    answerHTTP = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            switch (mode) {
                case 1: lastnameF.setText("POST DATA = " + lastnameS + " " + firstnameS); break;
                case 2: lastnameF.setText("GET DATA = " + answerHTTP); break;
            }
        }
    }
}
