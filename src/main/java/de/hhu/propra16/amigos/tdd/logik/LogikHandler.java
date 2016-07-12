package de.hhu.propra16.amigos.tdd.logik;

import de.hhu.propra16.amigos.tdd.xml.Exercise;
import vk.core.api.CompilationUnit;
import vk.core.api.CompileError;
import vk.core.api.TestFailure;
import vk.core.internal.InternalCompiler;

import java.util.HashMap;

public class LogikHandler {

    Exercise aufgabe;
    TDDState status;
    boolean aTDD;
    boolean babySteps;

    CodeObject lastPassed;

    CodeObject aktuell;

    String aTDDTest;
    CompilationUnit aTDDTestUnit;

    public LogikHandler(Exercise pAufgabe) {

        aufgabe = pAufgabe;

        lastPassed = new CodeObject();
        aktuell = new CodeObject();

        HashMap<String, String> options = aufgabe.getOptions();

        aTDD = options.keySet().contains("ATDD");
        babySteps = options.keySet().contains("babysteps");

        status = TDDState.WRITE_FAILING_TEST;
        if(aTDD) status = TDDState.WRITE_FAILING_ACCEPTANCE_TEST;

    }

    public void setCode(String pCode) {
        try {
            aktuell.setCode((String) aufgabe.getClasses().keySet().toArray()[0], pCode);
        }

        catch(Exception e) {
            //
        }
    }

    public void setTest(String pTest){
        try {
            aktuell.setTest((String) aufgabe.getTests().keySet().toArray()[0], pTest);
        }

        catch(Exception e) {
            // Exception wird im unwahrscheinlichen Fall gefangen
        }
    }

    public TDDState getState(){
        return status;
    }

    public boolean switchState(TDDState newState){
        if(aTDD && newState == TDDState.WRITE_FAILING_ACCEPTANCE_TEST) {
            status = TDDState.WRITE_FAILING_ACCEPTANCE_TEST;
            return true;
        }

        if(newState != getNextState() && newState != TDDState.WRITE_FAILING_TEST)
            return false;

        if(newState == TDDState.WRITE_FAILING_TEST) {
            status = TDDState.WRITE_FAILING_TEST;
            return true;
        }

        // From now on, a state can only be a next state as given by getNextState()

        if(newState == TDDState.REFACTOR) {
            if(tryCompileTest() == null && tryCompileCode() == null) {
                status = newState;
                return true;
            }

            else return false;
        }

        status = newState;
        return true;
    }

    public boolean isATDD(){
        return aTDD;
    }

    public boolean isATDDpassing(){
        if(aTDDTest == null || aTDDTestUnit == null || aktuell.getCodeUnit() == null)
            return false;

        if(!(tryCompileCode() == null))
            return false;

        InternalCompiler compile = new InternalCompiler(new CompilationUnit[] {aTDDTestUnit, aktuell.getCodeUnit()});

        try {
            compile.compileAndRunTests();

            if(compile.getTestResult().getNumberOfFailedTests() != 0)
                return false;

            return true;
        } catch(Exception e) {
            return false;
        }

    }

    public String[] setATDDTest(String pTest) {
        CompilationUnit testATDD = new CompilationUnit("ATDD", pTest, true);

        InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[] {testATDD, aktuell.getCodeUnit()});
        compileTest.compileAndRunTests();

        if(!compileTest.getCompilerResult().hasCompileErrors()) {
            aTDDTest = pTest;
            aTDDTestUnit = new CompilationUnit("ATDD", aTDDTest, true);

            return null;
        }

        Object[] errorObjects = compileTest.getCompilerResult().getCompilerErrorsForCompilationUnit(testATDD).toArray();
        String[] errors = new String[errorObjects.length];

        for(int i = 0; i < errors.length; i++) {
            errors[i] = ((CompileError) errorObjects[i]).toString();
        }

