package Graphics;

import Main.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import Main.InputManager;

public class Button extends Rectangle{
    private double fontSize;
    private String text;
    private Color textColor;

    private Runnable runnable;

    private boolean shown;



    public Button(double x, double y, double width, double height, double outlineSize, double fontSize, String text, Color color, Runnable runnable){
        super(x,y,width,height,outlineSize,color);
        textColor = toContrastColor(color);

        this.runnable = runnable;
        this.text = text;
        this.fontSize = fontSize;
    }



    @Override
    public void draw(GraphicsContext gc){
        super.draw(gc);

        shown = false;

        gc.setFill(textColor);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("arial", FontWeight.BOLD, fontSize));
        gc.fillText(text, x + (width/2), y + ((fontSize * 3/4) + height)/2);
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
