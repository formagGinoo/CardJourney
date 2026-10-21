package it.unicam.cs.mpgc.rpg130809.controller.ui;

import it.unicam.cs.mpgc.rpg130809.controller.SceneManager;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
import it.unicam.cs.mpgc.rpg130809.model.game.GameManager;
import it.unicam.cs.mpgc.rpg130809.model.game.SpriteManager;
import it.unicam.cs.mpgc.rpg130809.model.map.GameMap;
import it.unicam.cs.mpgc.rpg130809.model.shop.CardShopItem;
import it.unicam.cs.mpgc.rpg130809.model.shop.HealthPotionShopItem;
import it.unicam.cs.mpgc.rpg130809.model.shop.PurchaseResult;
import it.unicam.cs.mpgc.rpg130809.model.shop.ShopFactory;
import it.unicam.cs.mpgc.rpg130809.model.shop.ShopItem;
import it.unicam.cs.mpgc.rpg130809.model.shop.ShopManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Controller della schermata dello shop ({@code ShopView.fxml}).
 * <p>
 * Lo shop è una schermata temporanea: il catalogo viene creato qui tramite
 * {@link ShopFactory} e non viene memorizzato nel {@link GameManager}. Gli unici
 * dati persistenti sono quelli del giocatore e della mappa, letti dal
 * {@link GameManager} come nelle altre viste.
 * </p>
 * <p>
 * La vista è guidata da {@link ShopItem}: carte e pozione vengono rappresentate
 * allo stesso modo, mentre l'aspetto e il comportamento specifico sono scelti in
 * base al tipo concreto dell'articolo.
 * </p>
 */
public class ShopViewController {

    /**
     * Crea il controller dello shop, istanziato da JavaFX tramite il file FXML.
     */
    public ShopViewController() {
    }

    @FXML private ImageView backgroundImage;
    @FXML private ImageView shopIcon;
    @FXML private Label shopSubtitle;
    @FXML private Label hpLabel;
    @FXML private Label goldLabel;
    @FXML private Label deckLabel;
    @FXML private Label statusLabel;
    @FXML private FlowPane itemsPane;

    private Player player;
    private GameMap map;
    private ShopManager shop;

    private final List<ItemView> itemViews = new ArrayList<>();

    /**
     * Inizializza la vista, crea il catalogo dello shop e costruisce le tile
     * degli articoli.
     */
    @FXML
    public void initialize() {
        GameManager gameManager = GameManager.getInstance();
        this.player = gameManager.getPlayer();
        this.map = gameManager.getActiveGameMap();
        this.shop = ShopFactory.createShop();

        setupVisuals();
        updateHud();
        renderItems();
        showStatus("Scegli un articolo da acquistare.", "shop-status-info");
    }

    /**
     * Configura sfondo, icona e intestazione in base alla zona corrente.
     */
    private void setupVisuals() {
        shopIcon.setImage(SpriteManager.getShopSprite());

        if (map != null && map.getMapType() != null) {
            backgroundImage.setImage(new Image(map.getMapType().getMapBackgroundPath()));
            shopSubtitle.setText("Mercante di " + map.getMapType().getName());
        } else {
            shopSubtitle.setText("Mercante itinerante");
        }
    }

    /**
     * Costruisce le tile di tutti gli articoli in vendita.
     * <p>
     * La pozione viene mostrata per prima, seguita dalle carte ordinate per
     * prezzo crescente.
     * </p>
     */
    private void renderItems() {
        itemsPane.getChildren().clear();
        itemViews.clear();

        List<ShopItem> items = new ArrayList<>(shop.getItems());
        items.sort(Comparator
                .comparingInt((ShopItem item) -> item instanceof HealthPotionShopItem ? 0 : 1)
                .thenComparingInt(ShopItem::getPrice));

        for (ShopItem item : items) {
            itemsPane.getChildren().add(createItemNode(item));
        }
    }

