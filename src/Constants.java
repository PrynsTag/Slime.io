package src;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

public class Constants {
    static final int S = 10;
    static final int M = 30;
    static final int L = 50;
    static final int XL = 70;
    static final int XXL = 100;
    static final int NUMBER_OF_MOB = 10;
    static final int PLAYER_SIZE = 40;
    static final int FOOD_SIZE = 20;
    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int STEP_SIZE = 3;
    static final int SIZE_ADJUSTMENT = 10;
    static final Font font = Font.loadFont(Constants.class.getResourceAsStream("/fonts/splurge.ttf"), 50);
    static final LinearGradient linearGradient = new LinearGradient(
                0, 0, 0, 1, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#F2D324")),
                new Stop(1, Color.web("#F02B2B"))
        );
    static final String TITLE = "Slime.io!";
}