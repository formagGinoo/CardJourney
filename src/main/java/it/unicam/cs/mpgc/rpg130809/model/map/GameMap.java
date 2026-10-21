package it.unicam.cs.mpgc.rpg130809.model.map;

import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.EnemyFactory;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.EnemyType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Rappresenta la mappa logica di una specifica zona di gioco.
 * <p>
 * Ogni mappa è composta da una griglia di celle e a ogni cella è associato un
 * tipo {@link TileType}. {@link #loadFromLayout(String[])} popola la mappa a
 * partire da un layout testuale, dove ogni carattere rappresenta un tipo di
 * cella. La logica di assegnazione dei tipi di cella è:
 * </p>
 * <ul>
 * <li>{@link TileType#WALKABLE} per le celle calpestabili; qualsiasi carattere
 * non riconosciuto viene considerato calpestabile
 * <li>{@link TileType#OBSTACLE} per le celle non calpestabili, rappresentate dal
 * carattere {@code '#'}
 * <li>{@link TileType#ENEMY} per le celle occupate da un nemico, rappresentate
 * dal carattere {@code 'E'}
 * <li>{@link TileType#SHOP} per la cella del negozio, rappresentata dal
 * carattere {@code 'S'}
 * <li>{@link TileType#BOSS} per la cella del boss finale della mappa,
 * rappresentata dal carattere {@code 'B'}
 * <li>{@link TileType#EXIT} per la cella di uscita alla mappa successiva,
 * rappresentata dal carattere {@code 'N'}
 * </ul>
 * <p>
 * La mappa è creata da {@link MapFactory} e controllata dalla
 * {@code MapViewController}; i nemici sono istanziati tramite
 * {@link EnemyFactory} in base ai tipi previsti da {@link MapType}.
 * </p>
 */
public class GameMap {

    private final int width;
    private final int height;
    private final MapType mapType;
    private final TileType[][] tiles;
    private Position playerPosition;
    private final Map<Position, Enemy> enemies;
    private Position exitPosition;
    private Position bossPosition;
    private Position shopPosition;

    /**
     * Costruisce una nuova mappa per la tipologia di zona indicata con le dimensioni specificate.
     *
     * @param width la larghezza della mappa in numero di tile
     * @param height l'altezza della mappa in numero di tile
     * @param mapType la tipologia di zona associata alla mappa
     */
    public GameMap(int width, int height, MapType mapType) {
        this.width = width;
        this.height = height;
        this.mapType = mapType;
        this.tiles = new TileType[width][height];
        this.enemies = new HashMap<>();
    }

    /**
     * Carica e popola la mappa a partire da una matrice di caratteri rappresentante il layout.
     * <p>
     * Viene utilizzata una nuova istanza di {@link Random} per la scelta dei
     * nemici comuni.
     * </p>
     *
     * @param layout l'array di stringhe rappresentante le righe della matrice di gioco
     */
    public void loadFromLayout(String[] layout) {
        loadFromLayout(layout, new Random());
    }

    /**
     * Carica e popola la mappa a partire dal layout specificato utilizzando una data istanza di {@link Random}.
     *
     * @param layout l'array di stringhe rappresentante le righe della mappa
     * @param random l'istanza di {@link Random} per selezionare casualmente i nemici comuni
     */
    public void loadFromLayout(String[] layout, Random random) {
        enemies.clear();
        List<EnemyType> availableEnemies = mapType.getEnemies();

        for (int y = 0; y < height; y++) {
            String row = layout[y];
            for (int x = 0; x < width; x++) {
                char c = row.charAt(x);
                Position pos = new Position(x, y);
                switch (c) {
                    case '#' -> tiles[x][y] = TileType.OBSTACLE;
                    case 'S' -> {
                        tiles[x][y] = TileType.SHOP;
                        this.shopPosition = pos;
                    }
                    case 'N' -> {
                        tiles[x][y] = TileType.EXIT;
                        this.exitPosition = pos;
                    }
                    case 'P' -> {
                        tiles[x][y] = TileType.WALKABLE;
                        this.playerPosition = pos;
                    }
                    case 'E' -> {
                        tiles[x][y] = TileType.ENEMY;
                        if (availableEnemies != null && !availableEnemies.isEmpty()) {
                            EnemyType type = availableEnemies.get(random.nextInt(availableEnemies.size()));
                            enemies.put(pos, EnemyFactory.createEnemy(type));
                        }
                    }
                    case 'B' -> {
                        tiles[x][y] = TileType.BOSS;
                        this.bossPosition = pos;
                        if (mapType.getBoss() != null) {
                            enemies.put(pos, EnemyFactory.createEnemy(mapType.getBoss()));
                        }
                    }
                    default -> tiles[x][y] = TileType.WALKABLE;
                }
            }
        }
    }

    /**
     * Verifica se una determinata posizione sulla mappa è calpestabile dal giocatore.
     *
     * @param position la posizione da verificare
     * @return {@code true} se la coordinata è entro i limiti e non è un ostacolo, {@code false} altrimenti
     */
    public boolean canMoveTo(Position position) {
        if (position.getX() < 0 || position.getX() >= width || position.getY() < 0 || position.getY() >= height) {
            return false;
        }
        return tiles[position.getX()][position.getY()] != TileType.OBSTACLE;
    }

    /**
     * Verifica se il boss della mappa è stato sconfitto.
     *
     * @return {@code true} se la mappa non ha un boss oppure se il boss non è più presente tra i nemici vivi
     */
    public boolean isBossDefeated() {
        return bossPosition == null || !enemies.containsKey(bossPosition);
    }

    /**
     * Aggiorna la posizione attuale del giocatore sulla mappa.
     *
     * @param newPosition la nuova posizione di destinazione
     * @throws IllegalArgumentException if the destination is not walkable
     */
    public void movePlayer(Position newPosition) {
        if (canMoveTo(newPosition)) {
            this.playerPosition = newPosition;
        } else {
            throw new IllegalArgumentException("Cannot move to the specified position: " + newPosition);
        }
    }

    /**
     * Restituisce l'eventuale nemico situato alle coordinate specificate.
     *
     * @param position la posizione da interrogare
     * @return il nemico presente, oppure {@code null} se la cella è priva di nemici
     */
    public Enemy getEnemyAt(Position position) {
        return enemies.get(position);
    }

    /**
     * Rimuove un nemico dalla mappa in seguito alla sconfitta e ripristina la tile come calpestabile.
     *
     * @param position la coordinata da cui rimuovere il nemico
     */
    public void removeEnemy(Position position) {
        enemies.remove(position);
        if (tiles[position.getX()][position.getY()] == TileType.ENEMY || tiles[position.getX()][position.getY()] == TileType.BOSS) {
            tiles[position.getX()][position.getY()] = TileType.WALKABLE;
        }
    }

    /**
     * Restituisce la posizione attuale del giocatore.
     *
     * @return la coordinata corrente del giocatore
     */
    public Position getPlayerPosition() {
        return playerPosition;
    }

    /**
     * Restituisce la coordinata in cui si trova il negozio.
     *
     * @return la posizione del negozio, oppure {@code null} se non presente
     */
    public Position getShopPosition() {
        return shopPosition;
    }

    /**
     * Restituisce la coordinata del portale di uscita alla prossima zona.
     *
     * @return la posizione dell'uscita, oppure {@code null} se non presente
     */
    public Position getExitPosition() {
        return exitPosition;
    }

    /**
     * Restituisce la coordinata in cui è posizionato il boss finale della zona.
     *
     * @return la posizione del boss, oppure {@code null} se non presente
     */
    public Position getBossPosition() {
        return bossPosition;
    }

    /**
     * Restituisce la mappa di tutti i nemici correntemente vivi, associati alle loro coordinate.
     *
     * @return la mappa delle posizioni e dei nemici
     */
    public Map<Position, Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Restituisce la larghezza della mappa espressa in numero di tile.
     *
     * @return il numero di colonne della griglia
     */
    public int getWidth() {
        return width;
    }

    /**
     * Restituisce l'altezza della mappa espressa in numero di tile.
     *
     * @return il numero di righe della griglia
     */
    public int getHeight() {
        return height;
    }

    /**
     * Restituisce il tipo di cella ({@link TileType}) alle coordinate indicate.
     *
     * @param x coordinata orizzontale
     * @param y coordinata verticale
     * @return il tipo di tile della cella, o {@link TileType#OBSTACLE} se fuori limiti
     */
    public TileType getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return TileType.OBSTACLE;
        }
        return tiles[x][y];
    }

    /**
     * Restituisce la tipologia di zona associata a questa mappa.
     *
     * @return l'enum {@link MapType} della mappa
     */
    public MapType getMapType() {
        return mapType;
    }
}
