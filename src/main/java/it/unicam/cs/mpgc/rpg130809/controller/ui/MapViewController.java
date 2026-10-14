package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.GameConstants;
import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.game.SpriteManager;
import it.unicam.cs.mpgc.rpg130809.model.map.Position;
import it.unicam.cs.mpgc.rpg130809.model.map.TileType;
import it.unicam.cs.mpgc.rpg130809.model.map.GameMap;
import it.unicam.cs.mpgc.rpg130809.model.player.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import static it.unicam.cs.mpgc.rpg130809.GameConstants.TILE_SIZE;

/**
 * Controller per la visualizzazione e interazione con una mappa di zona.
 */
public class MapViewController {
    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImage;
    @FXML private GridPane mapGrid;
    @FXML private Label mapName;
    @FXML private Label hpLabel;
    @FXML private Label goldLabel;
    @FXML private Button deckButton;
    @FXML private Button gridButton;

    private GameMap map;
    private Player player;
    private boolean showGrid = false;

    /**
     * Inizializza il controller, configurando la griglia, lo sfondo, l'HUD e i controlli da tastiera.
     * Viene invocato automaticamente da JavaFX dopo il caricamento del FXML.
     */
    @FXML
    public void initialize() {
        this.player = GameManager.getInstance().getPlayer();
        this.map = GameManager.getInstance().getActiveGameMap();

        setupGrid();
        setupBackground();
        updateHud();
        renderMap();
        setupKeyboardControls();
    }

