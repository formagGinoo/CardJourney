package it.unicam.cs.mpgc.rpg130809.model.enemy;

import it.unicam.cs.mpgc.rpg130809.model.player.Player;

public class GolemEnemy extends Enemy {
    private static final EnemyType type = EnemyType.GOLEM;
    private static final String name = "Golem";

    public GolemEnemy() {
        super(type, name);
    }

    @Override
    public void takeTurn(Player player) {
        player.takeDamage(this.getType().getDamage());
    }
}
