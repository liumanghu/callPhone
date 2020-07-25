package com.example.callphone.Fragment;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.example.callphone.Adapter.RecordAdapter;
import com.example.callphone.R;
import com.example.callphone.Util.CallUtil;
import com.example.callphone.dataBase.DataBaseOperate;
import com.example.callphone.model.Record;
import com.example.callphone.viewModel.RecordViewModel;

import java.util.ArrayList;

public class RecordFragment extends Fragment {

    private RecordViewModel mViewModel;
    private RecyclerView recordRecycler;
    private DataBaseOperate operate;
    private RecordAdapter recordAdapter;
    ArrayList<Record> records;

    public static RecordFragment newInstance() {
        return new RecordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.record_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        recordRecycler = requireActivity().findViewById(R.id.recordRecycler);
        recordRecycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        operate = new DataBaseOperate(requireActivity(),1);
        records = operate.getAllRecords();
        recordAdapter = new RecordAdapter(records,this,operate);
        recordRecycler.setAdapter(recordAdapter);
        recordRecycler.addItemDecoration(new DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL)); //添加Android自带的分割线
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = recordAdapter.getPosition();
        switch (item.getItemId()){
            case R.id.sendMess:  //发短信
                String data = records.get(position).getPhoneNum().equals("")?records.get(position).getPhoneNum():records.get(position).getPhoneNum();
                CallUtil.sendMessage(this,data);
                break;
            case R.id.deleteRecord:  //删除记录
                operate.deleteRecord(records.get(position).getId());
                records.remove(position);
                recordAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }


}