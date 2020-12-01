package com.miyuki.cseditor.parser;

import java.util.HashMap;

import javax.swing.ImageIcon;

import com.miyuki.cseditor.TextLineNumber;

public class Parser {
	
	public static ImageIcon warning = loadImage("methpro_obj.png"),
							error = loadImage("methpri_obj.png"),
							classIcon = loadImage("class_obj.png"),
							enumIcon = loadImage("enum_obj.png"),
							interfaceIcon = loadImage("int_obj.png"),
							annotationIcon = loadImage("annotation_obj.png"),						
							info = loadImage("methpub_obj.png");
	
	private static HashMap<String,ImageIcon> persets = new HashMap<String,ImageIcon>();
	
	static {
		persets.put("else if",warning);
		persets.put("System.Attribute", annotationIcon);
		persets.put("class",classIcon);
		persets.put("interface",interfaceIcon);
		persets.put("enum",enumIcon);
	}
	
	public static void warn(TextLineNumber lineNumbers,String text){
		lineNumbers.warnings.clear();
		String[] lines = text.split("\\r?\\n");
		
		for(int il = 0; il < lines.length; il++){
			String l = lines[il].replace("   ","");
			if(l.startsWith("{"))
				lineNumbers.warnings.put(il + 1,error);
			else if(l.startsWith("//"))
				lineNumbers.warnings.put(il + 1,info);
			
			for(String str : persets.keySet()){
				if(l.contains(str)){
					lineNumbers.warnings.put(il + 1,persets.get(str));
					break;
				}
			}
		}
	}
	
	private static ImageIcon loadImage(String n ){
		return new ImageIcon(Parser.class.getResource("/com/miyuki/cseditor/resources/" + n));
	}
}
