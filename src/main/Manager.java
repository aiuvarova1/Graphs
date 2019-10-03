package main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.Group;
import javafx.scene.text.Text;

import java.io.IOException;

public class Manager extends Application{

    public static void main(String[] args) {

        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Manager.class.getResource(
                "MainScene.fxml"));
        stage.setTitle("Graph-tool");
        stage.setScene(new Scene(root));

        stage.setMinWidth(700);
        stage.setMinHeight(500);

        stage.show();
    }
}
