package com.example.todolist;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.db.TaskDbHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText name,pass;
    TaskDbHelper db;
    VideoView background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.Theme_TodoList);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = (EditText)findViewById(R.id.username);
        pass = (EditText)findViewById(R.id.password);
        db = new TaskDbHelper(this);
    }


    public void login(View view){
        String account = name.getText().toString();
        String password = pass.getText().toString();
        if(account.isEmpty() || password.isEmpty()){
            Toast toast = Toast.makeText(this,"Fill in the fields",Toast.LENGTH_LONG);
            toast.show();

        }else if(db.validateAccount(account, password)){
            Toast toast = Toast.makeText(this,"Wrong credentials",Toast.LENGTH_LONG);
            toast.show();
        }else{
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("user",account);
            startActivity(intent);
            finish();
        }
    }

    public void registerUser(View view){
        String account = name.getText().toString();
        String password = pass.getText().toString();
        if(account.isEmpty() || password.isEmpty()){
            Toast toast = Toast.makeText(this,"Fill in the fields",Toast.LENGTH_LONG);
            toast.show();
        }else if(db.checkAccount(account)){
            Toast toast = Toast.makeText(this,"A user already exists",Toast.LENGTH_LONG);
            toast.show();
        }else{
            db.insertNewAccount(account,password);
            Toast toast = Toast.makeText(this,"Usuario creado",Toast.LENGTH_LONG);
            toast.show();
        }
    }


}
