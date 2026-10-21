package it.unicam.cs.mpgc.rpg130809.model.map;

import it.unicam.cs.mpgc.rpg130809.GameConstants;

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
     *
     * @param mapType la zona di cui generare la mappa
     * @return la mappa di gioco pronta per l'esplorazione
     */
    public static GameMap createMap(MapType mapType) {
        String[] layout = getLayoutForMap(mapType);
        GameMap map = new GameMap(GameConstants.MAP_WIDTH, GameConstants.MAP_HEIGHT, mapType);
        map.loadFromLayout(layout);
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
