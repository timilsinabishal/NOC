package np.com.bishaltimilsina.instant.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Bishal on 12/8/2015.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Noc.db";
    public static final String TABLE_NEWS="news";
    public static final String TABLE_NOTICE="notice";
    public static final String TABLE_TENDER="tender";
    public static final String COLUMN_NAME="title";
    public static final String COLUMN_URL="url";

    public DatabaseOpenHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+TABLE_NEWS+"(title text,url text)");
        db.execSQL("CREATE TABLE " + TABLE_NOTICE + "(title text,url text)");
        db.execSQL("CREATE TABLE "+TABLE_TENDER+"(title text,url text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NEWS);
        db.execSQL("drop table if exists " +TABLE_NOTICE);
        db.execSQL("drop table if exists "+TABLE_TENDER);
        onCreate(db);
    }

    public String[] getAllTitle(String TABLE) {

        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            array_list.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            cursor.moveToNext();
        }
        String[] tables = new String[array_list.size()];
        tables = array_list.toArray(tables);
        db.close();
        return tables;
       }

    public void saveTable(String[] titles,String[] url,String TABLE){
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL("drop table if exists "+TABLE);
        db.execSQL("CREATE TABLE " + TABLE + "(title text,url text)");
        ContentValues values=new ContentValues();
        for(int i=0;i<titles.length;i++){
            values.put(COLUMN_NAME,titles[i]);
            values.put(COLUMN_URL,url[i]);
            db.insert(TABLE,null,values);
        }
        db.close();
    }

    public String getUrl(String TABLE,int position){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery("Select url from "+TABLE,null);
        cursor.moveToPosition(position);
        String url=cursor.getString(cursor.getColumnIndex(COLUMN_URL));
        return url;
    }
}
