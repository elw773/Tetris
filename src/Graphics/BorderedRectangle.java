package Graphics;

import Main.InputManager;
import Main.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Represents a graphical rectangle with a border that follows a colour pattern
 */
public class BorderedRectangle {
    protected double x;
    protected double y;
    protected double height;
    protected double width;

    protected double innerX;
    protected double innerY;
    protected double innerHeight;
    protected double innerWidth;

    protected double outlineSize;

    protected Color fillColor;
    protected Color outlineColor;

    /**
     * Converts the fill color to an apropriate outline colour using HSB transformations
     *
     * @param color the fill color
     * @return the outline color
     */
    protected static Color toOutlineColor(Color color){
        double opacity = color.getOpacity()*1.2;
        if(opacity > 1){
            opacity = 1;
        }
        return Color.hsb(color.getHue(), color.getSaturation()*0.8, color.getBrightness()*0.8, opacity);
    }

    /**
     * Converts the fill color to an apropriate contrasting colour, for text and such, using HSB transformations
     *
     * @param color the fill color
     * @return the contrastive color
     */
    protected static Color toContrastColor(Color color){
        return Color.hsb(color.getHue(), color.getSaturation()*0.8, color.getBrightness()*0.5);
    }

    public BorderedRectangle(double x, double y, double width, double height, double outlineSize, Color color){
        fillColor = color;
        outlineColor = toOutlineColor(color);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.outlineSize = outlineSize;

        this.innerX = x + outlineSize;
        this.innerY = y + outlineSize;
        this.innerWidth = width - (2*outlineSize);
        this.innerHeight = height - (2*outlineSize);
    }

    /**
     * Creates a BorderedRectangle with the ability to include or exclude the outline from the measurements
     *
     * ex: if excludeOutline is true, the total width will be (width + 2*outlineSize) and the outline will apear outside
     * of the given measurements. If not, it will appear within the given measurements and the total width will be the given width
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param outlineSize the width of the outline
     * @param excludeOutline true to exclude the outline from the measurements
     * @param color the fill color
     */
    public BorderedRectangle(double x, double y, double width, double height, double outlineSize, boolean excludeOutline, Color color){
        this(x - (excludeOutline?outlineSize:0),
                y - (excludeOutline?outlineSize:0),
                width + (excludeOutline?(2*outlineSize):0),
                height + (excludeOutline?(2*outlineSize):0),
                outlineSize,
                color
        );
    }

    /**
     * Draw the rectangle on the given 2d graphics context
     * @param gc the graphics context to draw the rectangle to
     */
    public void draw(GraphicsContext gc){
        gc.setFill(outlineColor);
        gc.fillRect(x, y, width, height);

        gc.setFill(fillColor);
        gc.fillRect(x+outlineSize, y+outlineSize, width - (outlineSize * 2), height - (outlineSize*2));
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getInnerHeight() {
        return innerHeight;
    }

    public double getInnerWidth() {
        return innerWidth;
    }

    public double getInnerX() {
        return innerX;
    }

    public double getInnerY() {
        return innerY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}