package com.example.callphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.callphone.Fragment.CallFragment;
import com.example.callphone.Fragment.ContactFragment;
import com.example.callphone.Fragment.RecordFragment;
import com.example.callphone.dataBase.DataBaseOperate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView btm;
    private FragmentManager fragm;
    private FragmentTransaction transaction;
    private DataBaseOperate dataBaseOperate;  //数据库操作类
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btm = findViewById(R.id.bottomNavigationView);
        dataBaseOperate = new DataBaseOperate(this,1);  //创建数据库操作类
        init();
        btm.setOnNavigationItemSelectedListener(l);

        SharedPreferences sharedPreferences = this.getSharedPreferences("share",MODE_PRIVATE);
        boolean isFirstStart = sharedPreferences.getBoolean("isFirst",true);
        editor = sharedPreferences.edit();
        if (isFirstStart){
            if (isReadableContent()){
                readAndInsertData();
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //向用户申请权限
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
                }
            }
        }

    }

    BottomNavigationView.OnNavigationItemSelectedListener l = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            fragm = getSupportFragmentManager();
            transaction =  fragm.beginTransaction();
            switch (menuItem.getItemId()){
                case R.id.call:
                    btm.getMenu().getItem(0).setChecked(true);
                    fragment = new CallFragment();
                    getSupportActionBar().setTitle("电话");
                    break;
                case R.id.contact:
                    btm.getMenu().getItem(1).setChecked(true);
                    fragment = new ContactFragment();
                    getSupportActionBar().setTitle("联系人");
                    break;
                case R.id.records:
                    btm.getMenu().getItem(2).setChecked(true);
                    fragment = new RecordFragment();
                    getSupportActionBar().setTitle("通话记录");
                    break;
            }

            transaction.replace(R.id.frameLayout,fragment);
            transaction.commit();
            return false;
        }
    };

    public void init(){  //初始化页面
        btm.getMenu().getItem(1).setChecked(true);
        fragm = getSupportFragmentManager();
        transaction = fragm.beginTransaction();
        transaction.add(R.id.frameLayout,new ContactFragment());
        transaction.commit();
        getSupportActionBar().setTitle("联系人");
    }

    public boolean isReadableContent(){  //检查当前APP是否获得读取content的权限
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED;
    }

    //用户对读取联系人权限的获取操作的回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                editor.putBoolean("isFirst",false);
                editor.commit();
                readAndInsertData();
            }else {
                Toast.makeText(this, "你拒绝了权限申请，应用无法正常显示!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void readAndInsertData(){
        //创建contentResolver读取联系人信息
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);

        while (cursor.moveToNext()){
            ContentValues contentValues = new ContentValues();
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contentValues.put("name",name);
            contentValues.put("phoneNum",phoneNum);
            dataBaseOperate.insertContact(contentValues);
        }
    }
}