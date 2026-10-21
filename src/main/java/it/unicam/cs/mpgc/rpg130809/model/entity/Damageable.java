package it.unicam.cs.mpgc.rpg130809.model.entity;

/**
 * Rappresenta un'entità che può subire danni durante un combattimento.
 * <p>
 * L'interfaccia astrae il bersaglio degli attacchi: viene implementata dal
 * {@link Player} e da
 * {@link it.unicam.cs.mpgc.rpg130809.model.entity.enemy.Enemy}, così
 * {@link it.unicam.cs.mpgc.rpg130809.model.entity.enemy.Enemy#takeTurn(Damageable)}
 * può colpire qualsiasi entità senza conoscerne il tipo concreto.
 * </p>
 */
public interface Damageable {

    /**
     * Applica una quantità di danni all'entità.
     * <p>
     * Le implementazioni devono garantire che la salute non scenda sotto zero.
     * </p>
     *
     * @param amount la quantità di danni da infliggere
     */
    void takeDamage(int amount);

    /**
     * Restituisce la salute corrente dell'entità.
     *
     * @return la salute corrente
     */
    int getHealth();
}
