package it.unicam.cs.mpgc.rpg130809.model.enemy;

import it.unicam.cs.mpgc.rpg130809.model.player.Player;

public class DragonEnemy extends Enemy {
    private static final EnemyType type = EnemyType.DRAGON;
    private static final String name = "Drago";

    public DragonEnemy() {
        super(type, name);
    }

    @Override
    public void takeTurn(Player player) {
        player.takeDamage(this.getType().getDamage());
    }
}
