package ru.silhin.player.sounds;

import javafx.scene.image.Image;

public final class SoundView {

    private final int soundListID;
    private final Image soundImage;
    private final String soundTitle;

    public SoundView(int soundListID, Image soundImage, String soundTitle) {
        this.soundListID = soundListID;
        this.soundImage = soundImage;
        this.soundTitle = soundTitle;
    }

    public Image getSoundImage() {
        return soundImage;
    }

    public String getSoundTitle() {
        return soundTitle;
    }

    public int getSoundListID() {
        return soundListID;
    }
}
