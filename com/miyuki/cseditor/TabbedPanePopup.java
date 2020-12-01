package com.miyuki.cseditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import com.miyuki.cseditor.editor.Editor;

public class TabbedPanePopup extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	
	public TabbedPanePopup(JTabbedPane pane) {
		
		JMenuItem mntmCloseWindow = new JMenuItem("Close Window");
		mntmCloseWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(pane.getSelectedComponent() == null)
					return;
				pane.remove(pane.getSelectedComponent());
			}
		});
		add(mntmCloseWindow);
		
		JMenuItem mntmCloseAll = new JMenuItem("Close All");
		mntmCloseAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pane.removeAll();
			}
		});
		add(mntmCloseAll);
		
		JMenuItem mntmOpenComment = new JMenuItem("Open Comment");
		mntmOpenComment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CSEditor.add("Comment",null,new Editor());	
			}
		});
		add(mntmOpenComment);
		
	}
}
