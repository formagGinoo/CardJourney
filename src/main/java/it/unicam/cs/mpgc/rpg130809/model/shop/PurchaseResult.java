package it.unicam.cs.mpgc.rpg130809.model.shop;

/**
 * Esito di un tentativo di acquisto nello shop.
 */
public enum PurchaseResult {

    /**
     * L'acquisto è andato a buon fine.
     */
    SUCCESS,

    /**
     * Il giocatore non possiede abbastanza oro.
     */
    NOT_ENOUGH_GOLD,

    /**
     * L'articolo non è disponibile in questo momento (ad esempio pozione a HP pieni).
     */
    NOT_AVAILABLE
}
