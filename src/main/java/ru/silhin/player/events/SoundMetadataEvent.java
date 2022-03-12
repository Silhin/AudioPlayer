package ru.silhin.player.events;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ru.silhin.player.logger.SoundLogManager;
import ru.silhin.player.utils.ResourceManager;

import java.util.logging.Logger;

public class SoundMetadataEvent implements Runnable {
    private static final Logger LOGGER = SoundLogManager.getLogger();
    public static final SoundMetadataEvent SOUNDS_METADATA_EMPTY = new SoundMetadataEvent((Media) null);

    private final Media sound;

    public SoundMetadataEvent(MediaPlayer player) {
        this.sound = player.getMedia();
    }

    public SoundMetadataEvent(Media media) { this.sound = media; }

    @Override
    public void run() {
        LOGGER.info("Sound Play: " + this.getTitle());
        LOGGER.info(
                String.format("Sound Duration: %02d:%02d",
                Math.round(this.getDuration().toSeconds() / 60),
                Math.round(this.getDuration().toSeconds() % 60))
        );
    }

    public Duration getDuration() {
        return sound == null || sound.getDuration() == null ? Duration.ZERO : sound.getDuration();
    }

    public Image getImage() {
        return sound != null && sound.getMetadata().get("image") != null ? (Image) sound.getMetadata().get("image") : ResourceManager.NO_SOUND_IMAGE;
    }

    public String getTitle() {
        if(sound != null) {
            if (sound.getMetadata().get("title") == null) {
                String text = sound.getSource().replace("%20", " ").replace("/", "\\");
                return (text.substring(text.lastIndexOf("\\") + 1));
            }
            return String.valueOf(sound.getMetadata().get("title"));
        }
        return " ";
    }
}

