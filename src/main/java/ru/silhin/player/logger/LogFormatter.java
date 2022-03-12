package ru.silhin.player.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RED = "\u001B[31m";

    @Override
    public String format(LogRecord record)
    {
        StringBuilder builder = new StringBuilder();

        if (Level.INFO.equals(record.getLevel())) {
            builder.append(ANSI_WHITE);
        }

        if(Level.WARNING.equals(record.getLevel())) {
            builder.append(ANSI_RED);
        }

        builder.append("[").append(date(record.getMillis())).append("] ");
        builder.append(record.getLevel()).append(" ");
        builder.append(record.getLoggerName()).append(": ");

        builder.append(record.getMessage());

        builder.append(ANSI_RESET);
        builder.append("\n");

        return builder.toString();
    }

    private String date(long milli) {
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date resultDate = new Date(milli) ;
        return date_format.format(resultDate);
    }

}
