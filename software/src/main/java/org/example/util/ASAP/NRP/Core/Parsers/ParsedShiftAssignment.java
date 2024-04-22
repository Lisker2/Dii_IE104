package org.example.util.ASAP.NRP.Core.Parsers;

import org.example.util.ASAP.NRP.Core.DateTime;

public class ParsedShiftAssignment {
  public String EmployeeID;
  
  public String ShiftTypeID;
  
  public DateTime Date;
  
  public ParsedShiftAssignment(DateTime date, String employeeID, String shiftTypeID) {
    this.EmployeeID = "";
    this.ShiftTypeID = "";
    this.Date = date;
    this.EmployeeID = employeeID;
    this.ShiftTypeID = shiftTypeID;
  }
}
