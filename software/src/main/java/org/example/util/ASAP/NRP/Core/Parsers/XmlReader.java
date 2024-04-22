package org.example.util.ASAP.NRP.Core.Parsers;

import org.example.util.ASAP.NRP.Core.DateTime;
import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class XmlReader {
  public String Name = "";
  
  public XMLStreamReader reader;
  
  public XmlReader(InputStream inputStream) {
    try {
      this.reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
    } catch (Exception ex) {
      System.out.println("Exception @XmlReader Constructor : " + ex.getMessage());
    } 
    if (this.reader == null)
      System.out.println("Error: reader is null"); 
  }
  
  public void ReadStartElement() {
    ReadStartElement(null);
  }
  
  public void ReadStartElement(String name) {
    try {
      int eventType = this.reader.next();
      while ((eventType == 4 && this.reader.isWhiteSpace()) || (
        eventType == 12 && this.reader.isWhiteSpace()) || 
        eventType == 6 || 
        eventType == 3 || 
        eventType == 5)
        eventType = this.reader.next(); 
      if (eventType == 1 || 
        eventType == 2 || 
        eventType == 9)
        this.Name = this.reader.getLocalName(); 
    } catch (Exception ex) {
      System.out.println("Error @ ReadStartElement: " + ex.getMessage());
    } 
  }
  
  public void ReadEndElement() {
    try {
      if (this.reader.getEventType() == 2)
        ReadStartElement(); 
    } catch (Exception ex) {
      System.out.println("Error @ ReadEndElement: " + ex.getMessage());
    } 
  }
  
  public String GetAttribute(String name) {
    try {
      String val = this.reader.getAttributeValue(null, name);
      return val;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return null;
    } 
  }
  
  public DateTime ReadElementContentAsDateTime() {
    try {
      String date = this.reader.getElementText();
      DateTime dt = DateTime.ParseDate(date);
      ReadEndElement();
      return dt;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return null;
    } 
  }
  
  public boolean ReadElementContentAsBoolean() {
    try {
      boolean b = XmlConvert.ToBoolean(this.reader.getElementText());
      ReadEndElement();
      return b;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return false;
    } 
  }
  
  public String ReadString() {
    try {
      boolean end = false;
      int eventType = this.reader.getEventType();
      while (eventType != 4 && 
        eventType != 5 && 
        eventType != 12 && 
        eventType != 9 && 
        eventType != 6 && 
        eventType != 11) {
        if (eventType == 2) {
          end = true;
          break;
        } 
        eventType = this.reader.next();
      } 
      if (end)
        return ""; 
      String str = this.reader.getText();
      Read();
      return str;
    } catch (Exception ex) {
      System.out.println("Error @ ReadString: " + ex.getMessage());
      return null;
    } 
  }
  
  public void MoveToContent() {
    try {
      int eventType = this.reader.nextTag();
      if (eventType == 1 || 
        eventType == 2 || 
        eventType == 9)
        this.Name = this.reader.getLocalName(); 
    } catch (Exception ex) {
      System.out.println("Error @ MoveToContent: " + ex.getMessage() + " (Name=" + this.Name + ")");
    } 
  }
  
  public void Read() {
    try {
      int eventType = this.reader.next();
      while ((eventType == 4 && this.reader.isWhiteSpace()) || (
        eventType == 12 && this.reader.isWhiteSpace()) || 
        eventType == 6 || 
        eventType == 3 || 
        eventType == 5)
        eventType = this.reader.next(); 
      if (eventType == 1 || 
        eventType == 2 || 
        eventType == 9)
        this.Name = this.reader.getLocalName(); 
    } catch (Exception ex) {
      System.out.println("Exception @ Read: " + ex.getMessage());
    } 
  }
  
  public void Close() {
    try {
      this.reader.close();
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    } 
  }
}
