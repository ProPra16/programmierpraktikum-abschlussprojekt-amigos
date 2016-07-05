package de.hhu.propra16.amigos.tdd.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartMaskController {

    public void manageStart(ActionEvent event){
        try
        {
            Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            oldStage.close();

            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("choose_exercise.fxml"));
            stage.setScene(new Scene(root, 400, 500));
            stage.show();
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
