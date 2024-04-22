package org.example.util.ASAP.NRP.Core.Constraints.Montreal;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftType;

public class MaxConsecutiveShiftTypes implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxConsecutiveShiftTypes(int Weight) {
    this.Title = "Max consecutive shift types";
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
    String info = "Max consecutive shift types";
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    for (int x = 0; x < schedulingPeriod.ShiftTypesCount; x++) {
      ShiftType shiftType = schedulingPeriod.GetShiftType(x);
      String ID = shiftType.ID;
      int i = shiftType.Index;
      int max = employee.EmployeeDescription.Contract.MaxConsecutiveShiftTypes[i];
      if (max > 0)
        info = String.valueOf(info) + "<br/>Requests max " + max + " consecutive '" + ID + "' shifts"; 
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
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    for (int shiftIndex = 0; shiftIndex < shiftTypeCount; shiftIndex++) {
      int max = employee.EmployeeDescription.Contract.MaxConsecutiveShiftTypes[shiftIndex];
      if (max >= 1) {
        int consDays = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveShifts[shiftIndex];
        for (int i = 0; i < numDaysInPeriod; i++) {
          boolean shiftWorked = employee.ShiftAssignments[i * shiftTypeCount + shiftIndex];
          if (shiftWorked)
            consDays++; 
          if (!shiftWorked || i + 1 == numDaysInPeriod) {
            if (consDays > max) {
              int pen = this.Weight * (consDays - max);
              penalty += pen;
              int end = i - 1;
              if (shiftWorked)
                end = i; 
              for (int j = end; j >= 0 && j > end - consDays; j--) {
                employee.ConstraintViolationPenalties[j] = employee.ConstraintViolationPenalties[j] + pen;
                if (SoftConstraints.UpdateViolationDescriptions) {
                  String label = (schedulingPeriod.GetShiftType(shiftIndex)).Label;
                  employee.ViolationDescriptions[j] = String.valueOf(employee.ViolationDescriptions[j]) + "Max " + max + " consecutive '" + label + "' shifts required." + System.getProperty("line.separator");
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
