package it.unicam.cs.mpgc.rpg130809.model.enemy;

import it.unicam.cs.mpgc.rpg130809.model.player.Player;

import java.util.UUID;

/**
 * Rappresenta un nemico generico nel gioco.
 * Ogni nemico specifico erediterà da questa classe e
 * implementerà la propria logica di combattimento nel metodo takeTurn.
 */
public abstract class Enemy {
    private final String id;
    private final EnemyType type;
    private final String name;
    private final int maxHealth;
    private int health;

    /**
     * Inizializza un nuovo nemico.
     * 
     * @param type Il tipo di nemico
     * @param name Il nome mostrato a schermo per questo nemico
     */
    public Enemy(EnemyType type, String name) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.name = name;
        this.maxHealth = type.getHealth();
        this.health = this.maxHealth;
    }

    /**
     * @return L'identificatore univoco di questa istanza di nemico
     */
    public String getId() {
        return id;
    }

    /**
     * @return Il tipo base del nemico
     */
    public EnemyType getType() {
        return type;
    }

    /**
     * @return Il nome del nemico
     */
    public String getName() {
        return name;
    }

    /**
     * @return Gli HP massimi del nemico
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * @return Gli HP correnti del nemico
     */
    public int getHealth() {
        return health;
    }

    /**
     * Imposta gli HP del nemico, assicurandosi che non superino il limite massimo.
     * 
     * @param health I nuovi HP del nemico
     */
    public void setHealth(int health) {
        this.health = Math.min(health, maxHealth);
    }

    /**
     * Infligge danni al nemico, garantendo che gli HP non scendano sotto zero.
     * 
     * @param amount Quantità di danni subiti
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
     * Esegue l'azione o l'attacco del nemico durante il suo turno.
     * Qualsiasi sottoclasse implementerà in modo autonomo come attaccare il giocatore.
     * 
     * @param player Il giocatore bersaglio dell'attacco
     */
    public abstract void takeTurn(Player player);
}
