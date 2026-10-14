package it.unicam.cs.mpgc.rpg130809.model.map;

import java.util.Objects;

/**
 * Rappresenta una coordinata bidimensionale immutabile (X, Y) all'interno della griglia della mappa.
 */
public class Position {
    private final int x;
    private final int y;

    /**
     * Costruisce una nuova posizione con le coordinate specificate.
     *
     * @param x La coordinata orizzontale (colonna)
     * @param y La coordinata verticale (riga)
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Restituisce la coordinata orizzontale.
     *
     * @return Il valore della coordinata X
     */
    public int getX() {
        return x;
    }

    /**
     * Restituisce la coordinata verticale.
     *
     * @return Il valore della coordinata Y
     */
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
