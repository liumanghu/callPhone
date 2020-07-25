package com.example.callphone.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.callphone.dataBase.DataBaseOperate;
import com.example.callphone.model.Record;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactDetailViewModel extends ViewModel {

    //获取某个电话的通信记录
    public ArrayList<HashMap<String,String>> getDatas(DataBaseOperate operate,String phoneNum){
        //为RecordList设置数据
        ArrayList<Record> records = operate.getRecordsByNum(phoneNum);
        ArrayList<HashMap<String,String>> datas = new ArrayList<>();
        for (int i = 0;i < records.size();++i){
            HashMap<String,String> map = new HashMap<>();
            map.put("name",records.get(i).getName());
            map.put("phoneNum",records.get(i).getPhoneNum());
            map.put("dateTime",records.get(i).getDataTime());
            datas.add(map);
        }
        return datas;
    }

}
