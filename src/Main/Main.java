package Main;

import Graphics.MainMenu;
import Graphics.TetrisMenu;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private static Main instance;
    public static Main getInstance(){
        return instance;
    }

    private Timeline timeline;
    public InputManager inputManager;

    private TetrisMenu tetrisMenu;
    private MainMenu mainMenu;
    private Canvas canvas;
    private ProgramState programState;

    /**
     * Represents the state of the program, with which menu is shown
     */
    private enum ProgramState{
        MAIN_MENU, CONTROLS, PLAY, AI
    }

    public void mainMenu(){
        programState = ProgramState.MAIN_MENU;
    }

    public void controls(){
        programState = ProgramState.CONTROLS;
    }

    public void play(){
        programState = ProgramState.PLAY;
    }

    public void ai(){
        programState = ProgramState.AI;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        instance = this;

        primaryStage.setTitle("Tetris");

        canvas = new Canvas(700, 800);

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
        programState = ProgramState.MAIN_MENU;


        tetrisMenu = new TetrisMenu();
        tetrisMenu.newGame(new Player());

        mainMenu = new MainMenu();

    }

    /**
     * Does everything for a single frame of the program
     */
    public void doFrame(){
        canvas.getGraphicsContext2D().clearRect(0,0,700,800);

        inputManager.resetClicks();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        switch (programState){
            case MAIN_MENU: mainMenu.doFrame(gc); break;
            case PLAY: tetrisMenu.doFrame(gc); break;
        }


    }

    long frameStart;
    /**
     * Initializes the timeline to animate the game
     */
    public void initializeTimeline(){
        KeyFrame kf = new KeyFrame(
                Duration.seconds(1.0/60),            // Time between each update
                new EventHandler<ActionEvent>() {
                    @Override
                    // This method will be called over and over
                    // It defines the transitions as things move
                    // This is where you would handle collisions, updates and drawing
                    public void handle(ActionEvent event) {

                        frameStart = System.currentTimeMillis();
                        doFrame();
                        if(System.currentTimeMillis() - frameStart > 2) {
                            //System.err.println("Frame time: " + (System.currentTimeMillis() - frameStart));
                        } else {
                            //System.out.println("Frame time: " + (System.currentTimeMillis() - frameStart));
                        }

                    }
                });
        // Start the animation
        timeline.getKeyFrames().add(kf);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
