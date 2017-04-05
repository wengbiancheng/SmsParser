package com.example.qq.smsparser.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qq.smsparser.entity.HelperMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库帮工表的操作类，主要进行数据的创建和销毁，数据库的增删查改等操作
 */
public class DbutilHelper{
    private static DbutilHelper dbutils=null;
    private String TABLE_HELPER="helperDB";
    private final String[] HELPER_COLS=new String[]{"_id","name","phone","choose"};

    public static DbutilHelper getInstance(){
        if(dbutils==null){
            dbutils=new DbutilHelper();
            return dbutils;
        }
        return dbutils;
    }

    //TODO saveHelper测试成功
    public long saveHelper(HelperMessage helperMessage,SQLiteDatabase write_sqlite){
        Log.e("SQLite","Helper数据库插入:saveHelper()");
        ContentValues values = new ContentValues();
//        values.put("_id", helperMessage.getId());
        values.put("name", helperMessage.getName());
        values.put("phone", helperMessage.getPhone());
        values.put("choose", helperMessage.isCheck()?1:0);
        return write_sqlite.insert(TABLE_HELPER, null, values);
    }

    public int deleteHelper(int id,SQLiteDatabase write_sqlite){
        Log.e("SQLite","Helper数据库删除:deleteHelper()");
        return write_sqlite.delete(TABLE_HELPER,"_id="+id,null);
    }

    public List<HelperMessage> getHelperListData(SQLiteDatabase read_sqlite){
        Cursor cursor = read_sqlite.query(TABLE_HELPER, HELPER_COLS, null, null, null, null, null);
        List<HelperMessage> list=new ArrayList<>();
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            HelperMessage helperMessage=new HelperMessage();
            helperMessage.setId(cursor.getInt(0));
            helperMessage.setName(cursor.getString(1));
            helperMessage.setPhone(cursor.getString(2));
            helperMessage.setCheck(cursor.getInt(3)==1?true:false);
            list.add(helperMessage);
            cursor.moveToNext();
        }
        Log.e("SQLite","Helper数据库检索:getHelperListData()---list集合的大小是:"+list.size());
        return list;
    }
    /**
     * 根据数据库来获取所有的帮工的电话号码
     * @return
     */
    public List<String> getHelperPhone(SQLiteDatabase read_sqlite){
        Log.e("SQLite","Helper数据库检索:getHelperPhone()");
        List<String> list=new ArrayList<>();

        Cursor cursor = read_sqlite.query(TABLE_HELPER, HELPER_COLS, null, null, null, null, null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        return list;
    }
}
