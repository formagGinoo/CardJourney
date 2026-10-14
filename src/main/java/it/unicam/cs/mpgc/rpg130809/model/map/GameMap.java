package it.unicam.cs.mpgc.rpg130809.model.map;

import it.unicam.cs.mpgc.rpg130809.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.enemy.EnemyFactory;
import it.unicam.cs.mpgc.rpg130809.model.enemy.EnemyType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Rappresenta la mappa logica e bidimensionale di una specifica zona di gioco.
 * <p>
 * Mantiene la griglia delle celle con i rispettivi {@link TileType}, la posizione del giocatore,
 * la posizione del negozio, dell'uscita, del Boss e la mappa dei nemici presenti sul terreno.
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
     * @param width   La larghezza della mappa in numero di tile
     * @param height  L'altezza della mappa in numero di tile
     * @param mapType La tipologia di zona associata alla mappa
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
     *
     * @param layout L'array di stringhe rappresentante le righe della matrice di gioco
     */
    public void loadFromLayout(String[] layout) {
        loadFromLayout(layout, new Random());
    }

    /**
     * Carica e popola la mappa a partire dal layout specificato utilizzando una data istanza di Random.
     *
     * @param layout L'array di stringhe rappresentante le righe della mappa
     * @param random L'istanza di Random per selezionare casualmente i nemici comuni tra quelli disponibili
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
     * @param position La posizione da verificare
     * @return {@code true} se la coordinata è entro i limiti e non è un ostacolo, {@code false} altrimenti
     */
    public boolean canMoveTo(Position position) {
        if (position.getX() < 0 || position.getX() >= width || position.getY() < 0 || position.getY() >= height) {
            return false;
        }
        return tiles[position.getX()][position.getY()] != TileType.OBSTACLE;
    }

    /**
     * Aggiorna la posizione attuale del giocatore sulla mappa.
     *
     * @param newPosition La nuova posizione di destinazione
     * @throws IllegalArgumentException se la posizione non è calpestabile
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
     * @param position La posizione da interrogare
     * @return Il nemico presente, oppure {@code null} se la cella è priva di nemici
     */
    public Enemy getEnemyAt(Position position) {
        return enemies.get(position);
    }

    /**
     * Rimuove un nemico dalla mappa in seguito alla sconfitta e ripristina la tile come calpestabile.
     *
     * @param position La coordinata da cui rimuovere il nemico
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
     * @return La coordinata corrente del giocatore
     */
    public Position getPlayerPosition() {
        return playerPosition;
    }

    /**
     * Restituisce la coordinata in cui si trova il negozio.
     *
     * @return La posizione del negozio, oppure {@code null} se non presente
     */
    public Position getShopPosition() {
        return shopPosition;
    }

    /**
     * Restituisce la coordinata del portale di uscita alla prossima zona.
     *
     * @return La posizione dell'uscita, oppure {@code null} se non presente
     */
    public Position getExitPosition() {
        return exitPosition;
    }

    /**
     * Restituisce la coordinata in cui è posizionato il Boss finale della zona.
     *
     * @return La posizione del Boss, oppure {@code null} se non presente
     */
    public Position getBossPosition() {
        return bossPosition;
    }

    /**
     * Restituisce la mappa di tutti i nemici correntemente vivi sulla mappa associati alle loro coordinate.
     *
     * @return La mappa delle posizioni e dei nemici
     */
    public Map<Position, Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Restituisce la larghezza della mappa espressa in numero di tile.
     *
     * @return Il numero di colonne della griglia
     */
    public int getWidth() {
        return width;
    }

    /**
     * Restituisce l'altezza della mappa espressa in numero di tile.
     *
     * @return Il numero di righe della griglia
     */
    public int getHeight() {
        return height;
    }

    /**
     * Restituisce il tipo di cella ({@link TileType}) alle coordinate (x, y) indicate.
     *
     * @param x Coordinata orizzontale
     * @param y Coordinata verticale
     * @return Il tipo di tile della cella, o {@link TileType#OBSTACLE} se fuori limiti
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
     * @return L'enum {@link MapType} della mappa
     */
    public MapType getMapType() {
        return mapType;
    }
}
