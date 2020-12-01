package com.miyuki.cseditor.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;

import org.yaml.snakeyaml.Yaml;

import com.miyuki.cseditor.CSEditor;

public class CodeHelper extends JFrame {
	private static final long serialVersionUID = 1L;

	public ImageIcon method, field;
	public boolean isShowing = false;
	private Helps helps;
	private JList<HelperItem> list;

	public CodeHelper(JTextPane e) {
		super("Helper");

		method = new ImageIcon(
				CodeHelper.class
						.getResource("/com/miyuki/cseditor/resources/methpro_obj.png"));
		field = new ImageIcon(
				CodeHelper.class
						.getResource("/com/miyuki/cseditor/resources/methpri_obj.png"));

		setSize(597, 229);
		setResizable(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(CSEditor.class.getResource("/com/miyuki/cseditor/resources/html_obj.gif")));
		setUndecorated(true);

		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);

		list = new JList<HelperItem>();

		list.setCellRenderer(new ListCellRenderer<HelperItem>() {

			@Override
			public Component getListCellRendererComponent(
					JList<? extends HelperItem> list, HelperItem value,
					int index, boolean isSelected, boolean cellHasFocus) {

				JLabel label = new JLabel();

				label.setText(value.name);

				switch (value.type) {
				case "field":
					label.setIcon(field);
					break;
				default:
					label.setIcon(method);
				}

				if (isSelected)
					label.setForeground(Color.BLUE);

				return label;
			}
		});

		JScrollPane jscp = new JScrollPane(list);
		jscp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		splitPane.setLeftComponent(jscp);

		JTextPane textArea = new JTextPane();
		textArea.setFocusable(false);
		textArea.setEditable(false);
		textArea.setBackground(new Color(240, 230, 140));
		splitPane.setRightComponent(textArea);

		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() != KeyEvent.VK_UP
						&& arg0.getKeyCode() != KeyEvent.VK_DOWN
						&& arg0.getKeyCode() != KeyEvent.VK_ENTER) {
					hideHelper(e);
				}

				if (list.getSelectedValue() != null) {
					textArea.setText(list.getSelectedValue().description
							+ "\n source from: Unity 2017.3");
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
						//add stuff
					}
				}
			}
		});

		loadDocFile();
	}

	public void show(String variable, int x, int y) {
		isShowing = true;
		setLocation(x, y + 14);
		setVisible(true);
	}

	public void hideHelper(Component e) {
		isShowing = false;
		setVisible(false);
		e.requestFocus();
	}

	public void loadDocFile() {
		Yaml yaml = new Yaml();

		try {
			InputStream in = new FileInputStream("docs\\docs.yml");
			helps = yaml.loadAs(in, Helps.class);
			AbstractListModel<HelperItem> listXD = new AbstractListModel<CodeHelper.HelperItem>() {
				private static final long serialVersionUID = 1L;

				@Override
				public int getSize() {
					return helps.helps.size();
				}

				@Override
				public HelperItem getElementAt(int index) {
					return helps.helps.get(index);
				}
			};
			this.list.setModel(listXD);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class Helps {
		public List<HelperItem> helps;
	}

	public static class HelperItem {
		public String name, description, type;

		@Override
		public String toString() {
			return "name " + name + "\ndesc: " + description + "\ntype: "
					+ type;
		}
	}
}
