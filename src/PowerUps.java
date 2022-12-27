package src;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.List;

public class PowerUps {
    private final Circle immunity;
    private final Circle speedBoost;

    public PowerUps() {
        immunity = new Circle(Constants.FOOD_SIZE, Color.rgb(250, 255, 0));
        immunity.setFill(new ImagePattern(new Image("/images/immunity.png")));
        immunity.setTranslateX(Constants.SCREEN_WIDTH - Constants.FOOD_SIZE);
        immunity.setTranslateY(Constants.SCREEN_HEIGHT - Constants.FOOD_SIZE);

        speedBoost = new Circle(Constants.FOOD_SIZE, Color.rgb(101, 255, 0));
        speedBoost.setFill(new ImagePattern(new Image("/images/speed_boost.png")));
        speedBoost.setTranslateX(Constants.SCREEN_WIDTH - Constants.FOOD_SIZE);
        speedBoost.setTranslateY(Constants.SCREEN_HEIGHT - Constants.FOOD_SIZE * 2);
    }

    /**
     * Remove power up from screen
     */
    void remove(Group root) {
        root.getChildren().remove(immunity);
        root.getChildren().remove(speedBoost);
    }

    /**
     * Generate power ups at random locations
     */
    void generate(Group root, Scene scene) {
        System.out.println("powerups generated");
        Circle immunity = this.getImmunity();
        Circle speedBoost = this.getSpeedBoost();

        List<Double> randomImmunityPosition = Utils.generateRandomXandY(scene, immunity.getRadius(), immunity.getRadius());
        List<Double> randomSpeedBoostPosition = Utils.generateRandomXandY(scene, speedBoost.getRadius(), speedBoost.getRadius());

        immunity.setTranslateX(randomImmunityPosition.get(0));
        immunity.setTranslateY(randomImmunityPosition.get(1));
        immunity.setVisible(true);
        immunity.setId(immunity.toString());

        speedBoost.setTranslateX(randomSpeedBoostPosition.get(0));
        speedBoost.setTranslateY(randomSpeedBoostPosition.get(1));
        speedBoost.setVisible(true);
        speedBoost.setId(speedBoost.toString());

        root.getChildren().add(immunity);
        root.getChildren().add(speedBoost);
    }

    public Circle getImmunity() {
        return immunity;
    }

    public Circle getSpeedBoost() {
        return speedBoost;
    }
}
