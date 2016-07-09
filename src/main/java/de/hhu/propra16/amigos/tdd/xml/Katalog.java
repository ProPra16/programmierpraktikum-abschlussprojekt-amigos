package de.hhu.propra16.amigos.tdd.xml;

import java.util.ArrayList;
import java.util.HashMap;

public class Katalog {
    private ArrayList<Exercise> exercises;

    public Katalog() {
        exercises = new ArrayList<>();
    }

    public void addExercise(String name, String description, HashMap<String, String> classes, HashMap<String, String> tests, HashMap<String, String> options) {
        exercises.add(new Exercise(name, description, classes, tests, options));
    }

    public int size() {
        return exercises.size();
    }

    public String getName(int a) {
        return exercises.get(a).getName();
    }

    public String getDescription(int a) {
        return exercises.get(a).getDescription();
    }

    public HashMap<String, String> getClasses(int a) {
        return exercises.get(a).getClasses();
    }

    public HashMap<String, String> getTests(int a) {
        return exercises.get(a).getTests();
    }

    public HashMap<String, String> getOptions(int a) {
        return exercises.get(a).getOptions();
    }

    public Exercise getExercise(int a) {
        if(a >= 0 && a < exercises.size())
            return exercises.get(a);

        else
            return null;
    }

}