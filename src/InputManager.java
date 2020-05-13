import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.EnumMap;

public class InputManager {
    private double mouseX;
    private double mouseY;
    private boolean mousePressed;
    private boolean mouseReleased;
    private boolean mouseClicked;
    private EnumMap<KeyCode, Boolean> keyPressedMap;
    private EnumMap<KeyCode, Boolean> keyReleasedMap;
    private EnumMap<KeyCode, Boolean> keyClickedMap;
    private Canvas canvas;

    public InputManager(Canvas canvas){
        this.canvas = canvas;

        keyPressedMap = new EnumMap<>(KeyCode.class);
        keyReleasedMap = new EnumMap<>(KeyCode.class);
        keyClickedMap = new EnumMap<>(KeyCode.class);


        //setup EnumMaps


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

        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                      @Override
                                      public void handle(KeyEvent event) {
                                          keyPressedMap.put(event.getCode(), true);
                                      }
                                  }
        );

        canvas.setOnKeyReleased(new EventHandler<KeyEvent>() {
                                   @Override
                                   public void handle(KeyEvent event) {
                                       keyReleasedMap.put(event.getCode(), true);
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

    public boolean isKeyPressed(KeyCode key) {
        if(keyPressedMap.containsKey(key)){
            return keyReleasedMap.get(key);
        }
        return false;
    }

    public boolean isKeyClicked(KeyCode key) {
        if(keyClickedMap.containsKey(key)){
            return keyClickedMap.get(key);
        }
        return false;
    }

    /**
     * Re-calculates the clicks that have occurred since the last reset
     *
     * Press and release booleans will only be set back to false if a click is registered
     * A click is when corresponding press and release booleans are true
     */
    public void resetClicks(){
        // mouse
        mouseClicked = mousePressed && mouseReleased;
        if(mouseClicked){
            mousePressed = false;
            mouseReleased = false;
        }

        for (KeyCode key:keyPressedMap.keySet()) {
            if(keyReleasedMap.containsKey(key)){
                if(keyPressedMap.get(key) && keyReleasedMap.get(key)){
                    keyClickedMap.put(key, true);
                    keyPressedMap.put(key, false);
                    keyReleasedMap.put(key, false);
                }
            } else {
                keyClickedMap.put(key, false);
            }
        }
    }
}
