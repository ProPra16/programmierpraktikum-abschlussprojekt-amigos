package de.hhu.propra16.amigos.tdd.tests;

import de.hhu.propra16.amigos.tdd.logik.LogikHandler;
import de.hhu.propra16.amigos.tdd.logik.TDDState;
import de.hhu.propra16.amigos.tdd.xml.Exercise;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class LogikHandlerTestsATDD {
    private final String testImports = "import static org.junit.Assert.*;\n" +
            "import org.junit.Test;\n";
    private final String onePassingTest = testImports +
            "public class Good {  \n" +
            "@Test\n" +
            " public void passing() { \n" +
            "assertTrue(true);\n" +
            " } }";
    private final String oneFailingTest = testImports +
            "public class Fail {  \n" +
            "@Test\n" +
            " public void fail() { \n" +
            "assertFalse(true);\n" +
            " } }";
    HashMap<String, String> classes = new HashMap<>();
    HashMap<String, String> tests = new HashMap<>();
    HashMap<String, String> options = new HashMap<>();
    private Exercise emptyExercise;
    private LogikHandler simpleHandler;

    @Before
    public void initialise() {
        classes.put("Code","Code");
        tests.put("Good","passing");
        options.put("ATDD","true");
        emptyExercise = new Exercise("name", "desc", classes, tests, options);
        simpleHandler = new LogikHandler(emptyExercise);
        simpleHandler.setCode("public class Code { }");
    }
    @Test
    public void switchState_getState_ATDD_pass() {
        assertTrue(simpleHandler.getState() == TDDState.WRITE_FAILING_ACCEPTANCE_TEST);
    }
    @Test
    public void switchState_WRITE_FAILING_ACCEPTANCE_TEST_to_REFACTOR_pass() {
        assertTrue(simpleHandler.switchState(TDDState.REFACTOR));
    }
    @Test
    public void switchState_WRITE_FAILING_ACCEPTANCE_TEST_to_WRITE_FAILING_TEST_pass() {
        assertTrue(simpleHandler.switchState(TDDState.WRITE_FAILING_TEST));
    }
    @Test
    public void switchState_WRITE_FAILING_ACCEPTANCE_TEST_to_WRITE_FAILING_TEST_fails() {
        simpleHandler.setATDDTest(testImports +
                "public class ATDD {  \n" +
                "@Test\n" +
                " public void passing() { \n" +
                "assertTrue(true);\n" +
                " } }");
        assertFalse(simpleHandler.switchState(TDDState.WRITE_FAILING_TEST));
    }
}
