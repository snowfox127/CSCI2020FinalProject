import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;


public class TitleController {

    private TranslateTransition titleTranslate;

    private static boolean playingPreview1 = false;
    private static boolean playingPreview2 = false;
    private static boolean playingPreview3 = false;
    private static long menuThemeTime;

    @FXML private VBox titleVBox;
    @FXML private GridPane ScoreGridPane;
    @FXML private GridPane BGMGridPane, settingsGridPane;
    @FXML private ImageView spaceBackground;
    //Main menu elements
    @FXML private Label startLabel, leaderboardLabel, BGMLabel, settingsLabel, quitLabel;
    //Universal menu elements
    @FXML private Label backLabel;
    //BGM menu elements
    @FXML private Label BGMTitle1, BGMTitle2, BGMTitle3, BGMPreview1, BGMPreview2, BGMPreview3, BGMSelect1, BGMSelect2, BGMSelect3;
    //Settings menu elements
    @FXML private Label musicVolUP, musicVolDN, effectVolUP, effectVolDN, sensitivityUP, sensitivityDN, musicVolLabel, effectVolLabel, sensitivityLabel;
    @FXML private Label settingsMusicLabel, settingsEffectLabel, settingsSensitivityLabel;
    //Leaderboard Elements
    @FXML private Label score1,score2,score3,score4,score5,score6,score7,score8;
    @FXML private Label player1,player2,player3,player4,player5,player6,player7,player8;
    private Label[] scores;

    private Label[] names;

