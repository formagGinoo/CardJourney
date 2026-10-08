package it.unicam.cs.mpgc.rpg130809.controller;

import it.unicam.cs.mpgc.rpg130809.GameConstants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static SceneManager instance;
    private Stage mainStage;

    private SceneManager() {
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void initialize(Stage stage) {
        this.mainStage = stage;
        mainStage.setTitle(GameConstants.GAME_TITLE);
        mainStage.setResizable(false);
    }

    public void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = createLoader(fxmlFile);
            mainStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FXMLLoader createLoader(String fxmlPath) {
        return new FXMLLoader(SceneManager.class.getResource(fxmlPath));
    }
}
