package ru.silhin.player;

import ru.silhin.player.logger.SoundLogManager;
import ru.silhin.player.sounds.SoundLoader;
import ru.silhin.player.sounds.SoundListHelper;
import ru.silhin.player.utils.ConfigFacade;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

public class Launcher {
    private static final Logger LOGGER = SoundLogManager.getLogger();

    private static final int numOfThreads = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        LOGGER.info("CLI args: " + Arrays.toString(args));
        SoundListHelper.create();
        try {
            String dirURL = "";
            for (String str : args) {
                File file = new File(str);
                if (file.exists() && (file.isFile() || file.isDirectory())) {
                    dirURL = str;
                    break;
                }
            }

            if (dirURL.isEmpty()) {
                ConfigFacade configFacade = new ConfigFacade("/assets/config.json");
                configFacade.read();
                dirURL = configFacade.getMusicFolderUrl();
            }

            LOGGER.info("Music Folder : " + dirURL);

            Thread thread = new Thread(new SoundLoader(new File(dirURL)));
            thread.start();

        } catch (NullPointerException e) {
            LOGGER.warning(e.getMessage());
        }

        Main.main(args);
    }
}
