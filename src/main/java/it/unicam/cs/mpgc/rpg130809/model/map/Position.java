package it.unicam.cs.mpgc.rpg130809.model.map;

import java.util.Objects;

/**
 * Rappresenta una coordinata bidimensionale immutabile (X, Y) all'interno della
 * griglia della mappa.
 * <p>
 * Le posizioni sono usate come chiavi nella mappa dei nemici di
 * {@link GameMap} e per confrontare la posizione del giocatore con quella del
 * negozio, dell'uscita e del boss. L'immutabilità e l'implementazione di
 * {@link #equals(Object)} e {@link #hashCode()} rendono la classe adatta a
 * questo scopo.
 * </p>
 */
public class Position {

    private final int x;
    private final int y;

    /**
     * Costruisce una nuova posizione con le coordinate specificate.
     *
     * @param x la coordinata orizzontale (colonna)
     * @param y la coordinata verticale (riga)
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Restituisce la coordinata orizzontale.
     *
     * @return il valore della coordinata X
     */
    public int getX() {
        return x;
    }

    /**
     * Restituisce la coordinata verticale.
     *
     * @return il valore della coordinata Y
     */
    public int getY() {
        return y;
    }

    /**
     * Confronta questa posizione con un'altra per uguaglianza di coordinate.
     *
     * @param obj l'oggetto da confrontare
     * @return {@code true} se entrambe le coordinate coincidono
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    /**
     * Calcola l'hash code della posizione a partire dalle due coordinate.
     *
     * @return l'hash code della posizione
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Restituisce una rappresentazione testuale della posizione.
     *
     * @return la stringa nel formato {@code (x, y)}
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
