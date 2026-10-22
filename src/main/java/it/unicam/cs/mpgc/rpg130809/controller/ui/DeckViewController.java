package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.card.Card;
import it.unicam.cs.mpgc.rpg130809.model.card.CardType;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.game.SpriteManager;
import it.unicam.cs.mpgc.rpg130809.model.map.GameMap;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller della schermata di visualizzazione del mazzo ({@code DeckView.fxml}).
 * <p>
 * Mostra tutte le carte contenute nel <b>Main Deck</b> del giocatore, lette dal
 * {@link GameManager}. La schermata è in sola lettura:
 * non modifica lo stato del mazzo e serve solo a consultare la collezione
 * posseduta.
 * </p>
 * <p>
 * Le carte sono raggruppate per {@link CardType}: ogni tile rappresenta un tipo
 * distinto e mostra, tramite un badge, quante copie di quel tipo sono presenti
 * nel mazzo. L'ordinamento segue l'ordine delle costanti dell'enum, cioè dalla
 * carta più forte alla più debole.
 * </p>
 */
public class DeckViewController {

    /**
     * Crea il controller della vista del mazzo, istanziato da JavaFX tramite il file FXML.
     */
    public DeckViewController() {
    }

    @FXML private ImageView backgroundImage;
    @FXML private Label deckSubtitle;
    @FXML private FlowPane cardsPane;

    private Player player;
    private GameMap map;

    /**
     * Inizializza la vista: recupera giocatore e mappa attiva dal
     * {@link GameManager}, configura sfondo e icona, aggiorna l'HUD e costruisce
     * le tile delle carte del Main Deck.
     */
    @FXML
    public void initialize() {
        GameManager gameManager = GameManager.getInstance();
        this.player = gameManager.getPlayer();
        this.map = gameManager.getActiveGameMap();

        setupVisuals();
        updateHud();
        renderCards();
    }

    /**
     * Configura lo sfondo in base alla zona corrente.
     * <p>
     * L'icona della pila di pesca è definita direttamente nel file FXML. Se non
     * c'è una mappa attiva lo sfondo resta vuoto.
     * </p>
     */
    private void setupVisuals() {
        if (map != null && map.getMapType() != null) {
            backgroundImage.setImage(new Image(map.getMapType().getMapBackgroundPath()));
        }
    }

    /**
     * Costruisce le tile delle carte del mazzo principale.
     * <p>
     * Le carte vengono raggruppate per tipo contando le copie di ciascuno; se il
     * mazzo è vuoto mostra invece un messaggio di riserva al posto della griglia.
     * </p>
     */
    private void renderCards() {
        cardsPane.getChildren().clear();

        Map<CardType, Long> copies = countCopies(player.getMainDeck().getCards());

        for (var entry : copies.entrySet()) {
            cardsPane.getChildren().add(createCardNode(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Raggruppa le carte per tipo, contando le copie e mantenendo l'ordinamento
     * dell'enum {@link CardType} (dalla più forte alla più debole).
     *
     * @param cards le carte del mazzo da raggruppare
     * @return mappa tipo → numero di copie, nell'ordine di forza decrescente
     */
    private Map<CardType, Long> countCopies(List<Card> cards) {
        List<CardType> types = new ArrayList<>(cards.stream().map(Card::getType).toList());
        types.sort(Comparator.comparingInt(CardType::ordinal));

        Map<CardType, Long> copies = new LinkedHashMap<>();
        for (CardType type : types) {
            copies.merge(type, 1L, Long::sum);
        }
        return copies;
    }

    /**
     * Crea la tile grafica di un tipo di carta.
     * <p>
     * Riproduce lo stile delle tile dello shop: sprite della carta, badge con il
     * numero di copie (visibile solo per più di una copia), nome e danno base.
     * </p>
     *
     * @param type il tipo di carta da rappresentare
     * @param count il numero di copie possedute
     * @return il nodo pronto per essere aggiunto alla griglia
     */
    private Node createCardNode(CardType type, long count) {
        VBox tile = new VBox(4.0);
        tile.setAlignment(Pos.TOP_CENTER);
        tile.setPrefWidth(132.0);
        tile.setMinWidth(132.0);
        tile.setMaxWidth(132.0);
        tile.getStyleClass().add("deck-card");

        StackPane imageContainer = new StackPane();
        imageContainer.getStyleClass().add("deck-card-image");
        imageContainer.setMinHeight(96.0);
        imageContainer.setPrefHeight(96.0);
        imageContainer.setMaxHeight(96.0);
        imageContainer.getChildren().add(createCardIcon(type));

        if (count > 1) {
            Label countBadge = new Label("x" + count);
            countBadge.getStyleClass().add("deck-count-badge");
            StackPane.setAlignment(countBadge, Pos.TOP_RIGHT);
            StackPane.setMargin(countBadge, new Insets(2.0, 2.0, 0.0, 0.0));
            imageContainer.getChildren().add(countBadge);
        }

        Label name = new Label(type.getCardName());
        name.getStyleClass().add("deck-card-name");
        name.setWrapText(true);
        name.setAlignment(Pos.CENTER);
        name.setTextAlignment(TextAlignment.CENTER);
        name.setMinHeight(26.0);
        name.setMaxWidth(116.0);

        Label damage = new Label("Danno: " + type.getDamage());
        damage.getStyleClass().add("deck-card-damage");
        damage.setWrapText(true);
        damage.setAlignment(Pos.CENTER);
        damage.setTextAlignment(TextAlignment.CENTER);
        damage.setMinHeight(22.0);
        damage.setMaxWidth(116.0);

        tile.getChildren().addAll(imageContainer, name, damage);
        return tile;
    }

    /**
     * Crea l'icona della carta a partire dallo sprite del suo tipo.
     *
     * @param type il tipo della carta
     * @return il nodo dell'icona
     */
    private Node createCardIcon(CardType type) {
        Image sprite = SpriteManager.getCardSprite(type);
        ImageView imageView = new ImageView(sprite);
        imageView.setFitWidth(58.0);
        imageView.setFitHeight(87.0);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        imageView.getStyleClass().add("deck-card-sprite");
        return imageView;
    }

    /**
     * Sincronizza l'HUD con lo stato corrente del giocatore.
     * <p>
     * Il mazzo è persistente durante tutta la partita: il sottotitolo mostra il
     * numero totale di carte possedute, indipendentemente dalla mappa.
     * </p>
     */
    private void updateHud() {
        if (player != null) {
            deckSubtitle.setText(player.getMainDeck().size() + " carte nel mazzo principale");
        }
    }

    /**
     * Torna alla mappa corrente. La vista viene ricaricata dal {@link SceneManager}
     * leggendo lo stato persistente del {@link GameManager}.
     */
    @FXML
    private void onBackToMap() {
        SceneManager.getInstance().switchScene("/fxml/MapView.fxml");
    }
}
