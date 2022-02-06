package com.example.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.material.textfield.TextInputEditText;

public class TaskDbHelper extends SQLiteOpenHelper {

    public TaskDbHelper(Context context) {
        super(context, "com.example.todolist.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ACCOUNTS (NAME TEXT PRIMARY KEY NOT NULL, PASSWORD TEXT NOT NULL)");
        db.execSQL("CREATE TABLE TASKS (ID INTEGER PRIMARY KEY AUTOINCREMENT,TASK TEXT NOT NULL, NAME TEXT)");
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

    public void addTask(String task, String user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("TASK", task);
        contentValues.put("NAME",user);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("TASKS", null, contentValues);
        db.close();
    }

    public String[] getTasks(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM TASKS WHERE NAME=?",new String[]{user});
        int regs = cursor.getCount();
        if(regs ==0){
            db.close();
            return null;
        }else{
            String[] tareas = new String[regs];
            cursor.moveToFirst();
            for(int i=0;i<regs;i++){
                tareas[i] = cursor.getString(1);
                cursor.moveToNext();
            }
            db.close();
            return tareas;
        }
    }

    public void deleteTask(String task, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("TASKS", "TASK=? AND Name=?",new String[]{task, user});
        db.close();
    }


    public void renameTask(String originalTask, String newTask) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TASK", newTask);
        db.update("TASKS",contentValues,"TASK=?",new String[]{originalTask});
        db.close();
    }
}