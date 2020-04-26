package com.example.parrottalker;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import java.io.File;
import java.io.IOException;

public class RecordMethods {

    private String outputFile;
    private MediaRecorder myMediaRecorder;
    private Context context;

    private long startHTime = 0L;
    private long endHTime = 0L;
    private long totalTime = 0L;

    public RecordMethods(String outputFile, Context context){
        this.outputFile = outputFile;
        this.context = context;

        myMediaRecorder = new MediaRecorder();
        myMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myMediaRecorder.setAudioSamplingRate(16000);
        myMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myMediaRecorder.setOutputFile(outputFile);
    }


    public void play(){
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void record(){
        try {

            myMediaRecorder.prepare();
            myMediaRecorder.start();
            startHTime = System.currentTimeMillis();
        } catch (IllegalStateException ise){
            // make somthing
        }catch (IOException ioe){
            //make somthing
        }
    }

    public void stop(){
        try {
            myMediaRecorder.stop();
            myMediaRecorder.release();
            myMediaRecorder = null;
            endHTime = System.currentTimeMillis();
            totalTime = endHTime - startHTime;
            File file = new File(outputFile);
            String fileName = file.getName().split("\\.")[0];

            saveDate(context, totalTime, fileName);

        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    public void saveDate(Context context, long duration, String name){
        SharedPreferences myPrefs = context.getSharedPreferences(MainActivity.WORD_INFO,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString(name, duration + "");
        editor.commit();
    }



}
