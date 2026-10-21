package it.unicam.cs.mpgc.rpg130809.model.shop;

import it.unicam.cs.mpgc.rpg130809.model.card.CardFactory;
import it.unicam.cs.mpgc.rpg130809.model.card.CardType;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;

/**
 * Adatta una {@link CardType} al contratto {@link ShopItem}.
 * <p>
 * La carta in sé non conosce lo shop: adotta il design pattern <b>Adapter</b> per
 * ed espone nome, prezzo e comportamento di acquisto. A ogni acquisto viene creata una nuova istanza di
 * {@link it.unicam.cs.mpgc.rpg130809.model.card.Card} tramite
 * {@link CardFactory}, così lo stesso articolo può essere comprato più volte
 * senza condividere la stessa carta.
 * </p>
 * <p>
 * Gli articoli sono raccolti da {@link ShopFactory} e venduti da
 * {@link ShopManager}.
 * </p>
 */
public class CardShopItem implements ShopItem {

    private final CardType cardType;
    private final int price;

    /**
     * Crea un articolo usando il costo base definito in {@link CardType}.
     *
     * @param cardType il tipo di carta venduto
     * @throws IllegalArgumentException if the card type is null
     */
    public CardShopItem(CardType cardType) {
        if (cardType == null) {
            throw new IllegalArgumentException("CardType cannot be null");
        }
        this.cardType = cardType;
        this.price = cardType.getShopCost();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return cardType.getCardName();
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return "Carta - " + cardType.getDamage() + " danni";
    }

    /** {@inheritDoc} */
    @Override
    public int getPrice() {
        return price;
    }

    /** {@inheritDoc} */
    @Override
    public void applyTo(Player player) {
        player.addCardToMainDeck(CardFactory.createCard(cardType));
    }

    /**
     * Restituisce il tipo di carta venduto da questo articolo.
     *
     * @return il tipo di carta
     */
    public CardType getCardType() {
        return cardType;
    }
}
