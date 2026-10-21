package it.unicam.cs.mpgc.rpg130809.model.entity.enemy;

/**
 * Definisce i tipi di nemici presenti nel gioco con i rispettivi valori base.
 * <p>
 * Ogni voce raccoglie nome, salute, danno per turno e oro rilasciato. I tipi
 * sono associati alle zone tramite
 * {@link it.unicam.cs.mpgc.rpg130809.model.map.MapType} e vengono istanziati da
 * {@link EnemyFactory}.
 * </p>
 */
public enum EnemyType {

    /** Slime, il nemico più debole. */
    SLIME("Slime", 15, 3, 10),

    /** Goblin. */
    GOBLIN("Goblin", 30, 5, 20),

    /** Bandito, boss della zona {@code PATH}. */
    BANDIT("Bandito", 45, 7, 35),

    /** Assassino. */
    ASSASSIN("Assassino", 40, 18, 50),

    /** Orco. */
    ORC("Orco", 60, 10, 65),

    /** Golem. */
    GOLEM("Golem", 120, 6, 80),

    /** Troll, boss della zona {@code FOREST}. */
    TROLL("Troll", 100, 15, 100),

    /** Vampiro. */
    VAMPIRE("Vampiro", 150, 12, 150),

    /** Drago, boss della zona {@code DRAGON_PEAK}. */
    DRAGON("Drago", 250, 25, 300);

    private final String enemyName;
    private final int health;
    private final int damage;
    private final int goldDrop;

    /**
     * Crea un tipo di nemico con i suoi valori di gioco.
     *
     * @param enemyName il nome mostrato all'utente
     * @param health la salute massima
     * @param damage il danno inflitto per turno
     * @param goldDrop l'oro rilasciato alla sconfitta
     */
    EnemyType(String enemyName, int health, int damage, int goldDrop) {
        this.enemyName = enemyName;
        this.health = health;
        this.damage = damage;
        this.goldDrop = goldDrop;
    }

    /**
     * Restituisce il nome del nemico.
     *
     * @return il nome in italiano
     */
    public String getEnemyName() {
        return enemyName;
    }

    /**
     * Restituisce la salute massima del nemico.
     *
     * @return la salute massima
     */
    public int getHealth() {
        return health;
    }

    /**
     * Restituisce il danno inflitto dal nemico per turno.
     *
     * @return il danno per turno
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Restituisce l'oro rilasciato dal nemico alla sconfitta.
     *
     * @return l'oro rilasciato
     */
    public int getGoldDrop() {
        return goldDrop;
    }
}
