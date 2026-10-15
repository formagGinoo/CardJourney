package it.unicam.cs.mpgc.rpg130809.model.entity;

/**
 * Rappresenta un'entità che può subire danni durante un combattimento.
 */
public interface Damageable {
    void takeDamage(int amount);

    int getHealth();
}