        return errors;
    }

    public boolean isBabySteps(){
        return babySteps;
    }

    public int babyStepsTime() {
        if(babySteps) {
            HashMap<String, String> options = aufgabe.getOptions();
            try {
                return Integer.parseInt(options.get("babysteps"));
            }
            catch(Exception e) {
                return -1;
            }
        }

        else return -2;
    }

    public CodeObject getAktuell() {
        return aktuell;
    }

    public CodeObject BabyStepBack(){
        aktuell.convertToValuesOf(lastPassed);
        return lastPassed;
    }

    public String[] tryCompileTest(){
        if(aktuell.getTestUnit() == null || aktuell.getCodeUnit() == null)
            return new String[] {"nothing to compile"};

        InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[] {aktuell.getTestUnit(), aktuell.getCodeUnit()});
        compileTest.compileAndRunTests();

        if(!compileTest.getCompilerResult().hasCompileErrors()) {
            return null;

        }

        Object[] errorObjects = compileTest.getCompilerResult().getCompilerErrorsForCompilationUnit(aktuell.getTestUnit()).toArray();
        String[] errors = new String[errorObjects.length];

        for(int i = 0; i < errors.length; i++) {
            errors[i] = ((CompileError) errorObjects[i]).toString();
        }

        return errors;  // Note that this will only return the messages of the compilation of the test

    }

    public String[] tryCompileCode(){
        if(aktuell.getCodeUnit() == null || aktuell.getCode().isEmpty())
            return new String[] {"nothing to compile"};

        InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[] {aktuell.getCodeUnit()});
        compileTest.compileAndRunTests();

        if(!compileTest.getCompilerResult().hasCompileErrors()) {
            return null;

        }

        Object[] errorObjects = compileTest.getCompilerResult().getCompilerErrorsForCompilationUnit(aktuell.getCodeUnit()).toArray();
        String[] errors = new String[errorObjects.length];

        for(int i = 0; i < errors.length; i++) {
            errors[i] = ((CompileError) errorObjects[i]).toString();
        }

        return errors;

    }


    public boolean isOneTestFailing(){
        try {
            if (aktuell.getTestUnit() == null || aktuell.getCodeUnit() == null)
                return false;

            InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[]{aktuell.getTestUnit(), aktuell.getCodeUnit()});
            compileTest.compileAndRunTests();

            return compileTest.getTestResult().getNumberOfFailedTests() > 0;
        } catch (Exception e) {
            return true;
        }

    }

    public String[] getFailingTests(){
        if(aktuell.getTestUnit() == null || aktuell.getCodeUnit() == null)
            return new String[0];

        InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[] {aktuell.getTestUnit(), aktuell.getCodeUnit()});
        compileTest.compileAndRunTests();

        Object[] failObjects = compileTest.getTestResult().getTestFailures().toArray();
        TestFailure[] fails = new TestFailure[failObjects.length];

        for(int i = 0; i < fails.length; i++) {
            fails[i] = (TestFailure) failObjects[i];
        }

        String[] rueckgabe = new String[fails.length];

        for(int i = 0; i < rueckgabe.length; i++) {
            if(rueckgabe[i] == null)
            rueckgabe[i] = fails[i].getMethodName();
	    else
	    rueckgabe[i] = fails[i].getMessage();
        }

        return rueckgabe;
    }

    public TDDState getNextState() {
        if(status == TDDState.WRITE_FAILING_TEST) return TDDState.MAKE_PASS_TEST;
        if(status == TDDState.MAKE_PASS_TEST) return TDDState.REFACTOR;
        if(status == TDDState.REFACTOR && aTDD) return TDDState.WRITE_FAILING_ACCEPTANCE_TEST;
        if(status == TDDState.REFACTOR && !aTDD) return TDDState.WRITE_FAILING_TEST;
        if(status == TDDState.WRITE_FAILING_ACCEPTANCE_TEST) return TDDState.WRITE_FAILING_TEST;

        return null;
    }
}
