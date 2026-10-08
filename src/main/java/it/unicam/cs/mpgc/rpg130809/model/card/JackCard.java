package it.unicam.cs.mpgc.rpg130809.model.card;

import it.unicam.cs.mpgc.rpg130809.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.player.Player;

/**
 * Rappresenta la carta JackCard nel gioco.
 * Eredita da Card e ne definisce il comportamento specifico tramite il metodo play.
 */
public class JackCard extends Card {
    private final static CardType type = CardType.JACK;
    private final static String name = "Jack";

    public JackCard() {
        super(type, name);
    }

    @Override
    public void play(Player player, Enemy enemy) {
        //TODO: Implement the play method for JackCard
    }
}
