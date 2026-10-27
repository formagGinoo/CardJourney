package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.game.SpriteManager;
import it.unicam.cs.mpgc.rpg130809.model.game.SoundManager;
import it.unicam.cs.mpgc.rpg130809.model.map.Position;
import it.unicam.cs.mpgc.rpg130809.model.map.GameMap;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
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
 * Controller per la vista della mappa ({@code MapView.fxml}).
 * <p>
 * Coordina la rappresentazione grafica della mappa con lo stato mantenuto da
 * {@link GameManager}: visualizza lo sfondo, le tile, il giocatore e le entità
 * presenti, aggiorna le info del giocatore e gestisce i comandi da tastiera
 * e i pulsanti della vista.
 * </p>
 * <p>
 * Il controller non contiene la logica di gioco della mappa. Prima di muovere
 * il giocatore delega la validazione a {@link GameMap}; in caso di incontro
 * delega invece la gestione della battaglia a {@link GameManager} e la transizione di scena a {@link SceneManager}.
 * </p>
 */
public class MapViewController {

    /**
     * Crea il controller della mappa, istanziato da JavaFX tramite il file FXML.
     */
    public MapViewController() {
    }

    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImage;
    @FXML private Pane mapPane;
    @FXML private Label mapName;
    @FXML private Label hpLabel;
    @FXML private Label goldLabel;
    @FXML private Button deckButton;

    private GameMap map;
    private Player player;

    /**
     * Inizializza la vista dopo che JavaFX ha caricato e iniettato i nodi definiti
     * nel file FXML.
     * <p>
     * Recupera il giocatore e la mappa attiva dal {@link GameManager}, configura
     * lo sfondo, aggiorna lo HUD, disegna le entità
     * iniziali e infine abilita la gestione dei comandi da tastiera. L'ordine
     * garantisce che la mappa sia disponibile prima delle operazioni di rendering.
     * </p>
     */
    @FXML
    public void initialize() {
        this.player = GameManager.getInstance().getPlayer();
        this.map = GameManager.getInstance().getActiveGameMap();

        SoundManager.getInstance().playMusic(map.getMapType().getSoundtrackPath());
        setupBackground();
        updateHud();
        renderMap();
        setupKeyboardControls();
    }

    /**
     * Imposta lo sfondo della vista in base al tipo della mappa attiva.
     * <p>
     * Il percorso della risorsa viene fornito dal tipo di mappa e l'immagine
     * risultante viene assegnata all'{@link ImageView} già definito nel FXML.
     * </p>
     */
    private void setupBackground() {
        String path = map.getMapType().getMapBackgroundPath();
        backgroundImage.setImage(new Image(path));
    }

    /**
     * Registra il gestore dei comandi da tastiera della vista.
     * <p>
     * Rende il pannello principale focalizzabile, gli assegna il focus al
     * successivo ciclo dell'application thread JavaFX e collega gli eventi di
     * pressione dei tasti a {@link #handleKey(KeyEvent)}. Sono supportati sia
     * i tasti freccia e WASD per il movimento.
     * </p>
     */
    private void setupKeyboardControls() {
        rootPane.setFocusTraversable(true);
        Platform.runLater(rootPane::requestFocus);
        rootPane.setOnKeyPressed(this::handleKey);
    }

    /**
     * Traduce un evento di tastiera in un'azione della vista.
     * <p>
     * I tasti freccia e WASD producono uno spostamento di una tile. L'evento viene consumato
     * anche quando il tasto non corrisponde a un'azione, impedendo che venga
     * propagato ad altri controlli della scena.
     * </p>
     *
     * @param event evento della tastiera ricevuto dal pannello principale
     */
    private void handleKey(KeyEvent event) {
        KeyCode code = event.getCode();
        switch (code) {
            case UP, W -> tryMove(0, -1);
            case DOWN, S -> tryMove(0, 1);
            case LEFT, A -> tryMove(-1, 0);
            case RIGHT, D -> tryMove(1, 0);
            default -> {}
        }
        event.consume();
    }

    /**
     * Tenta di spostare il giocatore di una tile nella direzione indicata.
     * <p>
     * Calcola la destinazione a partire dalla posizione corrente e lascia a
     * {@link GameMap#canMoveTo(Position)} la verifica della validità. Una
     * destinazione occupata da un nemico avvia la battaglia senza spostare il
     * giocatore. In caso contrario aggiorna la posizione e gestisce, se presenti,
     * il negozio o l'uscita; infine ridisegna la mappa e aggiorna l'HUD.
     * </p>
     *
     * @param dx variazione orizzontale della posizione -1, 0 o 1
     * @param dy variazione verticale della posizione -1, 0 o 1
     */
    private void tryMove(int dx, int dy) {
        Position currentPos = map.getPlayerPosition();
        Position targetPos = new Position(currentPos.getX() + dx, currentPos.getY() + dy);

        if (!map.canMoveTo(targetPos)) return;
        if (targetPos.equals(map.getExitPosition()) && !map.isBossDefeated()) {
            return;
        }
        Enemy enemy = map.getEnemyAt(targetPos);
        if (enemy != null) {
            handleEnemyEncounter(targetPos, enemy);
            return;
        }
        map.movePlayer(targetPos);

        if (targetPos.equals(map.getShopPosition())) {
            handleShopEncounter();
            return;
        }
        if (targetPos.equals(map.getExitPosition())) {
            handleExitEncounter();
            return;
        }

        renderMap();
        updateHud();
    }

