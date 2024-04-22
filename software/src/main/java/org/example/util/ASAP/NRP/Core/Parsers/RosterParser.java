package org.example.util.ASAP.NRP.Core.Parsers;

import org.example.util.ASAP.NRP.Core.DateTime;
import java.io.FileInputStream;

public class RosterParser extends Parser {
  ParsedRoster parsedRoster;
  
  public ParsedRoster Parse(String filePath) {
    String OrganisationID = null;
    String SchedulingPeriodID = null;
    String Penalty = null;
    String TimeStamp = null;
    String Algorithm = null;
    String CpuTime = null;
    String Evaluations = null;
    String CPU = null;
    String system = null;
    String FoundBy = null;
    String DateFound = null;
    try {
      FileInputStream fis = new FileInputStream(filePath);
      XmlReader reader = new XmlReader(fis);
      reader.MoveToContent();
      reader.ReadStartElement("Schedule");
      if (reader.Name.equalsIgnoreCase("OrganisationID")) {
        reader.ReadStartElement("OrganisationID");
        OrganisationID = reader.ReadString();
        reader.ReadEndElement();
      } 
      if (reader.Name.equalsIgnoreCase("SchedulingPeriodID")) {
        reader.ReadStartElement("SchedulingPeriodID");
        SchedulingPeriodID = reader.ReadString();
        reader.ReadEndElement();
      } 
      if (reader.Name.equalsIgnoreCase("Penalty")) {
        reader.ReadStartElement("Penalty");
        Penalty = reader.ReadString();
        reader.ReadEndElement();
      } 
      if (reader.Name.equalsIgnoreCase("TimeStamp")) {
        reader.ReadStartElement("TimeStamp");
        TimeStamp = reader.ReadString();
        reader.ReadEndElement();
      } 
      if (reader.Name.equalsIgnoreCase("DateFound")) {
        reader.ReadStartElement("DateFound");
        DateFound = reader.ReadString();
        reader.ReadEndElement();
      } 
      if (reader.Name.equalsIgnoreCase("FoundBy")) {
        reader.ReadStartElement("FoundBy");
        FoundBy = reader.ReadString();
        reader.ReadEndElement();
      } 
      if (reader.Name.equalsIgnoreCase("System")) {
        reader.ReadStartElement("System");
        system = reader.ReadString();
        reader.ReadEndElement();
      } 
      if (reader.Name.equalsIgnoreCase("CPU")) {
        reader.ReadStartElement("CPU");
        CPU = reader.ReadString();
        reader.ReadEndElement();
      } 
      if (reader.Name.equalsIgnoreCase("Algorithm")) {
        reader.ReadStartElement("Algorithm");
        Algorithm = reader.ReadString();
        reader.ReadEndElement();
      } 
      if (reader.Name.equalsIgnoreCase("CpuTime")) {
        reader.ReadStartElement("CpuTime");
        CpuTime = reader.ReadString();
        reader.ReadEndElement();
      } 
      if (reader.Name.equalsIgnoreCase("Evaluations")) {
        reader.ReadStartElement("Evaluations");
        Evaluations = reader.ReadString();
        reader.ReadEndElement();
      } 
      this.parsedRoster = new ParsedRoster(SchedulingPeriodID);
      this.parsedRoster.Algorithm = Algorithm;
      this.parsedRoster.OrganisationID = OrganisationID;
      this.parsedRoster.Penalty = Penalty;
      this.parsedRoster.TimeStamp = TimeStamp;
      this.parsedRoster.CPU = CPU;
      this.parsedRoster.system = system;
      this.parsedRoster.DateFound = DateFound;
      this.parsedRoster.FoundBy = FoundBy;
      this.parsedRoster.CpuTime = CpuTime;
      this.parsedRoster.Evaluations = Evaluations;
      while (!reader.Name.equalsIgnoreCase("Schedule")) {
        ParsedShiftAssignment sa = ParseShiftAssignment(reader);
        this.parsedRoster.AddParsedShiftAssignment(sa);
      } 
    } catch (Exception ex) {
      System.out.println("Exception: " + ex.getMessage());
    } 
    return this.parsedRoster;
  }
  
  private ParsedShiftAssignment ParseShiftAssignment(XmlReader reader) {
    reader.ReadStartElement("Assignment");
    reader.ReadStartElement("Date");
    String DateString = reader.ReadString();
    DateTime Date = DateTime.ParseDate(DateString);
    reader.ReadEndElement();
    reader.ReadStartElement("EmployeeID");
    String EmployeeID = reader.ReadString();
    reader.ReadEndElement();
    reader.ReadStartElement("ShiftID");
    String ShiftID = reader.ReadString();
    reader.ReadEndElement();
    reader.ReadEndElement();
    ParsedShiftAssignment sa = new ParsedShiftAssignment(Date, EmployeeID, ShiftID);
    if (this.VERBOSE) {
      System.out.println("----------------------------------------------");
      System.out.println("Date = " + Date);
      System.out.println("EmployeeID = " + EmployeeID);
      System.out.println("ShiftID = " + ShiftID);
    } 
    return sa;
  }
}
