package it.unicam.cs.mpgc.rpg130809;

import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.game.FontManager;
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
public class CardJourneyApplication extends Application {

    /**
     * Crea l'applicazione JavaFX; l'istanza è gestita dal framework.
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
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.initialize(stage);
        sceneManager.switchScene("/fxml/TitleScreen.fxml");
        stage.show();
    }
}
