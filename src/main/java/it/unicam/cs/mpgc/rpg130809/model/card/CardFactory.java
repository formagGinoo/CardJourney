package it.unicam.cs.mpgc.rpg130809.model.card;

/**
 * Factory per la creazione delle carte del gioco.
 * Utilizzato per lo shop, per le ricompense e per generare i mazzi.
 */
public class CardFactory {
    
    /**
     * Crea un'istanza della carta corrispondente al tipo specificato.
     * 
     * @param type Il tipo di carta da creare
     * @throws IllegalArgumentException se il tipo di carta è null
     * @return Una nuova istanza della carta richiesta.
     */
    public static Card createCard(CardType type) {
        if (type == null) {
            throw new IllegalArgumentException("CardType cannot be null");
        }
        return new Card(type);
    }
}
