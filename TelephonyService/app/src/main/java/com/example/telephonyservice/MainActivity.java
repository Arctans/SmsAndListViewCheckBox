package com.example.telephonyservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TelephonyManager mTelephonyManager;
    private ListView listView;
    private List<String> statusValue = new ArrayList<>();
    private  List<Map<String,String>> listMap= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.ACCESS_COARSE_LOCATION}
                , 0x123);
        listView = findViewById(R.id.listShow);
    }


    @Override
    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x123 && grantResults != null
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            String[] statusName = getResources().getStringArray(R.array.statusNames);
            String[] phoneType = getResources().getStringArray(R.array.phoneType);
            String[] simState = getResources().getStringArray(R.array.SimState);
            statusValue.add(mTelephonyManager.getImei());
            statusValue.add(mTelephonyManager.getDeviceSoftwareVersion());
            statusValue.add(mTelephonyManager.getNetworkOperator());
            statusValue.add(mTelephonyManager.getNetworkOperatorName());
            statusValue.add(phoneType[mTelephonyManager.getPhoneType()]);
            statusValue.add(mTelephonyManager.getAllCellInfo()!=null
                            ?mTelephonyManager.getAllCellInfo().toString():"未知信息");
            statusValue.add(mTelephonyManager.getSimCountryIso());
            statusValue.add(mTelephonyManager.getSimSerialNumber());
            statusValue.add(simState[mTelephonyManager.getSimState()]);

            for(int i=0;i<statusValue.size();i++){
                Map<String,String> map = new HashMap<>();
                map.put("name",statusName[i]);
                map.put("value",statusValue.get(i));
                listMap.add(map);
            }
            showListView();
        }else{
            Toast.makeText(this,R.string.permission_tip,Toast.LENGTH_LONG).show();
        }
    }

    private void showListView(){

        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,
                                        listMap,R.layout.line,new String[]{"name","value"}
                                        ,new int[]{R.id.name,R.id.value});
        listView.setAdapter(adapter);
    }
}
