package com.window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Window {
    private final String fxmlFile;
    private final String windowTitle;
    private Stage stage;
    protected final FXMLLoader fxmlLoader;
    private Scene scene;

    /**
     *
     * @param file is the file name that you want to open.
     * @param title is the title of the window.
     */
    Window(String file, String title) {
        fxmlFile = file;
        windowTitle = title;
        fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        setScene();
        setStage();
    }

    /**
     * Useful for reusing the code to set the scene without consistently typing the try... catch.. statement.
     */
    private void setScene() {
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("XML file " + fxmlFile + " could not be loaded");
        }
    }

    /**
     * Instantiation of the stage and setting of the title and scene.
     */
    private void setStage() {
        stage = new Stage();
        stage.setTitle(windowTitle);
        stage.setScene(scene);
    }

    /**
     * Useful method to call for opening the new window while keeping the stage private.
     */
    protected void showWindow() {
        stage.show();
    }

    /**
     * Useful method to call for opening the new window while keeping the stage private.
     */
    public void showWindowAndWait() {
        stage.showAndWait();
    }
}
