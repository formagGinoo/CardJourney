package it.unicam.cs.mpgc.rpg130809.model.enemy;

import it.unicam.cs.mpgc.rpg130809.model.player.Player;

public class OrcEnemy extends Enemy {
    private static final EnemyType type = EnemyType.ORC;
    private static final String name = "Orco";

    public OrcEnemy() {
        super(type, name);
    }

    @Override
    public void takeTurn(Player player) {
        player.takeDamage(this.getType().getDamage());
    }
}
