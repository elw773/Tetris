import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class InputManager {
    private double mouseX;
    private double mouseY;
    private boolean mousePressed;
    private boolean mouseReleased;
    private boolean mouseClicked;
    private boolean[] keyPressedMap;
    private boolean[] keyReleasedMap;
    private boolean[] keyClickedMap;
    private Canvas canvas;

    public InputManager(Canvas canvas){
        this.canvas = canvas;

        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                 @Override
                 public void handle(MouseEvent event) {
                     mousePressed = true;
                     mouseX = event.getX();
                     mouseY = event.getY();
                 }
             }
        );

        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
                  @Override
                  public void handle(MouseEvent event) {
                      mouseReleased = true;
                      mouseX = event.getX();
                      mouseY = event.getY();
                  }
              }
        );


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

    public boolean isKeyPressed(KeyCode key) {
        return false;
    }

    public boolean isKeyClocked(KeyCode key) {
        return false;
    }

    public void resetClicks(){

    }
}
