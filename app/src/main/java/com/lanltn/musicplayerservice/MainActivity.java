package com.lanltn.musicplayerservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lanltn.musicplayerservice.model.Song;
import com.lanltn.musicplayerservice.sample.PlayerComponentView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    PlayerComponentView playerComponentView;
    Button btnPlay;
    List<Song> songList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSongData();
        playerComponentView = findViewById(R.id.player_component_view);
        btnPlay = findViewById(R.id.button);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerComponentView.setVisibility(View.VISIBLE);
                playerComponentView.setDataInputSong(songList);
                playerComponentView.onStartPlayer();
                btnPlay.setVisibility(View.GONE);
            }
        });

    }

    private void initSongData() {
        songList.add(new Song());
        songList.add(new Song());
        songList.add(new Song());
        songList.add(new Song());
        songList.add(new Song());
        songList.add(new Song());
        songList.add(new Song());
    }
}
