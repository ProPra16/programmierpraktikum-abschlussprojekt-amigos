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

    CodeObject aktuell;

    /*String code;
    CompilationUnit codeUnit;

    String test;
    CompilationUnit testUnit;*/

    String aTDDTest;
    CompilationUnit aTDDTestUnit;

    public LogikHandler(Exercise pAufgabe) {

        aufgabe = pAufgabe;

        lastPassed = new CodeObject();
        aktuell = new CodeObject();

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
        try {
            aktuell.setCode((String) aufgabe.getClasses().keySet().toArray()[0], pCode);
        }

        catch(Exception e) {
            // Exception wird im unwahrscheinlichen Fall gefangen
        }
    }

    public void setTest(String pTest){
        try {
            aktuell.setCode((String) aufgabe.getTests().keySet().toArray()[0], pTest);
        }

        catch(Exception e) {
            // Exception wird im unwahrscheinlichen Fall gefangen
        }
    }

    public TDDState getState(){
        return status;
    }

    public boolean switchState(TDDState newState){
        if(!aTDD) {

            if (status == TDDState.REFACTOR) {
                if (newState == TDDState.WRITE_FAILING_TEST && tryCompileTest() && tryCompileCode() && !isOneTestFailing()) {
                    lastPassed.convertToValuesOf(aktuell);
                    status = newState;
                    return true;
                }
            }

            if (status == TDDState.WRITE_FAILING_TEST) {
                if (newState == TDDState.MAKE_PASS_TEST && isOneTestFailing()) {
                    status = newState;
                    return true;
                }

                if (newState == TDDState.REFACTOR) {
                    status = newState;
                    return true;
                }
            }

            if (status == TDDState.MAKE_PASS_TEST) {
                if (newState == TDDState.WRITE_FAILING_TEST) {
                    status = newState;
                    return true;
                }

                if (newState == TDDState.REFACTOR && tryCompileCode() && tryCompileTest() && !isOneTestFailing()) {
                    lastPassed.convertToValuesOf(aktuell);
                    status = newState;
                    return true;
                }
            }
        }

        else {
            if(status == TDDState.WRITE_FAILING_ACCEPTANCE_TEST) {
                if(newState == TDDState.REFACTOR) {
                    status = newState;
                    return true;
                }

                if(newState == TDDState.WRITE_FAILING_TEST && !isATDDpassing()) {
                    status = newState;
                    return true;
                }
            }

            if(status == TDDState.WRITE_FAILING_TEST) {
                if(newState == TDDState.WRITE_FAILING_ACCEPTANCE_TEST) {
                    status = newState;
                    return true;
                }
                if(newState == TDDState.MAKE_PASS_TEST && isOneTestFailing()) {
                    status = newState;
                    return true;
                }
            }

            if(status == TDDState.MAKE_PASS_TEST) {
                if(newState == TDDState.WRITE_FAILING_TEST) {
                    status = newState;
                    return true;
                }

                if(newState == TDDState.REFACTOR && !isOneTestFailing()) {
                    lastPassed.convertToValuesOf(aktuell);
                    status = newState;
                    return true;
                }
            }

            if(status == TDDState.REFACTOR) {
                if(newState == TDDState.WRITE_FAILING_ACCEPTANCE_TEST && !isOneTestFailing()) {
                    lastPassed.convertToValuesOf(aktuell);
                    status = newState;
                    return true;
                }
            }


        }

        return false;
    }

    public boolean isATDD(){
        return aTDD;
    }

    public boolean isATDDpassing(){
        if(aTDDTest == null || aTDDTestUnit == null || aktuell.getCodeUnit() == null)
            return false;

        if(!tryCompileCode())
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

    public boolean setATDDTest(String pTest) {
        InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[] {new CompilationUnit(pTest, "ATDD", true), aktuell.getCodeUnit()});
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

    public CodeObject getAktuell() {
        return aktuell;
    }

    public CodeObject BabyStepBack(){
        aktuell.convertToValuesOf(lastPassed);
        return lastPassed;
    }

    public boolean tryCompileTest(){
        if(aktuell.getTestUnit() == null || aktuell.getCodeUnit() == null)
            return false;

        InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[] {aktuell.getTestUnit(), aktuell.getCodeUnit()});
        compileTest.compileAndRunTests();

        if(compileTest.getCompilerResult().hasCompileErrors()) {
            return false;

        }

        return true;

    }

    public boolean tryCompileCode(){
        if(aktuell.getCodeUnit() == null)
            return false;

        InternalCompiler compileTest = new InternalCompiler(new CompilationUnit[] {aktuell.getCodeUnit()});
        compileTest.compileAndRunTests();

        if(compileTest.getCompilerResult().hasCompileErrors()) {
            return false;

        }

        return true;

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

        TestFailure[] fails = (TestFailure[]) compileTest.getTestResult().getTestFailures().toArray();

        String[] rueckgabe = new String[fails.length];

        for(int i = 0; i < rueckgabe.length; i++) {
            rueckgabe[i] = fails[i].getMessage();
        }

        return rueckgabe;
    }
}
