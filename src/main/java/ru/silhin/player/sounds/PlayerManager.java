package ru.silhin.player.sounds;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import ru.silhin.player.events.SoundMetadataEvent;
import ru.silhin.player.logger.SoundLogManager;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class PlayerManager {
    private static final Logger LOGGER = SoundLogManager.getLogger();

    private static final AtomicBoolean PLAYER_REPEAT = new AtomicBoolean(false);
    private static final AtomicInteger SOUND_ID = new AtomicInteger(0);
    private volatile static AtomicReference<MediaPlayer> PLAYER;

    private PlayerManager() {}

    public static MediaPlayer createPlayer() {
        if(PLAYER != null && PLAYER.get() != null) PLAYER.get().dispose();

        ArrayList<Media> sounds = SoundListHelper.getInstance().getPlaylist();
        if(sounds.size() > 0) {
            PLAYER = new AtomicReference<>(new MediaPlayer(sounds.get(SOUND_ID.get())));

            PLAYER.get().setOnReady(new SoundMetadataEvent(PLAYER.get()));
            PLAYER.get().setOnEndOfMedia(() -> {
                if (!PLAYER_REPEAT.get()) {
                    SOUND_ID.addAndGet(1);
                    if (SOUND_ID.get() >= sounds.size()) {
                        SOUND_ID.set(0);
                    }
                }
                PlayerManager.play();
            });
            return PLAYER.get();
        }
        return null;
    }

    public static MediaPlayer getInstance() {
        return PLAYER == null ? null : PLAYER.get();
    }

    public static void play(){
        MediaPlayer player = PlayerManager.createPlayer();
        if(player != null) {
            player.play();
        }
    }

    public static void nextSound() {
        PLAYER.get().dispose();

        SOUND_ID.addAndGet(1);
        if(SOUND_ID.get() >= SoundListHelper.getInstance().getPlaylist().size()) {
            SOUND_ID.set(0);
        }

        PlayerManager.play();
    }

    public static void prevSound() {
        PLAYER.get().dispose();

        if(SOUND_ID.get() > 0) {
            SOUND_ID.addAndGet(-1);
        } else if(SOUND_ID.get() < 0) SOUND_ID.set(0);

        PlayerManager.play();
    }

    public static void setRepeat() {
        if(PLAYER_REPEAT.get()) {
            LOGGER.info("Repeat On");
        } else {
            LOGGER.info("Repeat Off");
        }
        PLAYER_REPEAT.set(!PLAYER_REPEAT.get());
    }

    public static boolean isRepeat() {
        return PLAYER_REPEAT.get();
    }

    public static SoundMetadataEvent getMetadata() {
        return PLAYER != null ? (SoundMetadataEvent) PLAYER.get().getOnReady() : SoundMetadataEvent.SOUNDS_METADATA_EMPTY;
    }

    public static int getSoundID() {
        return SOUND_ID.get();
    }

    public static void setSoundID(int ID) {
        if(ID == SOUND_ID.get()) return;
        if(ID < 0 || ID > SoundListHelper.getInstance().getSoundPlaylist().size()) {
            SOUND_ID.set(0);
        }
        SOUND_ID.set(ID - 1);
        PlayerManager.nextSound();
    }
}
