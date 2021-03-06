package de.hhu.propra16.amigos.tdd.tests;

import de.hhu.propra16.amigos.tdd.logik.LogikHandler;
import de.hhu.propra16.amigos.tdd.logik.TDDState;
import de.hhu.propra16.amigos.tdd.xml.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class LogikHandlerTests {
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

    private void setupGoodBase() {
        classes.put("Code","Code");
        simpleHandler.setCode("public class Code { }");
        tests.put("Good","passing");
    }

    @Before
    public void initialise() {
        emptyExercise = new Exercise("name", "desc", classes, tests, options);
        simpleHandler = new LogikHandler(emptyExercise);
    }
    @Test
    public void tryCompileCode_no_code() {
        classes.put("test","test");
        simpleHandler.setCode("");
        assertTrue(simpleHandler.tryCompileCode()[0].contentEquals("nothing to compile"));
    }
    @Test
    public void tryCompileCode_bad_code() {
        classes.put("test","fail");
        simpleHandler.setCode("fail");
        assertFalse(simpleHandler.tryCompileCode() == null);
     }
    @Test
    public void tryCompileCode_good_code() {
        classes.put("Good","Good");
        simpleHandler.setCode("public class Good { }");
        assertTrue(simpleHandler.tryCompileCode() == null);
    }
    @Test
    public void tryCompileTest_no_code() {
        tests.put("test","fail");
        simpleHandler.setTest("");
        assertFalse(simpleHandler.tryCompileTest() == null);
    }
    @Test
    public void tryCompileTest_bad_test() {
        tests.put("test","fail");
        simpleHandler.setTest("fail");
        assertFalse(simpleHandler.tryCompileTest() == null);
    }
    @Test
    public void isOneTestFailing_true() {
        classes.put("Code","Code");
        simpleHandler.setCode("public class Code { }");
        tests.put("Fail","Fail");
        simpleHandler.setTest(oneFailingTest);
        assertTrue(simpleHandler.isOneTestFailing());
    }
    @Test
    public void isOneTestFailing_false() {
        setupGoodBase();
        simpleHandler.setTest(onePassingTest);
        assertFalse(simpleHandler.isOneTestFailing());
    }
    @Test
    public void tryCompileTest_good_test_pass() {
        setupGoodBase();
        simpleHandler.setTest(onePassingTest);

        assertTrue(simpleHandler.tryCompileTest() == null);
    }
    @Test
    public void getFailingTests_get_none() {
        setupGoodBase();
        simpleHandler.setTest(onePassingTest);
        assertEquals(simpleHandler.getFailingTests().length,0);
    }
    @Test
    public void getFailingTests_get_one() {
        classes.put("Code","Code");
        simpleHandler.setCode("public class Code { }");
        tests.put("Fail","Fail");
        simpleHandler.setTest(oneFailingTest);
        assertEquals(simpleHandler.getFailingTests().length,1);
    }
    @Test
    public void getFailingTests_get_two() {
        classes.put("Code","Code");
        simpleHandler.setCode("public class Code { }");
        tests.put("Fail","Fail");
        simpleHandler.setTest(testImports +
                "public class Fail {  \n" +
                "@Test\n" +
                " public void fail() { \n" +
                "assertFalse(true);\n" +
                " } \n" +
                "@Test\n" +
                "public void fail2() {\n" +
                "assertTrue(false);\n " +
                "}\n" +
                "}\n");
        assertEquals(simpleHandler.getFailingTests().length,2);
    }
    @Test
    public void switchState_WRITE_FAILING_TEST_to_MAKE_PASS_TEST_fails() {
        setupGoodBase();
        simpleHandler.setTest(onePassingTest);
        assertFalse(simpleHandler.switchState(TDDState.MAKE_PASS_TEST));
    }
    @Test
    public void switchState_WRITE_FAILING_TEST_to_MAKE_PASS_TEST_pass() {
        setupGoodBase();
        simpleHandler.setTest(oneFailingTest);
        assertTrue(simpleHandler.switchState(TDDState.MAKE_PASS_TEST));
    }
    @Test
    public void switchState_WRITE_FAILING_TEST_to_REFACTOR_fails() {
        setupGoodBase();
        simpleHandler.setTest(oneFailingTest);
        assertFalse(simpleHandler.switchState(TDDState.REFACTOR));
    }
    @Test
    public void getNextState_from_WRITE_FAILING_TEST_pass() {
        setupGoodBase();
        simpleHandler.setTest(oneFailingTest);
        assertTrue(simpleHandler.getNextState()==TDDState.MAKE_PASS_TEST);
    }
    @Test
    public void switchState_MAKE_PASS_TEST_to_REFACTOR_fails() {
        setupGoodBase();
        simpleHandler.setTest(oneFailingTest);
        simpleHandler.switchState(TDDState.MAKE_PASS_TEST);
        assertFalse(simpleHandler.switchState(TDDState.REFACTOR));
    }
    @Test
    public void switchState_MAKE_PASS_TEST_to_REFACTOR_pass() {
        setupGoodBase();
        simpleHandler.setTest(oneFailingTest);
        simpleHandler.switchState(TDDState.MAKE_PASS_TEST);
        simpleHandler.setTest(onePassingTest);
        assertTrue(simpleHandler.switchState(TDDState.REFACTOR));
    }
    @Test
    public void switchState_MAKE_PASS_TEST_to_WRITE_FAILING_TEST_pass() {
        setupGoodBase();
        simpleHandler.setTest(oneFailingTest);
        simpleHandler.switchState(TDDState.MAKE_PASS_TEST);
        assertTrue(simpleHandler.switchState(TDDState.WRITE_FAILING_TEST));
    }
    @Test
    public void getNextState_from_MAKE_TEST_PASS_pass() {
        setupGoodBase();
        simpleHandler.setTest(oneFailingTest);
        simpleHandler.switchState(TDDState.MAKE_PASS_TEST);
        assertTrue(simpleHandler.getNextState()==TDDState.REFACTOR);
    }
    @Test
    public void switchState_REFACTOR_to_WRITE_FAILING_TEST_fails_from_test() {
        setupGoodBase();
        simpleHandler.setTest(oneFailingTest);
        simpleHandler.switchState(TDDState.MAKE_PASS_TEST);
        simpleHandler.setTest(onePassingTest);
        simpleHandler.switchState(TDDState.REFACTOR);
        simpleHandler.setTest(oneFailingTest);
        assertFalse(simpleHandler.switchState(TDDState.WRITE_FAILING_TEST));
    }
    @Test
    public void switchState_REFACTOR_to_WRITE_FAILING_TEST_fails_from_code() {
        setupGoodBase();
        simpleHandler.setTest(oneFailingTest);
        simpleHandler.switchState(TDDState.MAKE_PASS_TEST);
        simpleHandler.setTest(onePassingTest);
        simpleHandler.switchState(TDDState.REFACTOR);
        simpleHandler.setCode("public class Code {");
        assertFalse(simpleHandler.switchState(TDDState.WRITE_FAILING_TEST));
    }
    @Test
    public void switchState_REFACTOR_to_WRITE_FAILING_TEST_pass() {
        setupGoodBase();
        simpleHandler.setTest(oneFailingTest);
        simpleHandler.switchState(TDDState.MAKE_PASS_TEST);
        simpleHandler.setTest(onePassingTest);
        simpleHandler.switchState(TDDState.REFACTOR);
        assertTrue(simpleHandler.switchState(TDDState.WRITE_FAILING_TEST));
    }
    @Test
    public void switchState_REFACTOR_to_MAKE_PASS_TEST_fails() {
        setupGoodBase();
        simpleHandler.setTest(oneFailingTest);
        simpleHandler.switchState(TDDState.MAKE_PASS_TEST);
        simpleHandler.setTest(onePassingTest);
        simpleHandler.switchState(TDDState.REFACTOR);
        assertFalse(simpleHandler.switchState(TDDState.MAKE_PASS_TEST));
    }
    @Test
    public void getNextState_from_REFACTOR_pass() {
        setupGoodBase();
        simpleHandler.setTest(oneFailingTest);
        simpleHandler.switchState(TDDState.MAKE_PASS_TEST);
        simpleHandler.setTest(onePassingTest);
        simpleHandler.switchState(TDDState.REFACTOR);
        assertTrue(simpleHandler.getNextState()==TDDState.WRITE_FAILING_TEST);
    }
}