    /**
     * Configura i vincoli di riga e colonna della griglia di gioco sulla base
     * delle dimensioni della mappa e della dimensione fissa delle tile.
     */
    private void setupGrid() {
        mapGrid.getColumnConstraints().clear();
        for (int i = 0; i < map.getWidth(); i++) {
            ColumnConstraints cc = new ColumnConstraints(GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
            mapGrid.getColumnConstraints().add(cc);
        }

        mapGrid.getRowConstraints().clear();
        for (int i = 0; i < map.getHeight(); i++) {
            RowConstraints rc = new RowConstraints(GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
            mapGrid.getRowConstraints().add(rc);
        }
    }

    /**
     * Imposta l'immagine di sfondo corrispondente alla mappa corrente.
     */
    private void setupBackground() {
        String url = map.getMapType().getMapBackgroundPath();
        backgroundImage.setImage(new Image(url));
    }

    /**
     * Inizializza i listener per i comandi da tastiera dell'utente.
     * Supporta sia i tasti freccia che WASD per il movimento, e F3 per il visualizzare la griglia.
     */
    private void setupKeyboardControls() {
        rootPane.setFocusTraversable(true);
        Platform.runLater(rootPane::requestFocus);
        rootPane.setOnKeyPressed(this::handleKeyPress);
    }

    /**
     * Gestisce l'evento di pressione tasti, mappando i tasti di movimento e le azioni speciali.
     *
     * @param event L'evento di tastiera catturato
     */
    private void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();
        switch (code) {
            case UP, W -> tryMove(0, -1);
            case DOWN, S -> tryMove(0, 1);
            case LEFT, A -> tryMove(-1, 0);
            case RIGHT, D -> tryMove(1, 0);
            case F3 -> onToggleGrid();
            default -> {}
        }
        event.consume();
    }

    /**
     * Tenta di spostare il giocatore nella direzione specificata (dx, dy).
     * Se la cella di destinazione contiene un nemico, avvia l'interazione con esso.
     * Se contiene il negozio o l'uscita, gestisce le relative interazioni.
     *
     * @param dx Spostamento orizzontale (-1, 0, +1)
     * @param dy Spostamento verticale (-1, 0, +1)
     */
    private void tryMove(int dx, int dy) {
        Position current = map.getPlayerPosition();
        if (current == null) return;

        Position target = new Position(current.getX() + dx, current.getY() + dy);
        if (!map.canMoveTo(target)) {
            return;
        }
        Enemy enemy = map.getEnemyAt(target);
        if (enemy != null) {
            handleEnemyEncounter(target, enemy);
            return;
        }
        map.movePlayer(target);
        if (target.equals(map.getShopPosition())) {
            handleShopEncounter();
        } else if (target.equals(map.getExitPosition())) {
            handleExitEncounter();
        }
        renderMap();
        updateHud();
    }

    /**
     * Gestisce l'interazione con un nemico sulla mappa.
     * Rimuove il nemico dalla mappa, sposta il giocatore e aggiorna la vista.
     *
     * @param pos   La posizione dell'incontro
     * @param enemy L'istanza del nemico con cui si sta interagendo
     */
    private void handleEnemyEncounter(Position pos, Enemy enemy) {
        System.out.println("Nemico: " + enemy.getType() + " a " + pos);
        // TODO: Gestione combattimento con il nemico
        map.removeEnemy(pos);
        map.movePlayer(pos);
        renderMap();
        updateHud();
    }

    /**
     * Gestisce l'interazione con il negozio.
     */
    private void handleShopEncounter() {
        System.out.println("Shop di: " + map.getMapType().getName());
        // TODO: Gestione interazione con il negozio
    }

    /**
     * Gestisce il completamento della mappa quando il giocatore raggiunge l'uscita.
     * Fa avanzare il giocatore alla mappa successiva e torna alla selezione delle mappe.
     */
    private void handleExitEncounter() {
        System.out.println("Zona completata! Avanzamento alla prossima zona...");
        player.advanceToNextMap();
        GameManager.getInstance().setActiveGameMap(null);
        SceneManager.getInstance().switchScene("/fxml/MapSelection.fxml");
    }

    /**
     * Renderizza l'intera griglia della mappa
     */
    private void renderMap() {
        mapGrid.getChildren().clear();
        if (showGrid) {
            renderGridOverlay();
        }

        Position shop = map.getShopPosition();
        if (shop != null) {
            mapGrid.add(createBadge(SpriteManager.getShopSprite()), shop.getX(), shop.getY());
        }

        Position exit = map.getExitPosition();
        if (exit != null) {
            mapGrid.add(createBadge(SpriteManager.getExitSprite()), exit.getX(), exit.getY());
        }

        for (var entry : map.getEnemies().entrySet()) {
            Position pos = entry.getKey();
            Enemy enemy = entry.getValue();
            mapGrid.add(createBadge(SpriteManager.getEnemySprite(enemy.getType())), pos.getX(), pos.getY());
        }

        Position playerPos = map.getPlayerPosition();
        if (playerPos != null) {
            mapGrid.add(createBadge(SpriteManager.getPlayerSprite()), playerPos.getX(), playerPos.getY());
        }
    }

    /**
     * Crea un badge visivo per un'entità sulla mappa.
     * Se lo sprite è disponibile, lo renderizza tramite {@link ImageView}.
     *
     * @param sprite          L'immagine dello sprite (può essere {@code null})
     * @return {@link StackPane} contenente lo sprite, pronto per essere aggiunto alla griglia
     */
    private StackPane createBadge(Image sprite) {
        StackPane badge = new StackPane();
        badge.setAlignment(Pos.CENTER);
        ImageView iv = new ImageView(sprite);
        iv.setFitWidth(64.0);
        iv.setFitHeight(64.0);
        iv.setPreserveRatio(true);
        badge.getChildren().add(iv);
        return badge;
    }

    /**
     * Renderizza le celle della griglia con evidenziazione grafica per il debug di ostacoli e percorsi calpestabili.
     */
    private void renderGridOverlay() {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Pane cell = new Pane();
                cell.setPrefSize(TILE_SIZE, TILE_SIZE);
                if (map.getTile(x, y) == TileType.OBSTACLE) {
                    cell.getStyleClass().add("grid-tile-obstacle");
                } else {
                    cell.getStyleClass().add("grid-tile-walkable");
                }
                mapGrid.add(cell, x, y);
            }
        }
    }

    /**
     * Aggiorna tutti gli indicatori dell'HUD: punti salute, oro, carte nel mazzo e nome della mappa.
     */
    private void updateHud() {
        if (player != null) {
            hpLabel.setText(player.getHealth() + "/" + player.getMaxHealth());
            goldLabel.setText(String.valueOf(player.getGold()));
            deckButton.setText("Mazzo (" + player.getMainDeck().size() + ")");
        }
        if (map != null && map.getMapType() != null) {
            mapName.setText(map.getMapType().getName());
        }
    }

    /**
     * Attiva o disattiva la visualizzazione della griglia.
     */
    @FXML
    private void onToggleGrid() {
        this.showGrid = !this.showGrid;
        gridButton.setText(showGrid ? "Griglia: ON" : "Griglia: OFF");
        renderMap();
        rootPane.requestFocus();
    }

    /**
     * Ritorna alla schermata di selezione delle mappe.
     */
    @FXML
    private void onBackToMap() {
        SceneManager.getInstance().switchScene("/fxml/MapSelection.fxml");
    }

    /**
     * Gestisce il click sul pulsante del mazzo per visualizzare le carte possedute dal giocatore.
     */
    @FXML
    private void onDeckButtonClicked() {
        System.out.println("Apertura visualizzazione mazzo: " + player.getMainDeck().size() + " carte.");
        //TODO: Implementare la visualizzazione del mazzo del giocatore
    }
}
