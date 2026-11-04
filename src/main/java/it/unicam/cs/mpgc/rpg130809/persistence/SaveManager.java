package it.unicam.cs.mpgc.rpg130809.persistence;

import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.game.GameStateListener;

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
     * Salva lo stato corrente del giocatore.
     * <p>
     * L'operazione è <b>benevola</b>: i fallimenti di I/O vengono loggati e non
     * propagati, perché un problema di salvataggio non deve mai interrompere
     * la partita. Se il file tecnico di scrittura non riesce, il file
     * precedente resta integro grazie alla scrittura atomica.
     * </p>
     *
     * @param player il giocatore di cui salvare lo stato
     * @throws IllegalArgumentException if the player is null
     */
    public void save(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        try {
            String json = serializer.toJson(SaveData.from(player));
            fileHandler.write(json);
            LOGGER.fine("Partita salvata in: " + fileHandler.getSaveFilePath());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Impossibile salvare la partita", e);
        }
    }

    /**
     * Salvataggio di sicurezza in fase di chiusura dell'applicazione.
     * <p>
     * Chiamato da {@code CardJourneyApplication#stop()} a fine ciclo di vita
     * JavaFX. A differenza di {@link #save(Player)}, un giocatore assente
     * (schermata del menu, nessuna partita attiva) è una condizione normale e
     * viene semplicemente ignorata. I fallimenti restano silenziosi, perché in
     * chiusura non c'è modo di gestirli in modo utile.
     * </p>
     */
    public void saveOnShutdown() {
        Player player = GameManager.getInstance().getPlayer();
        if (player != null) {
            save(player);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Delega al salvataggio standard: qui il giocatore è garantito non nullo
     * perché la notifica arriva da una partita attiva.
     * </p>
     *
     * @param player il giocatore nello stato corrente
     */
    @Override
    public void onPlayerStateChanged(Player player) {
        save(player);
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
     * Carica il salvataggio e ricostruisce il giocatore.
     * <p>
     * A differenza del salvataggio, qui un errore di lettura o di formato è
     * fatale: il chiamante (l'interfaccia) deve saperlo gestire e mostrare
     * all'utente che il salvataggio non è ripristinabile. I valori fuori dal
     * range di gioco vengono valutati dal costruttore designato di
     * {@code Player}, che li ridimensiona correttamente.
     * </p>
     *
     * @return il giocatore ricostruito dal salvataggio
     * @throws RuntimeException if reading, parsing or deserialization fails
     */
    public Player load() {
        try {
            String json = fileHandler.read();
            SaveData data = serializer.fromJson(json);
            return data.toPlayer();
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
