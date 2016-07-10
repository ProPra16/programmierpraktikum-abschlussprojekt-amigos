package de.hhu.propra16.amigos.tdd.tests;

import de.hhu.propra16.amigos.tdd.logik.LogikHandler;
import de.hhu.propra16.amigos.tdd.xml.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class LogikHandlerTests {
    private String testImports = "import static org.junit.Assert.*;\n" +
            "import org.junit.Test;\n";
    HashMap<String, String> classes = new HashMap<>();
    HashMap<String, String> tests = new HashMap<>();
    HashMap<String, String> options = new HashMap<>();
    private Exercise emptyExercise;
    private LogikHandler simpleHandler;

    @Before
    public void initialise() {
        emptyExercise = new Exercise("name", "desc", classes, tests, options);
        simpleHandler = new LogikHandler(emptyExercise);
    }
    @Test
    public void test_tryCompileCode_no_code() {
        classes.put("test","test");
        simpleHandler.setCode("");
        assertFalse(simpleHandler.tryCompileCode());
    }
    @Test
    public void test_tryCompileCode_bad_code() {
        classes.put("test","fail");
        simpleHandler.setCode("fail");
        assertFalse(simpleHandler.tryCompileCode());
     }
    @Test
    public void test_tryCompileCode_good_code() {
        classes.put("Good","Good");
        simpleHandler.setCode("public class Good { }");
        assertTrue(simpleHandler.tryCompileCode());
    }
    @Test
    public void test_tryCompileTest_no_code() {
        tests.put("test","fail");
        simpleHandler.setTest("");
        assertFalse(simpleHandler.tryCompileTest());
    }
    @Test
    public void test_tryCompileTest_bad_test() {
        tests.put("test","fail");
        simpleHandler.setTest("fail");
        assertFalse(simpleHandler.tryCompileTest());
    }
    @Test
    public void test_isOneTestFailing_true() {
        classes.put("Code","Code");
        simpleHandler.setCode("public class Code { }");
        tests.put("Fail","Fail");
        simpleHandler.setTest(testImports +
                "public class Fail {  \n" +
                "@Test\n" +
                " public void fail() { \n" +
                "assertFalse(true);\n" +
                " } }");
        assertTrue(simpleHandler.isOneTestFailing());
    }
    @Test
    public void test_isOneTestFailing_false() {
        classes.put("Code","Code");
        simpleHandler.setCode("public class Code { }");
        tests.put("Fail","Fail");
        simpleHandler.setTest(testImports +
                "public class Fail {  \n" +
                "@Test\n" +
                " public void fail() { \n" +
                "assertTrue(true);\n" +
                " } }");
        assertFalse(simpleHandler.isOneTestFailing());
    }
    @Test
    public void test_tryCompileTest_good_test_pass() {
        classes.put("Code","Code");
        simpleHandler.setCode("public class Code { }");
        tests.put("Good","passing");
        simpleHandler.setTest(testImports +
                "public class Good { \n" +
                "@Test\n" +
                "public void passing() { \n" +
                " assertTrue(true);\n" +
                " } \n" +
                "}\n");

        assertTrue(simpleHandler.tryCompileTest());
    }

   // @Test
}
