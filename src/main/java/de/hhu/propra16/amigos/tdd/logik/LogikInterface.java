package de.hhu.propra16.amigos.tdd.logik;


import vk.core.api.TestFailure;

import java.util.Collection;

public interface LogikInterface {
    /**
     * Eine Aufführung aller Funktionen,
     * die der LogikHandler aufführen muss.
     */

    public boolean pruefeAbgabe(int exercise);

    public Collection<TestFailure> getLatestFailures();

    public boolean isBabysteps(int exercise);

    public boolean isATDD(int exercise);

    /**
     * @return Gibt die Zeit für Babysteps zurück.
     * Im Falle, dass Babysteps gar nicht
     * gesetzt ist, wird -1 zurückgegeben.
     */
    public int giveTime(int exercise);

    /**
     * @return Alle Namen der Tests,
     * die nicht funktioniert haben.
     * Dabei geht es um den Namen der
     * Klasse. Eine Klasse kommt
     * dabei so oft vor, wie sie Fehler
     * hervorgebracht hat.
     */
    public String[] failedTests();

    /**
     * @param testName Name der Testklasse,
     *                 für die alle nicht
     *                 funktionierenden
     *                 Methoden ausgegeben
     *                 werden.
     * @return Alle nicht funktionierenden
     * Methoden für den Test. Es geht dabei
     * um die Methode in der Testklasse.
     */
    public String[] failedMethods(String testName);
}
