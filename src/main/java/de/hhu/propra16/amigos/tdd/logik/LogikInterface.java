package de.hhu.propra16.amigos.tdd.logik;

import de.hhu.propra16.amigos.tdd.xml.Exercise;
import vk.core.api.TestFailure;

import java.util.Collection;

public interface LogikInterface {

    // Información: Interface nicht für die Finale Version vorgesehen. Beim Code aufräumen auf jeden Fall entfernen, sonst sieht er noch aus wie bei Hempels hinter der Firewall.

    public void setCode(String code);

    public void setTest(String test);

    public TDDState getState();

    public boolean switchState(TDDState newState);

    public boolean isATDD();

    public boolean isATDDpassing();

    public boolean isBabySteps();

    public boolean setATDDTest(String pTest);

    public CodeObject BabyStepBack();

    public boolean tryCompileTest();

    public boolean tryCompileCode();

    public boolean isOneTestFailing();

    public String[] getFailingTests();

}
