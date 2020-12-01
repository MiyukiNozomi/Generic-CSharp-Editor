package com.miyuki.cseditor.explorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.miyuki.cseditor.CSEditor;
import com.miyuki.cseditor.editor.Editor;

public class FileExplorerPopup extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	
	public FileExplorerPopup(JTree tree,FileExplorer e,FileExplorerRender r) {
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tree.getSelectionPath() != null){
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
					
					if(node.getUserObject() instanceof File){
						File file = (File) node.getUserObject();
						
						if(!file.isDirectory()){
							Editor editor = new Editor();
							editor.openFile(file);
							ImageIcon icon = r.unknownFile;
							if(file.getName().contains(".")){
								String ext = file.getName().substring(file.getName().indexOf(".") + 1);	
								if(r.icons.containsKey(ext)){
									icon = r.icons.get(ext);
								}
							}
							CSEditor.add(file.getName(),icon, editor);
						}
					}
				}
			}
		});
		add(mntmOpen);
		
		JMenuItem mntmReload = new JMenuItem("Reload");
		mntmReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				e.requestRefresh();
			}
		});
		add(mntmReload);
		
	}
}
