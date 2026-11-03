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

    /** Brano del menu principale, condiviso da TitleScreen e MapSelection. */
    public static final String MAIN_THEME = "/sounds/main_theme.mp3";

    /** Brano della schermata di vittoria. */
    public static final String VICTORY_THEME = "/sounds/victory_theme.mp3";

    /** Brano della schermata dello shop. */
    public static final String SHOP_THEME = "/sounds/shop_theme.mp3";

    /** Volume di riproduzione della musica, da 0.0 (muto) a 1.0 (massimo). */
    public static final double MUSIC_VOLUME = 0.05;

    /** Vista del menu principale. */
    public static final String TITLE_SCREEN_FXML = "/fxml/TitleScreen.fxml";

    /** Vista di istruzioni di gioco. */
    public static final String HOW_TO_PLAY_FXML = "/fxml/HowToPlayView.fxml";

    /** Vista di selezione delle zone. */
    public static final String MAP_SELECTION_FXML = "/fxml/MapSelection.fxml";

    /** Vista della mappa esplorabile della zona corrente. */
    public static final String MAP_VIEW_FXML = "/fxml/MapView.fxml";

    /** Vista della battaglia. */
    public static final String BATTLE_VIEW_FXML = "/fxml/BattleView.fxml";

    /** Vista dei risultati di una battaglia. */
    public static final String BATTLE_RESULT_VIEW_FXML = "/fxml/BattleResultView.fxml";

    /** Vista del negozio. */
    public static final String SHOP_VIEW_FXML = "/fxml/ShopView.fxml";

    /** Vista di visualizzazione del mazzo del giocatore. */
    public static final String DECK_VIEW_FXML = "/fxml/DeckView.fxml";

    /** Vista di vittoria dell'intera partita. */
    public static final String VICTORY_VIEW_FXML = "/fxml/VictoryView.fxml";

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
