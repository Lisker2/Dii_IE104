package org.example.util.ASAP.NRP.Core.Parsers;

public class XmlConvert {
  public static boolean ToBoolean(String str) {
    if (str.equalsIgnoreCase("true") || str.equals("1"))
      return true; 
    return false;
  }
}
