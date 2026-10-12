package it.unicam.cs.mpgc.rpg130809.model.game;

import it.unicam.cs.mpgc.rpg130809.model.player.DeckFactory;
import it.unicam.cs.mpgc.rpg130809.model.player.Player;

/**
 * Gestore principale dello stato e della sessione di gioco
 * <p>
 * Implementa il design pattern <b>Singleton</b> per garantire un'unica istanza di gestione dello stato del gioco
 * durante l'intera esecuzione dell'applicazione.
 * </p>
 */
public class GameManager {

    private static GameManager instance;
    private Player player;

    private GameManager() {
    }

    /**
     * Restituisce l'unica istanza attiva di {@link GameManager}.
     * Se l'istanza non esiste ancora, viene creata.
     *
     * @return L'istanza di GameManager
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Avvia una nuova partita creando un nuovo giocatore con il mazzo iniziale
     * (Starter Deck fornito da {@link DeckFactory}) e i valori base di salute e oro.
     */
    public void startNewGame() {
        this.player = new Player(DeckFactory.createStarterDeck());
    }

    /**
     * Restituisce il giocatore attualmente associato alla sessione di gioco.
     *
     * @return Il giocatore corrente, oppure {@code null} se nessuna partita è attiva
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Imposta il giocatore della sessione di gioco.
     * <p>
     * Questo metodo è utilizzato principalmente durante il caricamento di una partita salvata.
     * </p>
     *
     * @param player Il giocatore da associare alla sessione
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
