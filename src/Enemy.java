package src;

import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.awt.*;

public class Enemy {
    public Circle enemy;
    public double speedX = 0;
    public double speedY = 0;
    public boolean isDead = false;

    public Enemy(double size, Color color) {
        enemy = new Circle(size, color);
        Image image = new Image("/images/enemy.png");
        enemy.setFill(new ImagePattern(image));
    }

    /**
     * Update enemy movement
     */
    public void update() {
        enemy.setTranslateX(enemy.getTranslateX() + speedX);
        enemy.setTranslateY(enemy.getTranslateY() + speedY);
    }

    /**
     * Bounce back when hitting the wall
     */
    public void handleWallCollision(Scene scene) {
        if (enemy.getTranslateX() > scene.getWidth() - enemy.getBoundsInLocal().getWidth() / 2)
            speedX = -1 * speedX;
        if (enemy.getTranslateX() < enemy.getBoundsInLocal().getWidth() / 2)
            speedX = -1 * speedX;
        if (enemy.getTranslateY() > scene.getHeight() - enemy.getBoundsInLocal().getHeight() / 2)
            speedY = -1 * speedY;
        if (enemy.getTranslateY() < enemy.getBoundsInLocal().getHeight() / 2)
            speedY = -1 * speedY;
    }

    public Circle get() {
        return enemy;
    }

    public void setIsDead(boolean life) {
        isDead = life;
    }

    public boolean isDead() {
        return isDead;
    }
}
