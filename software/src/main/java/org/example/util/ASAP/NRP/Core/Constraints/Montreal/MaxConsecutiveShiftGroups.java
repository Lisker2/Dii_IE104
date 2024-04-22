package org.example.util.ASAP.NRP.Core.Constraints.Montreal;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftGroup;

public class MaxConsecutiveShiftGroups implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxConsecutiveShiftGroups(int Weight) {
    this.Title = "Max consecutive shifts from a group";
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
    String info = "Max consecutive shifts from a group";
    SchedulingPeriod sp = employee.Roster.SchedulingPeriod;
    for (int x = 0; x < sp.ShiftGroupsCount; x++) {
      ShiftGroup sg = sp.GetShiftGroup(x);
      String ID = sg.Label;
      int i = sg.Index;
      int max = employee.EmployeeDescription.Contract.MaxConsecutiveShiftGroups[i];
      if (max > 0)
        info = String.valueOf(info) + "<br/>Requests max " + max + " consecutive shifts from group '" + ID + "'"; 
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
    int shiftTypeCount = schedulingPeriod.ShiftTypesCount;
    int shiftGroupCount = schedulingPeriod.ShiftGroupsCount;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    for (int index = 0; index < shiftGroupCount; index++) {
      int max = employee.EmployeeDescription.Contract.MaxConsecutiveShiftGroups[index];
      if (max >= 1) {
        ShiftGroup group = schedulingPeriod.GetShiftGroup(index);
        int consDays = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveShiftGroups[index];
        for (int day = 0; day < numDaysInPeriod; day++) {
          boolean shiftWorked = false;
          for (int i = 0; i < group.Group.length; i++) {
            if (employee.ShiftAssignments[day * shiftTypeCount + (group.Group[i]).Index]) {
              shiftWorked = true;
              break;
            } 
          } 
          if (shiftWorked)
            consDays++; 
          if (!shiftWorked || day + 1 == numDaysInPeriod) {
            if (consDays > max) {
              int pen = this.Weight * (consDays - max);
              penalty += pen;
              int end = day - 1;
              if (shiftWorked)
                end = day; 
              for (int j = end; j >= 0 && j > end - consDays; j--) {
                employee.ConstraintViolationPenalties[j] = employee.ConstraintViolationPenalties[j] + pen;
                if (SoftConstraints.UpdateViolationDescriptions) {
                  String label = group.Label;
                  employee.ViolationDescriptions[j] = String.valueOf(employee.ViolationDescriptions[j]) + "Max " + max + " consecutive shifts from shift group '" + label + "' required." + System.getProperty("line.separator");
                } 
              } 
            } 
            consDays = 0;
          } 
        } 
      } 
    } 
    return penalty;
  }
}
