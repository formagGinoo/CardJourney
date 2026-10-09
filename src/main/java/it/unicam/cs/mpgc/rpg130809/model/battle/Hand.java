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

        //Carta singola: restituisce il danno base della carta.
        if (selectedCards.size() == 1) {
            return selectedCards.getFirst().getDamage();
        }

        int baseSum = selectedCards.stream().mapToInt(Card::getDamage).sum();

        //Coppia, Tris, Poker: tutte le carte devono essere dello stesso tipo.
        boolean allSame = true;
        CardType firstType = selectedCards.getFirst().getType();
        for (Card c : selectedCards) {
            if (c.getType() != firstType) {
                allSame = false;
                break;
            }
        }

        // Se sono tutte uguali, applichiamo un moltiplicatore crescente in base alla quantità.
        if (allSame) {
            // Formula: 2 carte -> 1.5x | 3 carte -> 2.0x | 4 carte -> 2.5x | 5 carte -> 3.0x
            double multiplier = 1.0 + (0.5 * (selectedCards.size() - 1));
            return (int) (baseSum * multiplier);
        }

        //Scala, ovvero almeno 3 carte consecutive in ordine decrescente di valore.
        if (selectedCards.size() >= 3) {
            List<Card> sorted = new ArrayList<>(selectedCards);
            
            // Ordiniamo usando l'ordinal() di CardType.
            // Poiché nell'enum CardType l'ordine è: JOKER(0), ACE(1), KING(2)... TWO(13),
            // l'ordinal decrescente (c2 rispetto a c1) metterà prima le carte più deboli e alla fine le più forti
            sorted.sort((c1, c2) -> Integer.compare(c2.getType().ordinal(), c1.getType().ordinal()));

            boolean isStraight = true;
            for (int i = 0; i < sorted.size() - 1; i++) {
                int currentOrdinal = sorted.get(i).getType().ordinal();
                int nextOrdinal = sorted.get(i + 1).getType().ordinal();
                
                // Affinché sia una scala perfetta, ogni carta successiva deve avere 
                // un ordinal inferiore esattamente di 1 rispetto alla precedente.
                // 
                // ESEMPIO CON IL JOKER:
                // Se la lista ordinata è KING(2), ACE(1), JOKER(0):
                // 1° Ciclo: current(2) - 1 == next(1) -> Vero!
                // 2° Ciclo: current(1) - 1 == next(0) -> Vero!
                // Il sistema riconosce nativamente il Joker come carta successiva all'Asso.
                if (currentOrdinal - 1 != nextOrdinal) {
                    isStraight = false;
                    break;
                }
            }

            if (isStraight) {
                double multiplier = switch (selectedCards.size()) {
                    case 3 -> 1.5; // Scala da 3 (danno base * 1.5)
                    case 4 -> 2.0; // Scala da 4 (danno base * 2.0)
                    case 5 -> 3.0; // Scala reale (danno base * 3.0)
                    default -> 3.0;
                };
                return (int) (baseSum * multiplier);
            }
        }
        
        // Se si arriva qui, le carte non formano né un gruppo di carte uguali né una scala.
        // La mossa non è valida, quindi restituiamo 0 danni.
        return 0; 
    }

    /**
     * Pulisce la mano del giocatore.
     * Questo perché a ogni turno di battaglia la mano viene ricostruita.
     */
    public void clearHand() {
        cards.clear();
    }

    /**
     * Aggiunge una carta alla mano del giocatore.
     *
     * @param card La carta da aggiungere
     * @throws IllegalStateException Se la mano è piena e non si può aggiungere un'altra carta
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
     * @return true se la carta è stata rimossa, false altrimenti
     */
    public boolean removeCard(Card card) {
        return cards.remove(card);
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

    /**
     * Ottiene la dimensione attuale della mano del giocatore.
     *
     * @return La dimensione attuale della mano
     */
    public int getHandSize() {
        return cards.size();
    }
}
