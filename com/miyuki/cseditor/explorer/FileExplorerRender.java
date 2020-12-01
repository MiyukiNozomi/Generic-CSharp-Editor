package com.miyuki.cseditor.explorer;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

public class FileExplorerRender implements TreeCellRenderer {
	
	public HashMap<String,ImageIcon> icons = new HashMap<String,ImageIcon>();
	public HashMap<String,ImageIcon> folders = new HashMap<String,ImageIcon>();
	
	public ImageIcon source_folder,folder,unknownFile,packageFolder; 
	
	public FileExplorerRender() {
		source_folder = open("open.png");
		folder = open("folder.gif");
		unknownFile = open("ascii_obj.png");
		packageFolder = open("package_obj.png");
		
		icons.put("cs",open("html_obj.gif"));
		ImageIcon imgIcon = open("file-image.gif"),
				 model = open("jar_obj.png");
		icons.put("png", imgIcon);
		icons.put("jpg", imgIcon);
		icons.put("bmp", imgIcon);
		icons.put("fbx", model);
		icons.put("obj", model);
		icons.put("blend", model);
		
		folders.put("Assets",open("packagefolder_obj.png"));
		folders.put("Scripts",open("packagefolder_obj.png"));
		folders.put("UnityPackageManager",open("packagefolder_obj.png"));
		}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		JLabel result = new JLabel();
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		
		if(node.getUserObject() instanceof String){
			result.setText((String) node.getUserObject());
			result.setIcon(source_folder);
		}else{
			File f = (File) node.getUserObject();
			result.setText(f.getName());
			if(f.isDirectory()){
				result.setToolTipText("Contains: " + f.listFiles().length + " Files\n"
						+ "Size: " + (f.length() / 1000) + "kb\n"
					    + "Path: " + f.getPath() + "\n"
						+ "Last Modified: " + f.lastModified());
				if(folders.containsKey(f.getName())){
					result.setIcon(folders.get(f.getName()));
				}else if(f.getPath().contains("Scripts")){
					result.setIcon(packageFolder);
				}else {
					result.setIcon(folder);
				}
			}else{
				result.setToolTipText("Size: " + (f.length() / 1000) + "kb\n"
					    + "Path: " + f.getPath() + "\n"
						+ "Last Modified: " + f.lastModified());
				String ext = f.getName().substring(f.getName().indexOf(".") + 1);	
				if(icons.containsKey(ext)){
					result.setIcon(icons.get(ext));
				}else{
					result.setIcon(unknownFile);
				}
			}
		}
		
		if(selected){
			result.setForeground(Color.BLUE);
		}else{
			result.setForeground(tree.getForeground());
		}
		
		return result;
	}
	
	final ImageIcon open(String name){
		return new ImageIcon(FileExplorerRender.class.getResource("/com/miyuki/cseditor/resources/" + name));
	}
}
