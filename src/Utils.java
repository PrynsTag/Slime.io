package src;

import javafx.scene.Scene;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {
    /**
     * Generate random x and y coordinates
     */
    public static List<Double> generateRandomXandY(Scene scene, double width, double height) {
        Random rnd = new Random();
        double randomPositionX = rnd.nextInt((int) scene.getWidth());
        if (randomPositionX > (scene.getWidth() - width)) {
            randomPositionX = scene.getWidth() - width;
        }
        double randomPositionY = rnd.nextInt((int) scene.getHeight());
        if (randomPositionY > (scene.getHeight() - height)) {
            randomPositionY = scene.getHeight() - height;
        }

        return Arrays.asList(randomPositionX, randomPositionY);
    }
}
