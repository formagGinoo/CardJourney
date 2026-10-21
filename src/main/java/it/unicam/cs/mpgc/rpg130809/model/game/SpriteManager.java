package it.unicam.cs.mpgc.rpg130809.model.game;

import it.unicam.cs.mpgc.rpg130809.model.card.CardType;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.EnemyType;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestore per il caricamento e la fornitura
 * degli sprite utilizzati nelle schermate di gioco.
 * <p>
 * Implementa il pattern <b>Flyweight</b> memorizzando in cache le istanze di {@link Image}
 * caricate da disco, evitando duplicazioni in memoria e ottimizzando le performance.
 * </p>
 */
public class SpriteManager {

    private static final Map<String, Image> CACHE = new HashMap<>();

    /**
     * Restituisce lo sprite del giocatore.
     *
     * @return L'immagine del giocatore, o {@code null} se non presente
     */
    public static Image getPlayerSprite() {
        return loadSprite("/img/sprites/player.png");
    }

    /**
     * Restituisce lo sprite del negozio.
     *
     * @return L'immagine del negozio, o {@code null} se non presente
     */
    public static Image getShopSprite() {
        return loadSprite("/img/sprites/shop.png");
    }

    /**
     * Restituisce lo sprite della pozione di salute.
     *
     * @return L'immagine della pozione, o {@code null} se non presente
     */
    public static Image getHealthPotionSprite() {
        return loadSprite("/img/sprites/items/health_potion.png");
    }

    /**
     * Restituisce lo sprite del portale di uscita alla zona successiva.
     *
     * @return L'immagine dell'uscita, o {@code null} se non presente
     */
    public static Image getExitSprite() {
        return loadSprite("/img/sprites/exit.png");
    }

    /**
     * Restituisce lo sprite associato alla tipologia di nemico specificata.
     *
     * @param type Il tipo di nemico
     * @return L'immagine del nemico, o {@code null} se non presente
     */
    public static Image getEnemySprite(EnemyType type) {
        return loadSprite("/img/sprites/enemies/" + type.name().toLowerCase() + ".png");
    }

    /**
     * Restituisce lo sprite per la carta specificata.
     *
     * @param type Il tipo di carta
     * @return L'immagine della carta, o {@code null} se non presente
     */
    public static Image getCardSprite(CardType type) {
        return loadSprite("/img/sprites/cards/" + type.name().toLowerCase() + ".png");
    }

    /**
     * Carica un'immagine dal classpath sfruttando la cache in memoria.
     *
     * @param resourcePath Il percorso assoluto della risorsa all'interno del classpath
     * @return L'istanza di {@link Image} caricata, o {@code null} se la risorsa non è presente
     */
    private static Image loadSprite(String resourcePath) {
        if (CACHE.containsKey(resourcePath)) {
            return CACHE.get(resourcePath);
        }
        InputStream is = SpriteManager.class.getResourceAsStream(resourcePath);
        if (is != null) {
            Image image = new Image(is);
            CACHE.put(resourcePath, image);
            return image;
        }
        return null;
    }
}
