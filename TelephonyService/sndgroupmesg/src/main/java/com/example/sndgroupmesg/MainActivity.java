package com.example.sndgroupmesg;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText editContent;
    private TextView tv;
    List<String> numberList = new ArrayList<>();
    List<String> sendList  = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editContent = findViewById(R.id.contentId);
        tv = findViewById(R.id.tvShow);
        requestPermissions(new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_CONTACTS},0x123);
//        requestPermissions(new String[]{Manifest.permission.SEND_SMS},0x456);
    }
    public void myClick(View view){
        switch (view.getId()){
            case R.id.getPer:
                sendList.clear();
                getContactPerson();
                Log.d(TAG, "myClick: end");
                break;
            case R.id.sendMesg:
                sendMesg();
                break;
        }
    }

    private void getContactPerson(){

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,null,null,null);
        numberList.clear();
        while(cursor.moveToNext()){
            numberList.add(cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER))
                    .replace("-","").replace(" ",""));

        }

        Log.d(TAG, "getContactPerson: ");
        View selectView = LayoutInflater.from(MainActivity.this).inflate(R.layout.list,null);
        ListView listView = selectView.findViewById(R.id.listShow);
        listView.setAdapter(new Myadater());

        new AlertDialog.Builder(MainActivity.this).setView(selectView)
            .setPositiveButton("确定",(dialog,which)->{
            tv.setText(sendList.toString());
            }).show();



    }
    private void sendMesg(){
        SmsManager smsManager = SmsManager.getDefault();
        String content = editContent.getText().toString();
        if(content.equals("")){
            Toast.makeText(MainActivity.this,R.string.Mesg_tip,Toast.LENGTH_LONG).show();
            return;
        }
        for(int i = 0;i<sendList.size();i++){
            Log.d(TAG, "sendMesg: "+sendList.get(i));
            smsManager.sendTextMessage(sendList.get(i),null,content,
                                null,null);
        }
    }

    public class Myadater extends BaseAdapter{

        @Override
        public int getCount() {
            return numberList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            CheckBox cb;
            if(convertView==null){
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.itme,null);

            }else {
                view = convertView;
            }
            cb = view.findViewById(R.id.checkboxId);
            cb.setText(numberList.get(position));


            if(sendList!=null&&sendList.contains(numberList.get(position))){
                cb.setChecked(true);
            }else {
                cb.setChecked(false);
            }

            cb.setOnClickListener(v -> {
                CheckBox checkBox = (CheckBox) v;
                Log.d(TAG, "getView: click poistion "+position +" check is "+checkBox.isChecked());
                if(checkBox.isChecked()){
                    sendList.add(numberList.get(position));
                }else {
                    if(sendList.contains(numberList.get(position)))
                    sendList.remove(numberList.get(position));
                }
            });

            return view;
        }
    }
}
