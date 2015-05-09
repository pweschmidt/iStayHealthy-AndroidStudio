package com.pweschmidt.healthapps.documents;

public class XMLAttribute {
	private String name;
	private String value;

	/**
	 * 
	 * @param _name
	 * @param _value
	 */
	public XMLAttribute(String _name, String _value){
		name = _name;
		value = _value;
	}

	/**
	 * 
	 * @param _name
	 * @return
	 */
	public boolean isAttribute(String _name){
		return name.equalsIgnoreCase(_name);
	}

	/**
	 * 
	 * @return
	 */
	public String getName(){return name;}
	
	/**
	 * 
	 * @return
	 */
	public String getValue(){return value;}
	
	/**
	 * 
	 * @return
	 */
	public String toXMLString(){
		String xml = name + "=\"" + value + "\" ";
		return xml;
	}
}
