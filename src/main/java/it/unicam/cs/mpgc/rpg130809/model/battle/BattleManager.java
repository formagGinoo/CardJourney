package it.unicam.cs.mpgc.rpg130809.model.battle;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;
import it.unicam.cs.mpgc.rpg130809.model.deck.Deck;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce lo stato e la logica di una singola battaglia.
 * <p>
 * Vive solo finché il combattimento non termina (vittoria o sconfitta). È creato
 * da {@link it.unicam.cs.mpgc.rpg130809.model.game.GameManager#startBattle}
 * all'incontro con un {@link Enemy} e viene pilotato dalla vista
 * {@code BattleViewController}.
 * </p>
 * <p>
 * Coordina le entità coinvolte: il {@link Player}, il {@link Enemy}, la
 * {@link Hand} del giocatore e le due {@link Deck} usate come pila di pesca e
 * pila degli scarti. Le regole delle combo sono delegate a
 * {@link Hand#evaluateHand(List)}, mentre questo gestore applica i danni e
 * amministra il flusso dei turni.
 * </p>
 */
public class BattleManager {

    private final Player player;
    private final Enemy enemy;
    private final Deck drawPile;
    private final Deck discardPile;
    private final Hand playerHand;

    private boolean isPlayerTurn;
    private boolean goldRewarded;

    /**
     * Crea il gestore di una battaglia tra il giocatore e il nemico indicati.
     * <p>
     * Il mazzo di pesca viene inizializzato copiando le carte del mazzo
     * principale del giocatore, così la battaglia non modifica il mazzo
     * persistente. La pila degli scarti parte vuota e il turno iniziale spetta al
     * giocatore.
     * </p>
     *
     * @param player il giocatore che affronta la battaglia
     * @param enemy il nemico da sconfiggere
     * @throws IllegalArgumentException if the player or the enemy is null
     */
    public BattleManager(Player player, Enemy enemy) {
        if (player == null || enemy == null) {
            throw new IllegalArgumentException("Player and enemy are required");
        }
        this.player = player;
        this.enemy = enemy;
        this.drawPile = new Deck(player.getMainDeck().getCards());
        this.discardPile = new Deck();
        this.playerHand = new Hand(new ArrayList<>());
        this.isPlayerTurn = true;
        this.goldRewarded = false;
    }

    /**
     * Inizia la battaglia, mescola il mazzo e pesca la prima mano.
     */
    public void startBattle() {
        drawPile.shuffle();
        drawHand();
    }

    /**
     * Tenta di giocare le carte selezionate per fare una combo.
     * <p>
     * La combo viene valutata da {@link Hand#evaluateHand(List)}. Se è valida, il
     * danno viene applicato al nemico, le carte passano dalla mano alla pila
     * degli scarti e, quando il nemico viene sconfitto per la prima volta, il
     * giocatore riceve l'oro rilasciato dal nemico.
     * </p>
     * <p>
     * Il giocatore non ripesca carte dopo aver giocato una combo: deve
     * arrangiarsi con le carte rimaste in mano oppure terminare il turno.
     * </p>
     *
     * @param selectedCards le carte scelte dalla mano
     * @return {@code true} se la mossa è valida, {@code false} se la combo non esiste
     */
    public boolean playCombo(List<Card> selectedCards) {
        if (!isPlayerTurn) return false;
        int damage = playerHand.evaluateHand(selectedCards);
        if (damage <= 0) {
            return false;
        }
        enemy.takeDamage(damage);
        //Sposta le carte giocate dalla mano agli scarti
        for (Card c : selectedCards) {
            playerHand.removeCard(c);
            discardPile.addCard(c);
        }

        // Se il nemico è stato sconfitto, assegna l'oro droppato al giocatore
        if (enemy.getHealth() <= 0 && !goldRewarded) {
            player.addGold(enemy.getGoldDrop());
            goldRewarded = true;
        }

        //MEMO
        // Il giocatore dopo aver giocato una combo, non ripesca carte immediatamente.
        // Deve arrangiarsi con le carte rimaste in mano, o premere "Fine Turno".
        return true;
    }

    /**
     * Termina il turno del giocatore.
     * <p>
     * Scarta le carte rimanenti nella {@link Hand}, esegue l'attacco del nemico
     * tramite {@link Enemy#takeTurn} e pesca una nuova mano se la battaglia non è
     * terminata.
     * </p>
     *
     * @return {@code true} se durante la pescata è stato necessario rimescolare gli scarti
     */
    public boolean endTurn() {
        if (!isPlayerTurn || isBattleOver()) {
            return false;
        }

        isPlayerTurn = false;
        enemy.takeTurn(player);

        discardHand();
        playerHand.clearHand();
        boolean reshuffled = false;
        if (!isBattleOver()) {
            reshuffled = drawHand();
            isPlayerTurn = true;
        }
        return reshuffled;
    }

    /**
     * Sposta tutte le carte attualmente in mano nella pila degli scarti.
     */
    private void discardHand() {
        for (Card card : playerHand.getCards()) {
            discardPile.addCard(card);
        }
    }

    /**
     * Pesca carte fino a riempire la mano.
     * <p>
     * Se la pila di pesca è vuota, le carte della pila degli scarti vengono
     * spostate nella pila di pesca e rimescolate. L'operazione termina anche
     * quando entrambe le pile sono esaurite.
     * </p>
     *
     * @return {@code true} se è stato eseguito almeno un rimescolamento
     */
    public boolean drawHand() {
        boolean reshuffled = false;
        while (playerHand.getHandSize() < playerHand.getMaxHandSize()) {
            if (drawPile.size() == 0) {
                if (discardPile.size() == 0) {
                    break;
                }

                for (Card c : discardPile.getCards()) {
                    drawPile.addCard(c);
                }
                discardPile.clear();
                drawPile.shuffle();
                reshuffled = true;
            }
            Card drawn = drawPile.drawCard();
            if (drawn != null) {
                playerHand.addCard(drawn);
            }
        }
        return reshuffled;
    }

    /**
     * Indica se la battaglia è terminata.
     *
     * @return {@code true} se il giocatore o il nemico hanno esaurito gli HP
     */
    public boolean isBattleOver() {
        return player.getHealth() <= 0 || enemy.getHealth() <= 0;
    }

    /**
     * Indica se il giocatore ha vinto la battaglia.
     *
     * @return {@code true} se il nemico ha esaurito gli HP
     */
    public boolean isPlayerVictory() {
        return enemy.getHealth() <= 0;
    }

    /**
     * Restituisce il giocatore coinvolto nella battaglia.
     *
     * @return il giocatore
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Restituisce il nemico coinvolto nella battaglia.
     *
     * @return il nemico
     */
    public Enemy getEnemy() {
        return enemy;
    }

    /**
     * Restituisce la pila di pesca della battaglia.
     *
     * @return la pila di pesca
     */
    public Deck getDrawPile() {
        return drawPile;
    }

    /**
     * Restituisce la pila degli scarti della battaglia.
     *
     * @return la pila degli scarti
     */
    public Deck getDiscardPile() {
        return discardPile;
    }

    /**
     * Restituisce la mano del giocatore.
     *
     * @return la mano del giocatore
     */
    public Hand getPlayerHand() {
        return playerHand;
    }

    /**
     * Indica se è attualmente il turno del giocatore.
     *
     * @return {@code true} se il giocatore può agire
     */
    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }
}
