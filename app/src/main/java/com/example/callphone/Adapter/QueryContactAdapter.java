package com.example.callphone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callphone.R;
import com.example.callphone.Util.CallUtil;
import com.example.callphone.dataBase.DataBaseOperate;
import com.example.callphone.model.Contact;

import java.util.ArrayList;

public class QueryContactAdapter extends RecyclerView.Adapter<QueryContactAdapter.MViewHolder> {
    public ArrayList<Contact> datas;
    private CallUtil callUtil;
    private Fragment fragment;
    private DataBaseOperate operate;

    class MViewHolder extends RecyclerView.ViewHolder{
        TextView queryContactName,queryContactNum;
        LinearLayout queryrecycler_cell;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            queryContactName = itemView.findViewById(R.id.queryContactName);
            queryContactNum = itemView.findViewById(R.id.queryContactNum);
            queryrecycler_cell = itemView.findViewById(R.id.queryrecycler_cell);
        }
    }

    public QueryContactAdapter(ArrayList<Contact> datas, CallUtil callUtil, Fragment fragment, DataBaseOperate operate){
        this.datas = datas;
        this.callUtil = callUtil;
        this.fragment = fragment;
        this.operate = operate;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queryrecycler_cell,parent,false);
        final MViewHolder mViewHolder = new MViewHolder(view);
        mViewHolder.queryrecycler_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mViewHolder.queryContactNum.getText().toString();
                String name = mViewHolder.queryContactName.getText().toString();
                CallUtil.call(fragment,name,phoneNumber,operate);
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        holder.queryContactName.setText(datas.get(position).getName());
        holder.queryContactNum.setText(datas.get(position).getPhoneNum());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onViewRecycled(@NonNull MViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
