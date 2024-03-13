import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class GameSceneController {

    private int sensitivity = ConstantSettings.settingsValues[2];
    private double asteroidSpeed = 2;
    private String selectedMusic = Main.selectedMusic;

    private int life = 3;
    private int laserToggle=0;
    private int shipDamagedFlashingTimer = 0;
    private boolean moveLeft = false, moveRight = false;
    private int moveSlow = 0;

    @FXML private ImageView spaceship;
    private Image ship = new Image("Textures/Ship.png");
    private Image shipR = new Image("Textures/ShipR.png");
    private Image shipL = new Image("Textures/ShipL.png");

    @FXML private Scene scene;
    @FXML private Label scoreLabel, gameover;
    @FXML private ImageView starBackground, dustBackground, spaceBackground;
    @FXML private ImageView hpBar1, hpBar2, hpBar3;
    @FXML private Rectangle gunMeter;
    @FXML private ImageView laserBeam1, laserBeam2;
    @FXML private ImageView asteroid1, asteroid2, asteroid3, asteroid4, asteroid5, asteroid6, asteroid7, asteroid8;

    private ImageView[] asteroids;
    private double[] YSpeed = new double[8];
    private double[] rotateSpeed = new double[8];
    //private double[] scaleFactor = new double[8];


    private AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {

            //ship movement----------------------------------------------------------------------------
            if(moveLeft && !moveRight){
                if(spaceship.getTranslateX() > -640) {
                    spaceship.setTranslateX(spaceship.getTranslateX() - sensitivity + (moveSlow * 10));
                }
                else{
                    spaceship.setTranslateX(spaceship.getTranslateX() - 5);
                }
                spaceship.setImage(shipL);
            }
            if (moveRight && !moveLeft){
                if(spaceship.getTranslateX() < 580) {
                    spaceship.setTranslateX(spaceship.getTranslateX() + sensitivity - (moveSlow * 10));
                }
                else{
                    spaceship.setTranslateX(spaceship.getTranslateX() + 5);
                }
                spaceship.setImage(shipR);
            }
            if ((moveRight && moveLeft) || (!moveRight && !moveLeft)) {
                spaceship.setImage(ship);
            }
            //-----------------------------------------------------------------------------------------
            //gun power meter--------------------------------------------------------------------------
            if (gunMeter.getHeight() <= 116){
                gunMeter.setHeight(gunMeter.getHeight() + 4);
            }
            else if (gunMeter.getHeight() > 116){
                gunMeter.setHeight(116);
            }
            //-----------------------------------------------------------------------------------------
            //ship damage flash------------------------------------------------------------------------
            if(shipDamagedFlashingTimer==0) {
                spaceship.setVisible(true);
            }
            else if(shipDamagedFlashingTimer%8!=0) {
                spaceship.setVisible(false);
                shipDamagedFlashingTimer--;
            }
            else if(shipDamagedFlashingTimer%8==0) {
                spaceship.setVisible(true);
                shipDamagedFlashingTimer--;
            }

            collisionDetection();

            Main.score ++;
            scoreLabel.setText(Integer.toString(Main.score));
            asteroidSpeed+=0.0005;
        }
    };


    @FXML
    public void initialize() {
		Main.score=0;

		asteroids = new ImageView[]{asteroid1, asteroid2, asteroid3, asteroid4, asteroid5, asteroid6, asteroid7, asteroid8};
        for (int i = 0; i < 8; i++){
            resetAsteroid(i);
        }

        spaceship.setImage(ship);

        laserBeam1.setTranslateY(spaceship.getTranslateY());
        laserBeam2.setTranslateY(spaceship.getTranslateY());
        //start animation loop
        animationTimer.start();

        //Background scrolling
        parallaxBackground();

        //music
        TitleController.resetMusic();
        Main.music.stop();
        Main.music.setSoundFile(selectedMusic);
        Main.music.openSoundFile();
        Main.music.play();
    }

    //For re-assigning values-----------------------------------------------------------
    private void resetAsteroid(int i){
        YSpeed[i] = generateYSpeed();
        rotateSpeed[i] = generateRotateSpeed();
        //scaleFactor[i] = generateScaleFactor();
        asteroids[i].setLayoutY(Math.random()*(-1200)-150);
        asteroids[i].setLayoutX(590);
        asteroids[i].setTranslateX(Math.random()*(495+555-1)-555);
    }

    private void loseALife(){
        life--;
        playDamageNoise();
        shipDamagedFlashingTimer = 90;
        lifeMechanics();
    }

    private double generateYSpeed(){
        return Math.random() * 4 + asteroidSpeed;
    }

    private double generateRotateSpeed(){
        return Math.random() * 10 - Math.random() * 10;
    }

