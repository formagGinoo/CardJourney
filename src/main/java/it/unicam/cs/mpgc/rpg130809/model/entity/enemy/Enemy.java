package it.unicam.cs.mpgc.rpg130809.model.entity.enemy;

import it.unicam.cs.mpgc.rpg130809.model.entity.Damageable;

import java.util.UUID;

/**
 * Rappresenta un nemico generico nel gioco.
 * <p>
 * Ogni istanza ha un identificatore univoco ed è configurata da un
 * {@link EnemyType}, che ne definisce nome, salute, danno e oro rilasciato. I
 * nemici sono creati da {@link EnemyFactory}, posizionati sulla mappa da
 * {@link it.unicam.cs.mpgc.rpg130809.model.map.GameMap} e affrontati dal
 * {@link it.unicam.cs.mpgc.rpg130809.model.battle.BattleManager}.
 * </p>
 * <p>
 * Implementa {@link Damageable} perché può subire gli attacchi del giocatore,
 * mentre {@link #takeTurn(Damageable)} gli consente di attaccare a sua volta.
 * </p>
 */
public class Enemy implements Damageable {

    private final String id;
    private final EnemyType type;
    private final int maxHealth;
    private int health;

    /**
     * Inizializza un nuovo nemico del tipo indicato.
     * <p>
     * La salute parte al valore massimo previsto dal tipo.
     * </p>
     *
     * @param type il tipo di nemico
     * @throws IllegalArgumentException if the enemy type is null
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
     * Restituisce l'identificatore univoco di questa istanza di nemico.
     *
     * @return l'identificatore univoco
     */
    public String getId() {
        return id;
    }

    /**
     * Restituisce il tipo base del nemico.
     *
     * @return il tipo del nemico
     */
    public EnemyType getType() {
        return type;
    }

    /**
     * Restituisce il nome del nemico.
     *
     * @return il nome del nemico
     */
    public String getName() {
        return type.getEnemyName();
    }

    /**
     * Restituisce gli HP massimi del nemico.
     *
     * @return gli HP massimi
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Restituisce gli HP correnti del nemico.
     *
     * @return gli HP correnti
     */
    public int getHealth() {
        return health;
    }

    /**
     * Imposta gli HP del nemico, assicurandosi che restino tra zero e il limite massimo.
     *
     * @param health i nuovi HP del nemico
     */
    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    /**
     * Infligge danni al nemico, garantendo che gli HP non scendano sotto zero.
     *
     * @param amount la quantità di danni subiti
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
     * Restituisce la quantità di monete d'oro rilasciate da questo nemico quando viene sconfitto.
     *
     * @return l'oro rilasciato
     */
    public int getGoldDrop() {
        return type.getGoldDrop();
    }

    /**
     * Esegue l'attacco base del nemico durante il suo turno.
     *
     * @param target il bersaglio dell'attacco
     * @throws IllegalArgumentException if the target is null
     */
    public void takeTurn(Damageable target) {
        if (target == null) {
            throw new IllegalArgumentException("Attack target is required");
        }
        target.takeDamage(type.getDamage());
    }
}
