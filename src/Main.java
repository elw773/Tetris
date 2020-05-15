import TetrisGame.Tetromino.Tetromino;
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
import javafx.scene.input.KeyCode;
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

    /**
     * Represents the state of the program, with which menu is shown
     */
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

        inputManager = new InputManager(scene);

        // Create the animation class
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        initializeTimeline();

        primaryStage.show();

        timeline.play();
    }

    /**
     * Does everything for a single frame of the program
     */
    public void doFrame(){
        inputManager.resetClicks();

        if(inputManager.isMouseClicked()){
            System.out.println("x: " + inputManager.getMouseX() + " y: " + inputManager.getMouseY());
        }

        if(inputManager.isKeyClicked(KeyCode.A)){
            System.out.println("A");
        }
    }


    /**
     * Initializes the timeline to animate the game
     */
    public void initializeTimeline(){
        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.02),            // Time between each update
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
