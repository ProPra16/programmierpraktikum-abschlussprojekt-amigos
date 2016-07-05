package de.hhu.propra16.amigos.tdd;

import de.hhu.propra16.amigos.tdd.gui.ChooseExerciseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("gui/start_mask.fxml"));
        primaryStage.setTitle("TDD Trainer");
        primaryStage.setScene(new Scene(root, 400, 475));
        primaryStage.show();
    }


    public static void main(String[] args) {
        if(args.length > 0){
            ChooseExerciseController.filenameToRead = args[0];
        }
        launch(args);
    }
}
