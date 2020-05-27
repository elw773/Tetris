package Graphics;

import Main.InputManager;
import Main.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

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

    private Color fillColor;
    private Color outlineColor;

    protected static Color toOutlineColor(Color color){
        return Color.hsb(color.getHue(), color.getSaturation()*0.8, color.getBrightness()*0.8);
    }

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
        this.width = width + (2*outlineSize);
        this.height = height + (2*outlineSize);
    }

    public BorderedRectangle(double x, double y, double width, double height, double outlineSize, boolean excludeOutline, Color color){
        this(x - (excludeOutline?outlineSize:0),
                y - (excludeOutline?outlineSize:0),
                width + (excludeOutline?(2*outlineSize):0),
                height + (excludeOutline?(2*outlineSize):0),
                outlineSize,
                color
        );
    }

    public void draw(GraphicsContext gc){
        gc.setFill(outlineColor);
        gc.fillRect(x, y, width, height);

        gc.setFill(fillColor);
        gc.fillRect(x+outlineSize, y+outlineSize, width - (outlineSize * 2), height - (outlineSize*2));
    }
}