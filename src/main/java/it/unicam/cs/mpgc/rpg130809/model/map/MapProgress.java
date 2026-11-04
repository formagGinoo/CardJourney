package it.unicam.cs.mpgc.rpg130809.model.map;

import it.unicam.cs.mpgc.rpg130809.model.entity.enemy.EnemyType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Registro della progressione delle zone: per ogni zona ricorda quali
 * nemici sono ancora vivi, a quali coordinate e di che tipo.
 * <p>
 * La prima volta che una zona viene generata viene instanziata.
 * Ogni sconfitta rimuove il nemico da {@code aliveEnemies}.
 * Le posizioni senza nemico restano vuote e i superstiti
 * conservano il tipo originario. La distinzione tra zona mai creata
 * ({@link #hasZone(int)} {@code false}) e zona con tutti i nemici uccisi
 * evita respawn accidentali.
 * </p>
 */
public final class MapProgress {

    private final Map<Integer, Map<Position, EnemyType>> aliveEnemies;

    /**
     * Costruisce un registro vuoto, privo di zone registrate.
     */
    public MapProgress() {
        this.aliveEnemies = new HashMap<>();
    }

    /**
     * Registra la tabella dei nemici generati per una zona alla sua prima creazione.
     * <p>
     * Se la zona ha già una registrazione, questa viene conservata: la prima
     * registrazione vince, perché le successive creazioni della stessa zona
     * devono riprodurre la stessa disposizione di nemici.
     * </p>
     *
     * @param level il livello della zona da registrare
     * @param enemies i nemici generati, associati alle loro coordinate
     * @throws IllegalArgumentException if the enemy table is null
     */
    public void registerZone(int level, Map<Position, EnemyType> enemies) {
        Objects.requireNonNull(enemies, "Enemy table cannot be null");
        aliveEnemies.putIfAbsent(level, new HashMap<>(enemies));
    }

    /**
     * Registra la sconfitta di un nemico, rimuovendolo dalla tabella della zona.
     * <p>
     * Una posizione rimossa non viene mai ripopolata.
     * </p>
     *
     * @param level il livello della zona in cui avviene la sconfitta
     * @param position la coordinata del nemico sconfitto
     */
    public void recordDefeat(int level, Position position) {
        Map<Position, EnemyType> enemies = aliveEnemies.get(level);
        if (enemies != null) {
            enemies.remove(position);
        }
    }

    /**
     * Indica se la zona ha già una registrazione dei nemici.
     *
     * @param level il livello della zona
     * @return {@code true} se la zona è stata registrata, anche con tutti i nemici uccisi
     */
    public boolean hasZone(int level) {
        return aliveEnemies.containsKey(level);
    }

    /**
     * Restituisce i nemici ancora vivi della zona indicata.
     *
     * @param level il livello della zona
     * @return {@code Map<Position, EnemyType>}
     */
    public Map<Position, EnemyType> getAliveEnemies(int level) {
        Map<Position, EnemyType> enemies = aliveEnemies.get(level);
        return enemies == null ? Map.of() : Collections.unmodifiableMap(enemies);
    }

    /**
     * Restituisce i nemici vivi di tutte le zone registrate.
     * <p>
     * È usato per catturare lo stato per il salvataggio
     * </p>
     *
     * @return {@code Map<Integer, Map<Position, EnemyType>>}
     */
    public Map<Integer, Map<Position, EnemyType>> getAllAliveEnemies() {
        Map<Integer, Map<Position, EnemyType>> snapshot = new HashMap<>();
        aliveEnemies.forEach((level, enemies) ->
            snapshot.put(level, Map.copyOf(enemies)));
        return Collections.unmodifiableMap(snapshot);
    }

    /**
     * Azzera tutte le registrazioni: usato all'avvio di una nuova partita.
     */
    public void reset() {
        aliveEnemies.clear();
    }
}
