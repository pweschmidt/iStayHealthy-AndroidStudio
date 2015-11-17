package com.pweschmidt.healthapps.documents;


public class XMLAttribute {
	private String name;
	private String value;
    public static final String ampersand = "&";
    public static final String lt = "<";
    public static final String gt = ">";
    public static final String quote = "\"";
    public static final String singleQuote = "'";

    public static final String xmlAmp = "&amp;";
    public static final String xmlLt = "&lt;";
    public static final String xmlGt = "&gt;";
    public static final String xmlQuote = "&quot;";
    public static final String xmlApos = "&apos;";
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
	public String toXMLString()
    {
        if(null == value)
        {
            return "";
        }
        value = XMLAttribute.getEscapedXMLString(value);
		String xml = name + "=\"" + value + "\" ";
		return xml;
	}

    public static String getEscapedXMLString(String unescapedString)
    {
        if (null == unescapedString)
        {
            return "";
        }
        String escaped = unescapedString.replaceAll(XMLAttribute.ampersand, XMLAttribute.xmlAmp);
        escaped = escaped.replaceAll(XMLAttribute.lt,XMLAttribute.xmlLt);
        escaped = escaped.replaceAll(XMLAttribute.gt,XMLAttribute.xmlGt);
        escaped = escaped.replaceAll(XMLAttribute.quote,XMLAttribute.xmlQuote);
        escaped = escaped.replaceAll(XMLAttribute.singleQuote,XMLAttribute.xmlApos);
        return escaped;
    }

    public static String getUnescapedXMLString(String unescapedString)
    {
        String escaped = unescapedString.replaceAll(XMLAttribute.xmlAmp, XMLAttribute.ampersand);
        escaped = escaped.replaceAll(XMLAttribute.xmlLt, XMLAttribute.lt);
        escaped = escaped.replaceAll(XMLAttribute.xmlGt, XMLAttribute.gt);
        escaped = escaped.replaceAll(XMLAttribute.xmlQuote, XMLAttribute.quote);
        escaped = escaped.replaceAll(XMLAttribute.xmlApos, XMLAttribute.singleQuote);
        return escaped;
    }

}
