package it.unicam.cs.mpgc.rpg130809.model.map;

import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.EnemyType;

import java.util.List;
import java.util.Objects;

/**
 * Rappresenta le diverse mappe esplorabili nel gioco.
 * <p>
 * A ogni mappa sono associati un livello di difficoltà {@code level}, un nome
 * {@code name}, un insieme di nemici comuni affrontabili {@code enemies} e un
 * boss finale {@code boss}, che deve essere sconfitto per sbloccare la zona
 * successiva.
 * </p>
 * <p>
 * I valori di questa enumerazione sono utilizzati da {@link MapFactory} per
 * costruire la {@link GameMap}, da
 * {@link it.unicam.cs.mpgc.rpg130809.model.game.GameManager} per caricare la
 * zona corrente e dalla {@code MapViewController} per mostrare nome e sfondo.
 * </p>
 */
public enum MapType {

    /** Prima zona: Sentiero Scosceso. */
    PATH("Sentiero Scosceso", 1, List.of(EnemyType.SLIME, EnemyType.GOBLIN), EnemyType.BANDIT),

    /** Seconda zona: Boschetto Bisunto. */
    FOREST("Boschetto Bisunto", 2, List.of(EnemyType.ORC, EnemyType.GOLEM, EnemyType.ASSASSIN), EnemyType.TROLL),

    /** Terza zona: Picco del Drago. */
    DRAGON_PEAK("Picco del Drago", 3, List.of(EnemyType.VAMPIRE), EnemyType.DRAGON);

    private final String name;
    private final int level;
    private final List<EnemyType> enemies;
    private final EnemyType boss;

    /**
     * Costruisce una zona con le sue caratteristiche di gioco.
     *
     * @param name il nome della zona
     * @param tier il livello di difficoltà
     * @param enemies la lista di nemici presenti nella zona
     * @param boss il boss della zona, da sconfiggere per completarla
     */
    MapType(String name, int tier, List<EnemyType> enemies, EnemyType boss) {
        this.name = name;
        this.level = tier;
        this.enemies = enemies;
        this.boss = boss;
    }

    /**
     * Restituisce il nome della zona.
     *
     * @return il nome della zona
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce il livello di difficoltà della zona.
     *
     * @return il numero del livello
     */
    public int getLevel() {
        return level;
    }

    /**
     * Restituisce l'elenco dei nemici comuni presenti nella zona.
     *
     * @return la lista immutabile dei tipi di nemici comuni
     */
    public List<EnemyType> getEnemies() {
        return enemies;
    }

    /**
     * Restituisce il boss finale della zona.
     *
     * @return il tipo di nemico che funge da boss
     */
    public EnemyType getBoss() {
        return boss;
    }

    /**
     * Restituisce il percorso dell'immagine di sfondo della mappa.
     *
     * @return il percorso dell'immagine della mappa
     * @throws NullPointerException if the background image resource is missing
     */
    public String getMapBackgroundPath() {
        return Objects.requireNonNull(getClass().getResource("/img/mapview/map" + level + ".jpg")).toExternalForm();
    }

    /**
     * Restituisce il percorso del brano di sottofondo della mappa.
     * <p>
     * Ogni zona ha la propria traccia, identificata dal livello della mappa all'interno
     * della cartella dei suoni, in analogia allo sfondo di {@link #getMapBackgroundPath()}.
     * </p>
     *
     * @return il percorso del file audio della zona
     */
    public String getSoundtrackPath() {
        return "/sounds/map" + level + ".mp3";
    }

    /**
     * Restituisce il {@link MapType} corrispondente al livello specificato.
     *
     * @param level il livello della zona
     * @return la mappa associata al livello, oppure {@link #PATH} come valore predefinito
     */
    public static MapType fromLevel(int level) {
        for (MapType zone : values()) {
            if (zone.getLevel() == level) {
                return zone;
            }
        }
        return PATH;
    }
}
