package it.unicam.cs.mpgc.rpg130809.persistence;

import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.game.GameState;
import it.unicam.cs.mpgc.rpg130809.model.game.GameStateListener;
import it.unicam.cs.mpgc.rpg130809.model.map.MapProgress;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Facciata del sistema di persistenza del gioco.
 * <p>
 * Implementa il design pattern <b>Singleton</b>, coerentemente con gli altri
 * gestori dell'applicazione. Coordina {@link SaveDataSerializer} e
 * {@link SaveFileHandler} per offrire le tre operazioni essenziali:
 * salvataggio, verifica di esistenza e caricamento. Implementa anche
 * {@link GameStateListener}: registrata come listener del
 * {@code GameManager}, salva automaticamente a ogni cambio rilevante di
 * stato (Observer pattern).
 * </p>
 * <p>
 * Risponde al requisito di resistenza alle chiusure forzate: ogni scrittura è
 * atomica (a carico di {@link SaveFileHandler}) e i fallimenti di I/O non
 * propagano mai un'eccezione ai chiamanti del salvataggio — un salvataggio che
 * fallisce non deve arrestare la partita in corso. Gli errori vengono
 * registrati nel log e il file precedente eventualmente resta così com'è.
 * </p>
 */
public final class SaveManager implements GameStateListener {

    private static final Logger LOGGER = Logger.getLogger(SaveManager.class.getName());

    private static SaveManager instance;

    private final SaveDataSerializer serializer;
    private final SaveFileHandler fileHandler;

    /**
     * Costruttore privato conforme al pattern Singleton.
     * <p>
     * Il file di salvataggio risiede nella cartella di lavoro corrente, così
     * resta vicino all'eseguibile e al log dell'applicazione.
     * </p>
     */
    private SaveManager() {
        this(Path.of(System.getProperty("user.dir"), "saves"));
    }

    /**
     * Costruttore di estensione, usato dai test per isolare il filesystem.
     *
     * @param saveDirectory la cartella di salvataggio da usare
     */
    SaveManager(Path saveDirectory) {
        this(new SaveDataSerializer(), new SaveFileHandler(saveDirectory));
    }

    /**
     * Costruttore completo con dipendenze iniettate.
     *
     * @param serializer il serializzatore JSON
     * @param fileHandler il gestore del file di salvataggio
     * @throws IllegalArgumentException if any dependency is null
     */
    SaveManager(SaveDataSerializer serializer, SaveFileHandler fileHandler) {
        if (serializer == null || fileHandler == null) {
            throw new IllegalArgumentException("Serializer and file handler are required");
        }
        this.serializer = serializer;
        this.fileHandler = fileHandler;
    }

    /**
     * Restituisce l'unica istanza attiva di {@link SaveManager}.
     * <p>
     * L'istanza viene creata alla prima invocazione e riutilizzata per tutta
     * la durata dell'applicazione.
     * </p>
     *
     * @return l'istanza condivisa di {@link SaveManager}
     */
    public static SaveManager getInstance() {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }

    /**
     * Salva lo stato corrente del giocatore e della sessione.
     * <p>
     * Se il salvataggio fallisce, l'errore viene loggato ma non propagato, perché un problema di salvataggio non deve mai interrompere la partita.
     * </p>
     *
     * @param player il giocatore di cui salvare lo stato
     * @param mapProgress il registro dei nemici rimasti vivi da salvare
     * @param gameState lo stato della sessione al momento del salvataggio
     * @throws IllegalArgumentException if the player or the map progress is null
     */
    public void save(Player player, MapProgress mapProgress, GameState gameState) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (mapProgress == null) {
            throw new IllegalArgumentException("Map progress cannot be null");
        }
        if (gameState == null) {
            throw new IllegalArgumentException("Game state cannot be null");
        }
        try {
            String json = serializer.toJson(SaveData.from(player, mapProgress));
            fileHandler.write(json);
            LOGGER.fine("Partita salvata in: " + fileHandler.getSaveFilePath());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Impossibile salvare la partita", e);
        }
    }

    /**
     * Salva lo stato del giocatore corrente della sessione attiva.
     * <p>
     * Variante di {@link #save(Player, MapProgress, GameState)} per i
     * salvataggi fuori da {@link GameStateListener#onGameStateChanged(Player, MapProgress, GameState)}.
     * </p>
     *
     * @param player il giocatore di cui salvare lo stato
     */
    public void save(Player player) {
        save(player, GameManager.getInstance().getMapProgress(), GameManager.getInstance().getGameStatus());
    }

    /**
     * Salvataggio di sicurezza in fase di chiusura dell'applicazione.
     * <p>
     * Chiamato da {@code CardJourneyApplication#stop()} a fine ciclo di vita.
     * Salva solo se lo stato corrente è {@link GameState#isSavable()}:
     * chiudere l'app durante una battaglia o dopo una sconfitta
     * conserva l'ultimo salvataggio coerente invece di sovrascriverlo con uno
     * stato non ripristinabile.
     * </p>
     */
    public void saveOnShutdown() {
        GameManager gameManager = GameManager.getInstance();
        Player player = gameManager.getPlayer();
        if (player != null && gameManager.getGameStatus().isSavable()) {
            save(player);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Salva solo se lo stato notificato è {@link GameState#isSavable()
     * salvabile}: gli stati non ripristinabili (battaglia in corso, sconfitta)
     * non sovrascrivono il file, che conserva l'ultimo salvataggio coerente da
     * cui riprendere con "Continua partita".
     * </p>
     *
     * @param player il giocatore nello stato corrente
     * @param mapProgress i nemici rimasti vivi nello stato corrente
     * @param gameState lo stato della sessione al momento del cambiamento
     */
    @Override
    public void onGameStateChanged(Player player, MapProgress mapProgress, GameState gameState) {
        if (gameState.isSavable()) {
            save(player, mapProgress, gameState);
        } else {
            LOGGER.fine("Salvataggio saltato: lo stato " + gameState + " non è ripristinabile");
        }
    }

    /**
     * Indica se esiste un salvataggio ripristinabile.
     *
     * @return {@code true} se esiste un salvataggio utilizzabile
     */
    public boolean hasSave() {
        return fileHandler.exists();
    }

    /**
     * Carica il salvataggio e restituisce lo snapshot dello stato di gioco.
     * <p>
     * A differenza del salvataggio, qui un errore di lettura o di formato è
     * fatale: il chiamante (l'interfaccia) deve saperlo gestire e mostrare
     * all'utente che il salvataggio non è ripristinabile. I valori fuori dal
     * range di gioco vengono valutati dal costruttore designato di
     * {@code Player}, che li ridimensiona correttamente; il chiamante ricostruisce
     * giocatore e registro dei nemici con {@link SaveData#toPlayer()} e
     * {@link SaveData#toMapProgress()}.
     * </p>
     *
     * @return lo snapshot del salvataggio, pronto per la ricostruzione dello stato
     * @throws RuntimeException if reading, parsing or deserialization fails
     */
    public SaveData load() {
        try {
            String json = fileHandler.read();
            return serializer.fromJson(json);
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Impossibile leggere il file di salvataggio", e);
        }
    }

    /**
     * Elimina il salvataggio, se presente.
     * <p>
     * Delegato alle viste per i casi "partita vinta" e "nuova partita in corso".
     * I fallimenti di I/O vengono loggati e non propagati, come per il
     * salvataggio.
     * </p>
     */
    public void delete() {
        try {
            fileHandler.delete();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Impossibile eliminare il salvataggio", e);
        }
    }

    /**
     * Restituisce il percorso del file di salvataggio attualmente in uso.
     *
     * @return il percorso del file di salvataggio
     */
    public Path getSaveFilePath() {
        return fileHandler.getSaveFilePath();
    }
}
