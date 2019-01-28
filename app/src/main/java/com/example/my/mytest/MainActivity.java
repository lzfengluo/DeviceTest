package com.example.my.mytest;

import android.content.Intent;
import android.serialport.DeviceControlSpd;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_PATH = "/sys/class/misc/mtgpio/pin";
    private Button btn1;
    private Button btn2;
    private Button btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    DeviceControlSpd deviceControlSpd = new DeviceControlSpd(MAIN_PATH);
                    //红绿蓝：80 78 79
                    deviceControlSpd.MainPowerOn(79);
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    DeviceControlSpd deviceControlSpd = new DeviceControlSpd(MAIN_PATH);
                    deviceControlSpd.MainPowerOff(79);
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SerialportTestActivity.class);
                startActivity(intent);
            }
        });
    }
}
