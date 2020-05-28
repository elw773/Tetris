package Main;

import Graphics.Button;
import Graphics.ControlsMenu;
import Graphics.MainMenu;
import Graphics.TetrisMenu;
import TetrisGame.Game;
import TetrisGame.Mino;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Is the Main class and manages the menus of the program
 */
public class Main extends Application {
    private static Main instance;
    public static Main getInstance(){
        return instance;
    }

    private Timeline timeline;
    public InputManager inputManager;

    private TetrisMenu tetrisMenu;
    private MainMenu mainMenu;
    private ControlsMenu controlsMenu;

    private Canvas canvas;
    private ProgramState programState;
    private Button menuButton;

    /**
     * Represents the state of the program, with which menu is shown
     */
    private enum ProgramState{
        MAIN_MENU, CONTROLS, PLAY, AI
    }

    /**
     * Set the program to show the mainMenu
     */
    public void mainMenu(){
        programState = ProgramState.MAIN_MENU;
    }

    /**
     * Set the program to show the controls menu
     */
    public void controls(){
        programState = ProgramState.CONTROLS;
    }

    /**
     * Set the program to show the tetris menu with a new game and a player as the move getter
     */
    public void play(){
        tetrisMenu.newGame(new Game(), new Player());
        programState = ProgramState.PLAY;
    }

    /**
     * Set the program to show the tetris menu with a new game and an ai as the move getter
     */
    public void ai(){
        Game game = new Game();
        tetrisMenu.newGame(game, new AI(game));
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

        tetrisMenu = new TetrisMenu(700,800);
        mainMenu = new MainMenu();
        controlsMenu = new ControlsMenu();
        menuButton = new Button(0,0,100,50,5,20,"MENU", Color.web("#F3F5FF"), ()->Main.getInstance().mainMenu());
    }

    /**
     * Does the drawing and logic for a single frame of the program
     */
    public void doFrame(){
        canvas.getGraphicsContext2D().clearRect(0,0,700,800); // clear canvas

        inputManager.resetClicks();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if(programState != ProgramState.MAIN_MENU) { // show the menu button unless we are in the main menu
            menuButton.draw(gc);
            menuButton.isClicled();
        } else {
            menuButton.hide();
            if(inputManager.isKeyClicked(KeyCode.ENTER)){
                play();
            } else if(inputManager.isKeyClicked(KeyCode.ESCAPE)){
                Platform.exit();
                System.exit(0);
            }
        }

        switch (programState){
            case MAIN_MENU: mainMenu.doFrame(gc); break;
            case CONTROLS: controlsMenu.doFrame(gc); break;
            default: tetrisMenu.doFrame(gc);
        }
    }

    long frameStart;
    /**
     * Initializes the timeline to animate the game
     */
    public void initializeTimeline(){
        KeyFrame kf = new KeyFrame(
                Duration.seconds(1.0/60),            // 60 Hz, this is according to tetris guidelines
                new EventHandler<ActionEvent>() {
                    @Override
                    // This method will be called over and over
                    // It defines the transitions as things move
                    // This is where you would handle collisions, updates and drawing
                    public void handle(ActionEvent event) {

                        frameStart = System.currentTimeMillis();

                        doFrame();

                        if(System.currentTimeMillis() - frameStart > 2) {
                            System.err.println("Frame time: " + (System.currentTimeMillis() - frameStart));
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
