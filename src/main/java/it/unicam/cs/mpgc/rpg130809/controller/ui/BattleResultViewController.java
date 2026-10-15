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
 * Controller per la schermata dedicata ai risultati di combattimento (BattleResultView).
 * Gestisce la visualizzazione specifica per l'esito della battaglia:
 * vittoria con ricompensa in oro e ritorno alla mappa, oppure sconfitta con ritorno al menu principale.
 */
public class BattleResultViewController {
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
