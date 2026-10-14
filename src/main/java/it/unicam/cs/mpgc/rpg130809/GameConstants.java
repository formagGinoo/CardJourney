package it.unicam.cs.mpgc.rpg130809;

public class GameConstants {
    public static final String GAME_TITLE = "Card Journey";
    public static final double TILE_SIZE = 64.0;
    public static final int MAP_WIDTH = 15;
    public static final int MAP_HEIGHT = 10;
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