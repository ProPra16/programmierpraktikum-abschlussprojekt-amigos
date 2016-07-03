package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.IOException;


public class Controller
{


    public void manageStart()
    {
        try
        {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("choose_exercise.fxml"));
            stage.setScene(new Scene(root, 400, 500));
            stage.show();
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void manageComboBoxOk()
    {
        try
        {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));
            stage.setScene(new Scene(root, 1050, 800));
            stage.show();
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void loadKatalog()
    {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
        fileChooser.showOpenDialog(stage);
    }
    public void runCode()
    {
        System.out.println("Run Code");
    }
    public void runTest()
    {
        System.out.println("Run Test");
    }
}
