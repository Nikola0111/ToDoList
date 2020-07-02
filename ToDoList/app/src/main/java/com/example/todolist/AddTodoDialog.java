package com.example.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

public class AddTodoDialog extends AppCompatDialogFragment {

    private EditText todoEditText;
    private RadioButton low_radiobutton;
    private RadioButton medium_radiobutton;
    private RadioButton urgent_radiobutton;

    private AddTodoDialogListener listener;

    private String priority;

    private String existingTodo;
    private String existingPrioirity;
    private int forChange;

    public AddTodoDialog(String _todo, String _priority, int _forChange){
        existingTodo = _todo;
        existingPrioirity = _priority;
        forChange = _forChange;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_todo,null);

        todoEditText = (EditText) view.findViewById(R.id.todoPlainText);
        low_radiobutton = (RadioButton) view.findViewById(R.id.low_radiobutton);
        medium_radiobutton = (RadioButton) view.findViewById(R.id.medium_radiobutton);
        urgent_radiobutton = (RadioButton) view.findViewById(R.id.urgent_radiobutton);

        if(!existingTodo.equals("")) {
            todoEditText.setText(existingTodo);

            if(existingPrioirity.equals("Nizak")){
                low_radiobutton.toggle();
            }else if(existingPrioirity.equals("Srednji")){
                medium_radiobutton.toggle();
            }else{
                urgent_radiobutton.toggle();
            }
        }else{
            forChange = -1;
        }

        builder.setView(view)
                .setTitle("Add todo")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!low_radiobutton.isChecked() && !medium_radiobutton.isChecked() &&
                            !urgent_radiobutton.isChecked())
                             return;

                        String todo = todoEditText.getText().toString();

                        if(low_radiobutton.isChecked())
                            priority = "Nizak";
                        else if(medium_radiobutton.isChecked())
                            priority = "Srednji";
                        else if(urgent_radiobutton.isChecked())
                            priority = "Hitan";

                        listener.applyText(todo,priority,forChange);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddTodoDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement " +
                    "AddTodoDialogListener");
        }
    }
    public interface  AddTodoDialogListener{
        void applyText(String todoDialog, String priorityDialog,int forChange);
    }
}
