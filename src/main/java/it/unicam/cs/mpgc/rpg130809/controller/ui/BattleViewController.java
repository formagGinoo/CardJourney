package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.battle.BattleManager;
import it.unicam.cs.mpgc.rpg130809.model.card.Card;
import it.unicam.cs.mpgc.rpg130809.model.card.CardType;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.game.SpriteManager;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller della schermata di combattimento ({@code BattleView.fxml}).
 * <p>
 * Coordina la rappresentazione grafica e le interazioni della battaglia tra il giocatore
 * e il nemico. Mostra le statistiche delle entità, la mano del giocatore, le pile
 * di pesca e scarto e lo stato del turno.
 * </p>
 * <p>
 * Le regole del combattimento restano in {@link BattleManager}; questo controller
 * interpreta i comandi dell'utente, aggiorna l'interfaccia e coordina le
 * animazioni.
 * </p>
 */
public class BattleViewController {

    @FXML private ImageView backgroundImage;
    @FXML private Label mapLabel;
    @FXML private Label turnLabel;
    @FXML private Label statusLabel;

    @FXML private ImageView playerSprite;
    @FXML private Label playerDamage;
    @FXML private ProgressBar playerHpBar;
    @FXML private Label playerHpLabel;

    @FXML private Label comboLabel;

    @FXML private Label enemyLabel;
    @FXML private ImageView enemySprite;
    @FXML private Label enemyDamage;
    @FXML private ProgressBar enemyHpBar;
    @FXML private Label enemyHpLabel;
    @FXML private Label enemyAtkLabel;

    @FXML private VBox drawPile;
    @FXML private Label drawPileCountLabel;
    @FXML private HBox cards;
    @FXML private VBox discardPile;
    @FXML private Label discardPileCountLabel;

    @FXML private Button attackButton;
    @FXML private Button endTurnButton;

    @FXML private Pane animation;

    private BattleManager battleManager;
    private Player player;
    private Enemy enemy;
    private final List<Card> selectedCards = new ArrayList<>();
    private final Map<Card, StackPane> cardNodes = new HashMap<>();
    private boolean isAnimating = false;

    /**
     * Inizializza la vista collegandola allo stato della battaglia attiva.
     * <p>
     * Recupera combattimento, giocatore e nemico dal {@link GameManager}, registra
     * il callback per il rimescolamento delle pile, configura gli elementi statici
     * e sincronizza statistiche, pile, combo e turno.
     * </p>
     */
    @FXML
    public void initialize() {
        GameManager gm = GameManager.getInstance();
        this.battleManager = gm.getActiveBattleManager();
        this.player = battleManager.getPlayer();
        this.enemy = battleManager.getEnemy();
        setupVisuals();
        updateEntitiesStats();
        updatePileCounts();
        updateComboPreview();
        updateTurnInfo();
        Platform.runLater(() -> renderHand(null));
    }

    /**
     * Configura gli elementi visivi che non cambiano durante il turno.
     * <p>
     * Imposta lo sfondo della mappa attiva, il nome della zona, gli sprite delle
     * entità e le info del nemico, incluso il suo danno
     * base.
     * </p>
     */
    private void setupVisuals() {
        GameManager gm = GameManager.getInstance();
        String bgPath = gm.getActiveGameMap().getMapType().getMapBackgroundPath();
        backgroundImage.setImage(new Image(bgPath));
        mapLabel.setText(gm.getActiveGameMap().getMapType().getName());

        Image pSprite = SpriteManager.getPlayerSprite();
        playerSprite.setImage(pSprite);

        Image eSprite = SpriteManager.getEnemySprite(enemy.getType());
        enemySprite.setImage(eSprite);

        enemyLabel.setText(enemy.getName());
        enemyAtkLabel.setText("Attacco: " + enemy.getType().getDamage() + " DMG");
    }

    /**
     * Aggiorna barre e testi della salute delle due entità.
     * <p>
     * Il progresso delle barre è calcolato come rapporto tra salute corrente e
     * salute massima; la salute corrente viene limitata a zero per evitare
     * progressi negativi nella rappresentazione grafica.
     * </p>
     */
    private void updateEntitiesStats() {
        // Player
        double playerProgress = (double) Math.max(0, player.getHealth()) / player.getMaxHealth();
        playerHpBar.setProgress(playerProgress);
        playerHpLabel.setText(player.getHealth() + " / " + player.getMaxHealth() + " HP");

        // Enemy
        double enemyProgress = (double) Math.max(0, enemy.getHealth()) / enemy.getMaxHealth();
        enemyHpBar.setProgress(enemyProgress);
        enemyHpLabel.setText(enemy.getHealth() + " / " + enemy.getMaxHealth() + " HP");
    }

