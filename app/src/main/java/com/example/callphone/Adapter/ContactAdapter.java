package com.example.callphone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callphone.ContactDetailActivity;
import com.example.callphone.R;
import com.example.callphone.dataBase.DataBaseOperate;
import com.example.callphone.model.Contact;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private ArrayList<Contact> contacts;  //保存联系人数据
    private int position;   //保存当前开启上下文菜单的位置
    private Fragment fragment;
    private DataBaseOperate operate;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ContactAdapter (Fragment fragment,ArrayList<Contact> contacts,DataBaseOperate operate){
        this.fragment = fragment;
        this.contacts = contacts;
        this.operate = operate;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {   //创建RecyclerView的ViewHolder内部类
        ImageView contactIcon;
        TextView textView;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {    //重写ViewHolder的构造方法，获取其中的view对象
            super(itemView);
            contactIcon = itemView.findViewById(R.id.contactIcon);
            textView = itemView.findViewById(R.id.contactName);
            layout = itemView.findViewById(R.id.contactLayout);

            layout.setOnCreateContextMenuListener(this);  //设置上下文菜单监听器，相当于之前的注册ContextMenu
        }

        //重写监听器接口中的onCreateContextMenu方法，将Menu资源加载出来
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater menuInflater = new MenuInflater(fragment.requireActivity());
            menuInflater.inflate(R.menu.contactmenu,menu);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_cell,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Contact contact = contacts.get(position);
        if (contact.getName() != null){
            holder.textView.setText(contact.getName());
        }else {
            holder.textView.setText(contact.getPhoneNum());
        }
        if (contact.getIconUri() != null && !contact.getIconUri().equals("")){
            Uri uri = Uri.parse(contact.getIconUri());
            holder.contactIcon.setImageURI(uri);
        }

        //当recyclerView中的某项被长按之后获取position属性
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getLayoutPosition());
                return false;
            }
        });

        //为layout设置点击事件
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment.requireActivity(), ContactDetailActivity.class);
                intent.putExtra("contact",contacts.get(position));
                fragment.startActivity(intent);
            }
        });
    }

    //当某一项被移出recyclerView时将该项的OnLongClickListener取消
    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.layout.setOnLongClickListener(null);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
