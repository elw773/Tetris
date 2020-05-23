package Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MainMenu {
    public void doFrame(GraphicsContext gc){


        gc.setFill(Button.newColor(Color.ORANGERED));
        gc.fillRect(0,0,100,100);

        gc.setFill(Color.ORANGERED);
        gc.fillRect(10,10,80,80);
    }
}