    /**
     * Crea la tile grafica di un articolo.
     *
     * @param item l'articolo da rappresentare
     * @return il nodo pronto per essere aggiunto alla griglia
     */
    private Node createItemNode(ShopItem item) {
        VBox tile = new VBox(4.0);
        tile.setAlignment(Pos.TOP_CENTER);
        tile.setPrefWidth(132.0);
        tile.setMinWidth(132.0);
        tile.setMaxWidth(132.0);
        tile.getStyleClass().add("shop-item");
        if (item instanceof HealthPotionShopItem) {
            tile.getStyleClass().add("shop-item-potion");
        }

        StackPane imageContainer = new StackPane();
        imageContainer.getStyleClass().add("shop-item-image");
        imageContainer.setMinHeight(96.0);
        imageContainer.setPrefHeight(96.0);
        imageContainer.setMaxHeight(96.0);
        imageContainer.getChildren().add(createItemIcon(item));

        Label ownedBadge = new Label();
        ownedBadge.getStyleClass().add("shop-owned-badge");
        ownedBadge.setVisible(false);
        ownedBadge.setManaged(false);
        StackPane.setAlignment(ownedBadge, Pos.TOP_RIGHT);
        StackPane.setMargin(ownedBadge, new Insets(2.0, 2.0, 0.0, 0.0));
        imageContainer.getChildren().add(ownedBadge);

        Label name = new Label(item.getName());
        name.getStyleClass().add("shop-item-name");
        name.setWrapText(true);
        name.setAlignment(Pos.CENTER);
        name.setTextAlignment(TextAlignment.CENTER);
        name.setMinHeight(44.0);
        name.setMaxWidth(116.0);

        Label description = new Label(item.getDescription());
        description.getStyleClass().add("shop-item-description");
        description.setWrapText(true);
        description.setAlignment(Pos.CENTER);
        description.setTextAlignment(TextAlignment.CENTER);
        description.setMinHeight(40.0);
        description.setMaxWidth(116.0);

        Button buyButton = new Button();
        buyButton.getStyleClass().add("shop-buy-button");
        if (item instanceof HealthPotionShopItem) {
            buyButton.getStyleClass().add("shop-buy-button-potion");
        }
        buyButton.setMaxWidth(Double.MAX_VALUE);
        buyButton.setOnAction(_ -> onBuyClicked(item));

        tile.getChildren().addAll(imageContainer, name, description, buyButton);

        ItemView view = new ItemView(item, buyButton, ownedBadge);
        itemViews.add(view);
        refreshItemView(view);

        return tile;
    }

    /**
     * Crea l'icona dell'articolo in base al tipo concreto.
     *
     * @param item l'articolo
     * @return il nodo dell'icona
     */
    private Node createItemIcon(ShopItem item) {
        return switch (item) {
            case CardShopItem cardItem -> createCardIcon(cardItem);
            case HealthPotionShopItem ignored -> createPotionIcon();
            default -> createGenericIcon();
        };
    }

    /**
     * Crea l'icona di una carta a partire dallo sprite del suo tipo.
     *
     * @param item l'articolo di tipo carta
     * @return il nodo dell'icona
     */
    private Node createCardIcon(CardShopItem item) {
        ImageView imageView = new ImageView(SpriteManager.getCardSprite(item.getCardType()));
        imageView.setFitWidth(58.0);
        imageView.setFitHeight(87.0);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        imageView.getStyleClass().add("shop-item-card-image");
        return imageView;
    }

    /**
     * Crea l'icona della pozione. Usa lo sprite PNG se disponibile, altrimenti
     * ricade sull'icona disegnata via CSS.
     *
     * @return il nodo dell'icona della pozione
     */
    private Node createPotionIcon() {
        Image sprite = SpriteManager.getHealthPotionSprite();
        if (sprite == null) {
            return createFallbackPotionIcon();
        }

        ImageView imageView = new ImageView(sprite);
        imageView.setFitWidth(58.0);
        imageView.setFitHeight(87.0);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        imageView.getStyleClass().add("shop-item-potion-image");
        return imageView;
    }

    /**
     * Crea l'icona di riserva usata quando il PNG della pozione non è disponibile.
     *
     * @return il nodo dell'icona disegnata via CSS
     */
    private Node createFallbackPotionIcon() {
        Region cork = new Region();
        cork.setPrefSize(18.0, 8.0);
        cork.getStyleClass().add("shop-potion-cork");

        StackPane body = new StackPane();
        body.setPrefSize(54.0, 66.0);
        body.setMaxSize(54.0, 66.0);
        body.getStyleClass().add("shop-potion-body");

        Label plus = new Label("+");
        plus.getStyleClass().add("shop-potion-plus");
        body.getChildren().add(plus);

        VBox bottle = new VBox(-1.0, cork, body);
        bottle.setAlignment(Pos.CENTER);
        bottle.getStyleClass().add("shop-potion-icon");
        return bottle;
    }

