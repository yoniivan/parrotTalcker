package com.example.parrottalker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parrottalker.utils.ActionButton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class RecordDialog extends AppCompatDialogFragment  {

    private EditText recordEditText;
    private ActionButton record, stop, play;
    private RecordDialogListener listener;
    private String outputFile;
    private RecordMethods rm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        createFolder(MainActivity.FILE_PATH);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_pop_up_record, null);

        recordEditText = view.findViewById(R.id.record_edit_text);
        record = view.findViewById(R.id.record);
        stop = view.findViewById(R.id.pause);
        play = view.findViewById(R.id.play);

        final AlertDialog builder = new AlertDialog.Builder(getActivity()).setView(view)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .show();

        final Button positive = builder.getButton(AlertDialog.BUTTON_POSITIVE);

        positive.setOnClickListener(view1 -> {
            String wordTitle = recordEditText.getText().toString();

            if(wordTitle.length() == 0){
                Toast.makeText(getActivity(), "You must give a name.", Toast.LENGTH_LONG).show();
                return;
            }

            if(rm == null){
                Toast.makeText(getActivity(), "You didn't record any word.", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent =new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            builder.dismiss();
        });

        record.setEnabled(true);
        stop.setEnabled(false);


        // RECORD BUTTON
        record.setListener(v -> {
            setState(false,stop,play);
            outputFile = MainActivity.FILE_PATH + recordEditText.getText() + ".wav";
            rm = new RecordMethods(outputFile, getActivity());
            rm.record();
            record.setEnabled(false);
            stop.setEnabled(true);
        });

        // STOP BUTTON
        stop.setListener(v -> {
            setState(false,record,play);
            rm.stop();
            record.setEnabled(true);
            stop.setEnabled(false);

            Toast.makeText(getActivity(), "stop Record", Toast.LENGTH_SHORT).show();
        });

        // PLAY BUTTON
        play.setListener(v -> {
            setState(false,record,stop);
            if(rm != null){
                rm.play();
            }

        });
        return builder;
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (RecordDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement RecordDialogListener");
        }
    }

    public interface RecordDialogListener {
        void wordTitle(String word, String fle, long duration);
    }

    public void setState(boolean enable ,View... v){
        for(View vi : v){
            vi.setAlpha(enable ? 0.5f: 1f);
        }
    }

    public void createFolder(String filePath){
        File directory = new File(filePath);
        if (!(directory.exists() && directory.isDirectory())) {
            try {
                Files.createDirectories(Paths.get(filePath));
            } catch (IOException e) {}
        }
    }

}
