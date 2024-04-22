package org.example.util.ASAP.NRP.Core.Constraints.Montreal;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftType;

public class MinConsecutiveShiftTypes implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinConsecutiveShiftTypes(int Weight) {
    this.Title = "Min consecutive shift types";
    this.LongTitle = "Minimum number of consecutive shifts of a particular type";
    this.Weight = 0;
    this.ID = "Montreal.MinConsecutiveShiftTypes";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    for (int i = 0; i < employee.Contract.MinConsecutiveShiftTypes.length; i++)
      employee.Contract.MinConsecutiveShiftTypes[i] = -1; 
    employee.Contract.MinConsecutiveShiftTypesAL.clear();
    employee.Contract.MinConsecutiveShiftTypesUsed = false;
    employee.Contract.MinConsecutiveShiftTypesIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "";
  }
  
  public String GetDescription(Employee employee) {
    String info = "";
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    boolean first = true;
    for (int x = 0; x < schedulingPeriod.ShiftTypesCount; x++) {
      ShiftType shiftType = schedulingPeriod.GetShiftType(x);
      String label = shiftType.Label;
      int i = shiftType.Index;
      int min = employee.EmployeeDescription.Contract.MinConsecutiveShiftTypes[i];
      if (min > 1) {
        if (!first)
          info = String.valueOf(info) + "<br/>"; 
        first = false;
        info = String.valueOf(info) + "Requests min " + min + " consecutive '" + label + "' shifts";
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
    int shiftTypeCount = schedulingPeriod.ShiftTypesCount;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    for (int shiftIndex = 0; shiftIndex < shiftTypeCount; shiftIndex++) {
      int min = employee.EmployeeDescription.Contract.MinConsecutiveShiftTypes[shiftIndex];
      if (min > 1) {
        int consDays = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveShifts[shiftIndex];
        for (int i = 0; i < numDaysInPeriod; i++) {
          if (employee.ShiftAssignments[i * shiftTypeCount + shiftIndex]) {
            consDays++;
          } else if (consDays != 0 && consDays < min) {
            int diff = min - consDays;
            int pen = this.Weight * diff;
            penalty += pen;
            int start = i - consDays - diff;
            for (int k = start; k < i + diff && k < numDaysInPeriod; k++) {
              if (k >= 0) {
                employee.ConstraintViolationPenalties[k] = employee.ConstraintViolationPenalties[k] + pen;
                if (SoftConstraints.UpdateViolationDescriptions) {
                  String label = (schedulingPeriod.GetShiftType(shiftIndex)).Label;
                  employee.ViolationDescriptions[k] = String.valueOf(employee.ViolationDescriptions[k]) + "Min " + min + " consecutive '" + label + "' shifts required." + System.getProperty("line.separator");
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
