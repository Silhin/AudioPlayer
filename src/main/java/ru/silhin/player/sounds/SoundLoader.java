package ru.silhin.player.sounds;

import javafx.scene.media.Media;
import javafx.util.Duration;
import ru.silhin.player.logger.SoundLogManager;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoundLoader implements Runnable {

    private static final Logger LOGGER = SoundLogManager.getLogger();
    private final File file;

    public SoundLoader(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        readDirectory(file);
    }

    public boolean audioValidate(final String fileName) {
        String lowerFileName = fileName.toLowerCase();
        String[] nameList = lowerFileName.split("\\.");
        switch (nameList[nameList.length - 1]) {
            case "mp3":
            case "wav":
                return true;
            default:
                return false;
        }
    }

    private synchronized void readDirectory(final File file) throws NullPointerException {
        if (!file.exists()) return;

        LOGGER.info(Thread.currentThread().getName() + " | Filename: " + file.getName() + ", path: " + file.getAbsolutePath());

        if (file.isFile()) {
            if (audioValidate(file.getName())) {
                addMedia(new Media(file.getAbsoluteFile().toURI().toString()));
            }
        } else {
            try {
                if (file.isDirectory()) {
                    for (File file1 : Objects.requireNonNull(file.listFiles())) {
                        Thread thread = new Thread(new SoundLoader(file1));
                        thread.start();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void addMedia(Media media) {
        SoundListHelper.getInstance().addMediaOnPlaylist(media);
    }

}
