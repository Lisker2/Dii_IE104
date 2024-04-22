package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftType;

public class MaxShiftTypesPerWeek implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxShiftTypesPerWeek(int Weight) {
    this.Title = "Max shift types per week";
    this.LongTitle = "";
    this.Weight = 0;
    this.ID = "";
    this.Weight = Weight;
  }
  
  public void Delete(EmployeeDescription employee) {}
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "";
  }
  
  public String GetDescription(Employee employee) {
    String info = "";
    boolean required = false;
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    for (int i = 0; i < schedulingPeriod.NumWeeksInPeriod; i++) {
      for (int x = 0; x < schedulingPeriod.ShiftTypesCount; x++) {
        ShiftType shiftType = schedulingPeriod.GetShiftType(x);
        String ID = shiftType.ID;
        int j = shiftType.Index;
        if (employee.EmployeeDescription.Contract.MaxShiftTypesPerWeek[i][j] < 0) {
          info = (new StringBuilder(String.valueOf(info))).toString();
        } else {
          info = String.valueOf(info) + "<br/>In week " + (i + 1) + " requests max " + employee.EmployeeDescription.Contract.MaxShiftTypesPerWeek[i][j] + " '" + ID + "' shifts, receives " + employee.ShiftTypePerWeekCount[i][j];
          required = true;
        } 
      } 
    } 
    if (required)
      info = "Max shift types per week " + info; 
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
    int shiftTypeCount = schedulingPeriod.ShiftTypesCount;
    for (int i = 0; i < schedulingPeriod.NumWeeksInPeriod; i++) {
      for (int j = 0; j < schedulingPeriod.ShiftTypesCount; j++) {
        int max = employee.EmployeeDescription.Contract.MaxShiftTypesPerWeek[i][j];
        int count = employee.ShiftTypePerWeekCount[i][j];
        if (max >= 0 && count > max) {
          int pen = this.Weight * (count - max);
          penalty += pen;
          for (int k = i * 7; k < (i + 1) * 7 && k < schedulingPeriod.NumDaysInPeriod; k++) {
            if (employee.ShiftAssignments[k * shiftTypeCount + j]) {
              employee.ConstraintViolationPenalties[k] = employee.ConstraintViolationPenalties[k] + pen;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[k] = String.valueOf(employee.ViolationDescriptions[k]) + "Requests max " + max + " '" + (schedulingPeriod.GetShiftType(j)).Label + "' shifts in this week. " + System.getProperty("line.separator"); 
            } 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
