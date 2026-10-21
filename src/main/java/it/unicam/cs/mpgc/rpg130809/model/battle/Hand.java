package it.unicam.cs.mpgc.rpg130809.model.battle;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;
import it.unicam.cs.mpgc.rpg130809.model.card.CardType;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta la mano del giocatore durante una battaglia.
 * <p>
 * La mano contiene al massimo cinque carte, pescate dalla pila di
 * pesca e spostate nella pila degli scarti al termine del turno. È posseduta dal
 * {@link BattleManager}, che ne gestisce il ciclo di vita, e viene interrogata
 * dalla {@code BattleViewController} per mostrare le carte e le anteprime delle
 * combo.
 * </p>
 *
 * @see Card
 * @see BattleManager
 */
public class Hand {

    private final List<Card> cards;
    private final int maxHandSize = 5;

    /**
     * Costruisce una nuova mano con le carte specificate.
     *
     * @param cards le carte iniziali della mano
     */
    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Valuta le carte selezionate dal giocatore e calcola il danno totale della combo.
     * <p>
     * Regole delle combo:
     * <br> - 1 carta: danno base della carta.
     * <br> - 2+ carte uguali (coppia, tris, poker): somma dei danni moltiplicata per 1.5, 2.0, 2.5, 3.0.
     * <br> - Scala (almeno 3 carte consecutive): somma dei danni moltiplicata per 1.5 (se 3 carte), 2.0 (se 4), 3.0 (se 5).
     * <br> - Combinazione non valida: restituisce 0.
     * </p>
     *
     * @param selectedCards le carte selezionate dal giocatore nella UI per l'attacco
     * @return i danni totali calcolati, oppure 0 se la combinazione non è valida
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
     * <p>
     * A ogni turno di battaglia la mano viene ricostruita, quindi il contenuto
     * precedente viene rimosso prima della nuova pescata.
     * </p>
     */
    public void clearHand() {
        cards.clear();
    }

    /**
     * Aggiunge una carta alla mano del giocatore.
     *
     * @param card la carta da aggiungere
     * @throws IllegalStateException if the hand is already full
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
     * @param card la carta da rimuovere
     */
    public void removeCard(Card card) {
        cards.remove(card);
    }

    /**
     * Restituisce le carte attualmente presenti nella mano.
     *
     * @return la lista delle carte in mano
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Restituisce la dimensione massima della mano.
     *
     * @return il numero massimo di carte che la mano può contenere
     */
    public int getMaxHandSize() {
        return maxHandSize;
    }

    /**
     * Restituisce il numero di carte attualmente in mano.
     *
     * @return la dimensione attuale della mano
     */
    public int getHandSize() {
        return cards.size();
    }
}
