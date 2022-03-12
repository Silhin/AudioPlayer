package ru.silhin.player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.silhin.player.logger.SoundLogManager;
import ru.silhin.player.utils.ResourceManager;

import java.util.Objects;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger LOGGER = SoundLogManager.getLogger();

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/assets/fxml/scenes/player.fxml")));
            Scene scene = new Scene(root, 400, 420, Color.TRANSPARENT);
            scene.getStylesheets().add(ResourceManager.STYLESHEETS);

            primaryStage.setMinHeight(scene.getHeight());
            primaryStage.setMinWidth(scene.getWidth());
            primaryStage.setMaxHeight(scene.getHeight());
            primaryStage.setMaxWidth(scene.getWidth());

            primaryStage.setOpacity(0.8f);

            primaryStage.getIcons().add(ResourceManager.APP_ICON_IMAGE);
            primaryStage.setTitle("Player");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (NullPointerException e) {
            LOGGER.warning(e.getMessage());
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/assets/fxml/scenes/error.fxml")));
                Scene scene = new Scene(root);

                primaryStage.setScene(scene);
                primaryStage.show();
            } catch (NullPointerException ex) {
                LOGGER.warning(e.getMessage());
                System.exit(-1);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
