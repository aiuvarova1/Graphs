package main;


import entities.TexLabel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.Group;
import javafx.scene.text.Text;

import java.awt.event.KeyEvent;
import java.io.IOException;

public class Manager extends Application {

    public static void main(String[] args) {

        Application.launch(args);

    }

    @Override
    public void start(Stage stage) throws IOException {

        javafx.scene.text.Font.loadFont(TexLabel.class.
                getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/base/jlm_cmmi10.ttf"), 1);
        javafx.scene.text.Font.loadFont(TexLabel.class.
                getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/maths/jlm_cmsy10.ttf"), 1);
        javafx.scene.text.Font.loadFont(TexLabel.class.
                getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/latin/jlm_cmr10.ttf"), 1);

        javafx.scene.text.Font.loadFont(TexLabel.class.
                getResourceAsStream("/org/scilab/forge/jlatexmath/greek/fonts/maths/jlm_fcmrpg.ttf"), 1);
       // Font.
        Parent root = FXMLLoader.load(Manager.class.getResource(
                "MainScene.fxml"));
        stage.setTitle("Graph-tool");

        root.getStylesheets().add(getClass().getResource("MyStyle.css").toString());
        stage.setScene(new Scene(root));

        stage.setMinWidth(900);
        stage.setMinHeight(550);

        stage.getScene().setOnKeyReleased(Controller.shortCuts);


        stage.show();
    }
}
