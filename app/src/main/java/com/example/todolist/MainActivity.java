package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.view.View;
import android.widget.EditText;
import com.example.todolist.db.TaskContract;
import com.example.todolist.db.TaskDbHelper;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private String user;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TodoList); //volver al splash normal
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle = this.getIntent().getExtras();
        if(bundle !=null){
            user = bundle.getString("user");
        }
        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                mHelper.addTask(task, user);
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Task added succesfully",Toast.LENGTH_LONG);
                                toast.show();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        mHelper.deleteTask(task, user);
        updateUI();
    }

    public void editTask(View view){
        View parent = (View) view.getParent();
        TextView taskView = (TextView) parent.findViewById(R.id.task_title);
        String originalTask = taskView.getText().toString();
        EditText textBox = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Rename Task")
                .setMessage("Type the new task")
                .setView(textBox)
                .setPositiveButton("Rename",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newTask = textBox.getText().toString();
                        mHelper.renameTask(originalTask,newTask);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Task modified succesfully",Toast.LENGTH_LONG);
                        toast.show();
                        updateUI();

                    }
                })
                .setNegativeButton("Cancelar",null)
                .create();
        dialog.show();
    }


    private void updateUI() {
        try {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    mHelper.getTasks(user));
            mTaskListView.setAdapter(mAdapter);
        } catch (NullPointerException e) {
            mTaskListView.setAdapter(null);
        }
    }

}