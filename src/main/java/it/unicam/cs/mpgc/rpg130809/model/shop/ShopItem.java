package it.unicam.cs.mpgc.rpg130809.model.shop;

import it.unicam.cs.mpgc.rpg130809.model.entity.Player;

/**
 * Rappresenta un articolo acquistabile nello shop.
 * <p>
 * L'interfaccia astrae il concetto di "cosa si può comprare": una carta, una
 * pozione o qualsiasi futuro articolo. Lo {@link ShopManager} e la
 * {@code ShopViewController} lavorano solo con questa interfaccia, senza
 * conoscere il tipo concreto dell'articolo.
 * </p>
 * <p>
 * Il pagamento non è responsabilità dell'articolo:
 * {@link ShopManager#buy(ShopItem, Player)} controlla l'oro e lo scala, poi
 * chiama {@link #applyTo(Player)} per applicare l'effetto.
 * </p>
 *
 * @see CardShopItem
 * @see HealthPotionShopItem
 */
public interface ShopItem {

    /**
     * Restituisce il nome mostrato nello shop.
     *
     * @return il nome dell'articolo
     */
    String getName();

    /**
     * Restituisce una breve descrizione dell'effetto dell'articolo.
     *
     * @return la descrizione dell'articolo
     */
    String getDescription();

    /**
     * Restituisce il prezzo dell'articolo in oro.
     *
     * @return il prezzo in oro
     */
    int getPrice();

    /**
     * Indica se l'articolo può essere acquistato dal giocatore in questo momento.
     * <p>
     * Di default ogni articolo è sempre acquistabile. Le sottoclassi possono
     * sovrascrivere questo metodo, ad esempio per impedire di comprare una
     * pozione quando il giocatore ha già la salute al massimo.
     * </p>
     *
     * @param player il giocatore che vuole acquistare l'articolo
     * @return {@code true} se l'articolo può essere acquistato
     */
    default boolean isAvailableFor(Player player) {
        return true;
    }

    /**
     * Applica l'effetto dell'articolo al giocatore.
     * <p>
     * Viene chiamato da {@link ShopManager} solo dopo aver verificato la
     * disponibilità e scalato l'oro. Non deve quindi occuparsi del pagamento.
     * </p>
     *
     * @param player il giocatore che ha acquistato l'articolo
     */
    void applyTo(Player player);
}
