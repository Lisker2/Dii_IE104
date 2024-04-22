package org.example.util.ASAP.NRP.Core.Constraints.TEC;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftGroup;

public class MinShiftGroups implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinShiftGroups(int Weight) {
    this.Title = "Min shift groups";
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
    String info = "Min shift roups";
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    for (int x = 0; x < schedulingPeriod.ShiftGroupsCount; x++) {
      ShiftGroup shiftGroup = schedulingPeriod.GetShiftGroup(x);
      String ID = shiftGroup.ID;
      int i = shiftGroup.Index;
      if (employee.EmployeeDescription.Contract.MinShiftGroups[i] <= 0) {
        info = String.valueOf(info) + "<br/>No minimum number of shifts from group '" + ID + "' (" + shiftGroup.Label + "), receives " + employee.ShiftGroupCount[i];
      } else {
        info = String.valueOf(info) + "<br/>Requests min " + employee.EmployeeDescription.Contract.MinShiftGroups[i] + " shifts from group '" + ID + "' (" + shiftGroup.Label + "), receives " + employee.ShiftGroupCount[i];
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
    int shiftCount = schedulingPeriod.ShiftGroupsCount;
    for (int i = 0; i < shiftCount; i++) {
      int min = employee.EmployeeDescription.Contract.MinShiftGroups[i];
      int count = employee.ShiftGroupCount[i];
      if (min > 0 && count < min) {
        int pen = this.Weight * (min - count);
        penalty += pen;
      } 
    } 
    return penalty;
  }
}
