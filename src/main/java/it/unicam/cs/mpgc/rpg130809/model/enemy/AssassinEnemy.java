package it.unicam.cs.mpgc.rpg130809.model.enemy;

import it.unicam.cs.mpgc.rpg130809.model.player.Player;

public class AssassinEnemy extends Enemy {
    private static final EnemyType type = EnemyType.ASSASSIN;
    private static final String name = "Assassino";

    public AssassinEnemy() {
        super(type, name);
    }

    @Override
    public void takeTurn(Player player) {
        player.takeDamage(this.getType().getDamage());
    }
}
