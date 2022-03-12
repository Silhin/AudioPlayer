module ru.silhin.player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.logging;

    opens ru.silhin.player to javafx.fxml, javafx.base, javafx.media, com.fasterxml.jackson.core, com.fasterxml.jackson.databind;
    exports ru.silhin.player;
    exports ru.silhin.player.controller;
    exports ru.silhin.player.sounds;
    exports ru.silhin.player.events;
    exports ru.silhin.player.utils;
}