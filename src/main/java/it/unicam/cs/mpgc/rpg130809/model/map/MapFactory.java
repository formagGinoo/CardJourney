package it.unicam.cs.mpgc.rpg130809.model.map;

import it.unicam.cs.mpgc.rpg130809.GameConstants;

/**
 * Factory dedicata alla creazione e configurazione delle mappe di ciascuna zona.
 */
public class MapFactory {
    /**
     * Costruisce e popola una mappa completa per la zona richiesta.
     *
     * @param mapType La zona di cui generare la mappa
     * @return La mappa di gioco pronta per l'esplorazione
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
     * @param zone La zona richiesta
     * @return L'array di stringhe rappresentante la matrice di tile
     */
    public static String[] getLayoutForMap(MapType zone) {
        return switch (zone) {
            case PATH -> GameConstants.PATH_MAP_LAYOUT;
            case FOREST -> GameConstants.FOREST_MAP_LAYOUT;
            case DRAGON_PEAK -> GameConstants.DRAGON_PEAK_MAP_LAYOUT;
        };
    }
}
