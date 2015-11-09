package com.tutecentral.BUNK;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class MyDatabaseAdapter {

    MyHelper helper;
    Context c;
    public MyDatabaseAdapter(Context context)
    {
        helper=new MyHelper(context);
        c=context;
    }

    static class MyHelper extends SQLiteOpenHelper {

        public static final String DATABESE_NAME = "mydatabase";
        public static final String TABLE_NAME = "MyTable";
        public static final int DATABESE_VERSION = 1;
        public static final String UID = "_id";
        public static final String DATE = "Date";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " VARCHAR(255));";
        private Context context;

        public MyHelper(Context context) {
            super(context, DATABESE_NAME, null, DATABESE_VERSION);
            //Message.message(context, "constructor called");
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
              //  Message.message(context, "onCreate called");
            } catch (SQLException e) {
                Message.message(context, "" + e);
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try {
               // Message.message(context, "onUpgrade called");
                db.execSQL(DROP_TABLE);
                onCreate(db);
//                SharedPreferences settings = context.getSharedPreferences("SUBJECTS", Context.MODE_PRIVATE);
//                settings.edit().clear().commit();
            } catch (SQLException e) {
                Message.message(context, "" + e);
            }
        }
    }

    public long insertData(ArrayList<String> cc)
    {
        SharedPreferences prefA = c.getSharedPreferences("SUBJECTS", Context.MODE_PRIVATE);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int size= prefA.getAll().size();
        String[] items = new String[size+1];
        items[0]= MyHelper.DATE;
        int i=1;
        Map<String,?> allEntries = prefA.getAll();
        for (Map.Entry<String,?> entry : allEntries.entrySet()) {

            String single_item =  entry.getValue().toString();
            String[] arr = single_item.split(" ");
            items[i] = arr[0];
            i++;
        }

        for(int p=0;p<cc.size();p++)
            contentValues.put(items[p],cc.get(p));
        long id = db.insert(MyHelper.TABLE_NAME,null,contentValues);
        return id;
    }

    public String getSubjectData(String name)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns  = {MyHelper.UID,MyHelper.DATE,name};
        Cursor cursor = db.query(MyHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        StringBuffer stringBuffer = new StringBuffer();
        while(cursor.moveToNext())
        {
           int index = cursor.getColumnIndex(MyHelper.DATE);
            int index2 = cursor.getColumnIndex(name);
            String datevalue = cursor.getString(index);
            String attvalue = cursor.getString(index2);
            if(!attvalue.equals("0"))
            stringBuffer.append(datevalue + " " + attvalue + " ");
        }
        return stringBuffer.toString();
    }


    public String getAllData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(MyHelper.TABLE_NAME,null, null, null, null, null, null, null);
        if(cursor.getCount()<=0)
        {
            return null;
        }
        else
        {
            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext())
            {
                String[] cols = cursor.getColumnNames();
                for(int i=0;i<cols.length;i++)
                {
                    int index0 = cursor.getColumnIndex(cols[i]);
                    String s = cursor.getString(index0);
                    buffer.append(s+" ");
                }
                buffer.append("\n");
            }
            return buffer.toString();
        }
    }

    public void onUpgrade2(String s)
    {
        String TABLE_NAME = "MyTable";
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.execSQL("ALTER TABLE " + TABLE_NAME
                    + " ADD COLUMN " + s + " VARCHAR(255) DEFAULT '0';" );
        }catch (SQLException e)
        {
            Log.i("mymessage","exception occured");
        }
        Log.i("mymessage","donee");
    }

}
