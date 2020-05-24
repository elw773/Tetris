package Graphics;

import TetrisGame.Mino;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class MainMenu {
    private Button playButton;

    public MainMenu(){
        playButton = new Button(250,100,200,100,10,40,"PLAY", Color.GREENYELLOW, null);

    }

    public void doFrame(GraphicsContext gc){
        //playButton.show(gc);
        drawTetrisLogo(200,100,300,gc);
    }

    public void drawTetrisLogo(double x, double y, double width, GraphicsContext gc){
        gc.setFill(Mino.getDark(Mino.J));
        double squareSize = width/3;
        gc.fillRect(x,y,width,squareSize);
        gc.fillRect(x+squareSize, y+squareSize, squareSize, squareSize);


        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("arial", FontWeight.BOLD, squareSize));

        gc.setFill(Mino.getDark(Mino.Z));
        gc.fillText("T", x + (width/10), y + (squareSize * 7/8));

        gc.setFill(Mino.getDark(Mino.L));
        gc.fillText("E", x + (8*width/30), y + (squareSize * 7/8));

        gc.setFill(Mino.getDark(Mino.O));
        gc.fillText("T", x + (13*width/30), y + (squareSize * 7/8));

        gc.setFill(Mino.getDark(Mino.S));
        gc.fillText("R", x + (37*width/60), y + (squareSize * 7/8));

        gc.setFill(Mino.getDark(Mino.I));
        gc.fillText("I", x + (23*width/30), y + (squareSize * 7/8));

        gc.setFill(Mino.getDark(Mino.T));
        gc.fillText("S", x + (27*width/30), y + (squareSize * 7/8));
    }
}
