package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import javafx.fxml.FXML;

/**
 * Controller per la schermata delle istruzioni di gioco ({@code HowToPlayView.fxml}).
 * <p>
 * È una vista in sola lettura: mostra le regole base del gioco, il movimento sulla
 * mappa, il calcolo del danno e le combo disponibili in battaglia, il modo in cui
 * si avanza di zona e il funzionamento del negozio.
 * L'unica interazione possibile è il ritorno al menu principale.
 * </p>
 * <p>
 * La transizione di scena viene delegata a {@link SceneManager}, che riporterà
 * l'utente al {@code TitleScreen.fxml}.
 * </p>
 */
public class HowToPlayViewController {

    /**
     * Crea il controller delle istruzioni di gioco, istanziato da JavaFX tramite il file FXML.
     */
    public HowToPlayViewController() {
    }

    /**
     * Abbandona la schermata delle istruzioni e torna al menu principale.
     * <p>
     * La transizione viene delegata a {@link SceneManager}.
     * </p>
     */
    @FXML
    private void onBackToTitle() {
        SceneManager.getInstance().switchScene("/fxml/TitleScreen.fxml");
    }
}
