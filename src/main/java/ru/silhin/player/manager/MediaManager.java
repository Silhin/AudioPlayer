package ru.silhin.player.manager;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import ru.silhin.player.utils.PlaylistHelper;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class MediaManager
{
    private static boolean MEDIA_REPEAT = false;
    private static int SOUND_COUNT = 0;

    private static AtomicReference<MediaPlayer> PLAYER;
    public static MediaOnReady MEDIA_ON_READY;

    private MediaManager() {}

    public static MediaPlayer createPlayer() {
        ArrayList<Media> sounds = (ArrayList<Media>) PlaylistHelper.getInstance().getPlaylist();
        PLAYER = new AtomicReference<>(new MediaPlayer(sounds.get(SOUND_COUNT)));

        MEDIA_ON_READY = new MediaOnReady(PLAYER.get());

        PLAYER.get().setOnReady(MEDIA_ON_READY);
        PLAYER.get().setOnEndOfMedia(() -> {
            if(!MEDIA_REPEAT) {
                ++SOUND_COUNT;
                if (SOUND_COUNT >= sounds.size()) {
                    SOUND_COUNT = 0;
                }
            }

            MediaManager.createPlayer().play();
        });

        return PLAYER.get();
    }

    public static MediaPlayer getInstance() {
        return PLAYER.get();
    }

    public static void nextMedia() {
        PLAYER.get().stop();

        ++SOUND_COUNT;
        if(SOUND_COUNT >= PlaylistHelper.getInstance().getPlaylist().size()) {
            SOUND_COUNT = 0;
        }

        MediaManager.createPlayer().play();
    }

    public static void backMedia() {
        PLAYER.get().stop();

        if(SOUND_COUNT > 0) {
            --SOUND_COUNT;
        } else if(SOUND_COUNT < 0) SOUND_COUNT = 0;

        MediaManager.createPlayer().play();
    }

    public static void setRepeat() {
        MEDIA_REPEAT = !MEDIA_REPEAT;
    }

    public static boolean isRepeat() {
        return MEDIA_REPEAT;
    }
}
