package com.example.reproductordemusica;

import static com.example.reproductordemusica.R.layout.activity_main;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private int playback=0;
    private int savedPosition=0;
    private String path = "/sdcard/changes.mp3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mediaPlayer = new MediaPlayer();
        
        
        
        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (savedInstanceState != null) {
            playback = savedInstanceState.getInt("playback",0);
            mediaPlayer.seekTo(playback);
            mediaPlayer.start();
        }
        //Metodo para repetir cancion
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        });



    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                playAudio();
                break;
            case R.id.pause:
                pauseAudio();
                break;
            case R.id.stop:
                stopAudio();
                break;
            case R.id.adelantar:
                seekForwardAudio();
                break;
            case R.id.atras:
                seekBackwardAudio();
                break;
        }
    }


// Se guarda la posicion del mediaplayer

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mediaPlayer !=null){
playback = mediaPlayer.getCurrentPosition();
outState.putInt("playback",playback);
        }
        //outState.putInt("position", savedPosition);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
/// Metodos para el audio
private void playAudio() {
    if (!mediaPlayer.isPlaying()) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.seekTo(savedPosition);
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }else{
        Toast.makeText(this, "El audio ya se esta reproduciendo", Toast.LENGTH_SHORT).show();
    }
}

    private void pauseAudio() {
        if (!mediaPlayer.isPlaying()) {
            Toast.makeText(this, "El audio ya se esta pausado", Toast.LENGTH_SHORT).show();
        }else{
            mediaPlayer.pause();
            savedPosition = mediaPlayer.getCurrentPosition();

        }
    }

    private void stopAudio() {
        if (mediaPlayer.isPlaying()) {
           mediaPlayer.stop();
            mediaPlayer.seekTo(0);
            savedPosition =0;
        }
        else{
            Toast.makeText(this, "No puedes parar un audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void seekForwardAudio() {
        if (mediaPlayer.isPlaying()) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            if (currentPosition + 10000 < duration) {
                mediaPlayer.seekTo(currentPosition + 10000);
                savedPosition = currentPosition + 10000;
            }
            else{
                Toast.makeText(this, "No puedes adelantar mas", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void seekBackwardAudio() {
        if (mediaPlayer.isPlaying()) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            if (currentPosition - 10000 <= 0) {
                mediaPlayer.seekTo(currentPosition - 10000);
                savedPosition = currentPosition - 10000;
            }
            else{
                mediaPlayer.seekTo(currentPosition - 10000);
                savedPosition = currentPosition - 10000;
            }

        }
    }




}