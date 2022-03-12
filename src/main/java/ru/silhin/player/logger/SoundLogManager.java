package ru.silhin.player.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class SoundLogManager {

    public static Logger getLogger() {
        return SoundLogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
    }

    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);

        Handler handler = new ConsoleHandler();
        handler.setFormatter(new LogFormatter());

        logger.setUseParentHandlers(false);
        logger.addHandler(handler);

        logger.setFilter(record -> true);

        return logger;
    }

}
