package Graphics;

import Main.MoveGetter;
import TetrisGame.Game;
import javafx.scene.canvas.GraphicsContext;

public class TetrisMenu {
    private Game game = new Game();
    private boolean paused;
    private MoveGetter moveGetter;

    private long totalTime = 0;
    private long totalGameTime = 0;
    private long numUpdates = 0;



    public void newGame(MoveGetter moveGetter){
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

            game.drawBoard(200,100,300,gc);
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
