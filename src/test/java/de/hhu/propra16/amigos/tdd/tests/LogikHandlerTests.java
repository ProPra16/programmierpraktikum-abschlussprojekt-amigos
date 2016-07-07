package de.hhu.propra16.amigos.tdd.tests;

import de.hhu.propra16.amigos.tdd.logik.LogikHandler;
import de.hhu.propra16.amigos.tdd.xml.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class LogikHandlerTests {
    private Exercise emptyExercise = new Exercise("", "", new HashMap<>(),new HashMap<>(), new HashMap<>());
    private LogikHandler simpleHandler = new LogikHandler(emptyExercise);

    @Test
    public void test_tryCompileCode() {
        simpleHandler.setCode("");
        assertTrue(simpleHandler.tryCompileCode());
     }
   // @Test
}
