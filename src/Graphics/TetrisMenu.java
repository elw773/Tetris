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

public class TetrisMenu {
    private double width;
    private double height;

    private Game game = new Game();
    private boolean paused;
    private MoveGetter moveGetter;

    private long totalTime = 0;
    private long totalGameTime = 0;
    private long numUpdates = 0;

    private PauseableBorderedRectangle board;
    private PauseableBorderedRectangle hold;
    private PauseableBorderedRectangle next;
    private BorderedRectangle score;

    private BorderedTextRectangle countdownText;

    private int countdown;

    private Color stdColor = Color.WHITE;

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

        countdownText = new BorderedTextRectangle(unit*3, unit*3, unit, unit, unit/20, 50, "3", Color.WHITE);


        board = new PauseableBorderedRectangle(unit*2, unit, unit*3, unit*6, outlineSize, true, stdColor){
            @Override
            public void draw(boolean paused, GraphicsContext gc){
                super.draw(gc);

                if(!paused) {
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
                if(Integer.toString(game.getScore()).length() > 6){
                    scoreFontSize = fontSize * 6 / Integer.toString(game.getScore()).length();
                }
                gc.setFont(Font.font("arial", FontWeight.BOLD, scoreFontSize));
                gc.fillText(Integer.toString(game.getScore()), x+(width/2), textY + (fontSize*9/8));
            }
        };
    }



    public void newGame(Game game, MoveGetter moveGetter){
        this.game = game;
        this.moveGetter = moveGetter;
    }

    public void doFrame(GraphicsContext gc){

        if(game.gameIsOver()){
            System.out.println("GAME_OVER");
        } else {
            long start = System.currentTimeMillis();
            numUpdates ++;
            if(Main.getInstance().inputManager.isKeyClicked(KeyCode.ESCAPE)){
                paused = true;
                countdown = 180;
            } else if(Main.getInstance().inputManager.isKeyClicked(KeyCode.ENTER)){
                paused = false;
            }

            boolean gamePaused = paused || countdown != 0;


            if(!gamePaused) {
                game.update(moveGetter.getMove());
            }

            totalGameTime += (System.currentTimeMillis() - start);
            double gameAvg = (double)totalGameTime/numUpdates;
            board.draw(gamePaused, gc);
            hold.draw(gamePaused, gc);
            next.draw(gamePaused, gc);
            score.draw(gc);

            double squareSize = board.getInnerWidth() / game.getBoard().WIDTH;

            if(!gamePaused) {
                game.getCurrentTetromino().drawGhostRelative(board.getInnerX(), board.getInnerY(), squareSize, gc, game.getBoard());

                game.getCurrentTetromino().drawRelative(board.getInnerX(), board.getInnerY(), squareSize, game.getBoard(), gc);
            }
            if(!paused && countdown > 0){
                countdown--;
                countdownText.setText(Integer.toString((countdown/60)+1));
                countdownText.draw(gc);
            }
            //game.drawBoard(200,100,300,gc);
            //game.drawNext(550, 100, 100, gc);
            //game.drawHold(50, 100, 100, gc);

            totalTime += (System.currentTimeMillis() - start);
            double average = (double)totalTime/numUpdates;

            // System.out.printf("Average: %.4f \t gameAvg: %.4f \n", average, gameAvg);

            if((System.currentTimeMillis() - start) > 1){
                // System.err.println("Frame took " + (System.currentTimeMillis() - start));
            }
        }
    }
}
