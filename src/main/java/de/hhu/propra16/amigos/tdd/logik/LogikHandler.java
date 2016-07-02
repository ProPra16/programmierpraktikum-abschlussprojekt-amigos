package de.hhu.propra16.amigos.tdd.logik;

import de.hhu.propra16.amigos.tdd.xml.Katalog;
import vk.core.api.CompilationUnit;
import vk.core.api.TestFailure;
import vk.core.internal.InternalCompiler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class LogikHandler {
    private Katalog katalog;

    private Collection<TestFailure> latestFailures;

    public LogikHandler(Katalog pKatalog) {
        katalog = pKatalog;
    }

    /**
     * Zum jetzigen Zeitpunkt gilt, dass für das
     * korrekte Ausführen der Funktion die Änderung
     * durch den GUI-Controller gespeichert sind.
     *
     * Im Falle true haben alle Tests bestanden.
     */
    public boolean pruefeAbgabe(int exercise) {

        ArrayList<CompilationUnit> units = new ArrayList<>();
        for(String key : katalog.getClasses(exercise).keySet()) {
            units.add(new CompilationUnit(key, katalog.getClasses(exercise).get(key), false));
        }

        for(String key : katalog.getTests(exercise).keySet()) {
            units.add(new CompilationUnit(key, katalog.getTests(exercise).get(key),true));
        }

        InternalCompiler compiled = new InternalCompiler((CompilationUnit[]) units.toArray());

        compiled.compileAndRunTests();
        latestFailures = compiled.getTestResult().getTestFailures();

        return compiled.getTestResult().getNumberOfFailedTests() == 0;
    }

    public Collection<TestFailure> getLatestFailures() {
        return latestFailures;
    }
}
