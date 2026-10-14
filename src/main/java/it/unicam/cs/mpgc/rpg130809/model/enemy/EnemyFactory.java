package it.unicam.cs.mpgc.rpg130809.model.enemy;

public class EnemyFactory {
    public static Enemy createEnemy(EnemyType type) {
        return switch (type) {
            case SLIME -> new SlimeEnemy();
            case GOBLIN -> new GoblinEnemy();
            case BANDIT -> new BanditEnemy();
            case ASSASSIN -> new AssassinEnemy();
            case ORC -> new OrcEnemy();
            case GOLEM -> new GolemEnemy();
            case TROLL -> new TrollEnemy();
            case VAMPIRE -> new VampireEnemy();
            case DRAGON -> new DragonEnemy();
        };
    }
}
