package it.unicam.cs.mpgc.rpg130809.model.enemy;

import it.unicam.cs.mpgc.rpg130809.model.player.Player;

public class GoblinEnemy extends Enemy {
    private static final EnemyType type = EnemyType.GOBLIN;
    private static final String name = "Goblin";

    public GoblinEnemy() {
        super(type, name);
    }

    @Override
    public void takeTurn(Player player) {
        player.takeDamage(this.getType().getDamage());
    }
}
