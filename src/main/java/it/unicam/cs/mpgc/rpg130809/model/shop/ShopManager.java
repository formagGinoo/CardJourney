package it.unicam.cs.mpgc.rpg130809.model.shop;

import it.unicam.cs.mpgc.rpg130809.model.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce il catalogo dello shop e le transazioni di acquisto.
 * <p>
 * Lo shop non conosce i tipi concreti degli articoli: lavora solo con
 * {@link ShopItem}. Questo permette di aggiungere nuove categorie di oggetti
 * senza modificare questa classe. Il catalogo viene creato da
 * {@link ShopFactory} e usato dalla {@code ShopViewController}.
 * </p>
 * <p>
 * La transazione è centralizzata in {@link #buy(ShopItem, Player)}: verifica la
 * disponibilità, scala l'oro tramite {@link Player#spendGold(int)} e applica
 * l'effetto tramite {@link ShopItem#applyTo(Player)}.
 * </p>
 */
public class ShopManager {

    private final List<ShopItem> items;

    /**
     * Crea uno shop vuoto.
     */
    public ShopManager() {
        this.items = new ArrayList<>();
    }

    /**
     * Crea uno shop con gli articoli indicati.
     *
     * @param items gli articoli iniziali
     * @throws IllegalArgumentException if the item list is null
     */
    public ShopManager(List<ShopItem> items) {
        if (items == null) {
            throw new IllegalArgumentException("Items cannot be null");
        }
        this.items = new ArrayList<>(items);
    }

    /**
     * Aggiunge un articolo al catalogo.
     *
     * @param item l'articolo da aggiungere
     * @throws IllegalArgumentException if the item is null
     */
    public void addItem(ShopItem item) {
        if (item == null) {
            throw new IllegalArgumentException("ShopItem cannot be null");
        }
        this.items.add(item);
    }

    /**
     * Restituisce una vista immutabile degli articoli in vendita.
     *
     * @return la lista degli articoli
     */
    public List<ShopItem> getItems() {
        return List.copyOf(items);
    }

    /**
     * Tenta di acquistare un articolo per il giocatore.
     * <p>
     * La transazione è centralizzata qui: verifica che l'articolo sia in vendita
     * e disponibile, controlla e scala l'oro, poi applica l'effetto. Gli articoli
     * non devono occuparsi del pagamento.
     * </p>
     *
     * @param item l'articolo da acquistare
     * @param player il giocatore acquirente
     * @return l'esito dell'acquisto
     * @throws IllegalArgumentException if the item or the player is null
     */
    public PurchaseResult buy(ShopItem item, Player player) {
        if (item == null || player == null) {
            throw new IllegalArgumentException("Item and player are required");
        }
        if (!items.contains(item) || !item.isAvailableFor(player)) {
            return PurchaseResult.NOT_AVAILABLE;
        }
        if (!player.spendGold(item.getPrice())) {
            return PurchaseResult.NOT_ENOUGH_GOLD;
        }
        item.applyTo(player);
        return PurchaseResult.SUCCESS;
    }

    /**
     * Indica se il giocatore può permettersi l'articolo.
     * <p>
     * Utile alla UI per abilitare o disabilitare un pulsante d'acquisto.
     * </p>
     *
     * @param item l'articolo
     * @param player il giocatore
     * @return {@code true} se il giocatore ha abbastanza oro
     */
    public boolean canAfford(ShopItem item, Player player) {
        return item != null && player != null && player.getGold() >= item.getPrice();
    }
}
