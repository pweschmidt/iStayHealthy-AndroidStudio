package com.pweschmidt.healthapps.documents;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by piddie on 29/11/14.
 */
public class XMLErrorHandler implements ErrorHandler
{
    private String type;

    @Override
    public void warning(SAXParseException e) throws SAXException
    {
        type = "WARNING";
        throw (e);
    }

    @Override
    public void error(SAXParseException e) throws SAXException
    {
        type = "ERROR";
        throw (e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException
    {
        type = "FATALERROR";
        throw (e);
    }

    public String getType(){return type;}
}
