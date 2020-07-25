package com.example.callphone.viewModel;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.example.callphone.Fragment.CallFragment;
import com.example.callphone.dataBase.DataBaseOperate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    public String[] keyboard = new String[]{"1","2","3","4","5","6","7","8","9","*","0","#"};
    public String[] keyboard2 = new String[]{"@","ABC","DEF","GHI","JKL","MNO","PQRS","TUV","WXYZ","(P)","+","(W)"};

    public List<Map<String,String>> getDatas(){
        List<Map<String,String>> datas = new ArrayList<>();
        for (int i = 0;i<keyboard.length;++i){
            HashMap<String,String> map = new HashMap<>();
            map.put("num",keyboard[i]);
            map.put("text",keyboard2[i]);
            datas.add(map);
        }
        return datas;
    }

    public String getQueryString (String str){   //将输入的字符串加工成可以进行模糊匹配的字符
        String str1 = "%";
        for (int i=0;i<str.length();++i){
            str1 += str.charAt(i);
            str1 += "%";
        }
        return str1;
    }





}