package it.unicam.cs.mpgc.rpg130809.model.entity.enemy;

/**
 * Definisce i vari tipi di nemici presenti nel gioco, 
 * con i rispettivi valori base di Salute e Danno.
 */
public enum EnemyType {
    SLIME("Slime", 15, 3, 10),
    GOBLIN("Goblin", 30, 5, 20),
    BANDIT("Bandito", 45, 7, 35),
    ASSASSIN("Assassino", 40, 18, 50),
    ORC("Orco", 60, 10, 65),
    GOLEM("Golem", 120, 6, 80),
    TROLL("Troll", 100, 15, 100),
    VAMPIRE("Vampiro", 150, 12, 150),
    DRAGON("Drago", 250, 25, 300);

    private final String enemyName;
    private final int health;
    private final int damage;
    private final int goldDrop;

    EnemyType(String enemyName, int health, int damage, int goldDrop) {
        this.enemyName = enemyName;
        this.health = health;
        this.damage = damage;
        this.goldDrop = goldDrop;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public int getGoldDrop() {
        return goldDrop;
    }
}
