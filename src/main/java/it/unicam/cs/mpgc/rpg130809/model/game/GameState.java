package it.unicam.cs.mpgc.rpg130809.model.game;

import it.unicam.cs.mpgc.rpg130809.GameConstants;

/**
 * Rappresenta lo stato della sessione di gioco.
 * <p>
 * Il valore è mantenuto dal {@link GameManager}, che lo aggiorna nei punti di
 * mutazione dello stato della sessione (nuova partita, ingresso in mappa,
 * inizio e fine battaglia, shop, completamento zona, sconfitta). Le viste del
 * package {@code it.unicam.cs.mpgc.rpg130809.controller.ui} lo leggono per
 * instradare il flusso tramite {@link #getSceneFxml()}.
 * </p>
 * <p>
 * Lo stato governa anche la persistenza tramite {@link #isSavable()}: gli
 * stati non ripristinabili (battaglia in corso, partita terminata per
 * sconfitta) non generano salvataggi, così il file su disco conserva sempre
 * l'ultimo stato coerente da cui riprendere.
 * </p>
 */
public enum GameState {

    /** Nessuna partita attiva: la sessione non è ancora stata avviata o caricata. */
    NOT_STARTED(GameConstants.TITLE_SCREEN_FXML),

    /** La partita è attiva ma il giocatore sta scegliendo la zona da esplorare. */
    MAP_SELECTION(GameConstants.MAP_SELECTION_FXML),

    /** Il giocatore sta esplorando la mappa della zona corrente. */
    EXPLORING(GameConstants.MAP_VIEW_FXML),

    /** Una battaglia a turni è in corso. Non ripristinabile: non genera salvataggi. */
    IN_BATTLE(GameConstants.BATTLE_VIEW_FXML),

    /** Il giocatore sta acquistando nel negozio della zona corrente. */
    IN_SHOP(GameConstants.SHOP_VIEW_FXML),

    /** Il giocatore ha completato l'ultima zona: partita vinta. */
    VICTORY(GameConstants.VICTORY_VIEW_FXML),

    /** Il giocatore ha perso una battaglia: la sessione corrente è terminata. Non genera salvataggi. */
    GAME_OVER(GameConstants.TITLE_SCREEN_FXML);

    private final String sceneFxml;

    GameState(String sceneFxml) {
        this.sceneFxml = sceneFxml;
    }

    /**
     * Restituisce il percorso FXML della schermata associata a questo stato.
     *
     * @return il percorso della risorsa FXML della schermata dello stato
     */
    public String getSceneFxml() {
        return sceneFxml;
    }

    /**
     * Indica se in questo stato è possibile salvare la sessione di gioco.
     * <p>
     * Gli stati esclusi sono quelli da cui non si può riprendere la partita:
     * una battaglia a metà non è ripristinabile e la sconfitta terminerebbe la
     * sessione con un giocatore a 0 HP. Saltare il salvataggio in questi stati
     * conserva sul disco l'ultimo salvataggio coerente, da cui il giocatore
     * riprende con "Continua partita".
     * </p>
     *
     * @return {@code true} se lo stato può essere salvato, {@code false} altrimenti
     */
    public boolean isSavable() {
        return this != NOT_STARTED && this != IN_BATTLE && this != GAME_OVER;
    }
}
