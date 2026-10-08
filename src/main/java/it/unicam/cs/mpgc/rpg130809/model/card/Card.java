package it.unicam.cs.mpgc.rpg130809.model.card;

import it.unicam.cs.mpgc.rpg130809.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.player.Player;

import java.util.UUID;

/**
 * Rappresenta una carta del gioco.
 * Ogni carta ha un ID univoco, un tipo e un nome. Danno e costo sono invariabili.
 */
public abstract class Card {
    private final String id;
    private final CardType type;
    private final String name;

    /**
     * Costruisce una nuova carta
     *
     * @param type il tipo della carta
     * @param name il nome della carta
     */
    public Card(CardType type, String name) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.name = name;
    }

    /**
     * Restituisce l'id univoco della carta.
     *
     * @return l'id univoco della carta
     */
    public String getId() {
        return id;
    }

    /**
     * Restituisce il tipo della carta.
     *
     * @return il tipo della carta
     */
    public CardType getType() {
        return type;
    }

    /**
     * Restituisce il nome della carta.
     *
     * @return il nome della carta
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce il costo nello shop della carta (fisso, preso dal tipo).
     *
     * @return il costo nello shop
     */
    public int getShopCost() {
        return type.getShopCost();
    }

    /**
     * Restituisce il danno di base della carta (fisso, preso dal tipo).
     *
     * @return il danno della carta
     */
    public int getDamage() {
        return type.getDamage();
    }

    /**
     * Gioca la carta.
     *
     * @param player il giocatore
     * @param enemy il nemico
     */
    public abstract void play(Player player, Enemy enemy);
}
