package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.battle.BattleManager;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.game.SpriteManager;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Controller della schermata dedicata ai risultati di combattimento
 * ({@code BattleResultView.fxml}).
 * <p>
 * Legge l'esito dal {@link BattleManager} attivo nel {@link GameManager} e
 * presenta due varianti della stessa vista: vittoria con ricompensa in oro e
 * ritorno alla mappa, oppure sconfitta con ritorno al menu principale.
 * </p>
 * <p>
 * La vista non applica regole di combattimento: si limita a mostrare lo stato
 * già calcolato e a delegare la chiusura della battaglia a
 * {@link GameManager#endCurrentBattle(boolean)} e la navigazione a
 * {@link SceneManager}.
 * </p>
 *
 * @see BattleViewController
 */
public class BattleResultViewController {

    /**
     * Crea il controller della vista dei risultati, istanziato da JavaFX tramite il file FXML.
     */
    public BattleResultViewController() {
    }

    @FXML private ImageView backgroundImage;
    @FXML private Label battleResultTitle;
    @FXML private ImageView battleResultSprite;
    @FXML private Label battleResultSubtitle;
    @FXML private HBox battleResultReward;
    @FXML private Label battleResultGoldReward;
    @FXML private Label battleResultPlayerHp;
    @FXML private Label battleResultPlayerGold;
    @FXML private Button battleResultButton;
    private boolean isVictory = false;

    /**
     * Inizializza la vista caricando i dettagli della battaglia appena conclusa.
     * <p>
     * Recupera giocatore, nemico e {@link BattleManager} dal {@link GameManager},
     * imposta lo sfondo della zona e determina l'esito. Se il gestore della
     * battaglia non è più disponibile, l'esito viene ricostruito dallo stato di
     * salute delle due entità.
     * </p>
     */
    @FXML
    public void initialize() {
        GameManager gm = GameManager.getInstance();
        BattleManager bm = gm.getActiveBattleManager();
        Player player = gm.getPlayer();
        Enemy enemy = (bm != null) ? bm.getEnemy() : gm.getCurrentBattleEnemy();

        if (gm.getActiveGameMap() != null && gm.getActiveGameMap().getMapType() != null) {
            String bgPath = gm.getActiveGameMap().getMapType().getMapBackgroundPath();
            try {
                backgroundImage.setImage(new Image(bgPath));
            } catch (Exception ignored) {
            }
        }

        if (bm != null) {
            this.isVictory = bm.isPlayerVictory();
        } else if (player != null && enemy != null) {
            this.isVictory = (player.getHealth() > 0 && enemy.getHealth() <= 0);
        }

        if (isVictory) {
            setupVictory(player, enemy);
        } else {
            setupDefeat(player, enemy);
        }
    }

    /**
     * Configura l'interfaccia in caso di vittoria del giocatore.
     * <p>
     * Mostra il nemico sconfitto, la ricompensa in oro e le statistiche
     * aggiornate del giocatore. I valori provengono dal {@link GameManager} e
     * dal nemico, già aggiornati dal {@link BattleManager}.
     * </p>
     *
     * @param player il giocatore vincitore
     * @param enemy il nemico sconfitto, può essere {@code null}
     */
    private void setupVictory(Player player, Enemy enemy) {
        battleResultTitle.setText("VITTORIA!");
        battleResultTitle.getStyleClass().setAll("battle-result-title", "battle-result-victory-title");

        String enemyName = (enemy != null) ? enemy.getName() : "il nemico";
        battleResultSubtitle.setText("Hai sconfitto " + enemyName + " in combattimento!");

        Image pSprite = SpriteManager.getPlayerSprite();
        battleResultSprite.setImage(pSprite);
        int goldDrop = (enemy != null) ? enemy.getGoldDrop() : 0;
        battleResultReward.setVisible(true);
        battleResultReward.setManaged(true);
        battleResultGoldReward.setText("+" + goldDrop + " Oro");
        battleResultPlayerHp.setText("HP: " + player.getHealth() + " / " + player.getMaxHealth());
        battleResultPlayerGold.setText("Oro: " + player.getGold());

        battleResultButton.setText("Continua il Viaggio");
        battleResultButton.getStyleClass().setAll("battle-result-button", "battle-result-victory-button");
    }

    /**
     * Configura l'interfaccia in caso di sconfitta del giocatore.
     * <p>
     * Nasconde la ricompensa, mostra il nemico vittorioso e azzera gli HP
     * visualizzati del giocatore.
     * </p>
     *
     * @param player il giocatore sconfitto
     * @param enemy il nemico vittorioso, può essere {@code null}
     */
    private void setupDefeat(Player player, Enemy enemy) {
        battleResultTitle.setText("SCONFITTA!");
        battleResultTitle.getStyleClass().setAll("battle-result-title", "battle-result-defeat-title");

        String enemyName = (enemy != null) ? enemy.getName() : "un nemico";
        battleResultSubtitle.setText("Sei stato sconfitto da " + enemyName + "...");

        if (enemy != null) {
            Image eSprite = SpriteManager.getEnemySprite(enemy.getType());
            battleResultSprite.setImage(eSprite);
        }
        battleResultReward.setVisible(false);
        battleResultReward.setManaged(false);
        battleResultPlayerHp.setText("HP: 0 / " + player.getMaxHealth());
        battleResultPlayerHp.getStyleClass().setAll("battle-result-stat", "battle-result-defeat-player-hp");
        battleResultPlayerGold.setText("Oro: " + player.getGold());
        battleResultButton.setText("Menu Principale");
        battleResultButton.getStyleClass().setAll("battle-result-button", "battle-result-defeat-button");
    }

    /**
     * Gestisce il click sul pulsante d'azione principale.
     * <p>
     * Conclude la battaglia nel {@link GameManager} e apre la mappa in caso di
     * vittoria, oppure il menu principale in caso di sconfitta. La transizione
     * è delegata a {@link SceneManager}.
     * </p>
     */
    @FXML
    private void onBattleResultButtonClicked() {
        GameManager gm = GameManager.getInstance();
        if (isVictory) {
            gm.endCurrentBattle(true);
            SceneManager.getInstance().switchScene("/fxml/MapView.fxml");
        } else {
            gm.endCurrentBattle(false);
            SceneManager.getInstance().switchScene("/fxml/TitleScreen.fxml");
        }
    }

}
