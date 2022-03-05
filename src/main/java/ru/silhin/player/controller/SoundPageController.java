package ru.silhin.player.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.util.Duration;
import ru.silhin.player.manager.MediaManager;
import ru.silhin.player.utils.ConfigFacade;
import ru.silhin.player.utils.PlaylistHelper;
import ru.silhin.player.utils.SoundListFacade;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static ru.silhin.player.Main.START_SOUND;
import static ru.silhin.player.manager.MediaManager.MEDIA_ON_READY;

public class SoundPageController implements Initializable {

    private boolean textFlag = true;
    private boolean pauseFlag = false;

    @FXML
    public Button pauseButton;
    @FXML
    public ImageView soundImage;
    @FXML
    public Label soundName;

    @FXML
    public Label soundTimeNow;
    @FXML
    public Label soundTimeEnd;
    @FXML
    public Slider soundTime;
    @FXML
    public ImageView repeatButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final ArrayList<Media> sounds = new ArrayList<>();
        if(START_SOUND.isEmpty()) {
            sounds.addAll(SoundListFacade.read());
        } else {
            sounds.addAll(SoundListFacade.read(START_SOUND));
        }

        PlaylistHelper.create(sounds);

        repeatButton.setImage(new Image(ConfigFacade.getResource("/assets/images/repeat_off.png")));
        soundImage.setImage(new Image(ConfigFacade.getResource("/assets/images/no_image.png")));

        MediaManager.createPlayer().play();

        update();
    }

    private void updateText() {
        AtomicReference<Short> i = new AtomicReference<>((short) 0);
        Timeline textTimeline = new Timeline(
                new KeyFrame(
                        Duration.millis(250),
                        event -> {
                            if (MEDIA_ON_READY.getTitle().length() > 20) {
                                soundName.setText(MEDIA_ON_READY.getTitle().substring(i.get())
                                        + "  " + MEDIA_ON_READY.getTitle().substring(0, i.get())
                                );

                                if (textFlag) {
                                    i.set((short) (i.get() + 1));
                                } else {
                                    i.set((short) (i.get() - 1));
                                }

                                if (i.get() >= 20 || i.get() <= 0) {
                                    textFlag = !textFlag;
                                }

                            } else {
                                if (!soundName.getText().equalsIgnoreCase(MEDIA_ON_READY.getTitle())) {
                                    soundName.setText(MEDIA_ON_READY.getTitle());
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
                            if (!soundImage.getImage().equals(MEDIA_ON_READY.getImage())) {
                                soundImage.setImage(MEDIA_ON_READY.getImage());
                            }

                            if (soundTime.getMax() != MEDIA_ON_READY.getDuration().toSeconds()) {
                                soundTime.setMax(MEDIA_ON_READY.getDuration().toSeconds());
                                soundTimeEnd.setText(String.format("%02d:%02d", Math.round(soundTime.getMax() / 60), Math.round(soundTime.getMax() % 60)));
                            }

                            soundTime.setValue(MediaManager.getInstance().getCurrentTime().toSeconds());
                            soundTimeNow.setText(String.format("%02d:%02d", Math.round(soundTime.getValue() / 60), Math.round(soundTime.getValue() % 60)));
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void update() {
        updateText();
        updateTimeAndImage();

        soundTime.valueProperty().addListener(ov -> {
            if (soundTime.isValueChanging() || soundTime.isPressed()) {
                System.out.println(MediaManager.getInstance().getCurrentTime());
                System.out.println(soundTime.getValue() * 1000);

                System.out.println(MediaManager.getInstance().getCurrentTime().multiply(soundTime.getValue() * 1000 / MediaManager.getInstance().getCurrentTime().toMillis()));
                System.out.println(MediaManager.getInstance().getTotalDuration());

                System.out.println();

                MediaManager.getInstance().seek(MediaManager.getInstance().getCurrentTime().multiply(soundTime.getValue() * 1000 / MediaManager.getInstance().getCurrentTime().toMillis()));
            }
        });
    }

    public void soundBack() {
        if(pauseFlag) {
            this.pauseButton.setText("||");
            this.pauseFlag = !this.pauseFlag;
        }
        MediaManager.backMedia();
    }

    public void pause() {
        if (this.pauseFlag) {
            this.pauseButton.setText("||");
            MediaManager.getInstance().play();
        } else {
            MediaManager.getInstance().pause();
            this.pauseButton.setText("▶");
        }
        this.pauseFlag = !this.pauseFlag;
    }

    public void soundNext() {
        if(this.pauseFlag) {
            this.pauseButton.setText("||");
            this.pauseFlag = !this.pauseFlag;
        }
        MediaManager.nextMedia();
    }

    public void repeat() {
        MediaManager.setRepeat();
        if(MediaManager.isRepeat()) {
            repeatButton.setImage(new Image(ConfigFacade.getResource("/assets/images/repeat_on.png")));
        } else {
            repeatButton.setImage(new Image(ConfigFacade.getResource("/assets/images/repeat_off.png")));
        }
    }
}
