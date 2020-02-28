package com.florian_ligneul.canbus.change_highlighter;

import com.florian_ligneul.canbus.change_highlighter.inject.CanBusModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.florian_ligneul.canbus.change_highlighter.view.CanBusChangeHighligtherConstants.MAIN_FXML;
import static com.florian_ligneul.canbus.change_highlighter.view.CanBusChangeHighligtherConstants.THEME_CSS;

/**
 * Launcher class for the CanBus Change Highlighter
 */
public class Main extends Application {
    /**
     * Main method
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage applicationStage) throws IOException {
        Injector injector = Guice.createInjector(new CanBusModule());

        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_FXML));
        loader.setControllerFactory(injector::getInstance);

        // Load scene from FXML
        Scene scene = new Scene(loader.load());
        // Add css to the main scene
        scene.getStylesheets().add(getClass().getResource(THEME_CSS).toExternalForm());

        // Configure stage and display it
        applicationStage.setTitle("CAN Bus Change Highlighter");
        applicationStage.setScene(scene);
        applicationStage.show();
    }

}