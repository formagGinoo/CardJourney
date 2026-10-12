package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.player.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller JavaFX per la schermata della mappa del mondo ({@code MapScreen.fxml}).
 * <p>
 * Gestisce l'interfaccia di selezione delle zone, visualizzando le regioni disponibili
 * e disabilitando quelle inaccessibili in base alla progressione attuale del giocatore
 * registrata in {@link GameManager}.
 * </p>
 */
public class MapScreenController {

    @FXML
    private Button zone1Button;

    @FXML
    private Button zone2Button;

    @FXML
    private Button zone3Button;

    /**
     * Inizializza il controller dopo il caricamento del file FXML.
     * Recupera lo stato del giocatore dal {@link GameManager} e aggiorna
     * lo stato di abilitazione dei pulsanti delle zone.
     */
    @FXML
    public void initialize() {
        Player player = GameManager.getInstance().getPlayer();
        if (player != null) {
            updateZoneAvailability(player.getCurrentZone());
        }
    }

    /**
     * Aggiorna lo stato visivo e l'interattività dei pulsanti della mappa.
     * <p>
     * Regola di progressione: il giocatore non può tornare indietro a zone già superate
     * né saltare a zone future. Solo il pulsante corrispondente alla zona corrente
     * in cui si trova il giocatore rimane attivo.
     * </p>
     *
     * @param currentZone Il numero della zona attuale del giocatore (1, 2 o 3)
     */
    private void updateZoneAvailability(int currentZone) {
        if (zone1Button != null) zone1Button.setDisable(currentZone != 1);
        if (zone2Button != null) zone2Button.setDisable(currentZone != 2);
        if (zone3Button != null) zone3Button.setDisable(currentZone != 3);
    }

    /**
     * Gestisce il click sul pulsante della Zona 1 (Sentiero Scosceso).
     */
    @FXML
    private void onZone1Clicked() {
        // TODO: Avviare la battaglia o il flusso per la Zona 1 (Sentiero Scosceso)
    }

    /**
     * Gestisce il click sul pulsante della Zona 2 (Boschetto Bisunto).
     */
    @FXML
    private void onZone2Clicked() {
        // TODO: Avviare la battaglia o il flusso per la Zona 2 (Boschetto Bisunto)
    }

    /**
     * Gestisce il click sul pulsante della Zona 3 (Picco del Drago).
     */
    @FXML
    private void onZone3Clicked() {
        // TODO: Avviare la battaglia o il flusso per la Zona 3 (Picco del Drago)
    }
}
