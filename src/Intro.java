import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Intro {

    private Text disclaimer = new Text();
    private ImageView studioLogo = new ImageView();
    private ImageView javaFXLogo = new ImageView();

    //Change these values when modifying fade times
    private int fadeDelayInOut = 1000;
    private int fadeDelaySustain = 1000;

    public FadeTransition fadeIn(Node node){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(fadeDelayInOut), node);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        return fadeTransition;
    }

    public FadeTransition fadeOut(Node node){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(fadeDelayInOut), node);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        return fadeTransition;
    }

    private FadeTransition fadePause(Node node){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(fadeDelaySustain), node);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(1);
        return fadeTransition;
    }

    private SequentialTransition fadeInOut(Node node) {
        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(fadeIn(node), fadePause(node), fadeOut(node));
        return sequentialTransition;
    }

    Intro(){
        disclaimer.setText("This game is for demonstration purposes only.\nDo not redistribute.");
        disclaimer.setFill(Color.WHITE);
        disclaimer.setTextAlignment(TextAlignment.CENTER);
        disclaimer.setOpacity(0);
        studioLogo.setImage(new Image("/Misc/studioTitle.png"));
        studioLogo.setOpacity(0);
        javaFXLogo.setImage(new Image("/Misc/iu.png"));
        javaFXLogo.setOpacity(0);
    }

    public SequentialTransition play(Stage stage) {
        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(fadeInOut(disclaimer),
                                                    fadeInOut(studioLogo),
                                                        fadeInOut(javaFXLogo));
        GridPane gridPane = new GridPane();
        gridPane.add(disclaimer,0 ,0);
        gridPane.add(studioLogo,0, 0);
        gridPane.add(javaFXLogo,0, 0);
        gridPane.setAlignment(Pos.CENTER);
        GridPane.setHalignment(disclaimer, HPos.CENTER);
        GridPane.setHalignment(studioLogo, HPos.CENTER);
        GridPane.setHalignment(javaFXLogo, HPos.CENTER);
        Scene scene = new Scene(gridPane, Color.BLACK);
        stage.setScene(scene);

        return sequentialTransition;
    }

}

