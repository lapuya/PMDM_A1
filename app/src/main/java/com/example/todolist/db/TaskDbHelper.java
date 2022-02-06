package com.example.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.material.textfield.TextInputEditText;

public class TaskDbHelper extends SQLiteOpenHelper {

    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";


        db.execSQL(createTable);
        db.execSQL("CREATE TABLE ACCOUNTS (NAME TEXT PRIMARY KEY NOT NULL, PASSWORD TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }

    public boolean validateAccount(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM ACCOUNTS WHERE NAME=? AND PASSWORD=?",new String[]{String.valueOf(username),String.valueOf(password)});
        int regs = cursor.getCount();
        return regs <= 0;
    }

    public boolean checkAccount(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from ACCOUNTS where NAME = ?", new String[]{username});
        int num = cursor.getCount();
        return num > 0;
    }

    public void insertNewAccount(String account, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("NAME", account);
        contentValues.put("PASSWORD", password);
        db.insert("ACCOUNTS", null, contentValues);
        db.close();
    }
}