    /**
     * Crea un'icona segnaposto per gli articoli senza una grafica dedicata.
     *
     * @return il nodo dell'icona generica
     */
    private Node createGenericIcon() {
        Label placeholder = new Label("?");
        placeholder.getStyleClass().add("shop-generic-icon");
        return placeholder;
    }

    /**
     * Aggiorna prezzo, disponibilità e badge della tile.
     *
     * @param view la tile da aggiornare
     */
    private void refreshItemView(ItemView view) {
        ShopItem item = view.item();
        Button buyButton = view.buyButton();

        boolean available = item.isAvailableFor(player);
        boolean affordable = player.getGold() >= item.getPrice();

        if (!available) {
            buyButton.setText(item instanceof HealthPotionShopItem ? "HP pieni" : "Non disponibile");
            buyButton.setDisable(true);
        } else {
            buyButton.setText(item.getPrice() + " oro");
            buyButton.setDisable(!affordable);
        }

        long owned = countOwned(item);
        Label badge = view.ownedBadge();
        badge.setText("x" + owned);
        badge.setVisible(owned > 0);
        badge.setManaged(owned > 0);
    }

    /**
     * Aggiorna tutte le tile dopo un acquisto, così i prezzi non più
     * permettibili e la disponibilità della pozione restano coerenti.
     */
    private void refreshAllItems() {
        for (ItemView view : itemViews) {
            refreshItemView(view);
        }
    }

    /**
     * Conta quante copie di una carta sono presenti nel mazzo principale.
     * <p>
     * Gli articoli non-carta restituiscono zero.
     * </p>
     *
     * @param item l'articolo da verificare
     * @return il numero di copie possedute
     */
    private long countOwned(ShopItem item) {
        if (!(item instanceof CardShopItem cardItem)) {
            return 0;
        }
        return player.getMainDeck().getCards().stream()
                .filter(card -> card.getType() == cardItem.getCardType())
                .count();
    }

    /**
     * Gestisce il click sul pulsante d'acquisto di un articolo.
     *
     * @param item l'articolo da acquistare
     */
    private void onBuyClicked(ShopItem item) {
        PurchaseResult result = shop.buy(item, player);

        switch (result) {
            case SUCCESS -> {
                showStatus("Acquistato: " + item.getName() + " per " + item.getPrice() + " oro.",
                        "shop-status-success");
                updateHud();
                refreshAllItems();
            }
            case NOT_ENOUGH_GOLD -> showStatus("Non hai abbastanza oro per " + item.getName() + ".",
                    "shop-status-error");
            case NOT_AVAILABLE -> showStatus(
                    item instanceof HealthPotionShopItem && player.getHealth() >= player.getMaxHealth()
                            ? "Sei già al massimo della salute."
                            : item.getName() + " non è disponibile.",
                    "shop-status-error");
        }
    }

    /**
     * Sincronizza l'HUD con lo stato corrente del giocatore.
     */
    private void updateHud() {
        hpLabel.setText(player.getHealth() + "/" + player.getMaxHealth());
        goldLabel.setText(String.valueOf(player.getGold()));
        deckLabel.setText(String.valueOf(player.getMainDeck().size()));
    }

    /**
     * Mostra un messaggio di feedback con il colore adatto all'esito.
     *
     * @param message    il testo da mostrare
     * @param styleClass la classe CSS dello stato
     */
    private void showStatus(String message, String styleClass) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("shop-status-info", "shop-status-success", "shop-status-error");
        statusLabel.getStyleClass().add(styleClass);
    }

    /**
     * Torna alla mappa corrente. La vista viene ricaricata dal {@link SceneManager}
     * leggendo lo stato persistente del {@link GameManager}.
     */
    @FXML
    private void onBackToMap() {
        SceneManager.getInstance().switchScene("/fxml/MapView.fxml");
    }

    /**
     * Associa un articolo ai controlli grafici che devono essere aggiornati
     * dopo un acquisto.
     *
     * @param item l'articolo rappresentato dalla tile
     * @param buyButton il pulsante di acquisto della tile
     * @param ownedBadge il badge che mostra quante copie possiede il giocatore
     */
    private record ItemView(ShopItem item, Button buyButton, Label ownedBadge) {
    }
}
