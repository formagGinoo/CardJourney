package it.unicam.cs.mpgc.rpg130809.persistence;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;
import it.unicam.cs.mpgc.rpg130809.model.card.CardFactory;
import it.unicam.cs.mpgc.rpg130809.model.card.CardType;
import it.unicam.cs.mpgc.rpg130809.model.deck.Deck;
import it.unicam.cs.mpgc.rpg130809.model.deck.DeckFactory;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;

import java.util.List;
import java.util.Objects;

/**
 * Struttura dati che rappresenta lo <b>snapshot</b> della partita da
 * serializzare su disco.
 * <p>
 * È l'unico punto di contatto tra lo stato di gioco e il formato persistente:
 * contiene l'identificativo di versione del salvataggio, lo stato del
 * {@link Player} (salute, oro, zona corrente) e il mazzo principale ridotto
 * alla sequenza dei {@link CardType} delle sue carte. Gli identificatori
 * {@code UUID} delle carte sono ricreati al caricamento, perché non hanno
 * alcun ruolo nel gameplay.
 * </p>
 * <p>
 * La classe è immutabile: espone solo factory di conversione, nessun metodo
 * mutatore. La ricostruzione del {@code Player} avviene tramite il costruttore
 * designato {@link Player#Player(Deck, int, int, int)}, nato per il
 * caricamento da salvataggio, riutilizzando {@link CardFactory} per la creazione
 * delle carte e {@link DeckFactory} per il mazzo.
 * </p>
 */
public final class SaveData {

    private final int health;
    private final int gold;
    private final int currentMapLevel;
    private final List<CardType> mainDeck;

    /**
     * Costruisce il salvataggio dello stato di gioco da un giocatore.
     *
     * @param player il giocatore di cui catturare lo stato
     * @return il salvataggio pronto per la serializzazione
     * @throws IllegalArgumentException if the player is null
     */
    public static SaveData from(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        List<CardType> deckTypes = player.getMainDeck().getCards().stream()
            .map(Card::getType)
            .toList();
        return new SaveData(player.getHealth(), player.getGold(), player.getCurrentMapLevel(), deckTypes);
    }

    /**
     * Ricostruisce il giocatore a partire da questo salvataggio.
     * <p>
     * Le carte vengono ricreate con {@link CardFactory} nella stessa sequenza
     * salvata, così l'ordine del mazzo principale è conservato.
     * </p>
     *
     * @return il giocatore ricostruito dal salvataggio
     */
    public Player toPlayer() {
        List<Card> cards = mainDeck.stream()
            .map(CardFactory::createCard)
            .toList();
        return new Player(DeckFactory.createDeck(cards), health, gold, currentMapLevel);
    }

    /**
     * Costruisce il salvataggio con i valori indicati.
     *
     * @param health la salute del giocatore
     * @param gold l'oro del giocatore
     * @param currentMapLevel il livello della zona corrente del giocatore
     * @param mainDeck i tipi di carta del mazzo principale, in ordine
     * @throws IllegalArgumentException if mainDeck contains null or unknown card types
     */
    public SaveData(int health, int gold, int currentMapLevel, List<CardType> mainDeck) {
        this.health = health;
        this.gold = gold;
        this.currentMapLevel = currentMapLevel;
        this.mainDeck = List.copyOf(Objects.requireNonNull(mainDeck, "Main deck cannot be null"));
    }

    /**
     * Restituisce la salute del giocatore salvata.
     *
     * @return la salute del giocatore
     */
    public int getHealth() {
        return health;
    }

    /**
     * Restituisce l'oro del giocatore salvato.
     *
     * @return l'oro del giocatore
     */
    public int getGold() {
        return gold;
    }

    /**
     * Restituisce il livello della zona corrente del giocatore salvato.
     *
     * @return il livello della zona corrente
     */
    public int getCurrentMapLevel() {
        return currentMapLevel;
    }

    /**
     * Restituisce i tipi di carta del mazzo principale salvato, in ordine.
     *
     * @return la lista dei tipi di carta (lista immutabile)
     */
    public List<CardType> getMainDeck() {
        return mainDeck;
    }
}
