package it.unicam.cs.mpgc.rpg130809.model.player;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;

/**
 * Rappresenta il Mago (il giocatore) nel gioco. Contiene lo stato persistente del giocatore, come il mazzo principale (Main Deck), l'oro e la salute.
 */
public class Player {
    private int gold;
    private final Deck mainDeck;
    private int health;
    private final int maxHealth = 100;

    /**
     * Inizializza un nuovo giocatore.
     * <p>
     * Il giocatore inizia con:
     * <br> - Un mazzo principale
     * <br> - 50 monete d'oro
     * <br> - 100 punti salute
     * </p>
     * @param mainDeck Il mazzo persistente del giocatore
     */
    public Player(Deck mainDeck) {
        this.mainDeck = mainDeck;
        this.health = this.maxHealth;
        this.gold = 50;
    }

    /**
     * Ottiene la salute attuale del giocatore.
     *
     * @return La salute attuale del giocatore
     */
    public int getHealth() {
        return health;
    }

    /**
     * Imposta la salute del giocatore, assicurandosi che non superi la salute massima.
     *
     * @param health La nuova salute del giocatore
     */
    public void setHealth(int health) {
        this.health = Math.min(health, maxHealth);
    }

    /**
     * Infligge danni al giocatore, assicurandosi che la salute non scenda sotto zero.
     *
     * @param amount I danni da infliggere
     */
    public void takeDamage(int amount) {
        if (amount > 0) {
            this.health -= amount;
            if (this.health < 0) {
                this.health = 0;
            }
        }
    }

    /**
     * Ottiene la salute massima del giocatore.
     *
     * @return La salute massima del giocatore
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Ottiene l'oro attuale del giocatore.
     *
     * @return L'oro attuale del giocatore
     */
    public int getGold() {
        return gold;
    }

    /**
     * Imposta l'oro del giocatore.
     *
     * @param gold La nuova quantità di oro del giocatore
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Ottiene il mazzo principale del giocatore.
     *
     * @return Il mazzo principale del giocatore
     */
    public Deck getMainDeck() {
        return mainDeck;
    }

    /**
     * Aggiunge una carta al mazzo principale del giocatore.
     *
     * @param card La carta da aggiungere
     */
    public void addCardToMainDeck(Card card) {
        this.mainDeck.addCard(card);
    }
}
