package it.unicam.cs.mpgc.rpg130809;

import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.game.FontManager;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.persistence.SaveManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Punto di ingresso JavaFX dell'applicazione.
 * <p>
 * Inizializza le risorse condivise ({@link FontManager}), configura lo
 * {@link SceneManager} sullo {@link Stage} primario e mostra la schermata del
 * menu principale. La logica di gioco è delegata ai gestori del package
 * {@code model}, mentre la navigazione tra le viste è centralizzata in
 * {@link SceneManager}.
 * </p>
 *
 * @see GameLauncher
 */
public final class CardJourneyApplication extends Application {

    /**
     * Crea l'applicazione JavaFX; l'istanza è gestita dal framework.
     * <p>
     * Il costruttore deve restare pubblico e senza argomenti: il toolkit
     * JavaFX istanzia la classe tramite riflessione
     * ({@code Class.getConstructor()}) e una nasconderebbe il costruttore
     * rendendone impossibile l'avvio.
     * </p>
     */
    public CardJourneyApplication() {
    }

    /**
     * Avvia l'applicazione caricando i font, inizializzando lo stage e
     * aprendo la schermata del menu principale.
     *
     * @param stage lo stage primario fornito da JavaFX
     */
    @Override
    public void start(Stage stage) {
        FontManager.loadFonts();

        SaveManager saveManager = SaveManager.getInstance();
        GameManager.getInstance().addStateListener(saveManager);

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.initialize(stage);
        sceneManager.switchScene(GameConstants.TITLE_SCREEN_FXML);
        stage.show();
    }

    /**
     * Salva lo stato corrente alla chiusura dell'applicazione.
     * <p>
     * Chiamato dal toolkit JavaFX a fine ciclo di vita, dopo le viste e i
     * controller. Se nessuna partita è attiva (schermata del menu, giocatore
     * assente) il salvataggio viene semplicemente saltato.
     * </p>
     */
    @Override
    public void stop() {
        SaveManager.getInstance().saveOnShutdown();
    }
}
