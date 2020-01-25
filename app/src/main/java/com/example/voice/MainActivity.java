package com.example.voice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button startrec,stoprec,startp,stopp;
    String patnsave="";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;


    final int REQUEST_PERMISSION_MODE=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startrec=findViewById(R.id.sr);
        stoprec=findViewById(R.id.stopr);
        startp=findViewById(R.id.sp);
        stopp=findViewById(R.id.stopp);

        if(!checkPermissionFromDevice()){
            requestPermission();
        }


        startrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermissionFromDevice()) {

                    patnsave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                    setupMediaRecorder();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startp.setEnabled(false);
                    stopp.setEnabled(false);

                    Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                }
                else {
                    requestPermission();
                }
            }
        });

        stoprec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                startp.setEnabled(true);
                startrec.setEnabled(true);
                stopp.setEnabled(false);
                stoprec.setEnabled(false);
            }
        });

        startp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopp.setEnabled(true);
                startrec.setEnabled(false);
                stoprec.setEnabled(false);

                mediaPlayer=new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(patnsave);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                Toast.makeText(MainActivity.this, "playing", Toast.LENGTH_SHORT).show();
            }
        });

        stopp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startrec.setEnabled(true);
                startp.setEnabled(true);
                stopp.setEnabled(false);
                stoprec.setEnabled(false);

                if(mediaPlayer!= null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    setupMediaRecorder();
                }


            }
        });

    }

    private void setupMediaRecorder(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(patnsave);
    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            },REQUEST_PERMISSION_MODE);
    }

    private boolean checkPermissionFromDevice(){
        int write_external_storage_result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result=ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result== PackageManager.PERMISSION_GRANTED &&
                record_audio_result== PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION_MODE:
            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
                break;
        }
    }
}
