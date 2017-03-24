package com.example.qq.smsparser.controller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.qq.smsparser.entity.HelperMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库的操作类，主要进行数据的创建和销毁，数据库的增删查改等操作
 */
public class DbutilHelper {

    private static DbutilHelper dbutils=null;
    private Context context;
    private SQLiteDatabase read_sqlite;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase write_sqlite;
    private int version=5;

    private Dbutil dbutil=null;

    private final String[] HELPER_COLS=new String[]{"_id","name","phone","choose"};
    private String DB="SmsParseDB";
    private String TABLE_HELPER="helperDB";
    public String create_table_helper="CREATE TABLE "+TABLE_HELPER+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,phone TEXT,choose INTEGER);";

    public static DbutilHelper getInstance(Context context){
        if(dbutils==null){
            dbutils=new DbutilHelper(context);
            return dbutils;
        }
        return dbutils;
    }

    private DbutilHelper(Context context){
        this.context=context;
        init();
    }

    private void init(){
        if(helper==null){
            helper=new MySQLiteHelper(context,DB,null,version);
            write_sqlite=helper.getWritableDatabase();
            read_sqlite=helper.getReadableDatabase();
        }else{
            write_sqlite=helper.getWritableDatabase();
            read_sqlite=helper.getReadableDatabase();
        }
        if(dbutil==null){
            dbutil=Dbutil.getInstance(context);
        }
    }

    //TODO  存储帮工的数据、删除帮工的数据
    public long saveHelper(HelperMessage helperMessage){
        Log.e("SQLite","Helper数据库插入:saveHelper()");
        ContentValues values = new ContentValues();
        values.put("_id", helperMessage.getId());
        values.put("name", helperMessage.getName());
        values.put("phone", helperMessage.getPhone());
        values.put("choose", helperMessage.isCheck()?1:0);
        return write_sqlite.insert(TABLE_HELPER, null, values);
    }

    public int deleteHelper(int id){
        Log.e("SQLite","Helper数据库删除:deleteHelper()");
        return write_sqlite.delete(TABLE_HELPER,"id="+id,null);
    }

    public List<HelperMessage> getHelperListData(){
        Log.e("SQLite","Helper数据库检索:getHelperListData()");
        Cursor cursor = read_sqlite.query(TABLE_HELPER, HELPER_COLS, null, null, null, null, null);
        List<HelperMessage> list=new ArrayList<>();
        if(cursor!=null && cursor.moveToFirst()){
            while(cursor.moveToNext()){
                HelperMessage helperMessage=new HelperMessage();
                helperMessage.setId(cursor.getInt(0));
                helperMessage.setName(cursor.getString(1));
                helperMessage.setPhone(cursor.getString(2));
                helperMessage.setCheck(cursor.getInt(3)==1?true:false);
                list.add(helperMessage);
                cursor.moveToNext();
            }
        }
        Log.e("SQLite","Helper数据库检索:getHelperListData()---list集合的大小是:"+list.size());
        return list;
    }
    /**
     * 根据数据库来获取所有的帮工的电话号码
     * @return
     */
    public List<String> getHelperPhone(){
        Log.e("SQLite","Helper数据库检索:getHelperPhone()");
        List<String> list=new ArrayList<>();

        Cursor cursor = read_sqlite.query(TABLE_HELPER, HELPER_COLS, null, null, null, null, null);
        cursor.moveToFirst();
        int order_id;
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            list.add(cursor.getString(3));
            cursor.moveToNext();
        }
        return list;
    }

    class MySQLiteHelper extends SQLiteOpenHelper {

        public MySQLiteHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(create_table_helper);
            Log.e("SQLite","Helper数据库建立:OnCreate()");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.e("SQLite","Helper数据库更新:onUpgrade()");
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_HELPER);
            this.onCreate(db);
        }
    }
}
