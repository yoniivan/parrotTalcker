package com.example.parrottalker;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.parrottalker.utils.Permission;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity implements RecordDialog.RecordDialogListener{

    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/parrot/";
    public static final String WORD_INFO = "wordInfo";
    public ArrayList<RecordObject> wordList;
    private FloatingActionButton fav;
    private CustomAdapter list;
    private ListView customListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fav = findViewById(R.id.fab);

        try {
            Files.createDirectories(Paths.get(FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        wordList = new ArrayList<>();

        final File f = new File(FILE_PATH);
        File file[] = f.listFiles();
        if(file != null){
            Date date;

            RecordObject recordObject;

            for(int i = 0; i < file.length; i++){

                String name = file[i].getName().split("\\.")[0];
                try {
                    LocalDateTime timeDate = getCreateTime(file[i]);
                    date = Date.from( timeDate.atZone( ZoneId.systemDefault()).toInstant());

                    recordObject = new RecordObject(name, date);
                    wordList.add(recordObject);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogRecord();
            }
        });

        list = new CustomAdapter(this, wordList);
        customListView = findViewById(R.id.listViewMain);
        customListView.setAdapter(list);
    }

    public void openDialogRecord(){
        Permission.getInstance().requestRecordAndWriteStorage(new Permission.Listener() {
            @Override
            public void onPermission(boolean agree) {
                if(agree){
                    new RecordDialog().show(getSupportFragmentManager(), "record dialog");
                }
            }
        });
    }

    @Override
    public void wordTitle(String word, String file, long duration){
        RecordObject recordObject = new RecordObject(word);
        wordList.add(recordObject);
        Toast.makeText(MainActivity.this, word, Toast.LENGTH_LONG).show();
    }

    // GET'S DATE FROM FILE
    private static LocalDateTime getCreateTime(File file) throws IOException {
        Path path = Paths.get(file.getPath());
        BasicFileAttributeView basicfile = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        BasicFileAttributes attr = basicfile.readAttributes();
        long date = attr.creationTime().toMillis();
        Instant instant = Instant.ofEpochMilli(date);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

}
