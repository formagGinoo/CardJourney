package it.unicam.cs.mpgc.rpg130809.model.card;

import java.util.List;

/**
 * Enumerazione dei tipi di carta disponibili nel gioco.
 * <p>
 * Ogni voce definisce un nome, un danno base e un costo nello shop.
 * L'ordine delle costanti è significativo: dalla più forte ({@link #JOKER}) alla
 * più debole ({@link #TWO}). La {@link it.unicam.cs.mpgc.rpg130809.model.battle.Hand}
 * sfrutta questo ordine per riconoscere le scale nelle combo in {@link it.unicam.cs.mpgc.rpg130809.model.battle.Hand#evaluateHand(List)}.
 * </p>
 */
public enum CardType {

    /** Jolly, la carta più forte. */
    JOKER("Jolly", 30, 150),

    /** Asso. */
    ACE("Asso", 20, 120),

    /** Re. */
    KING("Re", 16, 110),

    /** Regina. */
    QUEEN("Regina", 14, 95),

    /** Jack. */
    JACK("Jack", 12, 75),

    /** Dieci. */
    TEN("Dieci", 10, 65),

    /** Nove. */
    NINE("Nove", 9, 50),

    /** Otto. */
    EIGHT("Otto", 8, 40),

    /** Sette. */
    SEVEN("Sette", 7, 35),

    /** Sei. */
    SIX("Sei", 6, 30),

    /** Cinque. */
    FIVE("Cinque", 5, 25),

    /** Quattro. */
    FOUR("Quattro", 4, 20),

    /** Tre. */
    THREE("Tre", 3, 15),

    /** Due, la carta più debole. */
    TWO("Due", 2, 10);

    private final String cardName;
    private final int damage;
    private final int shopCost;

    /**
     * Crea un tipo di carta con i suoi valori di gioco.
     *
     * @param cardName il nome mostrato all'utente
     * @param damage il danno base
     * @param shopCost il costo nello shop
     */
    CardType(String cardName, int damage, int shopCost) {
        this.cardName = cardName;
        this.damage = damage;
        this.shopCost = shopCost;
    }

    /**
     * Restituisce il nome della carta.
     *
     * @return il nome in italiano
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Restituisce il danno base della carta.
     *
     * @return il danno base
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Restituisce il costo della carta nello shop.
     *
     * @return il costo in oro
     */
    public int getShopCost() {
        return shopCost;
    }
}
