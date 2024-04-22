package org.example.util.ASAP.NRP.Core.Constraints.TEC;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftGroup;

public class MaxShiftGroups implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxShiftGroups(int Weight) {
    this.Title = "Max shift groups";
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
    String info = "Max shift groups";
    SchedulingPeriod sp = employee.Roster.SchedulingPeriod;
    for (int x = 0; x < sp.ShiftGroupsCount; x++) {
      ShiftGroup sg = sp.GetShiftGroup(x);
      String ID = sg.ID;
      int i = sg.Index;
      if (employee.EmployeeDescription.Contract.MaxShiftGroups[i] < 0) {
        info = String.valueOf(info) + "<br/>No limit on shifts from group '" + ID + "' (" + sg.Label + "), receives " + employee.ShiftGroupCount[i];
      } else {
        info = String.valueOf(info) + "<br/>Requests max " + employee.EmployeeDescription.Contract.MaxShiftGroups[i] + " shifts from group '" + ID + "' (" + sg.Label + "), receives " + employee.ShiftGroupCount[i];
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
    SchedulingPeriod sp = employee.Roster.SchedulingPeriod;
    int penalty = 0;
    for (int i = 0; i < sp.ShiftGroupsCount; i++) {
      int max = employee.EmployeeDescription.Contract.MaxShiftGroups[i];
      int count = employee.ShiftGroupCount[i];
      if (max >= 0 && count > max) {
        int pen = this.Weight * (count - max);
        penalty += pen;
        for (int day = 0; day < sp.NumDaysInPeriod; day++) {
          if (employee.ShiftGroupPerDayCount[day][i] > 0) {
            employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Requests max " + max + " shifts from group '" + (sp.GetShiftGroup(i)).ID + "' (" + (sp.GetShiftGroup(i)).Label + "). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