//    private double generateScaleFactor(){
//        return Math.random() * 0.4 + 0.6;
//    }
    //----------------------------------------------------------------------------------

    //For initializing and start background animation-----------------------------------
    private ParallelTransition generateAnimation(int milisDuration, ImageView bg){
        TranslateTransition transition = new TranslateTransition(Duration.millis(milisDuration), bg);
        transition.setFromY(-1600);
        transition.setToY(0);
        transition.setInterpolator(Interpolator.LINEAR);

        TranslateTransition transition1 = new TranslateTransition(Duration.millis(0), bg);
        transition1.setFromY(0);
        transition1.setToY(-1600);
        transition1.setInterpolator(Interpolator.LINEAR);

        ParallelTransition parallelTransition = new ParallelTransition(transition, transition1);
        parallelTransition.setCycleCount(Animation.INDEFINITE);
        return parallelTransition;
    }

    private void parallaxBackground(){

        ParallelTransition parallelTransitionStar = generateAnimation(700, starBackground);
        parallelTransitionStar.play();

        ParallelTransition parallelTransitionDust = generateAnimation(8000, dustBackground);
        parallelTransitionDust.play();

        ParallelTransition parallelTransitionSpace = generateAnimation(20000, spaceBackground);
        parallelTransitionSpace.play();
    }
    //----------------------------------------------------------------------------------

    //Play sounds-----------------------------------------------------------------------
    private void playLaserNoise() {
        SoundEffect laserSound = new SoundEffect("LaserFire");
        laserSound.play();
    }

    private void playExplosionNoise() {
        SoundEffect explosionSound = new SoundEffect("Explosion");
        explosionSound.play();
    }

    private void playDamageNoise() {
        SoundEffect damageSound = new SoundEffect("takeDamage");
        damageSound.play();
    }
    //----------------------------------------------------------------------------------

    //Check if objects collide----------------------------------------------------------
    private boolean checkCollision(ImageView asteroid, int offset1, int offset2, int offset3, int offset4){
        return (asteroid.getLayoutY() >= spaceship.getLayoutY() + offset1
                && asteroid.getLayoutY() <= spaceship.getLayoutY() + offset2
                && asteroid.getTranslateX() >= spaceship.getTranslateX() + offset3
                && asteroid.getTranslateX() <= spaceship.getTranslateX() + offset4);
    }

    private void collisionDetection() {
        //asteroid movement, asteroid collision detection
        for (int i = 0; i < 8; i++) {
            ImageView asteroid = asteroids[i];
            asteroid.setLayoutY(asteroid.getLayoutY() + YSpeed[i]);
            asteroid.setRotate(asteroid.getRotate() + rotateSpeed[i]);
            if (asteroid.getLayoutY() >= 850) {
                if(shipDamagedFlashingTimer == 0) {
                    loseALife();
                }
                resetAsteroid(i);
            }
            if (shipDamagedFlashingTimer == 0) {
                //asteroid exits bottom of screen
                if (asteroid.getLayoutY() >= 850) {
                    loseALife();
                }
                //lopsided asteroid collision
                else if (i == 0 || i == 4) {
                    if ((
                    //left edge of of asteroid
                    checkCollision(asteroid, -110, 15, -85, -28)
                    //right edge of of asteroid
                    || checkCollision(asteroid, -145, 30, 28, 85)
                    //closer to center of asteroid
                    || checkCollision(asteroid, -135, 30, -28, 28))
                    ) {
                        loseALife();
                        resetAsteroid(i);
                    }
                }
                //regular-shaped (rounder) asteroid collision
                else {
                    if ((//left edge of of asteroid
                    checkCollision(asteroid, -125, 30, -85, -40)
                    //right edge of of asteroid
                    || checkCollision(asteroid, -125, 30, 40, 85)
                    //closer to center of asteroid
                    || checkCollision(asteroid, -145, 30, -40, 40))
                    ) {
                        loseALife();
                        resetAsteroid(i);
                    }
                }// end for loop
            }
        }
    }

    private void hitDetection(){
        ImageView laserBeam;
        if(laserToggle==0) {
            laserBeam = laserBeam1;
            laserToggle=1;
        }
        else {
            laserBeam = laserBeam2;
            laserToggle=0;
        }
        laserBeam.setTranslateX(spaceship.getTranslateX());
        laserBeam.setOpacity(1);
        //asteroid movement, asteroid collision detection
        for(int i=0; i<8; i++) {
            ImageView asteroid=asteroids[i];
            //asteroid collision
            if (asteroid.getLayoutY() >= -asteroid.getFitHeight()
            && asteroid.getLayoutY() <= spaceship.getLayoutY()-120
            && asteroid.getTranslateX() >= laserBeam.getTranslateX() - 85
            && asteroid.getTranslateX() <= laserBeam.getTranslateX() + 85) {
                playExplosionNoise();
                resetAsteroid(i);
            }
        }// end for loop

        FadeTransition laserDecay = new FadeTransition(Duration.millis(200),laserBeam);
        laserDecay.setInterpolator(Interpolator.EASE_IN);
        laserDecay.setToValue(0);
        laserDecay.play();

        ScaleTransition laserShrink = new ScaleTransition(Duration.millis(200),laserBeam);
        laserShrink.setInterpolator(Interpolator.EASE_IN);
        laserShrink.setToX(0);

        ScaleTransition laserGrow = new ScaleTransition(Duration.millis(0),laserBeam);
        laserGrow.setToX(1);

        ParallelTransition laser = new ParallelTransition(laserShrink,laserGrow);
        laser.play();
    }
    //----------------------------------------------------------------------------------


    private void lifeMechanics(){
        if (life == 3){
            hpBar1.setOpacity(100);
            hpBar2.setOpacity(100);
            hpBar3.setOpacity(100);
        }

        else if(life == 2){
            hpBar1.setOpacity(100);
            hpBar2.setOpacity(100);
            hpBar3.setOpacity(0);
        }

        else if(life == 1){
            hpBar1.setOpacity(100);
            hpBar2.setOpacity(0);
            hpBar3.setOpacity(0);
        }

        else if(life == 0){
            hpBar1.setOpacity(0);
            hpBar2.setOpacity(0);
            hpBar3.setOpacity(0);
            animationTimer.stop();
            moveRight = false;
            moveLeft = false;
            gameover.setOpacity(1);
            gameover.setDisable(false);
            Main.music.stop();
            FadeTransition fadeTransition = new Intro().fadeOut(scene.getRoot());
            fadeTransition.setDelay(Duration.seconds(3));
            fadeTransition.play();
            fadeTransition.setOnFinished(actionEvent -> {
                Scene scoreScene = null;
                try {
                    scoreScene = FXMLLoader.load(getClass().getResource("ScoreScene.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                scoreScene.getRoot().setOpacity(0);
                scoreScene.setFill(Color.BLACK);
                Stage stage = (Stage) scene.getWindow();
                stage.setScene(scoreScene);
                FadeTransition fadeTransition2 = new Intro().fadeIn(scoreScene.getRoot());
                fadeTransition2.play();
                stage.show();
            });
        }
    }

    @FXML
    private void isKeyPressed(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.LEFT) {
            moveLeft = true;
//            //System.out.println(spaceship.getTranslateX());//DEBUG
            keyEvent.consume();
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            moveRight = true;
//            //System.out.println(spaceship.getTranslateX());//DEBUG
            keyEvent.consume();
        }

        if (keyEvent.getCode() == KeyCode.SHIFT){
            moveSlow = 1;
            keyEvent.consume();
        }

        if (keyEvent.getCode() == KeyCode.Z){
            if (gunMeter.getHeight() > 70) {
                gunMeter.setHeight(gunMeter.getHeight() - 70);

                hitDetection();

                //laser sound effects
                playLaserNoise();
            }
            else{
                gunMeter.setHeight(0);
            }
            keyEvent.consume();
        }

        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            Main.firstStartup = false;
            animationTimer.stop();
            moveRight = false;
            moveLeft = false;
            FadeTransition fadeTransition = new Intro().fadeOut(((Scene)keyEvent.getSource()).getRoot());
            fadeTransition.play();
            fadeTransition.setOnFinished(actionEvent -> {
                Parent titleScene = null;
                try {
                    titleScene = FXMLLoader.load(getClass().getResource("Title.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                titleScene.setOpacity(0);
                Stage stage = (Stage) ((Scene)keyEvent.getSource()).getWindow();
                stage.setScene(new Scene(titleScene, Color.BLACK));
                FadeTransition fadeTransition2 = new Intro().fadeIn(titleScene);
                fadeTransition2.play();
                stage.show();
            });
        }
    }
    @FXML
    private void isKeyReleased(KeyEvent keyEvent) {

        if (keyEvent.getCode() == KeyCode.LEFT){
            moveLeft = false;
            keyEvent.consume();
        }

        if (keyEvent.getCode() == KeyCode.RIGHT){
            moveRight = false;
            keyEvent.consume();
        }

        if(keyEvent.getCode() == KeyCode.SHIFT){
            moveSlow = 0;
            keyEvent.consume();
        }
    }
}
