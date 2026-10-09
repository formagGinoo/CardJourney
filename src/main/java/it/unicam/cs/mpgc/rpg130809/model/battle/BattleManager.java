package it.unicam.cs.mpgc.rpg130809.model.battle;

import it.unicam.cs.mpgc.rpg130809.model.card.Card;
import it.unicam.cs.mpgc.rpg130809.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg130809.model.player.Deck;
import it.unicam.cs.mpgc.rpg130809.model.player.Player;

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

    public BattleManager(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.drawPile = new Deck(player.getMainDeck().getCards());
        this.discardPile = new Deck();
        this.playerHand = new Hand(new ArrayList<>());
        this.isPlayerTurn = true;
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

        // Il giocatore dopo aver giocato una combo, non ripesca carte immediatamente.
        // Deve arrangiarsi con le carte rimaste in mano, o premere "Fine Turno".
        return true;
    }

    /**
     * Termina il turno del giocatore.
     * Scarta le carte rimanenti, esegue l'attacco del nemico e pesca una nuova mano.
     */
    public void endTurn() {
        isPlayerTurn = false;
        if (enemy.getHealth() > 0) {
            enemy.takeTurn(player);
        }

        //Scarta tutte le carte rimaste nella mano a fine turno
        for (Card c : playerHand.getHand()) {
            discardPile.addCard(c);
        }
        playerHand.clearHand();

        //Pesca una nuova mano per il turno successivo
        drawHand();
        isPlayerTurn = true;
    }

    /**
     * Pesca carte fino a riempire la mano.
     * Se il drawPile è vuoto, rimescola le carte in discardPile dentro il drawPile.
     */
    private void drawHand() {
        while (playerHand.getHandSize() < playerHand.getMaxHandSize()) {
            if (drawPile.size() == 0) {
                if (discardPile.size() == 0) break;

                for (Card c : discardPile.getCards()) {
                    drawPile.addCard(c);
                }
                discardPile.clear();
                drawPile.shuffle();
            }
            Card drawn = drawPile.drawCard();
            if (drawn != null) {
                playerHand.addCard(drawn);
            }
        }
    }

    public boolean isBattleOver() {
        return player.getHealth() <= 0 || enemy.getHealth() <= 0;
    }
}
