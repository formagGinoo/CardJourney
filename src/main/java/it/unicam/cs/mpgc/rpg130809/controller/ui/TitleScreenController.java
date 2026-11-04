package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.GameConstants;
import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.game.SoundManager;
import it.unicam.cs.mpgc.rpg130809.persistence.SaveManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

        loadGameButton.setDisable(!SaveManager.getInstance().hasSave());
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
        Platform.exit();
    }

    /**
     * Gestisce l'evento di visualizzazione delle istruzioni di gioco.
     */
    @FXML
    private void onHowToPlayButtonClicked() {
        SceneManager.getInstance().switchScene(GameConstants.HOW_TO_PLAY_FXML);
    }

    /**
     * Gestisce il click sul pulsante {@code Continua Partita}.
     * <p>
     * Carica il salvataggio tramite {@link SaveManager} e ricostruisce lo
     * stato di gioco. Al caricamento si apre direttamente la selezione delle
     * zone, dove la zona corrente è l'unica accessibile. Se il salvataggio è
     * illeggibile o corrotto, un messaggio avvisa il giocatore e la partita
     * non viene avviata.
     * </p>
     */
    @FXML
    private void onLoadGameButtonClicked() {
        try {
            Player player = SaveManager.getInstance().load();
            GameManager.getInstance().setPlayer(player);
        } catch (RuntimeException e) {
            new Alert(Alert.AlertType.WARNING,
                "Il salvataggio non è ripristinabile e verrà rimosso. Avvia una nuova partita.").show();
            SaveManager.getInstance().delete();
            loadGameButton.setDisable(true);
            return;
        }
        SceneManager.getInstance().switchScene(GameConstants.MAP_SELECTION_FXML);
    }
}
