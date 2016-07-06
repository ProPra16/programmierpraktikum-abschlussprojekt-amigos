package de.hhu.propra16.amigos.tdd.logik;

import de.hhu.propra16.amigos.tdd.xml.Exercise;
import de.hhu.propra16.amigos.tdd.xml.Katalog;
import vk.core.api.CompilationUnit;
import vk.core.api.TestFailure;
import vk.core.internal.InternalCompiler;

import java.util.ArrayList;
import java.util.Collection;

public class LogikHandler implements LogikInterface{

    Exercise aufgabe;
    TDDState status;
    boolean aTDD;
    boolean babySteps;

    public LogikHandler(Exercise pAufgabe) {
        aufgabe = pAufgabe;

        aTDD.


    }

    public void setCode(String code) {

    }

    public void setTest(String test){

    }

    public TDDState getState(){

    }

    public boolean switchState(TDDState newState){

    }

    public boolean isATDD(){

    }

    public boolean isATDDpassing(){

    }

    public boolean isBabySteps(){

    }

    public CodeObject BabyStepBack(){

    }

    public boolean tryCompileTest(){

    }

    public boolean tryCompileCode(){

    }

    public boolean isOneTestFailing(){

    }

    public String[] getFailingTests(){

    }
}
