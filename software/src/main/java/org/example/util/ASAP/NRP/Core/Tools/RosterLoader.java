package org.example.util.ASAP.NRP.Core.Tools;

import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.Parsers.ParsedRoster;
import org.example.util.ASAP.NRP.Core.Parsers.ParsedShiftAssignment;
import org.example.util.ASAP.NRP.Core.Parsers.RosterParser;
import org.example.util.ASAP.NRP.Core.Parsers.SchedulingPeriodParser;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftType;
import java.io.InputStream;

public class RosterLoader {
  public boolean VERBOSE = true;
  
  public boolean CheckParsedRoster(ParsedRoster parsedRoster, SchedulingPeriod schedulingPeriod) {
    boolean error = false;
    if (schedulingPeriod == null) {
      System.out.println("Error: Null SchedulingPeriod.");
      return false;
    } 
    for (ParsedShiftAssignment parsedShiftAssignment : parsedRoster.ParsedShiftAssignments) {
      EmployeeDescription employee = schedulingPeriod.GetEmployeeDescription(parsedShiftAssignment.EmployeeID);
      if (employee == null) {
        System.out.println("Error: Unable to find EmployeeID: " + parsedShiftAssignment.EmployeeID + " in SchedulingPeriod.");
        error = true;
      } 
      ShiftType shiftType = schedulingPeriod.GetShiftType(parsedShiftAssignment.ShiftTypeID);
      if (shiftType == null) {
        System.out.println("Error: Unable to find ShiftID: " + parsedShiftAssignment.ShiftTypeID + " in SchedulingPeriod.");
        error = true;
      } 
    } 
    return !error;
  }
  
  public Roster CreateEmptyRoster(String schedulingPeriodFile) {
    Roster roster = null;
    try {
      SchedulingPeriodParser spp = new SchedulingPeriodParser();
      spp.VERBOSE = this.VERBOSE;
      spp.ValidateFirst = true;
      SchedulingPeriod schedulingPeriod = spp.Parse(schedulingPeriodFile);
      if (schedulingPeriod == null) {
        System.out.println("Error : Problem parsing : " + schedulingPeriodFile);
        System.out.println("Unable to create roster.");
        return null;
      } 
      roster = new Roster(schedulingPeriod);
    } catch (Exception exception) {}
    return roster;
  }
  
  public Roster CreateEmptyRoster(InputStream is) {
    Roster roster = null;
    try {
      SchedulingPeriodParser spp = new SchedulingPeriodParser();
      spp.VERBOSE = this.VERBOSE;
      spp.ValidateFirst = true;
      SchedulingPeriod schedulingPeriod = spp.Parse(is);
      if (schedulingPeriod == null) {
        System.out.println("Error : Problem parsing ");
        System.out.println("Unable to create roster.");
        return null;
      } 
      roster = new Roster(schedulingPeriod);
    } catch (Exception exception) {}
    return roster;
  }
  
  public ParsedRoster LoadRoster(InputStream is, String rosterFile) {
    ParsedRoster parsedRoster = null;
    try {
      SchedulingPeriodParser spp = new SchedulingPeriodParser();
      spp.VERBOSE = this.VERBOSE;
      spp.ValidateFirst = true;
      SchedulingPeriod schedulingPeriod = spp.Parse(is);
      if (schedulingPeriod == null) {
        System.out.println("Unable to load roster.");
        return null;
      } 
      RosterParser sp = new RosterParser();
      sp.VERBOSE = this.VERBOSE;
      sp.ValidateFirst = true;
      parsedRoster = sp.Parse(rosterFile);
      if (parsedRoster == null) {
        System.out.println("Error: Problem parsing : " + rosterFile);
        System.out.println("Unable to load roster");
        return null;
      } 
      parsedRoster.CreateRoster(schedulingPeriod);
    } catch (Exception exception) {}
    return parsedRoster;
  }
  
  public ParsedRoster LoadRoster(String schedulingPeriodFile, String rosterFile) {
    ParsedRoster parsedRoster = null;
    try {
      SchedulingPeriodParser spp = new SchedulingPeriodParser();
      spp.VERBOSE = this.VERBOSE;
      spp.ValidateFirst = true;
      SchedulingPeriod schedulingPeriod = spp.Parse(schedulingPeriodFile);
      if (schedulingPeriod == null) {
        System.out.println("Error: Problem parsing : " + schedulingPeriodFile);
        System.out.println("Unable to load roster.");
        return null;
      } 
      RosterParser sp = new RosterParser();
      sp.VERBOSE = this.VERBOSE;
      sp.ValidateFirst = true;
      parsedRoster = sp.Parse(rosterFile);
      if (parsedRoster == null) {
        System.out.println("Error: Problem parsing : " + rosterFile);
        System.out.println("Unable to load roster");
        return null;
      } 
      parsedRoster.CreateRoster(schedulingPeriod);
    } catch (Exception exception) {}
    return parsedRoster;
  }
}
