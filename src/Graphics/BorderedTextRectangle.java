package Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * A graphical rectangle with a border and centered text
 */
public class BorderedTextRectangle extends BorderedRectangle {
    private double fontSize;
    private String text;
    private Color textColor;

    public BorderedTextRectangle(double x, double y, double width, double height, double outlineSize, double fontSize, String text, Color color){
        super(x,y,width,height,outlineSize,color);
        textColor = toContrastColor(color);

        this.text = text;
        this.fontSize = fontSize;
    }

    public void setText(String text){
        this.text = text;
    }

    /**
     * Draw the rectangle and the text on the given graphics context
     * @param gc the graphics context to draw the rectangle to
     */
    @Override
    public void draw(GraphicsContext gc){
        super.draw(gc);

        gc.setFill(textColor);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("arial", FontWeight.BOLD, fontSize));
        gc.fillText(text, x + (width/2), y + ((fontSize * 3/4) + height)/2);
    }
}
