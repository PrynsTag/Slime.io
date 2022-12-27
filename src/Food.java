package src;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Food {
    private final Circle food;
    private boolean isEaten = false;

    public Food(int size) {
        food = new Circle(size, Color.rgb(101, 255, 0));
        Image image = new Image("/images/food.png");
        food.setFill(new ImagePattern(image));
    }

    public Circle get() {
        return food;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setIsEaten(boolean value) {
        isEaten = value;
    }
}

