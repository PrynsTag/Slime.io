package src;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private RunGame game;

    @Override
    public void start(Stage s) {
        game = new RunGame(s);
        s.setTitle(game.getTitle());

        Scene scene = game.startMenu();
        s.getIcons().add(new Image("/images/logo.png"));
        s.setScene(scene);
        s.show();

        int fps = 60;
        int MILLISECOND_DELAY = 1000 / fps;
        double SECOND_DELAY = 1.0 / fps;

        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> game.step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
