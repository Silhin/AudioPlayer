package ru.silhin.player.manager;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ru.silhin.player.utils.ConfigFacade;

public class MediaOnReady implements Runnable {

    private Image image;
    private Duration duration;
    private String title;

    private final Media sounds;

    public MediaOnReady(MediaPlayer player) {
        this.sounds = player.getMedia();
    }

    @Override
    public void run() {
        this.duration = sounds.getDuration();

        this.image = (Image) sounds.getMetadata().get("image");
        this.title = String.valueOf(sounds.getMetadata().get("title"));

        System.out.println("Media Player play : " + getTitle());
    }

    public Duration getDuration() {
        return duration == null ? Duration.ZERO : duration;
    }

    public Image getImage() {
        return image == null ? new Image(ConfigFacade.getResource("/assets/images/no_image.png")) : image;
    }

    public String getTitle() {
        if ((title == null) || title.equalsIgnoreCase("null")) {
                String text = sounds.getSource().replace("%20", " ");
                text = text.replace("/", "\\");
                return (text.substring(text.lastIndexOf("\\")+1));
        }
        return title;
    }
}

