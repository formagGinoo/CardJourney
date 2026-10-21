package it.unicam.cs.mpgc.rpg130809;

import javafx.application.Application;

/**
 * Launcher dell'applicazione.
 * <p>
 * Delega l'avvio del ciclo di vita JavaFX a
 * {@link CardJourneyApplication}, mantenendo il metodo {@code main} separato
 * dalla classe {@link Application}.
 * </p>
 *
 * @see CardJourneyApplication
 */
public final class GameLauncher {

    /**
     * Costruttore privato: la classe espone solo il punto di ingresso.
     */
    private GameLauncher() {
    }

    /**
     * Avvia l'applicazione JavaFX.
     *
     * @param args argomenti della riga di comando, inoltrati a JavaFX
     */
    static void main(String[] args) {
        Application.launch(CardJourneyApplication.class, args);
    }
}
