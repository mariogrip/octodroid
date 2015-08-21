package com.mariogrip.octodroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by neil on 7/19/15.
 */
public class temp_history_db {

    temp_db_openHelper helper;
    temp_history_db(Context context){
        helper = new temp_db_openHelper(context);
    }

    public void addTemp(int time, int bedC, int bedS, int extC, int extS){

        SQLiteDatabase db =  helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(helper.TIME, time);
        values.put(helper.BED_CURRENT, bedC);
        values.put(helper.BED_SET, bedS);
        values.put(helper.EXT_CURRENT, extC);
        values.put(helper.EXT_SET, extS);

        db.insert(helper.TABLE_NAME, null, values);
    }
    public int getTemps(int time){
        return 0;
    }
    public void clearTemps(){

    }



    public class temp_db_openHelper extends SQLiteOpenHelper{

        private static final String TABLE_NAME = "temp_history";
        private static final String DATABASE_NAME = "temp_history_db";
        private static final int VERSION  = 0;
        private static final String _ID = "_ID";


       //Columns
        private static final String TIME = "time";
        private static final String BED_CURRENT = "bed_current";
        private static final String BED_SET = "bed_set";
        private static final String EXT_CURRENT = "ext_current";
        private static final String EXT_SET = "ext_set";

        //SQLite commands
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TIME + " INTEGER, " +  BED_CURRENT + " INTEGER, " + BED_SET + " INTEGER, "
                +  EXT_CURRENT + " INTEGER, " + EXT_SET + " INTEGER " + ")";

                ;

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


        public temp_db_openHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE);
            }catch (SQLiteException e){
               e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try{
                sqLiteDatabase.execSQL(DROP_TABLE);
            }catch (SQLiteException e){
                e.printStackTrace();
            }
        }

    }
}
