package it.unicam.cs.mpgc.rpg130809.model.entity.enemy;

/**
 * Factory per la creazione dei nemici del gioco.
 * <p>
 * Centralizza l'istanziazione di {@link Enemy}, usata da
 * {@link it.unicam.cs.mpgc.rpg130809.model.map.GameMap#loadFromLayout(String[])}
 * per popolare le celle nemiche e le celle del boss.
 * </p>
 *
 * @see Enemy
 * @see EnemyType
 */
public class EnemyFactory {

    /**
     * Costruttore privato: la classe espone solo metodi statici.
     */
    private EnemyFactory() {
    }

    /**
     * Crea un nemico del tipo specificato.
     *
     * @param type il tipo di nemico da creare
     * @return una nuova istanza di {@link Enemy}
     * @throws IllegalArgumentException if the enemy type is null
     */
    public static Enemy createEnemy(EnemyType type) {
        if (type == null) {
            throw new IllegalArgumentException("Enemy type is required");
        }
        return new Enemy(type);
    }
}
