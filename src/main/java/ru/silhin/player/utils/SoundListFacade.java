package ru.silhin.player.utils;

import javafx.scene.media.Media;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public final class SoundListFacade {

    public static boolean audioValidate(final String fileName) {
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

    private static Collection<Media> readDirectory(final File file) throws NullPointerException {
        final ArrayList<Media> list = new ArrayList<>();

        if(file.isDirectory()) {
            for (File file1 : Objects.requireNonNull(file.listFiles())) {

                System.out.println(
                        "File{name: " + file1.getName() +
                        ", path: " + file1.getAbsolutePath() +
                        ", isDirectory: " + file1.isDirectory() +
                        ", isFile: " + file1.isFile()
                );


                if(file1.isDirectory()) {
                    list.addAll(SoundListFacade.readDirectory(file1));
                    continue;
                }
                if(SoundListFacade.audioValidate(file1.getName())) {
                    Media media = new Media(file1.getAbsoluteFile().toURI().toString());
                    list.add(media);
                }
            }
        } else {
            if(file.isFile()) {
                if(SoundListFacade.audioValidate(file.getName())) {
                    Media media = new Media(file.getAbsoluteFile().toURI().toString());
                    list.add(media);
                }
            }
        }

        return list;
    }

    public static Collection<Media> read() {
        final ArrayList<Media> list = new ArrayList<>();

        ConfigFacade configFacade = new ConfigFacade("/assets/config.json");
        configFacade.read();
        String dirURL = configFacade.getMusicFolderUrl();

        System.out.println("Music Folder : " + dirURL);

        if(dirURL == null) return list;
        list.addAll(read(dirURL));

        return list;
    }

    public static Collection<Media> read(String path) {
        final ArrayList<Media> list = new ArrayList<>();

        try {
                File dir = new File(path);
                list.addAll(readDirectory(dir));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return list;
    }
}
