package com.example.callphone.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.callphone.model.Contact;
import com.example.callphone.model.Record;

import java.util.ArrayList;

public class DataBaseOperate extends SQLiteOpenHelper {

    String tableCreate_Contacts_sql = "create table Contacts ("
            +"id integer primary key autoincrement,"+
            "name text," +
            "phoneNum text NOT NULL,"+
            "telphone text,"+
            "Email text,"+
            "image text)";

    String tableCreate_CallRecord_sql = "create table CallRecord ("+
            "id integer primary key autoincrement,"+
            "name text,"
            +"phoneNum text NOT NULL,"+
            "contactTime datetime NOT NULL)";

    public DataBaseOperate(@Nullable Context context,  int version) {
        super(context, "call_database" ,null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tableCreate_Contacts_sql);
        db.execSQL(tableCreate_CallRecord_sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertContact (ContentValues contentValues){  //Contact表中插入contact
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("Contacts",null,contentValues);
        db.close();
    }

    public void insertRecord (ContentValues contentValues){  //向CallRecord表中插入record
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("CallRecord",null,contentValues);
        db.close();
    }

    public ArrayList<Contact> getContactsFromDatabase(){  //获取表中的所有的联系人数据
        ArrayList<Contact> contacts = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();  //获取数据库读对象
        Cursor cursor = sqLiteDatabase.query("Contacts",new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){
            Contact contact = new Contact(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("phoneNum")));
            contact.setE_meil(cursor.getString(cursor.getColumnIndex("Email")));
            contact.setTelephoneNum(cursor.getString(cursor.getColumnIndex("telphone")));
            contact.setIconUri(cursor.getString(cursor.getColumnIndex("image")));
            contacts.add(contact);
        }
        cursor.close();
        sqLiteDatabase.close();
        return contacts;
    }

    public ArrayList<Contact> match (String string){  //访问数据库返回模糊匹配结果
        ArrayList<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Contacts where phoneNum like '" + string + "'",null);
        if (cursor.moveToFirst()){
            do {
                Contact contact = new Contact(cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name"))
                        ,cursor.getString(cursor.getColumnIndex("phoneNum")));
                contacts.add(contact);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;
    }

    public String getNameByPhonenum(String phoneNum){   //通过电话号码获取联系人姓名
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Contacts",new String[]{"name"},"phoneNum = ?",new String[]{phoneNum},null,null,null);
        if (cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex("name"));
        }
        cursor.close();
        db.close();
        return name;
    }

    public ArrayList<Record> getAllRecords(){  //获取所有的电话记录
        ArrayList<Record> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("CallRecord",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                Record record = new Record(cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("phoneNum")),
                        cursor.getString(cursor.getColumnIndex("contactTime")));
                record.setName(cursor.getString(cursor.getColumnIndex("name")));
                records.add(record);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return records;
    }

    public void deleteRecord(int id){   //删除指定的电话记录
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CallRecord","id = "+id,null);
        db.close();
    }

    public void deleteContact(int id){  //删除指定的联系人
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Contacts","id = "+id,null);
        db.close();
    }

    public ArrayList<Record> getRecordsByNum (String PhoneNum){  //查询某个电话号码的通话记录
        ArrayList<Record> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("CallRecord",new String[]{"*"},"phoneNum = ?",new String[]{PhoneNum},null,null,null);
        if (cursor.moveToFirst()){
            do {
                Record record = new Record(cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("phoneNum")),
                        cursor.getString(cursor.getColumnIndex("contactTime")));
                record.setName(cursor.getString(cursor.getColumnIndex("name")));
                records.add(record);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return records;
    }

    public void updateContact(ContentValues contentValues,int id){ //更新联系人
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("Contacts",contentValues,"id = ?",new String[]{String.valueOf(id)});
        db.close();
    }
}
