package it.unicam.cs.mpgc.rpg130809.model.enemy;

/**
 * Definisce i vari tipi di nemici presenti nel gioco, 
 * con i rispettivi valori base di Salute (HP) e Danno.
 */
public enum EnemyType {
    SLIME(15, 3),
    GOBLIN(30, 5),
    BANDIT(45, 7),
    ASSASSIN(40, 18),
    ORC(60, 10),
    GOLEM(120, 6),
    TROLL(100, 15),
    LICH(150, 12),
    DRAGON(250, 25);

    private final int health;
    private final int damage;

    EnemyType(int health, int damage) {
        this.health = health;
        this.damage = damage;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }
}
