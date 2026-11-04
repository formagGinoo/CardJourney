package it.unicam.cs.mpgc.rpg130809.model.game;

import it.unicam.cs.mpgc.rpg130809.model.entity.Player;

/**
 * Contratto delle notifiche di cambiamento dello stato persistente di gioco.
 * <p>
 * Il {@link GameManager} segnala qui i momenti in cui lo stato del
 * {@link Player} è cambiato in modo rilevante (fine battaglia, cambio zona).
 * Il contratto vive nel modello: è il modello stesso a dichiarare quali
 * cambiamenti dello stato sono rilevanti per l'esterno, senza conoscere
 * alcuna implementazione concreta. Chi vuole reagire (il salvataggio
 * automatico, statistiche, telemetry) implementa questa interfaccia e si
 * registra con {@link GameManager#addStateListener(GameStateListener)}.
 * </p>
 */
public interface GameStateListener {

    /**
     * Notifica che lo stato persistente del giocatore è cambiato e andrebbe
     * registrato.
     *
     * @param player il giocatore nello stato corrente
     */
    void onPlayerStateChanged(Player player);
}
