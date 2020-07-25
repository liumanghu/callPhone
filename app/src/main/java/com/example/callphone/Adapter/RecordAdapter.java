package com.example.callphone.Adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callphone.Fragment.RecordFragment;
import com.example.callphone.R;
import com.example.callphone.Util.CallUtil;
import com.example.callphone.dataBase.DataBaseOperate;
import com.example.callphone.model.Record;
import com.example.callphone.viewModel.CallViewModel;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private ArrayList<Record> records;
    private RecordFragment fragment;
    private int position;
    private DataBaseOperate operate;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView recordItem,recordNum,dataTime;
        private ImageButton detailButton;
        private LinearLayout linearLayout;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            recordItem = itemView.findViewById(R.id.recordItem);
            recordNum = itemView.findViewById(R.id.recordNum);
            dataTime = itemView.findViewById(R.id.dataTime);
            linearLayout = itemView.findViewById(R.id.recordLayout);
            detailButton = itemView.findViewById(R.id.detailButton);
            linearLayout.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater menuInflater = new MenuInflater(fragment.requireActivity());
            menuInflater.inflate(R.menu.recordmenu,menu);
        }


    }



    public RecordAdapter (ArrayList<Record> records, RecordFragment fragment, DataBaseOperate operate){
        this.records = records;
        this.fragment = fragment;
        this.operate = operate;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_cell,parent,false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordViewHolder holder, int position) {
        Record record = records.get(position);
        if (record.getName().equals("")){
            holder.recordItem.setVisibility(View.GONE);
            holder.recordNum.setText(record.getPhoneNum());
            holder.dataTime.setText(record.getDataTime());
        }else{
            holder.recordItem.setText(record.getName());
            holder.recordNum.setText(record.getPhoneNum());
            holder.dataTime.setText(record.getDataTime());
        }

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getLayoutPosition());
                return false;
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallUtil.call(fragment,holder.recordItem.getText().toString(),holder.recordNum.getText().toString(),operate);
            }
        });

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    @Override
    public void onViewRecycled(@NonNull RecordViewHolder holder) {
        holder.linearLayout.setOnLongClickListener(null);
        holder.linearLayout.setOnClickListener(null);
        super.onViewRecycled(holder);
    }
}
