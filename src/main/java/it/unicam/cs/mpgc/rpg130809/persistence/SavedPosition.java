package it.unicam.cs.mpgc.rpg130809.persistence;

import it.unicam.cs.mpgc.rpg130809.model.map.Position;

/**
 * Rappresentazione serializzabile di una coordinata della mappa.
 *
 * @param x la coordinata orizzontale (colonna)
 * @param y la coordinata verticale (riga)
 */
public record SavedPosition(int x, int y) {

    /**
     * Crea la rappresentazione serializzabile a partire dalla posizione del modello.
     *
     * @param position la posizione del modello da convertire
     * @return la posizione pronta per la serializzazione
     * @throws IllegalArgumentException if the position is null
     */
    public static SavedPosition from(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        return new SavedPosition(position.getX(), position.getY());
    }

    /**
     * Ricostruisce la posizione del modello a partire dalla rappresentazione serializzata.
     *
     * @return la {@link Position} corrispondente alle coordinate salvate
     */
    public Position toPosition() {
        return new Position(x, y);
    }
}
