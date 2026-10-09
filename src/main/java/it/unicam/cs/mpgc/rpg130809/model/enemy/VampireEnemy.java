package it.unicam.cs.mpgc.rpg130809.model.enemy;

import it.unicam.cs.mpgc.rpg130809.model.player.Player;

public class VampireEnemy extends Enemy {
    private static final EnemyType type = EnemyType.VAMPIRE;
    private static final String name = "Vampiro";

    public VampireEnemy() {
        super(type, name);
    }

    @Override
    public void takeTurn(Player player) {
        int damage = this.getType().getDamage();
        player.takeDamage(damage);
        this.setHealth(this.getHealth() + damage); //si cura per la quantità di danno inflitto al giocatore
    }
}
