package it.unicam.cs.mpgc.rpg130809.model.battle;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;
import it.unicam.cs.mpgc.rpg130809.model.card.CardType;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta la mano del giocatore in battaglia.
 */
public class Hand {
    private final List<Card> cards;
    private final int maxHandSize = 5;

    /**
     * Costruisce una nuova mano con le carte specificate.
     *
     * @param cards Le carte della mano
     */
    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Valuta le carte selezionate dal giocatore e calcola il danno totale della combo.
     * <p>
     * Regole delle Combo:
     * <br> - 1 Carta: Danno base della carta.
     * <br> - 2+ Carte uguali (Coppia, Tris, Poker): Somma danni moltiplicata per 1.5, 2.0, 2.5 ecc.
     * <br> - Scala (almeno 3 carte consecutive): Somma danni moltiplicata per 1.5 (se 3 carte), 2.0 (se 4), 3.0 (se 5).
     * <br> - Combinazione non valida: Ritorna 0.
     * <p>
     *
     * @param selectedCards Le carte selezionate dal giocatore nella UI per l'attacco
     * @return I danni totali calcolati, oppure 0 se la combinazione non è valida.
     */
    public int evaluateHand(List<Card> selectedCards) {
        if (selectedCards == null || selectedCards.isEmpty()) {
            return 0;
        }

        if (selectedCards.size() == 1) {
            return selectedCards.getFirst().getDamage();
        }
        int baseSum = selectedCards.stream().mapToInt(Card::getDamage).sum();

        boolean allSame = true;
        CardType firstType = selectedCards.getFirst().getType();
        for (Card c : selectedCards) {
            if (c.getType() != firstType) {
                allSame = false;
                break;
            }
        }

        if (allSame) {
            // Moltiplicatore: 2 carte = 1.5x, 3 carte = 2.0x, 4 carte = 2.5x
            double multiplier = 1.0 + (0.5 * (selectedCards.size() - 1));
            return (int) (baseSum * multiplier);
        }

        if (selectedCards.size() >= 3) {
            List<Card> sorted = new ArrayList<>(selectedCards);
            sorted.sort((c1, c2) -> Integer.compare(c2.getType().ordinal(), c1.getType().ordinal()));

            boolean isStraight = true;
            for (int i = 0; i < sorted.size() - 1; i++) {
                int currentOrdinal = sorted.get(i).getType().ordinal();
                int nextOrdinal = sorted.get(i + 1).getType().ordinal();
                if (currentOrdinal - 1 != nextOrdinal) {
                    isStraight = false;
                    break;
                }
            }

            if (isStraight) {
                double multiplier = switch (selectedCards.size()) {
                    case 3 -> 1.5;
                    case 4 -> 2.0;
                    case 5 -> 3.0; // Scala reale da 5 carte: Danni triplicati!
                    default -> 3.0;
                };
                return (int) (baseSum * multiplier);
            }
        }

        return 0; 
    }

    /**
     * Pulisce la mano del giocatore.
     * Questo perché la mano viene ricostruita a ogni turno di battaglia.
     */
    public void clearHand() {
        cards.clear();
    }

    /**
     * Aggiunge una carta alla mano del giocatore.
     *
     * @param card La carta da aggiungere
     */
    public void addCard(Card card) {
        if (cards.size() < maxHandSize) {
            cards.add(card);
        } else {
            throw new IllegalStateException("Hand is full. Cannot add more cards.");
        }
    }

    /**
     * Rimuove una carta dalla mano del giocatore.
     *
     * @param card La carta da rimuovere
     */
    public void removeCard(Card card) {
        cards.remove(card);
    }

    /**
     * Ottiene la mano del giocatore.
     *
     * @return La mano del giocatore
     */
    public List<Card> getHand() {
        return cards;
    }

    /**
     * Ottiene la dimensione massima della mano del giocatore.
     *
     * @return La dimensione massima della mano
     */
    public int getMaxHandSize() {
        return maxHandSize;
    }
}
