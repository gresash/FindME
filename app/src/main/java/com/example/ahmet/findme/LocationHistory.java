package com.example.ahmet.findme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class LocationHistory extends AppCompatActivity {
    private final DbHelper dbHelper=new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);
        String data=dbHelper.readData();
        if(data.equals(""))
        {
            data="There's no data registered.";
        }
        TextView txtHistory=(TextView) findViewById(R.id.txtHistory);
        txtHistory.setText(data);


    }
}
