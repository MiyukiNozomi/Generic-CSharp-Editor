package com.miyuki.cseditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import com.miyuki.cseditor.editor.Editor;
import com.miyuki.cseditor.explorer.FileExplorer;

public class CSEditor extends JFrame {
	private static final long serialVersionUID = 1L;

	private static JTabbedPane tabbedPane;

	private static ImageIcon file, close,image;

	public CSEditor(String folder) {
		super("C# Editor");
		file = new ImageIcon(
				CSEditor.class
						.getResource("/com/miyuki/cseditor/resources/file_plain_obj.png"));
		image = new ImageIcon(
				CSEditor.class
						.getResource("/com/miyuki/cseditor/resources/file-image.gif"));
		close = new ImageIcon(
				CSEditor.class
						.getResource("/com/miyuki/cseditor/resources/methpri_obj.png"));
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						CSEditor.class
								.getResource("/com/miyuki/cseditor/resources/html_obj.gif")));
		setSize(600, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane(new FileExplorer(folder));
		splitPane.setLeftComponent(scrollPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3)
					new TabbedPanePopup(tabbedPane).show(tabbedPane, e.getX(),
							e.getY());
			}
		});
		splitPane.setRightComponent(tabbedPane);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setIcon(new ImageIcon(
				CSEditor.class
						.getResource("/com/miyuki/cseditor/resources/file_plain_obj.png")));
		menuBar.add(mnFile);

		JMenuItem mntmo = new JMenuItem(":O");
		mnFile.add(mntmo);

		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setIcon(new ImageIcon(CSEditor.class
				.getResource("/com/miyuki/cseditor/resources/methdef_obj.png")));
		menuBar.add(mnEdit);

		JMenuItem mntmCreatedByMiyukinozomi = new JMenuItem(
				"Created By MiyukiNozomi");
		mnEdit.add(mntmCreatedByMiyukinozomi);
		
		CSEditor.add("Welcome",image,new JPanel(){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paint(Graphics g) {
				Dimension size = super.getSize();

				Graphics2D g2d = (Graphics2D) g;
				g.setColor(Color.WHITE);
				g.fillRect(0,0,size.width,size.height);
				Paint oldPaint = g2d.getPaint();
				GradientPaint gradient = new GradientPaint(50, 50,Color.ORANGE, 300, 100, new Color(190,0,0));
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, size.width,size.height);
				g2d.setPaint(oldPaint);
				g2d.setFont(new Font("Dialog",Font.BOLD,70));
				g2d.drawString("Welcome",size.width / 2 - 40,size.height / 2 + 70);
			
			}
		});
	}

	public static void setTitleAt(Editor id, String title) {
		TabComponent com = (TabComponent) tabbedPane.getTabComponentAt(tabbedPane.indexOfComponent(id));
		com.titleLbl.setText(title);
		com.repaint();
	}

	public static void add(String name, ImageIcon icon, Component e) {
		tabbedPane.addTab(name, file, e);
		if (icon != null)
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(e),
					new TabComponent(tabbedPane, e, name, icon));
		else
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(e),
					new TabComponent(tabbedPane, e, name, file));
	}

	public static class TabComponent extends JPanel {
		private static final long serialVersionUID = 1L;

		public JLabel titleLbl;
		public JLabel closeButton;
		
		public TabComponent(final JTabbedPane tabbedPane,
				final Component panel, String title, ImageIcon icon) {
			setOpaque(false);
			setLayout(new BorderLayout());
			titleLbl = new JLabel(title);
			titleLbl.setIcon(icon);
			titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			add(titleLbl, BorderLayout.WEST);
			closeButton = new JLabel();
			closeButton.setIcon(close);
			closeButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() != MouseEvent.BUTTON1)
						return;
					tabbedPane.remove(panel);
				}
			});
			add(closeButton, BorderLayout.EAST);
		}
	}
}
