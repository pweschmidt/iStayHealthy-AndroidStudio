package com.pweschmidt.healthapps.documents;

import java.util.*;

public class XMLElement {
//	private static final String TAG = "XMLElement";
	private int nodeLevel;
	private String elementName;
	private ArrayList<XMLAttribute> attributes = new ArrayList<XMLAttribute>();
	private ArrayList<XMLElement> children = new ArrayList<XMLElement>();
	private static final String NEWLINE = "\n";
	private static final String TAB = "\t";
	private static final String BLANK = " ";
	private static final String OPEN = "<";
	private static final String OPENCLOSE = "</";
	private static final String CLOSE = ">";
	
	/**
	 * 
	 * @param name
	 */
	public XMLElement(String name){
		elementName = name;
		nodeLevel = 0;
	}

	/**
	 * 
	 * @param name
	 * @param level
	 */
	public XMLElement(String name, int level){
		elementName = name;
		nodeLevel = level;
	}

	/**
	 * 
	 * @return
	 */
	public int getLevel(){return nodeLevel;}
	/**
	 * 
	 * @param level
	 */
	public void setLevel(int level){nodeLevel = level;}
	
	/**
	 * 
	 * @param child
	 */
	public void addChild(XMLElement child){
		children.add(child);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void createChildElement(String name){
		XMLElement newChild = new XMLElement(name, nodeLevel + 1);
		children.add(newChild);
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void addAttribute(String name, String value){
		XMLAttribute attribute = new XMLAttribute(name, value);
		attributes.add(attribute);
	}
	
	/**
	 * 
	 * @return
	 */
	public String toXMLString(){
//		Log.d(TAG,"XML element "+elementName+" toXMLString");
		StringBuffer elementBuffer = new StringBuffer();
		String tabs = getTabs();
		elementBuffer.append(tabs);
		elementBuffer.append(OPEN+elementName+BLANK);

		Iterator <XMLAttribute> attIterator = attributes.iterator();
		while(attIterator.hasNext()){
			XMLAttribute attribute = (XMLAttribute)attIterator.next();
			String toString = attribute.toXMLString();
			if(null != toString)
			{
				if (!toString.equals(""))
				{
					elementBuffer.append(attribute.toXMLString());
				}
			}
		}
		elementBuffer.append(CLOSE);
		elementBuffer.append(NEWLINE);
		
		Iterator <XMLElement> elementIterator = children.iterator();
		while(elementIterator.hasNext()){
			XMLElement child = (XMLElement)elementIterator.next();
			elementBuffer.append(NEWLINE);
			elementBuffer.append(child.toXMLString());
		}
		
		elementBuffer.append(NEWLINE);
		elementBuffer.append(tabs);
		elementBuffer.append(OPENCLOSE+elementName+CLOSE);
		return elementBuffer.toString();
	}

	/**
	 * 
	 * @return
	 */
	private String getTabs(){
		StringBuffer tabBuffer = new StringBuffer();
		for(int i = 0 ; i < nodeLevel; ++i){
			tabBuffer.append(TAB);
		}
		return tabBuffer.toString();
	}
	
	
}
