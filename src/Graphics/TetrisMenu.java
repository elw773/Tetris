package Graphics;

import Main.MoveGetter;
import TetrisGame.Game;
import TetrisGame.Mino;
import TetrisGame.Tetromino.Tetromino;
import javafx.scene.canvas.GraphicsContext;
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

    private BorderedRectangle board;
    private BorderedRectangle hold;
    private BorderedRectangle next;
    private BorderedRectangle score;

    private Color stdColor = Color.WHITESMOKE;

    public TetrisMenu(double width, double height){
        this.width = width;
        this.height = height;

        double unit = width/7;
        double outlineSize = unit/20;


        board = new BorderedRectangle(unit*2, unit, unit*3, unit*6, outlineSize, true, stdColor){
            @Override
            public void draw(GraphicsContext gc){
                super.draw(gc);

                double squareSize = innerWidth / game.getBoard().WIDTH;
                for (int x = 0; x < game.getBoard().WIDTH; x++) {
                    for (int y = game.getBoard().FIRST_VISIBLE_Y; y < game.getBoard().HEIGHT; y++) {
                        Mino.draw(game.getBoard().toGraphicX(innerX, x, squareSize), game.getBoard().toGraphicY(innerY, y, squareSize), squareSize, game.getBoard().get(x, y), gc);
                    }
                }
            }
        };

        hold = new BorderedRectangle(unit/2, unit, unit, unit*4/6, outlineSize, true, stdColor){
            @Override
            public void draw(GraphicsContext gc){
                super.draw(gc);

                double squareSize = innerWidth / 6;

                if(game.getHold() != null){
                    game.getHold().drawAbsolute(innerX + squareSize, innerY + squareSize, squareSize, gc);
                }
            }
        };

        next = new BorderedRectangle(unit*11/2, unit, unit, unit*20/6, outlineSize, true, stdColor){
            @Override
            public void draw(GraphicsContext gc){
                super.draw(gc);

                double squareSize = innerWidth / 6;

                Tetromino[] next = game.getNext();
                for (int i = 0; i < next.length; i++) {
                    next[i].drawAbsolute(innerX + squareSize, innerY + squareSize + (squareSize * i * 3), squareSize, gc);
                }
            }
        };

        score = new BorderedRectangle(unit/2, unit*4, unit, unit*2, outlineSize, true, stdColor){
            @Override
            public void draw(GraphicsContext gc){
                super.draw(gc);

                gc.setFill(toContrastColor(fillColor));
                gc.setTextAlign(TextAlignment.CENTER);
                double fontSize = innerHeight/8;
                double textY = innerY+(fontSize*3/2);
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

            game.update(moveGetter.getMove());

            totalGameTime += (System.currentTimeMillis() - start);
            double gameAvg = (double)totalGameTime/numUpdates;
            board.draw(gc);
            hold.draw(gc);
            next.draw(gc);
            score.draw(gc);
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
