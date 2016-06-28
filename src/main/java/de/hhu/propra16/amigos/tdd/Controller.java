package de.hhu.propra16.amigos.tdd;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    public SwingNode swingNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
                LanguageSupportFactory.get().register(textArea);
                textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                textArea.setCodeFoldingEnabled(true);
                RTextScrollPane sp = new RTextScrollPane(textArea);
                swingNode.setContent(sp);
                textArea.setText(String.join("\n", new String[] {
                        "package com.example;",
                        "",
                        "import java.util.*;",
                        "",
                        "public class Foo extends Bar implements Baz {",
                        "",
                        "    /*",
                        "     * Vorteile:",
                        "     * -automatische Einrückung",
                        "     * -setzt automatisch Klammern",
                        "     * -Code completion per Ctrl-Space",
                        "     * Nachteile:",
                        "     * -ist per Swing eingebettet, könnte instabil werden",
                        "     */",
                        "    public static void main(String[] args) {",
                        "        // single-line comment",
                        "        for(String arg: args) {",
                        "            if(arg.length() != 0)",
                        "                System.out.println(arg);",
                        "            else",
                        "                System.err.println(\"Warning: empty string as argument\");",
                        "        }",
                        "    }",
                        "",
                        "}"
                }));
            }
        });
    }
}
