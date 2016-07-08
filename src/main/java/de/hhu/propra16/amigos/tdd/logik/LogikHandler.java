package de.hhu.propra16.amigos.tdd.logik;

import de.hhu.propra16.amigos.tdd.xml.Exercise;
import vk.core.api.CompilationUnit;
import vk.core.api.TestFailure;
import vk.core.internal.InternalCompiler;

import java.util.HashMap;

public class LogikHandler implements LogikInterface{

    Exercise aufgabe;
    TDDState status;
    boolean aTDD;
    boolean babySteps;

    CodeObject lastPassed; // in case of babysteps

    String code;
    CompilationUnit codeUnit;

    String test;
    CompilationUnit testUnit;

    String aTDDTest;
    CompilationUnit aTDDTestUnit;

    public LogikHandler(Exercise pAufgabe) {

        aufgabe = pAufgabe;

        HashMap<String, String> options = aufgabe.getOptions();

        if(options.keySet().contains("ATDD"))
            aTDD = true;
        else
            aTDD = false;

        if(options.keySet().contains("babysteps"))
            babySteps = true;
        else
            babySteps = false;

        status = TDDState.WRITE_FAILING_TEST;


    }

    public void setCode(String pCode) {
        code = pCode;
        try {
            codeUnit = new CompilationUnit((String) aufgabe.getClasses().keySet().toArray()[0], code, false);
        }

        catch(Exception e) {
            // Exception wird im unwahrscheinlichen Fall gefangen
        }
    }

    public void setTest(String pTest){
        test = pTest;

        testUnit = new CompilationUnit((String) aufgabe.getTests().keySet().toArray()[0], test, true);
    }

    public TDDState getState(){
        return status;
    }

    public boolean switchState(TDDState newState){
        if(status == TDDState.MAKE_PASS_TEST) {
            if(newState == TDDState.WRITE_FAILING_TEST && tryCompileCode()) {
                status = newState;
                return true;
            }

            if(newState == TDDState.REFACTOR && tryCompileCode() && tryCompileTest() && !isOneTestFailing()) {
                status = newState;
                return true;
            }
        }

        if(status == TDDState.REFACTOR) {
            if(newState == TDDState.WRITE_FAILING_TEST && tryCompileTest() && tryCompileCode() && !isOneTestFailing()) {
                status = newState;
                lastPassed = new CodeObject(code, test); // It will also be stored if ATDD is not activated
                return true;
            }
        }

        if(status == TDDState.WRITE_FAILING_TEST) {
            if(newState == TDDState.MAKE_PASS_TEST && tryCompileTest() && isOneTestFailing()) {
                status = newState;
                return true;
            }
        }

        return false;
    }

    public boolean isATDD(){
        return aTDD;
    }

    public boolean isATDDpassing(){
        if(aTDDTest == null || aTDDTestUnit == null || codeUnit == null)
            return false;

        if(!tryCompileCode())
            return false;

        InternalCompiler compile = new InternalCompiler(new CompilationUnit[] {aTDDTestUnit, codeUnit});

        try {
            compile.compileAndRunTests();

            if(compile.getTestResult().getNumberOfFailedTests() != 0)
                return false;

            return true;
        } catch(Exception e) {
            return false;
        }

    }

    public boolean setATDDTest(String pTest) {
        InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[] {new CompilationUnit(pTest, "ATDD", true), codeUnit});
        compileTest.compileAndRunTests();

        if(compileTest.getCompilerResult().hasCompileErrors()) {
            return false;
        }

        aTDDTest = pTest;
        aTDDTestUnit = new CompilationUnit(aTDDTest, "ATDD", true);

        return true;
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

    public CodeObject BabyStepBack(){
        return lastPassed;
    }

    public boolean tryCompileTest(){
        if(testUnit == null || codeUnit == null)
            return false;

        InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[] {testUnit, codeUnit});
        compileTest.compileAndRunTests();

        if(compileTest.getCompilerResult().hasCompileErrors()) {
            return false;

        }

        return true;

    }

    public boolean tryCompileCode(){
        if(codeUnit == null)
            return false;

        InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[] {codeUnit});
        compileTest.compileAndRunTests();

        if(compileTest.getCompilerResult().hasCompileErrors()) {
            return false;

        }

        return true;

    }


    public boolean isOneTestFailing(){
        if(testUnit == null || codeUnit == null)
            return false;

        InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[] {testUnit, codeUnit});
        compileTest.compileAndRunTests();

        return compileTest.getTestResult().getNumberOfFailedTests() > 0;

    }

    public String[] getFailingTests(){
        if(testUnit == null || codeUnit == null)
            return new String[0];

        InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[] {testUnit, codeUnit});
        compileTest.compileAndRunTests();

        TestFailure[] fails = (TestFailure[]) compileTest.getTestResult().getTestFailures().toArray();

        String[] rueckgabe = new String[fails.length];

        for(int i = 0; i < rueckgabe.length; i++) {
            rueckgabe[i] = fails[i].getMethodName();
        }

        return rueckgabe;
    }
}
