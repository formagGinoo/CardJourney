package it.unicam.cs.mpgc.rpg130809.model.shop;

import it.unicam.cs.mpgc.rpg130809.model.card.CardFactory;
import it.unicam.cs.mpgc.rpg130809.model.card.CardType;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;

/**
 * Adatta una {@link CardType} al contratto {@link ShopItem}.
 * <p>
 * La carta in sé non conosce lo shop: è questo adapter a esporre nome, prezzo
 * e comportamento di acquisto. A ogni acquisto viene creata una nuova istanza
 * di {@link it.unicam.cs.mpgc.rpg130809.model.card.Card}, così lo stesso
 * articolo può essere comprato più volte senza condividere la stessa carta.
 * </p>
 */
public class CardShopItem implements ShopItem {

    private final CardType cardType;
    private final int price;

    /**
     * Crea un articolo usando il costo base definito in {@link CardType}.
     *
     * @param cardType il tipo di carta venduto
     */
    public CardShopItem(CardType cardType) {
        if (cardType == null) {
            throw new IllegalArgumentException("CardType cannot be null");
        }
        this.cardType = cardType;
        this.price = cardType.getShopCost();
    }

    @Override
    public String getName() {
        return cardType.getCardName();
    }

    @Override
    public String getDescription() {
        return "Carta - " + cardType.getDamage() + " danni";
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void applyTo(Player player) {
        player.addCardToMainDeck(CardFactory.createCard(cardType));
    }

    /**
     * @return il tipo di carta venduto da questo articolo
     */
    public CardType getCardType() {
        return cardType;
    }
}
