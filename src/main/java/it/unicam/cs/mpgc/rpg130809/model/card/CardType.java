package it.unicam.cs.mpgc.rpg130809.model.card;

/**
 * Enum che rappresenta le informazioni sulle carte nel gioco.
 * Ogni carta ha un nome in italiano, un danno base e un costo nello shop.
 */
public enum CardType {
    JOKER("Jolly", 30, 300),
    ACE("Asso", 20, 200),
    KING("Re", 16, 140),
    QUEEN("Regina", 14, 110),
    JACK("Jack", 12, 75),
    TEN("Dieci", 10, 65),
    NINE("Nove", 9, 50),
    EIGHT("Otto", 8, 40),
    SEVEN("Sette", 7, 35),
    SIX("Sei", 6, 30),
    FIVE("Cinque", 5, 25),
    FOUR("Quattro", 4, 20),
    THREE("Tre", 3, 15),
    TWO("Due", 2, 10);

    private final String cardName;
    private final int damage;
    private final int shopCost;

    CardType(String cardName, int damage, int shopCost) {
        this.cardName = cardName;
        this.damage = damage;
        this.shopCost = shopCost;
    }

    public String getCardName() {
        return cardName;
    }

    public int getDamage() {
        return damage;
    }

    public int getShopCost() {
        return shopCost;
    }
}
