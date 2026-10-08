package it.unicam.cs.mpgc.rpg130809.model.card;



/**
 * Factory per la creazione delle carte del gioco.
 * Utilizzato per lo shop, per le ricompense e per generare i mazzi.
 */
public class CardFactory {
    
    /**
     * Crea un'istanza della classe concreta corrispondente al tipo di carta specificato.
     * 
     * @param type Il tipo di carta da creare
     * @return Una nuova istanza della carta richiesta.
     * @throws IllegalArgumentException se il tipo non è riconosciuto.
     */
    public static Card createCard(CardType type) {
        return switch (type) {
            case TWO -> new TwoCard();
            case THREE -> new ThreeCard();
            case FOUR -> new FourCard();
            case FIVE -> new FiveCard();
            case SIX -> new SixCard();
            case SEVEN -> new SevenCard();
            case EIGHT -> new EightCard();
            case NINE -> new NineCard();
            case TEN -> new TenCard();
            case JACK -> new JackCard();
            case QUEEN -> new QueenCard();
            case KING -> new KingCard();
            case ACE -> new AceCard();
            case JOKER -> new JokerCard();
            default -> throw new IllegalArgumentException("Invalid card type: " + type);
        };
    }
}
