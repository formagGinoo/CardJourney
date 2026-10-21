package it.unicam.cs.mpgc.rpg130809;

/**
 * Costanti condivise dell'applicazione.
 * <p>
 * Raccoglie il titolo della finestra, le dimensioni della griglia di gioco e i
 * layout testuali delle zone. I layout sono consumati da
 * {@link it.unicam.cs.mpgc.rpg130809.model.map.GameMap#loadFromLayout(String[])}
 * tramite
 * {@link it.unicam.cs.mpgc.rpg130809.model.map.MapFactory}.
 * </p>
 */
public final class GameConstants {

    /**
     * Costruttore privato: la classe raccoglie solo costanti.
     */
    private GameConstants() {
    }

    /** Titolo mostrato nella barra della finestra. */
    public static final String GAME_TITLE = "Card Journey";

    /** Dimensione in pixel di una singola tile della mappa. */
    public static final double TILE_SIZE = 64.0;

    /** Numero di colonne della griglia di gioco. */
    public static final int MAP_WIDTH = 15;

    /** Numero di righe della griglia di gioco. */
    public static final int MAP_HEIGHT = 10;

    /**
     * Layout della zona {@link it.unicam.cs.mpgc.rpg130809.model.map.MapType#PATH}.
     * <p>
     * Ogni carattere rappresenta una tile: {@code '#'} ostacolo, {@code 'E'}
     * nemico comune, {@code 'B'} boss, {@code 'S'} negozio, {@code 'N'} uscita e
     * {@code 'P'} posizione iniziale del giocatore.
     * </p>
     */
    public static final String[] PATH_MAP_LAYOUT = {
        "############..N",
        "##...######.B.#",
        "#.E...####...##",
        "#####.#...E..##",
        "#####.......###",
        "####..E..##.###",
        "##..E..###..###",
        "#....####.S.###",
        "....###.....###",
        "P..############"
    };

    /**
     * Layout della zona {@link it.unicam.cs.mpgc.rpg130809.model.map.MapType#FOREST}.
     *
     * @see #PATH_MAP_LAYOUT
     */
    public static final String[] FOREST_MAP_LAYOUT = {
        "#############.N",
        "##########..B.#",
        "##...#####...##",
        "##.E.####.E.###",
        "##...E....#####",
        "#####....######",
        "###......######",
        "##.....E....S.#",
        "#..###...##...#",
        "P.#############"
    };

    /**
     * Layout della zona {@link it.unicam.cs.mpgc.rpg130809.model.map.MapType#DRAGON_PEAK}.
     *
     * @see #PATH_MAP_LAYOUT
     */
    public static final String[] DRAGON_PEAK_MAP_LAYOUT = {
        "##########N####",
        "########..B..##",
        "########.....##",
        "#####.#..######",
        "###.E...#######",
        "###S.....######",
        "########...####",
        "########...E###",
        "#######....####",
        "######P.#######"
    };
}
