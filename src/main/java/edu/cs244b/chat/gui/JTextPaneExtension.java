package edu.cs244b.chat.gui;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.*;

/**
 * UI component to extend the JTextPane to support color and align
 *
 */
public class JTextPaneExtension extends JTextPane
{

	public void insert(String str, SimpleAttributeSet  attrSet)
	{
		StyledDocument doc = this.getStyledDocument();
		try
		{
			doc.setParagraphAttributes(doc.getLength(), 1, attrSet, false);
			doc.insertString(doc.getLength(), str, attrSet );
		} catch (BadLocationException e)
		{
			e.printStackTrace();
		}
	}

	public void append(String info, boolean isSelf)
	{
		if (isSelf) {
			SimpleAttributeSet right = new SimpleAttributeSet();
			StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
			StyleConstants.setForeground(right, Color.RED);
			insert(info, right);
		} else {
			SimpleAttributeSet left = new SimpleAttributeSet();
			StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
			StyleConstants.setForeground(left, Color.BLUE);
			insert(info, left);
		}
	}
}