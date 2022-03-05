package ru.silhin.player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.silhin.player.utils.ConfigFacade;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class Main extends Application {

    public static String START_SOUND = "";

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/assets/scenes/player.fxml")));
            Scene scene = new Scene(root, 400, 420, Color.TRANSPARENT);

            scene.getStylesheets().add(ConfigFacade.getResource("/assets/styles/style.css"));

            System.out.println("Scene Height: " + scene.getHeight());
            System.out.println("Scene Width: " + scene.getWidth());

            primaryStage.setMinHeight(scene.getHeight());
            primaryStage.setMinWidth(scene.getWidth());

            primaryStage.setMaxHeight(scene.getHeight());
            primaryStage.setMaxWidth(scene.getWidth());

            primaryStage.setOpacity(0.8f);
            primaryStage.getIcons().add(new Image(ConfigFacade.getResource("/assets/player_icon.png")));

            primaryStage.setTitle("Player");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (NullPointerException e) {
            e.printStackTrace();

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/assets/scenes/error.fxml")));
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("text.txt"));
            writer.write("IM WRITE");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String str : args) {
            File file = new File(str);
            if (file.exists() && (file.isFile() || file.isDirectory())) {
                START_SOUND = str;
                break;
            }
        }

        launch(args);
    }
}
