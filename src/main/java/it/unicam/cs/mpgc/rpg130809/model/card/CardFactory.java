package it.unicam.cs.mpgc.rpg130809.model.card;

/**
 * Factory per la creazione delle carte del gioco.
 * <p>
 * Centralizza l'istanziazione di {@link Card}, usata dallo starter deck di
 * {@link it.unicam.cs.mpgc.rpg130809.model.deck.DeckFactory}, dallo shop e dalle
 * ricompense. In questo modo i chiamanti non dipendono direttamente dal
 * costruttore della carta.
 * </p>
 *
 * @see Card
 * @see CardType
 */
public final class CardFactory {

    /**
     * Costruttore privato: la classe espone solo metodi statici.
     */
    private CardFactory() {
    }

    /**
     * Crea un'istanza della carta corrispondente al tipo specificato.
     *
     * @param type il tipo di carta da creare
     * @return una nuova istanza della carta richiesta
     * @throws IllegalArgumentException if the card type is null
     */
    public static Card createCard(CardType type) {
        if (type == null) {
            throw new IllegalArgumentException("CardType cannot be null");
        }
        return new Card(type);
    }
}
