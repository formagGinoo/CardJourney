package it.unicam.cs.mpgc.rpg130809.model.shop;

import it.unicam.cs.mpgc.rpg130809.model.card.CardType;

/**
 * Factory dedicata alla creazione del catalogo dello shop.
 */
public final class ShopFactory {

    private ShopFactory() {
    }

    /**
     * Crea lo shop base con tutte le carte e la pozione di salute.
     *
     * @return uno shop pronto all'uso
     */
    public static ShopManager createShop() {
        ShopManager shop = new ShopManager();

        for (CardType cardType : CardType.values()) {
            shop.addItem(new CardShopItem(cardType));
        }
        shop.addItem(new HealthPotionShopItem());

        return shop;
    }
}
