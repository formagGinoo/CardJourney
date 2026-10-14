package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller per la schermata del menu principale ({@code TitleScreen.fxml}).
 * <p>
 * Gestisce l'avvio di una nuova partita, il caricamento di salvataggi esistenti
 * e l'uscita dall'applicazione, delegando la gestione dello stato a {@link GameManager}
 * e lo switch delle scene a {@link SceneManager}.
 * </p>
 */
public class TitleScreenController {

    @FXML private Button newGameButton;
    @FXML private Button loadGameButton;

    /**
     * Inizializza i componenti grafici del menu principale.
     * <p>
     * Controlla la disponibilità di un salvataggio esistente e abilita/disabilita il pulsante di caricamento della partita.
     * </p>
     */
    public void initialize() {
        // TODO: Controlla se esiste un salvataggio e abilita/disabilita il pulsante di caricamento di conseguenza.
        // loadGameButton.setVisible(false);
        // loadGameButton.setManaged(false);
    }

    /**
     * Gestisce all' avvio di una nuova partita.
     * <p>
     * Inizializza lo stato del giocatore tramite il {@link GameManager}
     * e richiede a {@link SceneManager} il passaggio alla schermata della mappa.
     * </p>
     */
    @FXML
    private void onNewGameButtonClicked() {
        GameManager.getInstance().startNewGame();
        SceneManager.getInstance().switchScene("/fxml/MapSelection.fxml");
    }

    /**
     * Gestisce l'evento di chiusura dell'applicazione.
     */
    @FXML
    private void onExitGameButtonClicked() {
        System.exit(0);
    }
}
