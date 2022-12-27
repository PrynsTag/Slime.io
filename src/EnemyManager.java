package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnemyManager {
    private final List<Enemy> allEnemies = new ArrayList<>();
    private final Set<Enemy> enemiesToRemove = new HashSet<>();

    public List<Enemy> getAll() {
        return allEnemies;
    }

    public void add(Enemy... enemies) {
        allEnemies.addAll(Arrays.asList(enemies));
    }

    /**
     * Add all dead enemies to 'enemiesToRemove' to be removed
     */
    public void addEnemiesTobeRemoved(Enemy... enemies) {
        if (enemies.length > 1) {
            enemiesToRemove.addAll(Arrays.asList(enemies));
        } else {
            enemiesToRemove.add(enemies[0]);
        }
    }

    /**
     * Removes all enemies that are in the enemiesToRemove set.
     */
    public void cleanup() {
        allEnemies.removeAll(enemiesToRemove);
        enemiesToRemove.clear();
    }
}

