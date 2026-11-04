package it.unicam.cs.mpgc.rpg130809.model.game;

/**
 * Rappresenta lo stato complessivo della partita del giocatore.
 * <p>
 * Il valore è mantenuto dal {@link GameManager} e aggiornato nei punti
 * centralizzati di mutazione dello stato della partita in corso; le viste del
 * package {@code it.unicam.cs.mpgc.rpg130809.controller.ui} lo leggono per
 * instradare il flusso, ad esempio verso la schermata di vittoria quando la
 * partita è conclusa.
 * </p>
 * <p>
 * L'enumerazione è volutamente generalista per poter descrivere in futuro
 * anche stati come {@code GAME_OVER} o {@code IN_BATTLE} senza modificare i
 * client esistenti.
 * </p>
 */
public enum GameState {

    /** La partita è in corso, esistono zone successive da esplorare. */
    IN_PROGRESS,

    /** Il giocatore ha completato l'ultima zona: partita vinta. */
    VICTORY
}
