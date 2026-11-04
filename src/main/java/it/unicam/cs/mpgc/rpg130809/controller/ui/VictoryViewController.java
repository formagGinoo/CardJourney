package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.GameConstants;
import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.game.GameState;
import it.unicam.cs.mpgc.rpg130809.model.game.SoundManager;
import it.unicam.cs.mpgc.rpg130809.model.game.SpriteManager;
import it.unicam.cs.mpgc.rpg130809.persistence.SaveManager;
import it.unicam.cs.mpgc.rpg130809.model.map.MapType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * Controller della schermata di vittoria dell'intera partita
 * ({@code VictoryView.fxml}).
 * <p>
 * Viene aperta quando il giocatore completa l'ultima zona con il boss
 * sconfitto, come segnalato da {@link GameManager#completeCurrentZone()}.
 * Presenta il riepilogo dello stato del giocatore (salute, oro e numero di
 * carte del mazzo) e il pulsante per tornare al menu principale.
 * </p>
 * <p>
 * La vista è in sola lettura e non altera lo stato della partita: non azzera
 * i dati del {@link GameManager}, perché l'eventuale layer di persistenza
 * futuro deve poter leggere l'esito anche dopo la vittoria. Il reset è
 * responsabilità del flusso del menu principale, che avvia una nuova partita
 * tramite {@code startNewGame()}. La transizione è delegata a
 * {@link SceneManager}.
 * </p>
 *
 * @see GameState#VICTORY
 */
public class VictoryViewController {

    /**
     * Crea il controller della schermata di vittoria, istanziato da JavaFX tramite il file FXML.
     */
    public VictoryViewController() {
    }

    @FXML private ImageView victorySprite;
    @FXML private Label victorySubtitle;
    @FXML private Label victoryPlayerHp;
    @FXML private Label victoryPlayerGold;
    @FXML private Label victoryPlayerDeck;
    @FXML private Button victoryButton;

    /**
     * Inizializza la vista mostrando il riepilogo del giocatore vincitore.
     * <p>
     * Recupera il {@link Player} dal {@link GameManager}, imposta lo sprite,
     * le statistiche finali e i testi celebrativi; infine avvia il brano
     * dedicato alla vittoria. Se nessun giocatore è associato alla sessione
     * (accesso diretto alla vista senza partita attiva), la vista resta
     * generica senza statistiche.
     * </p>
     */
    @FXML
    public void initialize() {
        SoundManager.getInstance().playMusic(GameConstants.VICTORY_THEME);

        // La partita è finita: il salvataggio non serve più.
        SaveManager.getInstance().delete();

        Player player = GameManager.getInstance().getPlayer();
        victorySprite.setImage(SpriteManager.getPlayerSprite());

        MapType finalZone = MapType.fromLevel(player.getCurrentMapLevel());
        victorySubtitle.setText("Hai conquistato " + finalZone.getName() + " e completato il viaggio!");
        victoryPlayerHp.setText("HP: " + player.getHealth() + " / " + player.getMaxHealth());
        victoryPlayerGold.setText("Oro: " + player.getGold());
        victoryPlayerDeck.setText("Mazzo: " + player.getMainDeck().size());
        victoryButton.setText("Torna al Menu");
    }

    /**
     * Gestisce il click sul pulsante {@code Torna al Menu}.
     * <p>
     * La transizione viene delegata a {@link SceneManager}. Lo stato del
     * {@link GameManager} non viene azzerato qui: una nuova partita lo
     * ricreerà tramite {@code startNewGame()} dal menu principale.
     * </p>
     */
    @FXML
    private void onVictoryButtonClicked() {
        SceneManager.getInstance().switchScene(GameConstants.TITLE_SCREEN_FXML);
    }
}
