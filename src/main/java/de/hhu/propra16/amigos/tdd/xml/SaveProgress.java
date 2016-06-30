package de.hhu.propra16.amigos.tdd.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class SaveProgress {
    public static void save(Katalog k, File f) throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document dokument = builder.newDocument();

        Element exercises = dokument.createElement("exercises");
        dokument.appendChild(exercises);

        for(int i = 0; i < k.size(); i++){
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

            for(String key : k.getClasses(i).keySet()) {
                Element newClass = dokument.createElement("class");
                newClass.setAttribute("name", key);
                newClass.appendChild(dokument.createTextNode(k.getClasses(i).get(key)));
                classes.appendChild(newClass);
            }

            Element tests = dokument.createElement("tests");
            exercise.appendChild(tests);

            for(String key : k.getTests(i).keySet()) {
                Element newTest = dokument.createElement("test");
                newTest.setAttribute("name", key);
                newTest.appendChild(dokument.createTextNode(k.getTests(i).get(key)));
                tests.appendChild(newTest);
            }

            Element options = dokument.createElement("options");
            exercise.appendChild(options);

            for(String key : k.getOptions(i).keySet()) {
                Element newOption = dokument.createElement("option");
                newOption.setAttribute("name", key);
                newOption.setAttribute("value", Boolean.toString(k.getOptions(i).get(key)));
                options.appendChild(newOption);
            }

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            DOMSource quelle = new DOMSource(dokument);
            StreamResult ziel = new StreamResult(f);

            transformer.transform(quelle, ziel);
        }
    }
}
