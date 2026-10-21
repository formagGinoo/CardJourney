package it.unicam.cs.mpgc.rpg130809.model.deck;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rappresenta un mazzo di carte nel gioco.
 * <p>
 * La stessa classe viene usata per il <b>Main Deck</b> persistente del
 * {@link it.unicam.cs.mpgc.rpg130809.model.entity.Player}, per la pila di pesca
 * e per la pila degli scarti del
 * {@link it.unicam.cs.mpgc.rpg130809.model.battle.BattleManager}. Le carte sono
 * contenute in una lista interna, modificabile solo tramite i metodi esposti.
 * </p>
 *
 * @see Card
 * @see DeckFactory
 */
public class Deck {

    private final List<Card> cards;

    /**
     * Costruisce un mazzo vuoto.
     * <p>
     * Utile per creare il mazzo iniziale prima di popolarlo tramite la
     * {@link it.unicam.cs.mpgc.rpg130809.model.card.CardFactory}, oppure per
     * inizializzare una pila degli scarti.
     * </p>
     */
    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Costruisce un mazzo a partire da una lista di carte esistenti.
     * <p>
     * La lista viene copiata: è il costruttore ideale per creare il mazzo di
     * combattimento a partire dal Main Deck, senza intaccare il mazzo originale
     * quando le carte vengono pescate.
     * </p>
     *
     * @param cards la lista di carte da cui inizializzare il mazzo
     */
    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    /**
     * Aggiunge una carta alla fine del mazzo.
     * <p>
     * Da usare quando si comprano carte allo shop o si rimescolano gli scarti
     * nella pila di pesca.
     * </p>
     *
     * @param card la carta da aggiungere
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Mescola le carte presenti nel mazzo in ordine casuale.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Pesca l'ultima carta disponibile, rimuovendola dal mazzo.
     *
     * @return la carta pescata, oppure {@code null} se il mazzo è vuoto
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.removeLast();
    }

    /**
     * Restituisce il numero di carte attualmente presenti nel mazzo.
     *
     * @return la dimensione del mazzo
     */
    public int size() {
        return cards.size();
    }

    /**
     * Restituisce una copia della lista delle carte presenti nel mazzo.
     * <p>
     * La copia impedisce al chiamante di modificare direttamente lo stato
     * interno del mazzo.
     * </p>
     *
     * @return una nuova lista contenente le carte
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Svuota completamente il mazzo, rimuovendo tutte le carte presenti.
     * <p>
     * Utile in battaglia per svuotare la pila degli scarti in un colpo solo.
     * </p>
     */
    public void clear() {
        cards.clear();
    }
}
