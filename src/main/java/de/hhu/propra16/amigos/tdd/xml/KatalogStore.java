package de.hhu.propra16.amigos.tdd.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

public class KatalogStore {
    /**
     * Gilt als Vereinigung von KatalogLeser und SaveProgress
     * Bietet nur statische Methoden
     */

    /**
     *  Schmeißt Exceptions, damit Fehlermeldungen an anderen Stellen im Programm
     *  ausgegeben bzw. behandelt werden können
     */
    public static Katalog lese(FileInputStream f) throws Exception {
        if(f.available() == 0) {
            return new Katalog();
        }
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDokument = builder.parse(f);

            NodeList exercises = xmlDokument.getElementsByTagName("exercise");

            Katalog k = new Katalog();

            for (int i = 0; i < exercises.getLength(); i++) {
                Element aktuell = (Element) (exercises.item(i));
                String name = aktuell.getElementsByTagName("name").item(0).getTextContent().trim();
                String description = aktuell.getElementsByTagName("description").item(0).getTextContent().trim();

                HashMap<String, String> classes = new HashMap<>();
                for (int j = 0; j < aktuell.getElementsByTagName("class").getLength(); j++) {
                    classes.put(aktuell.getElementsByTagName("class").item(j).getAttributes().getNamedItem("name").getTextContent().trim(), aktuell.getElementsByTagName("class").item(j).getTextContent().trim());
                }

                HashMap<String, String> tests = new HashMap<>();
                for (int j = 0; j < aktuell.getElementsByTagName("test").getLength(); j++) {
                    tests.put(aktuell.getElementsByTagName("test").item(j).getAttributes().getNamedItem("name").getTextContent().trim(), aktuell.getElementsByTagName("test").item(j).getTextContent().trim());
                }

                HashMap<String, String> options = new HashMap<>();
                for (int j = 0; j < aktuell.getElementsByTagName("option").getLength(); j++) {
                    options.put(aktuell.getElementsByTagName("option").item(j).getAttributes().getNamedItem("name").getTextContent().trim(), aktuell.getElementsByTagName("option").item(j).getAttributes().getNamedItem("value").getTextContent().trim());
                }

                k.addExercise(name, description, classes, tests, options);
            }

            return k;
        }
        catch(Exception e) {
            return new Katalog();
        }
    }

    /**
     * @param k Übergibt den zu speichernden Katalog
     * @param f Übergibt den Ort zum Speichern jetzt als Stream
     */
    public static void save(Katalog k, FileOutputStream f) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document dokument = builder.newDocument();

            Element exercises = dokument.createElement("exercises");
            dokument.appendChild(exercises);

            for (int i = 0; i < k.size(); i++) {
                Element exercise = dokument.createElement("exercise");
                exercises.appendChild(exercise);

                Element name = dokument.createElement("name");
                name.appendChild(dokument.createTextNode(k.getName(i)));
                exercise.appendChild(name);

                Element description = dokument.createElement("description");
                description.appendChild(dokument.createTextNode(k.getDescription(i)));
                exercise.appendChild(description);

                Element classes = dokument.createElement("classes");
                exercise.appendChild(classes);

                for (String key : k.getClasses(i).keySet()) {
                    Element newClass = dokument.createElement("class");
                    newClass.setAttribute("name", key);
                    newClass.appendChild(dokument.createTextNode(k.getClasses(i).get(key)));
                    classes.appendChild(newClass);
                }

                Element tests = dokument.createElement("tests");
                exercise.appendChild(tests);

                for (String key : k.getTests(i).keySet()) {
                    Element newTest = dokument.createElement("test");
                    newTest.setAttribute("name", key);
                    newTest.appendChild(dokument.createTextNode(k.getTests(i).get(key)));
                    tests.appendChild(newTest);
                }

                Element options = dokument.createElement("options");
                exercise.appendChild(options);

                for (String key : k.getOptions(i).keySet()) {
                    Element newOption = dokument.createElement("option");
                    newOption.setAttribute("name", key);
                    newOption.setAttribute("value", k.getOptions(i).get(key));
                    options.appendChild(newOption);
                }

                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer();
                DOMSource quelle = new DOMSource(dokument);
                StreamResult ziel = new StreamResult(f);

                transformer.transform(quelle, ziel);
            }
        }
        catch (Exception e) {
            // Konsumierung
        }
    }
}
