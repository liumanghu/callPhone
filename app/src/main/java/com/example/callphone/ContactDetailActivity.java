package com.example.callphone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.callphone.Util.CallUtil;
import com.example.callphone.dataBase.DataBaseOperate;
import com.example.callphone.model.Contact;
import com.example.callphone.viewModel.ContactDetailViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactDetailActivity extends AppCompatActivity {
    private TabHost tabHost;
    private TextView contactName,contactEmail,contactNum;
    private ListView recordListView;
    private DataBaseOperate operate;
    private ImageView ContactCall,ContactSendMess;
    private ContactDetailViewModel contactDetailViewModel;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        //获取ViewModel
        contactDetailViewModel = new ViewModelProvider(ContactDetailActivity.this).get(ContactDetailViewModel.class);
        //如果当前的activity具有父级activity，则设置返回键
        if (NavUtils.getParentActivityName(ContactDetailActivity.this) != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        contactName = findViewById(R.id.ContactDetailName);
        contactEmail = findViewById(R.id.ContactDetailEmail);
        contactNum = findViewById(R.id.ContactDetailNum);
        recordListView = findViewById(R.id.recordListView);
        operate = new DataBaseOperate(ContactDetailActivity.this,1);
        ContactCall = findViewById(R.id.ContactDetailCall);
        ContactSendMess = findViewById(R.id.ContactDetailMess);
        ContactCall.setOnClickListener(l);
        ContactSendMess.setOnClickListener(l);
        //获取Intent，从Intent中读取当前的联系人数据
        Intent intent = getIntent();
        contact = intent.getParcelableExtra("contact");
        init(contact);
        //初始化TabHost
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("详情").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("通话记录").setContent(R.id.tab2));

        ArrayList<HashMap<String,String>>datas = contactDetailViewModel.getDatas(operate,contact.getPhoneNum());
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,datas,R.layout.record_list_cell,new String[]{"name","phoneNum","dateTime"},
                new int[]{R.id.DetailRecordName,R.id.DetailRecordNum,R.id.DetailRecordDate});
        recordListView.setAdapter(simpleAdapter);
    }
    //加载溢出菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.contactdetailmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //设置打电话和发短信两个按钮的点击事件
    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ContactDetailCall:
                    CallUtil.call(ContactDetailActivity.this,contact.getName(),contact.getPhoneNum(),operate);
                    break;
                case R.id.ContactDetailMess:
                    String data = contact.getName().equals("")?contact.getName():contact.getPhoneNum();
                    CallUtil.sendMessage(ContactDetailActivity.this,data);
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2){
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                CallUtil.callPhone(this);
            }else {
                Toast.makeText(this, "请同意权限之后重试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.updateContact){
            Intent intent = new Intent(ContactDetailActivity.this,AddContactActivity.class);
            intent.putExtra("action","update");
            intent.putExtra("updateContact",contact);
            startActivityForResult(intent,1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && resultCode == 1){
            contact = data.getParcelableExtra("updatedContact");
            init(contact);
        }
    }

    public void init(Contact contact){
        contactName.setText(contact.getName());
        contactEmail.setText(contact.getE_meil());
        contactNum.setText(contact.getPhoneNum());
        getSupportActionBar().setTitle(contact.getName());
    }
}