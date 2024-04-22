package org.example.util.ASAP.NRP.Core.Constraints.Montreal;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftType;

public class MaxShiftTypeRatios implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxShiftTypeRatios(int Weight) {
    this.Title = "Max shift type ratios";
    this.LongTitle = "";
    this.Weight = 0;
    this.ID = "";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {}
  
  public String ToXml(EmployeeDescription employee) {
    return "";
  }
  
  public String GetDescription(Employee employee) {
    String info = "Max shift type ratios";
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int totShifts = employee.ShiftsCount;
    for (int x = 0; x < schedulingPeriod.ShiftTypesCount; x++) {
      ShiftType shiftType = schedulingPeriod.GetShiftType(x);
      String ID = shiftType.ID;
      int i = shiftType.Index;
      if (employee.EmployeeDescription.Contract.MaxShiftTypeRatios[i] >= 0) {
        int ratio = 0;
        if (totShifts != 0)
          ratio = (int)(employee.ShiftTypeCount[i] / totShifts * 100.0F); 
        info = String.valueOf(info) + "<br/>Requests max " + employee.EmployeeDescription.Contract.MaxShiftTypeRatios[i] + "% '" + ID + "' shifts, receives " + ratio + "%";
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
    int totShifts = employee.ShiftsCount;
    for (int i = 0; i < shiftCount; i++) {
      int max = employee.EmployeeDescription.Contract.MaxShiftTypeRatios[i];
      int ratio = 0;
      if (totShifts != 0)
        ratio = (int)(employee.ShiftTypeCount[i] / totShifts * 100.0F); 
      if (max >= 0 && ratio > max) {
        int pen = this.Weight * (ratio - max);
        penalty += pen;
        for (int day = 0; day < schedulingPeriod.NumDaysInPeriod; day++) {
          int index = shiftCount * day + i;
          if (employee.ShiftAssignments[index]) {
            employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Requests max " + max + "% '" + (schedulingPeriod.GetShiftType(i)).ID + "' shifts, receives " + ratio + "% " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
