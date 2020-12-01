package com.miyuki.cseditor.editor;

import java.awt.Color;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Hightlight extends DefaultStyledDocument {
	private static final long serialVersionUID = 1L;
	
	private HashMap<String,AttributeSet> keywords;
	
	private final StyleContext cont = StyleContext.getDefaultStyleContext();
	private final AttributeSet black = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,Color.DARK_GRAY),
					   		   comment = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,new Color(20,230,20)),		
							   annotation = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,new Color(160,50,200)),	
							   fields = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,new Color(40,50,200)),		
							   type = cont.addAttribute(cont.getEmptySet(),StyleConstants.Foreground,new Color(120,120,200)),
							   //method = cont.addAttribute(cont.getEmptySet(),StyleConstants.Foreground,new Color(120,120,255)),
							   top_level = cont.addAttribute(cont.getEmptySet(),StyleConstants.Foreground,new Color(200,50,40)),
							   bold = cont.addAttribute(cont.getEmptySet(),StyleConstants.Bold,true);
	
	public Hightlight() {
		keywords = new HashMap<String,AttributeSet>();
		
		keywords.put("public|private|protected|static|yield|final|goto|break|return", fields);
		keywords.put("class|long|int|double|float|float2|float3|float4|bool|char|short|enum|interface|abstract|get|void|set", type);
		keywords.put("if|else|switch|for|while|struct|yield", type);
		keywords.put("yield|var|override", annotation);
		keywords.put("Raycast|Color|RaycastHit|Ray|Text|Slider|Header|NavMeshAgent|Physics|Terrian|Transform|Light|Vector3|Vector2|Camera|GameObject|Quaternion|Animation|Animator|AudioListener|AudioSource|Audio|Input|TimeSpan|List", type);
		keywords.put("using|new|true|false|this|super",top_level);
	}
	
    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        str = str.replaceAll("\t", "   ");
        super.insertString(offset, str, a);
        
        String text = getText(0, getLength());
		int before = findLastNonWordChar(text, offset);
		if (before < 0)
			before = 0;
		int after = findFirstNonWordChar(text, offset + str.length());
		int wordL = before;
		int wordR = before;

		while (wordR <= after) {
			if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {

				Pattern pattern = Pattern.compile("\\/\\/.*");
				Matcher matcher = pattern.matcher(text);

				while (matcher.find()) {
					setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), comment,
							false);
				}
				
				pattern = Pattern.compile("#.*");
				matcher = pattern.matcher(text);

				while (matcher.find()) {
					setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(),annotation,
							false);
					setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(),bold,
							false);
				}
				
				pattern = Pattern.compile("\\/\\*.*?\\*\\/", Pattern.DOTALL);
				matcher = pattern.matcher(text);

				while (matcher.find()) {
					setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), comment,
							false);
				}

				boolean setted = false;
				
				for(String key : keywords.keySet()){
					if(text.substring(wordL, wordR).matches("(\\W)*(" + key + ")")){
						setCharacterAttributes(wordL, wordR - wordL,keywords.get(key), false);
						setCharacterAttributes(wordL,wordR - wordL,bold,false);
						setted = true;
						break;
					}
				}
				
				if(!setted)
					setCharacterAttributes(wordL, wordR - wordL, black, true);
				
				wordL = wordR;
			}
			wordR++;
		}
    }
    
    private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}
}