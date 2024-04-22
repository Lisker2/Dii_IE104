package org.example.util.ASAP.NRP.Core.Constraints.Montreal;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MinConsecutiveWorkingWeekends implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinConsecutiveWorkingWeekends(int weight) {
    this.Title = "Min consecutive working weekends";
    this.LongTitle = "";
    this.Weight = 0;
    this.ID = "";
    this.Weight = weight;
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
    return "Requests min " + employee.EmployeeDescription.Contract.MinConsecutiveWorkingWeekends + 
      " consecutive working weekends.";
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
    int min = employee.EmployeeDescription.Contract.MinConsecutiveWorkingWeekends;
    int consecutiveWorkingWeekends = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveWorkingWeekends;
    int saturdayIndex = schedulingPeriod.FirstSaturday;
    if (saturdayIndex == 6) {
      if (consecutiveWorkingWeekends == 0 && employee.DayType[0] == 1)
        consecutiveWorkingWeekends = 1; 
    } else if (saturdayIndex < 0) {
      if (consecutiveWorkingWeekends < min)
        penalty += this.Weight * (min - consecutiveWorkingWeekends); 
      return penalty;
    } 
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    while (saturdayIndex < numDaysInPeriod) {
      boolean workingWeekend = false;
      if (employee.DayType[saturdayIndex] == 1) {
        consecutiveWorkingWeekends++;
        workingWeekend = true;
      } else if (saturdayIndex + 1 < employee.DayType.length && 
        employee.DayType[saturdayIndex + 1] == 1) {
        consecutiveWorkingWeekends++;
        workingWeekend = true;
      } 
      if (!workingWeekend && consecutiveWorkingWeekends != 0) {
        if (consecutiveWorkingWeekends < min) {
          int extra = min - consecutiveWorkingWeekends;
          int pen = this.Weight * extra;
          penalty += pen;
          int temp = saturdayIndex + (extra - 1) * 7;
          int i;
          for (i = temp; i >= saturdayIndex; i -= 7) {
            if (i + 1 < numDaysInPeriod)
              if (employee.DayType[i] != 1 && employee.DayType[i + 1] != 1) {
                employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + pen;
                if (SoftConstraints.UpdateViolationDescriptions)
                  employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "Too few consecutive working weekends. " + System.getProperty("line.separator"); 
                employee.ConstraintViolationPenalties[i + 1] = employee.ConstraintViolationPenalties[i + 1] + pen;
                if (SoftConstraints.UpdateViolationDescriptions)
                  employee.ViolationDescriptions[i + 1] = String.valueOf(employee.ViolationDescriptions[i + 1]) + "Too few consecutive working weekends. " + System.getProperty("line.separator"); 
              }  
          } 
          for (i = saturdayIndex - 7; i >= 0 && i > saturdayIndex - consecutiveWorkingWeekends + 7; i -= 7) {
            if (i < numDaysInPeriod) {
              employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + pen;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "Too few consecutive working weekends. " + System.getProperty("line.separator"); 
            } 
            if (i + 1 < numDaysInPeriod) {
              employee.ConstraintViolationPenalties[i + 1] = employee.ConstraintViolationPenalties[i + 1] + pen;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[i + 1] = String.valueOf(employee.ViolationDescriptions[i + 1]) + "Too few consecutive working weekends. " + System.getProperty("line.separator"); 
            } 
          } 
          temp = saturdayIndex - consecutiveWorkingWeekends + 7;
          for (i = temp; i >= 0 && i >= saturdayIndex - min * 7; i -= 7) {
            if (employee.DayType[i] != 1 && employee.DayType[i + 1] != 1) {
              employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + pen;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "Too few consecutive working weekends. " + System.getProperty("line.separator"); 
              employee.ConstraintViolationPenalties[i + 1] = employee.ConstraintViolationPenalties[i + 1] + pen;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[i + 1] = String.valueOf(employee.ViolationDescriptions[i + 1]) + "Too few consecutive working weekends. " + System.getProperty("line.separator"); 
            } 
          } 
        } 
        consecutiveWorkingWeekends = 0;
      } 
      saturdayIndex += 7;
    } 
    return penalty;
  }
}
