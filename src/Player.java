package src;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Timer;
import java.util.TimerTask;

public class Player {
    ImageView player;
    private double speedX = 0;
    private double speedY = 0;
    private boolean isDead = false;
    private boolean isImmune = false;
    private int speedBoostMultiplier = 1;
    private int foodsEaten = 0;
    private int blobsEaten = 0;
    private final Image goo = new Image("/images/gooplayer.png");

    public Player() {
        player = new ImageView(goo);
        player.setFitHeight(Constants.PLAYER_SIZE);
        player.setPreserveRatio(true);
        player.setX(20);
        player.setY(20);

        setImmunity(false);
        setSpeedBoost(false);
    }

    public ImageView getImage() {
        return player;
    }

    public boolean getIsDead() {
        return isDead;
    }

    public void setIsDead(boolean bol) {
        isDead = bol;
    }

    /**
     * Set player immunity and change color
     */
    public void setImmunity(boolean value) {
        isImmune = value;

        if (value) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setContrast(0.4);
            colorAdjust.setHue(-0.05);
            colorAdjust.setBrightness(0.9);
            colorAdjust.setSaturation(0.8);

            player.setEffect(colorAdjust);
        } else {
            player.setEffect(null);
        }
    }

    /**
     * Double the player speed
     */
    public void setSpeedBoost(boolean value) {
        if (value) {
            speedBoostMultiplier = 2;
            player.setImage(new Image("/images/goo_speed_boost.png"));
        } else {
            speedBoostMultiplier = 1;
            player.setImage(goo);
        }
    }

    /**
     * What to do each time a key is pressed
     *
     * @param code the key that was pressed
     */
    public void handleKeyInput(KeyCode code, Scene scene) {
        switch (code) {
            case RIGHT:
            case D:
                speedX = (speedX + Constants.STEP_SIZE) * speedBoostMultiplier;
                break;

            case LEFT:
            case A:
                speedX = (speedX - Constants.STEP_SIZE) * speedBoostMultiplier;
                break;

            case UP:
            case W:
                speedY = (speedY - Constants.STEP_SIZE) * speedBoostMultiplier;
                break;

            case DOWN:
            case S:
                speedY = (speedY + Constants.STEP_SIZE) * speedBoostMultiplier;
                break;

            case SPACE:
                speedX = 0;
                speedY = 0;
                break;

            case EQUALS:
            case ADD:
                player.setFitHeight(player.getFitHeight() + Constants.SIZE_ADJUSTMENT);
                player.setPreserveRatio(true);
                player.setSmooth(true);
                handleWallClipping(scene);
                break;

            case SUBTRACT:
            case MINUS:
                player.setFitHeight(player.getFitHeight() - Constants.SIZE_ADJUSTMENT);
                player.setPreserveRatio(true);
                player.setSmooth(true);
                break;

            default:
                // do nothing
        }
    }

    /**
     * Move the player
     */
    public void move(Scene scene) {
        if (player.getX() > scene.getWidth() - player.getBoundsInLocal().getWidth())
            speedX = -1 * speedX;
        if (player.getX() < 0)
            speedX = -1 * speedX;
        if (player.getY() > scene.getHeight() - player.getBoundsInLocal().getHeight())
            speedY = -1 * speedY;
        if (player.getY() < 0)
            speedY = -1 * speedY;

        player.setX(player.getX() + speedX);
        player.setY(player.getY() + speedY);
    }

    /**
     * Check if enemy intersects with player and
     * set player to dead if player is smaller than enemy and not immune
     */
    public void handleEnemyCollision(Scene scene, Enemy enemy) {
        if (enemy.get().getBoundsInParent().intersects(player.getBoundsInParent())) {
            if (player.getFitHeight() > enemy.get().getBoundsInLocal().getHeight() * 0.8) {
                player.setFitHeight(player.getFitHeight() + enemy.get().getBoundsInLocal().getHeight() / 4);
                player.setPreserveRatio(true);
                player.setSmooth(true);
                handleWallClipping(scene);
                enemy.setIsDead(true);
                blobsEaten++;
            } else {
                if (!isImmune) {
                    isDead = true;
                }
            }
        }
    }

    /**
     * Check if food intersects with player and
     * increase player size
     */
    public void handleFoodCollision(Scene scene, Food food) {
        if (food.get().getBoundsInParent().intersects(player.getBoundsInParent())) {
            player.setFitHeight(player.getFitHeight() + Constants.SIZE_ADJUSTMENT);
            player.setPreserveRatio(true);
            player.setSmooth(true);
            handleWallClipping(scene);
            food.setIsEaten(true);
            foodsEaten++;
        }
    }

    /**
     * Check if power-ups intersects with player and
     * set appropriate power-ups to player
     */
    public void handlePowerUpsCollision(Group root, PowerUps powerUps) {
        Circle immunity = powerUps.getImmunity();
        Circle speedBoost = powerUps.getSpeedBoost();

        if (immunity.getBoundsInParent().intersects(player.getBoundsInParent())) {
            setImmunity(true);

            root.getChildren().remove(immunity);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    setImmunity(false);
                }
            }, 5000);
        }
        if (speedBoost.getBoundsInParent().intersects(player.getBoundsInParent())) {
            setSpeedBoost(true);

            root.getChildren().remove(speedBoost);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    setSpeedBoost(false);
                }
            }, 5000);
        }
    }

    /**
     * Prevent player from going out of bounds
     */
    private void handleWallClipping(Scene scene) {
        if (player.getX() + player.getBoundsInLocal().getWidth() > scene.getWidth())
            player.setX(scene.getWidth() - player.getBoundsInLocal().getWidth());
        if (player.getY() + player.getBoundsInLocal().getHeight() > scene.getHeight())
            player.setY(scene.getHeight() - player.getBoundsInLocal().getHeight());
        if (player.getX() + player.getBoundsInLocal().getWidth() < 0)
            player.setX(0);
        if (player.getY() + player.getBoundsInLocal().getHeight() < 0)
            player.setY(0);
    }

    /**
     * Player death animation
     */
    public void deathAnimation(int frame, Group root) {
        ImageView deadFrame = new ImageView(new Image("/images/Deathanimation" + frame / 8 + ".png"));
        deadFrame.setFitHeight(player.getFitHeight());
        root.getChildren().remove(player);

        double X = player.getX();
        double Y = player.getY();

        player = deadFrame;
        player.setX(X);
        player.setY(Y);

        root.getChildren().add(player);
    }

    public boolean isImmune() {
        return isImmune;
    }

    public int getFoodsEaten() {
        return foodsEaten;
    }

    public int getBlobsEaten() {
        return blobsEaten;
    }
}
