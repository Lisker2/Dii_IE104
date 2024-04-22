package org.example.util.ASAP.NRP.Core.Constraints.Montreal;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftType;

public class MinShiftTypeRatios implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinShiftTypeRatios(int weight) {
    this.Title = "Min shift type ratios";
    this.LongTitle = "";
    this.Weight = 0;
    this.ID = "";
    this.Weight = weight;
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
    String info = "Min shift type ratios";
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int totShifts = employee.ShiftsCount;
    for (int x = 0; x < schedulingPeriod.ShiftTypesCount; x++) {
      ShiftType shiftType = schedulingPeriod.GetShiftType(x);
      String ID = shiftType.ID;
      int i = shiftType.Index;
      if (employee.EmployeeDescription.Contract.MinShiftTypeRatios[i] >= 0) {
        int ratio = 0;
        if (totShifts != 0)
          ratio = (int)(employee.ShiftTypeCount[i] / totShifts * 100.0F); 
        info = String.valueOf(info) + "<br/>Requests min " + employee.EmployeeDescription.Contract.MinShiftTypeRatios[i] + "% '" + ID + "' shifts, receives " + ratio + "%";
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
      int min = employee.EmployeeDescription.Contract.MinShiftTypeRatios[i];
      int ratio = 0;
      if (totShifts != 0)
        ratio = (int)(employee.ShiftTypeCount[i] / totShifts * 100.0F); 
      if (min >= 0 && ratio < min) {
        int pen = this.Weight * (min - ratio);
        penalty += pen;
      } 
    } 
    return penalty;
  }
}
