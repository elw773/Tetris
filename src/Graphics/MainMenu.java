package Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MainMenu {
    private Button test = new Button(100,100,200,100,10,40,"PLAY", Color.GREENYELLOW, null);

    public void doFrame(GraphicsContext gc){
        test.show(gc);
    }
}
