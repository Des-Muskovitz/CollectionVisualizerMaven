package com.colvis;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;


//inherites JTextPane
public class ElementDisplayer extends JTextPane {

    //Variable initalization
    private static StyledDocument styledDocument;
    private static final SimpleAttributeSet CENTER = new SimpleAttributeSet();


    //Constructor
    public ElementDisplayer(String text, int borderSize, Color color){
        super();

        //use standard borderLayout
        this.setLayout(new BorderLayout());

        //add inputed text
        this.setText(text);

        //Center align text
        styledDocument = this.getStyledDocument();
        StyleConstants.setAlignment(CENTER, StyleConstants.ALIGN_CENTER);
        styledDocument.setParagraphAttributes(0, styledDocument.getLength(), CENTER, false);

        //set colored border
        this.setBorder(BorderFactory.createLineBorder(color, borderSize));

        this.setEditable(false);
    }

}
