package com.example.callphone.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.callphone.Adapter.ContactAdapter;
import com.example.callphone.AddContactActivity;
import com.example.callphone.dataBase.DataBaseOperate;
import com.example.callphone.R;
import com.example.callphone.model.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ContactFragment extends Fragment {

    private RecyclerView recyclerView;
    private DataBaseOperate dataBaseOperate;
    private FloatingActionButton addContact;
    ArrayList<Contact> contacts;
    ContactAdapter adapter;

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = requireActivity().findViewById(R.id.contactList);
        dataBaseOperate = new DataBaseOperate(requireActivity(),1);
        addContact = requireActivity().findViewById(R.id.addContactBtn);
        // TODO: Use the ViewModel

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), AddContactActivity.class);
                intent.putExtra("action","add");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        contacts = dataBaseOperate.getContactsFromDatabase();
        adapter = new ContactAdapter(this,contacts,dataBaseOperate);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL)); //添加Android自带的分割线
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {  //当上下文菜单的选项被选中时调用
        int position = adapter.getPosition();
        if (item.getItemId() == R.id.deleteContact){
            dataBaseOperate.deleteContact(contacts.get(position).getId());
            contacts.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(requireActivity(), "删除成功", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }
}