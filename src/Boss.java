package src;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Boss {
    private ImageView boss;
    private boolean isDead = false;
    private final int size = 150;

    public void generateBoss(Group root) {
        boss = new ImageView(new Image("/Images/gooboss.png"));
        boss.setFitHeight(this.size);
        boss.setPreserveRatio(true);

        boss.setX(Constants.SCREEN_WIDTH - boss.getBoundsInParent().getWidth() / 2);
        boss.setY(Constants.SCREEN_HEIGHT / 2.0 - boss.getBoundsInParent().getHeight() / 2);

        root.getChildren().add(boss);
    }

    /**
     * Boss movement
     */
    public void move(Player player) {
        double speedX = ((player.getImage().getX() - boss.getX() > 0) ? 0.5 : -0.5);
        double speedY = ((player.getImage().getY() - boss.getY() > 0) ? 0.5 : -0.5);
        boss.setX(boss.getX() + speedX);
        boss.setY(boss.getY() + speedY);
    }

    /**
     * Boss collision vs player
     */
    public void handlePlayerCollision(Player player) {
        if (player.getImage().getBoundsInParent().intersects(boss.getBoundsInParent())) {
            if (boss.getFitHeight() > player.getImage().getBoundsInParent().getHeight() * 1.2) {
                if (!player.isImmune()) {
                    player.setIsDead(true);
                }
            } else {
                if (!isDead) {
                    player.getImage().setFitHeight(player.getImage().getFitHeight() + boss.getFitHeight() / 4);
                    player.getImage().setPreserveRatio(true);
                    player.getImage().setSmooth(true);
                    isDead = true;
                }
            }
        }
    }

    /**
     * Boss death animation
     */
    public void deathAnimation(int frame, Group root) {
        ImageView deathFrame = new ImageView(new Image("/Images/Deathboss" + frame / 8 + ".png"));
        root.getChildren().remove(boss);

        double X = boss.getX();
        double Y = boss.getY();

        boss = deathFrame;
        boss.setFitHeight(this.size - frame * 3);
        boss.setPreserveRatio(true);
        boss.setX(X);
        boss.setY(Y);

        root.getChildren().add(boss);
    }

    public double getY() {
        return boss.getY();
    }

    public double getX() {
        return boss.getX();
    }

    public boolean getIsDead() {
        return isDead;
    }
}
