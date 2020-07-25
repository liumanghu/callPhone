package com.example.callphone.Fragment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.callphone.Adapter.QueryContactAdapter;
import com.example.callphone.MainActivity;
import com.example.callphone.Util.CallUtil;
import com.example.callphone.dataBase.DataBaseOperate;
import com.example.callphone.model.Contact;
import com.example.callphone.viewModel.CallViewModel;
import com.example.callphone.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CallFragment extends Fragment {

    private CallViewModel mViewModel;
    private GridView keyBoardGride;
    private ImageButton keyBoardClose,callButton,textClear;
    private ConstraintLayout keyboardLayout;
    private FloatingActionButton keyboardOpen;
    private TextView callText;
    private RecyclerView queryRecycler;
    QueryContactAdapter adapter;  //queryRecycler的适配器
    private static DataBaseOperate operate;

    public static CallFragment newInstance() {
        return new CallFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.call_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CallViewModel.class);
        keyBoardGride = requireActivity().findViewById(R.id.keybordGride);
        List<Map<String,String>> datas = mViewModel.getDatas();
        SimpleAdapter simpleAdapter = new SimpleAdapter(requireActivity(),
                datas,R.layout.keyboard_cell,new String[]{"num","text"},new int[]{R.id.keyboardNum,R.id.keyboardText});
        keyBoardGride.setAdapter(simpleAdapter);
        keyboardOpen = requireActivity().findViewById(R.id.keyboardOpen);
        keyBoardClose = requireActivity().findViewById(R.id.keyboardClose);
        keyboardLayout = requireActivity().findViewById(R.id.keyboard);
        callText = requireActivity().findViewById(R.id.editPhone);
        callButton = requireActivity().findViewById(R.id.btn_call);
        textClear = requireActivity().findViewById(R.id.btn_clear);
        operate = new DataBaseOperate(requireActivity(),1);
        queryRecycler = requireActivity().findViewById(R.id.queryRecycler);
        queryRecycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new QueryContactAdapter(new ArrayList<Contact>(),new CallUtil(),this,operate);  //先为recycler设置空的数据
        queryRecycler.setAdapter(adapter);

        keyBoardClose.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                keyboardLayout.setVisibility(View.GONE);
                keyboardOpen.setVisibility(View.VISIBLE);
            }
        });
        keyboardOpen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                keyboardLayout.setVisibility(View.VISIBLE);
                keyboardOpen.setVisibility(View.GONE);
            }
        });
        callText.addTextChangedListener(tx);
        keyBoardGride.setOnItemClickListener(l);
        textClear.setOnClickListener(l1);
        callButton.setOnClickListener(l2);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (callText.getVisibility() == View.GONE) {
            callButton.setClickable(false);
            textClear.setClickable(false);
        }
    }

    TextWatcher tx = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (callText.getText().toString().equals("")){
                callText.setVisibility(View.GONE);
                callButton.setClickable(false);
                textClear.setClickable(false);
            }else {
                callText.setVisibility(View.VISIBLE);
                callButton.setClickable(true);
                textClear.setClickable(true);
                String queryString = mViewModel.getQueryString(callText.getText().toString());
                ArrayList<Contact> contacts = operate.match(queryString);
                adapter.datas = contacts;
                adapter.notifyDataSetChanged();   //adapter中的数据更新后修改通知recycleView从新绘制
            }
        }
    };

    AdapterView.OnItemClickListener l = new AdapterView.OnItemClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (callText.getVisibility() == View.GONE){
                callText.setVisibility(View.VISIBLE);
            }
            callText.setText(callText.getText().toString() + mViewModel.keyboard[position]);
        }
    };

    View.OnClickListener l1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = callText.getText().toString();
            callText.setText(str.substring(0,str.length()-1));
        }
    };

    View.OnClickListener l2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phoneNum = callText.getText().toString();
            String name = operate.getNameByPhonenum(phoneNum);
            CallUtil.call(CallFragment.this,name,phoneNum,operate);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                CallUtil.callPhone(CallFragment.this);
            }else {
                Toast.makeText(requireActivity(), "你拒绝了权限的申请", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void insertRecord (ContentValues contentValues){
        operate.insertRecord(contentValues);
    }

}