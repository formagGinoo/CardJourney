package it.unicam.cs.mpgc.rpg130809.model.game;

import it.unicam.cs.mpgc.rpg130809.GameConstants;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestore centralizzato della musica dell'applicazione.
 * <p>
 * Implementa il design pattern <b>Singleton</b> e mantiene un unico
 * {@link MediaPlayer} per l'intera durata dell'esecuzione. Ogni vista dichiara
 * il brano che le compete tramite {@link #playMusic(String)}: se il brano
 * richiesto è già quello in riproduzione la richiesta viene ignorata e la
 * musica continua senza ripartire dall'inizio; in caso contrario la traccia
 * corrente viene fermata e sostituita dalla nuova.
 * </p>
 * <p>
 * Grazie a questo comportamento il passaggio tra due schermate che condividono
 * lo stesso tema, come {@code TitleScreen} e {@code MapSelection}, è
 * trasparente all'utente e la traccia non viene mai interrotta. I brani sono
 * caricati dal classpath e tenuti in cache, così come avviene in
 * {@link SpriteManager} per gli sprite.
 * </p>
 */
public final class SoundManager {

    private static SoundManager instance;

    private final Map<String, Media> cache = new HashMap<>();
    private MediaPlayer currentMusic;
    private String currentTrack;

    /**
     * Costruttore privato conforme al pattern Singleton.
     */
    private SoundManager() {
    }

    /**
     * Restituisce l'unica istanza attiva di {@link SoundManager}.
     * <p>
     * L'istanza viene creata alla prima invocazione e riutilizzata per tutta la
     * durata dell'applicazione, così la riproduzione non è legata al ciclo di
     * vita delle singole viste.
     * </p>
     *
     * @return l'istanza condivisa di {@link SoundManager}
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Avvia la riproduzione del brano relativo alla risorsa indicata.
     * <p>
     * Se il brano richiesto coincide con quello già in riproduzione il metodo
     * non ha effetto, evitando ripartenze involontarie a ogni cambio di vista.
     * Se la risorsa audio non è presente nel classpath, la richiesta viene
     * ignorata e la musica rimane quella in riproduzione.
     * </p>
     *
     * @param resourcePath il percorso del brano all'interno del classpath
     */
    public void playMusic(String resourcePath) {
        if (resourcePath == null || resourcePath.equals(currentTrack)) {
            return;
        }

        Media track = loadSound(resourcePath);
        if (track == null) {
            System.err.println("Sound resource not found: " + resourcePath);
            return;
        }

        stopMusic();
        currentTrack = resourcePath;
        currentMusic = new MediaPlayer(track);
        currentMusic.setCycleCount(MediaPlayer.INDEFINITE);
        currentMusic.setVolume(GameConstants.MUSIC_VOLUME);
        currentMusic.play();
    }

    /**
     * Arresta la riproduzione del brano corrente, se presente.
     * <p>
     * Il player viene rimosso per liberare le risorse native associate.
     * </p>
     */
    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
            currentTrack = null;
        }
    }

    /**
     * Carica un brano dal classpath sfruttando la cache in memoria.
     * <p>
     * Il {@link Media} ottenuto è indipendente dalle viste e può quindi essere
     * riutilizzato da player creati in momenti diversi dell'applicazione.
     * </p>
     *
     * @param resourcePath il percorso assoluto della risorsa all'interno del classpath
     * @return l'istanza di {@link Media} caricata, o {@code null} se la risorsa non è presente
     */
    private Media loadSound(String resourcePath) {
        if (cache.containsKey(resourcePath)) {
            return cache.get(resourcePath);
        }

        URL resource = SoundManager.class.getResource(resourcePath);
        if (resource == null) {
            return null;
        }

        Media sound = new Media(resource.toExternalForm());
        cache.put(resourcePath, sound);
        return sound;
    }
}
