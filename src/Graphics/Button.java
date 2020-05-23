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
    private Runnable runnable;

    private Color fillColor;
    private Color outlineColor;
    private Color textColor;

    public static Color toOutlineColor(Color color){
        return Color.hsb(color.getHue()*0.9, color.getSaturation()*0.8, color.getBrightness()*0.8);
    }

    public Button(double x, double y, double width, double height, String text, Color color, Runnable runnable){
        double hue = color.getHue();
        double saturation = color.getSaturation();
        double brightness = color.getBrightness();
    }



    public void show(GraphicsContext gc){

    }

    public void hide(){

    }
}