    /**
     * Avvia l'incontro con un nemico e apre la schermata di battaglia.
     * <p>
     * Salva nel {@link GameManager} la posizione dell'incontro e il nemico
     * coinvolto, quindi delega a {@link SceneManager} il caricamento della vista
     * di battaglia.
     * </p>
     *
     * @param pos posizione sulla mappa in cui è stato trovato il nemico
     * @param enemy istanza del nemico con cui il giocatore deve combattere
     */
    private void handleEnemyEncounter(Position pos, Enemy enemy) {
        GameManager.getInstance().startBattle(pos, enemy);
        SceneManager.getInstance().switchScene("/fxml/BattleView.fxml");
    }

    /**
     * Gestisce l'ingresso del giocatore nel negozio della mappa corrente.
     * <p>
     * Apre la schermata dello shop. Il catalogo è creato dalla vista stessa e
     * non viene memorizzato nel {@link GameManager}, perché lo shop è una
     * schermata temporanea. Al ritorno, {@code MapView} ricarica giocatore e
     * mappa dallo stato persistente.
     * </p>
     */
    private void handleShopEncounter() {
        SceneManager.getInstance().switchScene("/fxml/ShopView.fxml");
    }

    /**
     * Completa la zona quando il giocatore raggiunge la posizione di uscita.
     * <p>
     * Aggiorna la progressione del giocatore con
     * {@link Player#advanceToNextMap()}, rimuove la mappa corrente dal
     * {@link GameManager} e torna alla schermata di selezione delle mappe.
     * </p>
     */
    private void handleExitEncounter() {
        System.out.println("Zona completata! Avanzamento alla prossima zona...");
        player.advanceToNextMap();
        GameManager.getInstance().setActiveGameMap(null);
        SceneManager.getInstance().switchScene("/fxml/MapSelection.fxml");
    }

    /**
     * Ricostruisce il contenuto visibile del pannello della mappa.
     * <p>
     * Svuota i nodi precedenti e disegna nell'ordine il negozio, l'uscita,
     * i nemici e il giocatore. L'ordine determina il livello di sovrapposizione:
     * il giocatore resta in primo piano rispetto alle altre entità.
     * </p>
     */
    private void renderMap() {
        mapPane.getChildren().clear();

        Position shop = map.getShopPosition();
        if (shop != null) {
            addBadge(SpriteManager.getShopSprite(), shop);
        }

        Position exit = map.getExitPosition();
        if (exit != null) {
            addBadge(SpriteManager.getExitSprite(), exit);
        }

        for (var entry : map.getEnemies().entrySet()) {
            Position pos = entry.getKey();
            Enemy enemy = entry.getValue();
            addBadge(SpriteManager.getEnemySprite(enemy.getType()), pos);
        }

        Position playerPos = map.getPlayerPosition();
        if (playerPos != null) {
            addBadge(SpriteManager.getPlayerSprite(), playerPos);
        }
    }

    /**
     * Posiziona il badge di un'entità sulla griglia della mappa.
     * <p>
     * Le coordinate logiche della {@link Position} vengono convertite in pixel
     * moltiplicandole per {@code TILE_SIZE}.
     * </p>
     *
     * @param sprite immagine da visualizzare nel badge
     * @param position posizione logica dell'entità sulla mappa
     */
    private void addBadge(Image sprite, Position position) {
        StackPane badge = createBadge(sprite);
        badge.setLayoutX(position.getX() * TILE_SIZE);
        badge.setLayoutY(position.getY() * TILE_SIZE);
        mapPane.getChildren().add(badge);
    }

    /**
     * Crea un badge di un'entità sul pannello della mappa.
     * <p>
     * Inserisce lo sprite in un {@link ImageView} centrato all'interno di uno
     * {@link StackPane}. L'immagine viene ridimensionata a 64x64 pixel per farla
     * entrare nella tile associata alla posizione.
     * </p>
     *
     * @param sprite immagine da visualizzare nel badge
     * @return contenitore pronto per essere aggiunto al pannello della mappa
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
     * Sincronizza l'hud con lo stato corrente del giocatore e della mappa.
     * <p>
     * Aggiorna salute, oro e numero di carte del mazzo quando il giocatore è
     * disponibile; aggiorna inoltre il nome della mappa.
     * I controlli null evitano di aggiornare dati non ancora inizializzati durante il caricamento della vista.
     * </p>
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
     * Abbandona la vista corrente e torna alla selezione delle mappe.
     * <p>
     * La transizione viene delegata a {@link SceneManager}.
     * </p>
     */
    @FXML
    private void onBackToMap() {
        SceneManager.getInstance().switchScene("/fxml/MapSelection.fxml");
    }

    /**
     * Gestisce il click sul pulsante del mazzo.
     * <p>
     * Apre la schermata di visualizzazione delle carte possedute dal giocatore.
     * La vista è in sola lettura: al ritorno, {@code MapView} ricarica giocatore
     * e mappa dallo stato persistente del {@link GameManager}.
     * </p>
     */
    @FXML
    private void onDeckButtonClicked() {
        SceneManager.getInstance().switchScene("/fxml/DeckView.fxml");
    }
}
