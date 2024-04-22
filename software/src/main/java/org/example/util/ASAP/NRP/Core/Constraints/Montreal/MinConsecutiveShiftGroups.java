package org.example.util.ASAP.NRP.Core.Constraints.Montreal;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftGroup;

public class MinConsecutiveShiftGroups implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinConsecutiveShiftGroups(int Weight) {
    this.Title = "Min consecutive shifts from a group";
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
    String info = "Min consecutive shifts from a group";
    SchedulingPeriod sp = employee.Roster.SchedulingPeriod;
    for (int x = 0; x < sp.ShiftGroupsCount; x++) {
      ShiftGroup sg = sp.GetShiftGroup(x);
      String ID = sg.Label;
      int i = sg.Index;
      int min = employee.EmployeeDescription.Contract.MinConsecutiveShiftGroups[i];
      if (min > 1)
        info = String.valueOf(info) + "<br/>Requests min " + min + " consecutive shifts from group '" + ID + "'"; 
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
    int shiftGroupCount = schedulingPeriod.ShiftGroupsCount;
    int shiftTypeCount = schedulingPeriod.ShiftTypesCount;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    for (int index = 0; index < shiftGroupCount; index++) {
      int min = employee.EmployeeDescription.Contract.MinConsecutiveShiftGroups[index];
      if (min > 1) {
        ShiftGroup group = schedulingPeriod.GetShiftGroup(index);
        int consDays = 0;
        for (int day = 0; day < numDaysInPeriod; day++) {
          boolean shiftWorked = false;
          for (int i = 0; i < group.Group.length; i++) {
            if (employee.ShiftAssignments[day * shiftTypeCount + (group.Group[i]).Index]) {
              shiftWorked = true;
              break;
            } 
          } 
          if (shiftWorked) {
            consDays++;
          } else if (consDays != 0 && consDays < min) {
            int diff = min - consDays;
            int pen = this.Weight * diff;
            penalty += pen;
            int start = day - consDays - diff;
            for (int k = start; k < day + diff && k < numDaysInPeriod; k++) {
              if (k >= 0) {
                employee.ConstraintViolationPenalties[k] = employee.ConstraintViolationPenalties[k] + pen;
                if (SoftConstraints.UpdateViolationDescriptions) {
                  String label = group.Label;
                  employee.ViolationDescriptions[k] = String.valueOf(employee.ViolationDescriptions[k]) + "Min " + min + " consecutive shifts from shift group '" + label + "' required." + System.getProperty("line.separator");
                } 
              } 
            } 
            consDays = 0;
          } else {
            consDays = 0;
          } 
        } 
      } 
    } 
    return penalty;
  }
}
