package ru.silhin.player.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import ru.silhin.player.events.SoundMetadataEvent;
import ru.silhin.player.logger.SoundLogManager;
import ru.silhin.player.sounds.SoundListHelper;
import ru.silhin.player.sounds.SoundView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import static ru.silhin.player.controller.PlayerPageController.OPEN_PLAYLIST_FLAG;
import static ru.silhin.player.controller.SoundListPageController.UPDATE_CELL;

public class SoundViewCell extends ListCell<SoundView> {
    private static final Logger LOGGER = SoundLogManager.getLogger();

    private FXMLLoader mLLoader = null;

    @FXML
    public Label soundTitle;

    @FXML
    public ImageView soundImage;

    @FXML
    public GridPane gridPane;

    @Override
    protected void updateItem(SoundView item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/assets/fxml/soundListCell.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            gridPane.setOnDragDetected(event -> {
                LOGGER.info("Drag Detected");
                Dragboard board = gridPane.startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(item.getSoundListID()));
                content.putHtml(item.getSoundTitle());

                board.setContent(content);
                UPDATE_CELL.set(true);
                LOGGER.info("UPDATE_CELL: " + UPDATE_CELL.get());
            });

            gridPane.setOnDragEntered(event -> {
                Dragboard board = event.getDragboard();

                LOGGER.info("Drag Entered");
                LOGGER.info("Item ID = " + item.getSoundListID());
                LOGGER.info("Item Title = " + item.getSoundTitle());
                LOGGER.info("Board ID = " + board.getString());
                LOGGER.info("Board Title = " + board.getHtml());

                ArrayList<Media> playlist = SoundListHelper.getInstance().getPlaylist();
                int soundID = Integer.parseInt(board.getString());
                if(item.getSoundListID() != soundID || !playlist.get(item.getSoundListID()).equals(playlist.get(soundID))) {
                    SoundListHelper.getInstance().getPlaylist().add(item.getSoundListID(), playlist.get(soundID));
                    SoundListHelper.getInstance().getPlaylist().remove(soundID);

                    ClipboardContent content = new ClipboardContent();
                    SoundMetadataEvent metadata = new SoundMetadataEvent(playlist.get(item.getSoundListID()));
                    content.putString(String.valueOf(item.getSoundListID()));
                    content.putHtml(metadata.getTitle());
                    board.setContent(content);
                }
            });

            gridPane.setOnDragDone(event -> {
                LOGGER.info("Drag Done");
                UPDATE_CELL.set(false);
                LOGGER.info("UPDATE_CELL: " + UPDATE_CELL.get());
            });

            soundImage.setImage(item.getSoundImage());
            soundTitle.setText(item.getSoundTitle());

            setText(null);
            setGraphic(gridPane);
        }
    }
}
