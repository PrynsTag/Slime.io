package src;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodManager {
    private final List<Food> allFoods = new ArrayList<>();
    private final Set<Food> foodToRemove = new HashSet<>();

    public List<Food> getAll() {
        return allFoods;
    }

    public void add(Food food) {
        allFoods.add(food);
    }

    /**
     * Add all eaten foods to 'foodToRemove' to be removed
     */
    public void getFoodToBeRemoved(Food... food) {
        if (food.length > 1) {
            foodToRemove.addAll(Arrays.asList(food));
        } else {
            foodToRemove.add(food[0]);
        }
    }

    /**
     * Removes all foods that are in the foodToRemove set.
     */
    public void cleanup() {
        allFoods.removeAll(foodToRemove);
        foodToRemove.clear();
    }

    /**
     * Generate food at random locations
     */
    public void generate(Group root, Scene scene, int numFood) {
        for (int i = 0; i < numFood; i++) {
            Food food = new Food(Constants.FOOD_SIZE);
            Circle foodCircle = food.get();

            List<Double> randomPosition = Utils.generateRandomXandY(scene, foodCircle.getRadius() * 2, foodCircle.getRadius() * 2);

            foodCircle.setTranslateX(randomPosition.get(0));
            foodCircle.setTranslateY(randomPosition.get(1));
            foodCircle.setVisible(true);
            foodCircle.setId(foodCircle.toString());

            this.add(food);
            root.getChildren().add(foodCircle);
        }
    }
}
