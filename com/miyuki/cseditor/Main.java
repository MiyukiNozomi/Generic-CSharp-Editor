package com.miyuki.cseditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private boolean alreadyOpening;
	
	public Main(String workspace) {
		getContentPane().setBackground(Color.WHITE);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/com/miyuki/cseditor/resources/html_obj.gif")));
		setTitle("C# Editor Launcher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(537,190);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(new ImageIcon(Main.class.getResource("/com/miyuki/cseditor/resources/html_obj.gif")).getImage().getScaledInstance(110,100,0)));
		label.setBounds(0, 51, 110, 110);
		getContentPane().add(label);
		
		textField = new JTextField();
		textField.setBounds(120, 52, 353, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(alreadyOpening)
					return;
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(chooser.showOpenDialog(Main.this) == JFileChooser.APPROVE_OPTION){
					try{
						Files.write(Paths.get("config.txt"),chooser.getSelectedFile().getPath().getBytes());
					}catch(Exception er){
						JOptionPane.showMessageDialog(Main.this,er.getMessage());
					}
					alreadyOpening = true;
					new CSEditor(chooser.getSelectedFile().getPath()).setVisible(true);
					setVisible(false);
				}
			}
		});
		btnNewButton.setIcon(new ImageIcon(Main.class.getResource("/com/miyuki/cseditor/resources/methdef_obj.png")));
		btnNewButton.setBounds(483, 51, 39, 23);
		getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Launch");
		btnNewButton_1.setBounds(433, 127, 89, 23);
		getContentPane().add(btnNewButton_1);
		
		JLabel lblOhNo = new JLabel(workspace);
		lblOhNo.setForeground(Color.BLUE);
		lblOhNo.setBounds(120, 83, 353, 14);
		lblOhNo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(alreadyOpening)
					return;
				textField.setText(lblOhNo.getText());
			}
		});
		getContentPane().add(lblOhNo);
		
		JPanel panel = new JPanel(){
			private static final long serialVersionUID = 140240595049504943L;
			@Override
			public void paint(Graphics g) {
				Dimension size = super.getSize();

				Graphics2D g2d = (Graphics2D) g;
				g.setColor(Color.WHITE);
				g.fillRect(0,0,size.width,size.height);
				Paint oldPaint = g2d.getPaint();
				GradientPaint gradient = new GradientPaint(50, 50,Color.BLUE, 300, 100, Color.CYAN);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, size.width, 40);
				g2d.setPaint(oldPaint);
				g2d.setFont(new Font("Dialog",Font.PLAIN,16));
				g2d.drawString("Select your unity project folder",10,size.height / 2 + 8);
			}
		};
		panel.setBounds(0, 0, 531, 40);
		getContentPane().add(panel);
		
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(alreadyOpening)
					return;
				File f = new File(textField.getText());
				
				if(f.exists()){
					if(f.isDirectory()){
						try{
							Files.write(Paths.get("config.txt"),f.getPath().getBytes());
						}catch(Exception er){
							JOptionPane.showMessageDialog(Main.this,er.getMessage());
						}
						alreadyOpening = true;
						new CSEditor(f.getPath()).setVisible(true);
						setVisible(false);
					}
				}
			}
		});
		
		setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		File f = new File("config.txt");
		String wk = "";

		if (f.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(f));

			String ln = "";
			while ((ln = reader.readLine()) != null) {
				wk += ln;
			}
			;

			reader.close();
		} else {
			wk = "oh no";
		}

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new Main(wk);
	}
}
