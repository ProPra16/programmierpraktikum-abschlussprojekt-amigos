package de.hhu.propra16.amigos.tdd.gui;

import de.hhu.propra16.amigos.tdd.xml.Katalog;
import de.hhu.propra16.amigos.tdd.xml.KatalogLeser;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ChooseExerciseController implements Initializable{
    @FXML
    private ListView listView;
    @FXML
    private Label catalogLoadedLabel;

    public static String filenameToRead = "catalog.xml";
    private Katalog loadedCatalog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tryLoadFile(new File(filenameToRead), true);
    }

    private void tryLoadFile(File file, boolean silent){
        try{
            Katalog loaded = KatalogLeser.lese(file);
            if(loaded == null) throw new IllegalArgumentException();

            this.loadedCatalog = loaded;
            ObservableList<String> listViewItemList = FXCollections.observableArrayList();
            for(int i = 0; i < loaded.size(); i++){
                listViewItemList.add(i, loaded.getName(i));
            }
            this.listView.setItems(listViewItemList);
            if(loaded.size() != 0) this.listView.getSelectionModel().select(0);
            this.catalogLoadedLabel.setText("Currently loaded: " + file.getName());
            this.playTransition();
        }catch(Exception ex){
            if(!silent){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(("Error"));
                alert.setContentText("Please choose a valid catalog file");
                alert.showAndWait();
            }
            this.catalogLoadedLabel.setText("Currently loaded: (none)");
        }
    }

    private void playTransition(){
        FadeTransition ft = new FadeTransition(Duration.millis(900), this.listView);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public void loadKatalog(){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Catalog File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null){
            tryLoadFile(selectedFile, false);
        }
    }
    public void listViewClick(MouseEvent click){
        if(click.getClickCount() == 2){
            startExercise();
        }
    }

    public void startExercise(){
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editor.fxml"));
            Parent root = loader.load();
            ExerciseController exContr = (ExerciseController) loader.getController();
            Stage currentStage = (Stage)this.listView.getScene().getWindow();
            exContr.initialize(currentStage, this.loadedCatalog, null);//wait until Logic Team implements getting Exercise from Katalog

            currentStage.hide();
            stage.setOnCloseRequest(event -> {
                currentStage.show();
                currentStage.requestFocus();
            });
            stage.setMinHeight(600);
            stage.setMinWidth(500);
            stage.setScene(new Scene(root, 1050, 800));
            stage.show();
            stage.setMaximized(true);
        }catch(Exception ex){
            ex.printStackTrace();;
        }
    }




}