    /**
     * Aggiorna i contatori visualizzati per drawPile e discardPile.
     * <p>
     * I valori sono letti direttamente dal {@link BattleManager}, così l'hud
     * riflette le modifiche successive a pesca, scarto e rimescolamento.
     * </p>
     */
    private void updatePileCounts() {
        drawPileCountLabel.setText(String.valueOf(battleManager.getDrawPile().size()));
        discardPileCountLabel.setText(String.valueOf(battleManager.getDiscardPile().size()));
    }

    /**
     * Renderizza le carte della mano corrente.
     * <p>
     * Rimuove la rappresentazione precedente, svuota la mappa tra carte e nodi grafici,
     * azzera le selezioni e crea un nodo grafico interattivo per ogni carta posseduta.
     * È usato quando non è necessaria un'animazione di pesca dal drawPile.
     * </p>
     */
    private void renderHand() {
        cards.getChildren().clear();
        cardNodes.clear();
        selectedCards.clear();

        List<Card> handCards = battleManager.getPlayerHand().getCards();
        for (Card card : handCards) {
            StackPane cardNode = createCardNode(card);
            cardNodes.put(card, cardNode);
            cards.getChildren().add(cardNode);
        }
    }

    /**
     * Renderizza le carte della mano corrente animando la pesca delle carte dal drawPile.
     * <p>
     * I nodi grafici vengono creati inizialmente trasparenti per permettere il
     * calcolo del layout. Per ogni carta viene poi animato uno sprite dalla pila
     * alla posizione finale, con movimento, scala, dissolvenza e rotazione. Se la
     * scena non è pronta per calcolare le coordinate, la mano viene mostrata
     * immediatamente senza animazione.
     * </p>
     *
     * @param onFinished callback opzionale invocato al termine della pescata, può essere {@code null}}
     */
    private void renderHand(Runnable onFinished) {
        cards.getChildren().clear();
        cardNodes.clear();
        selectedCards.clear();

        List<Card> handCards = battleManager.getPlayerHand().getCards();
        if (handCards.isEmpty()) {
            if (onFinished != null) onFinished.run();
            return;
        }

        // Crea i nodi delle carte con opacità 0 per consentire il calcolo del layout
        for (Card card : handCards) {
            StackPane cardNode = createCardNode(card);
            cardNode.setOpacity(0.0);
            cardNodes.put(card, cardNode);
            cards.getChildren().add(cardNode);
        }

        cards.applyCss();
        cards.layout();

        if (drawPile == null || animation == null || drawPile.getScene() == null || animation.getScene() == null) {
            for (StackPane node : cardNodes.values()) {
                node.setOpacity(1.0);
            }
            if (onFinished != null) onFinished.run();
            return;
        }

        Bounds drawSceneBounds = drawPile.localToScene(drawPile.getBoundsInLocal());
        if (drawSceneBounds == null) {
            for (StackPane node : cardNodes.values()) {
                node.setOpacity(1.0);
            }
            if (onFinished != null) onFinished.run();
            return;
        }

        Bounds drawInAnim = animation.sceneToLocal(drawSceneBounds);
        double startX = drawInAnim.getMinX() + (drawInAnim.getWidth() / 2.0) - 42.0;
        double startY = drawInAnim.getMinY() + (drawInAnim.getHeight() / 2.0) - 63.0;

        ParallelTransition dealTransition = new ParallelTransition();

        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            StackPane cardNode = cardNodes.get(card);

            Bounds cardBounds = animation.sceneToLocal(cardNode.localToScene(cardNode.getBoundsInLocal()));
            double endX = (cardBounds != null) ? cardBounds.getMinX() : (300.0 + i * 90.0);
            double endY = (cardBounds != null) ? cardBounds.getMinY() : 480.0;

            ImageView flyingCard = createCardForAnimation(card.getType(), 84.0, 126.0, "flying-card-sprite", startX, startY, 0.0);
            animation.getChildren().add(flyingCard);

            TranslateTransition move = new TranslateTransition(Duration.millis(240), flyingCard);
            move.setToX(endX - startX);
            move.setToY(endY - startY);
            move.setInterpolator(Interpolator.EASE_OUT);

            ScaleTransition scale = new ScaleTransition(Duration.millis(240), flyingCard);
            scale.setFromX(0.5);
            scale.setFromY(0.5);
            scale.setToX(1.0);
            scale.setToY(1.0);

            FadeTransition fade = new FadeTransition(Duration.millis(240), flyingCard);
            fade.setFromValue(0.4);
            fade.setToValue(1.0);

            RotateTransition rotate = new RotateTransition(Duration.millis(240), flyingCard);
            rotate.setFromAngle(-20);
            rotate.setToAngle(0);

            ParallelTransition fly = new ParallelTransition(move, scale, fade, rotate);
            fly.setOnFinished(e -> {
                animation.getChildren().remove(flyingCard);
                cardNode.setOpacity(1.0);
                ScaleTransition settle = new ScaleTransition(Duration.millis(90), cardNode);
                settle.setFromX(1.08);
                settle.setFromY(1.08);
                settle.setToX(1.0);
                settle.setToY(1.0);
                settle.play();
            });

            SequentialTransition seq = createAnimationSequence(i, 45L);
            seq.getChildren().add(fly);

            dealTransition.getChildren().add(seq);
        }

