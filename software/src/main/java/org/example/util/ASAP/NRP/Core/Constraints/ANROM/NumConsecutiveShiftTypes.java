package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class NumConsecutiveShiftTypes implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public NumConsecutiveShiftTypes(int Weight) {
    this.Title = "Number of consecutive shift types";
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
    String info = "Valid lengths of sequences of consecutive shift types:";
    for (int shiftIndex = 0; shiftIndex < employee.Roster.SchedulingPeriod.ShiftTypesCount; shiftIndex++) {
      String ShiftTypeID = (employee.Roster.SchedulingPeriod.GetShiftType(shiftIndex)).ID;
      info = String.valueOf(info) + "<br/>" + ShiftTypeID + " : ";
      for (int i = 0; i < (employee.EmployeeDescription.Contract.ValidNumConsecutiveShiftTypes[shiftIndex]).length; i++) {
        int j = i + 1;
        if (employee.EmployeeDescription.Contract.ValidNumConsecutiveShiftTypes[shiftIndex][i])
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
    int shiftTypeCount = schedulingPeriod.ShiftTypesCount;
    int previousConsecutiveDaysOff = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveHolidayDaysOff;
    for (int shiftIndex = 0; shiftIndex < schedulingPeriod.ShiftTypesCount; shiftIndex++) {
      int consDays = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveShifts[shiftIndex];
      int consDaysOff = previousConsecutiveDaysOff;
      int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
      for (int i = 0; i < numDaysInPeriod; i++) {
        int index = shiftTypeCount * i + shiftIndex;
        if (employee.ShiftAssignments[index]) {
          consDays++;
        } else if (employee.DayType[i] == 2) {
          consDaysOff++;
        } else {
          boolean permitted = false;
          for (int j = consDays; j <= consDays + consDaysOff && 
            j <= (employee.EmployeeDescription.Contract.ValidNumConsecutiveShiftTypes[shiftIndex]).length && 
            j > 0; j++) {
            if (employee.EmployeeDescription.Contract.ValidNumConsecutiveShiftTypes[shiftIndex][j - 1]) {
              permitted = true;
              break;
            } 
          } 
          if (!permitted && consDays != 0) {
            penalty += this.Weight;
            for (int k = i - 1; k >= 0 && k >= i - consDays + consDaysOff; k--) {
              employee.ConstraintViolationPenalties[k] = employee.ConstraintViolationPenalties[k] + this.Weight;
              if (SoftConstraints.UpdateViolationDescriptions) {
                String ID = (schedulingPeriod.GetShiftType(shiftIndex)).ID;
                employee.ViolationDescriptions[k] = String.valueOf(employee.ViolationDescriptions[k]) + "Invalid length of consecutive '" + ID + "' shifts. " + System.getProperty("line.separator");
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
