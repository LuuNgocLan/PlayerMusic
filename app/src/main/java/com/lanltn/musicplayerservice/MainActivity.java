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
        songList.add(new Song("1","Baby one more time", "https://a.tumblr.com/tumblr_m75w84HcKi1qav986o1.mp3",0.2,1,"https://data.whicdn.com/images/267356458/original.png","LISA",Song.SONG_CHOOSE_PLAY,2));
        songList.add(new Song("2","DUDUDU",   "http://minhlacongai.com/wp-content/uploads/2018/09/t%C3%B3c-d%C3%A0i-u%E1%BB%91n-xo%C4%83n-nh%E1%BA%B9-ph%E1%BA%A7n-%C4%91u%C3%B4i-758x532.jpg",0.2,1,"https://p.scdn.co/mp3-preview/6f93741e97573296a477f3a325c5b83e54d56c19?cid=73c8b632d25b48b3b537832fb728dc29;","LISA",Song.SONG_CHOOSE_PLAY,2));
        songList.add(new Song("3","Thunderlounds", "https://a.tumblr.com/tumblr_m75w84HcKi1qav986o1.mp3",0.2,1,"https://photo-2-baomoi.zadn.vn/w1000_r1/2018_06_30_329_26715244/6f05eb58201ec940900f.jpg","LISA",Song.SONG_CHOOSE_PLAY,2));
        songList.add(new Song("4","IU", "https://a.tumblr.com/tumblr_m75w84HcKi1qav986o1.mp3",0.2,1,"https://i.pinimg.com/originals/72/93/74/72937431e02a8ddeda520d1ba0ff1f74.png","LISA",Song.SONG_CHOOSE_PLAY,2));
        songList.add(new Song("5","DUDUDU", "https://a.tumblr.com/tumblr_m75w84HcKi1qav986o1.mp3",0.2,1,"https://i.pinimg.com/236x/88/0a/d0/880ad0cf9a00885e09a1a82428782fdd--bigbang-live-bigbang-gd.jpg","LISA",Song.SONG_CHOOSE_PLAY,2));
        songList.add(new Song("6","DUDUDU", "https://a.tumblr.com/tumblr_m75w84HcKi1qav986o1.mp3",0.2,1,"https://i.pinimg.com/736x/eb/30/c4/eb30c43eec3bafc8cd01774e743230f6--iu-hair-kpop-girls.jpg","LISA",Song.SONG_CHOOSE_PLAY,2));
        songList.add(new Song("7","DUDUDU", "https://a.tumblr.com/tumblr_m75w84HcKi1qav986o1.mp3",0.2,1,"https://www.sbs.com.au/popasia/sites/sbs.com.au.popasia/files/styles/full/public/IU_eating_disorder.jpg","LISA",Song.SONG_CHOOSE_PLAY,2));
        songList.add(new Song("8","DUDUDU", "https://a.tumblr.com/tumblr_m75w84HcKi1qav986o1.mp3",0.2,1,"https://vignette.wikia.nocookie.net/kpopgirls/images/d/dd/BLACKPINK_Lisa_Square_Up_Teaser_Image.png/revision/latest?cb=20180617160645","LISA",Song.SONG_CHOOSE_PLAY,2));

    }
}
