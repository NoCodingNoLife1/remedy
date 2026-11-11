package com.mlms;

import com.mlms.util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize Database
        DatabaseUtil.initializeDatabase();

        URL fxmlLocation = getClass().getResource("/com/mlms/fxml/LoginView.fxml");
        if (fxmlLocation == null) {
            System.err.println("LoginView.fxml not found!");
            return;
        }

        Parent root = FXMLLoader.load(fxmlLocation);
        Scene scene = new Scene(root, 1000, 700);

        primaryStage.setTitle("Remedium Labs System");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/mlms/images/logo.jpg")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
