package it.unicam.cs.mpgc.rpg130809.model.game;

import it.unicam.cs.mpgc.rpg130809.model.map.GameMap;
import it.unicam.cs.mpgc.rpg130809.model.map.MapFactory;
import it.unicam.cs.mpgc.rpg130809.model.map.MapType;
import it.unicam.cs.mpgc.rpg130809.model.player.DeckFactory;
import it.unicam.cs.mpgc.rpg130809.model.player.Player;

/**
 * Gestore principale dello stato e della sessione di gioco
 * <p>
 * Implementa il design pattern <b>Singleton</b> per garantire un'unica istanza di gestione dello stato del gioco
 * durante l'intera esecuzione dell'applicazione.
 * </p>
 */
public class GameManager {

    private static GameManager instance;
    private Player player;
    private GameMap activeGameMap;

    private GameManager() {
    }

    /**
     * Restituisce l'istanza di {@link GameManager}.
     * Se l'istanza non esiste ancora, viene creata.
     *
     * @return L'istanza di GameManager
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Avvia una nuova partita creando un nuovo giocatore con il mazzo iniziale
     * fornito da {@link DeckFactory}) e i valori base di salute e oro.
     * Il giocatore inizia nella prima mappa (Sentiero Scosceso).
     */
    public void startNewGame() {
        this.player = new Player(DeckFactory.createStarterDeck());
        MapType mapType = MapType.fromLevel(player.getCurrentMapLevel());
        this.activeGameMap = MapFactory.createMap(mapType);
    }

    /**
     * Restituisce il giocatore attualmente associato alla sessione di gioco.
     *
     * @return Il giocatore corrente, oppure {@code null} se nessuna partita è attiva
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Imposta il giocatore della sessione di gioco.
     * <p>
     * Questo metodo è utilizzato principalmente durante il caricamento di una partita salvata.
     * </p>
     *
     * @param player Il giocatore da associare alla sessione
     */
    public void setPlayer(Player player) {
        this.player = player;
        MapType mapType = MapType.fromLevel(player.getCurrentMapLevel());
        this.activeGameMap = MapFactory.createMap(mapType);
    }

    /**
     * Restituisce la mappa di gioco attualmente attiva.
     *
     * @return La mappa attiva, oppure {@code null} se non è ancora stata caricata una zona
     */
    public GameMap getActiveGameMap() {
        return activeGameMap;
    }

    /**
     * Imposta la mappa di gioco attiva.
     *
     * @param activeGameMap La mappa da impostare
     */
    public void setActiveGameMap(GameMap activeGameMap) {
        this.activeGameMap = activeGameMap;
    }

    /**
     * Carica e inizializza la mappa specificata tramite {@link MapFactory}.
     *
     * @param mapType Il tipo di mappa da caricare
     */
    public void loadGameMap(MapType mapType) {
        if (player == null) {
            throw new IllegalStateException("Cannot load a map before a player is initialized");
        }
        player.setCurrentMapLevel(mapType.getLevel());
        this.activeGameMap = MapFactory.createMap(mapType);
    }
}
