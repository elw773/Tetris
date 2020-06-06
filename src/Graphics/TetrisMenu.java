package Graphics;

import Main.Main;
import Main.MoveGetter;
import TetrisGame.Game;
import TetrisGame.Mino;
import TetrisGame.Tetromino.Tetromino;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import Main.InputManager;

public class TetrisMenu implements Menu{
    private double width;
    private double height;

    private Game game = new Game();
    private boolean paused;
    private boolean gameOver;
    private MoveGetter moveGetter;

    private long totalTime = 0;
    private long totalGameTime = 0;
    private long numUpdates = 0;

    private PauseableBorderedRectangle board;
    private PauseableBorderedRectangle hold;
    private PauseableBorderedRectangle next;
    private BorderedRectangle score;

    private BorderedTextRectangle interruptText;

    private int countdown;

    private Color stdColor = Main.transparent?Color.gray(0.2,0.45):Color.web("#F3F5FF");

    /**
     * A BorderedRectangle that, when paused, does not draw its contents
     */
    private abstract class PauseableBorderedRectangle extends BorderedRectangle{
        PauseableBorderedRectangle(double x, double y, double width, double height, double outlineSize, boolean excludeOutline, Color color){
            super(x,y,width,height,outlineSize,excludeOutline,color);
        }
        public abstract void draw(boolean paused, GraphicsContext gc);
    }

    public TetrisMenu(double width, double height){
        paused = false;
        this.width = width;
        this.height = height;

        double unit = width/7;
        double outlineSize = unit/20;

        interruptText = new BorderedTextRectangle(unit*2.5, unit*3, unit*2, unit, unit/20, 30, "3", Color.web("#ffcccb"));

        board = new PauseableBorderedRectangle(unit*2, unit, unit*3, unit*6, outlineSize, true, stdColor){
            @Override
            public void draw(boolean paused, GraphicsContext gc){
                super.draw(gc);

                if(!paused) {
                    // draw all of the Minos on the visible part of the board
                    double squareSize = innerWidth / game.getBoard().WIDTH;
                     for (int x = 0; x < game.getBoard().WIDTH; x++) {
                        for (int y = game.getBoard().FIRST_VISIBLE_Y; y < game.getBoard().HEIGHT; y++) {
                            Mino.draw(game.getBoard().toGraphicX(innerX, x, squareSize), game.getBoard().toGraphicY(innerY, y, squareSize), squareSize, game.getBoard().get(x, y), gc);
                        }
                    }
                }
            }
        };

        hold = new PauseableBorderedRectangle(unit/2, unit, unit, unit*4/6, outlineSize, true, stdColor){
            @Override
            public void draw(boolean paused, GraphicsContext gc){
                super.draw(gc);
                if(!paused) {
                    // draw the held mino
                    double squareSize = innerWidth / 6;

                    if (game.getHold() != null) {
                        game.getHold().drawAbsolute(innerX + squareSize, innerY + squareSize, squareSize, gc);
                    }
                }
            }
        };

        next = new PauseableBorderedRectangle(unit*11/2, unit, unit, unit*20/6, outlineSize, true,  stdColor){
            @Override
            public void draw(boolean paused, GraphicsContext gc){
                super.draw(gc);
                if(!paused) {
                    // draw all of the next minos
                    double squareSize = innerWidth / 6;

                    Tetromino[] next = game.getNext();
                    for (int i = 0; i < next.length; i++) {
                        next[i].drawAbsolute(innerX + squareSize, innerY + squareSize + (squareSize * i * 3), squareSize, gc);
                    }
                }
            }
        };

        score = new BorderedRectangle(unit/2, unit*4, unit, unit*2, outlineSize, true, stdColor){
            @Override
            public void draw(GraphicsContext gc){
                super.draw(gc);

                gc.setFill(toContrastColor(fillColor));
                gc.setTextAlign(TextAlignment.CENTER);
                double fontSize = height/8;
                double textY = y+(fontSize*3/2);
                gc.setFont(Font.font("arial", FontWeight.BOLD, fontSize));
                String text = "Score:\n" + game.getScore() + "\nLevel:\n" + game.getLevel() + "\nLines:\n" + game.getLines();
                gc.fillText("Score:\n\nLevel:\n" + game.getLevel() + "\nLines:\n" + game.getLines(), x+(width/2), textY);

                double scoreFontSize = fontSize;
                if(Integer.toString(game.getScore()).length() > 6){ // make font smaller if there are more than 6 digits
                    scoreFontSize = fontSize * 6 / Integer.toString(game.getScore()).length();
                }
                gc.setFont(Font.font("arial", FontWeight.BOLD, scoreFontSize));
                gc.fillText(Integer.toString(game.getScore()), x+(width/2), textY + (fontSize*9/8));
            }
        };
    }

