package com.example.ahmet.findme;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Info extends AppCompatActivity {

    private final DbHelper dbHelper = new DbHelper(this);
    private double locLat;
    private double locLong;
    private String sLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent i = getIntent();

        locLat = i.getDoubleExtra("Latitude",0);
        locLong = i.getDoubleExtra("Longitude",0);
        String elevation=i.getStringExtra("Elevation");
        setContentView(R.layout.activity_info);
        String sInfo;
        TextView txtLocationInfo;
        txtLocationInfo = (TextView) findViewById(R.id.txtLocationInfo);
        sLocation=getLocation();
        if(sLocation.equals("")){
            sInfo="Location data could not be obtained.";
        }
        else {

            sInfo = "You are in "+ sLocation;
            String sElevation=getElevation(elevation);
            if (!sElevation.equals(""))
                sInfo += ", currently " + sElevation + "m above sea level";
            String sTemperature=getTemperature();
            if (!sTemperature.equals("")) {
                sInfo += ", and the local temperature is " + sTemperature + " degrees Celsius.";
            }
            else{
                sInfo+=" (the temperature couldn't be obtained)";
            }
            txtLocationInfo.setText(sInfo);
            if(!sLocation.equals("")&&!sTemperature.equals("")) {
                dbHelper.insertData(sLocation,sTemperature);
            }
        }
    }
    private String getLocation() {
        Geocoder g = new Geocoder(this);
        try {
            List<Address> address = g.getFromLocation(locLat, locLong, 1);
            if (address.isEmpty()) {
                return "";
            }
            return address.get(0).getLocality();
        } catch (IOException e) {
            return "";
        }
    }

    private String getTemperature() {

        String response = "";
        try {
            String request = "http://api.openweathermap.org/data/2.5/weather?q=" + sLocation + "&units=metric&type=like&APPID=a672a5a287766b05630790d1839df7eb";
            JsonReader jReader = new DownloadJSONTask().execute(request).get();
            jReader.beginObject();
            while (jReader.hasNext()) {
                String name = jReader.nextName();
                if (name.equals("main")) {
                    jReader.beginObject();
                    while(jReader.hasNext()) {
                        name = jReader.nextName();
                        if (name.equals("temp")) {
                            response = String.valueOf(Math.round(jReader.nextDouble()));
                            break;
                        } else {
                            jReader.skipValue();
                        }
                    }
                    break;
                }
                jReader.skipValue();
            }

        } catch (Exception e) {
            response = "";
        }


        return response;
    }


    private String getElevation(String elevation) {
        String response="";
        if(elevation.equals("")) {


            try {
                String request = "https://maps.googleapis.com/maps/api/elevation/json?locations=" + locLat + "," + locLong + "&key=AIzaSyB45sOqVQnrH3gi5pzF9SIzpUdQ5CAmRNA";
                JsonReader jReader = new DownloadJSONTask().execute(request).get();
                if (jReader != null) {
                    jReader.beginObject();
                    while (jReader.hasNext()) {
                        String name = jReader.nextName();
                        if (name.equals("results")) {
                            jReader.beginArray();
                            jReader.beginObject();
                            while (jReader.hasNext()) {
                                name = jReader.nextName();
                                if (name.equals("elevation")) {
                                    response = String.valueOf(Math.round(jReader.nextDouble()));
                                } else {
                                    jReader.skipValue();
                                }
                            }
                            jReader.endObject();
                            jReader.endArray();

                        } else {
                            jReader.skipValue();
                        }

                    }

                    jReader.endObject();

                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                response = "";
            }
        }
        else{
            response=elevation;
        }

        return response;
    }
    private class DownloadJSONTask extends AsyncTask<String, Void, JsonReader> {

        @Override
        protected JsonReader doInBackground(String... address) {
            JsonReader result = null;
            for (String addres : address) {

                result = getResponse(addres);
            }
            return result;
        }

        private JsonReader getResponse(String address) {
            JsonReader jReader = null;
            try {
                URL url = new URL(address);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();
                jReader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                urlConnection.disconnect();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return jReader;

        }
    }

}