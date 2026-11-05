package it.unicam.cs.mpgc.rpg130809.model.game;

import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
import it.unicam.cs.mpgc.rpg130809.model.map.MapProgress;

/**
 * Contratto delle notifiche di cambiamento dello stato di gioco.
 * <p>
 * Il {@link GameManager} segnala qui i momenti in cui lo stato della sessione
 * è cambiato in modo rilevante. Il {@link Player}, il {@link MapProgress}
 * con i nemici rimasti vivi in ciascuna zona e il {@link GameState} corrente.
 * Il contratto vive nel modello: è il modello stesso a dichiarare quali
 * cambiamenti sono rilevanti per l'esterno, senza conoscere alcuna
 * implementazione concreta. Chi vuole reagire (il salvataggio automatico,
 * statistiche, telemetry) implementa questa interfaccia e si registra con
 * {@link GameManager#addStateListener(GameStateListener)}.
 * </p>
 */
public interface GameStateListener {

    /**
     * Notifica che lo stato di gioco è cambiato e andrebbe registrato.
     *
     * @param player il giocatore nello stato corrente
     * @param mapProgress i nemici rimasti vivi in ciascuna zona nello stato corrente
     * @param gameState lo stato della sessione al momento del cambiamento
     */
    void onGameStateChanged(Player player, MapProgress mapProgress, GameState gameState);
}
