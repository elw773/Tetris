package Graphics;

import Main.MoveGetter;
import TetrisGame.Game;
import TetrisGame.Mino;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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

    private Color stdColor = Color.LIGHTGRAY;

    public TetrisMenu(double width, double height){
        this.width = width;
        this.height = height;

        double unit = width/7;
        double outlineSize = unit/20;


        board = new BorderedRectangle(unit*2, unit, unit*3, unit*6, outlineSize, true, stdColor){
            @Override
            public void draw(GraphicsContext gc){
                super.draw(gc);

                double squareSize = (width-(2*this.outlineSize)) / 10;
                for (int x = 0; x < game.getBoard().WIDTH; x++) {
                    for (int y = game.getBoard().FIRST_VISIBLE_Y; y < game.getBoard().HEIGHT; y++) {
                        Mino.draw(game.getBoard().toGraphicX(this.x +outlineSize, x, squareSize), game.getBoard().toGraphicY(this.y+outlineSize, y, squareSize), squareSize, game.getBoard().get(x, y), gc);
                    }
                }
            }
        };

        hold = new BorderedRectangle(unit/2, unit, unit*3, unit*6, outlineSize, true, stdColor){
            @Override
            public void draw(GraphicsContext gc){
                super.draw(gc);

                
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
            //game.drawBoard(200,100,300,gc);
            game.drawNext(550, 100, 100, gc);
            game.drawHold(50, 100, 100, gc);

            totalTime += (System.currentTimeMillis() - start);
            double average = (double)totalTime/numUpdates;

            // System.out.printf("Average: %.4f \t gameAvg: %.4f \n", average, gameAvg);

            if((System.currentTimeMillis() - start) > 1){
                // System.err.println("Frame took " + (System.currentTimeMillis() - start));
            }
        }
    }
}
