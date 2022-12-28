package src;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class RunGame {
    private Group root = new Group();
    private Scene scene;
    private Player player;
    private int frame = 1;
    private final EnemyManager enemyManager = new EnemyManager();
    private final FoodManager foodManager = new FoodManager();
    PowerUps powerUps = new PowerUps();
    private final Stage stage;
    private int level = 1;
    private boolean freeze = false;
    private Boss boss;
    private int time = 0;
    private double speed = 1.0;
    private Timer timer;
    private Timer statusBarTimer;
    private Text statusBar;

    public String getTitle() {
        return Constants.TITLE;
    }

    //Constructor to pass on Stage
    public RunGame(Stage stage) {
        this.stage = stage;
    }

    /**
     * starts the splash menu and init the enter button for new game
     */
    public Scene startMenu() {
        Group root = new Group();
        scene = new Scene(root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, Color.BLACK);

        ImageView background = new ImageView(new Image("/images/welcome.png"));
        background.setFitWidth(scene.getWidth());
        background.setFitHeight(scene.getHeight());
        root.getChildren().add(background);

        setTitle(root, "Title", 2, 3);
        ImageView newGame = setTitle(root, "new_game", 2, 2.05);
        ImageView instructions = setTitle(root, "instructions", 2, 1.7);
        ImageView about = setTitle(root, "about", 2, 1.45);

        newGame.setOnMouseClicked(event -> newGame(event, level));
        instructions.setOnMouseClicked(this::instructions);
        about.setOnMouseClicked(this::about);
        freeze = true;

        return scene;
    }

    /**
     * init starts new game on the first level, create mobs,player and set controls
     */
    public Scene initNewGame() {
        time = 0;
        freeze = false;
        scene = new Scene(root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, Color.BLACK);

        player = new Player();
        root.getChildren().add(player.getImage());

        generateEnemies();
        foodManager.generate(root, scene, 5);
        generatePowerUps();
        generateStatusBar();

        scene.setOnKeyPressed(e -> player.handleKeyInput(e.getCode(), scene));

        return scene;
    }

    public void generateStatusBar() {
        if (statusBar != null) {
            root.getChildren().remove(statusBar);
        }
        statusBar = new Text();
        Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/splurge.ttf"), 40);
        statusBar.setFont(font);
        statusBar.setFill(Constants.linearGradient);
        statusBar.setX(0);
        statusBar.setY(40);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> statusBar.setText(
                        "  Food: " + player.getFoodsEaten() +
                                "  Blobs: " + player.getBlobsEaten() +
                                "  Size: " + player.getImage().getFitHeight() +
                                "  Time: " + time / 100
                ));
            }
        };
        statusBarTimer = new Timer();
        statusBarTimer.scheduleAtFixedRate(timerTask, 0, 1000);
        root.getChildren().add(statusBar);
    }

    public Scene initInstructions() {
        Group root = new Group();
        scene = new Scene(root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, Color.BLACK);

        ImageView background = new ImageView(new Image("/images/welcome.png"));
        background.setFitWidth(scene.getWidth());
        background.setFitHeight(scene.getHeight());
        root.getChildren().add(background);

        back(root);

        Text instructions = new Text();
        instructions.setText(
                "Use the WASD keys to move around and eat the food or smaller enemy to grow.\n" +
                        "Avoid the enemies and don't get hit by the boss.\n" +
                        "Collect the power ups to get a speed boost or immunity.\n\n" +
                        "Good luck!"
        );
        instructions.setFont(Constants.font);
        instructions.setX(scene.getWidth() / 2 - 400);
        instructions.setY(scene.getHeight() / 2 - 100);
        instructions.setFill(Constants.linearGradient);
        instructions.setTextAlignment(TextAlignment.CENTER);
        instructions.setWrappingWidth(scene.getWidth());
        root.getChildren().add(instructions);

        return scene;
    }

    public Scene initAbout() {
        Group root = new Group();
        scene = new Scene(root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, Color.BLACK);

        ImageView background = new ImageView(new Image("/images/welcome.png"));
        background.setFitWidth(scene.getWidth());
        background.setFitHeight(scene.getHeight());
        root.getChildren().add(background);

        back(root);

        Text about = new Text();
        about.setText("This game was made by: \n-Patrick Josh Atayde \n-Ryan Villacorte. \n\n" +
                "Resources Used to Make the Game:\n" +
                "\tcooltext.com - WordArt splashes\n" +
                "spriters-resource.com - Sprite resources\n" +
                "cmsc22 base code - Base template for the game\n"
        );
        Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/splurge.ttf"), 30);
        about.setFont(font);
        about.setX(scene.getWidth() / 2 - 340);
        about.setY(scene.getHeight() / 2 - 50);
        about.setFill(Constants.linearGradient);
        about.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(about);

        return scene;
    }

    /*
     * Back button for instructions and about
     */
    private void back(Group root) {
        ImageView back = setTitle(root, "back", 6, 8);
        back.setOnMouseClicked(event -> {
            scene = startMenu();
            stage.setScene(scene);
            stage.show();
        });
    }

    /**
     * Generate timer for powerups to appear every 10 seconds and disappear after 5 seconds
     */
    public void generatePowerUps() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        TimerTask displayPowerUps = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    powerUps.generate(root, scene);

                    Timer newtimer = new Timer();
                    newtimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(RunGame.this::removeAllPowerUps);
                        }
                    }, 5000);
                });
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(displayPowerUps, 5000, 10000);
    }

    /**
     * starts level 2, similar to init creates boss, player and boss shooter, set controls
     */
    public Scene initBoss() {
        freeze = false;
        scene = new Scene(root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, Color.BLACK);

        player = new Player();
        root.getChildren().add(player.getImage());

        boss = new Boss();
        boss.generateBoss(root);

        scene.setOnKeyPressed(e -> player.handleKeyInput(e.getCode(), scene));

        return scene;
    }

    /**
     * steps through timeline, freezes on player death
     *
     * @param elapsedTime
     */
    public void step(double elapsedTime) {
        time++;

        if (!freeze) {
            player.move(scene);
            player.handlePowerUpsCollision(root, powerUps);

            updateEnemies();
            updateFoods();
            handleEnemyDeath();
            handleFoodEaten();

            gameCondition();

            if (level != 1) {
                if (!boss.getIsDead()) {
                    boss.move(player);
                    boss.handlePlayerCollision(player);
                    if (time % 100 == 0) {
                        speed += 0.01;
                        generateBossBullets(new Random(), speed);
                    }
                }
            }
        }
    }

    /**
     * generate shots that the boss shoots at you 50-50 big/small shots, the speed increases over time
     *
     * @param rnd   Random number
     * @param speed of the shots
     */
    private void generateBossBullets(Random rnd, double speed) {
        Color color = Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
        Enemy boss = new Enemy((rnd.nextBoolean() ? 50 : 15), color);
        Circle bossCircle = boss.get();

        double valX = ((player.getImage().getX() - this.boss.getX() > 0) ? 1.5 : -1.5);
        double valY = ((player.getImage().getY() - this.boss.getY() > 0) ? 1.5 : -1.5);

        double ratioX = ((player.getImage().getX() - this.boss.getX()) / (player.getImage().getY() - this.boss.getY()));
        double ratioY = 1;

        if (ratioX > 1) {
            ratioY = (1 / ratioX);
            ratioX = 1;
        }
        if (ratioX < 1) {
            ratioY = -(1 / ratioX);
            ratioX = 1;
        }

        boss.speedX = ratioX * valX * speed;
        boss.speedY = ratioY * valY * speed;

        bossCircle.setTranslateX(this.boss.getX());
        bossCircle.setTranslateY(this.boss.getY());
        bossCircle.setVisible(true);
        bossCircle.setId(boss.toString());

        enemyManager.add(boss);
        root.getChildren().add(bossCircle);
    }

    /**
     * checks win/lose/level conditions and do death animations
     */
    private void gameCondition() {
        if (player.getIsDead()) {
            if (frame == 60) {
                gameOver();
            } else if (frame % 8 == 0) {
                player.deathAnimation(frame, root);
            }
            frame++;
        }
        if (level == 1) {
            if (enemyManager.getAll().isEmpty())
                nextLevelBlock();
        }
        if (level != 1) {
            if (boss.getIsDead()) {
                if (frame == 60) {
                    winCondition();
                } else if (frame % 8 == 0) {
                    boss.deathAnimation(frame, root);
                }
                frame++;
            }
        }
    }

    /**
     * Key set for level 1 game or level 2 game
     *
     * @param code KeyCode
     * @param part level
     */
    private void newGame(KeyCode code, int part) {
        switch (code) {
            case ENTER:
                if (part < 3) {
                    removeAllEnemies();
                    removeAllFoods();
                    removeAllPowerUps();

                    root = new Group();

                    ImageView gameBackground = new ImageView(new Image("/images/game.png"));
                    gameBackground.setFitWidth(scene.getWidth());
                    gameBackground.setFitHeight(scene.getHeight());
                    root.getChildren().add(gameBackground);

                    level = 1;
                    Scene scene = initNewGame();
                    frame = 1;

                    stage.setScene(scene);
                    stage.show();
                }
                break;
            case SPACE:
                if (part > 1) {
                    removeAllEnemies();
                    removeAllFoods();
                    removeAllPowerUps();

                    root = new Group();

                    ImageView gameBackground = new ImageView(new Image("/images/game.png"));
                    gameBackground.setFitWidth(scene.getWidth());
                    gameBackground.setFitHeight(scene.getHeight());
                    root.getChildren().add(gameBackground);

                    level = 2;
                    Scene scene2 = initBoss();
                    frame = 1;

                    stage.setScene(scene2);
                    stage.show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * New Game button
     *
     * @param input Mouse input
     * @param part  level
     */
    private void newGame(MouseEvent input, int part) {
        switch (input.getButton()) {
            case PRIMARY:
                if (part < 3) {
                    removeAllEnemies();
                    removeAllFoods();
                    removeAllPowerUps();

                    root = new Group();

                    ImageView gameBackground = new ImageView(new Image("/images/game.png"));
                    gameBackground.setFitWidth(scene.getWidth());
                    gameBackground.setFitHeight(scene.getHeight());
                    root.getChildren().add(gameBackground);

                    level = 1;
                    Scene scene = initNewGame();
                    frame = 1;

                    stage.setScene(scene);
                    stage.show();
                }
                break;
            case SECONDARY:
                if (part > 1) {
                    removeAllEnemies();
                    removeAllFoods();
                    removeAllPowerUps();

                    root = new Group();

                    ImageView gameBackground = new ImageView(new Image("/images/game.png"));
                    gameBackground.setFitWidth(scene.getWidth());
                    gameBackground.setFitHeight(scene.getHeight());
                    root.getChildren().add(gameBackground);

                    level = 2;
                    Scene scene2 = initBoss();
                    frame = 1;

                    stage.setScene(scene2);
                    stage.show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Instruction button
     */
    private void instructions(MouseEvent input) {
        if (input.getButton() == MouseButton.PRIMARY) {
            level = 0;
            Scene scene = initInstructions();
            frame = 1;
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * About button
     */
    private void about(MouseEvent input) {
        if (input.getButton() == MouseButton.PRIMARY) {
            level = 0;
            Scene scene = initAbout();
            frame = 1;
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * set any title splashes
     *
     * @param name name of file (assume is png currently)
     */
    private ImageView setTitle(Group root, String name, double xValue, double yValue) {
        Image splash = new Image("/images/" + name + ".png");
        ImageView title = new ImageView(splash);
        title.setX(scene.getWidth() / xValue - title.getBoundsInLocal().getWidth() / 2);
        title.setY(scene.getHeight() / yValue - title.getBoundsInLocal().getHeight() / 2);

        root.getChildren().add(title);

        return title;
    }

    /**
     * set splashes + key on win level 2
     */
    private void winCondition() {
        if (statusBarTimer != null) {
            statusBarTimer.cancel();
            statusBarTimer.purge();
        }
        removeAllPowerUps();
        freeze = true;
        setTitle(root, "wingame", 2, 3);
        setTitle(root, "playagain", 2, 2.3);
        setTitle(root, "spacetorestart", 2, 1.9);
        scene.setOnKeyPressed(e -> newGame(e.getCode(), 2));
    }

    /**
     * set splashes + key on win level 1
     */
    private void nextLevelBlock() {
        removeAllPowerUps();
        freeze = true;
        setTitle(root, "completed1", 2, 3);
        setTitle(root, "continue", 2, 2);
        setTitle(root, "entertorestart", 2, 1.7);
        scene.setOnKeyPressed(e -> newGame(e.getCode(), 2));
    }

    /**
     * set splashes + key on lose both levels
     */
    private void gameOver() {
        if (statusBarTimer != null) {
            statusBarTimer.cancel();
            statusBarTimer.purge();
        }
        removeAllPowerUps();
        freeze = true;
        setTitle(root, "gameover", 2, 3);
        setTitle(root, "entertorestart", 2, 2);
        if (level > 1)
            setTitle(root, "spacetorestart", 2, 1.7);
        scene.setOnKeyPressed(e -> newGame(e.getCode(), level));
    }

    /**
     * Sprite wipe with each scene
     */
    private void removeAllEnemies() {
        for (Enemy enemy : enemyManager.getAll()) {
            enemyManager.addEnemiesTobeRemoved(enemy);
            root.getChildren().remove(enemy.get());
        }
        enemyManager.cleanup();
    }

    /**
     * Sprite wipe with each scene
     */
    private void removeAllFoods() {
        for (Food food : foodManager.getAll()) {
            foodManager.getFoodToBeRemoved(food);
            root.getChildren().remove(food.get());
        }
        foodManager.cleanup();
    }

    /**
     * Sprite wipe with each scene
     */
    private void removeAllPowerUps() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        powerUps.remove(root);
    }

    /**
     * Remove enemies from scene when they are dead
     */
    private void handleEnemyDeath() {
        for (Enemy enemy : enemyManager.getAll()) {
            if (enemy.isDead()) {
                enemyManager.addEnemiesTobeRemoved(enemy);
                root.getChildren().remove(enemy.get());
            }
        }
        enemyManager.cleanup();
    }

    /**
     * Remove foods from scene when they are eaten
     */
    private void handleFoodEaten() {
        for (Food food : foodManager.getAll()) {
            if (food.isEaten()) {
                foodManager.getFoodToBeRemoved(food);
                root.getChildren().remove(food.get());
            }
        }
        foodManager.cleanup();
    }

    /**
     * Check if food is eaten and update status of food to dead
     */
    private void updateFoods() {
        for (Food food : foodManager.getAll()) {
            player.handleFoodCollision(scene, food);
        }
    }

    /**
     * update collision enemy vs player/wall
     */
    private void updateEnemies() {
        for (Enemy enemy : enemyManager.getAll()) {
            enemy.update();
            if (level == 1) {
                enemy.handleWallCollision(scene);
            }
            player.handleEnemyCollision(scene, enemy);
        }
    }

    /**
     * Generate enemies at random sizes
     */
    private void generateEnemies() {
        Random rnd = new Random();
        formEnemy(Constants.NUMBER_OF_MOB / 2, rnd, Constants.S);
        formEnemy(Constants.NUMBER_OF_MOB / 4, rnd, Constants.M);
        formEnemy(Constants.NUMBER_OF_MOB / 6, rnd, Constants.L);
        formEnemy(Constants.NUMBER_OF_MOB / 8, rnd, Constants.XL);
        formEnemy(1, rnd, Constants.XXL);
    }

    /**
     * helper function to create enemies
     *
     * @param numMob number of enemies to create
     * @param size   size of enemy
     */
    private void formEnemy(int numMob, Random rnd, int size) {
        for (int i = 0; i < numMob; i++) {
            Color color = Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
            Enemy b = new Enemy(size, color);
            Circle circle = b.get();
            b.speedX = (rnd.nextDouble()) * (rnd.nextBoolean() ? 1 : -1);
            b.speedY = (rnd.nextDouble()) * (rnd.nextBoolean() ? 1 : -1);

            List<Double> position = Utils.generateRandomXandY(scene, circle.getRadius() * 2, circle.getRadius() * 2);

            circle.setTranslateX(position.get(0));
            circle.setTranslateY(position.get(1));
            circle.setVisible(true);
            circle.setId(b.toString());

            enemyManager.add(b);
            root.getChildren().add(circle);
        }
    }
}