package com.example.ahmet.findme;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{
    private Button btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnHistory=(Button) findViewById(R.id.btnHistory);
        if(isHistoryAvailable()){
            btnHistory.setVisibility(View.VISIBLE);
        }


    }
    public void onHistoryButtonClick(View v){

        Intent i=new Intent(getBaseContext(), LocationHistory.class);
        startActivity(i);
    }

    public void onLocationButtonClick(View v){

        Intent i=new Intent(getBaseContext(), MapActivity.class);
        startActivity(i);


    }
    private boolean isHistoryAvailable(){
        DbHelper dbHelper=new DbHelper(this);
        return !dbHelper.readData().equals("");
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(isHistoryAvailable()){
            btnHistory.setVisibility(View.VISIBLE);
        }
    }



}
