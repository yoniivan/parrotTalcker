package com.example.parrottalker;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordObject {

    private String word;
    private Date date;


    public RecordObject(String word){
        this.word = word;
        this.date = new Date();
    }

    public RecordObject(String word, Date date){
        this.word = word;
        this.date = date;
    }


    public String getWord() {
        return word;
    }


    public Date getDate() {
        return date;
    }

    public String dateFormat(String format){
        String DATE_FORMAT = format;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(this.getDate());
    }







}
