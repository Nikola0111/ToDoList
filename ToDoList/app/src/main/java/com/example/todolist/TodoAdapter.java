package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TodoAdapter extends BaseAdapter {

    private ArrayList<String> todos;
    private ArrayList<String> priorities;
    private ArrayList<CheckBox> finished;

    LayoutInflater inflater;

    public TodoAdapter(Context c, ArrayList<String> _todos, ArrayList<String> _priorities){
        todos = _todos;
        priorities = _priorities;

        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return todos.size();
    }

    @Override
    public Object getItem(int i) {
        return todos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = inflater.inflate(R.layout.layout_todos, null);
        TextView todoTextView = (TextView) v.findViewById(R.id.todoTextView);
        TextView priorityTextView = (TextView) v.findViewById(R.id.priorityTextView);

        String todo = todos.get(i);
        String priority = priorities.get(i);

        todoTextView.setText(todo);
        priorityTextView.setText(priority);

        return v;
    }
}
