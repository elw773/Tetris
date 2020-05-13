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
                                          keyPressedMap.put(event.getCode(), Boolean.TRUE);
                                      }
                                  }
        );

        canvas.setOnKeyReleased(new EventHandler<KeyEvent>() {
                                   @Override
                                   public void handle(KeyEvent event) {
                                       keyReleasedMap.put(event.getCode(), Boolean.TRUE);
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

    public boolean isKeyClocked(KeyCode key) {
        if(keyClickedMap.containsKey(key)){
            return keyClickedMap.get(key);
        }
        return false;
    }

    public void resetClicks(){

    }
}
