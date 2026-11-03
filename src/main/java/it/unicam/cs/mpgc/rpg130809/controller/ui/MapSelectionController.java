package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.GameConstants;
import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.map.MapType;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
import it.unicam.cs.mpgc.rpg130809.model.game.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller per la schermata della mappa del mondo ({@code MapSelection.fxml}).
 * <p>
 * Gestisce l'interfaccia di selezione delle zone, visualizzando le zone disponibili
 * e disabilitando quelle inaccessibili in base alla progressione attuale del giocatore
 * registrata in {@link GameManager}. La zona scelta viene caricata tramite
 * {@link GameManager#loadGameMap(MapType)} e la transizione a
 * {@code MapView.fxml} è delegata a {@link SceneManager}.
 * </p>
 */
public class MapSelectionController {

    /**
     * Crea il controller della selezione mappe, istanziato da JavaFX tramite il file FXML.
     */
    public MapSelectionController() {
    }

    @FXML private Button pathButton;
    @FXML private Button forestButton;
    @FXML private Button dragonPeakButton;

    /**
     * Recupera lo stato del giocatore dal {@link GameManager} e aggiorna
     * quali zone sono accessibili.
     */
    @FXML
    public void initialize() {
        SoundManager.getInstance().playMusic(GameConstants.MAIN_THEME);

        Player player = GameManager.getInstance().getPlayer();
        updateMapVisibility(player.getCurrentMapLevel());
    }

    /**
     * Aggiorna quale mappa è visibile e abilitata in base ai progressi del giocatore.
     * <p>
     * Il giocatore non può tornare indietro a una mappa già superate
     * né saltare a una mappa futura. Solo il pulsante corrispondente alla mappa corrente
     * in cui si trova il giocatore rimane attivo.
     * </p>
     *
     * @param currentMapLevel il livello della mappa attuale del giocatore
     */
    private void updateMapVisibility(int currentMapLevel) {
        if (pathButton != null) pathButton.setDisable(currentMapLevel != 1);
        if (forestButton != null) forestButton.setDisable(currentMapLevel != 2);
        if (dragonPeakButton != null) dragonPeakButton.setDisable(currentMapLevel != 3);
    }

    /**
     * Gestisce il click sul pulsante per andare al Sentiero Scosceso.
     */
    @FXML
    private void onPathButtonClicked() {
        GameManager.getInstance().loadGameMap(MapType.PATH);
        SceneManager.getInstance().switchScene(GameConstants.MAP_VIEW_FXML);
    }

    /**
     * Gestisce il click sul pulsante per andare al Boschetto Bisunto.
     */
    @FXML
    private void onForestButtonClicked() {
        GameManager.getInstance().loadGameMap(MapType.FOREST);
        SceneManager.getInstance().switchScene(GameConstants.MAP_VIEW_FXML);
    }

    /**
     * Gestisce il click sul pulsante per andare al Picco del Drago.
     */
    @FXML
    private void onDragonPeakButtonClicked() {
        GameManager.getInstance().loadGameMap(MapType.DRAGON_PEAK);
        SceneManager.getInstance().switchScene(GameConstants.MAP_VIEW_FXML);
    }
}
