package it.unicam.cs.mpgc.rpg130809.model.entity;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;
import it.unicam.cs.mpgc.rpg130809.model.deck.Deck;

/**
 * Rappresenta il Mago (il giocatore) nel gioco. Contiene lo stato persistente del giocatore, come il mazzo principale (Main Deck), l'oro e la salute.
 */
public class Player implements Damageable {
    private int gold;
    private final Deck mainDeck;
    private int health;
    private final int maxHealth = 100;
    private int currentMapLevel;

    /**
     * Inizializza un nuovo giocatore.
     * <p>
     * Il giocatore inizia con:
     * <br> - Un mazzo principale
     * <br> - 50 monete d'oro
     * <br> - 100 punti salute
     * Il giocatore inizia nella prima mappa (Sentiero Scosceso).
     * </p>
     * @param mainDeck Il mazzo persistente del giocatore
     */
    public Player(Deck mainDeck) {
        this(mainDeck, 100, 50, 1);
    }

    /**
     * Inizializza un nuovo giocatore con parametri specifici.
     * <p>
     * Utilizzato per ricaricare la partita da un salvataggio, permettendo di impostare la salute, l'oro e la zona corrente del giocatore.
     * </p>
     * @param mainDeck Il mazzo persistente del giocatore
     * @param health La salute iniziale del giocatore
     * @param gold L'oro iniziale del giocatore
     * @param currentMapLevel Il livello della zona corrente del giocatore
     */
    public Player(Deck mainDeck, int health, int gold, int currentMapLevel) {
        this.mainDeck = mainDeck;
        this.health = Math.min(health, maxHealth);
        this.gold = gold;
        this.currentMapLevel = Math.max(1, currentMapLevel);
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
     * Aggiunge una carta al mazzo principale del giocatore.
     *
     * @param card La carta da aggiungere
     */
    public void addCardToMainDeck(Card card) {
        this.mainDeck.addCard(card);
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
        this.health = Math.clamp(health, 0, maxHealth);
    }

    /**
     * Cura il giocatore di una quantità di salute, senza superare la salute massima.
     *
     * @param amount La quantità di salute da recuperare
     */
    public void heal(int amount) {
        if (amount > 0) {
            setHealth(this.health + amount);
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
     * Aggiunge una quantità di oro al giocatore.
     *
     * @param amount La quantità di oro da aggiungere
     */
    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
        }
    }

    /**
     * Prova a spendere una quantità di oro.
     * <p>
     * L'oro viene scalato solo se il giocatore possiede la cifra richiesta.
     * </p>
     *
     * @param amount La quantità di oro da spendere
     * @return {@code true} se l'oro è stato speso, {@code false} se l'oro non basta
     */
    public boolean spendGold(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (this.gold < amount) {
            return false;
        }
        this.gold -= amount;
        return true;
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
     * Fa avanzare il giocatore alla mappa successiva
     */
    public void advanceToNextMap() {
        if (this.currentMapLevel < 3) {
            this.currentMapLevel++;
        }
    }

    /**
     * Ottiene il livello della mappa corrente del giocatore.
     *
     * @return Il livello della mappa corrente del giocatore
     */
    public int getCurrentMapLevel() {
        return currentMapLevel;
    }

    /**
     * Imposta il livello della mappa corrente del giocatore.
     *
     * @param mapLevel Il nuovo livello della mappa corrente del giocatore
     */
    public void setCurrentMapLevel(int mapLevel) {
        if (mapLevel >= 1 && mapLevel <= 3) {
            this.currentMapLevel = mapLevel;
        }
    }
}
