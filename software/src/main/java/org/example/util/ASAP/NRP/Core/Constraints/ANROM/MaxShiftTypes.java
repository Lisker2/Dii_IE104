package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftType;
import org.example.util.ASAP.NRP.Core.ShiftTypeAndValue;

public class MaxShiftTypes implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxShiftTypes(int Weight) {
    this.Title = "Max shift types";
    this.LongTitle = "Maximum number of shifts of a particular type";
    this.Weight = 0;
    this.ID = "ANROM.MaxShiftTypes";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    for (int i = 0; i < employee.Contract.MaxShiftTypes.length; i++)
      employee.Contract.MaxShiftTypes[i] = -1; 
    employee.Contract.MaxShiftTypesAL.clear();
    employee.Contract.MaxShiftTypesUsed = false;
    employee.Contract.MaxShiftTypesIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    String str = "<MaxShiftTypes Weight=\"" + this.Weight + "\">" + System.getProperty("line.separator");
    for (ShiftTypeAndValue stv : employee.Contract.MaxShiftTypesAL) {
      if (stv.Value >= 0)
        str = String.valueOf(str) + " <MaxShiftType><ShiftType>" + stv.ShiftType.ID + "</ShiftType><Value>" + stv.Value + "</Value></MaxShiftType>" + System.getProperty("line.separator"); 
    } 
    str = String.valueOf(str) + "</MaxShiftTypes>" + System.getProperty("line.separator");
    return str;
  }
  
  public String GetDescription(Employee employee) {
    String info = "";
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    for (int x = 0; x < schedulingPeriod.ShiftTypesCount; x++) {
      ShiftType shiftType = schedulingPeriod.GetShiftType(x);
      String label = shiftType.Label;
      int i = shiftType.Index;
      if (employee.EmployeeDescription.Contract.MaxShiftTypes[i] < 0) {
        info = String.valueOf(info) + "No limit on '" + label + "' shifts, receives " + employee.ShiftTypeCount[i];
      } else {
        info = String.valueOf(info) + "Requests max " + employee.EmployeeDescription.Contract.MaxShiftTypes[i] + " '" + label + "' shifts, receives " + employee.ShiftTypeCount[i];
      } 
      if (x + 1 < schedulingPeriod.ShiftTypesCount)
        info = String.valueOf(info) + "<br/>"; 
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
      int max = employee.EmployeeDescription.Contract.MaxShiftTypes[i];
      int count = employee.ShiftTypeCount[i];
      if (max >= 0 && count > max) {
        int pen = this.Weight * (count - max);
        penalty += pen;
        for (int day = 0; day < schedulingPeriod.NumDaysInPeriod; day++) {
          int index = shiftCount * day + i;
          if (employee.ShiftAssignments[index]) {
            employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Requests max " + max + " '" + (schedulingPeriod.GetShiftType(i)).Label + "' shifts. " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
