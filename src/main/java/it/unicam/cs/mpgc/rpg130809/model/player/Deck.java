package it.unicam.cs.mpgc.rpg130809.model.player;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rappresenta un mazzo di carte nel gioco.
 * Può essere utilizzato sia per rappresentare il 'Main Deck' (il mazzo persistente del giocatore)
 * sia il 'Combat Deck' (il mazzo da cui il giocatore pesca durante un singolo combattimento).
 */
public class Deck {

    private final List<Card> cards;

    /**
     * Costruisce un mazzo vuoto.
     * Utile per creare il mazzo iniziale prima di popolarlo tramite la CardFactory,
     * oppure per inizializzare una pila degli scarti (Discard Pile).
     */
    public Deck() {
        this.cards = new ArrayList<>();
    }
    
    /**
     * Costruisce un mazzo a partire da una lista di carte esistenti.
     * È il costruttore ideale per creare il mazzo di combattimento copiando le carte del Main Deck
     * all'inizio di ogni battaglia, per non intaccare il mazzo originale quando le carte vengono pescate.
     * 
     * @param masterCards la lista di carte da cui inizializzare il mazzo
     */
    public Deck(List<Card> masterCards) {
        this.cards = new ArrayList<>(masterCards);
    }

    /**
     * Aggiunge una carta alla fine del mazzo.
     * Da usare quando si comprano carte allo shop o si mischiano gli scarti nel mazzo di pesca.
     * 
     * @param card la carta da aggiungere
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Mescola le carte presenti nel mazzo in ordine casuale.
     * Indispensabile prima di iniziare a pescare in un combattimento.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Pesca la prima carta disponibile.
     * Rimuove fisicamente la carta dal mazzo.
     * 
     * @return la carta pescata, oppure null se il mazzo è vuoto.
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
     * @return la grandezza del mazzo
     */
    public int size() { 
        return cards.size(); 
    }

    /**
     * Restituisce una copia della lista delle carte presenti nel mazzo.
     * 
     * @return una nuova lista contenente le carte
     */
    /**
     * Restituisce una copia della lista delle carte presenti nel mazzo.
     * 
     * @return una nuova lista contenente le carte
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Svuota completamente il mazzo, rimuovendo tutte le carte presenti.
     * Utile in battaglia per svuotare la pila degli scarti in un colpo solo.
     */
    public void clear() {
        cards.clear();
    }
}
