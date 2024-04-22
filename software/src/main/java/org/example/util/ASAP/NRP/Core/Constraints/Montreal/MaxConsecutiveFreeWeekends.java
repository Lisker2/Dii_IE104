package org.example.util.ASAP.NRP.Core.Constraints.Montreal;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxConsecutiveFreeWeekends implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxConsecutiveFreeWeekends(int Weight) {
    this.Title = "Max consecutive free weekends";
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
    return "Requests max " + employee.EmployeeDescription.Contract.MaxConsecutiveFreeWeekends + 
      " consecutive non-working weekends.";
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
    int max = employee.EmployeeDescription.Contract.MaxConsecutiveFreeWeekends;
    int consecutive = 0;
    int saturdayIndex = schedulingPeriod.FirstSaturday;
    if (saturdayIndex == 6) {
      if (employee.EmployeeDescription.SchedulingHistory.LastDayType == 0 && 
        employee.DayType[0] == 0)
        consecutive = 1; 
    } else if (saturdayIndex < 0) {
      if (consecutive > max)
        penalty += this.Weight * (consecutive - max); 
      return penalty;
    } 
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    while (saturdayIndex < numDaysInPeriod) {
      boolean free = false;
      if (employee.DayType[saturdayIndex] == 0 && 
        saturdayIndex + 1 < employee.DayType.length && 
        employee.DayType[saturdayIndex + 1] == 0) {
        consecutive++;
        free = true;
      } 
      if (!free || saturdayIndex + 7 >= numDaysInPeriod) {
        if (consecutive > max) {
          int pen = this.Weight * (consecutive - max);
          penalty += pen;
          int lastSat = free ? saturdayIndex : (saturdayIndex - 7);
          for (int i = consecutive; i > 0; i--) {
            if (lastSat >= 0 && employee.DayType[lastSat] == 0) {
              employee.ConstraintViolationPenalties[lastSat] = employee.ConstraintViolationPenalties[lastSat] + pen;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[lastSat] = String.valueOf(employee.ViolationDescriptions[lastSat]) + "Too many consecutive free weekends. " + System.getProperty("line.separator"); 
            } 
            if (lastSat + 1 >= 0 && lastSat + 1 < numDaysInPeriod && employee.DayType[lastSat + 1] == 0) {
              employee.ConstraintViolationPenalties[lastSat + 1] = employee.ConstraintViolationPenalties[lastSat + 1] + pen;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[lastSat + 1] = String.valueOf(employee.ViolationDescriptions[lastSat + 1]) + "Too many consecutive free weekends. " + System.getProperty("line.separator"); 
            } 
            lastSat -= 7;
            if (lastSat + 1 < 0)
              break; 
          } 
        } 
        consecutive = 0;
      } 
      saturdayIndex += 7;
    } 
    return penalty;
  }
}
