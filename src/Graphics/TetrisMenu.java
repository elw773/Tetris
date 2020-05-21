package Graphics;

import Main.MoveGetter;
import TetrisGame.Game;
import javafx.scene.canvas.GraphicsContext;

public class TetrisMenu {
    private Game game = new Game();
    private boolean paused;
    private MoveGetter moveGetter;


    public void newGame(MoveGetter moveGetter){
        this.moveGetter = moveGetter;
    }

    public void doFrame(GraphicsContext gc){
        game.update(moveGetter.getMove());
        game.drawBoard(200,200,500,gc);
        game.drawNext(500, 200, 300, gc);
    }
}
