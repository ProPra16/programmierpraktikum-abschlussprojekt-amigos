package de.hhu.propra16.amigos.tdd.gui.controls;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.JavaFormatterOptions;
import org.fxmisc.richtext.PlainTextChange;

public class FormattableTDDCodeArea extends TDDCodeArea{
    private final Formatter formatter;
    private boolean lastCharAddedWasOpenBracket = false;

    public FormattableTDDCodeArea(){
        super();
        JavaFormatterOptions options = new JavaFormatterOptions(JavaFormatterOptions.JavadocFormatter.NONE, JavaFormatterOptions.Style.AOSP, JavaFormatterOptions.SortImports.NO);
        this.formatter = new Formatter(options);
        this.plainTextChanges().subscribe(this::bracketInsertListener);
    }

    public void formatCode(){
        try{
            this.setText(formatter.formatSource(this.getText()));
        }catch(Exception ex){}
    }

    private void bracketInsertListener(PlainTextChange change){
        if(this.lastCharAddedWasOpenBracket){
            this.lastCharAddedWasOpenBracket = false;
            if(change.getInserted().equals("\n")){
                this.insertText(change.getPosition(), "\n\n}");
                this.moveTo(this.getCaretPosition() - 2);
            }
        }else{
            if(change.getInserted().equals("{")) this.lastCharAddedWasOpenBracket = true;
        }
    }

}