        dealTransition.setOnFinished(e -> {
            updatePileCounts();
            if (onFinished != null) onFinished.run();
        });

        dealTransition.play();
    }

    /**
     * Crea il nodo grafico e interattivo di una carta della mano.
     * <p>
     * Il nodo visualizza lo sprite della carta e, al click, attiva la selezione della carta
     * purché non sia in corso un'animazione e sia il turno del giocatore.
     * </p>
     *
     * @param card carta da rappresentare
     * @return {@link StackPane} contenente lo sprite e il gestore del click
     */
    private StackPane createCardNode(Card card) {
        StackPane node = new StackPane();
        node.getStyleClass().add("battle-view-hand-card");
        node.setAlignment(Pos.CENTER);

        Image sprite = SpriteManager.getCardSprite(card.getType());
        ImageView cardImageView = new ImageView(sprite);
        cardImageView.setFitWidth(84.0);
        cardImageView.setFitHeight(126.0);
        cardImageView.setPreserveRatio(true);
        cardImageView.setSmooth(false);
        cardImageView.getStyleClass().add("battle-view-hand-card-image");

        node.getChildren().add(cardImageView);
        node.setOnMouseClicked(_ -> {
            if (isAnimating || !battleManager.isPlayerTurn()) return;
            toggleCardSelection(card, node);
        });

        return node;
    }

    /**
     * Gestisce la selezione di una carta e aggiorna l'anteprima della combo.
     * <p>
     * Una carta selezionata viene aggiunta a {@link #selectedCards}, evidenziata
     * tramite CSS e sollevata visivamente; deselezionandola si ripristina la
     * posizione originale. L'aggiornamento della combo ricalcola danno e validità.
     * </p>
     *
     * @param card carta da selezionare o deselezionare
     * @param node nodo grafico associato alla carta
     */
    private void toggleCardSelection(Card card, StackPane node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(120), node);
        if (selectedCards.contains(card)) {
            selectedCards.remove(card);
            node.getStyleClass().remove("battle-view-hand-card-selected");
            tt.setToY(0);
        } else {
            selectedCards.add(card);
            node.getStyleClass().add("battle-view-hand-card-selected");
            tt.setToY(-22);
        }
        tt.play();
        updateComboPreview();
    }

    /**
     * Aggiorna anteprima e pulsante di attacco.
     * <p>
     * La mano viene valutata dal {@link BattleManager}: una combinazione valida
     * mostra il danno e il relativo nome (carta singola, combinazione di carte
     * dello stesso tipo o scala), mentre una combinazione non valida disabilita
     * l'attacco.
     */
    private void updateComboPreview() {
        if (selectedCards.isEmpty()) {
            comboLabel.setText("Nessuna Carta Selezionata");
            comboLabel.setStyle("-fx-border-color: #475569; -fx-text-fill: #94a3b8;");
            attackButton.setDisable(true);
            return;
        }

        int damage = battleManager.getPlayerHand().evaluateHand(selectedCards);
        boolean isValid = damage > 0;

        if (isValid) {
            String comboName;
            int comboSize = selectedCards.size();
            if (comboSize == 1) {
                comboName = "Singola (" + damage + " DMG)";
            } else if (isAllSameCardType(selectedCards)) {
                comboName = switch (comboSize) {
                    case 2 -> "Coppia! (" + damage + " DMG)";
                    case 3 -> "Tris! (" + damage + " DMG)";
                    case 4 -> "Poker! (" + damage + " DMG)";
                    default -> "";
                };
            } else {
                comboName = "Scala (" + comboSize + " carte)! (" + damage + " DMG)";
            }
            comboLabel.setText("Combo: " + comboName);
            comboLabel.setStyle("-fx-border-color: #22d3ee; -fx-text-fill: #67e8f9;");
            attackButton.setDisable(!battleManager.isPlayerTurn());
        } else {
            comboLabel.setText("Combinazione non valida (0 DMG)");
            comboLabel.setStyle("-fx-border-color: #ef4444; -fx-text-fill: #fca5a5;");
            attackButton.setDisable(true);
        }
    }

    /**
     * Verifica se tutte le carte della lista hanno lo stesso tipo.
     *
     * @param cards carte da confrontare
     * @return {@code true} se la lista non è vuota e ogni carta ha il tipo della prima
     */
    private boolean isAllSameCardType(List<Card> cards) {
        if (cards.isEmpty()) return false;
        var first = cards.getFirst().getType();
        for (Card c : cards) {
            if (c.getType() != first) return false;
        }
        return true;
    }

    /**
     * Crea uno sprite di una carta per un'animazione nella vista.
     *
     * @param type tipo della carta da rappresentare
     * @param width larghezza iniziale dello sprite
     * @param height altezza iniziale dello sprite
     * @param styleClass classe CSS da applicare allo sprite
     * @param x coordinata X iniziale nella vista
     * @param y coordinata Y iniziale nella vista
     * @param opacity opacità iniziale
     * @return sprite configurato e posizionato
     */
    private ImageView createCardForAnimation(
            CardType type,
            double width,
            double height,
            String styleClass,
            double x,
            double y,
            double opacity) {
        ImageView card = new ImageView(SpriteManager.getCardSprite(type));
        card.setFitWidth(width);
        card.setFitHeight(height);
        card.setPreserveRatio(true);
        card.setSmooth(false);
        card.getStyleClass().add(styleClass);
        card.setLayoutX(x);
        card.setLayoutY(y);
        card.setOpacity(opacity);
        return card;
    }

    /**
     * Crea una sequenza con un ritardo passato per l'animazione di ingresso delle carte.
     *
     * @param index indice della carta nella sequenza
     * @param delayPerCard ritardo aggiunto per ogni posizione
     * @return sequenza pronta a ricevere l'animazione principale
     */
    private SequentialTransition createAnimationSequence(int index, long delayPerCard) {
        SequentialTransition sequence = new SequentialTransition();
        if (index > 0) {
            sequence.getChildren().add(new PauseTransition(Duration.millis(index * delayPerCard)));
        }
        return sequence;
    }

    /**
     * Aggiorna indicatori, messaggi e controlli in base al turno corrente.
     * <p>
     * Durante il turno del giocatore abilita la conclusione del turno e aggiorna
     * l'anteprima della combo; durante quello nemico disabilita le azioni manuali
     * e mostra il messaggio relativo all'attacco in preparazione.
     * </p>
     */
    private void updateTurnInfo() {
        if (battleManager.isPlayerTurn()) {
            turnLabel.setText("Tuo Turno");
            turnLabel.getStyleClass().setAll("battle-view-turn-label", "battle-view-turn-player");
            statusLabel.setText("Tocca a te! Seleziona delle carte per formare una combo e attacca o termina il turno.");
            endTurnButton.setDisable(false);
            updateComboPreview();
        } else {
            turnLabel.setText("Turno Nemico");
            turnLabel.getStyleClass().setAll("battle-view-turn-label", "battle-view-turn-enemy");
            statusLabel.setText(enemy.getName() + " sta pianificando il suo attacco...");
            attackButton.setDisable(true);
            endTurnButton.setDisable(true);
        }
    }

    /**
     * Gestisce il click sul pulsante {@code Attacca}.
     * <p>
     * Valida il contesto del comando, blocca i controlli concorrenti, delega a
     * {@link BattleManager#playCombo(List)} l'applicazione della combo e avvia
     * l'animazione. Al termine sincronizza statistiche, pile e mano; se la
     * battaglia è terminata avvia la gestione del risultato.
     * </p>
     */
    @FXML
    private void onAttackButtonClicked() {
        if (isAnimating || selectedCards.isEmpty() || !battleManager.isPlayerTurn()) return;

        isAnimating = true;
        attackButton.setDisable(true);
        endTurnButton.setDisable(true);

        List<Card> cardsToPlay = new ArrayList<>(selectedCards);
        int damage = battleManager.getPlayerHand().evaluateHand(cardsToPlay);

        boolean success = battleManager.playCombo(cardsToPlay);
        if (!success) {
            isAnimating = false;
            updateComboPreview();
            return;
        }

        statusLabel.setText("Hai sferrato un attacco da " + damage + " danni!");

        playCardAttackAnimation(cardsToPlay, damage, () -> {
            updateEntitiesStats();
            updatePileCounts();
            renderHand();
            updateComboPreview();

            if (battleManager.isBattleOver()) {
                handleBattleEnd();
            } else {
                isAnimating = false;
                endTurnButton.setDisable(false);
            }
        });
    }

    /**
     * Anima l'attacco con le carte selezionate.
     * <p>
     * Le carte volano dalla mano al nemico, provocano scuotimento e popup del
     * danno, quindi proseguono verso la pila degli scarti. Se i nodi grafici o
     * le coordinate della scena non sono disponibili, usa l'animazione semplificata
     * del giocatore.
     * </p>
     *
     * @param cardsToPlay carte utilizzate nell'attacco
     * @param damage danno inflitto al nemico
     * @param onFinished callback invocato al termine dell'animazione
     */
    private void playCardAttackAnimation(List<Card> cardsToPlay, int damage, Runnable onFinished) {
        if (animation == null || enemySprite == null || cardsToPlay.isEmpty()) {
            playPlayerAttackAnimation(damage, onFinished);
            return;
        }

        Bounds enemySceneBounds = enemySprite.localToScene(enemySprite.getBoundsInLocal());
        Bounds discardSceneBounds = (discardPile != null) ? discardPile.localToScene(discardPile.getBoundsInLocal()) : null;

        if (enemySceneBounds == null || animation.getScene() == null) {
            playPlayerAttackAnimation(damage, onFinished);
            return;
        }

        Bounds enemyInAnim = animation.sceneToLocal(enemySceneBounds);
        double targetX = enemyInAnim.getMinX() + (enemyInAnim.getWidth() / 2.0);
        double targetY = enemyInAnim.getMinY() + (enemyInAnim.getHeight() / 2.0);

        double discardX;
        double discardY;
        if (discardSceneBounds != null) {
            Bounds discardInAnim = animation.sceneToLocal(discardSceneBounds);
            discardX = discardInAnim.getMinX() + (discardInAnim.getWidth() / 2.0);
            discardY = discardInAnim.getMinY() + (discardInAnim.getHeight() / 2.0);
        } else {
            discardX = animation.getWidth() - 100;
            discardY = animation.getHeight() - 100;
        }

        //Scatto del giocatore in avanti
        TranslateTransition pLunge = new TranslateTransition(Duration.millis(160), playerSprite);
        pLunge.setByX(30);
        pLunge.setAutoReverse(true);
        pLunge.setCycleCount(2);
        pLunge.play();

        ParallelTransition allCardsTransition = new ParallelTransition();

        for (int i = 0; i < cardsToPlay.size(); i++) {
            Card card = cardsToPlay.get(i);
            StackPane cardNode = cardNodes.get(card);

            double startX;
            double startY;
            if (cardNode != null && cardNode.getScene() != null) {
                Bounds cardBounds = animation.sceneToLocal(cardNode.localToScene(cardNode.getBoundsInLocal()));
                startX = cardBounds.getMinX();
                startY = cardBounds.getMinY();
                cardNode.setOpacity(0.0);
            } else {
                startX = 350.0 + (i * 30.0);
                startY = 480.0;
            }

            ImageView flyingCard = createCardForAnimation(card.getType(), 76.0, 114.0, "flying-card-attack", startX, startY, 1.0);
            animation.getChildren().add(flyingCard);

            // Fase 1: Volo verso il nemico
            TranslateTransition flyToEnemy = new TranslateTransition(Duration.millis(260), flyingCard);
            flyToEnemy.setToX(targetX - startX - 38);
            flyToEnemy.setToY(targetY - startY - 57);
            flyToEnemy.setInterpolator(Interpolator.EASE_IN);

            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(260), flyingCard);
            scaleUp.setToX(1.15);
            scaleUp.setToY(1.15);

            RotateTransition spin = new RotateTransition(Duration.millis(260), flyingCard);
            spin.setByAngle((i % 2 == 0 ? 1 : -1) * (15 + i * 5));

            ParallelTransition toEnemy = new ParallelTransition(flyToEnemy, scaleUp, spin);

            // Fase 2: Rimbalzo verso la pila degli scarti
            TranslateTransition flyToDiscard = new TranslateTransition(Duration.millis(320), flyingCard);
            flyToDiscard.setToX(discardX - startX - 38);
            flyToDiscard.setToY(discardY - startY - 57);
            flyToDiscard.setInterpolator(Interpolator.EASE_OUT);

            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(320), flyingCard);
            scaleDown.setToX(0.4);
            scaleDown.setToY(0.4);

            FadeTransition fade = new FadeTransition(Duration.millis(320), flyingCard);
            fade.setFromValue(1.0);
            fade.setToValue(0.1);

            RotateTransition spinDiscard = new RotateTransition(Duration.millis(320), flyingCard);
            spinDiscard.setByAngle(180);

            ParallelTransition toDiscard = new ParallelTransition(flyToDiscard, scaleDown, fade, spinDiscard);
            toDiscard.setOnFinished(e -> animation.getChildren().remove(flyingCard));

            SequentialTransition singleCardSeq = createAnimationSequence(i, 80L);
            singleCardSeq.getChildren().addAll(toEnemy, toDiscard);

            allCardsTransition.getChildren().add(singleCardSeq);
        }

        // Shake e danno sul nemico all'impatto delle carte
        PauseTransition hitDelay = new PauseTransition(Duration.millis(260));
        hitDelay.setOnFinished(e -> {
            showDamagePopup(enemyDamage, "-" + damage);
            TranslateTransition eShake = new TranslateTransition(Duration.millis(50), enemySprite);
            eShake.setByX(12);
            eShake.setAutoReverse(true);
            eShake.setCycleCount(6);
            eShake.setOnFinished(ev -> enemySprite.setTranslateX(0));
            eShake.play();
        });
        hitDelay.play();

        allCardsTransition.setOnFinished(e -> {
            if (onFinished != null) onFinished.run();
        });

        allCardsTransition.play();
    }

    /**
     * Anima lo scarto delle carte rimaste nella mano.
     * <p>
     * Ogni carta viene resa trasparente nella mano e una copia grafica vola verso
     * la pila degli scarti. Al termine la mano visuale viene svuotata, la pila
     * viene evidenziata e i contatori vengono aggiornati. Se l'animazione non
     * può essere eseguita, la pulizia avviene immediatamente.
     * </p>
     *
     * @param onFinished callback invocato dopo la pulizia della mano
     */
    private void playHandToDiscardAnimation(Runnable onFinished) {
        List<Card> currentHand = new ArrayList<>(battleManager.getPlayerHand().getCards());
        if (currentHand.isEmpty() || animation == null || discardPile == null || animation.getScene() == null) {
            cards.getChildren().clear();
            cardNodes.clear();
            selectedCards.clear();
            if (onFinished != null) onFinished.run();
            return;
        }

        Bounds discardSceneBounds = discardPile.localToScene(discardPile.getBoundsInLocal());
        if (discardSceneBounds == null) {
            cards.getChildren().clear();
            cardNodes.clear();
            selectedCards.clear();
            if (onFinished != null) onFinished.run();
            return;
        }

        Bounds discardInAnim = animation.sceneToLocal(discardSceneBounds);
        double discardX = discardInAnim.getMinX() + (discardInAnim.getWidth() / 2.0);
        double discardY = discardInAnim.getMinY() + (discardInAnim.getHeight() / 2.0);

        ParallelTransition discardTransition = new ParallelTransition();

        for (int i = 0; i < currentHand.size(); i++) {
            Card card = currentHand.get(i);
            StackPane cardNode = cardNodes.get(card);

            double startX;
            double startY;
            if (cardNode != null && cardNode.getScene() != null) {
                Bounds cardBounds = animation.sceneToLocal(cardNode.localToScene(cardNode.getBoundsInLocal()));
                startX = cardBounds.getMinX();
                startY = cardBounds.getMinY();
                cardNode.setOpacity(0.0);
            } else {
                startX = 350.0 + (i * 40.0);
                startY = 480.0;
            }

            ImageView flyingCard = createCardForAnimation(card.getType(), 76.0, 114.0, "flying-card-sprite", startX, startY, 1.0);
            animation.getChildren().add(flyingCard);

            TranslateTransition move = new TranslateTransition(Duration.millis(240), flyingCard);
            move.setToX(discardX - startX - 38);
            move.setToY(discardY - startY - 57);
            move.setInterpolator(Interpolator.EASE_IN);

            ScaleTransition scale = new ScaleTransition(Duration.millis(240), flyingCard);
            scale.setToX(0.4);
            scale.setToY(0.4);

            FadeTransition fade = new FadeTransition(Duration.millis(240), flyingCard);
            fade.setFromValue(1.0);
            fade.setToValue(0.2);

            RotateTransition rotate = new RotateTransition(Duration.millis(240), flyingCard);
            rotate.setByAngle(i % 2 == 0 ? 60 : -60);

            ParallelTransition fly = new ParallelTransition(move, scale, fade, rotate);
            fly.setOnFinished(e -> animation.getChildren().remove(flyingCard));

            SequentialTransition seq = createAnimationSequence(i, 30L);
            seq.getChildren().add(fly);
            discardTransition.getChildren().add(seq);
        }

        discardTransition.setOnFinished(e -> {
            cards.getChildren().clear();
            cardNodes.clear();
            selectedCards.clear();
            updatePileCounts();
            if (onFinished != null) onFinished.run();
        });

        discardTransition.play();
    }

    /**
     * Gestisce il click sul pulsante {@code Fine Turno}.
     * <p>
     * Disabilita i controlli, scarta animatamente la mano e prepara il turno
     * nemico.
     * </p>
     */
    @FXML
    private void onEndTurnButtonClicked() {
        if (isAnimating || !battleManager.isPlayerTurn()) return;

        isAnimating = true;
        attackButton.setDisable(true);
        endTurnButton.setDisable(true);
        selectedCards.clear();
        updateComboPreview();

        statusLabel.setText("Scarto della mano...");
        playHandToDiscardAnimation(() -> {
            turnLabel.setText("Turno Nemico");
            turnLabel.getStyleClass().setAll("battle-view-turn-label", "battle-view-turn-enemy");
            statusLabel.setText(enemy.getName() + " ti attacca!");
            PauseTransition enemyDelay = new PauseTransition(Duration.millis(350));
            enemyDelay.setOnFinished(e -> playEnemyAttackSequence());
            enemyDelay.play();
        });
    }

    /**
     * Esegue la sequenza completa del turno nemico.
     * <p>
     * Mostra l'attacco, applica poi le regole del turno tramite
     * {@link BattleManager#endTurn()} e aggiorna statistiche e pile. Se il mazzo
     * viene rimescolato, avvia l'animazione della UI. Se la battaglia continua,
     * distribuisce la nuova mano e riabilita i controlli.
     * </p>
     */
    private void playEnemyAttackSequence() {
        int enemyDamage = enemy.getType().getDamage();

        boolean reshuffled = battleManager.endTurn();
        playEnemyAttackAnimation(enemyDamage, () -> {
            updateEntitiesStats();
            updatePileCounts();

            if (battleManager.isBattleOver()) {
                handleBattleEnd();
            } else {
                if (reshuffled) {
                    triggerShuffleAnimation();
                }
                // Distribuisce la nuova mano animata dalla Draw Pile
                renderHand(() -> {
                    updateTurnInfo();
                    isAnimating = false;
                    endTurnButton.setDisable(false);
                });
            }
        });
    }

    /**
     * Anima l'attacco semplificato del giocatore.
     * <p>
     * Il giocatore scatta in avanti, il nemico viene scosso e viene mostrato il
     * popup del danno; il callback viene invocato al termine dello scuotimento.
     * </p>
     *
     * @param damage danno da visualizzare sul nemico
     * @param onFinished callback opzionale eseguito al termine
     */
    private void playPlayerAttackAnimation(int damage, Runnable onFinished) {
        // Scatto giocatore
        TranslateTransition pLunge = new TranslateTransition(Duration.millis(140), playerSprite);
        pLunge.setByX(35);
        pLunge.setAutoReverse(true);
        pLunge.setCycleCount(2);

        // Shake nemico
        TranslateTransition eShake = new TranslateTransition(Duration.millis(60), enemySprite);
        eShake.setByX(12);
        eShake.setAutoReverse(true);
        eShake.setCycleCount(6);

        showDamagePopup(enemyDamage, "-" + damage);

        pLunge.setOnFinished(e -> {
            eShake.play();
        });

        eShake.setOnFinished(e -> {
            enemySprite.setTranslateX(0);
            if (onFinished != null) onFinished.run();
        });

        pLunge.play();
    }

    /**
     * Anima l'attacco del nemico contro il giocatore.
     * <p>
     * Il nemico scatta verso sinistra, il giocatore viene scosso e il danno viene
     * mostrato tramite popup; il callback segnala la fine dell'effetto visivo.
     * </p>
     *
     * @param damage danno da visualizzare sul giocatore
     * @param onFinished callback opzionale eseguito al termine
     */
    private void playEnemyAttackAnimation(int damage, Runnable onFinished) {
        // Scatto nemico
        TranslateTransition eLunge = new TranslateTransition(Duration.millis(140), enemySprite);
        eLunge.setByX(-35);
        eLunge.setAutoReverse(true);
        eLunge.setCycleCount(2);

        // Shake giocatore
        TranslateTransition pShake = new TranslateTransition(Duration.millis(60), playerSprite);
        pShake.setByX(-12);
        pShake.setAutoReverse(true);
        pShake.setCycleCount(6);

        showDamagePopup(playerDamage, "-" + damage);

        eLunge.setOnFinished(e -> pShake.play());

        pShake.setOnFinished(e -> {
            playerSprite.setTranslateX(0);
            if (onFinished != null) onFinished.run();
        });

        eLunge.play();
    }

    /**
     * Mostra un popup di danno fluttuante con dissolvenza.
     * <p>
     * L'etichetta viene resa visibile, spostata verso l'alto e dissolta
     * simultaneamente; al termine dell'animazione viene nascosta.
     * </p>
     *
     * @param popup etichetta da animare
     * @param text testo da visualizzare
     */
    private void showDamagePopup(Label popup, String text) {
        popup.setText(text);
        popup.setOpacity(1.0);
        popup.setTranslateY(0);
        popup.setVisible(true);

        TranslateTransition tt = new TranslateTransition(Duration.millis(600), popup);
        tt.setByY(-28);

        FadeTransition ft = new FadeTransition(Duration.millis(600), popup);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);

        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.setOnFinished(e -> popup.setVisible(false));
        pt.play();
    }

    /**
     * Anima il rimescolamento della pila degli scarti nella pila di pesca.
     * <p>
     * Visualizza una cascata di carte lungo traiettorie curve tra le due pile,
     * aggiorna lo stato dell'HUD e segnala il completamento del rimescolamento.
     * L'esecuzione viene posticipata al thread JavaFX; in assenza
     * di coordinate valide viene mantenuto almeno l'aggiornamento dei contatori.
     * </p>
     */
    private void triggerShuffleAnimation() {
        Platform.runLater(() -> {
            statusLabel.setText("Mazzo esaurito! Rimescolamento degli scarti nel mazzo...");

            if (discardPile == null || drawPile == null || animation == null) {
                updatePileCounts();
                return;
            }
            if (discardPile.getScene() == null || drawPile.getScene() == null) {
                updatePileCounts();
                return;
            }

            // Calcola le posizioni globali delle due pile
            Bounds discardBounds = discardPile.localToScene(discardPile.getBoundsInLocal());
            Bounds drawBounds = drawPile.localToScene(drawPile.getBoundsInLocal());
            if (discardBounds == null || drawBounds == null) {
                updatePileCounts();
                return;
            }

            Bounds discardInAnim = animation.sceneToLocal(discardBounds);
            Bounds drawInAnim = animation.sceneToLocal(drawBounds);

            double startX = discardInAnim.getMinX() + (discardInAnim.getWidth() / 2.0);
            double startY = discardInAnim.getMinY() + (discardInAnim.getHeight() / 2.0);
            double endX = drawInAnim.getMinX() + (drawInAnim.getWidth() / 2.0);
            double endY = drawInAnim.getMinY() + (drawInAnim.getHeight() / 2.0);

            ParallelTransition cascadeTransition = new ParallelTransition();
            CardType[] sampleTypes = {CardType.ACE, CardType.KING, CardType.QUEEN, CardType.JACK, CardType.JOKER};

            int cardCount = 5;
            for (int i = 0; i < cardCount; i++) {
                CardType type = sampleTypes[i % sampleTypes.length];
                ImageView flyingCard = createCardForAnimation(
                        type, 48.0, 72.0, "flying-card-sprite",
                        0.0, 0.0, 0.0);

                animation.getChildren().add(flyingCard);

                // Traiettoria ad arco parabolico verso l'alto sopra la mano del giocatore
                Path arcPath = new Path();
                arcPath.getElements().add(new MoveTo(startX, startY));
                double controlX = (startX + endX) / 2.0 + ((i % 2 == 0 ? 1 : -1) * 12.0);
                double controlY = Math.min(startY, endY) - (95.0 + (i % 3) * 15.0);
                arcPath.getElements().add(new QuadCurveTo(controlX, controlY, endX, endY));

                PathTransition pathMove = new PathTransition(Duration.millis(260), arcPath, flyingCard);
                pathMove.setInterpolator(Interpolator.EASE_BOTH);

                // Rotazione dinamica durante il volo
                RotateTransition rotate = new RotateTransition(Duration.millis(260), flyingCard);
                rotate.setFromAngle(i % 2 == 0 ? 30 : -25);
                rotate.setToAngle(i % 2 == 0 ? -180 : 180);

                // Scala dinamica: parte compatta ed entra nel mazzo
                ScaleTransition scale = new ScaleTransition(Duration.millis(260), flyingCard);
                scale.setFromX(0.75);
                scale.setFromY(0.75);
                scale.setToX(0.5);
                scale.setToY(0.5);

                FadeTransition fade = new FadeTransition(Duration.millis(260), flyingCard);
                fade.setFromValue(1.0);
                fade.setToValue(0.3);

                ParallelTransition cardFly = new ParallelTransition(pathMove, rotate, scale, fade);
                cardFly.setOnFinished(e -> animation.getChildren().remove(flyingCard));

                SequentialTransition cardSeq = createAnimationSequence(i, 35L);
                cardSeq.getChildren().add(cardFly);

                cascadeTransition.getChildren().add(cardSeq);
            }

            cascadeTransition.setOnFinished(e -> {
                updatePileCounts();
                statusLabel.setText("Mazzo rimescolato!");
            });

            cascadeTransition.play();
        });
    }

    /**
     * Gestisce la conclusione della battaglia e apre la vista del risultato.
     * <p>
     * Disabilita i controlli e determina l'esito tramite
     * {@link BattleManager#isPlayerVictory()}. L'entità sconfitta sbiadisce e,
     * al termine dell'animazione, passa a {@code BattleResultView.fxml}.
     * </p>
     */
    private void handleBattleEnd() {
        isAnimating = true;
        attackButton.setDisable(true);
        endTurnButton.setDisable(true);

        boolean victory = battleManager.isPlayerVictory();

        if (victory) {
            FadeTransition enemyFade = new FadeTransition(Duration.millis(600), enemySprite);
            enemyFade.setToValue(0.0);
            enemyFade.setOnFinished(e -> SceneManager.getInstance().switchScene("/fxml/BattleResultView.fxml"));
            enemyFade.play();
        } else {
            FadeTransition playerFade = new FadeTransition(Duration.millis(600), playerSprite);
            playerFade.setToValue(0.2);
            playerFade.setOnFinished(e -> SceneManager.getInstance().switchScene("/fxml/BattleResultView.fxml"));
            playerFade.play();
        }
    }
}
