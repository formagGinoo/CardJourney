package it.unicam.cs.mpgc.rpg130809.model.shop;

import it.unicam.cs.mpgc.rpg130809.model.card.CardType;

/**
 * Factory dedicata alla creazione del catalogo dello shop.
 * <p>
 * Produce uno {@link ShopManager} contenente un {@link CardShopItem} per ogni
 * {@link CardType} e una {@link HealthPotionShopItem}. Il catalogo viene creato
 * dalla {@code ShopViewController} a ogni apertura dello shop.
 * </p>
 */
public final class ShopFactory {

    /**
     * Costruttore privato: la classe espone solo metodi statici.
     */
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
