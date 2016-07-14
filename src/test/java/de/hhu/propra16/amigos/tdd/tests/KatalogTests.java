package de.hhu.propra16.amigos.tdd.tests;

import static org.junit.Assert.*;
import de.hhu.propra16.amigos.tdd.xml.Katalog;
import de.hhu.propra16.amigos.tdd.xml.KatalogStore;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class KatalogTests {
    private static Katalog testKatalog = new Katalog();
    private static final String oneRomanExercise = "<exercises>" +
            "    <exercise>\n" +
            "\n" +
            "        <name>\n" +
            "            Roman Numbers ( Babysteps )\n" +
            "        </name>\n" +
            "\n" +
            "        <description>\n" +
            "Your Code should convert arabic numbers into roman numbers.\n" +
            "Be careful: you only have 3 minutes for each phase except refactoring, so try to take small steps to avoid getting your code reset.\n" +
            "        </description>\n" +
            "\n" +
            "        <classes>\n" +
            "\n" +
            "            <class name=\"RomanNumberConverter\">\n" +
            "public class RomanNumberConverter {\n" +
            "    public static String convertToRoman(int input){\n" +
            "        //Implement your solution here\n" +
            "    }\n" +
            "}\n" +
            "            </class>\n" +
            "\n" +
            "            <tests>\n" +
            "\n" +
            "                <test name=\"RomanNumbersTest\">\n" +
            "import static org.junit.Assert.*;\n" +
            "import org.junit.Test;\n" +
            "\n" +
            "public class RomanNumbersTest {\n" +
            "\n" +
            "    @Test\n" +
            "    public void testSomething() {\n" +
            "        //implement tests for small steps here\n" +
            "    }\n" +
            "}\n" +
            "                </test>\n" +
            "\n" +
            "            </tests>\n" +
            "\n" +
            "        </classes>\n" +
            "\n" +
            "        <options>\n" +
            "\n" +
            "            <option name=\"babysteps\" value=\"60\" />\n" +
            "<option name=\"ATDD\" value=\"true\" />" + // modification
            "\n" +
            "        </options>\n" +
            "\n" +
            "    </exercise>\n" +
            "</exercises>"; // source: our catalog.xml

    @BeforeClass
    public static void initialise() {
        try {
           testKatalog = KatalogStore.lese(new ByteArrayInputStream(oneRomanExercise.getBytes(StandardCharsets.UTF_8)));
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
        final String twoExercises = "<exercises><exercise><name>test1</name><description>test</description><classes><class name=\"empty\">public class empty {}</class><tests><test name=\"empty\">public class empty {}</test></tests></classes><options></options></exercise>" +
                "<exercise><name>test2</name><description>test</description><classes><class name=\"empty\">public class empty {}</class><tests><test name=\"empty\">public class empty {}</test></tests></classes><options></options></exercise></exercises>";
        try {
            Katalog sizeTwoKatalog = KatalogStore.lese(new ByteArrayInputStream(twoExercises.getBytes(StandardCharsets.UTF_8)));
            assertEquals(2,sizeTwoKatalog.size());
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }

    }
}