    /**
     * Sets the game and moveGetter to the given objects
     * @param game the new game for the menu to use
     * @param moveGetter the new moveGetter for the menu to use
     */
    public void newGame(Game game, MoveGetter moveGetter){
        this.game = game;
        this.moveGetter = moveGetter;
        paused = false;
        countdown = 0;
    }

    /**
     * Draw the menu and handle the logic for one frame
     * pre- game and moveGetter are not null
     * post- the game will have gone through one frame
     * @param gc the graphics context to draw the menu on
     */
    public void doFrame(GraphicsContext gc){
        InputManager inputManager = Main.getInstance().inputManager;
        int level = 0;
        boolean shift = inputManager.isKeyPressed(KeyCode.SHIFT);
        if(inputManager.isKeyClicked(KeyCode.DIGIT0)){
            game.reset(shift?10:0);
            countdown = 180;
            paused = false;
        } else if(inputManager.isKeyClicked(KeyCode.DIGIT1)){
            game.reset(shift?11:1);
            countdown = 180;
            paused = false;
        } else if(inputManager.isKeyClicked(KeyCode.DIGIT2)){
            game.reset(shift?12:2);
            countdown = 180;
            paused = false;
        } else if(inputManager.isKeyClicked(KeyCode.DIGIT3)){
            game.reset(shift?13:3);
            countdown = 180;
            paused = false;
        } else if(inputManager.isKeyClicked(KeyCode.DIGIT4)){
            game.reset(shift?14:4);
            countdown = 180;
            paused = false;
        } else if(inputManager.isKeyClicked(KeyCode.DIGIT5)){
            game.reset(shift?15:5);
            countdown = 180;
            paused = false;
        } else if(inputManager.isKeyClicked(KeyCode.DIGIT6)){
            game.reset(shift?16:6);
            countdown = 180;
            paused = false;
        } else if(inputManager.isKeyClicked(KeyCode.DIGIT7)){
            game.reset(shift?17:7);
            countdown = 180;
            paused = false;
        } else if(inputManager.isKeyClicked(KeyCode.DIGIT8)){
            game.reset(shift?18:8);
            countdown = 180;
            paused = false;
        } else if(inputManager.isKeyClicked(KeyCode.DIGIT9)){
            game.reset(shift?19:9);
            countdown = 180;
            paused = false;
        }

        if(Main.getInstance().inputManager.isKeyClicked(KeyCode.ESCAPE)){
            if(paused){
                Main.getInstance().mainMenu();
            }
            paused = true;
            countdown = 180;
        } else if(Main.getInstance().inputManager.isKeyClicked(KeyCode.ENTER)){
            paused = false;
            if(game.gameIsOver()){
                game.reset(1);
            }
        }

        boolean gamePaused = true; // the game should only continue if un-paused and the countdown is done

        if(game.gameIsOver()){
            paused = true;
            countdown = 180;
            interruptText.setText("Game Over");
            interruptText.draw(gc);
        } else if(paused){
            interruptText.setText("Paused");
        } else if(countdown > 0){
            countdown--;
            interruptText.setText(Integer.toString((countdown/60)+1));
        } else {
            game.update(moveGetter.getMove());
            gamePaused = false;
        }

        // draw
        board.draw(gamePaused, gc);
        hold.draw(gamePaused, gc);
        next.draw(gamePaused, gc);
        score.draw(gc);

        if(gamePaused){ // this must be drawn at the end since the tetrominos need to be visible over the board
            interruptText.draw(gc);
        } else {
            double squareSize = board.getInnerWidth() / game.getBoard().WIDTH;
            game.getCurrentTetromino().drawGhostRelative(board.getInnerX(), board.getInnerY(), squareSize, gc, game.getBoard());

            game.getCurrentTetromino().drawRelative(board.getInnerX(), board.getInnerY(), squareSize, game.getBoard(), gc);
        }
    }
}
