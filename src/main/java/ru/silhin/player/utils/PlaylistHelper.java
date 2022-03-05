package ru.silhin.player.utils;

import javafx.scene.media.Media;

import java.util.ArrayList;
import java.util.Collection;

public final class PlaylistHelper {

    private static PlaylistHelper INSTANCE;

    private final ArrayList<Media> playlist;

    private PlaylistHelper(ArrayList<Media> playlist) {
        this.playlist = playlist;
    }

    public static void create(ArrayList<Media> playlist) {
        INSTANCE = new PlaylistHelper(playlist);
    }

    public static PlaylistHelper getInstance() {
        return INSTANCE;
    }

    public Collection<Media> getPlaylist() {
        return playlist;
    }
}
