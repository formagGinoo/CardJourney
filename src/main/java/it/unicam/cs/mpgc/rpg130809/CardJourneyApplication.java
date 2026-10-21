package it.unicam.cs.mpgc.rpg130809;

import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.game.FontManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class CardJourneyApplication extends Application {
    @Override
    public void start(Stage stage) {
        FontManager.loadFonts();
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.initialize(stage);
        sceneManager.switchScene("/fxml/TitleScreen.fxml");
        stage.show();
    }
}
