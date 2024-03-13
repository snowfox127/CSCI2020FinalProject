import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

// ASTEROIDS GAME CREATED BY: JKMN
// 27/MAR/2019 FINALIZED

public class Main extends Application {
    public static int score = -1;
    public static String username = "Anonymous";
    public static Music music = new Music();
    public static String selectedMusic;
    public static boolean newScoreReady=false;
    public static LeaderBoardClient leaderBoardClient = new LeaderBoardClient();

    public static boolean firstStartup = true;

    @Override
    public void start(Stage stage) {
        stage.getIcons().add(new Image("/Misc/icon.png"));
        stage.setTitle("JKMN-ASTEROIDS");
        stage.setWidth(1280);
        stage.setHeight(800);

        ConstantSettings.init();

        //intro scene, with studio title and javafx logo
        SequentialTransition intro = new Intro().play(stage);
        stage.show();
        intro.play();
        music.setSoundFile("menuMusic");
        music.openSoundFile();
        music.playLong(58700000);

        //when intro animation is finished, load in main menu
        intro.setOnFinished(actionEvent -> {
            Parent TitleScreen = null;
            try {
                TitleScreen = FXMLLoader.load(getClass().getResource("Title.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            TitleScreen.setOpacity(0);
            FadeTransition fadeTransition = new Intro().fadeIn(TitleScreen);
            Scene menuScene = new Scene(TitleScreen, Color.BLACK);
            stage.setScene(menuScene);
            stage.show();
            fadeTransition.play();
            firstStartup=false;
        });
    }

    public static LeaderBoardClient getClient() {
        return leaderBoardClient;
    }
    public static int getClientScores(int i) {
        return leaderBoardClient.getScores(i);
    }
    public static String getClientNames(int i) {
        return leaderBoardClient.getNames(i);
    }
}

