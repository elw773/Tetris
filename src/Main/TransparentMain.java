package Main;

import javafx.application.Application;

/**
 * Launches the main program, but with transparent appearance
 */
public class TransparentMain {
    public static void main(String[] args) {
        Main.transparent = true;
        Application.launch(Main.class, "");
    }
}
