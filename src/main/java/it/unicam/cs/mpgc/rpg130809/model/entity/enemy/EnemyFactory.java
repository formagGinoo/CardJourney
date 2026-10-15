package it.unicam.cs.mpgc.rpg130809.model.entity.enemy;

public class EnemyFactory {
    public static Enemy createEnemy(EnemyType type) {
        if (type == null) {
            throw new IllegalArgumentException("Enemy type is required");
        }
        return new Enemy(type);
    }
}
