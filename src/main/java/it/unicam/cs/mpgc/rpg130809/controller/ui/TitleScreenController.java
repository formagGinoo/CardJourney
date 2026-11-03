package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.GameConstants;
import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.game.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller per la schermata del menu principale ({@code TitleScreen.fxml}).
 * <p>
 * Gestisce l'avvio di una nuova partita, il caricamento di salvataggi esistenti
 * e l'uscita dall'applicazione, delegando la gestione dello stato a {@link GameManager}
 * e lo switch delle scene a {@link SceneManager}.
 * </p>
 * <p>
 * La nuova partita crea un {@link it.unicam.cs.mpgc.rpg130809.model.entity.Player}
 * con il mazzo iniziale e apre la schermata di selezione delle mappe. Il
 * caricamento dei salvataggi è previsto ma non ancora implementato.
 * </p>
 */
public class TitleScreenController {

    /**
     * Crea il controller del menu principale, istanziato da JavaFX tramite il file FXML.
     */
    public TitleScreenController() {
    }

    @FXML private Button loadGameButton;

    /**
     * Inizializza i componenti grafici del menu principale.
     * <p>
     * Controlla la disponibilità di un salvataggio esistente e abilita/disabilita il pulsante di caricamento della partita.
     * </p>
     */
    public void initialize() {
        SoundManager.getInstance().playMusic(GameConstants.MAIN_THEME);

        // TODO: Controlla se esiste un salvataggio e abilita/disabilita il pulsante di caricamento di conseguenza.
        // loadGameButton.setVisible(false);
        // loadGameButton.setManaged(false);
    }

    /**
     * Gestisce l'avvio di una nuova partita.
     * <p>
     * Inizializza lo stato del giocatore tramite il {@link GameManager}
     * e richiede a {@link SceneManager} il passaggio alla schermata della mappa.
     * </p>
     */
    @FXML
    private void onNewGameButtonClicked() {
        GameManager.getInstance().startNewGame();
        SceneManager.getInstance().switchScene(GameConstants.MAP_SELECTION_FXML);
    }

    /**
     * Gestisce l'evento di chiusura dell'applicazione.
     */
    @FXML
    private void onExitGameButtonClicked() {
        System.exit(0);
    }

    /**
     * Gestisce l'evento di visualizzazione delle istruzioni di gioco.
     */
    @FXML
    private void onHowToPlayButtonClicked() {
        SceneManager.getInstance().switchScene(GameConstants.HOW_TO_PLAY_FXML);
    }
}
