import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private static Main instance;
    public static Main getInstance(){
        return instance;
    }

    private Timeline timeline;
    private InputManager inputManager;
    private enum ProgramState{
        MAIN_MENU, CONTROLS, PLAY, AI
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        instance = this;

        primaryStage.setTitle("Tetris");

        Canvas canvas = new Canvas(700, 800);

        Group board = new Group();
        Scene scene = new Scene(board);
        board.getChildren().add(canvas);
        primaryStage.setScene(scene);

        inputManager = new InputManager(canvas);

        // Create the animation class
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        initializeTimeline();

        primaryStage.show();

        timeline.play();
    }

    public void doFrame(){

    }


    /**
     * Initializes the timeline to animate the game
     */
    public void initializeTimeline(){
        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.005),            // Time between each update
                new EventHandler<ActionEvent>() {
                    @Override
                    // This method will be called over and over
                    // It defines the transitions as things move
                    // This is where you would handle collisions, updates and drawing
                    public void handle(ActionEvent event) {
                        doFrame();
                    }
                });
        // Start the animation
        timeline.getKeyFrames().add(kf);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
