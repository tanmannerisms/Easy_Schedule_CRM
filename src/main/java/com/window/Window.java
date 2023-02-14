package com.window;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import com.easyschedule.Main;

public class Window {
    private String fxmlFile;
    private String windowTitle;
    protected Stage stage;
    protected FXMLLoader fxmlLoader;
    private Scene scene;

    /**
     *
     * @param file is the file name that you want to open.
     * @param title is the title of the window.
     */
    public Window(String file, String title) {
        fxmlFile = file;
        windowTitle = title;
        fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
        setScene();
        setStage();
    }

    /**
     * Used for keeping the current window and changing the scene
     *
     * @param file the file to set the new scene to
     * @param title the title to set the new scene to.
     */
    public void changeScene(String file, String title) {
        fxmlFile = file;
        windowTitle = title;
        fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
        setScene();
        stage.setTitle(windowTitle);
        stage.setScene(scene);
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
    public void showWindow() {
        stage.show();
    }

    /**
     * Useful method to call for opening the new window while keeping the stage private. Stops current thread of parent
     * Window from running.
     */
    public void showWindowAndWait() {
        stage.showAndWait();
    }
    protected static Stage getParentWindow(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        return (Stage) button.getScene().getWindow();
    }
}
