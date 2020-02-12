package com.example.smsmanagerdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SmsManager smsManager;
    private EditText editNumber;
    private EditText editContent;
    private Button btSnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(new String[]{Manifest.permission.SEND_SMS},0x123);
        editContent = findViewById(R.id.contentId);
        editNumber = findViewById(R.id.numberId);
        btSnd = findViewById(R.id.sndMsgId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0x123&&grantResults!=null&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            smsManager = SmsManager.getDefault();
            btSnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNum = editNumber.getText().toString();
                    String contentStr = editContent.getText().toString();
                    PendingIntent pendingIntent = PendingIntent.getActivity(
                            MainActivity.this,0,new Intent(),0);

                    smsManager.sendTextMessage(phoneNum,null,contentStr,pendingIntent,null);
                    Toast.makeText(MainActivity.this,
                            R.string.send_tip,Toast.LENGTH_LONG).show();
                }
            });

        }else{
            Toast.makeText(MainActivity.this,
                    R.string.permission_tip, Toast.LENGTH_LONG).show();
        }
    }
}
