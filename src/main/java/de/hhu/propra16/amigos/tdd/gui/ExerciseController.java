package de.hhu.propra16.amigos.tdd.gui;

import de.hhu.propra16.amigos.tdd.xml.Exercise;
import de.hhu.propra16.amigos.tdd.xml.Katalog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ExerciseController {
    private Exercise exercise;
    private Katalog katalog;
    private Stage selectExerciseStage;

    @FXML
    private Label exerciseNameLabel, compileStatusLabel, testStatusLabel, timerLabel;

    @FXML
    private ImageView cycleImage, cycleImageOverlay;

    @FXML
    private TextArea compilerOutput;

    public void initialize(Stage selectExerciseStage, Katalog loadedCatalog, Exercise selectedExercise){
        this.selectExerciseStage = selectExerciseStage;
        this.katalog = loadedCatalog;
        this.exercise = selectedExercise;
    }


    public void runCode(){

    }


}
