package com.lanltn.musicplayerservice.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Binder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.lanltn.musicplayerservice.model.Artist;
import com.lanltn.musicplayerservice.model.PlayerState;
import com.lanltn.musicplayerservice.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerMusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnBufferingUpdateListener {

    public final static String PLAYER_BROADCAST_LISTENER = "PLAYER_BROADCAST_LISTENER";

    public final static String PLAYER_UPDATE_KEY = "PLAYER_UPDATE_KEY";

    public final static String PLAYER_RESET_SEEK_BAR = "PLAYER_RESET_SEEKBAR";
    public final static String PLAYER_SONG_INFO = "PLAYER_SONG_INFO";
    public final static String PLAYER_IS_PLAYING = "PLAYER_IS_PLAYING";
    public final static String PLAYER_IS_ADD_FAVORITE = "PLAYER_IS_ADD_FAVORITE"; // true is add, false is remove
    public final static String PLAYER_IS_UPDATE_FAVORITE_WHOLE_SCREEN = "PLAYER_IS_UPDATE_FAVORITE_WHOLE_SCREEN";
    public final static String PLAYER_REMOTE_UPDATE_FAVOURITE = "PLAYER_REMOTE_UPDATE_FAVOURITE";
    public final static String PLAYER_REMOTE_ENABLE_FAVOURITE = "PLAYER_REMOTE_ENABLE_FAVOURITE";
    public final static String PLAYER_IS_SKIP_NEXT = "PLAYER_IS_SKIP_NEXT"; // true is next, false is previous
    public final static String PLAYER_SONG_INDEX = "PLAYER_SONG_INDEX";
    public final static String PLAYER_UPDATE_PRIMARY_PROGRESS_KEY = "PLAYER_UPDATE_PRIMARY_PROGRESS_KEY";
    public final static String PLAYER_CHECK_NETWORK_CONNECTION = "PLAYER_CHECK_NETWORK_CONNECTION";

    private MediaPlayer mPlayer;
    private List<Song> mSongs = new ArrayList<>();
    private List<Artist> mArtistList = new ArrayList<>();
    private int mSongIndexed;
    private final IBinder musicBind = new MusicBinder();

    private final Handler handler = new Handler();
    private int mMediaFileLengthInMilliseconds;
    private boolean mIsPlayable = false;

    private PlayerState playerState;
    private boolean isReleasedPlayer;
    private int BUFFER_STREAMING_PROGRESS = 50;
    private boolean isArtistPlayer;
    private AudioManager mAudioManager;
    private boolean audioStateMedia = false;

    private Context mMainContext;

    public void onCreate() {
        super.onCreate();
        mSongIndexed = 0;
        mPlayer = new MediaPlayer();
        isReleasedPlayer = false;
        initMusicPlayer();
        initSongData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleNotificationController(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(412);//NOTIFICATION_ID=412
    }

    private void handleNotificationController(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

//        switch (intent.getAction()) {
//            case MusicPlayerReceiver.ACTION_PAUSE:
//                pauseSong(false);
//                break;
//            case MusicPlayerReceiver.ACTION_PLAY:
//
//                pauseSong(true);
//                break;
//            case MusicPlayerReceiver.ACTION_NEXT:
//                playNextSong(true);
//                break;
//            case MusicPlayerReceiver.ACTION_PREV:
//                playPreviousSong();
//                break;
//        }
    }

    public void initMusicPlayer() {
//        mPlayer.setWakeMode(getApplicationContext(),
//                PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnBufferingUpdateListener(this);
    }

    /**
     * request focus for media
     */
    private void requestFocus() {
        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

    public void setSong(int songIndex) {
        this.mSongIndexed = songIndex;
    }

    /**
     * check network connection and play song
     */
    public void playSong() {
        initSongData();
        if (isReleasedPlayer) {
            return;
        }
        mPlayer.reset();
        isReleasedPlayer = false;
        //check network connection
        if (false) {
            mIsPlayable = false;
            broadcastIsPlayingMedia(false);
            broadcastPlaySongInfo();
            broadcastCheckNetwork();
            return;
        }

        if (mSongs == null || mSongs.size() == 0) {
            return;
        }
        requestFocus();
        String url = mSongs.get(mSongIndexed).getUrl();
        Log.d("SERVICE_LINK_PLAY:", mSongIndexed + " " + url);
        try {
            mPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SERVICE_LINK_DIE:", url);
        }

        try {
            mPlayer.prepareAsync();
            mIsPlayable = false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            broadcastPlaySongInfo();
            broadcastIsPlayingMedia(false);
        }
    }

    private void broadcastPlaySongInfo() {
        //TODO: issue about Parcelable
//        playerState = new PlayerState();
//        playerState.setKey(PLAYER_SONG_INFO);
//        playerState.setValueBoolean(isArtistPlayer);
//        playerState.setValueInt(mSongIndexed);
//        playerState.setSong(mSongs.get(mSongIndexed));
//        playerState.setArtist(mArtistList.get(getArtistIndexFromSong(playerState.getSong())));
//        playerState.setSpotifyPlaylistId(mFesChoice.getSpotifyPlaylistId());
        sendBroadcastUpdateView(playerState);
    }

    private int getArtistIndexFromSong(Song song) {
        //TODO: check list and find nums of artist in this Song
        return 0;
    }

    public void pauseSong(boolean isPaused) {
        if (!mIsPlayable) {
            broadcastCheckNetwork();
            return;
        }

        broadcastIsPlayingMedia(!isPaused);

        if (isPaused) {
            mPlayer.pause();
        } else {
            requestFocus();
            mPlayer.start();
            primarySeekBarProgressUpdater();
        }
    }

    private void broadcastCheckNetwork() {
        //TODO: ISSUE ABOUT PARCELABLE
//        playerState = new PlayerState();
//        playerState.setKey(PLAYER_CHECK_NETWORK_CONNECTION);
        sendBroadcastUpdateView(playerState);
    }

    public void setSongList(List<Artist> artistList, List<Song> theSongs) {
        mSongIndexed = 0;
        mArtistList = artistList;
        mSongs = theSongs;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mPlayer.stop();
        mPlayer.release();
        isReleasedPlayer = true;
        handler.removeCallbacksAndMessages(null);
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

        // Delay 1s for next song. Prevent next too fast if url or network errors.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                broadcastIsPlayingMedia(false);
                playNextSong(true);
            }
        }, 1000);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mIsPlayable = true;
        mMediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL
        try {
            mediaPlayer.start();
        } catch (Exception ex) {
            mIsPlayable = false;
            return;
        }
        broadcastIsPlayingMedia(true);
        broadcastResetSeekBar();
        primarySeekBarProgressUpdater();
    }

    private void broadcastResetSeekBar() {
        //TODO: ISSUE ABOUT PARCELABLE
//        playerState = new PlayerState();
//        playerState.setKey(PLAYER_RESET_SEEK_BAR);
//        playerState.setValueInt(mMediaFileLengthInMilliseconds / BUFFER_STREAMING_PROGRESS);
        sendBroadcastUpdateView(playerState);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
    }

    public void seekSongToPosition(int positionPercent) {
        int position = (mMediaFileLengthInMilliseconds * positionPercent) / 100;
        mPlayer.seekTo(position);
    }

    public class MusicBinder extends Binder {
        public PlayerMusicService getService() {
            return PlayerMusicService.this;
        }
    }

    private void primarySeekBarProgressUpdater() {
        int position = (int) (((float) mPlayer.getCurrentPosition() / mMediaFileLengthInMilliseconds) * 100);
        int currentTime = mPlayer.getCurrentPosition() / 1000;
        int leftTime = (mMediaFileLengthInMilliseconds - mPlayer.getCurrentPosition()) / 1000;
        //TODO:ISSUE ABOUT PARCELABLE
//        playerState = new PlayerState();
//        playerState.setKey(PLAYER_UPDATE_PRIMARY_PROGRESS_KEY);
//        playerState.setValueInt(mPlayer.getCurrentPosition() / BUFFER_STREAMING_PROGRESS);
//        playerState.setCurrentTime(currentTime);
//        playerState.setLeftTime(leftTime);
        sendBroadcastUpdateView(playerState);

        if (mPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1 * BUFFER_STREAMING_PROGRESS);
        }
    }

    private void sendBroadcastUpdateView(PlayerState playerState) {
        Intent intent = new Intent();
        intent.setAction(PLAYER_BROADCAST_LISTENER);
        intent.putExtra(PLAYER_UPDATE_KEY, playerState);
        sendBroadcast(intent);
    }

    private void broadcastIsPlayingMedia(boolean isPlaying) {
//        playerState = new PlayerState();
//        playerState.setKey(PLAYER_IS_PLAYING);
//        playerState.setValueBoolean(isPlaying);
        sendBroadcastUpdateView(playerState);
    }

    private boolean isAvailableNextSong() {
        if (mSongs == null) {
            return false;
        }
        return mSongIndexed < mSongs.size() - 1;
    }

    public boolean isAvailablePreviousSong() {
        return mSongIndexed != 0;
    }

    public void playNextSong(boolean isAutoNextSong) {
        if (isAvailableNextSong()) {
            mSongIndexed++;
        } else {
            // TODO: Save this condition of playing music if Client wants to stop auto playing if end list
            //  if (isAutoNextSong) {
            //     return;
            //  }

            mSongIndexed = 0;
        }

//        playerState = new PlayerState();
//        playerState.setKey(PLAYER_IS_SKIP_NEXT);
//        playerState.setValueBoolean(true);
//        playerState.setValueInt(mSongIndexed);

        sendBroadcastUpdateView(playerState);
        playSong();
    }

    public int getCurrentSong() {
        return mSongIndexed;
    }

    public void playPreviousSong() {
        if (isAvailablePreviousSong()) {
            mSongIndexed--;
        } else {
            if (mSongs != null && mSongs.size() > 1) {
                mSongIndexed = mSongs.size() - 1;
            } // else: replay same index song
        }

//        playerState = new PlayerState();
//        playerState.setKey(PLAYER_IS_SKIP_NEXT);
//        playerState.setValueBoolean(false);
//        playerState.setValueInt(mSongIndexed);
        sendBroadcastUpdateView(playerState);
        playSong();
    }

    public void playSongWithIndex(int index) {
        mSongIndexed = index;
        playSong();
    }

    public Artist getCurrentArtist() {
        int indexArtist = getArtistIndexFromSong(mSongs.get(mSongIndexed));
        return mArtistList.get(indexArtist);
    }

    public void onResetFavorite() {
    }

    public void onResetWithoutLoginFavoriteArtist() {

    }

    public void updateArtistFavorite(Artist artist) {

    }

    public void onAddFavorite() {

    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    public void setMainContext(Context mMainContext) {
        this.mMainContext = mMainContext;
    }

    public void setDataSongs(List<Song> songList) {
        this.mSongs = songList;
    }

    /**
     * FAKE DATA TO CHECK MEDIA SERVICE
     */

    private void initSongData() {
        mSongs.clear();
        mSongs.add(new Song("1", "Baby one more time", "https://a.tumblr.com/tumblr_m75w84HcKi1qav986o1.mp3", 0.2, 1, "https://data.whicdn.com/images/267356458/original.png", "LISA", Song.SONG_CHOOSE_PLAY, 2));
        mSongs.add(new Song("2", "DUDUDU", "https://p.scdn.co/mp3-preview/7ec5ca8cc8b98dd4dee8754912a8948a8dad83e8?cid=73c8b632d25b48b3b537832fb728dc29", 0.2, 1, "https://p.scdn.co/mp3-preview/6f93741e97573296a477f3a325c5b83e54d56c19?cid=73c8b632d25b48b3b537832fb728dc29;", "LISA", Song.SONG_CHOOSE_PLAY, 2));
        mSongs.add(new Song("3", "Thunderlounds", "https://p.scdn.co/mp3-preview/7ec5ca8cc8b98dd4dee8754912a8948a8dad83e8?cid=73c8b632d25b48b3b537832fb728dc29", 0.2, 1, "https://photo-2-baomoi.zadn.vn/w1000_r1/2018_06_30_329_26715244/6f05eb58201ec940900f.jpg", "LISA", Song.SONG_CHOOSE_PLAY, 2));
        mSongs.add(new Song("4", "IU", "https://p.scdn.co/mp3-preview/b29e3d5cbfe27f3ea875b2bec70d9b05b2309aa0?cid=73c8b632d25b48b3b537832fb728dc29", 0.2, 1, "https://i.pinimg.com/originals/72/93/74/72937431e02a8ddeda520d1ba0ff1f74.png", "LISA", Song.SONG_CHOOSE_PLAY, 2));
        mSongs.add(new Song("5", "DUDUDU", "https://p.scdn.co/mp3-preview/6f93741e97573296a477f3a325c5b83e54d56c19?cid=73c8b632d25b48b3b537832fb728dc29", 0.2, 1, "https://i.pinimg.com/236x/88/0a/d0/880ad0cf9a00885e09a1a82428782fdd--bigbang-live-bigbang-gd.jpg", "LISA", Song.SONG_CHOOSE_PLAY, 2));
        mSongs.add(new Song("6", "DUDUDU", "https://a.tumblr.com/tumblr_m75w84HcKi1qav986o1.mp3", 0.2, 1, "https://i.pinimg.com/736x/eb/30/c4/eb30c43eec3bafc8cd01774e743230f6--iu-hair-kpop-girls.jpg", "LISA", Song.SONG_CHOOSE_PLAY, 2));
        mSongs.add(new Song("7", "DUDUDU", "https://p.scdn.co/mp3-preview/7ec5ca8cc8b98dd4dee8754912a8948a8dad83e8?cid=73c8b632d25b48b3b537832fb728dc29", 0.2, 1, "https://www.sbs.com.au/popasia/sites/sbs.com.au.popasia/files/styles/full/public/IU_eating_disorder.jpg", "LISA", Song.SONG_CHOOSE_PLAY, 2));
        mSongs.add(new Song("8", "DUDUDU", "https://a.tumblr.com/tumblr_m75w84HcKi1qav986o1.mp3", 0.2, 1, "https://vignette.wikia.nocookie.net/kpopgirls/images/d/dd/BLACKPINK_Lisa_Square_Up_Teaser_Image.png/revision/latest?cb=20180617160645", "LISA", Song.SONG_CHOOSE_PLAY, 2));

    }
}