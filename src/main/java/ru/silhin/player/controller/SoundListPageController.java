package ru.silhin.player.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.silhin.player.logger.SoundLogManager;
import ru.silhin.player.sounds.PlayerManager;
import ru.silhin.player.sounds.SoundListHelper;
import ru.silhin.player.sounds.SoundView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import static ru.silhin.player.controller.PlayerPageController.OPEN_PLAYLIST_FLAG;

public class SoundListPageController implements Initializable {
    private static final Logger LOGGER = SoundLogManager.getLogger();
    public static final AtomicBoolean UPDATE_CELL = new AtomicBoolean(false);

    public ListView<SoundView> listView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OPEN_PLAYLIST_FLAG.set(true);
        LOGGER.info("OPEN_PLAYLIST_FLAG: " + OPEN_PLAYLIST_FLAG.get());

        ObservableList<SoundView> soundPlaylistObservableList = FXCollections.observableArrayList();
        soundPlaylistObservableList.addAll(SoundListHelper.getInstance().getSoundPlaylist());

        listView.setItems(soundPlaylistObservableList);
        listView.setCellFactory(param -> new SoundViewCell());
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!= null) {
                PlayerManager.setSoundID(newValue.getSoundListID());
            }
        });

        update();
    }

    public void update() {
        Timeline textTimeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {
                            if (PlayerManager.getInstance() != null) {
                                listView.getSelectionModel().select(PlayerManager.getSoundID());
                            }

                            if(UPDATE_CELL.get()) {
                                ObservableList<SoundView> soundPlaylistObservableList = FXCollections.observableArrayList();
                                soundPlaylistObservableList.addAll(SoundListHelper.getInstance().getSoundPlaylist());
                                listView.getItems().clear();
                                listView.setItems(soundPlaylistObservableList);
                            }
                        }
                )
        );
        textTimeline.setCycleCount(Animation.INDEFINITE);
        textTimeline.play();
    }

    public void exit(ActionEvent event) {
        OPEN_PLAYLIST_FLAG.set(false);
        LOGGER.info("OPEN_PLAYLIST_FLAG: " + OPEN_PLAYLIST_FLAG.get());
        ((Stage) (((Node) (event.getSource())).getScene().getWindow())).close();
    }
}
