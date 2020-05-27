package Graphics;

import Main.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import Main.InputManager;

/**
 * A BorderedTextRectangle that can be clicked to run a runnable
 */
public class Button extends BorderedTextRectangle {

    private Runnable runnable;

    private boolean shown;

    public Button(double x, double y, double width, double height, double outlineSize, double fontSize, String text, Color color, Runnable runnable){
        super(x,y,width,height,outlineSize,fontSize,text,color);

        this.runnable = runnable;
    }

    /**
     * Draws the button on the graphics context
     * post- shown is set to true
     *
     * @param gc the graphics context to draw the button to
     */
    @Override
    public void draw(GraphicsContext gc){
        super.draw(gc);

        shown = true;
    }

    /**
     * Hides the button so it will not register clicks
     *
     * post- shown is set to false
     */
    public void hide(){
        shown = false;
    }

    /**
     * determines if the button is clicked
     * post- runnable will be run if the button was clicked
     * @return true if clicked
     */
    public boolean isClicled(){
        InputManager inputManager = Main.getInstance().inputManager;

        if(inputManager.isMouseClicked() && shown) {
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
