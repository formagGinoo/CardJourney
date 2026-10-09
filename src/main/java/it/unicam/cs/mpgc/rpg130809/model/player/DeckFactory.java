package it.unicam.cs.mpgc.rpg130809.model.player;

import it.unicam.cs.mpgc.rpg130809.model.card.CardFactory;
import it.unicam.cs.mpgc.rpg130809.model.card.CardType;

/**
 * Factory dedicata alla creazione di mazzi di carte (Deck).
 */
public class DeckFactory {

    /**
     * Genera il mazzo base (Starter Deck) con cui il player inizia la partita.
     * <p>
     * Il mazzo base è volutamente debole e costringerà il giocatore a comprare
     * carte migliori nello shop.
     * <p>
     * Composizione (10 carte):
     * <br> - 4x TWO
     * <br> - 4x THREE
     * <br> - 2x FOUR
     * 
     * @return Un nuovo oggetto Deck contenente le carte iniziali.
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
}
