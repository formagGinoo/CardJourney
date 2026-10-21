package it.unicam.cs.mpgc.rpg130809.model.entity;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;
import it.unicam.cs.mpgc.rpg130809.model.deck.Deck;

/**
 * Rappresenta il giocatore nel gioco.
 * <p>
 * Contiene lo stato persistente del giocatore: il mazzo principale
 * ({@link Deck}), l'oro, la salute e il livello della zona corrente. È creato e
 * conservato dal {@link it.unicam.cs.mpgc.rpg130809.model.game.GameManager}, che
 * lo passa al {@link it.unicam.cs.mpgc.rpg130809.model.battle.BattleManager} per
 * i combattimenti e allo {@link it.unicam.cs.mpgc.rpg130809.model.shop.ShopManager} per gli acquisti.
 * </p>
 * <p>
 * Implementa {@link Damageable} perché può essere bersaglio degli attacchi nemici.
 * </p>
 */
public class Player implements Damageable {

    private int gold;
    private final Deck mainDeck;
    private int health;
    private final int maxHealth = 100;
    private int currentMapLevel;

    /**
     * Inizializza un nuovo giocatore con i valori di partenza.
     * <p>
     * Il giocatore inizia con:
     * <br> - il mazzo principale fornito
     * <br> - 50 monete d'oro
     * <br> - 100 punti salute
     * <br> - la prima mappa (Sentiero Scosceso)
     * </p>
     *
     * @param mainDeck il mazzo persistente del giocatore
     */
    public Player(Deck mainDeck) {
        this(mainDeck, 100, 50, 1);
    }

    /**
     * Inizializza un nuovo giocatore con parametri specifici.
     * <p>
     * Utilizzato per ricaricare la partita da un salvataggio, permettendo di
     * impostare salute, oro e zona corrente. La salute viene limitata al valore
     * massimo e il livello della mappa non può essere inferiore a 1.
     * </p>
     *
     * @param mainDeck il mazzo persistente del giocatore
     * @param health la salute del giocatore
     * @param gold l'oro del giocatore
     * @param currentMapLevel il livello della zona corrente del giocatore
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
     * @param amount la quantità di danni da infliggere
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
     * @param card la carta da aggiungere
     */
    public void addCardToMainDeck(Card card) {
        this.mainDeck.addCard(card);
    }

    /**
     * Restituisce la salute attuale del giocatore.
     *
     * @return la salute attuale
     */
    public int getHealth() {
        return health;
    }

    /**
     * Imposta la salute del giocatore, limitandola tra zero e la salute massima.
     *
     * @param health la nuova salute del giocatore
     */
    public void setHealth(int health) {
        this.health = Math.clamp(health, 0, maxHealth);
    }

    /**
     * Cura il giocatore di una quantità di salute, senza superare la salute massima.
     *
     * @param amount la quantità di salute da recuperare
     */
    public void heal(int amount) {
        if (amount > 0) {
            setHealth(this.health + amount);
        }
    }

    /**
     * Restituisce la salute massima del giocatore.
     *
     * @return la salute massima
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Restituisce l'oro attuale del giocatore.
     *
     * @return l'oro attuale
     */
    public int getGold() {
        return gold;
    }

    /**
     * Imposta l'oro del giocatore.
     *
     * @param gold la nuova quantità di oro
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Aggiunge una quantità di oro al giocatore.
     *
     * @param amount la quantità di oro da aggiungere
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
     * @param amount la quantità di oro da spendere
     * @return {@code true} se l'oro è stato speso, {@code false} se l'oro non basta
     * @throws IllegalArgumentException if the amount is negative
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
     * Restituisce il mazzo principale del giocatore.
     *
     * @return il mazzo principale
     */
    public Deck getMainDeck() {
        return mainDeck;
    }

    /**
     * Fa avanzare il giocatore alla mappa successiva.
     * <p>
     * Il livello viene incrementato solo se non è già l'ultimo (3).
     * </p>
     */
    public void advanceToNextMap() {
        if (this.currentMapLevel < 3) {
            this.currentMapLevel++;
        }
    }

    /**
     * Restituisce il livello della mappa corrente del giocatore.
     *
     * @return il livello della mappa corrente
     */
    public int getCurrentMapLevel() {
        return currentMapLevel;
    }

    /**
     * Imposta il livello della mappa corrente del giocatore.
     * <p>
     * Il valore viene applicato solo se compreso tra 1 e 3.
     * </p>
     *
     * @param mapLevel il nuovo livello della mappa corrente
     */
    public void setCurrentMapLevel(int mapLevel) {
        if (mapLevel >= 1 && mapLevel <= 3) {
            this.currentMapLevel = mapLevel;
        }
    }
}
