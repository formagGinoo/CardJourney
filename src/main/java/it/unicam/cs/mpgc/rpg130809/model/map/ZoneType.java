package it.unicam.cs.mpgc.rpg130809.model.map;

import it.unicam.cs.mpgc.rpg130809.model.enemy.EnemyType;

import java.util.List;

/**
 * Rappresenta le diverse zone o regioni esplorabili nella mappa di gioco.
 * <p>
 * Ogni zona è associata a un livello di difficoltà progressivo (Tier),
 * a un insieme di nemici comuni affrontabili casualmente e a un boss finale
 * che deve essere sconfitto per sbloccare la zona successiva.
 * </p>
 */
public enum ZoneType {

    /**
     * Prima zona di gioco: adatta al mazzo iniziale del giocatore.
     * Popolata da mostri deboli (Slime e Goblin) con il Bandito come boss.
     */
    PATH("Sentiero Scosceso", 1, List.of(EnemyType.SLIME, EnemyType.GOBLIN), EnemyType.BANDIT),

    /**
     * Seconda zona di gioco (Mid-Game): richiede combo e carte migliorate.
     * Popolata da Orchi, Golem e Assassini, con il Troll come boss.
     */
    FOREST("Boschetto Bisunto", 2, List.of(EnemyType.ORC, EnemyType.GOLEM, EnemyType.ASSASSIN), EnemyType.TROLL),

    /**
     * Terza zona di gioco (Late-Game): sfida finale del gioco.
     * Popolata da Vampiri élite con il Drago come boss finale.
     */
    DRAGON_PEAK("Picco del Drago", 3, List.of(EnemyType.VAMPIRE), EnemyType.DRAGON);

    private final String name;
    private final int tier;
    private final List<EnemyType> enemies;
    private final EnemyType boss;

    /**
     * Costruisce una zona con le sue caratteristiche di gioco.
     *
     * @param name    Il nome visibile della zona
     * @param tier    Il livello numerico di progressione (1, 2 o 3)
     * @param enemies La lista di nemici comuni che possono apparire nella zona
     * @param boss    Il boss a guardia della fine della zona
     */
    ZoneType(String name, int tier, List<EnemyType> enemies, EnemyType boss) {
        this.name = name;
        this.tier = tier;
        this.enemies = enemies;
        this.boss = boss;
    }

    /**
     * Ottiene il nome descrittivo della zona.
     *
     * @return Il nome della zona
     */
    public String getName() {
        return name;
    }

    /**
     * Ottiene il livello di difficoltà (Tier) della zona.
     *
     * @return Il numero del tier
     */
    public int getTier() {
        return tier;
    }

    /**
     * Ottiene l'elenco dei nemici comuni presenti nella zona.
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
}