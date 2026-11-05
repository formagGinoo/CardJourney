package it.unicam.cs.mpgc.rpg130809.model.deck;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;
import it.unicam.cs.mpgc.rpg130809.model.card.CardFactory;
import it.unicam.cs.mpgc.rpg130809.model.card.CardType;

import java.util.List;

/**
 * Factory dedicata alla creazione dei mazzi di carte ({@link Deck}).
 * <p>
 * Fornisce il mazzo iniziale del giocatore, usato da
 * {@link it.unicam.cs.mpgc.rpg130809.model.game.GameManager#startNewGame()} per
 * creare il {@link it.unicam.cs.mpgc.rpg130809.model.entity.Player}.
 * </p>
 *
 * @see Deck
 * @see CardFactory
 */
public final class DeckFactory {

    /**
     * Costruttore privato: la classe espone solo metodi statici.
     */
    private DeckFactory() {
    }

    /**
     * Genera il mazzo base (Starter Deck) con cui il giocatore inizia la partita.
     * <p>
     * Il mazzo base è volutamente debole e costringerà il giocatore a comprare
     * carte migliori nello shop. Composizione (10 carte):
     * <br> - 4x {@link CardType#TWO}
     * <br> - 4x {@link CardType#THREE}
     * <br> - 2x {@link CardType#FOUR}
     * </p>
     *
     * @return un nuovo {@link Deck} contenente le carte iniziali
     */
    public static Deck createStarterDeck() {
        Deck starter = new Deck();

        for(int i = 0; i < 4; i++) {
            starter.addCard(CardFactory.createCard(CardType.TWO));
        }
        for(int i = 0; i < 4; i++) {
            starter.addCard(CardFactory.createCard(CardType.THREE));
        }
        for(int i = 0; i < 2; i++) {
            starter.addCard(CardFactory.createCard(CardType.FOUR));
        }

        return starter;
    }

    /**
     * Crea un mazzo a partire da una lista di carte esistenti.
     * <p>
     * Utilizzato in particolare dal sistema di persistenza per ricostruire il
     * mazzo principale del giocatore da un salvataggio, preservando l'ordine
     * delle carte. La lista viene copiata dal costruttore di {@link Deck}.
     * </p>
     *
     * @param cards la lista di carte da cui creare il mazzo
     * @return un nuovo {@link Deck} contenente le carte indicate, nello stesso ordine
     * @throws IllegalArgumentException if the card list is null
     */
    public static Deck createDeck(List<Card> cards) {
        if (cards == null) {
            throw new IllegalArgumentException("Card list cannot be null");
        }
        return new Deck(cards);
    }
}
