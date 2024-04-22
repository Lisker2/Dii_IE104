package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class IdenticalShiftTypesDuringWeekend implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public IdenticalShiftTypesDuringWeekend(int Weight) {
    this.Title = "Identical shift types during weekend";
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
    return "";
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
    int saturdayIndex = schedulingPeriod.FirstSaturday;
    int shiftTypeCount = schedulingPeriod.ShiftTypesCount;
    if (employee.EmployeeDescription.SchedulingHistory.Exists && 
      schedulingPeriod.FirstSunday == 0)
      for (int i = 0; i < shiftTypeCount; i++) {
        if (employee.EmployeeDescription.SchedulingHistory.LastDayAssignments[i] != employee.ShiftAssignments[i] && 
          employee.EmployeeDescription.SchedulingHistory.LastDayType != 2 && 
          employee.DayType[0] != 2) {
          penalty += this.Weight;
          employee.ConstraintViolationPenalties[0] = employee.ConstraintViolationPenalties[0] + this.Weight;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[0] = String.valueOf(employee.ViolationDescriptions[0]) + "Identical shift types required on both days of the weekend. " + System.getProperty("line.separator"); 
        } 
      }  
    if (saturdayIndex < 0)
      return penalty; 
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    while (saturdayIndex < numDaysInPeriod) {
      if (saturdayIndex + 1 < numDaysInPeriod)
        for (int i = 0; i < shiftTypeCount; i++) {
          int j = saturdayIndex * shiftTypeCount;
          if (employee.ShiftAssignments[j + i] != employee.ShiftAssignments[j + i + shiftTypeCount] && 
            employee.DayType[saturdayIndex] != 2 && 
            employee.DayType[saturdayIndex + 1] != 2) {
            penalty += this.Weight;
            employee.ConstraintViolationPenalties[saturdayIndex] = employee.ConstraintViolationPenalties[saturdayIndex] + this.Weight;
            employee.ConstraintViolationPenalties[saturdayIndex + 1] = employee.ConstraintViolationPenalties[saturdayIndex + 1] + this.Weight;
            if (SoftConstraints.UpdateViolationDescriptions) {
              employee.ViolationDescriptions[saturdayIndex] = String.valueOf(employee.ViolationDescriptions[saturdayIndex]) + "Identical shift types required on both days of the weekend. " + System.getProperty("line.separator");
              employee.ViolationDescriptions[saturdayIndex + 1] = String.valueOf(employee.ViolationDescriptions[saturdayIndex + 1]) + "Identical shift types required on both days of the weekend. " + System.getProperty("line.separator");
            } 
          } 
        }  
      saturdayIndex += 7;
    } 
    return penalty;
  }
}