    @FXML
    private void isolateLabel(Label label){
        startLabel.setOpacity(0);
        leaderboardLabel.setOpacity(0);
        BGMLabel.setOpacity(0);
        settingsLabel.setOpacity(0);
        quitLabel.setOpacity(0);
        label.setOpacity(1);
    }
    @FXML
    private void reset(){
        backLabel.setDisable(true);
        BGMGridPane.setDisable(true);
        ScoreGridPane.setDisable(true);
        settingsGridPane.setDisable(true);

        backLabel.setOpacity(0);
        BGMGridPane.setOpacity(0);
        settingsGridPane.setOpacity(0);
        ScoreGridPane.setOpacity(0);
    }
    @FXML
    private void goBack(){
        reset();
        startLabel.setOpacity(1);
        leaderboardLabel.setOpacity(1);
        BGMLabel.setOpacity(1);
        settingsLabel.setOpacity(1);
        quitLabel.setOpacity(1);
    }
    @FXML
    private TranslateTransition menuTransitionEnter(int toHeight){
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), titleVBox);
        transition.setFromY(0);
        transition.setToY(toHeight);
        return transition;
    }
    @FXML
    private TranslateTransition menuTransitionExit(){
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), titleVBox);
        transition.setFromY(titleVBox.getTranslateY());
        transition.setToY(0);
        return transition;
    }
    @FXML
    private void initialize(){
        Main.getClient().connectToServer();
        goBack();

        scores = new Label[]{score1, score2, score3, score4, score5, score6, score7, score8};
        names = new Label[]{player1, player2, player3, player4, player5, player6, player7 ,player8};
        for(Label i: scores) {
            i.setPrefWidth(500);
        }
        for(Label i: names)  {
            i.setPrefWidth(500);
        }

        musicVolLabel.setText(Integer.toString(ConstantSettings.settingsValues[0]));
        effectVolLabel.setText(Integer.toString(ConstantSettings.settingsValues[1]));
        sensitivityLabel.setText(Integer.toString(ConstantSettings.settingsValues[2]));


        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(25000), spaceBackground);
        translateTransition.setFromY(-800);
        translateTransition.setToY(0);
        translateTransition.setInterpolator(Interpolator.LINEAR);

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.millis(0), spaceBackground);
        translateTransition2.setFromY(0);
        translateTransition2.setToY(-800);

        ParallelTransition parallelTransition = new ParallelTransition(translateTransition, translateTransition2);
        parallelTransition.setCycleCount(Animation.INDEFINITE);
        parallelTransition.play();

        //start playing music for main menu
        if(!Main.firstStartup) {
            Main.music.stop();
            Main.music.setSoundFile("menuMusic");
            Main.music.openSoundFile();
            Main.music.play();
        }
    }
    @FXML
    private void labelEnter(MouseEvent event) {
        ((Label)event.getTarget()).setTextFill(Color.RED);
    }
    @FXML
    private void labelExit(MouseEvent event) {
        ((Label)event.getTarget()).setTextFill(Color.WHITE);
    }
    @FXML
    private void startLabelClick(MouseEvent event) {
        FadeTransition fadeTransition = new Intro().fadeOut(((Node)event.getSource()).getParent().getParent());
        fadeTransition.play();
        fadeTransition.setOnFinished(actionEvent -> {
            Scene gameScene = null;
            try {
                gameScene = FXMLLoader.load(getClass().getResource("GameScene.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            gameScene.getRoot().setOpacity(0);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(gameScene);
            FadeTransition fadeTransition2 = new Intro().fadeIn(gameScene.getRoot());
            fadeTransition2.play();
            stage.show();
        });
    }
    @FXML
    private void leaderboardLabelClick(){

        //Leaderboard Elements

        isolateLabel(leaderboardLabel);
        titleVBox.setDisable(true);
        titleTranslate = menuTransitionEnter(-135);
        titleTranslate.play();
        titleTranslate.setOnFinished(actionEvent -> {
            backLabel.setOpacity(1);
            backLabel.setDisable(false);
            ScoreGridPane.setOpacity(1);
            ScoreGridPane.setDisable(false);

        });
        setScoreText();
    }
    @FXML
    private void BGMLabelClick(){
        isolateLabel(BGMLabel);
        titleVBox.setDisable(true);
        titleTranslate = menuTransitionEnter(-182);
        titleTranslate.play();
        menuThemeTime = Main.music.pause();
        titleTranslate.setOnFinished(actionEvent -> {
            backLabel.setOpacity(1);
            backLabel.setDisable(false);
            BGMGridPane.setOpacity(1);
            BGMGridPane.setDisable(false);
        });
    }
    @FXML
    private void BGMPreview1Click() {
        if(!playingPreview1) {
            //if main menu music is playing, save it's timestamp
            if(playingPreview2 || playingPreview3) {
                Main.music.stop();
            }
            playingPreview1=true;
            playingPreview2=false;
            playingPreview3=false;

            Main.music.setSoundFile("LostFuture");
            Main.music.openSoundFile();
            Main.music.play();

        }
        else {
            playingPreview1=false;
            Main.music.stop();
        }
    }
    @FXML
    private void BGMPreview2Click() {
        if(!playingPreview2) {
            //if main menu music is playing, save it's timestamp
            if(playingPreview1 || playingPreview3) {
                Main.music.stop();
            }
            playingPreview1=false;
            playingPreview2=true;
            playingPreview3=false;

            Main.music.setSoundFile("SpaceFlight");
            Main.music.openSoundFile();
            Main.music.play();

        }
        else {
            playingPreview2=false;
            Main.music.stop();
        }
    }
    @FXML
    private void BGMPreview3Click() {
        if(!playingPreview3) {
            //if main menu music is playing, save it's timestamp
            if(playingPreview1 || playingPreview2) {
                Main.music.stop();
            }
            playingPreview1=false;
            playingPreview2=false;
            playingPreview3=true;

            Main.music.setSoundFile("Stardust");
            Main.music.openSoundFile();
            Main.music.play();

        }
        else {
            playingPreview3=false;
            Main.music.stop();
        }
    }
    @FXML
    private void BGMSelect1Click() {
        Main.selectedMusic = "LostFuture";
        ConstantSettings.BGMSelection = 0;
        backLabelClick();
    }
    @FXML
    private void BGMSelect2Click() {
        Main.selectedMusic = "SpaceFlight";
        ConstantSettings.BGMSelection = 1;
        backLabelClick();
    }
    @FXML
    private void BGMSelect3Click() {
        Main.selectedMusic = "Stardust";
        ConstantSettings.BGMSelection = 2;
        backLabelClick();
    }
    @FXML
    public static void resetMusic() {
        menuThemeTime=0;
        playingPreview1=false;
        playingPreview2=false;
        playingPreview3=false;
    }
    @FXML
    private void settingsLabelClick(){
        isolateLabel(settingsLabel);
        titleVBox.setDisable(true);
        titleTranslate = menuTransitionEnter(-235);
        titleTranslate.play();
        titleTranslate.setOnFinished(actionEvent -> {
            backLabel.setOpacity(1);
            backLabel.setDisable(false);
            settingsGridPane.setDisable(false);
            settingsGridPane.setOpacity(1);
        });
    }
    //for changing setting parameters------------------------------------------
    @FXML
    private void setMusicDN(){
        settingsDN(0, 0, musicVolLabel);
        Main.music.setVolume((float) ConstantSettings.settingsValues[0]/10);
    }
    @FXML
    private void setMusicUP(){
        settingsUP(0, 10, musicVolLabel);
        Main.music.setVolume((float) ConstantSettings.settingsValues[0]/10);
    }
    @FXML
    private void setEffectDN(){
        settingsDN(1, 0, effectVolLabel);
    }
    @FXML
    private void setEffectUP(){
        settingsUP(1, 10, effectVolLabel);
    }
    @FXML
    private void setSensitivityDN(){
        settingsDN(2, 1, sensitivityLabel);
    }
    @FXML
    private void setSensitivityUP(){
        settingsUP(2, 40, sensitivityLabel);
    }
    private void settingsDN(int settingsItem, int limit, Label label){
        if (ConstantSettings.settingsValues[settingsItem] > limit){
            ConstantSettings.settingsValues[settingsItem]--;
            label.setText(Integer.toString(ConstantSettings.settingsValues[settingsItem]));
        }
    }
    private void settingsUP(int settingsItem, int limit, Label label){
        if (ConstantSettings.settingsValues[settingsItem] < limit){
            ConstantSettings.settingsValues[settingsItem]++;
            label.setText(Integer.toString(ConstantSettings.settingsValues[settingsItem]));
        }
    }
    //--------------------------------------------------------------------------------------
    @FXML
    private void setScoreText(){
        Main.getClient().connectToServer();
        for(int i=0; i<8; i++) {
            scores[i].setText(String.valueOf((Main.getClientScores(i))));
            names[i].setText(Main.getClientNames(i));
        }
    }
    @FXML
    private void backLabelClick(){
        //If exiting settings menu/BGM menu, write changes to file
        if (settingsLabel.getOpacity() == 1 || BGMLabel.getOpacity() == 1){
            ConstantSettings.writeSettingToFile();
        }
        //stop music from BGM page
        if(BGMLabel.getOpacity() == 1) {
            Main.music.stop();
        }
        //set all submenu opacity to 0
        reset();
        //for returning from BGM page
        if (!Main.music.isPlaying()) {
            Main.music.setSoundFile("menuMusic");
            Main.music.openSoundFile();
            Main.music.play(menuThemeTime);
        }
        //Animation for putting labels back to place
        titleTranslate = menuTransitionExit();
        titleTranslate.play();
        titleTranslate.setOnFinished(actionEvent -> {
            goBack();
            titleVBox.setDisable(false);
        });
    }
    @FXML
    private void quitLabelClick(){
        System.exit(0);
    }

}
