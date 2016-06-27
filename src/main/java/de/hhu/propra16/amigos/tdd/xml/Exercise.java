package de.hhu.propra16.amigos.tdd.xml;


import java.util.HashMap;

public class Exercise {
    private String name;
    private String description;
    private HashMap<String, String> classes;
    private HashMap<String, String> tests;


    public Exercise(String name, String description, HashMap<String, String> classes, HashMap<String, String> tests) {
        this.name = name;
        this.description = description;
        this.classes = classes;
        this.tests = tests;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<String, String> getClasses() {
        return classes;
    }

    public HashMap<String, String> getTests() {
        return tests;
    }

}