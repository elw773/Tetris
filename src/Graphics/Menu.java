package Graphics;

import javafx.scene.canvas.GraphicsContext;

/**
 * Represents a menu that operates on a frame by frame basis
 */
public interface Menu {
    /**
     * Draw the menu and handle the logic for one frame
     * @param gc the graphics context to draw the menu on
     */
    void doFrame(GraphicsContext gc);
}
