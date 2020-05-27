package Graphics;

import Main.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import Main.InputManager;

public class Button extends BorderedTextRectangle {

    private Runnable runnable;

    private boolean shown;



    public Button(double x, double y, double width, double height, double outlineSize, double fontSize, String text, Color color, Runnable runnable){
        super(x,y,width,height,outlineSize,fontSize,text,color);

        this.runnable = runnable;
    }



    @Override
    public void draw(GraphicsContext gc){
        super.draw(gc);

        shown = false;
    }

    public void hide(){
        shown = false;
    }

    public boolean isClicled(){
        InputManager inputManager = Main.getInstance().inputManager;

        if(inputManager.isMouseClicked()) {
            double mouseX = inputManager.getMouseX();
            double mouseY = inputManager.getMouseY();
            if(x < mouseX && mouseX < x+width  &&  y < mouseY && mouseY < y+height){
                runnable.run();
                return true;
            }
        }
        return false;
    }
}
