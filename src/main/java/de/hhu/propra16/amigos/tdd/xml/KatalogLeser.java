package de.hhu.propra16.amigos.tdd.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class KatalogLeser {
    /**
     *  Schmeißt Exceptions, damit Fehlermeldungen an anderen Stellen im Programm
     *  ausgegeben bzw. behandelt werden können
     */

    public static Katalog lese(File f) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDokument = builder.parse(f);

        NodeList exercises = xmlDokument.getElementsByTagName("exercise");

        Katalog k = new Katalog();

        for(int i = 0; i < exercises.getLength(); i++) {
            Element aktuell = (Element) (exercises.item(i));
            String name = aktuell.getElementsByTagName("name").item(0).getTextContent().trim();
            String description = aktuell.getElementsByTagName("description").item(0).getTextContent().trim();

            HashMap<String, String> classes = new HashMap<>();
            for(int j = 0; j < aktuell.getElementsByTagName("class").getLength(); j++) {
                classes.put(aktuell.getElementsByTagName("class").item(j).getAttributes().getNamedItem("name").getTextContent().trim(), aktuell.getElementsByTagName("class").item(j).getTextContent().trim());
            }

            HashMap<String, String> tests = new HashMap<>();
            for(int j = 0; j < aktuell.getElementsByTagName("test").getLength(); j++) {
                tests.put(aktuell.getElementsByTagName("test").item(j).getAttributes().getNamedItem("name").getTextContent().trim(), aktuell.getElementsByTagName("test").item(j).getTextContent().trim());
            }

            HashMap<String, Boolean> options = new HashMap<>();
            for(int j = 0; j < aktuell.getElementsByTagName("option").getLength(); j++) {
                options.put(aktuell.getElementsByTagName("option").item(j).getAttributes().getNamedItem("name").getTextContent().trim(), Boolean.parseBoolean(aktuell.getElementsByTagName("option").item(j).getAttributes().getNamedItem("value").getTextContent().trim()));
            }

            k.addExercise(name, description, classes, tests, options);
        }

        return k;
    }
}