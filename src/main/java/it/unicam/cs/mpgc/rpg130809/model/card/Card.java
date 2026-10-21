package it.unicam.cs.mpgc.rpg130809.model.card;

import java.util.UUID;

/**
 * Rappresenta una carta del gioco.
 * <p>
 * Ogni carta ha un identificatore univoco e un {@link CardType} immutabile, che
 * definisce nome, danno e costo nello shop. Le carte sono create tramite
 * {@link CardFactory}, raccolte nei mazzi
 * ({@link it.unicam.cs.mpgc.rpg130809.model.deck.Deck}) e utilizzate in
 * battaglia dalla {@link it.unicam.cs.mpgc.rpg130809.model.battle.Hand}.
 * </p>
 */
public class Card {

    private final String id;
    private final CardType type;

    /**
     * Costruisce una nuova carta in base al suo tipo.
     * <p>
     * L'identificatore viene generato automaticamente e il nome è derivato dal
     * tipo, per evitare modifiche esterne.
     * </p>
     *
     * @param type il tipo della carta, che definisce anche nome, danno e costo
     */
    public Card(CardType type) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
    }

    /**
     * Restituisce l'identificatore univoco della carta.
     *
     * @return l'id univoco della carta
     */
    public String getId() {
        return id;
    }

    /**
     * Restituisce il tipo della carta.
     *
     * @return il tipo della carta
     */
    public CardType getType() {
        return type;
    }

    /**
     * Restituisce il nome della carta.
     *
     * @return il nome della carta
     */
    public String getName() {
        return type.getCardName();
    }

    /**
     * Restituisce il danno di base della carta.
     *
     * @return il danno della carta, ricavato dal tipo
     */
    public int getDamage() {
        return type.getDamage();
    }
}
