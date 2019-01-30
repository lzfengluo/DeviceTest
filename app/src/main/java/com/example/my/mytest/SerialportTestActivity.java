package com.example.my.mytest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.serialport.SerialPortSpd;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SerialportTestActivity extends Activity {

    private TextView tv1;
    private EditText et1;
    private Button bt1;
    private Button bt2;
    private Button bt3;

    private ReadThread mReadThread;
    private Handler handler;
    private byte[] temp1;
//    private String temp1;
    public static final String SERIAL_TTYMT1 = "/dev/ttyMT1";

    private int fd;
    private SerialPortSpd mSerialPortSpd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialport_test);

        tv1 = (TextView) findViewById(R.id.tv1);
        et1 = (EditText) findViewById(R.id.et1);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);

        mSerialPortSpd = new SerialPortSpd();
        fd = mSerialPortSpd.getFd();

//        发送数据
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et1.getText().toString();
                int len = send(msg);
                Toast.makeText(SerialportTestActivity.this,"发送信息"+len,Toast.LENGTH_SHORT).show();
            }
        });
//        打开串口
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mSerialPortSpd.OpenSerial(SERIAL_TTYMT1,9600);
                    Toast.makeText(SerialportTestActivity.this,"打开串口",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
//        关闭串口
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSerialPortSpd.CloseSerial(fd);
                finish();
            }
        });
        mReadThread = new ReadThread();
        mReadThread.start();

//        获取串口信息
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Toast.makeText(SerialportTestActivity.this,msg.what+" shou",Toast.LENGTH_SHORT).show();
                    String temp2 = String.valueOf(msg.obj);
                    tv1.setText(temp2+"\n");
            }
        };

    }

    public int send (String str){
        byte[] s =str.getBytes();
        int len = mSerialPortSpd.WriteSerialByte(fd,s);
        return len;
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class ReadThread extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                fd = mSerialPortSpd.getFd();
                temp1 = mSerialPortSpd.ReadSerial(fd,1024);

                if (temp1 != null) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = temp1;
                    handler.sendMessage(msg);

                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}


