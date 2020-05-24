package Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Button {
    private double x;
    private double y;
    private double height;
    private double width;
    private double outlineSize;
    private double fontSize;
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
        return Color.hsb(color.getHue(), color.getSaturation()*0.8, color.getBrightness()*0.5);
    }

    public Button(double x, double y, double width, double height, double outlineSize, double fontSize, String text, Color color, Runnable runnable){
        fillColor = color;
        outlineColor = toOutlineColor(color);
        textColor = toTextColor(color);

        this.runnable = runnable;
        this.text = text;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.outlineSize = outlineSize;
        this.fontSize = fontSize;
    }



    public void show(GraphicsContext gc){
        shown = true;

        gc.setFill(outlineColor);
        gc.fillRect(x, y, width, height);

        gc.setFill(fillColor);
        gc.fillRect(x+outlineSize, y+outlineSize, width - (outlineSize * 2), height - (outlineSize*2));


        gc.setFill(textColor);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("arial", FontWeight.BOLD, fontSize));
        gc.fillText(text, x + (width/2), y + ((fontSize * 3/4) + height)/2);
    }

    public void hide(){
        shown = false;
    }

    public boolean isClicled(){
        return false;
    }
}
