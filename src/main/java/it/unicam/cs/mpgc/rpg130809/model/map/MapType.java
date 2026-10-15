package it.unicam.cs.mpgc.rpg130809.model.map;

import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.EnemyType;

import java.util.List;
import java.util.Objects;

/**
 * Rappresenta le diverse mappe esplorabili nel gioco.
 * <p>
 * A ogni mappa è associato un livello di difficoltà {@code level}, un nome {@code name},
 * e un insieme di nemici comuni affrontabili {@code enemies} e un boss finale {@code boss}
 * che deve essere sconfitto per sbloccare la zona successiva.
 * </p>
 */
public enum MapType {

    PATH("Sentiero Scosceso", 1, List.of(EnemyType.SLIME, EnemyType.GOBLIN), EnemyType.BANDIT),
    FOREST("Boschetto Bisunto", 2, List.of(EnemyType.ORC, EnemyType.GOLEM, EnemyType.ASSASSIN), EnemyType.TROLL),
    DRAGON_PEAK("Picco del Drago", 3, List.of(EnemyType.VAMPIRE), EnemyType.DRAGON);

    private final String name;
    private final int level;
    private final List<EnemyType> enemies;
    private final EnemyType boss;

    /**
     * Costruisce una zona con le sue caratteristiche di gioco.
     *
     * @param name    Il nome della zona
     * @param tier    Il livello di difficoltà
     * @param enemies La lista di nemici che ci sono nella zona
     * @param boss    Il boss della zona, da sconfiggere per completarla
     */
    MapType(String name, int tier, List<EnemyType> enemies, EnemyType boss) {
        this.name = name;
        this.level = tier;
        this.enemies = enemies;
        this.boss = boss;
    }

    /**
     * Ottiene il nome della zona.
     *
     * @return Il nome della zona
     */
    public String getName() {
        return name;
    }

    /**
     * Ottiene il livello di difficoltà della zona.
     *
     * @return Il numero del level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Ottiene l'elenco dei nemici presenti nella zona.
     *
     * @return La lista immutabile dei tipi di nemici comuni
     */
    public List<EnemyType> getEnemies() {
        return enemies;
    }

    /**
     * Ottiene il boss finale della zona.
     *
     * @return Il tipo di nemico che funge da boss
     */
    public EnemyType getBoss() {
        return boss;
    }

    /**
     * Ottiene il percorso dell'immagine di sfondo della mappa.
     *
     * @return Il percorso dell'immagine della mappa
     */
    public String getMapBackgroundPath() {
        return Objects.requireNonNull(getClass().getResource("/img/mapview/map" + level + ".jpg")).toExternalForm();
    }

    /**
     * Restituisce il MapType corrispondente al level specificato.
     *
     * @param level Il livello della zona
     * @return La mappa associata al level, oppure PATH come default
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