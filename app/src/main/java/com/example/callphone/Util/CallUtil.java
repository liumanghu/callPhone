package com.example.callphone.Util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.callphone.dataBase.DataBaseOperate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CallUtil {
    private static String phoneNum = "";  //电话目标号码
    private static String name = "";   //联系人姓名
    private static DataBaseOperate operate;

    public static void call(Fragment fragment, String contactName, String data, DataBaseOperate Operate){  //打电话
        phoneNum = data;
        name = contactName;
        operate = Operate;
        if (ContextCompat.checkSelfPermission(fragment.requireActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            callPhone(fragment);
        }else {
            fragment.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},1);
        }
    }

    public static void call(Activity activity,String contactName, String data, DataBaseOperate Operate){  //打电话
        phoneNum = data;
        name = contactName;
        operate = Operate;
        if (ContextCompat.checkSelfPermission(activity,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            callPhone(activity);
        }else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},2);
        }
    }

    public static void callPhone (Fragment fragment){  //开启打电话的服务
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phoneNum));
        fragment.startActivity(intent);
        //添加通话记录
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("phoneNum",phoneNum);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        contentValues.put("contactTime",formatter.format(date));
        operate.insertRecord(contentValues);
    }

    public static void callPhone (Activity activity){  //开启打电话服务
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phoneNum));
        activity.startActivity(intent);
        //添加通话记录
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("phoneNum",phoneNum);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        contentValues.put("contactTime",formatter.format(date));
        operate.insertRecord(contentValues);
    }

    public static void sendMessage (Fragment fragment, String acceptor){  //发送短信
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"+ acceptor));
        fragment.startActivity(intent);
    }

    public static void sendMessage (Activity activity, String acceptor){  //发送短信
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"+ acceptor));
        activity.startActivity(intent);
    }
}
