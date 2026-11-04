package it.unicam.cs.mpgc.rpg130809.model.game;

import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
import it.unicam.cs.mpgc.rpg130809.model.map.MapProgress;

/**
 * Contratto delle notifiche di cambiamento dello stato persistente di gioco.
 * <p>
 * Il {@link GameManager} segnala qui i momenti in cui lo stato persistente è
 * cambiato in modo rilevante (fine battaglia, cambio zona), trasportando
 * l'intero stato da registrare: il {@link Player} e il {@link MapProgress}
 * con i nemici rimasti vivi in ciascuna zona. Il contratto vive nel modello:
 * è il modello stesso a dichiarare quali cambiamenti dello stato sono
 * rilevanti per l'esterno, senza conoscere alcuna implementazione concreta.
 * Chi vuole reagire (il salvataggio automatico, statistiche, telemetry)
 * implementa questa interfaccia e si registra con
 * {@link GameManager#addStateListener(GameStateListener)}.
 * </p>
 */
public interface GameStateListener {

    /**
     * Notifica che lo stato persistente di gioco è cambiato e andrebbe registrato.
     *
     * @param player il giocatore nello stato corrente
     * @param mapProgress i nemici rimasti vivi in ciascuna zona nello stato corrente
     */
    void onGameStateChanged(Player player, MapProgress mapProgress);
}
