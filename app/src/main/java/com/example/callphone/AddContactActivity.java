package com.example.callphone;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.callphone.dataBase.DataBaseOperate;
import com.example.callphone.model.Contact;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddContactActivity extends AppCompatActivity {
    private ImageButton closeBtn,okBtn;
    private EditText editName,editNum,editTelphone,editEmail;
    private DataBaseOperate operate;
    private Contact contact;
    private boolean isAdd;
    private ImageView IconImage;
    private Uri imageUri;
    private File outPutImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        getSupportActionBar().hide();
        operate = new DataBaseOperate(this,1);
        closeBtn = findViewById(R.id.closeBtn);
        okBtn = findViewById(R.id.insertBtn);
        editName = findViewById(R.id.editName);
        editNum = findViewById(R.id.editPhone);
        editTelphone = findViewById(R.id.editPhone2);
        editEmail = findViewById(R.id.editEmailAddress);
        IconImage = findViewById(R.id.IconImage);
        IconImage.setOnClickListener(l1);

        Intent intent = getIntent();
        String action = intent.getStringExtra("action");
        if (action.equals("add"))isAdd = true;
        else if (action.equals("update")){
            isAdd = false;
            contact = intent.getParcelableExtra("updateContact");
            editName.setText(contact.getName());
            editNum.setText(contact.getPhoneNum());
            editTelphone.setText(contact.getTelephoneNum());
            editEmail.setText(contact.getE_meil());
            if (contact.getIconUri() != null && !contact.getIconUri().equals("")){
                Uri uri = Uri.parse(contact.getIconUri());
                IconImage.setImageURI(uri);
            }
        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        okBtn.setOnClickListener(l);
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isAdd){
                if ( editNum.getText().toString().equals("")){
                    Toast.makeText(AddContactActivity.this, "您未输入有效的电话", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name",editName.getText().toString());
                    contentValues.put("phoneNum",editNum.getText().toString());
                    contentValues.put("telphone",editTelphone.getText().toString());
                    contentValues.put("Email",editEmail.getText().toString());
                    if (imageUri != null){
                        contentValues.put("image",imageUri.toString());
                    }
                    operate.insertContact(contentValues);
                    finish();
                }
            }else {
                if ( editNum.getText().toString().equals("")){
                    Toast.makeText(AddContactActivity.this, "您未输入有效的电话", Toast.LENGTH_SHORT).show();
                }else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name",editName.getText().toString());
                    contentValues.put("phoneNum",editNum.getText().toString());
                    contentValues.put("telphone",editTelphone.getText().toString());
                    contentValues.put("Email",editEmail.getText().toString());
                    if (imageUri != null){
                        contentValues.put("image",imageUri.toString());
                    }
                    operate.updateContact(contentValues,contact.getId());
                    contact.setName(editName.getText().toString());
                    contact.setPhoneNum(editNum.getText().toString());
                    contact.setTelephoneNum(editTelphone.getText().toString());
                    contact.setE_meil(editEmail.getText().toString());
                    if (imageUri != null){
                        contact.setIconUri(imageUri.toString());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("updatedContact",contact);
                    setResult(1,intent);
                    finish();
                }
            }
        }
    };

    View.OnClickListener l1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
            builder.setItems(new String[]{"开启相机","从相册中选择"},l2);
            builder.setNegativeButton("取消",null);
            builder.show();
        }
    };

    DialogInterface.OnClickListener l2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == 0){ //开启相机功能
                outPutImage = new File(getExternalCacheDir(),"output_image.jpg");  //创建一个保存图片的文件
                if (outPutImage.exists()){
                    outPutImage.delete();
                }
                try {
                    outPutImage.createNewFile();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        imageUri = FileProvider.getUriForFile(AddContactActivity.this,"com.example.callphone.fileprovider",outPutImage);
                    }else {
                        Uri.fromFile(outPutImage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //打开相机拍照
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,1);
            }else if (which == 1){
                //打开相册选择相片
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //指定只显示图片
                intent.setType("image/*");
                startActivityForResult(intent,2);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            //显示拍照的照片
            if (resultCode == Activity.RESULT_OK){
                IconImage.setImageURI(imageUri);
            }
        }else if (requestCode == 2){
            //显示选择的图片
            if (resultCode == Activity.RESULT_OK){
                imageUri = data.getData();
                //获取所选择的图片并将其显示出来
                IconImage.setImageURI(imageUri);
            }
        }
    }
}