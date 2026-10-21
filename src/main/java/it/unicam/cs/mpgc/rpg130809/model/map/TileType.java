package it.unicam.cs.mpgc.rpg130809.model.map;

/**
 * Tipi di cella che possono comporre la griglia di una {@link GameMap}.
 * <p>
 * Il tipo di ogni tile viene assegnato durante il caricamento del layout da
 * {@link GameMap#loadFromLayout(String[])} e consultato per validare i
 * movimenti del giocatore e per gestire gli incontri.
 * </p>
 */
public enum TileType {

    /** Cella calpestabile */
    WALKABLE,

    /** Cella non calpestabile. */
    OBSTACLE,

    /** Cella del negozio. */
    SHOP,

    /** Cella di uscita verso la zona successiva. */
    EXIT,

    /** Cella occupata da un nemico comune. */
    ENEMY,

    /** Cella occupata dal boss della zona. */
    BOSS
}
