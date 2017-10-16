package com.example.danih.mp3;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;

public class PlayerActivity extends Activity {

    static private MediaPlayer media;
    private ArrayList<File> arrayRecuperado;
    private ImageView botaoBack;
    private ImageView botaoForward;
    private ImageView botaoPlayPause;
    private TextView nomeDaMusica;
    private SeekBar seekbar;
    private TextView tempoMusica;
    private int posicao;
    private Thread atualizarSeekbar;
    private Handler  handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        botaoBack = (ImageView) findViewById(R.id.backSong);
        botaoForward = (ImageView) findViewById(R.id.forwardSong);
        botaoPlayPause = (ImageView) findViewById(R.id.pausePlay);
        nomeDaMusica = (TextView) findViewById(R.id.textSong);
        seekbar = findViewById(R.id.seekbar);
        tempoMusica = (TextView) findViewById(R.id.duracao);

        atualizarSeekbar = new Thread(){
            @Override
            public void run() {
                int duracao = media.getDuration();
                int posicaoAtual = 0;
                seekbar.setMax(duracao);
                while(posicaoAtual < duracao){
                    try{
                        sleep(500);
                        posicaoAtual = media.getCurrentPosition();
                        seekbar.setProgress(posicaoAtual);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        if(media != null){
            media.stop();
            media.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        arrayRecuperado = (ArrayList) b.getParcelableArrayList("song");
        posicao = b.getInt("pos");

        Uri uri = Uri.parse(arrayRecuperado.get(posicao).toString());
        media = MediaPlayer.create(getApplicationContext(), uri);
        nomeDaMusica.setText(arrayRecuperado.get(posicao).getName().toString().replace(".mp3", ""));
        media.start();
        atualizarSeekbar.start();

        /* PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(media != null){
                    int mCurrentPosition = media.getCurrentPosition() / 1000;
                    seekbar.setProgress(mCurrentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        }); */

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempoMusica.setText(progress/1000 + "/" + seekbar.getMax()/1000);
                //media.seekTo(progress*1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                media.seekTo(seekbar.getProgress());
            }
        });

        botaoPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(media.isPlaying()){
                    media.pause();
                    botaoPlayPause.setImageResource(R.drawable.play1);
                }else{
                    media.start();
                    botaoPlayPause.setImageResource(R.drawable.pause);
                }
            }
        });

        botaoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media.stop();
                media.release();
                posicao = (posicao-1<0) ? arrayRecuperado.size() - 1: posicao - 1;
                Uri uri = Uri.parse(arrayRecuperado.get(posicao).toString());
                media = MediaPlayer.create(getApplicationContext(), uri);
                nomeDaMusica.setText(arrayRecuperado.get(posicao).getName().toString().replace(".mp3", ""));
                media.start();
            }
        });

        botaoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media.stop();
                media.release();
                posicao = (posicao+1)%arrayRecuperado.size();
                Uri uri = Uri.parse(arrayRecuperado.get(posicao).toString());
                media = MediaPlayer.create(getApplicationContext(), uri);
                nomeDaMusica.setText(arrayRecuperado.get(posicao).getName().toString().replace(".mp3", ""));
                media.start();
            }
        });


    }
}
