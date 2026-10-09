package it.unicam.cs.mpgc.rpg130809.model.enemy;

import it.unicam.cs.mpgc.rpg130809.model.player.Player;

public class BanditEnemy extends Enemy {
    private static final EnemyType type = EnemyType.BANDIT;
    private static final String name = "Bandito";

    public BanditEnemy() {
        super(type, name);
    }

    @Override
    public void takeTurn(Player player) {
        player.takeDamage(this.getType().getDamage());
    }
}
