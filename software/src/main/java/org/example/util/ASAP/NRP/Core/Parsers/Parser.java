package org.example.util.ASAP.NRP.Core.Parsers;

public class Parser {
  public boolean VERBOSE = false;
  
  public boolean ValidateFirst = true;
  
  boolean isXMLValid = true;
  
  protected boolean ValidateXML(String filePath) {
    return this.isXMLValid;
  }
}
