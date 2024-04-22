package org.example.util.ASAP.NRP.Core.Parsers;

import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.Shift;
import java.util.ArrayList;

public class ParsedRoster {
  public String OrganisationID = null;
  
  public String Penalty = null;
  
  public String TimeStamp = null;
  
  public String Algorithm = null;
  
  public String CpuTime = null;
  
  public String Evaluations = null;
  
  public String CPU = null;
  
  public String system = null;
  
  public String FoundBy = null;
  
  public String DateFound = null;
  
  public String SchedulingPeriodID;
  
  public Roster Roster;
  
  public ArrayList<ParsedShiftAssignment> ParsedShiftAssignments;
  
  public ParsedRoster(String schedulingPeriodID) {
    this.Roster = null;
    this.SchedulingPeriodID = schedulingPeriodID;
    this.ParsedShiftAssignments = new ArrayList<ParsedShiftAssignment>();
  }
  
  public boolean AddParsedShiftAssignment(ParsedShiftAssignment parsedShiftAssignment) {
    this.ParsedShiftAssignments.add(parsedShiftAssignment);
    return true;
  }
  
  public void CreateRoster(SchedulingPeriod schedulingPeriod) {
    this.Roster = new Roster(schedulingPeriod);
    for (ParsedShiftAssignment sa : this.ParsedShiftAssignments) {
      if (!AssignShift(this.Roster, sa))
        System.out.println("Error creating roster from file. Unable to assign a shift... "); 
    } 
    this.Roster.RecalculateAllPenalties();
  }
  
  public static boolean AssignShift(Roster roster, ParsedShiftAssignment sa) {
    Employee employee = roster.GetEmployee(sa.EmployeeID);
    if (employee == null)
      return false; 
    Shift shift = new Shift(roster.SchedulingPeriod.GetShiftType(sa.ShiftTypeID), sa.Date, roster.SchedulingPeriod);
    int violationCount = employee.ViolationsForAssigningShift(shift);
    if (violationCount != -1) {
      roster.AssignShift(employee, shift);
    } else {
      return false;
    } 
    return true;
  }
}
