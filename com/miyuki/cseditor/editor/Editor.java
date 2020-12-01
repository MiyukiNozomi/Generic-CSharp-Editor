package com.miyuki.cseditor.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Caret;
import javax.swing.text.Utilities;

import com.miyuki.cseditor.CSEditor;
import com.miyuki.cseditor.TextLineNumber;
import com.miyuki.cseditor.parser.Parser;

public class Editor extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTextPane document;
	private JTextField detail;
	private TextLineNumber lineNumbers;
	private File file;
	private CodeHelper helper;

	public Editor() {
		document = new JTextPane();
		document.setDocument(new Hightlight());
		document.setFont(new Font("Courier New", Font.PLAIN, 16));
		document.setCaretColor(Color.RED);
		document.setForeground(Color.DARK_GRAY);
		document.setSelectionColor(new Color(Color.CYAN.getRed(), Color.CYAN
				.getGreen(), Color.CYAN.getBlue(), 5));
		document.setSelectedTextColor(new Color(0,200,200));

		
		document.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent arg0) {
				try {
					int caretPos = document.getCaretPosition();
					int rowNum = (caretPos == 0) ? 1 : 0;
					for (int offset = caretPos; offset > 0;) {
						offset = Utilities.getRowStart(document, offset) - 1;
						rowNum++;
					}

					int offset = Utilities.getRowStart(document, caretPos);
					int colNum = caretPos - offset + 1;

					detail.setText("Line " + rowNum + " Col " + colNum);
					if (helper.isShowing)
						helper.hideHelper(Editor.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		document.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (helper.isShowing)
					helper.hideHelper(Editor.this);
			}
		});

		document.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {				
				if (helper.isShowing)
					helper.hideHelper(Editor.this);
				if (file != null)
					CSEditor.setTitleAt(Editor.this, "*" + file.getName());
				help(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				Parser.warn(lineNumbers, document.getText());
			}
		});

		KeyStroke save = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);
		KeyStroke helperKS = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,
				Event.CTRL_MASK);

		document.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(helperKS,
				"helperKeyStroke");
		document.getActionMap().put("helperKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Caret caret = document.getCaret();
				if (caret == null || caret.getMagicCaretPosition() == null) {
					System.out.println("NULL");
					return;
				}
				Point p = new Point(caret.getMagicCaretPosition());
				p.x += document.getLocationOnScreen().x;
				p.y += document.getLocationOnScreen().y;

				helper.show("", p.x, p.y);
			}
		});

		document.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(save,
				"saveKeyStroke");
		document.getActionMap().put("saveKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (file != null)
					CSEditor.setTitleAt(Editor.this, file.getName());
				saveFile();
			}
		});

		lineNumbers = new TextLineNumber(document);

		setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(document);
		scroll.setRowHeaderView(lineNumbers);
		add(scroll, BorderLayout.CENTER);

		detail = new JTextField();
		add(detail, BorderLayout.SOUTH);
		helper = new CodeHelper(document);

	}

	public void help(KeyEvent e) {

		String text = document.getText();
		int caret = document.getCaretPosition();
		if ((caret - 1) >= 0) {
			String textBefore = text.substring(0, caret);
			String textAfter = text.substring(caret);
			char before = text.charAt(caret - 1);
			
			if (before == '{' && e.getKeyCode() == KeyEvent.VK_ENTER) {
				String result = textBefore + "\n\n}" + textAfter;
				document.setText(result);
				document.setCaretPosition(caret + 1);
				e.consume();
			} else if (e.getKeyChar() == '"') {
				String result = textBefore + "\"" + textAfter;
				document.setText(result);
				document.setCaretPosition(caret + 1);
				e.consume();
			} else if (e.getKeyChar() == '\'') {
				String result = textBefore + "\'" + textAfter;
				document.setText(result);
				document.setCaretPosition(caret + 1);
				e.consume();
			}
		}
	}

	public void openFile(File f) {
		try {
			this.file = f;
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String total = "", line = "";

			while ((line = reader.readLine()) != null) {
				total += line + "\n";
			}

			reader.close();
			document.setText(total);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void saveFile() {
		try {
			if (file == null)
				return;

			Files.write(Paths.get(file.getPath()), document.getText()
					.getBytes());
			detail.setText("Saved!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public File getFile() {
		return file;
	}
}