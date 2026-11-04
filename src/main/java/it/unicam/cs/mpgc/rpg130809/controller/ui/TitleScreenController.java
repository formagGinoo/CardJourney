package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.GameConstants;
import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.game.SoundManager;
import it.unicam.cs.mpgc.rpg130809.persistence.SaveData;
import it.unicam.cs.mpgc.rpg130809.persistence.SaveManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

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
 * caricamento ripristina giocatore e progressione dei nemici dal salvataggio.
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
     * Se esiste un salvataggio, chiede conferma all'utente perché i progressi
     * correnti andranno perduti: alla conferma il salvataggio viene rimosso
     * prima di iniziare, così lo stato su disco non contiene mai per sbaglio
     * i progressi della partita precedente. Inizializza poi lo stato del
     * giocatore tramite il {@link GameManager} e richiede a
     * {@link SceneManager} il passaggio alla schermata della mappa.
     * </p>
     */
    @FXML
    private void onNewGameButtonClicked() {
        if (SaveManager.getInstance().hasSave() && !confirmSaveOverwrite()) {
            return;
        }
        SaveManager.getInstance().delete();
        GameManager.getInstance().startNewGame();
        SceneManager.getInstance().switchScene(GameConstants.MAP_SELECTION_FXML);
    }

    /**
     * Mostra la finestra di conferma per la sovrascrittura del salvataggio.
     *
     * @return {@code true} se l'utente conferma l'avvio di una nuova partita
     */
    private boolean confirmSaveOverwrite() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Nuova Partita");
        alert.setHeaderText("Attenzione: Esiste già un salvataggio!");
        setWrappedContent(alert, "Avviando una nuova partita il salvataggio esistente verrà cancellato "
            + "e i progressi andranno persi. Vuoi continuare?");
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    private static void setWrappedContent(Alert alert, String message) {
        Label content = new Label(message);
        content.setWrapText(true);
        alert.getDialogPane().setContent(content);
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
     * stato di gioco: giocatore e registro dei nemici rimasti vivi, così le
     * zone ricreate riproducono esattamente i nemici non ancora sconfitti.
     * Al caricamento si apre direttamente la selezione delle
     * zone, dove la zona corrente è l'unica accessibile. Se il salvataggio è
     * illeggibile o corrotto, un messaggio avvisa il giocatore e la partita
     * non viene avviata.
     * </p>
     */
    @FXML
    private void onLoadGameButtonClicked() {
        try {
            SaveData data = SaveManager.getInstance().load();
            GameManager.getInstance().setPlayer(data.toPlayer(), data.toMapProgress());
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Caricamento non riuscito");
            alert.setHeaderText("Salvataggio corrotto o illeggibile");
            setWrappedContent(alert, "Il salvataggio non è ripristinabile e verrà rimosso. Avvia una nuova partita.");
            alert.show();
            SaveManager.getInstance().delete();
            loadGameButton.setDisable(true);
            return;
        }
        SceneManager.getInstance().switchScene(GameConstants.MAP_SELECTION_FXML);
    }
}
