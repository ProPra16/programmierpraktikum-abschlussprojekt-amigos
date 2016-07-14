package de.hhu.propra16.amigos.tdd.tests;

import static org.junit.Assert.*;
import de.hhu.propra16.amigos.tdd.xml.Katalog;
import de.hhu.propra16.amigos.tdd.xml.KatalogStore;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class KatalogTests {
    private static Katalog testKatalog = new Katalog();
    private static final String oneTestExercise = "<exercises><exercise><name>test1</name>" +
            "<description>test description</description><classes><class name=\"empty1\">public class empty1 {}</class>" +
            "<tests><test name=\"empty2\">public class empty2 {}</test></tests></classes>" +
            "<options> <option name=\"babysteps\" value=\"60\" />" +
            "<option name=\"ATDD\" value=\"true\" />" +
            "</options></exercise></exercises>";

    @BeforeClass
    public static void initialise() {
        try {
           testKatalog = KatalogStore.lese(new ByteArrayInputStream(oneTestExercise.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sizeIsOne() {
        assertEquals(1,testKatalog.size());
    }
    @Test
    public void sizeIsTwo() {
        final String twoExercises = "<exercises><exercise><name>test1</name><description>test description</description><classes><class name=\"empty1\">public class empty1 {}</class><tests><test name=\"empty2\">public class empty2 {}</test></tests></classes><options></options></exercise>" +
                "<exercise><name>test2</name><description>test</description><classes><class name=\"empty\">public class empty {}</class><tests><test name=\"empty\">public class empty {}</test></tests></classes><options></options></exercise></exercises>";
        try {
            Katalog sizeTwoKatalog = KatalogStore.lese(new ByteArrayInputStream(twoExercises.getBytes(StandardCharsets.UTF_8)));
            assertEquals(2,sizeTwoKatalog.size());
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }
    @Test
    public void getName() {
        assertEquals("test1",testKatalog.getName(0));
    }
    @Test
    public void getDescription() {
        assertEquals("test description",testKatalog.getDescription(0));
    }
    @Test
    public void getClasses() {
        assertEquals("empty1",testKatalog.getClasses(0).keySet().toArray()[0]);
    }
    @Test
    public void getTests() {
        assertEquals("empty2",testKatalog.getTests(0).keySet().toArray()[0]);
    }
    @Test
    public void getOptionsATDD() {
        assertEquals("ATDD",testKatalog.getOptions(0).keySet().toArray()[1]);
    }
    @Test
    public void getOptionsBabysteps() {
        assertEquals("babysteps",testKatalog.getOptions(0).keySet().toArray()[0]);
    }
    @Test
    public void getExercise_notnull() {
        assertFalse(testKatalog.getExercise(0)==null);
    }
    @Test
    public void getExercise_null() {
        assertTrue(testKatalog.getExercise(1)==null);
    }
    @Test
    public void KatalogStoreSave() {
        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" " +
                "standalone=\"no\"?><exercises><exercise><name>test1</name><description>test description</description>" +
                "<classes><class name=\"empty1\">public class empty1 {}</class></classes><tests><test name=\"empty2\">public class empty2 {}</test></tests>" +
                "<options><option name=\"babysteps\" value=\"60\"/><option name=\"ATDD\" value=\"true\"/></options></exercise></exercises>";
        OutputStream stream = new ByteArrayOutputStream();
        KatalogStore.save(testKatalog, stream);
        assertEquals(expected,stream.toString());
    }
}
