package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftGroup;

public class NumConsecutiveShiftGroups implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public NumConsecutiveShiftGroups(int Weight) {
    this.Title = "Number of consecutive shifts from a group";
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
    String info = "Valid lengths of sequences of consecutive shifts from groups:";
    for (int index = 0; index < employee.Roster.SchedulingPeriod.ShiftGroupsCount; index++) {
      ShiftGroup grp = employee.Roster.SchedulingPeriod.GetShiftGroup(index);
      info = String.valueOf(info) + "<br/>" + grp.ID + " (" + grp.Label + ") : ";
      for (int i = 0; i < (employee.EmployeeDescription.Contract.ValidNumConsecutiveShiftGroups[index]).length; i++) {
        int j = i + 1;
        if (employee.EmployeeDescription.Contract.ValidNumConsecutiveShiftGroups[index][i])
          info = String.valueOf(info) + j + ", "; 
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
    int previousConsecutiveDaysOff = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveHolidayDaysOff;
    for (int index = 0; index < schedulingPeriod.ShiftGroupsCount; index++) {
      int consDays = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveShiftGroups[index];
      int consDaysOff = previousConsecutiveDaysOff;
      int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
      for (int i = 0; i < numDaysInPeriod; i++) {
        if (employee.ShiftGroupPerDayCount[i][index] > 0) {
          consDays++;
        } else if (employee.DayType[i] == 2) {
          consDaysOff++;
        } else {
          boolean permitted = false;
          for (int j = consDays; j <= consDays + consDaysOff && 
            j <= (employee.EmployeeDescription.Contract.ValidNumConsecutiveShiftGroups[index]).length && 
            j > 0; j++) {
            if (employee.EmployeeDescription.Contract.ValidNumConsecutiveShiftGroups[index][j - 1]) {
              permitted = true;
              break;
            } 
          } 
          if (!permitted && consDays != 0) {
            penalty += this.Weight;
            for (int k = i - 1; k >= 0 && k >= i - consDays + consDaysOff; k--) {
              employee.ConstraintViolationPenalties[k] = employee.ConstraintViolationPenalties[k] + this.Weight;
              if (SoftConstraints.UpdateViolationDescriptions) {
                ShiftGroup grp = schedulingPeriod.GetShiftGroup(index);
                employee.ViolationDescriptions[k] = String.valueOf(employee.ViolationDescriptions[k]) + "Invalid length of consecutive shifts from group '" + grp.ID + "' (" + grp.Label + ")." + System.getProperty("line.separator");
              } 
            } 
          } 
          consDays = 0;
          consDaysOff = 0;
        } 
      } 
    } 
    return penalty;
  }
}
