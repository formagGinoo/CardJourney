package it.unicam.cs.mpgc.rpg130809.model.shop;

import it.unicam.cs.mpgc.rpg130809.model.entity.Player;

/**
 * Pozione di salute acquistabile nello shop.
 * <p>
 * L'effetto è immediato: al momento dell'acquisto il giocatore viene curato,
 * senza aggiungere oggetti a un inventario. La pozione non è acquistabile
 * quando il giocatore ha già la salute al massimo, per evitare di sprecare oro.
 * </p>
 */
public class HealthPotionShopItem implements ShopItem {

    private static final String ITEM_NAME = "Pozione di Salute";
    private static final int HEAL_AMOUNT = 50;
    private static final int PRICE = 100;

    private final String name;
    private final int healAmount;
    private final int price;

    /**
     * Crea la pozione con i valori di default del gioco.
     */
    public HealthPotionShopItem() {
        this(ITEM_NAME, HEAL_AMOUNT, PRICE);
    }

    /**
     * Crea una pozione personalizzata.
     *
     * @param name       il nome mostrato nello shop
     * @param healAmount la quantità di salute curata
     * @param price      il prezzo in oro
     */
    public HealthPotionShopItem(String name, int healAmount, int price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (healAmount <= 0) {
            throw new IllegalArgumentException("Heal amount must be positive");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.name = name;
        this.healAmount = healAmount;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Cura di " + healAmount + " HP";
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public boolean isAvailableFor(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        return player.getHealth() < player.getMaxHealth();
    }

    @Override
    public void applyTo(Player player) {
        player.heal(healAmount);
    }

}
