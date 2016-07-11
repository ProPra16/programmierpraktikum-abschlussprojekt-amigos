package de.hhu.propra16.amigos.tdd.gui;

import de.hhu.propra16.amigos.tdd.gui.controls.TDDCodeArea;
import de.hhu.propra16.amigos.tdd.logik.CodeObject;
import de.hhu.propra16.amigos.tdd.logik.LogikHandler;
import de.hhu.propra16.amigos.tdd.logik.TDDState;
import de.hhu.propra16.amigos.tdd.xml.Exercise;
import de.hhu.propra16.amigos.tdd.xml.Katalog;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ExerciseController {
    private Exercise exercise;
    private Katalog katalog;
    private Stage selectExerciseStage;
    private LogikHandler logikHandler;

    @FXML
    private Label compileStatusLabel, testStatusLabel, atddStatusLabel, timerLabel, exerciseLabel, exerciseDescription, nextStep;
    @FXML
    private ImageView cycleImage, cycleImageOverlay;
    @FXML
    private TextArea compilerArea;
    @FXML
    private CheckBox displayCodeTestsBesideCheckbox;
    @FXML
    private TDDCodeArea codeArea, testArea, atddArea;
    @FXML
    private Tab codeTestTab, testTab, atddTab, outputTab;
    @FXML
    private TabPane codeTabPane;
    @FXML
    private GridPane menuPane, codeTestGridPane;
    @FXML
    private Button nextPhaseButton, prevPhaseButton;
    @FXML
    private VBox babyStepContainer, exerciseContainer, outputTabContainer;

    private Integer timerSecondsLeft;
    private Integer babyStepsTime = 120;
    private Timer babyStepsTimer;


    public void initialize(Stage selectExerciseStage, Katalog loadedCatalog, Exercise selectedExercise){
        this.selectExerciseStage = selectExerciseStage;
        this.katalog = loadedCatalog;
        this.exercise = selectedExercise;
        this.codeArea.setText(this.exercise.getClasses().entrySet().iterator().next().getValue());
        this.testArea.setText(this.exercise.getTests().entrySet().iterator().next().getValue());
        this.exerciseLabel.setText(this.exercise.getName());
        this.exerciseDescription.setText(this.exercise.getDescription());
        this.logikHandler = new LogikHandler(exercise);


        if(this.logikHandler.isBabySteps()){
            this.babyStepsTime = this.logikHandler.babyStepsTime() * 60;
        }else{
            menuPane.getChildren().remove(this.babyStepContainer);
        }

        hideUnusedTabs();
        applyStateToGUI();

        if(!this.logikHandler.isATDD()){
            outputTabContainer.getChildren().remove(atddStatusLabel);
        }

        Image phaseImage = getImageOfPhase(this.logikHandler.getState());
        this.cycleImage.setImage(phaseImage);
        this.cycleImageOverlay.setImage(phaseImage);

        FadeTransition ft = new FadeTransition(Duration.millis(2500), exerciseContainer);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

    }

    private void applyStateToGUI(){
        switch(this.logikHandler.getState()){
            case MAKE_PASS_TEST:
                codeArea.setDisable(false);
                testArea.setDisable(true);
                atddArea.setDisable(true);
                nextStep.setText("Current task: Change your code minimally to make the test pass");
                break;
            case WRITE_FAILING_TEST:
                codeArea.setDisable(true);
                testArea.setDisable(false);
                atddArea.setDisable(true);
                nextStep.setText("Current task: Write a failing or not compiling test");
                break;
            case WRITE_FAILING_ACCEPTANCE_TEST:
                codeArea.setDisable(true);
                testArea.setDisable(true);
                atddArea.setDisable(false);
                nextStep.setText("Current task: Write an failing or not compiling acceptance test");
            case REFACTOR:
                codeArea.setDisable(false);
                testArea.setDisable(false);
                atddArea.setDisable(true);
                nextStep.setText("Current task: Refactor the code and/or tests if you want");
                break;
        }

        FadeTransition ft = new FadeTransition(Duration.millis(800), nextStep);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        this.prevPhaseButton.setDisable(this.logikHandler.getState() != TDDState.MAKE_PASS_TEST );
        if(this.logikHandler.getState() != TDDState.MAKE_PASS_TEST  && this.logikHandler.isATDD()){
            this.prevPhaseButton.setDisable(false);
            this.prevPhaseButton.setText("Change Accept Test");
        }else{
            this.prevPhaseButton.setText("Prev Phase");
        }

        if(this.babyStepsTimer != null){
            babyStepsTimer.cancel();
        }
        if(this.logikHandler.isBabySteps() && this.logikHandler.getState() != TDDState.REFACTOR){
            babyStepContainer.setVisible(true);
            startBabyStepTimer();
        }else{
            babyStepContainer.setVisible(false);
        }
    }

    public void runCode(){
        runAndUpdateGUI(true);
    }

    private void runAndUpdateGUI(boolean switchAlwaysToOutputTab){
        this.logikHandler.setCode(this.codeArea.getText());
        this.logikHandler.setTest(this.testArea.getText());
        if(this.logikHandler.isATDD()){
            this.logikHandler.setATDDTest(this.atddArea.getText());
        }

        if(this.logikHandler.tryCompileCode()){
            this.compileStatusLabel.setText("Code compiles");
            this.compileStatusLabel.getStyleClass().remove("red");
        }else{
            this.compileStatusLabel.setText("Code doesn't compile");
            this.compileStatusLabel.getStyleClass().add("red");
        }
        String[] failingTests = null;
        try{
            failingTests = this.logikHandler.getFailingTests();
        }catch(Exception ex){}

        if(this.logikHandler.tryCompileTest()){
            if(failingTests.length == 0 ){
                if(switchAlwaysToOutputTab) this.codeTabPane.getSelectionModel().select(outputTab);
                this.testStatusLabel.setText("Tests passing");
                this.testStatusLabel.getStyleClass().remove("red");
            }else{
                this.codeTabPane.getSelectionModel().select(outputTab);
                this.testStatusLabel.setText("Tests run with " + failingTests.length + " failures");
                this.testStatusLabel.getStyleClass().add("red");
            }
        }else{
            this.codeTabPane.getSelectionModel().select(outputTab);
            this.testStatusLabel.setText("Tests dont compile");
            this.testStatusLabel.getStyleClass().add("red");
        }
        doOutputScaleAnimation();

        StringBuilder stringBuilder = new StringBuilder();
        if(failingTests != null){
            for(String k : failingTests){
                stringBuilder.append(k);
                stringBuilder.append("\n");
            }
        }
        compilerArea.setText(stringBuilder.toString());

        if(this.logikHandler.isATDD()){
            if(this.logikHandler.isATDDpassing()){
                this.atddStatusLabel.setText("Accept. Test not failing");
                this.atddStatusLabel.getStyleClass().remove("red");
            }else{
                this.atddStatusLabel.setText("Accept. Test is failing");
                this.atddStatusLabel.getStyleClass().add("red");
            }
        }
    }

    private void doOutputScaleAnimation(){
        ParallelTransition pt = new ParallelTransition();
        for(Node c : outputTabContainer.getChildren()){
            ScaleTransition scale = new ScaleTransition(Duration.millis(250), c);
            scale.setFromX(1);
            scale.setToX(1.1);
            scale.setFromY(1);
            scale.setToY(1.1);
            scale.setAutoReverse(true);
            scale.setCycleCount(2);
            pt.getChildren().add(scale);
        }
        pt.play();

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
                timerSecondsLeft--;
                if(timerSecondsLeft == 0) babyStepsTimer.cancel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        timerLabel.setText(String.format("%02d:%02d",timerSecondsLeft/60, timerSecondsLeft % 60));
                        if(timerSecondsLeft == 0) babyStepTimeout();
                    }
                });
            }
        }, 1000, 1000);

    }

    private void babyStepTimeout(){
        CodeObject oldcode = this.logikHandler.BabyStepBack();
        this.codeArea.setText(oldcode.getCode());
        this.testArea.setText(oldcode.getTest());

        Alert alert = new Alert(Alert.AlertType.ERROR, "You have to be faster!", ButtonType.CLOSE);
        alert.setContentText("Sorry, the time for this phase expired :( As this exercise is configured with a time limit for the green and red Phase, your code was reset. Try again and be faster this time! (maybe take some smaller steps...)");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();

        startBabyStepTimer();
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

        Image newImg = getImageOfPhase(newState);
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

    private Image getImageOfPhase(TDDState newState) {
        InputStream newResource = null;
        if(newState == TDDState.WRITE_FAILING_TEST) newResource = this.getClass().getResourceAsStream("red.png");
        if(newState == TDDState.MAKE_PASS_TEST) newResource = this.getClass().getResourceAsStream("green.png");
        if(newState == TDDState.REFACTOR) newResource = this.getClass().getResourceAsStream("black.png");
        if(newState == TDDState.WRITE_FAILING_ACCEPTANCE_TEST) newResource = this.getClass().getResourceAsStream("atdd.png");

        return new Image(newResource);
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

        if(!this.logikHandler.isATDD()){
            if(codeTabPane.getTabs().contains(atddTab)) codeTabPane.getTabs().remove(atddTab);
        }
    }

    public void nextPhase(){
        runAndUpdateGUI(false);
        if(this.logikHandler.switchState(this.logikHandler.getNextState())){
            this.switchStateAnimation(this.logikHandler.getState());
            this.applyStateToGUI();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "You can't move on right now", ButtonType.CLOSE);
            alert.setContentText("Sorry, you cant move on to the next phase. Please fulfill your task before! Check the Output for more details.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public void prevPhase(){
        if(this.logikHandler.getState() == TDDState.MAKE_PASS_TEST){
            this.logikHandler.switchState(TDDState.WRITE_FAILING_TEST);
        }else{
            this.logikHandler.switchState(TDDState.WRITE_FAILING_ACCEPTANCE_TEST);
        }

        this.switchStateAnimation(this.logikHandler.getState());
        this.applyStateToGUI();
    }


}
