package ru.silhin.player.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.silhin.player.events.SoundMetadataEvent;
import ru.silhin.player.logger.SoundLogManager;
import ru.silhin.player.sounds.PlayerManager;
import ru.silhin.player.sounds.SoundListHelper;
import ru.silhin.player.utils.ResourceManager;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class PlayerPageController implements Initializable {

    private static final Logger LOGGER = SoundLogManager.getLogger();
    
    public static final AtomicBoolean OPEN_PLAYLIST_FLAG = new AtomicBoolean(false);
    public ImageView playlistImage;

    public Button pauseButton;
    public ImageView soundImage;
    public Label soundName;

    public Label soundTimeNow;
    public Label soundTimeEnd;
    public Slider soundTime;
    public ImageView repeatButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repeatButton.setImage(ResourceManager.REPEAT_OFF_BUTTON_IMAGE);
        soundImage.setImage(ResourceManager.NO_SOUND_IMAGE);
        playlistImage.setImage(ResourceManager.PLAYLIST_BUTTON_IMAGE);

        LOGGER.info("Playlist = " + SoundListHelper.getInstance().getPlaylist().size());

        PlayerManager.play();
        update();
    }

    private void updateText() {
        final AtomicReference<Short> i = new AtomicReference<>((short) 0);
        final AtomicReference<Boolean> textFlag = new AtomicReference<>(true);
        Timeline textTimeline = new Timeline(
                new KeyFrame(
                        Duration.millis(250),
                        event -> {
                            SoundMetadataEvent metadata = PlayerManager.getMetadata();
                            if (metadata.getTitle().length() > 20) {
                                soundName.setText(metadata.getTitle().substring(i.get()) + "  " + metadata.getTitle().substring(0, i.get()));

                                i.set((short) (i.get() + (textFlag.get() ? 1 : -1)));
                                if(i.get() >= 20 || i.get() <= 0) {
                                    textFlag.set(!textFlag.get());
                                }
                            } else {
                                if (!soundName.getText().equalsIgnoreCase(metadata.getTitle())) {
                                    soundName.setText(metadata.getTitle());
                                }
                            }
                        }
                )
        );
        textTimeline.setCycleCount(Animation.INDEFINITE);
        textTimeline.play();
    }

    private void updateTimeAndImage() {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {
                            if (PlayerManager.getInstance() != null) {
                                soundTime.setValue(PlayerManager.getInstance().getCurrentTime().toSeconds());
                                soundTimeNow.setText(String.format("%02d:%02d", TimeUnit.SECONDS.toMinutes((long) soundTime.getValue()), Math.round(soundTime.getValue() % 60)));
                            }

                            if (!soundImage.getImage().equals(PlayerManager.getMetadata().getImage())) {
                                soundImage.setImage(PlayerManager.getMetadata().getImage());
                            }

                            if (soundTime.getMax() != PlayerManager.getMetadata().getDuration().toSeconds()) {
                                soundTime.setMax(PlayerManager.getMetadata().getDuration().toSeconds());
                                soundTimeEnd.setText(String.format("%02d:%02d", TimeUnit.SECONDS.toMinutes((long) soundTime.getMax()), Math.round(soundTime.getMax() % 60)));
                            }
                        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void update() {
        if(PlayerManager.getInstance() != null) {
            updateText();
            updateTimeAndImage();

            soundTime.valueProperty().addListener(ov -> {
                if (soundTime.isValueChanging() || soundTime.isPressed()) {

                    LOGGER.info(String.valueOf(PlayerManager.getInstance().getCurrentTime()));
                    LOGGER.info(String.valueOf(soundTime.getValue() * 1000));
                    LOGGER.info(String.valueOf(PlayerManager.getInstance().getCurrentTime().multiply(soundTime.getValue() * 1000 / PlayerManager.getInstance().getCurrentTime().toMillis())));
                    LOGGER.info(String.valueOf(PlayerManager.getInstance().getTotalDuration()));

                    PlayerManager.getInstance().seek(PlayerManager.getInstance().getCurrentTime().multiply(soundTime.getValue() * 1000 / PlayerManager.getInstance().getCurrentTime().toMillis()));
                }
            });
        } else {
            soundTime.setMax(0);
            soundTime.setValue(0);
            soundTime.setDisable(true);
            soundTimeEnd.setText("00:00");
            soundTimeNow.setText("00:00");
            soundName.setText("< Not Sound >");
            repeatButton.setDisable(true);
            pauseButton.setText("▶");
        }
    }

    public void soundPrev() {
        if (PlayerManager.getInstance() != null) {
            if (PlayerManager.getInstance().getStatus() == MediaPlayer.Status.PAUSED) {
                this.pauseButton.setText("||");
            }
            PlayerManager.prevSound();
            LOGGER.info("Play prev Sound");
        }
    }

    public void pause() {
        if(PlayerManager.getInstance() != null) {
            if (PlayerManager.getInstance().getStatus() == MediaPlayer.Status.PAUSED) {
                this.pauseButton.setText("||");
                LOGGER.info("Player play");
                PlayerManager.getInstance().play();
            } else {
                PlayerManager.getInstance().pause();
                LOGGER.info("Player pause");
                this.pauseButton.setText("▶");
            }
        } else {
            this.pauseButton.setText("▶");
        }
    }

    public void soundNext() {
        if(PlayerManager.getInstance() != null) {
            if (PlayerManager.getInstance().getStatus() == MediaPlayer.Status.PAUSED) {
                this.pauseButton.setText("||");
            }
            PlayerManager.nextSound();
            LOGGER.info("Play next Sound");
        }
    }

    public void repeat() {
        PlayerManager.setRepeat();
        if(PlayerManager.isRepeat()) {
            repeatButton.setImage(ResourceManager.REPEAT_ON_BUTTON_IMAGE);
        } else {
            repeatButton.setImage(ResourceManager.REPEAT_OFF_BUTTON_IMAGE);
        }
    }

    public void openPlaylist() {
        LOGGER.info("OPEN_PLAYLIST_FLAG: " + OPEN_PLAYLIST_FLAG.get());
        if(!OPEN_PLAYLIST_FLAG.get()) {

            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/assets/fxml/scenes/playlist.fxml")));
                Scene scene = new Scene(root, 442.0, 550.0, Color.TRANSPARENT);

                scene.getStylesheets().add(ResourceManager.STYLESHEETS);

                stage.setMinWidth(scene.getWidth());
                stage.setMinHeight(scene.getHeight());
                stage.setMaxWidth(scene.getWidth());
                stage.setMaxHeight(scene.getHeight());

                stage.setOpacity(0.8);

                stage.setScene(scene);
                stage.getIcons().add(ResourceManager.APP_ICON_IMAGE);
                stage.setTitle("Playlist");
                stage.show();
            } catch (NullPointerException | IOException e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }
}
