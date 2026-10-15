package it.unicam.cs.mpgc.rpg130809.model.entity.enemy;

import it.unicam.cs.mpgc.rpg130809.model.entity.Damageable;

import java.util.UUID;

/**
 * Rappresenta un nemico generico nel gioco.
 * I nemici condividono l'attacco base definito in questa classe.
 */
public class Enemy implements Damageable {
    private final String id;
    private final EnemyType type;
    private final int maxHealth;
    private int health;

    /**
     * Inizializza un nuovo nemico.
     * 
     * @param type Il tipo di nemico
     */
    public Enemy(EnemyType type) {
        if (type == null) {
            throw new IllegalArgumentException("Enemy type is required");
        }
        this.id = UUID.randomUUID().toString();
        this.type = type;
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
        return type.getEnemyName();
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
        this.health = Math.max(0, Math.min(health, maxHealth));
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
     * @return La quantità di monete d'oro rilasciate da questo nemico quando sconfitto
     */
    public int getGoldDrop() {
        return type.getGoldDrop();
    }

    /**
     * Esegue l'attacco base del nemico durante il suo turno.
     * 
     * @param target Il bersaglio dell'attacco
     */
    public void takeTurn(Damageable target) {
        if (target == null) {
            throw new IllegalArgumentException("Attack target is required");
        }
        target.takeDamage(type.getDamage());
    }
}
