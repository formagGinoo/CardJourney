package it.unicam.cs.mpgc.rpg130809.controller.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TitleScreenController {
    @FXML
    private Button newGameButton;
    @FXML
    private Button loadGameButton;
    @FXML
    private Button exitGameButton;

    public void initialize() {
        //TODO: Controlla se esiste un salvataggio e abilita/disabilita il pulsante di caricamento di conseguenza.
        //loadGameButton.setVisible(false);
        //loadGameButton.setManaged(false);
    }

    @FXML
    private void onExitGameButtonClicked() {
        System.exit(0);
    }
}
