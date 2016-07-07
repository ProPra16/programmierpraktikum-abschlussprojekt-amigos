package de.hhu.propra16.amigos.tdd.gui;

import de.hhu.propra16.amigos.tdd.gui.controls.TDDCodeArea;
import de.hhu.propra16.amigos.tdd.xml.Exercise;
import de.hhu.propra16.amigos.tdd.xml.Katalog;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class ExerciseController {
    private Exercise exercise;
    private Katalog katalog;
    private Stage selectExerciseStage;
    public enum TDDState{ //Placeholder, remove later
        WRITE_FAILING_TEST,MAKE_PASS_TEST, REFACTOR, ATDD
    }
    private TDDState displayedState = TDDState.WRITE_FAILING_TEST;

    @FXML
    private Label exerciseNameLabel, compileStatusLabel, testStatusLabel, atddStatusLabel, timerLabel;
    @FXML
    private ImageView cycleImage, cycleImageOverlay;
    @FXML
    private TextArea compilerOutput;
    @FXML
    private CheckBox displayCodeTestsBesideCheckbox;
    @FXML
    private TDDCodeArea codeArea, testArea, tddCodeArea;
    @FXML
    private Tab codeTestTab, testTab, atddTab;
    @FXML
    private TabPane codeTabPane;
    @FXML
    private GridPane menuPane, codeTestGridPane;
    @FXML
    private Button nextPhaseButton, prevPhaseButton;
    @FXML
    private VBox babyStepContainer;

    private Integer timerSecondsLeft;
    private Integer babyStepsTime;
    private Timer babyStepsTimer;
    public void initialize(Stage selectExerciseStage, Katalog loadedCatalog, Exercise selectedExercise){
        this.selectExerciseStage = selectExerciseStage;
        this.katalog = loadedCatalog;
        this.exercise = selectedExercise;

        hideUnusedTabs();
        if(true){ //isAtdd
            menuPane.getChildren().remove(babyStepContainer);
            atddStatusLabel.setVisible(false);
        }
    }

    public void runCode(){
        switchStateAnimation(TDDState.MAKE_PASS_TEST);//Example
    }

    private void startBabyStepTimer(){
        if(babyStepsTimer != null){
            babyStepsTimer.cancel();
        }
        timerSecondsLeft = babyStepsTime;
        babyStepsTimer = new Timer();
        babyStepsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        timerSecondsLeft--;
                        timerLabel.setText(String.format("%03d:%03d",timerSecondsLeft/60, timerSecondsLeft % 60));
                        if(timerSecondsLeft == 0) babyStepTimeout();
                    }
                });
            }
        }, 1000, 1000);

    }

    private void babyStepTimeout(){

    }

    private void switchStateAnimation(TDDState newState){
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(800), cycleImage);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(-180);
        FadeTransition ft = new FadeTransition(Duration.millis(800), cycleImage);
        ft.setFromValue(1);
        ft.setToValue(0);
        RotateTransition rotateTransition2 = new RotateTransition(Duration.millis(800), cycleImageOverlay);
        rotateTransition2.setFromAngle(180);
        rotateTransition2.setToAngle(0);

        InputStream newResource = null;
        if(newState == TDDState.WRITE_FAILING_TEST) newResource = this.getClass().getResourceAsStream("red.png");
        if(newState == TDDState.MAKE_PASS_TEST) newResource = this.getClass().getResourceAsStream("green.png");
        if(newState == TDDState.REFACTOR) newResource = this.getClass().getResourceAsStream("black.png");
        if(newState == TDDState.ATDD) newResource = this.getClass().getResourceAsStream("atdd.png");

        Image newImg = new Image(newResource);
        rotateTransition2.setOnFinished(event -> {
            cycleImage.setImage(newImg);
        });
        FadeTransition ft2 = new FadeTransition(Duration.millis(800), cycleImageOverlay);
        ft2.setFromValue(0);
        ft2.setToValue(1);

        ft.play();
        cycleImageOverlay.setImage(newImg);
        rotateTransition.play();
        ft.play();
        ft2.play();
        rotateTransition2.play();
    }


    public void checkBesideChanged(){
        if(this.displayCodeTestsBesideCheckbox.isSelected()){
            GridPane.setColumnSpan(codeArea,1);
            testTab.setContent(null);
            codeTestGridPane.getChildren().add(1, testArea);
            codeTestTab.setText("Code/Tests");
        }else{
            codeTestGridPane.getChildren().remove(testArea);
            testTab.setContent(testArea);
            GridPane.setColumnSpan(codeArea,2);
            codeTestTab.setText("Code");
        }
        hideUnusedTabs();
    }

    private void hideUnusedTabs(){
        if(this.displayCodeTestsBesideCheckbox.isSelected()){
            if(codeTabPane.getTabs().contains(testTab)) codeTabPane.getTabs().remove(testTab);
        }else{
            if(!codeTabPane.getTabs().contains(testTab)) codeTabPane.getTabs().add(1, testTab);
        }

        if(true){//isAtdd()
            if(codeTabPane.getTabs().contains(atddTab)) codeTabPane.getTabs().remove(atddTab);
        }

        if(true && this.displayCodeTestsBesideCheckbox.isSelected()){ // isAtdd()
            if(!codeTabPane.getStyleClass().contains("hide-bar")) codeTabPane.getStyleClass().add("hide-bar");
        }else{
            if(codeTabPane.getStyleClass().contains("hide-bar")) codeTabPane.getStyleClass().remove("hide-bar");
        }
    }


}
