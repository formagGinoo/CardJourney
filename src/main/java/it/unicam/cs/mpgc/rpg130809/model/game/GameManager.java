package it.unicam.cs.mpgc.rpg130809.model.game;

import it.unicam.cs.mpgc.rpg130809.model.battle.BattleManager;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.map.GameMap;
import it.unicam.cs.mpgc.rpg130809.model.map.MapFactory;
import it.unicam.cs.mpgc.rpg130809.model.map.MapType;
import it.unicam.cs.mpgc.rpg130809.model.map.Position;
import it.unicam.cs.mpgc.rpg130809.model.deck.DeckFactory;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;

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
    private Position currentBattlePosition;
    private Enemy currentBattleEnemy;
    private BattleManager activeBattleManager;

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

    /**
     * Avvia un incontro di combattimento con il nemico specificato alla posizione indicata.
     *
     * @param pos La posizione del nemico sulla mappa
     * @param enemy L'istanza del nemico
     */
    public void startBattle(Position pos, Enemy enemy) {
        this.currentBattlePosition = pos;
        this.currentBattleEnemy = enemy;
        this.activeBattleManager = new BattleManager(player, enemy);
        this.activeBattleManager.startBattle();
    }

    /**
     * @return Il {@link BattleManager} attualmente attivo, o {@code null} se non in battaglia
     */
    public BattleManager getActiveBattleManager() {
        return activeBattleManager;
    }

    /**
     * @return La posizione del nemico nel combattimento attuale
     */
    public Position getCurrentBattlePosition() {
        return currentBattlePosition;
    }

    /**
     * @return L'istanza del nemico attualmente affrontato
     */
    public Enemy getCurrentBattleEnemy() {
        return currentBattleEnemy;
    }

    /**
     * Conclude la battaglia corrente. Se vinta, rimuove il nemico e sposta il giocatore su quella casella.
     *
     * @param won {@code true} se il giocatore ha vinto la battaglia
     */
    public void endCurrentBattle(boolean won) {
        if (won && activeGameMap != null && currentBattlePosition != null) {
            activeGameMap.removeEnemy(currentBattlePosition);
            activeGameMap.movePlayer(currentBattlePosition);
        }
        this.currentBattlePosition = null;
        this.currentBattleEnemy = null;
        this.activeBattleManager = null;
    }
}
