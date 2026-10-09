package it.unicam.cs.mpgc.rpg130809.model.card;

import java.util.UUID;

/**
 * Rappresenta una carta del gioco.
 * Ogni carta ha un ID univoco, un tipo e un nome. Danno e costo sono invariabili.
 */
public class Card {
    private final String id;
    private final CardType type;

    /**
     * Costruisce una nuova carta in base al suo tipo.
     * Il nome viene generato automaticamente per evitare modifiche esterne.
     *
     * @param type il tipo della carta (definisce anche il nome, il danno e il costo)
     */
    public Card(CardType type) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
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
        return type.getCardName();
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
}
