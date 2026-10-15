package it.unicam.cs.mpgc.rpg130809.model.battle;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;
import it.unicam.cs.mpgc.rpg130809.model.deck.Deck;
import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce lo stato e la logica di una singola battaglia.
 * Vive solo finché il combattimento non termina (vittoria o sconfitta).
 */
public class BattleManager {
    private final Player player;
    private final Enemy enemy;
    private final Deck drawPile;
    private final Deck discardPile;
    private final Hand playerHand;
    
    private boolean isPlayerTurn;
    private boolean goldRewarded;

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
     * 
     * @param selectedCards Le carte scelte dalla mano.
     * @return true se la mossa è valida, false se la combo non esiste.
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
     * Scarta le carte rimanenti, esegue l'attacco del nemico e pesca una nuova mano.
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

    private void discardHand() {
        for (Card card : playerHand.getCards()) {
            discardPile.addCard(card);
        }
    }

    /**
     * Pesca carte fino a riempire la mano.
     * Se il drawPile è vuoto, sposta le carte dal discardPile al drawPile e le rimescola.
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

    public boolean isBattleOver() {
        return player.getHealth() <= 0 || enemy.getHealth() <= 0;
    }

    public boolean isPlayerVictory() {
        return enemy.getHealth() <= 0;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public Deck getDrawPile() {
        return drawPile;
    }

    public Deck getDiscardPile() {
        return discardPile;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public boolean isGoldRewarded() {
        return goldRewarded;
    }
}
