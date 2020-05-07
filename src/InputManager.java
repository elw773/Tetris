import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;

public class InputManager {
    private double mouseX;
    private double mouseY;
    private boolean mousePressed;
    private boolean mouseClicked;
    private boolean[] keyMap;
    private boolean[] keyClickMap;
    private Canvas canvas;

    public InputManager(Canvas canvas){

    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public boolean isMousePressed(){
        return mousePressed;
    }

    public boolean isMouseClicked() {
        return mouseClicked;
    }

    public boolean[] getKeyMap() {
        return keyMap;
    }

    public boolean[] getKeyClickMap() {
        return keyClickMap;
    }

    public boolean isKeyPressed(KeyEvent key) {
        return false;
    }

    public boolean isKeyClocked(KeyEvent key) {
        return false;
    }
}
