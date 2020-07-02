package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddTodoDialog.AddTodoDialogListener {

    private SwipeMenuListView todoListView;
    private FloatingActionButton addTodo;

    private ArrayList<String> todos;
    private ArrayList<String> priorities;

    private String editTodo;
    private String editPriority;
    private int forChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FAB dugme za otvaranje novog prozora, u kom se unosi naziv aktivnosti, i koliko je hitno
        //custom layout za ovo iznad
        //cuvanje u fajl
        //filtriranje po prioritetu

        try{
            File file = new File(this.getFilesDir(), "todos.dat");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            todos = (ArrayList<String>) ois.readObject();
            ois.close();
        }catch(Exception e){
            todos = new ArrayList<String>();
        }

        try{
            File file = new File(this.getFilesDir(), "priorities.dat");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            priorities = (ArrayList<String>) ois.readObject();
            ois.close();
        }catch(Exception e) {
            priorities = new ArrayList<String>();
        }

        todoListView = findViewById(R.id.todosListView);
        addTodo = (FloatingActionButton) findViewById(R.id.addTodoButton);

        final TodoAdapter todoAdapter = new TodoAdapter(this, todos, priorities);
        todoListView.setAdapter(todoAdapter);

        MainActivity thiz = this;

        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTodo = "";
                editPriority = "";
                openDialog();
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0xff,
                        0x00)));
                // set item width
                editItem.setWidth(170);
                // set item title
                editItem.setIcon(R.drawable.ic_edit);
                // set item title fontsize
                editItem.setTitleSize(18);
                // set item title font color
                editItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        todoListView.setMenuCreator(creator);

        todoListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        editTodo = todos.get(position);
                        editPriority = priorities.get(position);
                        forChange = position;
                        openDialog();
                        break;
                    case 1:
                        todos.remove(position);
                        priorities.remove(position);
                        todoListView.invalidateViews();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    public void openDialog(){
        AddTodoDialog addTodoDialog = new AddTodoDialog(editTodo,editPriority, forChange);
        addTodoDialog.show(getSupportFragmentManager(), "Add Todo Dialog");
    }

    @Override
    public void applyText(String todoDialog, String priorityDialog, int forChange) {
        if(forChange == -1) {
            todos.add(todoDialog);
            priorities.add(priorityDialog);
        }else{
            todos.set(forChange, todoDialog);
            priorities.set(forChange, priorityDialog);
        }

        TodoAdapter newTodoAdapter = new TodoAdapter(this,todos,priorities);
        todoListView.setAdapter(newTodoAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            File file = new File(this.getFilesDir(), "todos.dat");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(todos);
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            File file = new File(this.getFilesDir(), "priorities.dat");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(priorities);
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }
}