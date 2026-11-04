package it.unicam.cs.mpgc.rpg130809.persistence;

import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.EnemyType;
import it.unicam.cs.mpgc.rpg130809.model.map.Position;

/**
 * Rappresentazione serializzabile di un nemico vivo, con la sua posizione sulla mappa.
 *
 * @param position la posizione del nemico sulla mappa
 * @param type il tipo del nemico
 */
public record SavedEnemy(SavedPosition position, EnemyType type) {

    /**
     * Crea la rappresentazione serializzabile a partire da posizione e tipo del modello.
     *
     * @param position la posizione del nemico sulla mappa
     * @param type il tipo del nemico
     * @return la voce pronta per la serializzazione
     * @throws IllegalArgumentException if the position is null
     * @throws NullPointerException if the enemy type is null
     */
    public static SavedEnemy from(Position position, EnemyType type) {
        return new SavedEnemy(SavedPosition.from(position), type);
    }

    /**
     * Ricostruisce la posizione del modello a partire dalla rappresentazione serializzata.
     *
     * @return la {@link Position} corrispondente alle coordinate salvate
     */
    public Position toPosition() {
        return position.toPosition();
    }
}
