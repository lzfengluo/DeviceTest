package com.example.my.mytest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

public class NfcActivity extends Activity {

    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_nfc);
        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        String infoText = "看下这个支持nfc吗 或者 开了没";
        if (nfcAdapter == null) {
            Toast.makeText(getApplicationContext(), infoText, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //获取 Tag 读取 ID 得到字节数组  转字符串 转码  得到卡号（默认16进制 这请自便）
        Long cardNo = Long.parseLong(flipHexStr(ByteArrayToHexString(tag.getId())), 16);
        String num="";
//        if (cardNo.toString().getBytes().length == 10) {
        num = cardNo.toString();
//        } else {
//        转的时候有个地方需要注意下 如果开头为零的话 会省略所以位数会改变
//            num = "0" + cardNo.toString();
//        }

        TextView textView = (TextView) findViewById(R.id.nfc_text);
        textView.setText(num);
        Toast.makeText(getApplicationContext(), num, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    public static String flipHexStr(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= s.length() - 2; i = i + 2) {
            result.append(new StringBuilder(s.substring(i, i + 2)).reverse());
        }
        return result.reverse().toString();
    }

    // 16转10进制
    public static String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F"};
        StringBuilder out = new StringBuilder();


        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out.append(hex[i]);
            i = in & 0x0f;
            out.append(hex[i]);
        }
        return out.toString();
    }

}
