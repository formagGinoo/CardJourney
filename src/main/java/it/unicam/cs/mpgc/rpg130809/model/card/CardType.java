package it.unicam.cs.mpgc.rpg130809.model.card;

/**
 * Enum che rappresenta i diversi tipi di carte nel gioco.
 * Ogni tipo di carta ha associati un danno base e un costo nello shop.
 */
public enum CardType {
    JOKER(30, 300),
    ACE(20, 200),
    KING(16, 140),
    QUEEN(14, 110),
    JACK(12, 75),
    TEN(10, 65),
    NINE(9, 50),
    EIGHT(8, 40),
    SEVEN(7, 35),
    SIX(6, 30),
    FIVE(5, 25),
    FOUR(4, 20),
    THREE(3, 15),
    TWO(2, 10);

    private final int damage;
    private final int shopCost;

    CardType(int damage, int shopCost) {
        this.damage = damage;
        this.shopCost = shopCost;
    }

    public int getDamage() {
        return damage;
    }

    public int getShopCost() {
        return shopCost;
    }
}
