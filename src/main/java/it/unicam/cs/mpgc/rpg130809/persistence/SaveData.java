package it.unicam.cs.mpgc.rpg130809.persistence;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;
import it.unicam.cs.mpgc.rpg130809.model.card.CardFactory;
import it.unicam.cs.mpgc.rpg130809.model.card.CardType;
import it.unicam.cs.mpgc.rpg130809.model.deck.Deck;
import it.unicam.cs.mpgc.rpg130809.model.deck.DeckFactory;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.EnemyType;
import it.unicam.cs.mpgc.rpg130809.model.map.MapProgress;
import it.unicam.cs.mpgc.rpg130809.model.map.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Struttura dati che rappresenta lo <b>snapshot</b> della partita da
 * serializzare su disco.
 * <p>
 * È l'unico punto di contatto tra lo stato di gioco e il formato persistente:
 * contiene l'identificativo di versione del salvataggio, lo stato del
 * {@link Player} (salute, oro, zona corrente), il mazzo principale ridotto
 * alla sequenza dei {@link CardType} delle sue carte e le tabelle dei nemici
 * rimasti vivi in ciascuna zona ({@link MapProgress}). Gli identificatori
 * {@code UUID} delle carte sono ricreati al caricamento, perché non hanno
 * alcun ruolo nel gameplay.
 * </p>
 * <p>
 * La classe è immutabile: espone solo factory di conversione, nessun metodo
 * mutatore. La ricostruzione del {@code Player} avviene tramite il costruttore
 * designato {@link Player#Player(Deck, int, int, int)}, nato per il
 * caricamento da salvataggio, riutilizzando {@link CardFactory} per la creazione
 * delle carte e {@link DeckFactory} per il mazzo; la ricostruzione di
 * {@code MapProgress} riproduce le tabelle dei nemici vivi, così le posizioni
 * ripulite restano vuote e i superstiti conservano il tipo.
 * </p>
 * <p>
 * I nemici vivi sono rappresentati come liste di {@link SavedEnemy} per
 * livello di zona.
 * </p>
 */
public final class SaveData {

    private final int health;
    private final int gold;
    private final int currentMapLevel;
    private final List<CardType> mainDeck;
    private final Map<Integer, List<SavedEnemy>> aliveEnemies;

    /**
     * Costruisce il salvataggio dello stato di gioco da un giocatore e dal registro dei nemici vivi per zona.
     *
     * @param player il giocatore di cui catturare lo stato
     * @param mapProgress il registro dei nemici rimasti vivi
     * @return il salvataggio pronto per la serializzazione
     * @throws IllegalArgumentException if the player or the map progress is null
     */
    public static SaveData from(Player player, MapProgress mapProgress) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (mapProgress == null) {
            throw new IllegalArgumentException("Map progress cannot be null");
        }
        List<CardType> deckTypes = player.getMainDeck().getCards().stream()
            .map(Card::getType)
            .toList();

        return new SaveData(player.getHealth(), player.getGold(), player.getCurrentMapLevel(), deckTypes, toPersistentEnemies(mapProgress));
    }

    /**
     * Converte {@link MapProgress} nella rappresentazione serializzabile.
     *
     * @param mapProgress il registro dei nemici vivi da convertire
     * @return la mappa {@code <Integer, List<SavedEnemy>>}
     */
    private static Map<Integer, List<SavedEnemy>> toPersistentEnemies(MapProgress mapProgress) {
        Map<Integer, List<SavedEnemy>> persistentEnemies = new HashMap<>();

        mapProgress.getAllAliveEnemies().forEach((level, enemies) -> {
            List<SavedEnemy> savedEnemies = enemies.entrySet().stream()
                    .map(entry -> SavedEnemy.from(entry.getKey(), entry.getValue()))
                    .toList();

            persistentEnemies.put(level, savedEnemies);
        });

        return persistentEnemies;
    }

    /**
     * Ricostruisce il giocatore a partire da questo salvataggio.
     * <p>
     * Le carte vengono ricreate con {@link CardFactory} nella stessa sequenza
     * salvata, così l'ordine del mazzo principale è conservato.
     * </p>
     *
     * @return il giocatore ricostruito dal salvataggio
     */
    public Player toPlayer() {
        List<Card> cards = mainDeck.stream()
            .map(CardFactory::createCard)
            .toList();
        return new Player(DeckFactory.createDeck(cards), health, gold, currentMapLevel);
    }

    /**
     * Ricostruisce il registro dei nemici rimasti vivi a partire da questo salvataggio.
     * <p>
     * Il campo può essere assente nei salvataggi precedenti: in tal caso viene restituito
     * un registro vuoto, che alla prima creazione di ogni zona genera i nemici
     * casualmente e li registra.
     * </p>
     *
     * @return il registro dei nemici ricostruito dal salvataggio
     */
    public MapProgress toMapProgress() {
        MapProgress mapProgress = new MapProgress();
        if (aliveEnemies == null) {
            return mapProgress;
        }
        aliveEnemies.forEach((level, enemies) -> {
            if (level == null || enemies == null) {
                return;
            }
            Map<Position, EnemyType> converted = new HashMap<>();
            for (SavedEnemy savedEnemy : enemies) {
                if (savedEnemy != null && savedEnemy.position() != null && savedEnemy.type() != null) {
                    converted.put(savedEnemy.toPosition(), savedEnemy.type());
                }
            }
            mapProgress.registerZone(level, converted);
        });
        return mapProgress;
    }

    /**
     * Costruisce il salvataggio con i valori indicati.
     *
     * @param health la salute del giocatore
     * @param gold l'oro del giocatore
     * @param currentMapLevel il livello della zona corrente del giocatore
     * @param mainDeck i tipi di carta del mazzo principale, in ordine
     * @param aliveEnemies le liste dei nemici vivi per livello di zona
     * @throws IllegalArgumentException if mainDeck contains null or unknown card types
     */
    public SaveData(int health, int gold, int currentMapLevel, List<CardType> mainDeck, Map<Integer, List<SavedEnemy>> aliveEnemies) {
        this.health = health;
        this.gold = gold;
        this.currentMapLevel = currentMapLevel;
        this.mainDeck = List.copyOf(Objects.requireNonNull(mainDeck, "Main deck cannot be null"));
        this.aliveEnemies = deepCopyOf(Objects.requireNonNull(aliveEnemies, "Alive enemies cannot be null"));
    }

    private static Map<Integer, List<SavedEnemy>> deepCopyOf(
            Map<Integer, List<SavedEnemy>> aliveEnemies) {
        Map<Integer, List<SavedEnemy>> copy = new HashMap<>();
        aliveEnemies.forEach((level, enemies) ->
            copy.put(level, List.copyOf(Objects.requireNonNull(enemies, "Enemy table cannot be null"))));
        return Map.copyOf(copy);
    }

    /**
     * Restituisce la salute del giocatore salvata.
     *
     * @return la salute del giocatore
     */
    public int getHealth() {
        return health;
    }

    /**
     * Restituisce le liste dei nemici vivi salvate, per livello di zona.
     *
     * @return la mappa livello → lista dei nemici vivi (non modificabile)
     */
    public Map<Integer, List<SavedEnemy>> getAliveEnemies() {
        return aliveEnemies;
    }
}
