package ru.silhin.player.utils;

import javafx.scene.image.Image;

import java.util.Objects;

public class ResourceManager {

    public static final String STYLESHEETS = ResourceManager.getResource("styles/style.css");
    public static final Image APP_ICON_IMAGE = new Image(ResourceManager.getResource("playerIcon.png"));
    public static final Image NO_SOUND_IMAGE = new Image(ResourceManager.getResource("images/noImage.png"));
    public static final Image REPEAT_OFF_BUTTON_IMAGE = new Image(ResourceManager.getResource("images/repeatOff.png"));
    public static final Image REPEAT_ON_BUTTON_IMAGE = new Image(ResourceManager.getResource("images/repeatOn.png"));
    public static final Image PLAYLIST_BUTTON_IMAGE = new Image(ResourceManager.getResource("images/playlist.png"));

    public static String getResource(String path) {
        return Objects.requireNonNull(Objects.requireNonNull(ResourceManager.class.getResource("/assets/" + path)).toExternalForm());
    }
}
