package it.unicam.cs.mpgc.rpg130809.model.map;

import it.unicam.cs.mpgc.rpg130809.GameConstants;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.EnemyType;

import java.util.Map;
import java.util.Random;

/**
 * Factory dedicata alla creazione e configurazione delle mappe di ciascuna zona.
 * <p>
 * Associa ogni {@link MapType} al layout testuale definito in
 * {@link GameConstants} e costruisce una {@link GameMap} completa, usata da
 * {@link it.unicam.cs.mpgc.rpg130809.model.game.GameManager}.
 * </p>
 *
 * @see GameMap
 * @see MapType
 */
public final class MapFactory {

    /**
     * Costruttore privato: la classe espone solo metodi statici.
     */
    private MapFactory() {
    }

    /**
     * Costruisce e popola una mappa completa per la zona richiesta.
     * <p>
     * I nemici vengono generati casualmente: è la creazione della zona senza
     * una registrazione precedente in {@link MapProgress}.
     * </p>
     *
     * @param mapType la zona di cui generare la mappa
     * @return la mappa di gioco pronta per l'esplorazione
     */
    public static GameMap createMap(MapType mapType) {
        return createMap(mapType, null);
    }

    /**
     * Costruisce e popola una mappa completa per la zona richiesta, generando i nemici secondo la tabella indicata.
     *
     * @param mapType la zona di cui generare la mappa
     * @param aliveEnemies la tabella posizione → tipo dei nemici vivi da riprodurre,
     *                     oppure {@code null} per la generazione casuale alla prima creazione
     * @return la mappa di gioco pronta per l'esplorazione
     */
    public static GameMap createMap(MapType mapType, Map<Position, EnemyType> aliveEnemies) {
        String[] layout = getLayoutForMap(mapType);
        GameMap map = new GameMap(GameConstants.MAP_WIDTH, GameConstants.MAP_HEIGHT, mapType);
        map.loadFromLayout(layout, new Random(), aliveEnemies);
        return map;
    }

    /**
     * Restituisce la matrice di caratteri corrispondente al layout della zona specificata.
     *
     * @param zone la zona richiesta
     * @return l'array di stringhe rappresentante la matrice di tile
     */
    private static String[] getLayoutForMap(MapType zone) {
        return switch (zone) {
            case PATH -> GameConstants.PATH_MAP_LAYOUT;
            case FOREST -> GameConstants.FOREST_MAP_LAYOUT;
            case DRAGON_PEAK -> GameConstants.DRAGON_PEAK_MAP_LAYOUT;
        };
    }
}
