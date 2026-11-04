package it.unicam.cs.mpgc.rpg130809.model.game;

import it.unicam.cs.mpgc.rpg130809.model.battle.BattleManager;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.EnemyType;
import it.unicam.cs.mpgc.rpg130809.model.map.GameMap;
import it.unicam.cs.mpgc.rpg130809.model.map.MapFactory;
import it.unicam.cs.mpgc.rpg130809.model.map.MapProgress;
import it.unicam.cs.mpgc.rpg130809.model.map.MapType;
import it.unicam.cs.mpgc.rpg130809.model.map.Position;
import it.unicam.cs.mpgc.rpg130809.model.deck.DeckFactory;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gestore principale dello stato e della sessione di gioco.
 * <p>
 * Implementa il design pattern <b>Singleton</b> per garantire un'unica istanza
 * di gestione dello stato durante l'intera esecuzione dell'applicazione.
 * </p>
 * <p>
 * Conserva il {@link Player}, la {@link GameMap} attiva, il {@link MapProgress}
 * con i nemici rimasti vivi in ciascuna zona e il {@link BattleManager}
 * corrente. Le viste del package
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
    private MapProgress mapProgress;
    private GameState status;
    private Position currentBattlePosition;
    private Enemy currentBattleEnemy;
    private BattleManager activeBattleManager;
    private final List<GameStateListener> stateListeners;

    /**
     * Costruttore privato conforme al pattern Singleton.
     */
    private GameManager() {
        this.stateListeners = new ArrayList<>();
        this.mapProgress = new MapProgress();
        this.status = GameState.NOT_STARTED;
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
     * azzera la progressione dei nemici e prepara la mappa corrispondente al
     * livello corrente del giocatore. Lo stato della sessione diventa
     * {@link GameState#MAP_SELECTION}: la zona parte quando il giocatore la
     * sceglie esplicitamente.
     * </p>
     */
    public void startNewGame() {
        this.player = new Player(DeckFactory.createStarterDeck());
        this.mapProgress.reset();
        createMapFor(MapType.fromLevel(player.getCurrentMapLevel()));
        this.status = GameState.MAP_SELECTION;
    }

    /**
     * Registra un listener da notificare quando lo stato persistente del
     * giocatore cambia.
     * <p>
     * È il punto di aggancio del salvataggio automatico: l'applicazione
     * registra all'avvio un listener che delega a {@code SaveManager}, senza
     * che il modello dipenda dalla persistenza concreta.
     * Questo metodo in futuro potrebbe essere utilizzato anche per
     * statistiche, telemetry o altre funzionalità
     * che reagiscono ai cambiamenti dello stato di gioco.
     * </p>
     *
     * @param listener il listener da registrare
     * @throws IllegalArgumentException if the listener is null
     */
    public void addStateListener(GameStateListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("State listener cannot be null");
        }
        this.stateListeners.add(listener);
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
     * Restituisce il registro dei nemici rimasti vivi in ciascuna zona.
     *
     * @return il {@link MapProgress} della sessione corrente
     */
    public MapProgress getMapProgress() {
        return mapProgress;
    }

    /**
     * Imposta il giocatore della sessione di gioco ripristinando la progressione dei nemici.
     * <p>
     * Questo metodo è utilizzato durante il caricamento di una partita
     * salvata: lo stato della sessione diventa {@link GameState#MAP_SELECTION},
     * da cui il giocatore sceglie la zona da esplorare.
     * </p>
     *
     * @param player il giocatore da associare alla sessione
     * @param mapProgress i nemici rimasti vivi da ripristinare
     * @throws IllegalArgumentException if the map progress is null
     */
    public void setPlayer(Player player, MapProgress mapProgress) {
        if (mapProgress == null) {
            throw new IllegalArgumentException("Map progress cannot be null");
        }
        this.player = player;
        this.mapProgress = mapProgress;
        createMapFor(MapType.fromLevel(player.getCurrentMapLevel()));
        this.status = GameState.MAP_SELECTION;
    }

    /**
     * Restituisce lo stato complessivo della sessione di gioco.
     *
     * @return lo stato corrente della sessione, {@link GameState#NOT_STARTED}
     *         se nessuna partita è ancora attiva
     */
    public GameState getGameStatus() {
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
     * Aggiorna il livello della mappa corrente del giocatore e passa allo
     * stato {@link GameState#EXPLORING}. I nemici
     * sono generati secondo {@link MapProgress} in base a {@link MapType}.
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
        createMapFor(mapType);
        this.status = GameState.EXPLORING;
        notifyGameStateChanged();
    }

    /**
     * Avvia un combattimento con il nemico specificato alla posizione indicata.
     * <p>
     * Crea un nuovo {@link BattleManager} per il giocatore corrente, passa allo
     * stato {@link GameState#IN_BATTLE} e avvia subito la battaglia,
     * memorizzando posizione e nemico per la conclusione dell'incontro.
     * </p>
     *
     * @param pos la posizione del nemico sulla mappa
     * @param enemy l'istanza del nemico
     */
    public void startBattle(Position pos, Enemy enemy) {
        this.currentBattlePosition = pos;
        this.currentBattleEnemy = enemy;
        this.activeBattleManager = new BattleManager(player, enemy);
        this.status = GameState.IN_BATTLE;
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
     * Se la battaglia è stata vinta, il nemico viene rimosso dalla mappa, la
     * sconfitta viene registrata in {@link MapProgress} (così il nemico non
     * respawnerebbe nemmeno ricreando la zona), il giocatore viene spostato
     * sulla casella dell'incontro e lo stato torna {@link GameState#EXPLORING}.
     * In caso di sconfitta la sessione assume {@link GameState#GAME_OVER}: lo
     * stato non è salvabile, così il file su disco conserva l'ultimo
     * salvataggio pre battaglia da cui riprendere.
     * </p>
     *
     * @param won {@code true} se il giocatore ha vinto la battaglia
     */
    public void endCurrentBattle(boolean won) {
        if (won && activeGameMap != null && currentBattlePosition != null) {
            activeGameMap.removeEnemy(currentBattlePosition);
            mapProgress.recordDefeat(activeGameMap.getMapType().getLevel(), currentBattlePosition);
            activeGameMap.movePlayer(currentBattlePosition);
        }
        this.currentBattlePosition = null;
        this.currentBattleEnemy = null;
        this.activeBattleManager = null;
        this.status = won ? GameState.EXPLORING : GameState.GAME_OVER;
        notifyGameStateChanged();
    }

    /**
     * Completa la zona corrente.
     * <p>
     * Fa avanzare la progressione del giocatore con
     * {@link Player#advanceToNextMap()}, libera la mappa attiva e aggiorna lo
     * stato della sessione: se l'ultima zona appena completata non è la finale
     * si torna a {@link GameState#MAP_SELECTION} per scegliere la zona
     * successiva, in caso contrario la partita è completata e assume
     * {@link GameState#VICTORY}.
     * </p>
     *
     * @return il nuovo {@link GameState} della partita
     * @throws IllegalStateException if no player has been initialized yet
     */
    public GameState completeCurrentZone() {
        if (player == null) {
            throw new IllegalStateException("Cannot complete a zone before a player is initialized");
        }
        this.status = (activeGameMap != null && activeGameMap.getMapType().hasNextLevel())
            ? GameState.MAP_SELECTION
            : GameState.VICTORY;
        player.advanceToNextMap();
        this.activeGameMap = null;
        notifyGameStateChanged();
        return status;
    }

    /**
     * Apre il negozio della zona corrente, portando la sessione in
     * {@link GameState#IN_SHOP}. Non notifica i listener: l'ingresso nello shop
     * non modifica dati persistenti.
     *
     * @return il nuovo {@link GameState} della sessione
     */
    public GameState enterShop() {
        this.status = GameState.IN_SHOP;
        return status;
    }

    /**
     * Chiude il negozio e torna all'esplorazione della mappa corrente, senza
     * ricaricarla. Non notifica i listener: non modifica dati persistenti.
     *
     * @return il nuovo {@link GameState} della sessione
     */
    public GameState exitShop() {
        this.status = GameState.EXPLORING;
        return status;
    }

    /**
     * Abbandona la vista della mappa corrente tornando alla selezione delle
     * zone, senza liberare la mappa: il giocatore potrà rientrare nella stessa
     * zona ricreandola con {@link MapProgress}. Non notifica i listener: non
     * modifica dati persistenti.
     *
     * @return il nuovo {@link GameState} della sessione
     */
    public GameState returnToMapSelection() {
        this.status = GameState.MAP_SELECTION;
        return status;
    }

    /**
     * Crea la mappa della zona indicata e ne registra i nemici se la zona non
     * ha ancora una registrazione nel {@link MapProgress}.
     * <p>
     * Alla prima creazione di una zona i nemici sono generati casualmente e la
     * loro composizione viene registrata: tutte le successive ricreazioni
     * usano {@link MapProgress}. Una zona registrata con
     * tutti i nemici uccisi resta quindi vuota.
     * </p>
     *
     * @param mapType il tipo di mappa da creare
     */
    private void createMapFor(MapType mapType) {
        Map<Position, EnemyType> aliveEnemies = mapProgress.hasZone(mapType.getLevel())
            ? mapProgress.getAliveEnemies(mapType.getLevel())
            : null;
        this.activeGameMap = MapFactory.createMap(mapType, aliveEnemies);
        if (aliveEnemies == null) {
            mapProgress.registerZone(mapType.getLevel(), activeGameMap.getEnemyTypes());
        }
    }

    /**
     * Notifica a tutti i listener registrati che lo stato persistente di
     * gioco è cambiato.
     * <p>
     * Le eccezioni di un listener non interrompono la partita né impediscono
     * la notifica agli altri listener: un fallimento della persistenza non
     * deve mai propagarsi al gameplay.
     * </p>
     */
    private void notifyGameStateChanged() {
        if (player == null) {
            return;
        }
        for (GameStateListener listener : stateListeners) {
            try {
                listener.onGameStateChanged(player, mapProgress, status);
            } catch (RuntimeException e) {
                System.getLogger(GameManager.class.getName()).log(System.Logger.Level.WARNING, "State listener failed", e);
            }
        }
    }
}
