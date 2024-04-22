package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxConsecutiveWorkingWeekends implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxConsecutiveWorkingWeekends(int weight) {
    this.Title = "Max consecutive working weekends";
    this.LongTitle = "Maximum number of consecutive working weekends";
    this.Weight = 0;
    this.ID = "ANROM.MaxConsecutiveWorkingWeekends";
    this.Weight = weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MaxConsecutiveWorkingWeekendsIsOn = false;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MaxConsecutiveWorkingWeekends weight=\"" + this.Weight + "\">" + 
      employee.Contract.MaxConsecutiveWorkingWeekends + 
      "</MaxConsecutiveWorkingWeekends>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max " + employee.EmployeeDescription.Contract.MaxConsecutiveWorkingWeekends + 
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
    int maxConsecutiveWorkingWeekends = employee.EmployeeDescription.Contract.MaxConsecutiveWorkingWeekends;
    int consecutiveWorkingWeekends = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveWorkingWeekends;
    int saturdayIndex = schedulingPeriod.FirstSaturday;
    if (saturdayIndex == 6) {
      if (consecutiveWorkingWeekends == 0 && employee.DayType[0] == 1)
        consecutiveWorkingWeekends = 1; 
    } else if (saturdayIndex < 0) {
      if (consecutiveWorkingWeekends > maxConsecutiveWorkingWeekends)
        penalty += this.Weight * (consecutiveWorkingWeekends - maxConsecutiveWorkingWeekends); 
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
      if (!workingWeekend || saturdayIndex + 7 >= numDaysInPeriod) {
        if (consecutiveWorkingWeekends > maxConsecutiveWorkingWeekends) {
          int pen = this.Weight * (consecutiveWorkingWeekends - maxConsecutiveWorkingWeekends);
          penalty += pen;
          int lastSat = workingWeekend ? saturdayIndex : (saturdayIndex - 7);
          for (int i = consecutiveWorkingWeekends; i > 0; i--) {
            if (lastSat >= 0 && employee.DayType[lastSat] == 1) {
              employee.ConstraintViolationPenalties[lastSat] = employee.ConstraintViolationPenalties[lastSat] + pen;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[lastSat] = String.valueOf(employee.ViolationDescriptions[lastSat]) + "Too many consecutive working weekends. " + System.getProperty("line.separator"); 
            } 
            if (lastSat + 1 >= 0 && lastSat + 1 < numDaysInPeriod && employee.DayType[lastSat + 1] == 1) {
              employee.ConstraintViolationPenalties[lastSat + 1] = employee.ConstraintViolationPenalties[lastSat + 1] + pen;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[lastSat + 1] = String.valueOf(employee.ViolationDescriptions[lastSat + 1]) + "Too many consecutive working weekends. " + System.getProperty("line.separator"); 
            } 
            lastSat -= 7;
            if (lastSat + 1 < 0)
              break; 
          } 
        } 
        consecutiveWorkingWeekends = 0;
      } 
      saturdayIndex += 7;
    } 
    return penalty;
  }
}
