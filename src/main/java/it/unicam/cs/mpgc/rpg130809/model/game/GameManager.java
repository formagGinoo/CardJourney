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
 * Gestore principale dello stato e della sessione di gioco.
 * <p>
 * Implementa il design pattern <b>Singleton</b> per garantire un'unica istanza
 * di gestione dello stato durante l'intera esecuzione dell'applicazione.
 * </p>
 * <p>
 * Conserva il {@link Player}, la {@link GameMap} attiva e il
 * {@link BattleManager} corrente. Le viste del package
 * {@code controller.ui} leggono e aggiornano lo stato tramite questa classe,
 * mentre la creazione di mappe e mazzi è delegata a {@link MapFactory} e
 * {@link DeckFactory}. La transizione tra le scene resta a carico di
 * {@link it.unicam.cs.mpgc.rpg130809.controller.SceneManager}.
 * </p>
 */
public final class GameManager {

    private static GameManager instance;
    private Player player;
    private GameMap activeGameMap;
    private GameStatus status;
    private Position currentBattlePosition;
    private Enemy currentBattleEnemy;
    private BattleManager activeBattleManager;

    /**
     * Costruttore privato conforme al pattern Singleton.
     */
    private GameManager() {
    }

    /**
     * Restituisce l'istanza di {@link GameManager}.
     * <p>
     * Se l'istanza non esiste ancora, viene creata.
     * </p>
     *
     * @return l'istanza condivisa di {@link GameManager}
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Avvia una nuova partita.
     * <p>
     * Crea un nuovo {@link Player} con il mazzo iniziale fornito da
     * {@link DeckFactory#createStarterDeck()} e i valori base di salute e oro,
     * quindi carica la mappa corrispondente al livello corrente del giocatore.
     * Il giocatore inizia nella prima mappa (Sentiero Scosceso).
     * </p>
     */
    public void startNewGame() {
        this.player = new Player(DeckFactory.createStarterDeck());
        MapType mapType = MapType.fromLevel(player.getCurrentMapLevel());
        this.activeGameMap = MapFactory.createMap(mapType);
        this.status = GameStatus.IN_PROGRESS;
    }

    /**
     * Restituisce il giocatore attualmente associato alla sessione di gioco.
     *
     * @return il giocatore corrente, oppure {@code null} se nessuna partita è attiva
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Imposta il giocatore della sessione di gioco.
     * <p>
     * Questo metodo è utilizzato principalmente durante il caricamento di una
     * partita salvata. La mappa attiva viene ricreata in base al livello del
     * giocatore.
     * </p>
     *
     * @param player il giocatore da associare alla sessione
     */
    public void setPlayer(Player player) {
        this.player = player;
        MapType mapType = MapType.fromLevel(player.getCurrentMapLevel());
        this.activeGameMap = MapFactory.createMap(mapType);
        this.status = GameStatus.IN_PROGRESS;
    }

    /**
     * Restituisce lo stato complessivo della partita corrente.
     *
     * @return lo stato della partita, oppure {@code null} se nessuna partita è attiva
     */
    public GameStatus getGameStatus() {
        return status;
    }

    /**
     * Restituisce la mappa di gioco attualmente attiva.
     *
     * @return la mappa attiva, oppure {@code null} se non è ancora stata caricata una zona
     */
    public GameMap getActiveGameMap() {
        return activeGameMap;
    }

    /**
     * Carica e inizializza la mappa specificata tramite {@link MapFactory}.
     * <p>
     * Aggiorna anche il livello della mappa corrente del giocatore.
     * </p>
     *
     * @param mapType il tipo di mappa da caricare
     * @throws IllegalStateException if no player has been initialized yet
     */
    public void loadGameMap(MapType mapType) {
        if (player == null) {
            throw new IllegalStateException("Cannot load a map before a player is initialized");
        }
        player.setCurrentMapLevel(mapType.getLevel());
        this.activeGameMap = MapFactory.createMap(mapType);
    }

    /**
     * Avvia un combattimento con il nemico specificato alla posizione indicata.
     * <p>
     * Crea un nuovo {@link BattleManager} per il giocatore corrente e avvia
     * subito la battaglia, memorizzando posizione e nemico per la conclusione
     * dell'incontro.
     * </p>
     *
     * @param pos la posizione del nemico sulla mappa
     * @param enemy l'istanza del nemico
     */
    public void startBattle(Position pos, Enemy enemy) {
        this.currentBattlePosition = pos;
        this.currentBattleEnemy = enemy;
        this.activeBattleManager = new BattleManager(player, enemy);
        this.activeBattleManager.startBattle();
    }

    /**
     * Restituisce il gestore della battaglia attualmente attiva.
     *
     * @return il {@link BattleManager} attivo, o {@code null} se non in battaglia
     */
    public BattleManager getActiveBattleManager() {
        return activeBattleManager;
    }

    /**
     * Restituisce il nemico attualmente affrontato.
     *
     * @return l'istanza del nemico, oppure {@code null} se non in battaglia
     */
    public Enemy getCurrentBattleEnemy() {
        return currentBattleEnemy;
    }

    /**
     * Conclude la battaglia corrente.
     * <p>
     * Se la battaglia è stata vinta, il nemico viene rimosso dalla mappa e il
     * giocatore viene spostato sulla casella dell'incontro. In ogni caso i
     * riferimenti alla battaglia vengono azzerati.
     * </p>
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

    /**
     * Completa la zona corrente.
     * <p>
     * Fa avanzare la progressione del giocatore con
     * {@link Player#advanceToNextMap()}, libera la mappa attiva e aggiorna lo
     * stato della partita: se l'ultima zona appena completata non è la finale
     * la partita resta {@link GameStatus#IN_PROGRESS}, in caso contrario la
     * partita è completata e assume {@link GameStatus#VICTORY}; instradare
     * verso la schermata di vittoria resta compito delle viste.
     * </p>
     *
     * @return il nuovo {@link GameStatus} della partita
     * @throws IllegalStateException if no player has been initialized yet
     */
    public GameStatus completeCurrentZone() {
        if (player == null) {
            throw new IllegalStateException("Cannot complete a zone before a player is initialized");
        }
        this.status = (activeGameMap != null && activeGameMap.getMapType().hasNextLevel())
            ? GameStatus.IN_PROGRESS
            : GameStatus.VICTORY;
        player.advanceToNextMap();
        this.activeGameMap = null;
        return status;
    }
}
