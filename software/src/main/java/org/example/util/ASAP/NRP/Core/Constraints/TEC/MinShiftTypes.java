package org.example.util.ASAP.NRP.Core.Constraints.TEC;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftType;
import org.example.util.ASAP.NRP.Core.ShiftTypeAndValue;

public class MinShiftTypes implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinShiftTypes(int Weight) {
    this.Title = "Min shift types";
    this.LongTitle = "Maximum number of shifts of a particular type";
    this.Weight = 0;
    this.ID = "TEC.MinShiftTypes";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    for (int i = 0; i < employee.Contract.MinShiftTypes.length; i++)
      employee.Contract.MinShiftTypes[i] = -1; 
    employee.Contract.MinShiftTypesAL.clear();
    employee.Contract.MinShiftTypesUsed = false;
    employee.Contract.MinShiftTypesIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    String str = "<MinShiftTypes Weight=\"" + this.Weight + "\">" + System.getProperty("line.separator");
    for (ShiftTypeAndValue stv : employee.Contract.MinShiftTypesAL) {
      if (stv.Value >= 0)
        str = String.valueOf(str) + " <MinShiftType><ShiftType>" + stv.ShiftType.ID + "</ShiftType><Value>" + stv.Value + "</Value></MinShiftType>" + System.getProperty("line.separator"); 
    } 
    str = String.valueOf(str) + "</MinShiftTypes>" + System.getProperty("line.separator");
    return str;
  }
  
  public String GetDescription(Employee employee) {
    String info = "Min shift types";
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    for (int x = 0; x < schedulingPeriod.ShiftTypesCount; x++) {
      ShiftType shiftType = schedulingPeriod.GetShiftType(x);
      String ID = shiftType.Label;
      int i = shiftType.Index;
      if (employee.EmployeeDescription.Contract.MinShiftTypes[i] <= 0) {
        info = String.valueOf(info) + "<br/>No minimum '" + ID + "' shifts, receives " + employee.ShiftTypeCount[i];
      } else {
        info = String.valueOf(info) + "<br/>Requests min " + employee.EmployeeDescription.Contract.MinShiftTypes[i] + " '" + ID + "' shifts, receives " + employee.ShiftTypeCount[i];
      } 
    } 
    return info;
  }
  
  public int Calculate(Employee employee, int startDay, int endDay) {
    return Calculate(employee);
  }
  
  public int Calculate(Employee employee, int maxPenalty, int startDay, int endDay) {
    return Calculate(employee);
  }
  
  public int Calculate(Employee employee, int startDay, int endDay, boolean updateStructure) {
    return Calculate(employee);
  }
  
  public int Calculate(Employee employee) {
    if (this.Weight == 0)
      return 0; 
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int penalty = 0;
    int shiftCount = schedulingPeriod.ShiftTypesCount;
    for (int i = 0; i < shiftCount; i++) {
      int min = employee.EmployeeDescription.Contract.MinShiftTypes[i];
      int count = employee.ShiftTypeCount[i];
      if (min > 0 && count < min) {
        int pen = this.Weight * (min - count);
        penalty += pen;
      } 
    } 
    return penalty;
  }
}
