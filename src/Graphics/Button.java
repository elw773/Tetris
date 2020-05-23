package Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Button {
    private double x;
    private double y;
    private double height;
    private double width;
    private boolean shown;
    private String text;
    private Runnable runnable;

    private Color fillColor;
    private Color outlineColor;
    private Color textColor;

    public static Color toOutlineColor(Color color){
        return Color.hsb(color.getHue(), color.getSaturation()*0.8, color.getBrightness()*0.8);
    }

    public static Color toTextColor(Color color){
        if(color.getBrightness() > 0.5){
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

    public Button(double x, double y, double width, double height, String text, Color color, Runnable runnable){
        fillColor = color;
        outlineColor = toOutlineColor(color);
        textColor = toTextColor(color);

        this.runnable = runnable;
        this.text = text;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }



    public void show(GraphicsContext gc){

    }

    public void hide(){

    }

    public boolean isClicled(){
        return false;
    }
}
