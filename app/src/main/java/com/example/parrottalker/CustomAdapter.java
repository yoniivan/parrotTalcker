package com.example.parrottalker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parrottalker.utils.ActionButton;
import com.example.parrottalker.utils.MusicPlayer;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;

class CustomAdapter extends ArrayAdapter<RecordObject> {

    private TextView textViewWords, textViewDate;
    private EditText etNum;
    private ActionButton play, stop;
    private RecordObject item;
    private ArrayList<RecordObject> list;
    int count;
    private EditText[] arrEditText;

    public CustomAdapter(Context context, ArrayList<RecordObject> list) {
        super(context, R.layout.main_list, list);
        this.list = list;
        arrEditText = new EditText[list.size()];
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.main_list, null);

        item = getItem(position);
        textViewDate = convertView.findViewById(R.id.list_date);
        textViewWords = convertView.findViewById(R.id.list_word);
        etNum = convertView.findViewById(R.id.et_list_times);
        etNum.setRawInputType(Configuration.KEYBOARD_12KEY);
        arrEditText[count] = etNum;
        stop = convertView.findViewById(R.id.btn_list_stop);
        play = convertView.findViewById(R.id.btn_list_play);
        count++;
        play.setOnClickListener(view -> {

            String fullPath = MainActivity.FILE_PATH + list.get(position).getWord() + ".wav";
            String et_num = arrEditText[position].getText().toString();
            int num = ((et_num.isEmpty()) ? 1 : Integer.parseInt(et_num));
            MusicPlayer.get().play(fullPath, num);

        });
        textViewDate.setText(item.dateFormat("yyyy/MM/dd"));
        textViewWords.setText(item.getWord());
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirm");
                String wordDelete = textViewWords.getText().toString();
                builder.setMessage("Are you sure you want to delete '" + wordDelete + "' ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String deleted = deleteWord();
                        if(deleted != null){
                            Toast.makeText(getContext(), "'" + deleted + "'" + "was Deleted.", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(getContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getContext().startActivity(intent);
                        }else{
                            Toast.makeText(getContext(), "An error has occurred.", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayer.get().stopAll();
            }
        });
        return convertView;
    }

    public String deleteWord(){
        String fileName = textViewWords.getText().toString();
        String filePath = MainActivity.FILE_PATH + fileName + ".wav";
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
            return fileName;
        }
        return null;

    }
}
