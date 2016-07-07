package de.hhu.propra16.amigos.tdd.tests;

import de.hhu.propra16.amigos.tdd.logik.LogikHandler;
import de.hhu.propra16.amigos.tdd.xml.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class LogikHandlerTests {
    HashMap<String, String> classes = new HashMap<>();
    HashMap<String, String> tests = new HashMap<>();
    HashMap<String, String> options = new HashMap<>();
    private Exercise emptyExercise = new Exercise("name", "desc", classes, tests, options);
    private LogikHandler simpleHandler = new LogikHandler(emptyExercise);

    @Test
    public void test_tryCompileCode_no_code() {
        classes.put("test","fail");
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
   // @Test
}
